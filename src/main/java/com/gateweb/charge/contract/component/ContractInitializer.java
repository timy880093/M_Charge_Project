package com.gateweb.charge.contract.component;

import com.gateweb.charge.chargePolicy.ChargePolicyProvider;
import com.gateweb.charge.contract.remainingCount.component.RemainingContractComponent;
import com.gateweb.charge.contract.remainingCount.component.RemainingContractInitializer;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.charge.feeCalculation.billingItemGenerator.ContractRentalFeeBillingItemGenerator;
import com.gateweb.charge.feeCalculation.dataGateway.ContractRentalFeeBillingDataCollector;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.ChargeRule;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.ChargeRuleRepository;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class ContractInitializer {
    final Logger logger = LogManager.getLogger(getClass());
    @Autowired
    ContractRentalFeeBillingItemGenerator contractRentalFeeBillingItemGenerator;
    @Autowired
    ContractRentalFeeBillingDataCollector contractRentalFeeBillingDataCollector;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    RemainingContractComponent remainingContractComponent;
    @Autowired
    ChargePolicyProvider chargePolicyProvider;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    RemainingContractInitializer remainingContractInitializer;
    @Autowired
    ChargeRuleRepository chargeRuleRepository;

    EventBus chargeSystemEventBus;

    @Autowired
    @Qualifier("chargeSystemEventBus")
    public void setChargeSystemEventBus(EventBus chargeSystemEventBus) {
        this.chargeSystemEventBus = chargeSystemEventBus;
    }

    public void initialContract(
            Contract contract
            , String previousEventId
            , Long callerId) {
        createInitialFee(contract, callerId);
        //只有首個以張計費合約會寫入起始張數
        if (isFirstRemainingContract(contract.getCompanyId(), contract.getContractId())) {
            Optional<InvoiceRemaining> newRecordOpt = remainingContractInitializer.chargeByRemainingCountInitializer(contract);
            if (newRecordOpt.isPresent()) {
                invoiceRemainingRepository.save(newRecordOpt.get());
            } else {
                logger.error("initialize failed contract: " + contract.getContractId());
            }
        }
        changeContractStatus(contract, ContractStatus.E, previousEventId, callerId);
    }

    public void createInitialFee(Contract contract, Long callerId) {
        Set<BillingItem> billingItemSet =
                new HashSet<>(
                        contractRentalFeeBillingItemGenerator.gen(
                                contractRentalFeeBillingDataCollector, contract.getContractId()
                        )
                );
        billingItemSet.stream().forEach(billingItem -> {
            billingItemRepository.save(billingItem);
            //進eventBus
            ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                    EventSource.BILLING_ITEM
                    , EventAction.CREATE
                    , billingItem.getBillingItemId()
                    , callerId
            );
            chargeSystemEventBus.post(chargeSystemEvent);
        });
    }

    public void changeContractStatus(
            Contract contract
            , ContractStatus newStatus
            , String previousEventId
            , Long callerId) {
        //初始化直接啟用
        contract.setStatus(newStatus);
        contractRepository.save(contract);
        ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                EventSource.CONTRACT
                , EventAction.INITIAL
                , contract.getContractId()
                , previousEventId
                , callerId
        );
        chargeSystemEventBus.post(chargeSystemEvent);
    }

    public boolean canContractBeInitialized(Contract contract) {
        return contract.getEffectiveDate() != null
                && contract.getExpirationDate() != null
                && contract.getExpirationDate().isAfter(contract.getEffectiveDate())
                && contract.getStatus().equals(ContractStatus.C);
    }

    public boolean isRemainingContract(Long contractId) {
        Optional<ChargeRule> remainingChargeRuleOpt = chargeRuleRepository.getChargeRuleByContractIdAndRemainingCountIsTrue(contractId);
        return remainingChargeRuleOpt.isPresent();
    }

    public boolean isFirstRemainingContract(Long companyId, Long contractId) {
        if (isRemainingContract(contractId)) {
            List<InvoiceRemaining> invoiceRemainingList = invoiceRemainingRepository.findByCompanyId(companyId);
            return invoiceRemainingList.isEmpty();
        } else {
            return false;
        }
    }
}
