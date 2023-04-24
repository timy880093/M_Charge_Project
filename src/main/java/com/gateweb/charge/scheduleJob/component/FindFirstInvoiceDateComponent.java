package com.gateweb.charge.scheduleJob.component;

import com.gateweb.charge.chargePolicy.cycle.builder.CronCycle;
import com.gateweb.utils.ConcurrentUtils;
import com.gateweb.utils.LocalDateTimeUtils;
import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.orm.einv.repository.EinvInvoiceMainRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;

@Component
public class FindFirstInvoiceDateComponent {
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    EinvInvoiceMainRepository einvInvoiceMainRepository;

    /**
     * 此Function的目的在於找出可申報期別
     * 找出可申報期可以用於縮小尋找客戶首次上傳發票的範圍
     * 第一張的邏輯雖然清楚但畢竟不能真的撈歷史資料，不是這樣子的
     * <p>
     * 可上傳區間為單數月15日前到上一個單數月15日
     * 0 0 0 15 JAN,MAR,MAY,JUL,SEP,NOV ? *
     *
     * @param targetDateTime
     * @return
     */
    public CustomInterval getAcceptableInvoiceDateRangeByLocalDateTime(LocalDateTime targetDateTime) {
        CronCycle cronCycle = new CronCycle();
        cronCycle.buildCycle("0 0 0 15 JAN,MAR,MAY,JUL,SEP,NOV ? *");
        LocalDateTime startDate = cronCycle.getPrevFireTime(targetDateTime);
        LocalDateTime endDate = cronCycle.getNextValidTimeAfter(targetDateTime);
        return new CustomInterval(startDate, endDate);
    }

    public Optional<String> findFirstInvoiceDateByBusinessNoWithRetryInYm(String businessNo, String yearMonth) {
        Callable<String> futureTask = new Callable<String>() {
            @Override
            public String call() throws Exception {
                logger.info("findFirstInvoiceDateByBusinessNoWithRetry,businessNo:" + businessNo + ",fromYm:" + yearMonth);
                String result = einvInvoiceMainRepository.findFirstInvoiceDateByBusinessNoAndYearMonthAfter(
                        businessNo
                        , yearMonth
                );
                return result;
            }
        };
        return ConcurrentUtils.callableWithRetry(futureTask);
    }

    public Optional<String> findFirstInvoiceDateByBusinessNoWithRetry(String businessNo, java.time.LocalDateTime from, java.time.LocalDateTime to) {
        String result = null;
        Set<String> ymSet = LocalDateTimeUtils.localDateTimeRangeToTwYmSet(from, to);
        for (String ym : ymSet) {
            Optional<String> invoiceDate = findFirstInvoiceDateByBusinessNoWithRetryInYm(businessNo, ym);
            if (invoiceDate.isPresent()) {
                result = invoiceDate.get();
                break;
            }
        }
        return Optional.ofNullable(result);
    }

    public Optional<String> findFirstInvoiceDateFromCompanyCreateDate(String businessNo, LocalDateTime companyCreateDate) {
        CustomInterval acceptableRange = getAcceptableInvoiceDateRangeByLocalDateTime(companyCreateDate);
        Optional<String> invoiceDateTimeOptional = findFirstInvoiceDateByBusinessNoWithRetry(
                businessNo
                , acceptableRange.getStartLocalDateTime()
                , LocalDateTime.now());
        return invoiceDateTimeOptional;
    }

}
