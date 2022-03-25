package com.gateweb.utils.bean.typeConverter;

import com.gateweb.charge.enumeration.BillingItemType;
import org.apache.commons.beanutils.Converter;

public class BillingItemTypeEnumConverter implements Converter {
    @Override
    public <T> T convert(Class<T> aClass, Object o) {
        BillingItemType result = null;
        try {
            result = BillingItemType.valueOf(String.valueOf(o));
        } catch (Exception ex) {
            ex.getMessage();
        }
        return (T) result;
    }
}
