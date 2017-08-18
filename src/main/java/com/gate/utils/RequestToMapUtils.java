package com.gate.utils;

import com.gate.core.bean.BaseFormBean;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by simon on 2014/7/7.
 */
public class RequestToMapUtils {

    public BaseFormBean parserRequestToMap(HttpServletRequest request, BaseFormBean formBeanObject)
            throws Exception {
        Map parameterMap = request.getParameterMap();
        MapBeanConverterUtils.mapToBean(parameterMap,formBeanObject);
        return formBeanObject;
    }



}
