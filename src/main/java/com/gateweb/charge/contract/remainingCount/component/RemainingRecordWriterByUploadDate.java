package com.gateweb.charge.contract.remainingCount.component;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.exception.ConversionFailedException;
import com.gateweb.charge.feeCalculation.bean.ChargeByRemainingCountCalData;
import com.gateweb.charge.feeCalculation.dataCounter.IasrDataCounterByUploadDate;
import com.gateweb.charge.notice.component.RemainingCountThresholdNoticeComponent;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;
import java.util.Set;

@Deprecated
@Component
public class RemainingRecordWriterByUploadDate {
    final Logger logger = LogManager.getLogger(getClass());

    InvoiceRemainingRepository invoiceRemainingRepository;
    RemainingContractComponent remainingContractComponent;
    ContractRepository contractRepository;
    IasrDataCounterByUploadDate iasrDataCounterByUploadDate;
    RemainingCountThresholdNoticeComponent remainingCountThresholdNotice;

    public RemainingRecordWriterByUploadDate(InvoiceRemainingRepository invoiceRemainingRepository, RemainingContractComponent remainingContractComponent, ContractRepository contractRepository, IasrDataCounterByUploadDate iasrDataCounterByUploadDate, RemainingCountThresholdNoticeComponent remainingCountThresholdNotice) {
        this.invoiceRemainingRepository = invoiceRemainingRepository;
        this.remainingContractComponent = remainingContractComponent;
        this.contractRepository = contractRepository;
        this.iasrDataCounterByUploadDate = iasrDataCounterByUploadDate;
        this.remainingCountThresholdNotice = remainingCountThresholdNotice;
    }

    public void executeWriter(Set<Company> targetCompanySet) {
        Set<ChargeByRemainingCountCalData> chargeByRemainingCountCalDataSet
                = remainingContractComponent.chargeByRemainingCountCalDataCollector(targetCompanySet);
        chargeByRemainingCountCalDataSet.stream().forEach(chargeByRemainingCountCalData -> {
            try {
                Optional<CustomInterval> searchIntervalOpt = getSearchInterval(chargeByRemainingCountCalData);
                boolean validSearchEndDate = isValidSearchEndDate(
                        searchIntervalOpt.get().getEndLocalDateTime()
                        , chargeByRemainingCountCalData.getPreviousInvoiceRemaining().getUploadDate()
                        , chargeByRemainingCountCalData.getContract().getExpirationDate()
                );
                if (searchIntervalOpt.isPresent() && validSearchEndDate) {
                    Optional<Integer> usageCountOpt = iasrDataCounterByUploadDate.count(
                            chargeByRemainingCountCalData.getCompany().getBusinessNo()
                            , searchIntervalOpt.get()
                    );
                    InvoiceRemaining nextInvoiceRemaining = remainingContractComponent.genNextInvoiceRemaining(
                            chargeByRemainingCountCalData.getPreviousInvoiceRemaining()
                            , usageCountOpt
                            , searchIntervalOpt.get().getEndLocalDateTime()
                    );
                    boolean needToBeRecordedFlag = needToBeRecorded(
                            chargeByRemainingCountCalData.getExecutionDateTime()
                            , searchIntervalOpt.get().getStartLocalDateTime()
                            , chargeByRemainingCountCalData.getContract().getExpirationDate()
                            , nextInvoiceRemaining.getRemaining()
                            , nextInvoiceRemaining.getUsage()
                    );
                    if (needToBeRecordedFlag) {
                        invoiceRemainingRepository.save(nextInvoiceRemaining);
                        //寄送門檻通知信
                        remainingCountThresholdNotice.prepareNoticeIfNecessary(
                                nextInvoiceRemaining
                                , chargeByRemainingCountCalData.getContract()
                        );
                    }
                    if (nextInvoiceRemaining.getRemaining() < 0) {
                        updateContractEndDate(
                                nextInvoiceRemaining.getContractId()
                                , nextInvoiceRemaining.getUploadDate()
                        );
                    }
                }
            } catch (ConversionFailedException e) {
                logger.error(e.getMessage());
            }
        });
    }

