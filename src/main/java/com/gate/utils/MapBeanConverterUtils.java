package com.gate.utils;


import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by simon on 2014/7/4.
 */
public class MapBeanConverterUtils {


    public static void mapToBean(Map dataMap, Object object) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.populate(object, dataMap);
    }

    public static Map beanToMap(Object object) throws IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        return BeanUtils.describe(object);
    }



}
