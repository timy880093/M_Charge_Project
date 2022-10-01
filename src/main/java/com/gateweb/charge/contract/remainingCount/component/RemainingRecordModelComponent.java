package com.gateweb.charge.contract.remainingCount.component;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.remainingCount.bean.RemainingRecordModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RemainingRecordModelComponent {
    protected final Logger logger = LogManager.getLogger(getClass());
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    IasrDataCounterByInvoiceDate iasrDataCounterByInvoiceDate;
    @Autowired
    RemainingCountAmountProvider remainingCountAmountProvider;

    public Optional<CustomInterval> genRemainingRecordInterval(String prevInvoiceDate, String currentInvoiceDate) {
        try {
            Optional<LocalDate> calculateStartDateOpt = LocalDateTimeUtils.parseLocalDateFromString(
                    prevInvoiceDate
                    , "yyyyMMdd"
            );
            Optional<LocalDate> calculateEndDateOpt = LocalDateTimeUtils.parseLocalDateFromString(
                    currentInvoiceDate
                    , "yyyyMMdd"
            );
            //實際的startDateTime為+1天，因為不包含起始
            if (calculateStartDateOpt.isPresent() && calculateEndDateOpt.isPresent()) {
                return Optional.of(
                        genRemainingRecordInterval(
                                calculateStartDateOpt.get()
                                , calculateEndDateOpt.get()
                        )
                );
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return Optional.empty();
    }

    private LocalDate genNextLocalDate(LocalDate prevLocalDate) {
        LocalDate nextLocalDate = LocalDateTimeUtils.toTheEndOfTheMonth(prevLocalDate);
        if (nextLocalDate.equals(prevLocalDate)) {
            return genNextLocalDate(prevLocalDate.plusDays(1));
        }
        return nextLocalDate;
    }

    /**
     * 這個很複雜
     *
     * @param prevLocalDate
     * @return
     */
    private List<CustomInterval> genNextLocalDateTimeList(LocalDate prevLocalDate) {
        List<CustomInterval> resultList = new ArrayList<>();
        LocalDate nextLocalDate = genNextLocalDate(prevLocalDate);
        CustomInterval currentInterval = genRemainingRecordInterval(
                prevLocalDate
                , nextLocalDate
        );
        boolean lessOrEqualToCurrentDateTime = currentInterval.getEndLocalDateTime().isBefore(LocalDateTime.now())
                || currentInterval.contains(LocalDateTime.now());
        if (lessOrEqualToCurrentDateTime) {
            resultList.add(currentInterval);
            LocalDateTime nextStartLocalDateTime = currentInterval.getEndLocalDateTime();
            resultList.addAll(genNextLocalDateTimeList(nextStartLocalDateTime.toLocalDate()));
        }
        return resultList;
    }

    public List<CustomInterval> genNextLocalDateTimeList(InvoiceRemaining prevInvoiceRemaining) {
        Optional<LocalDate> invoiceDateOpt = LocalDateTimeUtils.parseLocalDateFromString(
                prevInvoiceRemaining.getInvoiceDate()
                , "yyyyMMdd"
        );
        if (invoiceDateOpt.isPresent()) {
            return genNextLocalDateTimeList(invoiceDateOpt.get());
        } else {
            return new ArrayList<>();
        }
    }

    public CustomInterval genRemainingRecordInterval(LocalDate start, LocalDate end) {
        //實際的startDateTime為+1天，因為不包含起始
        return new CustomInterval(
                start.plusDays(1).atStartOfDay()
                , end.plusDays(1).atStartOfDay().minusSeconds(1)
        );
    }

    public Optional<RemainingRecordModel> getRemainingRecordModel(InvoiceRemaining targetRecord) {
        Optional<InvoiceRemaining> prevRemainingRecordOpt =
                invoiceRemainingRepository.findTopInvoiceRemainingByCompanyIdAndContractIdAndInvoiceDateLessThanOrderByInvoiceDateDesc(
                        targetRecord.getCompanyId()
                        , targetRecord.getContractId()
                        , targetRecord.getInvoiceDate()
                );
        if (prevRemainingRecordOpt.isPresent()) {
            return genRemainingRecordModel(prevRemainingRecordOpt.get(), targetRecord);
        }
        return Optional.empty();
    }

    public Optional<RemainingRecordModel> genModelForNonMarginNegativeCase(
            Company company
            , Contract renewContract
            , InvoiceRemaining prevRecord
            , InvoiceRemaining targetRecord) {
        Optional<RemainingRecordModel> remainingRecordModelOptional = genRemainingRecordModel(prevRecord, targetRecord);
        if (remainingRecordModelOptional.isPresent()) {
            //取得新合約的張數
            Optional<Integer> remainingOpt = remainingCountAmountProvider.getRemainingCountFromPackageId(renewContract.getPackageId());
            Optional<Integer> usageOpt = iasrDataCounterByInvoiceDate.count(company.getBusinessNo()
                    , remainingRecordModelOptional.get().getInvoiceDateInterval()

            );
            remainingRecordModelOptional.get().getTargetRecord().setRemaining(
                    remainingOpt.get() + prevRecord.getRemaining() - usageOpt.get()
            );
            remainingRecordModelOptional.get().getTargetRecord().setModifyDate(LocalDateTime.now());
            return remainingRecordModelOptional;
        }
        return Optional.empty();
    }

    public Optional<RemainingRecordModel> genRemainingRecordModel(InvoiceRemaining prevRecord, InvoiceRemaining targetRecord) {
        Optional<LocalDate> startDateOpt = LocalDateTimeUtils.parseLocalDateFromString(
                prevRecord.getInvoiceDate()
                , "yyyyMMdd"
        );
        Optional<LocalDate> endDateOpt = LocalDateTimeUtils.parseLocalDateFromString(
                targetRecord.getInvoiceDate()
                , "yyyyMMdd"
        );
        if (startDateOpt.isPresent() && endDateOpt.isPresent()) {
            LocalDateTime startDateTime = startDateOpt.get().plusDays(1).atStartOfDay();
            LocalDateTime endDateTime = endDateOpt.get().plusDays(1).atStartOfDay().minusSeconds(1);
            CustomInterval invoiceDateInterval = new CustomInterval(
                    startDateTime
                    , endDateTime
            );
            return Optional.of(new RemainingRecordModel(
                    prevRecord
                    , targetRecord
                    , invoiceDateInterval
            ));
        }
        return Optional.empty();
    }

    public Optional<RemainingRecordModel> genForExpireCaseSplitRenew(
            Company company
            , RemainingRecordModel remainingRecordModel
            , CustomInterval customInterval) {
        Optional<Integer> usageOpt = iasrDataCounterByInvoiceDate.count(company.getBusinessNo(), customInterval);
        Optional<String> invoiceDateOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                customInterval.getEndLocalDateTime(), "yyyyMMdd"
        );
        if (usageOpt.isPresent() && invoiceDateOpt.isPresent()) {
            remainingRecordModel.getTargetRecord().setContractId(null);
            remainingRecordModel.getTargetRecord().setInvoiceDate(invoiceDateOpt.get());
            remainingRecordModel.getTargetRecord().setUsage(usageOpt.get());
            remainingRecordModel.getTargetRecord().setRemaining(
                    remainingRecordModel.getPrevRecord().getRemaining() - usageOpt.get()
            );
            remainingRecordModel.getTargetRecord().setModifyDate(LocalDateTime.now());
            return Optional.of(remainingRecordModel);
        }
        return Optional.empty();
    }

    public Optional<RemainingRecordModel> updateForExpireCaseNonSplitRenew(
            Contract renewContract
            , RemainingRecordModel remainingRecordModel) {
        //取得新合約的張數
        Optional<Integer> remainingOpt = remainingCountAmountProvider.getRemainingCountFromPackageId(renewContract.getPackageId());
        if (remainingOpt.isPresent()) {
            remainingRecordModel.getTargetRecord().setRemaining(remainingOpt.get());
            //清空合約號碼
            remainingRecordModel.getTargetRecord().setContractId(null);
            return Optional.of(remainingRecordModel);
        }
        return Optional.empty();
    }

    public Optional<RemainingRecordModel> genRenewRemainingRecordModel(
            Company company
            , Contract contract
            , InvoiceRemaining prevRecord
            , CustomInterval customInterval
            , boolean overrideRemaining) {
        //取得新合約的張數
        Optional<Integer> remainingOpt = remainingCountAmountProvider.getRemainingCountFromPackageId(contract.getPackageId());
        Optional<Integer> usageOpt = iasrDataCounterByInvoiceDate.count(company.getBusinessNo(), customInterval);
        Optional<String> invoiceDateOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                customInterval.getEndLocalDateTime(), "yyyyMMdd"
        );
        if (remainingOpt.isPresent()) {
            InvoiceRemaining invoiceRemaining = new InvoiceRemaining();
            invoiceRemaining.setCompanyId(prevRecord.getCompanyId());
            invoiceRemaining.setInvoiceDate(invoiceDateOpt.get());
            invoiceRemaining.setUsage(usageOpt.get());
            if (overrideRemaining) {
                invoiceRemaining.setRemaining(remainingOpt.get() - usageOpt.get());
            } else {
                invoiceRemaining.setRemaining(remainingOpt.get() + prevRecord.getRemaining() - usageOpt.get());
            }
            invoiceRemaining.setCreateDate(LocalDateTime.now());
            invoiceRemaining.setModifyDate(LocalDateTime.now());
            return Optional.of(new RemainingRecordModel(
                    prevRecord
                    , invoiceRemaining
                    , customInterval
            ));
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
