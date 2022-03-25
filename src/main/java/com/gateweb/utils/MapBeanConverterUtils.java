package com.gateweb.utils;


import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by simon on 2014/7/4.
 */
@Deprecated
public class MapBeanConverterUtils {
    public void mapToBean(Map dataMap, Object object) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.populate(object, dataMap);
    }
}
