package com.gateweb.utils.bean.typeConverter;

import com.gateweb.charge.enumeration.DeductStatus;
import org.apache.commons.beanutils.Converter;

public class DeductStatusEnumConverter implements Converter {
    @Override
    public <T> T convert(Class<T> aClass, Object o) {
        DeductStatus result = null;
        try {
            result = DeductStatus.valueOf(String.valueOf(o));
        } catch (Exception ex) {
            ex.getMessage();
        }
        return (T) result;
    }
}
