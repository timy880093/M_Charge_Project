package com.gateweb.charge.service.impl;

import com.gateweb.charge.chargePolicy.ChargePolicyProvider;
import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.component.*;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.exception.DeleteBilledBillingItemException;
import com.gateweb.charge.exception.InvalidOperationException;
import com.gateweb.charge.feeCalculation.bean.ContractOverageFeeBillingData;
import com.gateweb.charge.feeCalculation.dataGateway.ContractOverageFeeBillingDataCollector;
import com.gateweb.charge.frontEndIntegration.bean.OutToBillRequest;
import com.gateweb.charge.mapper.ContractMapper;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.service.BillService;
import com.gateweb.charge.service.BillingService;
import com.gateweb.charge.service.ContractService;
import com.gateweb.charge.service.dataGateway.ContractDataGateway;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.PackageRef;
import com.gateweb.orm.charge.entity.view.ContractFetchView;
import com.gateweb.orm.charge.repository.*;
import com.gateweb.utils.ConcurrentUtils;
import com.gateweb.utils.bean.BeanConverterUtils;
import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.gateweb.utils.ConcurrentUtils.pool;

@Service
public class ContractServiceImpl implements ContractService {
    protected final Logger logger = LogManager.getLogger(getClass());
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    @Autowired
    ContractRepository contractRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ContractFetchViewRepository contractFetchViewRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    BillingService billingService;
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    ContractMapper contractMapper;
    @Autowired
    BillService billService;
    @Autowired
    ContractPeriodicFeeCalculator contractPeriodicFeeCalculator;
    @Autowired
    ContractRenewComponent contractRenewComponent;
    @Autowired
    ContractInitializer contractInitializer;
    @Autowired
    ContractDataGateway contractDataGateway;
    @Autowired
    ContractTerminateComponent contractTerminateComponent;
    @Autowired
    ContractValidationComponent contractValidationComponent;
    @Autowired
    ContractEnableComponent contractEnableComponent;
    @Autowired
    ContractOverageFeeBillingDataCollector contractOverageFeeBillingDataCollector;
    @Autowired
    ChargePolicyProvider chargePolicyProvider;

    EventBus chargeSystemEventBus;

    @Autowired
    @Qualifier("chargeSystemEventBus")
    public void setChargeSystemEventBus(EventBus chargeSystemEventBus) {
        this.chargeSystemEventBus = chargeSystemEventBus;
    }

    @Override
    public List<ContractFetchView> getNotExpireContractFetchViewList() {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        List<ContractFetchView> contractViewList = contractFetchViewRepository.findByExpirationDateAfter(timestamp);
        return contractViewList;
    }

    @Override
    public List<ContractFetchView> searchAllContractFetchView() {
        return contractFetchViewRepository.findAll();
    }

    @Override
    public List<ContractFetchView> findDeductibleContractList() {
        List<ContractFetchView> contractFetchViewList = new ArrayList<>();
        List<Contract> contractList = contractRepository.findDeductibleContractList();
        contractList.stream().forEach(contract -> {
            Optional<ContractFetchView> contractFetchViewOptional = contractFetchViewRepository.findById(contract.getContractId());
            if (contractFetchViewOptional.isPresent()) {
                contractFetchViewList.add(contractFetchViewOptional.get());
            }
        });
        return contractFetchViewList;
    }

    public void renewContractByRequest(String calFeeYearMonth, Long callerId) {
        Optional<CustomInterval> renewThresholdOpt = contractRenewComponent.getRenewIntervalByYmStr(calFeeYearMonth);
        if (renewThresholdOpt.isPresent()) {
            //根據期別續約
            contractRenewComponent.renewContractByInterval(renewThresholdOpt.get(), callerId);
        }
    }

    public void renewContractByRequest(String calFeeYearMonth, Long companyId, Long callerId) {
        Optional<CustomInterval> renewThresholdOpt = contractRenewComponent.getRenewIntervalByYmStr(calFeeYearMonth);
        if (renewThresholdOpt.isPresent()) {
            contractRenewComponent.renewContractByInterval(renewThresholdOpt.get(), companyId, callerId);
        }
    }

    /**
     * 這個順序必需保持先算超額再進行續約
     * 因為扣抵需要先扣抵超額，兩者為獨立事件
     *
     * @param outToBillRequest
     * @param callerId
     */
    @Override
    public void calculateContractFeeAndOutToBill(OutToBillRequest outToBillRequest, Long callerId) {
        if (outToBillRequest.getCondition().getCompanyId() != null) {
            calculateContractPeriodicFeeByRequest(
                    outToBillRequest.getCondition().getCalFeeYearMonth()
                    , outToBillRequest.getCondition().getCompanyId()
                    , callerId
            );
            renewContractByRequest(
                    outToBillRequest.getCondition().getCalFeeYearMonth()
                    , outToBillRequest.getCondition().getCompanyId()
                    , callerId
            );
        } else {
            calculateContractPeriodicFeeByRequest(
                    outToBillRequest.getCondition().getCalFeeYearMonth()
                    , callerId
            );
            renewContractByRequest(
                    outToBillRequest.getCondition().getCalFeeYearMonth()
                    , callerId
            );
        }
        billService.transactionOutToBillByAjaxRequest(
                outToBillRequest,
                callerId
        );
    }

