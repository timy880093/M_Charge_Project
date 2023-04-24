package com.gateweb.charge.contract.remainingCount.remainingRecordFrame;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.utils.LocalDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RemainingRecordFrameUtils {
    private static Logger logger = LoggerFactory.getLogger(RemainingRecordFrameUtils.class);

    public static Optional<CustomInterval> genRemainingRecordInvoiceDateInterval(RemainingRecordFrame remainingRecordFrame) {
        return genRemainingRecordInvoiceDateInterval(
                remainingRecordFrame.getPrevRecord().getInvoiceDate()
                , remainingRecordFrame.getTargetRecord().getInvoiceDate()
        );
    }

    public static Optional<CustomInterval> genRemainingRecordInvoiceDateInterval(String prevInvoiceDate, String currentInvoiceDate) {
        try {
            Optional<LocalDate> calculateStartDateOpt = LocalDateTimeUtils.parseLocalDateFromString(
                    prevInvoiceDate
                    , "yyyyMMdd"
            );
            Optional<LocalDate> calculateEndDateOpt = LocalDateTimeUtils.parseLocalDateFromString(
                    currentInvoiceDate
                    , "yyyyMMdd"
            );
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

    public static CustomInterval genRemainingRecordInterval(LocalDate start, LocalDate end) {
        //實際的startDateTime為+1天，因為不包含起始
        return new CustomInterval(
                start.plusDays(1).atStartOfDay()
                , end.plusDays(1).atStartOfDay().minusSeconds(1)
        );
    }

    private static LocalDate genNextLocalDate(LocalDate prevLocalDate) {
        LocalDate nextLocalDate = LocalDateTimeUtils.toTheEndOfTheMonth(prevLocalDate);
        if (nextLocalDate.equals(prevLocalDate)) {
            return genNextLocalDate(prevLocalDate.plusDays(1));
        }
        return nextLocalDate;
    }

    private static List<CustomInterval> genNextLocalDateTimeList(LocalDate prevLocalDate) {
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

    public static List<CustomInterval> genNextLocalDateTimeList(InvoiceRemaining prevInvoiceRemaining) {
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

}
