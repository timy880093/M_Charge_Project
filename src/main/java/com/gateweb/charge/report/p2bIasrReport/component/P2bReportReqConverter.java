package com.gateweb.charge.report.p2bIasrReport.component;

import com.gateweb.charge.report.p2bIasrReport.bean.P2bIasrReportGstaticReq;
import com.gateweb.utils.LocalDateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Optional;

public class P2bReportReqConverter {
    private static Logger logger = LoggerFactory.getLogger(P2bReportReqConverter.class);

    public static Optional<P2bIasrReportGstaticReq> gen(String seller, String yyyyMMddStart, String yyyyMMddEnd) {
        Optional<LocalDate> startDateOpt = LocalDateTimeUtils.parseLocalDateFromString(yyyyMMddStart, "yyyyMMdd");
        Optional<LocalDate> endDateOpt = LocalDateTimeUtils.parseLocalDateFromString(yyyyMMddEnd, "yyyyMMdd");
        if (startDateOpt.isPresent() && endDateOpt.isPresent()) {
            LocalDateTime from = startDateOpt.get().atStartOfDay();
            LocalDateTime to = endDateOpt.get().atStartOfDay().plusDays(1).minusSeconds(1);
            return gen(seller, from, to);
        }
        return Optional.empty();
    }

    private static Optional<P2bIasrReportGstaticReq> gen(String seller, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            if (StringUtils.isEmpty(seller)) {
                return Optional.empty();
            }
            P2bIasrReportGstaticReq p2bIasrReportGstaticReq = new P2bIasrReportGstaticReq();
            p2bIasrReportGstaticReq.setSellerIdentifier(seller);
            p2bIasrReportGstaticReq.setStartLocalDateTime(
                    startDate
            );
            p2bIasrReportGstaticReq.setEndLocalDateTime(
                    endDate
            );
            long diffMonth = ChronoUnit.MONTHS.between(
                    p2bIasrReportGstaticReq.getStartLocalDateTime().toLocalDate(),
                    p2bIasrReportGstaticReq.getEndLocalDateTime().toLocalDate()
            );
            List<String> monthStrList = new ArrayList<>();
            for (int i = 0; i < diffMonth; i++) {
                LocalDateTime targetLocalDateTime = p2bIasrReportGstaticReq.getStartLocalDateTime();
                if (i != 0) {
                    targetLocalDateTime = targetLocalDateTime.plusMonths(i);
                }
                Optional<String> monthStrOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                        targetLocalDateTime, "yyyyMM"
                );
                if (monthStrOpt.isPresent()) {
                    monthStrList.add(monthStrOpt.get());
                } else {
                    throw new InvalidPropertiesFormatException(targetLocalDateTime.toString());
                }
            }
            p2bIasrReportGstaticReq.setMonthStrList(monthStrList);
            return Optional.of(p2bIasrReportGstaticReq);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return Optional.empty();
    }
}
