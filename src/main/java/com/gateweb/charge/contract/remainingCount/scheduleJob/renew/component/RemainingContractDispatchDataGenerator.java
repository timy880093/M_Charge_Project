package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component;

import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.RemainingContractDispatchData;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RemainingContractDispatchDataGenerator {
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    NegativeRemainingRenewDataCollectorDispatcher negativeRemainingRenewDataCollectorDispatcher;
    @Autowired
    ContractExpireRenewDataCollectorDispatcher contractExpireRenewDataCollectorDispatcher;

    public Optional<RemainingContractDispatchData> gen(Company company, Contract contract) {
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
        Optional<RemainingContractRenewDataCollectorDispatcher> collectorOpt
                = getCollectorDispatcher(negativeRecordOpt, expireRecordOptional);
        if (collectorOpt.isPresent()) {
            Optional<InvoiceRemaining> targetInvoiceRemainingOpt = Optional.empty();
            if (collectorOpt.get() instanceof NegativeRemainingRenewDataCollectorDispatcher
                    && negativeRecordOpt.isPresent()) {
                targetInvoiceRemainingOpt = Optional.of(negativeRecordOpt.get());
            } else if (collectorOpt.get() instanceof ContractExpireRenewDataCollectorDispatcher
                    && expireRecordOptional.isPresent()) {
                targetInvoiceRemainingOpt = Optional.of(expireRecordOptional.get());
            }
            if (targetInvoiceRemainingOpt.isPresent()) {
                RemainingContractDispatchData remainingContractDispatchData = new RemainingContractDispatchData(
                        company
                        , contract
                        , targetInvoiceRemainingOpt.get()
                        , collectorOpt.get()
                );
                return Optional.of(remainingContractDispatchData);
            }
        }
        return Optional.empty();
    }

    public Optional<RemainingContractRenewDataCollectorDispatcher> getCollectorDispatcher(Optional<InvoiceRemaining> negativeRecordOpt, Optional<InvoiceRemaining> expireRecordOpt) {
        if (negativeRecordOpt.isPresent() && !expireRecordOpt.isPresent()) {
            return Optional.of(negativeRemainingRenewDataCollectorDispatcher);
        } else if (!negativeRecordOpt.isPresent() && expireRecordOpt.isPresent()) {
            return Optional.of(contractExpireRenewDataCollectorDispatcher);
        } else if (negativeRecordOpt.isPresent() && expireRecordOpt.isPresent()) {
            Integer negativeRecordInvoiceDateInt = Integer.parseInt(negativeRecordOpt.get().getInvoiceDate());
            Integer expireRecordInvoiceDateInt = Integer.parseInt(expireRecordOpt.get().getInvoiceDate());
            if (negativeRecordInvoiceDateInt.compareTo(expireRecordInvoiceDateInt) < 0) {
                return Optional.of(negativeRemainingRenewDataCollectorDispatcher);
            }
            if (expireRecordInvoiceDateInt.compareTo(negativeRecordInvoiceDateInt) < 0) {
                return Optional.of(contractExpireRenewDataCollectorDispatcher);
            }
            if (negativeRecordInvoiceDateInt.compareTo(expireRecordInvoiceDateInt) == 0) {
                return Optional.of(contractExpireRenewDataCollectorDispatcher);
            }
        }
        return Optional.empty();
    }
}
