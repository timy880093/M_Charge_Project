package com.gateweb.utils.bean.typeConverter;

import com.gateweb.charge.enumeration.ContractStatus;
import org.apache.commons.beanutils.Converter;

public class ContractStatusEnumConverter implements Converter {

    @Override
    public <T> T convert(Class<T> aClass, Object o) {
        ContractStatus result = null;
        try {
            result = ContractStatus.valueOf(String.valueOf(o));
        } catch (Exception ex) {
            ex.getMessage();
        }
        return (T) result;
    }
}
