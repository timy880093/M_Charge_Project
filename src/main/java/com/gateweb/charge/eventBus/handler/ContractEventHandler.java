package com.gateweb.charge.eventBus.handler;

import com.gateweb.charge.contract.component.ContractBillingComponent;
import com.gateweb.charge.contract.component.ContractInitializer;
import com.gateweb.charge.contract.component.ContractValidationComponent;
import com.gateweb.charge.contract.component.RemainingContractComponent;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.notice.component.ContractInitializeNoticeComponent;
import com.gateweb.charge.service.ContractService;
import com.gateweb.charge.service.dataGateway.ContractDataGateway;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.PackageRef;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.PackageRefRepository;
import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ContractEventHandler implements ChargeSystemEventHandler {
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    ContractDataGateway contractDataGateway;
    @Autowired
    ContractService contractService;
    @Autowired
    ContractInitializeNoticeComponent contractInitializeNoticeComponent;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    ContractInitializer contractInitializer;
    @Autowired
    ContractValidationComponent contractValidationComponent;
    @Autowired
    ContractBillingComponent contractBillingComponent;
    @Autowired
    RemainingContractComponent remainingContractComponent;

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    @Override
    public void onMessage(EventBus eventBus, ChargeSystemEvent chargeSystemEvent) {
        switch (chargeSystemEvent.getEventAction()) {
            case CREATE:
                afterContractCreate(
                        chargeSystemEvent.getSourceId()
                        , chargeSystemEvent.getEventId()
                        , chargeSystemEvent.getCallerId()
                );
                break;
            case INITIAL:
                afterContractInitial(
                        chargeSystemEvent.getSourceId()
                        , chargeSystemEvent.getEventId()
                        , chargeSystemEvent.getCallerId()
                );
                break;
            case FULFILL_EFFECTIVE_DATE:
                afterFillEffectiveDate(
                        chargeSystemEvent.getSourceId()
                        , chargeSystemEvent.getEventId()
                        , chargeSystemEvent.getCallerId()
                );
                break;
            case ENABLE:
                break;
        }
    }

    public void afterContractCreate(Long contractId, String previousEventId, Long callerId) {
        Optional<Contract> contractOptional = contractDataGateway.findByContractId(contractId);
        if (contractOptional.isPresent()) {
            if (contractInitializer.canContractBeInitialized(contractOptional.get())) {
                contractInitializer.initialContract(
                        contractOptional.get(), previousEventId, callerId
                );
            } else {
                logger.info("Wait for auto fulfillment contract: {}", contractId);
            }
        }
    }

    boolean billingDirectly(Contract contract) {
        //以張計費的也直接出帳
        boolean isChargeByRemainingCount = remainingContractComponent.isChargeByRemainingCount(
                contract.getPackageId()
        );
        //首次直接出帳(非季繳)
        boolean firstContract = contract.getIsFirstContract();
        return firstContract || isChargeByRemainingCount;
    }

    /**
     * 在季繳的情境中，無法自動出帳的原因是你不知道哪些項目應該出，交由他們手動處理
     *
     * @param contractId
     * @param previousEventId
     * @param callerId
     */
    public void afterContractInitial(Long contractId, String previousEventId, Long callerId) {
        Optional<Contract> contractOptional = contractDataGateway.findByContractId(contractId);
        if (contractOptional.isPresent()) {
            boolean billingDirectly = billingDirectly(contractOptional.get());
            //自動出帳判斷
            if (billingDirectly) {
                contractBillingComponent.billingContract(
                        contractOptional.get()
                        , previousEventId
                        , callerId
                );
            }

            if (contractInitializeNoticeComponent.needToBeNoticeAfterInitialized(contractOptional.get())) {
                contractInitializeNoticeComponent.sendContractEnabledNoticeMail(contractOptional.get(), callerId);
            }

            //事件二，所有項目被扣抵，直接啟用合約
            Set<PackageRef> packageRefSet = new HashSet<>(packageRefRepository.findByFromPackageId(contractOptional.get().getPackageId()));
            Set<BillingItem> billingItemSet = new HashSet<>();
            packageRefSet.stream().forEach(packageRef -> {
                billingItemSet.addAll(billingItemRepository.findByPackageRefId(packageRef.getPackageRefId()));
            });
            Set<BillingItem> memoBillingItemSet = billingItemSet.stream().filter(billingItem -> {
                return billingItem.getIsMemo();
            }).collect(Collectors.toSet());
            if (billingItemSet.size() == memoBillingItemSet.size()) {
                contractService.enableContract(contractId, callerId);
            }
        }
    }

    public void afterFillEffectiveDate(Long contractId, String previousEventId, Long callerId) {
        Optional<Contract> contractOptional = contractDataGateway.findByContractId(contractId);
        if (contractOptional.isPresent()) {
            contractInitializer.initialContract(
                    contractOptional.get(),
                    previousEventId,
                    callerId
            );
        }
    }
}
