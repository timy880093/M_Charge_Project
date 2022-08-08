package com.gateweb.charge.contract.component;

import com.gateweb.charge.contract.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.bean.RemainingRecordModel;
import com.gateweb.charge.contract.bean.request.RemainingContractRenewReq;
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
import com.gateweb.utils.LocalDateTimeUtils;
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
    NegativeRemainingRenewDataCollector negativeRemainingRenewDataCollector;
    @Autowired
    ContractExpireRenewDataCollector contractExpireRenewDataCollector;
    @Autowired
    RemainingRecordModelComponent remainingRecordModelComponent;

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

    /**
     * 一律先進行負項拆分好了，過期拆分無法處理負項問題
     *
     * @param contract
     * @return
     */
    Optional<RemainingContractRenewReq> genRemainingContractRenewReq(Company company, Contract contract) {
        Optional<InvoiceRemaining> negativeRecordOpt
                = invoiceRemainingRepository.findTopInvoiceRemainingByCompanyIdAndContractIdAndRemainingLessThanOrderByInvoiceDate(
                contract.getCompanyId()
                , contract.getContractId()
                , 1
        );
        Optional<String> expirationDate = LocalDateTimeUtils.parseLocalDateTimeToString(
                contract.getExpirationDate(), "yyyyMMdd");
        Optional<InvoiceRemaining> expireRecordOptional = Optional.empty();
        if (expirationDate.isPresent()) {
            expireRecordOptional
                    = invoiceRemainingRepository.findTopInvoiceRemainingByCompanyIdAndContractIdAndInvoiceDateIsGreaterThanOrderByInvoiceDateAsc(
                    contract.getCompanyId()
                    , contract.getContractId()
                    , expirationDate.get()
            );
        }

        Optional<RemainingContractRenewDataCollector> collectorOpt = getCollector(negativeRecordOpt, expireRecordOptional);

        InvoiceRemaining targetInvoiceRemaining = null;
        if (collectorOpt.isPresent()) {
            if (collectorOpt.get() instanceof NegativeRemainingRenewDataCollector
                    && negativeRecordOpt.isPresent()) {
                targetInvoiceRemaining = negativeRecordOpt.get();
            } else if (collectorOpt.get() instanceof ContractExpireRenewDataCollector
                    && expireRecordOptional.isPresent()) {
                targetInvoiceRemaining = expireRecordOptional.get();
            }

            boolean isFirstRecordOfTheContract = remainingRecordModelComponent.isFirstRecordOfTheContract(negativeRecordOpt.get());
            if (isFirstRecordOfTheContract) {
                //負項為該合約的首個項目，需要向下再找一個，因為找不到前一個，所以他就當那個prevRecord
                Optional<InvoiceRemaining> nextRecordOpt = invoiceRemainingRepository.findTopInvoiceRemainingByCompanyIdAndContractIdAndInvoiceDateIsGreaterThanOrderByInvoiceDateAsc(
                        negativeRecordOpt.get().getCompanyId()
                        , negativeRecordOpt.get().getContractId()
                        , negativeRecordOpt.get().getInvoiceDate()
                );
                if (nextRecordOpt.isPresent()) {
                    Optional<RemainingRecordModel> remainingRecordModelOpt = remainingRecordModelComponent.genRemainingRecordModel(
                            negativeRecordOpt.get()
                            , nextRecordOpt.get()
                    );
                    if (remainingRecordModelOpt.isPresent()) {
                        return Optional.of(new RemainingContractRenewReq(
                                company
                                , collectorOpt.get()
                                , contract
                                , remainingRecordModelOpt.get()
                        ));
                    }
                }
            } else {
                Optional<RemainingRecordModel> remainingRecordModelOpt = remainingRecordModelComponent.getRemainingRecordModel(
                        targetInvoiceRemaining
                );
                if (remainingRecordModelOpt.isPresent()) {
                    return Optional.of(new RemainingContractRenewReq(
                            company
                            , collectorOpt.get()
                            , contract
                            , remainingRecordModelOpt.get()
                    ));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<RemainingContractRenewDataCollector> getCollector(Optional<InvoiceRemaining> negativeRecordOpt, Optional<InvoiceRemaining> expireRecordOpt) {
        if (negativeRecordOpt.isPresent() && !expireRecordOpt.isPresent()) {
            return Optional.of(negativeRemainingRenewDataCollector);
        } else if (!negativeRecordOpt.isPresent() && expireRecordOpt.isPresent()) {
            return Optional.of(contractExpireRenewDataCollector);
        } else if (negativeRecordOpt.isPresent() && expireRecordOpt.isPresent()) {
            Integer negativeRecordInvoiceDateInt = Integer.parseInt(negativeRecordOpt.get().getInvoiceDate());
            Integer expireRecordInvoiceDateInt = Integer.parseInt(expireRecordOpt.get().getInvoiceDate());
            if (negativeRecordInvoiceDateInt.compareTo(expireRecordInvoiceDateInt) < 0) {
                return Optional.of(negativeRemainingRenewDataCollector);
            }
            if (expireRecordInvoiceDateInt.compareTo(negativeRecordInvoiceDateInt) < 0) {
                return Optional.of(contractExpireRenewDataCollector);
            }
            if (negativeRecordInvoiceDateInt.compareTo(expireRecordInvoiceDateInt) == 0) {
                return Optional.of(contractExpireRenewDataCollector);
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

