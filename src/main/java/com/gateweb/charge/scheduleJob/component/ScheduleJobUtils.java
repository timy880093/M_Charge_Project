package com.gateweb.charge.scheduleJob.component;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScheduleJobUtils {
    private static Logger logger = LoggerFactory.getLogger(ScheduleJobUtils.class);

    public static List<String> getPreviousAndCurrentYearMonthStrList() {
        List<String> result = new ArrayList<>();
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousMonthDate = LocalDate.now().minusMonths(1);
            String currentMonthStr = StringUtils.leftPad(
                    String.valueOf(currentDate.getMonthValue()), 2, "0"
            );
            String prevMonthStr = StringUtils.leftPad(
                    String.valueOf(previousMonthDate.getMonthValue()), 2, "0"
            );
            String prevYearMonth = currentDate.getYear() + prevMonthStr;
            String currentYearMonth = currentDate.getYear() + currentMonthStr;
            result.add(prevYearMonth);
            result.add(currentYearMonth);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }
}
