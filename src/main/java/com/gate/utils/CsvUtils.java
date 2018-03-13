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
    public List<String> combineBeanToList(
            Object mainObject
            , List<String> excludeMainParameterNameList
            , List detailObject
            , List<String> excludeDetailParameterNameList
            , String separator){
        List<String> headerData = getCombineBeanHeaderData(
                    mainObject.getClass()
                    , excludeMainParameterNameList
                    , detailObject.get(0).getClass()
                    , excludeDetailParameterNameList
        );
        String[] headerDataArray = headerData.toArray(new String[]{});
        List<String[]> valueDataArray = getCombinedBeanValueData(
                mainObject
                , excludeMainParameterNameList
                , detailObject
                , excludeDetailParameterNameList
        );
        List<String> resultData = new ArrayList<>();
        resultData.add(StringUtils.join(headerDataArray,separator));
        for(String[] lineDataList : valueDataArray){
            resultData.add(StringUtils.join(lineDataList,separator));
        }
        return resultData;
    }

    public List<String> getCombineBeanHeaderData(
            Class mainClazz
            , List<String> excludeMainParameterNameList
            , Class detailClazz
            , List<String> excludeDetailParameterNameList) {
        List<String> beanHeaderList = new ArrayList<>();
        /**
         * 使用PropertiesDescriptor取得參數
         * 先取得mainClass的欄位名稱
         */
        beanHeaderList.addAll(getBeanHeaderData(mainClazz,excludeMainParameterNameList));
        /**
         * 取得details欄位的名稱
         */
        beanHeaderList.addAll(getBeanHeaderData(detailClazz,excludeDetailParameterNameList));
        return beanHeaderList;
    }

    public List<String> getBeanHeaderData(Class clazz, List<String> excludeParameterNameList){
        List<String> resultList = new ArrayList<>();

        BeanInfo detailBeanInfo = null;
        try {
            detailBeanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor detailClassDescriptors[] = detailBeanInfo.getPropertyDescriptors();
            for(PropertyDescriptor propertyDescriptor : detailClassDescriptors){
                if(excludeParameterNameList.contains(propertyDescriptor.getName())){
                    continue;
                }
                if(propertyDescriptor.getName().equals("class")){
                    continue;
                }
                resultList.add(propertyDescriptor.getName());
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } finally {
            return resultList;
        }
    }

    public List<String[]> getCombinedBeanValueData(
            Object mainObject
            , List<String> excludeMainParameterName
            , List<Object> detailObjectList
            , List<String> excludeDetailParameterName){
        List<String[]> resultList = new ArrayList<>();
        List<String> mainObjectValueList
                = objectListToStringList(getBeanValueData(mainObject,excludeMainParameterName));
        for(Object detailObject : detailObjectList){
            List<String> detailObjectValueList
                    = objectListToStringList(getBeanValueData(detailObject,excludeDetailParameterName));
            List<String> combinedObjectValueList = new ArrayList<>();
            combinedObjectValueList.addAll(mainObjectValueList);
            combinedObjectValueList.addAll(detailObjectValueList);
            resultList.add(combinedObjectValueList.toArray(new String[]{}));
        }
        return resultList;
    }

    public List<Object> getBeanValueData(Object object,List<String> excludeParameterName){
        List<Object> beanValueList = new ArrayList<>();
        /**
         * 使用PropertiesDescriptor取得參數。
         */
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor descriptors[] = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor propertyDescriptor : descriptors){
                if(!excludeParameterName.contains(propertyDescriptor.getName())){
                    Object value = PropertyUtils.getProperty(object,propertyDescriptor.getName());
                    beanValueList.add(value);
                }
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
            }else if(object==null){
                resultList.add("");
            }else if(object instanceof  Class){
                //過瀘裡頭包含的Class參數，不加進參數中。
            }else{
                resultList.add(object.toString());
            }
        }
        return resultList;
    }

}