    public void calculateContractPeriodicFeeByRequest(String calFeeYearMonth, Long companyId, Long callerId) {
        Set<CustomInterval> overageCalculateIntervalSet = contractPeriodicFeeCalculator.getOverageCalculateIntervalByYmStr(
                calFeeYearMonth
        );
        overageCalculateIntervalSet.stream().forEach(customInterval -> {
            Set<CompletableFuture<Void>> overageCompletableFutureList = new HashSet<>();
            overageCompletableFutureList.add(CompletableFuture.runAsync(() -> {
                Set<ContractOverageFeeBillingData> contractOverageFeeBillingData;
                contractOverageFeeBillingData = new HashSet<>(
                        contractOverageFeeBillingDataCollector.collect(customInterval, companyId)
                );
                contractPeriodicFeeCalculator.executePostPaidPeriodicBilling(
                        contractOverageFeeBillingData
                        , callerId
                );
            }, pool));
            ConcurrentUtils.completableGet(overageCompletableFutureList);
        });
    }

    public void calculateContractPeriodicFeeByRequest(String calFeeYearMonth, Long callerId) {
        Set<CustomInterval> overageCalculateIntervalSet = contractPeriodicFeeCalculator.getOverageCalculateIntervalByYmStr(
                calFeeYearMonth
        );
        Set<CompletableFuture<Void>> overageCompletableFutureList = Collections.synchronizedSet(new HashSet<>());
        overageCalculateIntervalSet.parallelStream().forEach(customInterval -> {
            overageCompletableFutureList.add(CompletableFuture.runAsync(() -> {
                Set<ContractOverageFeeBillingData> contractOverageFeeBillingData = Collections.synchronizedSet(
                        new HashSet<>(contractOverageFeeBillingDataCollector.collect(customInterval))
                );
                contractPeriodicFeeCalculator.executePostPaidPeriodicBilling(
                        contractOverageFeeBillingData
                        , callerId
                );
            }, pool));
        });
        ConcurrentUtils.completableGet(overageCompletableFutureList);
    }

    @Override
    public void enableContract(Long contractId, Long callerId) {
        Optional<Contract> contractOptional = contractRepository.findById(contractId);
        if (contractOptional.isPresent()) {
            enableContract(contractOptional.get(), callerId);
        }
    }

    @Override
    public void enableContract(Contract contract, Long callerId) {
        if (!contract.getStatus().equals(ContractStatus.E)) {
            contractEnableComponent.enableContract(contract, callerId);
        }
    }

    @Override
    public void initialContract(Long contractId, Long callerId) {
        Optional<Contract> contractOptional = contractRepository.findById(contractId);
        if (contractOptional.isPresent()) {
            contractInitializer.initialContract(
                    contractOptional.get()
                    , null
                    , callerId
            );
        }
    }

    @Override
    /**
     * 並非帳單付了才不能刪除，理論上就算帳單沒有付也不能刪除，只要出帳就不能刪，因為帳單金額會不正確
     * billingItem中包含已扣抵項目沒有關系，因為刪除後就是還回原金額而已，不會有影響
     */
    public void cancelInitialContract(Contract contract, Long callerId) throws DeleteBilledBillingItemException {
        if (contract.getStatus().equals(ContractStatus.B)) {
            Set<PackageRef> packageRefSet = new HashSet<>(
                    packageRefRepository.findByFromPackageId(contract.getPackageId())
            );
            Set<BillingItem> billingItemSet = new HashSet<>();
            Set<BillingItem> billedBillingItemSet = new HashSet<>();
            for (PackageRef packageRef : packageRefSet) {
                billingItemSet = new HashSet<>(billingItemRepository.findByPackageRefId(packageRef.getPackageRefId()));
                billedBillingItemSet = billingItemSet.stream().filter(billingItem -> {
                    return billingItem.getBillId() != null;
                }).collect(Collectors.toSet());
            }
            if (billedBillingItemSet.isEmpty()) {
                billingItemSet.stream().forEach(billingItem -> {
                    try {
                        billingService.transactionDeleteBillingItem(billingItem, callerId);
                    } catch (InvalidOperationException e) {
                        e.printStackTrace();
                    }
                });
                contract.setStatus(ContractStatus.C);
                contractRepository.save(contract);
            } else {
                throw new DeleteBilledBillingItemException();
            }
        }
    }

    /**
     * 使用當前月份作為區間
     *
     * @param contract
     * @param callerInfo
     */
    @Deprecated
    @Override
    public void continueContractWithCurrentDateTime(Contract contract, CallerInfo callerInfo) {
        Optional<Contract> continueResult = contractRenewComponent.renewContract(contract, callerInfo.getUserEntity().getUserId().longValue());
        if (continueResult.isPresent()) {
            contractInitializer.initialContract(
                    continueResult.get(),
                    null,
                    callerInfo.getUserEntity().getUserId().longValue()
            );
        }
    }

}
