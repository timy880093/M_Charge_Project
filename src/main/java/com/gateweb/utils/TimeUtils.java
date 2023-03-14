package com.gateweb.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
}