package com.gateweb.utils.bean.typeConverter;

import org.apache.commons.beanutils.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeConverter implements Converter {
    @Override
    public <T> T convert(Class<T> aClass, Object o) {
        LocalDateTime result = null;
        try {
            if (o instanceof Long) {
                result = Instant.ofEpochMilli(
                        Long.valueOf((String.valueOf(o)))).atZone(ZoneId.systemDefault()).toLocalDateTime();
            } else if (o instanceof Double) {
                result = Instant.ofEpochMilli(
                        Double.valueOf(String.valueOf(o)).longValue()
                ).atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return (T) result;
    }
}
