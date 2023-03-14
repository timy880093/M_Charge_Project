package com.gateweb.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class LocalDateTimeUtils {
    final static private Logger logger = LogManager.getLogger(LocalDateTimeUtils.class);
    public static String EGUI_DATE_FORMAT = "yyyy-MM-dd";

    public Optional<YearMonth> getYearMonthFromLocalDateTime(LocalDateTime localDateTime) {
        return Optional.of(YearMonth.of(localDateTime.getYear(), localDateTime.getMonth()));
    }

    public static Optional<YearMonth> getYearMonthFromTwYm(String yyyyMM) {
        Optional<YearMonth> yearMonthOptional = Optional.empty();
        try {
            Integer year = Integer.parseInt(yyyyMM.substring(0, 3)) + 1911;
            Integer month = Integer.parseInt(yyyyMM.substring(3, 5));
            yearMonthOptional = Optional.of(YearMonth.of(year, month));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return yearMonthOptional;
    }

    public static Optional<YearMonth> parseYearMonthFromString(String str, String pattern) {
        YearMonth result = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            result = YearMonth.parse(str, formatter);
        } catch (Exception e) {
            e.getMessage();
        }
        return Optional.ofNullable(result);
    }

    public static String yearMonthToString(YearMonth yearMonth, String pattern) {
        String result = "";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            result = yearMonth.format(formatter);
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public static Set<String> localDateTimeRangeToTwYmSet(LocalDateTime from, LocalDateTime to) {
        Set<String> result = new HashSet<>();
        LocalDateTime targetFrom = from;
        while (targetFrom.isBefore(to)) {
            result.add(getTwYearMonth(targetFrom));
            targetFrom = targetFrom.plusMonths(1);
        }
        return result;
    }

    public static Optional<LocalDate> parseLocalDateFromString(String str, String pattern) {
        LocalDate result = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            result = LocalDate.parse(str, formatter);
        } catch (Exception e) {
            e.getMessage();
        }
        return Optional.ofNullable(result);
    }

    public static Optional<LocalDateTime> parseLocalDateTimeFromString(String str, String pattern) {
        LocalDateTime result = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            result = LocalDateTime.parse(str, formatter);
        } catch (Exception e) {
            e.getMessage();
        }
        return Optional.ofNullable(result);
    }

    public static Optional<String> parseLocalDateTimeToString(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        return Optional.ofNullable(localDateTime.format(format));
    }

    public static LocalDateTime fromTimestamp(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    public static LocalDateTime fromDate(Date date) {
        return new Timestamp(date.getTime()).toLocalDateTime();
    }

    public LocalDateTime toTheEndOfTheDate(LocalDateTime localDateTime) {
        localDateTime.withHour(23).withMinute(59).withSecond(59);
        return localDateTime;
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime fromJodaDateTime(DateTime dateTime) {
        Timestamp timestamp = new Timestamp(dateTime.getMillis());
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime;
    }

    public static String getTwYearMonth(LocalDateTime date) {
        if (date.getMonthValue() % 2 != 0) {
            return String.valueOf(date.getYear() * 100 - 191100 + date.plusMonths(1).getMonthValue());
        } else {
            return String.valueOf(date.getYear() * 100 - 191100 + date.getMonthValue());
        }
    }

    public static String getYearMonth(LocalDateTime date) {
        StringBuffer sb = new StringBuffer();
        if (date.getMonthValue() % 2 != 0) {
            sb.append(date.getYear());
            sb.append(date.plusMonths(1).getMonthValue());
        } else {
            sb.append(date.getYear());
            sb.append(date.getMonthValue());
        }
        return sb.toString();
    }

    public static LocalDateTime pushToLastSecond(LocalDateTime localDateTime) {
        return localDateTime.plusDays(1).withHour(0).withSecond(0).minusSeconds(1);
    }

    /**
     * @param dateString
     * @param format
     * @return
     */
    public static Date stringToDate(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public LocalDateTime longToLocalDateTime(Long millis) {
        Timestamp timestamp = new Timestamp(millis);
        return timestamp.toLocalDateTime();
    }

    public static LocalDateTime toTheEndOfTheMonth(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate().withDayOfMonth(1).plusMonths(1).atStartOfDay().minusSeconds(1);
    }

    public static LocalDate toTheEndOfTheMonth(LocalDate localDate) {
        return localDate
                .plusMonths(1).withDayOfMonth(1).atStartOfDay().minusSeconds(1).toLocalDate();
    }

    public static Optional<String> parseLocalDateToString(LocalDate localDate, String pattern) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        return Optional.of(localDate.format(format));
    }

    public static String getCurrentDateAsInvoiceDate() {
        return parseLocalDateToString(LocalDate.now(), EGUI_DATE_FORMAT).get();
    }
}
