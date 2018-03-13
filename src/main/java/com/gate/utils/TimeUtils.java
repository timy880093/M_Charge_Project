package com.gate.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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

	private static final Log log = LogFactory.getLog(TimeUtils.class);

	protected static final String DEFAULT_TIME_FORMAT = "yyyy/MM/dd";

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

	public final Timestamp getNullTimestamp(){
		return string2Timestamp("1800/01/01");
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
	 * 得到指定日期的第二天，且小時，分鐘，秒，毫秒都是0，如：2005-04-09 00:00:00.0
	 * <p>
	 * <code>getNextDayTimestamp</code>
	 * </p>
	 *

	 * @return Timestamp
	 * @author Joe 2004-8-15
	 * @since 1.0
	 */
	public Timestamp getFutureDate(Timestamp nextPublishDate,
			Timestamp endDate, int frequency) {

		log.info(">>>>>>>>:nextPublishDate " + nextPublishDate);
		log.info(">>>>>>>>:endDate " + endDate);
		log.info(">>>>>>>>:frequency " + frequency);

		Calendar startCal = null;
		Calendar endCal = null;

		startCal = string2Calendar("yyyy-MM-dd", timestamp2String(null,
				nextPublishDate));
		Calendar cal = string2Calendar("yyyy-MM-dd", timestamp2String(null,
				nextPublishDate));
		if (null != endDate && !endDate.equals(getNullTimestamp())) {
			endCal = string2Calendar("yyyy-MM-dd", timestamp2String(null,
					endDate));
		}
		if (frequency == 1) {
			startCal.add(Calendar.MONTH, 2);
			if ((null != endDate && !endDate
					.equals(getNullTimestamp()))
					&& endCal.after(startCal)
					|| (null == endDate || endDate
							.equals(getNullTimestamp()))) {
				cal.add(Calendar.MONTH, 2);
			}
		} else if (frequency == 2) {
			startCal.add(Calendar.MONTH, 1);
			if ((null != endDate && !endDate
					.equals(getNullTimestamp()))
					&& endCal.after(startCal)
					|| (null == endDate || endDate
							.equals(getNullTimestamp()))) {
				cal.add(Calendar.MONTH, 1);
			}
		} else if (frequency == 3) {
			startCal.add(Calendar.WEEK_OF_YEAR, 2);
			if ((null != endDate && !endDate
					.equals(getNullTimestamp()))
					&& endCal.after(startCal)
					|| (null == endDate || endDate
							.equals(getNullTimestamp()))) {
				cal.add(Calendar.WEEK_OF_YEAR, 2);
			}
		} else if (frequency == 4) {
			startCal.add(Calendar.WEEK_OF_YEAR, 1);
			if ((null != endDate && !endDate
					.equals(getNullTimestamp()))
					&& endCal.after(startCal)
					|| (null == endDate || endDate
							.equals(getNullTimestamp()))) {
				cal.add(Calendar.WEEK_OF_YEAR, 1);
			}
		} else if (frequency == 5) {
			startCal.add(Calendar.DAY_OF_YEAR, 1);
			if ((null != endDate && !endDate
					.equals(getNullTimestamp()))
					&& endCal.after(startCal)
					|| (null == endDate || endDate
							.equals(getNullTimestamp()))) {
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
		}

		return string2Timestamp(calendar2String(cal));
	}

	/**
	 * 得到指定日期的第二天，且小時，分鐘，秒，毫秒都是0，如：2005-04-09 00:00:00.0
	 * <p>
	 * <code>getNextDayTimestamp</code>
	 * </p>
	 *

	 * @return Timestamp
	 * @author Joe 2004-8-15
	 * @since 1.0
	 */
	public Timestamp getPreviousDate(Timestamp publishDate, int frequency) {

//		log.info(">>>>>>>>:nextPublishDate " + publishDate);
//		log.info(">>>>>>>>:frequency " + frequency);

		Calendar cal = string2Calendar("yyyy-MM-dd", timestamp2String(null,
				publishDate));

		if (frequency == 1) {
			cal.add(Calendar.MONTH, -2);
		} else if (frequency == 2) {
			cal.add(Calendar.MONTH, -1);
		} else if (frequency == 3) {
			cal.add(Calendar.WEEK_OF_YEAR, -2);
		} else if (frequency == 4) {
			cal.add(Calendar.WEEK_OF_YEAR, -1);
		}

		return string2Timestamp(calendar2String(cal));
	}

	public final String TimeStamp2String(String format, Timestamp time) {
		if (format == null) {
			format = DEFAULT_TIME_FORMAT;
		}

		SimpleDateFormat df = new SimpleDateFormat(format);

		String str = df.format(time);

		return str;
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
	 * 取得當前日期字符串,yyyy-MM-dd
	 *
	 * @return Calendar
	 * @author Morgan 2005-01-13
	 * @since 1.0
	 */
	public String getCurrentDateString() {
		return calendar2String(Calendar.getInstance());
	}

	/**
	 * 取得當前日期字符串
	 *
	 * @return Calendar
	 * @author Morgan 2005-01-13
	 * @since 1.0
	 */
	public String getCurrentDateString(String format) {
		return calendar2String(format, Calendar.getInstance(), null);
	}

	/**
	 * 取得當前日期
	 *
	 * @return Timestamp
	 * @author Jason 2004-8-9
	 * @since 1.0
	 */
	public Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 取得當前日期的Timestamp，如果format不等於null,則只有format規定的內容有值
	 * 如：yyyy-MM-dd對應的值是2005-04-08 12:12 45 341
	 * <p>
	 * <code>getTodayTimestamp</code>
	 * </p>
	 *
	 * @param format
	 * @return
	 * @author Jason 2005-4-8
	 * @since 1.0
	 */
	public Timestamp getCurrentTimestamp(String format) {
		String today = date2String(format, getCurrentTimestamp(), null);
		return string2Timestamp(format, today);
	}

	/**
	 * 將Calendar轉為String,如果cal等於null,則返回def
	 * <p>
	 * <code>calendar2String</code>
	 * </p>
	 *
	 * @param format
	 * @param cal
	 * @param def
	 * @return
	 * @author Jason 2005-4-11
	 * @since 1.0
	 */
	public String calendar2String(String format, Calendar cal, String def) {
		if (cal == null) {
			return def;
		}

		if (format == null) {
			format = DEFAULT_TIME_FORMAT;
		}

		return new SimpleDateFormat(format).format(cal.getTime());
	}

	/**
	 * 將Calendar轉為String,如果cal等於null,則返回def
	 * @return Timestamp
	 * @author Morgan 2004-12-22
	 * @since 1.0
	 */
	public String calendar2String(Calendar cal, String def) {
		return calendar2String(null, cal, def);
	}

	/**
	 * 將Calendar轉為String,如果cal等於null,則返回null
	 * @return Timestamp
	 * @author Morgan 2004-12-22
	 * @since 1.0
	 */
	public String calendar2String(Calendar cal) {
		return calendar2String(null, cal, null);
	}

	/**
	 * 將java.util.Date轉為String,如果date等於time,則返回def
	 * @return String
	 * @author Jason 2004-9-29
	 * @since 1.0
	 */
	public String date2String(String format, Date date,
			String def) {
		if (date == null) {
			return def;
		}

		if (format == null) {
			format = DEFAULT_TIME_FORMAT;
		}

		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 將java.util.Date轉為String,如果date等於time,則返回null
	 * @return String
	 * @author Jason 2004-9-29
	 * @since 1.0
	 */
	public String date2String(String format, Date date) {
		return date2String(format, date, null);
	}

	/**
	 * 將Date轉為String,如果date等於time,則返回def
	 * @return String
	 * @author Morgan 2004-12-22
	 * @since 1.0
	 */
	public String date2String(Date date, String def) {
		return date2String(null, date, def);
	}

	/**
	 * 將Date轉為String,如果date等於time,則返回null
	 * @return String
	 * @author Morgan 2004-12-22
	 * @since 1.0
	 */
	public String date2String(Date date) {
		return date2String(null, date, null);
	}

	/**
	 * 將String轉為Timestamp
	 * @return String
	 * @author Jason 2004-8-9
	 * @since 1.0
	 */
	public final Timestamp string2Timestamp(String format, String time) {
		if (format == null) {
			format = DEFAULT_TIME_FORMAT;
		}
		if (StringUtils.isEmpty(time)) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				format);

		try {
			return new Timestamp(simpleDateFormat.parse(time).getTime());
		} catch (ParseException e) {
			try {
				SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(
						"yyyy-MM-dd");
				return new Timestamp(simpleDateFormat2.parse(time).getTime());
			} catch (ParseException e2) {
				throw new IllegalArgumentException("時間字串解析錯誤");
			}

		}
	}

	/**
	 * 將String轉為Timestamp
	 *
	 * @param time
	 *            String
	 * @return String
	 * @author Morgan 2004-12-22
	 * @since 1.0
	 */
	public final Timestamp string2Timestamp(String time) {
		return string2Timestamp(null, time);
	}

	/**
	 * 將String轉為Calendar
	 * @return Timestamp
	 * @author Jason 2004-8-9
	 * @since 1.0
	 */
	public final Calendar string2Calendar(String format, String cal) {
		if (format == null) {
			format = DEFAULT_TIME_FORMAT;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date = null;

		try {
			date = simpleDateFormat.parse(cal);
		} catch (ParseException e) {
			throw new IllegalArgumentException("時間字串解析錯誤");
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 將String轉為Calendar
	 * @return Timestamp
	 * @author Morgan 2004-12-22
	 * @since 1.0
	 */
	public final Calendar string2Calendar(String cal) {
		return string2Calendar(null, cal);
	}

	/**
	 * 取得指定年指定月的天數
	 *
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @return int
	 * @author Jason 2004-8-9
	 * @since 1.0
	 */
	public final int getMonthDays(int year, int month) {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("月份值輸入不正確");
		}

		switch (month) {
		case 1:
			return 31;
		case 2:
			if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
				return 29;
			} else {
				return 28;
			}
		case 3:
			return 31;
		case 4:
			return 30;
		case 5:
			return 31;
		case 6:
			return 30;
		case 7:
			return 31;
		case 8:
			return 31;
		case 9:
			return 30;
		case 10:
			return 31;
		case 11:
			return 30;
		case 12:
			return 31;
		}
		return 0;
	}

	/**
	 * 取得日期 sdate2-sdate1 的差值
	 *
	 * @param sdate1
	 *            String
	 * @param sdate2
	 *            String
	 * @param format
	 *            String Date的format格式
	 * @param tz
	 *            TimeZone
	 * @return int 取得日期 sdate2-sdate1 的差值
	 * @author Jason 2004-8-9
	 * @since 1.0
	 */
	public int getDaysDiff(String sdate1, String sdate2, String format,
			TimeZone tz) {
		Date date1 = string2Timestamp(format, sdate1);
		Date date2 = string2Timestamp(format, sdate2);

		Calendar cal1 = null;
		Calendar cal2 = null;

		if (tz == null) {
			cal1 = Calendar.getInstance();
			cal2 = Calendar.getInstance();
		} else {
			cal1 = Calendar.getInstance(tz);
			cal2 = Calendar.getInstance(tz);
		}

		// different date might have different offset
		cal1.setTime(date1);
		long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET)
				+ cal1.get(Calendar.DST_OFFSET);

		cal2.setTime(date2);
		long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET)
				+ cal2.get(Calendar.DST_OFFSET);

		// Use integer calculation, truncate the decimals
		int hr1 = (int) (ldate1 / 3600000); // 60*60*1000
		int hr2 = (int) (ldate2 / 3600000);

		int days1 = hr1 / 24;
		int days2 = hr2 / 24;

		return days2 - days1;
		// int dateDiff = days2 - days1;
		// int weekOffset = (cal2.get(Calendar.DAY_OF_WEEK) -
		// cal1.get(Calendar.DAY_OF_WEEK)) < 0 ? 1 : 0;
		// int weekDiff = dateDiff / 7 + weekOffset;
		// int yearDiff = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
		// int monthDiff = yearDiff * 12 + cal2.get(Calendar.MONTH) -
		// cal1.get(Calendar.MONTH);
	}

	/**
	 * 將Date數據類型轉為Timestamp類型，時間是0點
	 * <p>
	 * <code>date2Timestamp</code>
	 * </p>
	 *
	 * @param date
	 *            Date
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

	/**
	 * 得到指定日期的第二天，且小時，分鐘，秒，毫秒都是0，如：2005-04-09 00:00:00.0
	 * <p>
	 * <code>getNextDayTimestamp</code>
	 * </p>
	 *
	 * @param date
	 *            Date
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

	/**
	 * 得到指定日期的第二天，且小時，分鐘，秒，毫秒都是0，如：2005-04-09 00:00:00.0
	 * <p>
	 * <code>getNextDayTimestamp</code>
	 * </p>
	 *
	 * @param date
	 *            Date
	 * @return Timestamp
	 * @author Joe 2004-8-15
	 * @since 1.0
	 */
	public Timestamp getNextDayTimestamp(Date date,int days) {
		Calendar cal = null;

		if (date != null) {
			cal = Calendar.getInstance();
			cal.setTime(date);
		}

		cal.add(Calendar.DAY_OF_YEAR, days);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return new Timestamp(cal.getTimeInMillis());
	}

	public Timestamp getNextMonthTimestamp(Date date) {
		Calendar cal = null;

		if (date != null) {
			cal = Calendar.getInstance();
			cal.setTime(date);
		}

		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return new Timestamp(cal.getTimeInMillis());
	}

	public Timestamp getNextMonthTimestamp(Date date,int mons) {
		Calendar cal = null;

		if (date != null) {
			cal = Calendar.getInstance();
			cal.setTime(date);
		}

		cal.add(Calendar.MONTH, mons);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return new Timestamp(cal.getTimeInMillis());
	}

	/**
	 * 取得當前日期的第二天的開始時間，如：2005-04-09 00:00:00.0
	 * <p>
	 * <code>getNextDayTimestamp</code>
	 * </p>
	 *
	 * @return
	 * @author Jason 2005-4-8
	 * @since 1.0
	 */
	public Timestamp getNextDayTimestamp() {
		return getNextDayTimestamp(getCurrentTimestamp());
	}

	public int compareYears(Timestamp startDate, Timestamp endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}
		String startYear = date2String(startDate).substring(0, 4);
		String endYear = date2String(endDate).substring(0, 4);
		if (StringUtils.isNotEmpty(startYear)
				&& StringUtils.isNumeric(startYear)
				&& StringUtils.isNotEmpty(endYear)
				&& StringUtils.isNumeric(endYear)) {
			return Integer.valueOf(endYear).intValue()
					- Integer.valueOf(startYear).intValue();
		}
		return 0;
	}

	public String getTanwanYear(Timestamp date) {
		if (date == null) {
			return "";
		}
		String year = date2String(date).substring(0, 4);
		if (StringUtils.isNotEmpty(year) && StringUtils.isNumeric(year)) {
			return Integer.toString(Integer.valueOf(year).intValue() - 1911);
		}
		return "";
	}

	public int getTanwanSeason(Timestamp date) {
		if (date == null) {
			return 0;
		}
		String year = date2String(date).substring(0, 4);
		Timestamp startDate = string2Timestamp(year + "/01/15");
		Timestamp endDate = string2Timestamp(year + "/03/16");
		if (date.after(startDate) && date.before(endDate)) {
			return 1;
		}
		startDate = string2Timestamp(year + "/03/15");
		endDate = string2Timestamp(year + "/06/16");
		if (date.after(startDate) && date.before(endDate)) {
			return 2;
		}
		startDate = string2Timestamp(year + "/06/15");
		endDate = string2Timestamp(year + "/09/16");
		if (date.after(startDate) && date.before(endDate)) {
			return 3;
		}
		return 4;
	}


    /**
     * 取得發票期別(民國年+雙數月)
     * @param currentTimestamp
     * @return
     */
    public String getYearMonth(Timestamp currentTimestamp) {
        String taiwanYear = getTanwanYear(currentTimestamp);
        String month = timestamp2String("MM", currentTimestamp);
        month = String.format("%2s", Integer.valueOf(month) + (Integer.valueOf(month) % 2)).replace(' ', '0');
        return taiwanYear + month;
    }


    public String getYearMonth(String invoiceDate){
		String taiwanYear = "";
		String year = invoiceDate.substring(0, 4);
		if (StringUtils.isNotEmpty(year) && StringUtils.isNumeric(year)) {
			taiwanYear = ""+ Integer.toString(Integer.valueOf(year).intValue() - 1911);
		}
		String month = invoiceDate.substring(4,6);
		month = String.format("%2s", Integer.valueOf(month) + (Integer.valueOf(month) % 2)).replace(' ', '0');
        return taiwanYear+month;
    }
    /**
     *
     * @param dateString
     * @param format
     * @return
     */
    public Date stringToDate(String dateString,String format){
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
     * @param date
     * @return
     */
    public Date getMonthOneDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE,1);
        return cal.getTime();
    }

    /**
     * 取得該月最後一天
     * @param date
     * @return
     */
    public Date getMonthLastDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public Date getLastDay(Date date){
        Calendar cal = date2Calendar(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

	public boolean isDateLine(){
		boolean dateLine = false;
		String today = getCurrentDateString("MMdd");
		if(Integer.parseInt(today.substring(0,2))%2==1){
			if(Integer.parseInt(today.substring(2))<=5){
				dateLine = true;
			}
		}
		return dateLine;
	}

	/**
	 * 取得該月第一天
	 * @param date
	 * @return
	 */
	public String getMonthOneDay(Timestamp date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE,1);

		return calendar2String("yyyyMMdd",cal,null);
	}

	/**
	 * 取得該月最後一天
	 * @param date
	 * @return
	 */
	public String getMonthLastDay(Timestamp date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar2String("yyyyMMdd",cal,null);
	}


	public Timestamp getDefaultDatebyTaiwanDate(String format,String date) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date1 = simpleDateFormat.parse(date);
		cal.setTime(date1);
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1911);
		return new Timestamp(cal.getTime().getTime());
	}

	public String getYearMonth2(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		month = month +1;
		if(month<10){
			return year + "0"+ month;
		}

		return year+""+month;
	}

	public String getYYYYMMDD(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		String strMonth = "";
		String strDay = "";

		month = month +1;
		if(month<10){
			strMonth =  "0"+ month;
		}
		if(day<10){
			strDay = "0" +day;
		}

		return ""+year+strMonth+strDay;
	}

	public String getYYYYMM(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		month = month +1;
		if(month<10){
			return year + "0"+ month;
		}

		return year+""+month;
	}

	public Date parseDate(String yyyymm){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(yyyymm.substring(0,4)));
		cal.set(Calendar.MONTH, Integer.parseInt(yyyymm.substring(4,6))-1);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		return cal.getTime();
	}

	public Date addMonth(Date date, int n){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}

	public Date getYearMonth2(String outYm){
		Date date = new Date();
		date.setYear(Integer.parseInt(outYm.substring(0,4)));
		date.setMonth(Integer.parseInt(outYm.substring(4,6)));
		return date;
	}

	public Date parseDateYYYY_MM_DD(String val){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(val.substring(0,4)));
		cal.set(Calendar.MONTH, Integer.parseInt(val.substring(5,7))-1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(val.substring(8, 10)));
		return cal.getTime();
	}

	public java.sql.Date parseDateYYYYMMDD(String str){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date parsed = null;
		try {
			parsed = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.sql.Date sql = new java.sql.Date(parsed.getTime());
		return sql;
	}

	public Timestamp stringToTimestamp(String str_date, String formate){

		DateFormat formatter;
		//formatter = new SimpleDateFormat("dd/MM/yyyy");
		formatter = new SimpleDateFormat(formate);
		// you can change format of date
		Date date = null;
		try{
			date = formatter.parse(str_date);
		} catch (ParseException e) {
			System.out.println(e);
			e.printStackTrace();
		}

		java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
		return timeStampDate;


	}


    public void main(String[] args) {

        System.out.println(isDateLine());
    }


}