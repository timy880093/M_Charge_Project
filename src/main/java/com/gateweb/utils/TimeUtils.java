package com.gateweb.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

/**
 * <p>
 * Title：TimeUtils.java
 * </p>
 * <p>
 * Description：時間日期操作工具類
 * </p>
 * <p>
 * Copyright：Copyright (c) 2004 TECHMORE,Inc
 * </p>
 * <p>
 * Company：TECHMORE,Inc
 * </p>
 *
 * @author Jason 2005-4-11
 * @version 1.0
 */
@Component
public class TimeUtils {

    private static final Logger logger = LoggerFactory.getLogger(TimeUtils.class);

    protected static final String DEFAULT_SLASH_TIME_FORMAT = "yyyy/MM/dd";
    protected static final String DEFAULT_DASH_TIME_FORMAT = "yyyy-MM-dd";

    // Default date long "1800-01-01 00:00:00.0"
    public static final long DEFAULT_DATE = -5364691200000L;

    public static final String TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    /**
     * 取得默認日期
     *
     * @return Calendar
     */
    public final Timestamp getDefaultTimestamp() {
        return new Timestamp(DEFAULT_DATE); // "1800-01-01 00:00:00.0"
    }

    /**
     * 將Timestamp轉為String
     *
     * @return Timestamp
     */
    public String timestamp2String(String format, Timestamp time) {
        if (time == null) {
            return null;
        }

        if (time.equals(getDefaultTimestamp())) {
            return "";
        }

        if (format == null) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(
                format);

        return formatter.format(time);
    }

    /**
     * 將Date數據類型轉為Timestamp類型，時間是24點
     * <p>
     * <code>getCurrentDayWithSpecialPattern</code>
     * </p>
     *
     * @return String
     * @author Ginger 2005-4-21
     * @since 1.0
     */
    public String getCurrentDayWithSpecialPattern() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String today = "";
        if (hour >= 12) {
            today = calendar2String("yyyy年MM月dd日下午hh:mm", c);
        } else {

            today = calendar2String("yyyy年MM月dd日上午hh:mm", c);
        }
        return today;
    } // end getCurrentDayWithSpecialPattern

    public String getCurrentDayWithSpecialPattern2() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String today = "";
        if (hour >= 12) {
            today = calendar2String("yyyy年MM月dd日下午hh時mm分ss秒", c);
        } else {

            today = calendar2String("yyyy年MM月dd日上午hh時mm分ss秒", c);
        }
        return today;
    } // end getCurrentDayWithSpecialPattern

    // -----------------------------------------------------------calendar2String

    /**
     * 將Calendar轉為String
     *
     * @return Timestamp
     */
    public String calendar2String(String format, Calendar cal) {
        if (cal.equals(getDefaultCalendar())) {
            return "";
        }

        if (format == null) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(
                format);

        if (cal == null) {
            return null;
        }
        return formatter.format(cal.getTime());
    }

    // --------------------------------------------------------getDefaultCalendar

    /**
     * 取得默認日期
     *
     * @return Calendar
     */
    public final Calendar getDefaultCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(DEFAULT_DATE); // "1800-01-01 00:00:00.0"
        return cal;
    }

    /**
     * 將java.util.Date數據類型轉為Calendar類型
     * <p>
     * <code>date2Calendar</code>
     * </p>
     *
     * @param date
     * @return
     * @author Jason 2005-4-8
     * @since 1.0
     */
    public Calendar date2Calendar(Date date) {
        Calendar cal = null;
        if (date != null) {
            cal = GregorianCalendar.getInstance();
            cal.setTime(date);
        }
        return cal;
    }

    /**
     * 取得當前日期
     *
     * @return Calendar
     * @author Jason 2004-8-9
     * @since 1.0
     */
    public Calendar getCurrentCalendar() {
        return Calendar.getInstance();
    }

    /**
     * 將Date數據類型轉為Timestamp類型，時間是0點
     * <p>
     * <code>date2Timestamp</code>
     * </p>
     *
     * @param date Date
     * @return Timestamp
     * @author Joe 2004-8-15
     * @since 1.0
     */
    public Timestamp date2Timestamp(Date date) {
        if (date instanceof Timestamp) {
            return (Timestamp) date;
        }
        return new Timestamp(date.getTime());
    }

    public Optional<Calendar> string2Calendar(String format, String str) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            cal.setTime(sdf.parse(str));
            return Optional.of(cal);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * 得到指定日期的第二天，且小時，分鐘，秒，毫秒都是0，如：2005-04-09 00:00:00.0
     * <p>
     * <code>getNextDayTimestamp</code>
     * </p>
     *
     * @param date Date
     * @return Timestamp
     * @author Joe 2004-8-15
     * @since 1.0
     */
    public Timestamp getNextDayTimestamp(Date date) {
        Calendar cal = null;

        if (date != null) {
            cal = Calendar.getInstance();
            cal.setTime(date);
        }

        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return new Timestamp(cal.getTimeInMillis());
    }

    public String simpleDateFormat(Timestamp timestamp, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(timestamp.getTime()));
    }

    /**
     * 取得發票期別(民國年+雙數月)
     *
     * @param currentTimestamp
     * @return
     */
    public String getYearMonth(Timestamp currentTimestamp) {
        int year = Integer.parseInt(new DateTime(currentTimestamp).year().toString()) - 1911;
        String month = timestamp2String("MM", currentTimestamp);
        month = String.format("%2s", Integer.valueOf(month) + (Integer.valueOf(month) % 2)).replace(' ', '0');
        return year + month;
    }


    public String getYearMonth(String invoiceDate) {
        String taiwanYear = "";
        String year = invoiceDate.substring(0, 4);
        if (StringUtils.isNotEmpty(year) && StringUtils.isNumeric(year)) {
            taiwanYear = "" + Integer.toString(Integer.valueOf(year).intValue() - 1911);
        }
        String month = invoiceDate.substring(4, 6);
        month = String.format("%2s", Integer.valueOf(month) + (Integer.valueOf(month) % 2)).replace(' ', '0');
        return taiwanYear + month;
    }

    /**
     * @param dateString
     * @param format
     * @return
     */
    public Date stringToDate(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 取得該月第一天
     *
     * @param date
     * @return
     */
    public Date getMonthOneDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        return cal.getTime();
    }

    /**
     * 取得該月最後一天
     *
     * @param date
     * @return
     */
    public Date getMonthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public Date getLastDay(Date date) {
        Calendar cal = date2Calendar(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public Timestamp getDefaultDatebyTaiwanDate(String format, String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date1 = simpleDateFormat.parse(date);
        cal.setTime(date1);
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1911);
        return new Timestamp(cal.getTime().getTime());
    }

    public String getYYYYMMDD(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String strMonth = "";
        String strDay = "";

        month = month + 1;
        if (month < 10) {
            strMonth = "0" + month;
        }
        if (day < 10) {
            strDay = "0" + day;
        }

        return "" + year + strMonth + strDay;
    }

    public Date parseDate(String yyyymm) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(yyyymm.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.parseInt(yyyymm.substring(4, 6)) - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        return cal.getTime();
    }

    public Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }
}