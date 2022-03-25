package com.gateweb.utils.bean.typeConverter;

import com.gateweb.charge.enumeration.BillStatus;
import org.apache.commons.beanutils.Converter;

public class BillStatusEnumConverter implements Converter {
    @Override
    public <T> T convert(Class<T> aClass, Object o) {
        BillStatus result = null;
        try {
            result = BillStatus.valueOf(String.valueOf(o));
        } catch (Exception ex) {
            ex.getMessage();
        }
        return (T) result;
    }
}
