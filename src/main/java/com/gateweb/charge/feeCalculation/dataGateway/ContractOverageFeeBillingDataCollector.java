package com.gateweb.charge.feeCalculation.dataGateway;

import com.gateweb.charge.chargePolicy.ChargePolicyProvider;
import com.gateweb.charge.chargePolicy.bean.ChargePolicy;
import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.enumeration.ChargePolicyType;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.feeCalculation.bean.ContractOverageFeeBillingData;
import com.gateweb.charge.feeCalculation.dataCounter.DataCounter;
import com.gateweb.charge.feeCalculation.dataCounter.DataCounterGateway;
import com.gateweb.charge.service.dataGateway.ContractDataGateway;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ContractOverageFeeBillingDataCollector {

    private ContractRepository contractRepository;
    private BillingItemRepository billingItemRepository;
    private CompanyRepository companyRepository;
    private DataCounterGateway dataCounterGateway;
    private ChargePolicyProvider chargePolicyProvider;
    private InvoiceRemainingRepository invoiceRemainingRepository;
    private ContractDataGateway contractDataGateway;

    public ContractOverageFeeBillingDataCollector(ContractRepository contractRepository, BillingItemRepository billingItemRepository, CompanyRepository companyRepository, DataCounterGateway dataCounterGateway, ChargePolicyProvider chargePolicyProvider, InvoiceRemainingRepository invoiceRemainingRepository, ContractDataGateway contractDataGateway) {
        this.contractRepository = contractRepository;
        this.billingItemRepository = billingItemRepository;
        this.companyRepository = companyRepository;
        this.dataCounterGateway = dataCounterGateway;
        this.chargePolicyProvider = chargePolicyProvider;
        this.invoiceRemainingRepository = invoiceRemainingRepository;
        this.contractDataGateway = contractDataGateway;
    }

    /**
     * 將合約與超額計算的發起脫勾
     * 以20200320~20200319的合約為例，其202003產生的費用應包含202001,202002，若為相同合約則沒關系，若為不同合約則以原合約為主
     * 也因為這個規則，其202001,202002的計算規則應該永遠以原所屬合約為主，因為若為相同合約，兩邊計算規則就會相同
     * 若不同，則以較早的合約為主
     * 但發起超額計算的依據不同，超額計算的發起不能再以合約日期主導，要以該公司於該月份是否擁有合約
     *
     * @return
     */
    public Collection<ContractOverageFeeBillingData> collect(CustomInterval customInterval) {
        Set<Contract> contractSet = contractDataGateway.findByContractStatusAndIntervalOverlaps(
                ContractStatus.E, customInterval
        );
        return collect(customInterval, contractSet);
    }

    public Collection<ContractOverageFeeBillingData> collect(CustomInterval customInterval, Long companyId) {
        Set<Contract> contractSet = contractDataGateway.findByStatusNotDisableAndCompanyIdAndIntervalOverlaps(
                companyId
                , customInterval
        );
        return collect(customInterval, contractSet);
    }

    public Collection<ContractOverageFeeBillingData> collect(CustomInterval customInterval, Collection<Contract> contractCollection) {
        Set<ContractOverageFeeBillingData> billingDataSet = new HashSet<>();
        HashMap<Long, Company> companyHashMap = new HashMap<>();
        MultiValueMap companyContractMap = new MultiValueMap();
        //依公司進行分類
        contractCollection.stream().forEach(contract -> {
            if (companyContractMap.containsKey(contract.getCompanyId())) {
                companyContractMap.put(contract.getCompanyId(), contract);
            } else {
                Optional<Company> companyOptional = companyRepository.findByCompanyId(contract.getCompanyId().intValue());
                if (companyOptional.isPresent()) {
                    companyHashMap.put(contract.getCompanyId(), companyOptional.get());
                }
                companyContractMap.put(contract.getCompanyId(), contract);
            }
        });
        //若包含兩個合約，則應以日期早的為主
        companyContractMap.keySet().stream().forEach(key -> {
            Set companyContractSet = new HashSet(companyContractMap.getCollection(key));
            if (companyContractSet.size() > 1) {
                //先清空再處理
                companyContractMap.getCollection(key).clear();
                companyContractMap.put(key, findMinEffectiveDateContract(companyContractSet));
            }
        });
        //遍歷所有合約
        companyContractMap.values().stream().forEach(contractObj -> {
            Contract contract = (Contract) contractObj;
            Optional<ContractOverageFeeBillingData> billingDataOpt = genOverageFeeBillingData(
                    companyHashMap.get(contract.getCompanyId()).getBusinessNo()
                    , contract
                    , customInterval
            );
            if (billingDataOpt.isPresent()) {
                billingDataSet.add(billingDataOpt.get());
            }
        });
        return billingDataSet;
    }

    public Optional<ContractOverageFeeBillingData> genOverageFeeBillingData(
            String businessNo, Contract contract, CustomInterval customInterval) {
        Optional result = Optional.empty();
        Optional<ChargePolicy> postpaidChargePolicyOptional = chargePolicyProvider.genChargePolicy(
                ChargePolicyType.OVERAGE
                , contract
        );
        if (postpaidChargePolicyOptional.isPresent()) {
            //取得資料計數器
            Optional<DataCounter> dataCounterOptional = dataCounterGateway.getDataCounter(
                    postpaidChargePolicyOptional.get().getChargeBaseType()
            );
            if (dataCounterOptional.isPresent()) {
                ContractOverageFeeBillingData billingData = new ContractOverageFeeBillingData();
                billingData.setCompanyId(contract.getCompanyId());
                billingData.setContractId(contract.getContractId());
                billingData.setChargePolicy(postpaidChargePolicyOptional.get());
                billingData.setCalculateInterval(customInterval);
                billingData.setDataCounter(dataCounterOptional.get());
                billingData.setPackageRefId(postpaidChargePolicyOptional.get().getPackageRef().getPackageRefId());
                billingData.setBusinessNo(businessNo);
                //前一個合約為以張計費的例外處理
                billingData.setPreviousInvoiceRemaining(getPreviousInvoiceRemaining(contract));
                result = Optional.of(billingData);
            }
        }
        return result;
    }

    private Contract findMinEffectiveDateContract(Collection<Contract> contractCollection) {
        Contract resultContract = null;
        for (Contract contract : contractCollection) {
            if (resultContract == null
                    || resultContract.getEffectiveDate().isAfter(contract.getEffectiveDate())) {
                resultContract = contract;
            }
        }
        return resultContract;
    }

    public Optional<InvoiceRemaining> getPreviousInvoiceRemaining(Contract contract) {
        Optional result = Optional.empty();
        Optional<Contract> contractOptional = contractRepository.findTopByCompanyIdAndContractIdBeforeOrderByContractIdDesc(
                contract.getCompanyId()
                , contract.getContractId()
        );
        if (contractOptional.isPresent()) {
            Optional<InvoiceRemaining> invoiceRemainingOptional = invoiceRemainingRepository.findTopInvoiceRemainingByCompanyIdAndContractIdOrderByCreateDateDesc(
                    contract.getCompanyId()
                    , contractOptional.get().getContractId()
            );
            //這個invoiceRemaining並沒有被處理過
            Optional<BillingItem> existRecordOpt = billingItemRepository.findTopByPrevInvoiceRemainingIdIsNotNullAndContractId(
                    contract.getContractId()
            );
            if (invoiceRemainingOptional.isPresent()
                    && !existRecordOpt.isPresent()
                    && invoiceRemainingOptional.get().getRemaining() < 0) {
                result = Optional.of(invoiceRemainingOptional.get());
            }
        }
        return result;
    }

}
