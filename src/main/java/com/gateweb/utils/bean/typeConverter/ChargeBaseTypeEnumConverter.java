package com.gateweb.utils.bean.typeConverter;

import com.gateweb.charge.enumeration.ChargeBaseType;
import org.apache.commons.beanutils.Converter;

public class ChargeBaseTypeEnumConverter implements Converter {
    @Override
    public <T> T convert(Class<T> aClass, Object o) {
        ChargeBaseType result = null;
        try {
            result = ChargeBaseType.valueOf(String.valueOf(o));
        } catch (Exception ex) {
            ex.getMessage();
        }
        return (T) result;
    }
}
