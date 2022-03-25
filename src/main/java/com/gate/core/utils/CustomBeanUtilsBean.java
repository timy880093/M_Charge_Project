package com.gate.core.utils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by simon on 2014/7/2.
 */
public class CustomBeanUtilsBean {

    private static BeanUtilsBean beanUtilsBean = null;

    private CustomBeanUtilsBean() {
    }

    /**
     * 型別預設值註冊,請參考一下資料
     * http://commons.apache.org/proper/commons-beanutils/javadocs/v1.8.3/apidocs/index.html
     * org.apache.commons.beanutilsClass ConvertUtilsBean
     */
    public static void register() {
        if (beanUtilsBean == null) {
            Date defaultValue = null;
            Timestamp defaultTimestampValue = null;
            BigDecimal defaultBigDecimalValue = null;
            Float defaultFloatValue = null;
            Boolean defaultBooleanValue = null;
            Date defaultDateValue = null;
            BigInteger defaultBigIntegerValue = null;
            Time defaultTimeValue = null;
            //2013/11/06 added
            Short defaultShortValue = null;
            Double defaultDoubleValue = null;
            Integer defaultIntegerValue = null;
            Long defaultLongValue = null;
            java.sql.Date defaultSqlDateValue = null;

            Converter converter = new DateConverter(defaultValue);
            Converter sqlTimestampConverter = new SqlTimestampConverter(defaultTimestampValue);
            Converter bigDecimalConverter = new BigDecimalConverter(defaultBigDecimalValue);
            Converter floatConverter = new FloatConverter(defaultFloatValue);
            Converter booleanConverter = new BooleanConverter(defaultBooleanValue);
            Converter dateConverter = new DateConverter(defaultDateValue);
            Converter bigIntegerConverter = new BigIntegerConverter(defaultBigIntegerValue);
            Converter sqlTimeConverter = new SqlTimeConverter(defaultTimeValue);
            //2013/11/06 added
            Converter shortConverter = new ShortConverter(defaultShortValue);
            Converter doubleConverter = new DoubleConverter(defaultDoubleValue);
            Converter integerConverter = new IntegerConverter(defaultIntegerValue);
            Converter longConverter = new LongConverter(defaultLongValue);
            //2014/7/14 added
            Converter sqlDateConverter = new SqlDateConverter(defaultSqlDateValue);

            beanUtilsBean = BeanUtilsBean.getInstance();
            //先re-establish
            beanUtilsBean.getConvertUtils().deregister();
            beanUtilsBean.getConvertUtils().register(converter, java.util.Date.class);
            beanUtilsBean.getConvertUtils().register(sqlTimestampConverter, Timestamp.class);
            beanUtilsBean.getConvertUtils().register(bigDecimalConverter, BigDecimal.class);
            beanUtilsBean.getConvertUtils().register(floatConverter, Float.class);
            beanUtilsBean.getConvertUtils().register(booleanConverter, Boolean.class);
            beanUtilsBean.getConvertUtils().register(dateConverter, Date.class);
            beanUtilsBean.getConvertUtils().register(bigIntegerConverter, BigInteger.class);
            beanUtilsBean.getConvertUtils().register(sqlTimeConverter, Time.class);
            //2013/11/06 added
            beanUtilsBean.getConvertUtils().register(shortConverter, Short.class);
            beanUtilsBean.getConvertUtils().register(doubleConverter, Double.class);
            beanUtilsBean.getConvertUtils().register(integerConverter, Integer.class);
            beanUtilsBean.getConvertUtils().register(longConverter, Long.class);
            //2014/7/14 added
            beanUtilsBean.getConvertUtils().register(sqlDateConverter, java.sql.Date.class);
        }
    }
}
