package com.gateweb.charge.contract.remainingCount.remainingRecordFrame;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.remainingCount.source.RemainingCountAmountProvider;
import com.gateweb.charge.feeCalculation.dataCounter.IasrDataCounterByInvoiceDate;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class RemainingRecordFrameComponent {
    protected final Logger logger = LogManager.getLogger(getClass());
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    IasrDataCounterByInvoiceDate iasrDataCounterByInvoiceDate;
    @Autowired
    RemainingCountAmountProvider remainingCountAmountProvider;

    public Optional<RemainingRecordFrame> getRemainingRecordModel(InvoiceRemaining targetRecord) {
        Optional<InvoiceRemaining> prevRemainingRecordOpt =
                invoiceRemainingRepository.findTopInvoiceRemainingByCompanyIdAndContractIdAndInvoiceDateLessThanOrderByInvoiceDateDesc(
                        targetRecord.getCompanyId()
                        , targetRecord.getContractId()
                        , targetRecord.getInvoiceDate()
                );
        if (prevRemainingRecordOpt.isPresent()) {
            return Optional.of(new RemainingRecordFrame(prevRemainingRecordOpt.get(), targetRecord));
        }
        return Optional.empty();
    }

    public Optional<RemainingRecordFrame> genModelForNonMarginNegativeCase(
            Company company
            , Contract renewContract
            , InvoiceRemaining prevRecord
            , InvoiceRemaining targetRecord) {
        RemainingRecordFrame remainingRecordFrame = new RemainingRecordFrame(prevRecord, targetRecord);
        Optional<CustomInterval> remainingRecordInvoiceDateIntervalOpt
                = RemainingRecordFrameUtils.genRemainingRecordInvoiceDateInterval(remainingRecordFrame);
        if (remainingRecordInvoiceDateIntervalOpt.isPresent()) {
            //取得新合約的張數
            Optional<Integer> remainingOpt = remainingCountAmountProvider.getRemainingCountFromPackageId(renewContract.getPackageId());
            Optional<Integer> usageOpt = iasrDataCounterByInvoiceDate.count(company.getBusinessNo()
                    , remainingRecordInvoiceDateIntervalOpt.get()
            );
            remainingRecordFrame.getTargetRecord().setRemaining(
                    remainingOpt.get() + prevRecord.getRemaining() - usageOpt.get()
            );
            remainingRecordFrame.getTargetRecord().setModifyDate(LocalDateTime.now());
            return Optional.of(remainingRecordFrame);
        }
        return Optional.empty();
    }

    /**
     * 取得新產生的marginRecord的Interval
     *
     * @param prevRecord
     * @param contract
     * @return
     */
    public Optional<CustomInterval> getExpireCaseMarginRecordInterval(InvoiceRemaining prevRecord, Contract contract) {
        Optional<LocalDate> marginRecordStartDateOpt = LocalDateTimeUtils.parseLocalDateFromString(prevRecord.getInvoiceDate(), "yyyyMMdd");
        LocalDateTime marginRecordEndDate = contract.getExpirationDate();
        if (marginRecordStartDateOpt.isPresent()) {
            return Optional.of(new CustomInterval(marginRecordStartDateOpt.get().atStartOfDay(), marginRecordEndDate));
        }
        return Optional.empty();
    }

    public boolean isFirstRecordOfTheContract(InvoiceRemaining invoiceRemaining) {
        Optional<InvoiceRemaining> firstRecordOfContractOpt
                = invoiceRemainingRepository.findTopByCompanyIdAndContractIdOrderByInvoiceDate(
                invoiceRemaining.getCompanyId()
                , invoiceRemaining.getContractId()
        );
        if (firstRecordOfContractOpt.isPresent()
                && firstRecordOfContractOpt.get().getInvoiceRemainingId().equals(invoiceRemaining.getInvoiceRemainingId())) {
            return true;
        } else {
            return false;
        }
    }

}
