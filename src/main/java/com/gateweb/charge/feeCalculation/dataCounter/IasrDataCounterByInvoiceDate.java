package com.gateweb.charge.feeCalculation.dataCounter;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.orm.charge.entity.ChargeIasrEntity;
import com.gateweb.orm.charge.repository.ChargeIasrRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Component
public class IasrDataCounterByInvoiceDate implements DataCounter {
    private Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    ChargeIasrRepository chargeIasrRepository;

    public List<ChargeIasrEntity> getInvoiceAmountSummaryReportByBusinessNoAndInvoiceDate(String businessNo, long fromMillis, long toMillis) {
        DateTime fromDateTime = new DateTime(fromMillis);
        java.sql.Date from = new java.sql.Date(fromDateTime.getMillis());
        java.sql.Date to = new java.sql.Date(toMillis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //文字可以用lessThanEqual
        List<ChargeIasrEntity> invoiceAmountSummaryReportEntityList
                = chargeIasrRepository.findBySellerIsAndInvoiceDateGreaterThanEqualAndInvoiceDateLessThanEqual(
                businessNo
                , simpleDateFormat.format(from)
                , simpleDateFormat.format(to)
        );
        return invoiceAmountSummaryReportEntityList;
    }

    /**
     * 這裡要區分幾種情況，如果無法得到查詢結果應該就要回傳空的物件
     *
     * @param businessNo
     * @param customInterval
     * @return
     */
    @Override
    public Optional<Integer> count(String businessNo, CustomInterval customInterval) {
        Optional<Integer> result = Optional.empty();
        long fromMillis = customInterval.getSqlStartTimestamp().getTime();
        long toMillis = customInterval.getSqlEndTimestamp().getTime();
        try {
            List<ChargeIasrEntity> invoiceAmountSummaryReportEntityList = getInvoiceAmountSummaryReportByBusinessNoAndInvoiceDate(businessNo, fromMillis, toMillis);
            Integer usedCount = 0;
            for (ChargeIasrEntity chargeIasrEntity : invoiceAmountSummaryReportEntityList) {
                usedCount += chargeIasrEntity.getAmount();
            }
            result = Optional.of(usedCount);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }
}
