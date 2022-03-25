package com.gateweb.utils.bean.typeConverter;

import com.gateweb.charge.enumeration.ChargePlan;
import org.apache.commons.beanutils.Converter;

public class ChargePlanEnumConverter implements Converter {
    @Override
    public <T> T convert(Class<T> aClass, Object o) {
        ChargePlan result = null;
        try {
            result = ChargePlan.valueOf(String.valueOf(o));
        } catch (Exception ex) {
            ex.getMessage();
        }
        return (T) result;
    }
}
