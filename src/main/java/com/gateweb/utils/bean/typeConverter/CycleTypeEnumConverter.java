package com.gateweb.utils.bean.typeConverter;

import com.gateweb.charge.enumeration.CycleType;
import org.apache.commons.beanutils.Converter;

public class CycleTypeEnumConverter implements Converter {

    @Override
    public <T> T convert(Class<T> aClass, Object o) {
        CycleType result = null;
        try {
            result = CycleType.valueOf(String.valueOf(o));
        } catch (Exception ex) {
            ex.getMessage();
        }
        return (T) result;
    }
}
