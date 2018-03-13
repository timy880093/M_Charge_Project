package com.gate.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eason on 3/13/2018.
 * 匯出Order格式的CSV檔案。
 */
@Component
public class CsvUtils {
    public List<String> combineBeanToList(Object mainObject, List<Object> detailObject,String separator){
        List<String> headerData = getCombineBeanHeaderData(mainObject.getClass(),detailObject.get(0).getClass());
        String[] headerDataArray = headerData.toArray(new String[]{});
        List<String[]> valueDataArray = getCombinedBeanValueData(mainObject,detailObject);
        List<String> resultData = new ArrayList<>();
        resultData.add(StringUtils.join(headerDataArray,separator));
        resultData.add(StringUtils.join(valueDataArray,separator));
        return resultData;
    }

    public List<String> getCombineBeanHeaderData(Class mainClazz,Class detailClazz) {
        List<String> beanHeaderList = new ArrayList<>();
        /**
         * 使用PropertiesDescriptor取得參數
         * 先取得mainClass的欄位名稱
         */
        try {
            BeanInfo mainBeanInfo = null;
            mainBeanInfo = Introspector.getBeanInfo(mainClazz);
            PropertyDescriptor mainClassDescriptors[] = mainBeanInfo.getPropertyDescriptors();
            for(PropertyDescriptor propertyDescriptor : mainClassDescriptors){
                beanHeaderList.add(propertyDescriptor.getName());
            }
            /**
             * 取得details欄位的名稱
             */
            BeanInfo detailBeanInfo = Introspector.getBeanInfo(detailClazz);
            PropertyDescriptor detailClassDescriptors[] = detailBeanInfo.getPropertyDescriptors();
            for(PropertyDescriptor propertyDescriptor : detailClassDescriptors){
                beanHeaderList.add(propertyDescriptor.getName());
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return beanHeaderList;
    }

    public List<String[]> getCombinedBeanValueData(Object mainObject, List<Object> detailObjectList){
        List<String[]> resultList = new ArrayList<>();
        List<String> mainObjectValueList = objectListToStringList(getBeanValueData(mainObject));
        for(Object detailObject : detailObjectList){
            List<String> detailObjectValueList = objectListToStringList(getBeanValueData(detailObject));
            List<String> combinedObjectValueList = new ArrayList<>();
            combinedObjectValueList.addAll(mainObjectValueList);
            combinedObjectValueList.addAll(detailObjectValueList);
            resultList.add(combinedObjectValueList.toArray(new String[]{}));
        }
        return resultList;
    }

    public List<Object> getBeanValueData(Object object){
        List<Object> beanValueList = new ArrayList<>();
        /**
         * 使用PropertiesDescriptor取得參數。
         */
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor descriptors[] = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor propertyDescriptor : descriptors){
                Object value = PropertyUtils.getProperty(object,propertyDescriptor.getName());
                beanValueList.add(value);
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return beanValueList;
    }

    /**
     * 根據各個型別進行轉換。
     * @param objectList
     * @return
     */
    public List<String> objectListToStringList(List<Object> objectList){
        List<String> resultList = new ArrayList<>();
        for(Object object: objectList){

            if(object instanceof BigDecimal){
                //去掉多餘的零
                BigDecimal decimalValue = (BigDecimal) object;
                resultList.add(decimalValue.stripTrailingZeros().toPlainString());
            }

            if(object instanceof String){
                resultList.add(object.toString());
            }
        }
        return resultList;
    }
}
