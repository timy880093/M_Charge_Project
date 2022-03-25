package com.gateweb.utils.bean.typeConverter;

import com.gateweb.charge.enumeration.DeductType;
import org.apache.commons.beanutils.Converter;

public class DeductTypeEnumConverter implements Converter {
    @Override
    public <T> T convert(Class<T> aClass, Object o) {
        DeductType result = null;
        try {
            result = DeductType.valueOf(String.valueOf(o));
        } catch (Exception ex) {
            ex.getMessage();
        }
        return (T) result;
    }
}