    public Optional<CustomInterval> getSearchInterval(ChargeByRemainingCountCalData chargeByRemainingCountCalData) throws ConversionFailedException {
        Optional<CustomInterval> result = Optional.empty();
        LocalDateTime startDate = getSearchStartDate(
                chargeByRemainingCountCalData.getPreviousInvoiceRemaining()
                , chargeByRemainingCountCalData.getContract()
        );
        Optional<LocalDateTime> endDateOpt = getSearchEndDate(
                chargeByRemainingCountCalData.getExecutionDateTime()
                , startDate
                , chargeByRemainingCountCalData.getContract().getExpirationDate()
        );
        if (endDateOpt.isPresent() && startDate.isBefore(endDateOpt.get())) {
            result = Optional.of(new CustomInterval(startDate, endDateOpt.get()));
        }
        return result;
    }

    public LocalDateTime getSearchStartDate(InvoiceRemaining invoiceRemaining, Contract contract) {
        LocalDateTime startDate;
        if (invoiceRemaining.getUsage() == null) {
            startDate = contract.getEffectiveDate();
        } else {
            startDate = invoiceRemaining.getUploadDate().plusSeconds(1);
        }
        return startDate;
    }

    public Optional<LocalDateTime> getSearchEndDate(
            LocalDateTime now
            , LocalDateTime searchStartDate
            , LocalDateTime contractExpirationDate) throws ConversionFailedException {
        Optional<LocalDateTime> endDate = Optional.empty();
        String currentTwYearMonth = LocalDateTimeUtils.getTwYearMonth(now);
        String startDateTwYearMonth = LocalDateTimeUtils.getTwYearMonth(searchStartDate);
        if (currentTwYearMonth.equals(startDateTwYearMonth)) {
            endDate = Optional.of(now.withHour(0).withSecond(0).minusSeconds(1));
        } else {
            Optional<YearMonth> yearMonthOpt = LocalDateTimeUtils.getYearMonthFromTwYm(startDateTwYearMonth);
            if (yearMonthOpt.isPresent()) {
                LocalDateTime targetEndDate = yearMonthOpt.get().atEndOfMonth().plusDays(1).atStartOfDay().minusSeconds(1);
                if (targetEndDate.isBefore(contractExpirationDate)) {
                    endDate = Optional.of(targetEndDate);
                } else {
                    endDate = Optional.of(contractExpirationDate);
                }
            }
        }
        return endDate;
    }

    public boolean isValidSearchEndDate(
            LocalDateTime searchEndDate
            , LocalDateTime previousUploadDate
            , LocalDateTime contractExpirationDate) {
        boolean before = searchEndDate.isBefore(contractExpirationDate);
        boolean eq = searchEndDate.compareTo(contractExpirationDate) == 0;
        boolean notEqualsToPrev = previousUploadDate.compareTo(searchEndDate) != 0;
        return (before || eq) && notEqualsToPrev;
    }

    public void updateContractEndDate(Long contractId, LocalDateTime realEndDate) {
        Optional<Contract> contractOptional = contractRepository.findById(contractId);
        if (contractOptional.isPresent()) {
            contractOptional.get().setExpirationDate(realEndDate);
            contractRepository.save(contractOptional.get());
        }
    }

    /**
     * 真值表：
     * A: mixFlag
     * a1: currentYmNotEqualsToStartYm
     * a2: currentTimestampAfterExpirationDate
     * <p>
     * 這個case下，單獨將a1,a2與其它數值配會得到一樣的真值表，所以可以這樣用
     * a1,a2,r
     * 0,0,0
     * 0,1,1
     * 1,0,1
     * 1,1,1
     * <p>
     * B: remainingCountIsNegative
     * C: usageCountIsZero
     * A,B,C,R
     * 0,0,0,0
     * 0,0,1,0
     * 0,1,0,1
     * 0,1,1,0
     * 1,0,0,1
     * 1,0,1,1
     * 1,1,0,1
     * 1,1,1,1
     * <p>
     * 簡化：A+BC'
     *
     * @param now
     * @param searchStartDate
     * @param contractExpirationDate
     * @param nextRemaining
     * @param nextUsage
     * @return
     */
    public boolean needToBeRecorded(
            LocalDateTime now
            , LocalDateTime searchStartDate
            , LocalDateTime contractExpirationDate
            , Integer nextRemaining
            , Integer nextUsage) {
        String currentYearMonth = LocalDateTimeUtils.getYearMonth(now);
        String startDateYearMonth = LocalDateTimeUtils.getYearMonth(searchStartDate);
        boolean aFlag = !currentYearMonth.equals(startDateYearMonth) || now.isAfter(contractExpirationDate);
        boolean remainingCountIsNegative = nextRemaining < 0;
        boolean usageCountIsZero = nextUsage == 0;
        return aFlag || (remainingCountIsNegative && !usageCountIsZero);
    }

}
