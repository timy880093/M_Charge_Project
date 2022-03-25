package com.gateweb.charge.feeCalculation.dataCounter;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.orm.einv.repository.EinvInvoiceMainRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class IasrDataCounterByUploadDate implements DataCounter {

    @Autowired
    EinvInvoiceMainRepository einvInvoiceMainRepository;

    @Override
    public Optional<Integer> count(String businessNo, CustomInterval customInterval) {
        BigInteger result = BigInteger.ZERO;
        //處理一般開發票
        String cYearMonth = LocalDateTimeUtils.getTwYearMonth(customInterval.getStartLocalDateTime());
        Optional<BigInteger> usageCountOpt = einvInvoiceMainRepository.findUsageCountByUploadDate(
                customInterval.getStartLocalDateTime()
                , customInterval.getEndLocalDateTime()
                , cYearMonth
                , businessNo
        );
        //處理回頭開發票
        String cPrevYearMonth = LocalDateTimeUtils.getTwYearMonth(customInterval.getStartLocalDateTime().minusMonths(2));
        Optional<BigInteger> prevUsageCountOpt = einvInvoiceMainRepository.findUsageCountByUploadDate(
                customInterval.getStartLocalDateTime()
                , customInterval.getEndLocalDateTime()
                , cPrevYearMonth
                , businessNo
        );
        if (usageCountOpt.isPresent()) {
            result = result.add(usageCountOpt.get());
        }
        if (prevUsageCountOpt.isPresent()) {
            result = result.add(prevUsageCountOpt.get());
        }
        return Optional.of(result.intValue());
    }
}
