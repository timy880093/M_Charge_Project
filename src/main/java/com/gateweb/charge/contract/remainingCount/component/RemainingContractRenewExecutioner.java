package com.gateweb.charge.contract.remainingCount.component;

import com.gateweb.charge.contract.component.ContractExpireRenewDataCollector;
import com.gateweb.charge.contract.component.ContractRenewComponent;
import com.gateweb.charge.contract.remainingCount.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.remainingCount.bean.RemainingContractDispatchData;
import com.gateweb.charge.contract.remainingCount.bean.RemainingContractRenewReq;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.ChargePackageRepository;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.orm.einv.repository.EinvInvoiceMainRepository;
import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class RemainingContractRenewExecutioner {
    final Logger logger = LogManager.getLogger(getClass());
    @Autowired
    RemainingContractComponent remainingContractComponent;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    ContractRenewComponent contractRenewComponent;
    @Autowired
    ChargePackageRepository chargePackageRepository;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    EinvInvoiceMainRepository einvInvoiceMainRepository;
    @Autowired
    RemainingContractDispatchDataGenerator remainingContractDispatchDataGenerator;
    @Autowired
    NegativeRemainingContractRenewReqGenerator negativeRemainingContractRenewReqGenerator;
    @Autowired
    ExpireRemainingContractRenewReqGenerator expireRemainingContractRenewReqGenerator;

    EventBus chargeSystemEventBus;

    @Autowired
    @Qualifier("chargeSystemEventBus")
    public void setChargeSystemEventBus(EventBus chargeSystemEventBus) {
        this.chargeSystemEventBus = chargeSystemEventBus;
    }

    public void executeRenew(Set<Company> targetCompanySet) {
        targetCompanySet.stream().forEach(company -> {
            try {
                Optional<Contract> contractOptional = getLatestContract(company.getCompanyId().longValue());
                if (!contractOptional.isPresent()) {
                    return;
                }
                Optional<RemainingContractRenewReq> remainingContractRenewReqOpt =
                        genRemainingContractRenewReq(company, contractOptional.get());
                if (remainingContractRenewReqOpt.isPresent()) {
                    Optional<ChargeRemainingCountRenewData> chargeRemainingCountRenewDataOpt
                            = remainingContractRenewReqOpt.get()
                            .getRemainingContractRenewDataCollector().execute(remainingContractRenewReqOpt.get());
                    if (chargeRemainingCountRenewDataOpt.isPresent()) {
                        saveChargeRemainingCountRenewData(chargeRemainingCountRenewDataOpt.get());
                    }
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        });
    }

    public void saveChargeRemainingCountRenewData(ChargeRemainingCountRenewData chargeRemainingCountRenewData) {
        contractRepository.save(chargeRemainingCountRenewData.getOriginalContract());
        contractRepository.save(chargeRemainingCountRenewData.getRenewContract());
        //更新記錄
        invoiceRemainingRepository.save(chargeRemainingCountRenewData.getRemainingRecordModel().getPrevRecord());
        //更新他自己的合約id
        chargeRemainingCountRenewData.getRemainingRecordModel().getTargetRecord()
                .setContractId(chargeRemainingCountRenewData.getRenewContract().getContractId());
        //存入合約拆分項
        invoiceRemainingRepository.save(chargeRemainingCountRenewData.getRemainingRecordModel().getTargetRecord());
        //更新之後的記數項目為新的合約
        chargeRemainingCountRenewData.getUpdateRecordList().stream().forEach(invoiceRemaining -> {
            invoiceRemaining.setContractId(chargeRemainingCountRenewData.getRenewContract().getContractId());
            invoiceRemainingRepository.save(invoiceRemaining);
        });
        ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                EventSource.CONTRACT
                , EventAction.CREATE
                , chargeRemainingCountRenewData.getRenewContract().getContractId()
                , chargeRemainingCountRenewData.getOriginalContract().getCreatorId()
        );
        chargeSystemEventBus.post(chargeSystemEvent);
    }
    
    Optional<RemainingContractRenewReq> genRemainingContractRenewReq(Company company, Contract contract) {
        Optional<RemainingContractDispatchData> remainingContractDispatchDataOptional
                = remainingContractDispatchDataGenerator.gen(company, contract);

        if (remainingContractDispatchDataOptional.isPresent()) {
            if (remainingContractDispatchDataOptional.get().getRemainingContractRenewDataCollector()
                    instanceof NegativeRemainingRenewDataCollector) {
                return negativeRemainingContractRenewReqGenerator.gen(remainingContractDispatchDataOptional.get());
            }
            if (remainingContractDispatchDataOptional.get().getRemainingContractRenewDataCollector()
                    instanceof ContractExpireRenewDataCollector) {
                return expireRemainingContractRenewReqGenerator.gen(remainingContractDispatchDataOptional.get());
            }
        }

        return Optional.empty();
    }

    public Optional<Contract> getLatestContract(Long companyId) {
        Optional<InvoiceRemaining> renewTargetRecordOpt
                = invoiceRemainingRepository.findTopInvoiceRemainingByCompanyIdOrderByInvoiceDateDesc(companyId);
        if (renewTargetRecordOpt.isPresent()) {
            return contractRepository.findById(renewTargetRecordOpt.get().getContractId());
        } else {
            return Optional.empty();
        }
    }

}

