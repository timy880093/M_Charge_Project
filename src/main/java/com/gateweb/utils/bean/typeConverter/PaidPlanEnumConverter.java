package com.gateweb.utils.bean.typeConverter;

import com.gateweb.charge.enumeration.PaidPlan;
import org.apache.commons.beanutils.Converter;

public class PaidPlanEnumConverter implements Converter {
    @Override
    public <T> T convert(Class<T> aClass, Object o) {
        PaidPlan result = null;
        try {
            result = PaidPlan.valueOf(String.valueOf(o));
        } catch (Exception ex) {
            ex.getMessage();
        }
        return (T) result;
    }
}
