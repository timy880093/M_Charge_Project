package com.gateweb.charge.contract.remainingCount.scheduleJob.writeRecord;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component.RemainingContractComponent;
import com.gateweb.charge.feeCalculation.bean.ChargeByRemainingCountCalData;
import com.gateweb.charge.feeCalculation.dataCounter.IasrDataCounterByInvoiceDate;
import com.gateweb.charge.notice.component.RemainingCountThresholdNoticeComponent;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Component
public class RemainingRecordWriterByInvoiceDate {
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    IasrDataCounterByInvoiceDate iasrDataCounterByInvoiceDate;
    @Autowired
    RemainingContractComponent remainingContractComponent;
    @Autowired
    RemainingCountThresholdNoticeComponent remainingCountThresholdNotice;

    final Logger logger = LogManager.getLogger(getClass());

    /**
     * 當前日期大於目標計算的日期且其數字不是零
     *
     * @return
     */
    @Deprecated
    public boolean needToBeRecorded(
            Integer remainingCount
            , CustomInterval calculateInterval
            , LocalDateTime contractEndLocalDateTime) {
        boolean remainingCountGreaterThanZero = remainingCount > 0;
        boolean beforeNow = calculateInterval.getStartLocalDateTime().isBefore(LocalDateTime.now());
        boolean calculateIntervalInsideOfContractEndDate
                = contractEndLocalDateTime.isAfter(calculateInterval.getEndLocalDateTime())
                || contractEndLocalDateTime.isEqual(calculateInterval.getEndLocalDateTime());
        return remainingCountGreaterThanZero && beforeNow && calculateIntervalInsideOfContractEndDate;
    }

    public Optional<InvoiceRemaining> genNextInvoiceRemaining(
            final ChargeByRemainingCountCalData chargeByRemainingCountCalData
            , CustomInterval targetCalculateInterval
            , int remaining) {
        Optional<InvoiceRemaining> nextInvoiceRemainingOpt = Optional.empty();
        Optional<Integer> usageCountOpt = iasrDataCounterByInvoiceDate.count(
                chargeByRemainingCountCalData.getCompany().getBusinessNo()
                , targetCalculateInterval
        );
        Optional<String> lastInvoiceDateOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                targetCalculateInterval.getEndLocalDateTime()
                , "yyyyMMdd"
        );
        if (usageCountOpt.isPresent() && lastInvoiceDateOpt.isPresent()) {
            InvoiceRemaining invoiceRemaining = new InvoiceRemaining();
            invoiceRemaining.setUsage(usageCountOpt.get());
            invoiceRemaining.setCompanyId(chargeByRemainingCountCalData.getCompany().getCompanyId().longValue());
            invoiceRemaining.setContractId(chargeByRemainingCountCalData.getContract().getContractId());
            invoiceRemaining.setInvoiceDate(lastInvoiceDateOpt.get());
            invoiceRemaining.setCreateDate(chargeByRemainingCountCalData.getExecutionDateTime());
            invoiceRemaining.setModifyDate(chargeByRemainingCountCalData.getExecutionDateTime());
            invoiceRemaining.setRemaining(
                    remaining - usageCountOpt.get()
            );
            nextInvoiceRemainingOpt = Optional.of(invoiceRemaining);
        }
        return nextInvoiceRemainingOpt;
    }

    @Transactional
    public void executeWriter(Set<Company> targetCompanySet) {
        Set<ChargeByRemainingCountCalData> chargeByRemainingCountCalDataSet
                = remainingContractComponent.chargeByRemainingCountCalDataCollector(targetCompanySet);
        chargeByRemainingCountCalDataSet.stream().forEach(chargeByRemainingCountCalData -> {
            try {
                int remaining = chargeByRemainingCountCalData.getPreviousInvoiceRemaining().getRemaining();
                for (CustomInterval nextCalculateInterval : chargeByRemainingCountCalData.getNextCalculateIntervalList()) {
                    Optional<InvoiceRemaining> nextInvoiceRemainingOpt = genNextInvoiceRemaining(
                            chargeByRemainingCountCalData
                            , nextCalculateInterval
                            , remaining
                    );
                    if (nextInvoiceRemainingOpt.isPresent()) {
                        invoiceRemainingRepository.save(nextInvoiceRemainingOpt.get());
                        remaining = nextInvoiceRemainingOpt.get().getRemaining();
                        //寄送門檻通知信
                        remainingCountThresholdNotice.prepareNoticeIfNecessary(
                                nextInvoiceRemainingOpt.get()
                                , chargeByRemainingCountCalData.getContract()
                        );
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });
    }
}
