package com.gateweb.utils;

import com.gateweb.base.exception.NullPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.Optional;

public class ObjectUtil {
    protected static final Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    public static <T> void objectPropertyNullCheck(T object) throws NullPropertyException {
        boolean invalid = false;
        PropertyDescriptor[] propertyDescriptorList = BeanUtils.getPropertyDescriptors(object.getClass());
        for (PropertyDescriptor propertyDescriptor : propertyDescriptorList) {
            try {
                Optional<Object> valueOptional = Optional.ofNullable(propertyDescriptor.getReadMethod().invoke(object));
                if (!valueOptional.isPresent()) {
                    invalid = true;
                    break;
                }
            } catch (Exception e) {
                logger.error("Failed invoke ReadMethod:{}", e.getMessage());
            }
        }
        if (invalid) {
            throw new NullPropertyException();
        }
    }
}
