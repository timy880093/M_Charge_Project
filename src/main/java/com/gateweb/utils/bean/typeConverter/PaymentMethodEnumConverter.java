package com.gateweb.utils.bean.typeConverter;

import com.gateweb.charge.enumeration.PaymentMethod;
import org.apache.commons.beanutils.Converter;

public class PaymentMethodEnumConverter implements Converter {
    @Override
    public <T> T convert(Class<T> aClass, Object o) {
        PaymentMethod result = null;
        try {
            result = PaymentMethod.valueOf(String.valueOf(o));
        } catch (Exception ex) {
            ex.getMessage();
        }
        return (T) result;
    }
}
