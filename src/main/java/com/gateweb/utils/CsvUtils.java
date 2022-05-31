package com.gateweb.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CsvUtils {
    private static Logger logger = LogManager.getLogger(CsvUtils.class);

    public static void objectListToCsvOutputStream(List objectList, OutputStream outputStream) {
        try {
            List<String> headerValueList = genBeanHeaderData(objectList.get(0).getClass(), new ArrayList<>());
            String headerStringData = dataListToCsvLineData(headerValueList, ",");
            List<String> detailStringDataList = new ArrayList<>();
            //後續明細資料
            for (Object obj : objectList) {
                List<Object> beanDataObjectList = genBeanValueData(obj, new ArrayList<>());
                List<String> beanDataArrayList = objectListToStringList(beanDataObjectList);
                String detailStringData = dataListToCsvLineData(beanDataArrayList, ",");
                detailStringDataList.add(detailStringData);
            }
            List<String> summaryStringDataList = new ArrayList<>();
            summaryStringDataList.add(headerStringData);
            summaryStringDataList.addAll(detailStringDataList);
            FileUtils.writeTextFileToOutputStream(outputStream, summaryStringDataList);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void stringListToCsvOutputStream(List<String> stringList, OutputStream outputStream) {
        try {
            FileUtils.writeTextFileToOutputStream(outputStream, stringList);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static List<String> mapListToCsvLineData(
            List<String> headerList
            , List<HashMap<String, String>> dataMapList
            , String separator) {
        List<String> resultList = new ArrayList<>();
        dataMapList.stream().forEach(dataMap -> {
            resultList.add(mapToCsvLineData(headerList, dataMap, separator));
        });
        return resultList;
    }

    private static String mapToCsvLineData(
            List<String> headerList
            , HashMap<String, String> dataMap
            , String separator) {
        List<String> dataList = new ArrayList<>();
        headerList.stream().forEach(header -> {
            if (dataMap.containsKey(header)) {
                dataList.add(dataMap.get(header));
            } else {
                dataList.add("");
            }
        });
        return StringUtils.join(dataList, separator);
    }

    public static List<String> combineBeanToList(
            Object mainObject
            , List<String> excludeMainParameterNameList
            , List detailObject
            , List<String> excludeDetailParameterNameList
            , String separator) {
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
        resultData.add(StringUtils.join(headerDataArray, separator));
        for (String[] lineDataList : valueDataArray) {
            resultData.add(StringUtils.join(lineDataList, separator));
        }
        return resultData;
    }

    private static List<String> getCombineBeanHeaderData(
            Class mainClazz
            , List<String> excludeMainParameterNameList
            , Class detailClazz
            , List<String> excludeDetailParameterNameList) {
        List<String> beanHeaderList = new ArrayList<>();
        /**
         * 使用PropertiesDescriptor取得參數
         * 先取得mainClass的欄位名稱
         */
        beanHeaderList.addAll(genBeanHeaderData(mainClazz, excludeMainParameterNameList));
        /**
         * 取得details欄位的名稱
         */
        beanHeaderList.addAll(genBeanHeaderData(detailClazz, excludeDetailParameterNameList));
        return beanHeaderList;
    }

    private static List<String> genBeanHeaderData(Class clazz, List<String> excludeParameterNameList) {
        List<String> resultList = new ArrayList<>();
        if (excludeParameterNameList == null) {
            excludeParameterNameList = new ArrayList<>();
        }

        BeanInfo detailBeanInfo = null;
        try {
            detailBeanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor detailClassDescriptors[] = detailBeanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : detailClassDescriptors) {
                if (excludeParameterNameList.contains(propertyDescriptor.getName())) {
                    continue;
                }
                if (propertyDescriptor.getName().equals("class")) {
                    continue;
                }
                resultList.add(propertyDescriptor.getName());
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private static List<String[]> getCombinedBeanValueData(
            Object mainObject
            , List<String> excludeMainParameterName
            , List<Object> detailObjectList
            , List<String> excludeDetailParameterName) {
        List<String[]> resultList = new ArrayList<>();
        List<String> mainObjectValueList
                = objectListToStringList(genBeanValueData(mainObject, excludeMainParameterName));
        for (Object detailObject : detailObjectList) {
            List<String> detailObjectValueList
                    = objectListToStringList(genBeanValueData(detailObject, excludeDetailParameterName));
            List<String> combinedObjectValueList = new ArrayList<>();
            combinedObjectValueList.addAll(mainObjectValueList);
            combinedObjectValueList.addAll(detailObjectValueList);
            resultList.add(combinedObjectValueList.toArray(new String[]{}));
        }
        return resultList;
    }

    private static List<Object> genBeanValueData(Object object, List<String> excludeParameterName) {
        List<Object> beanValueList = new ArrayList<>();
        /**
         * 使用PropertiesDescriptor取得參數。
         */
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor descriptors[] = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : descriptors) {
                if (!excludeParameterName.contains(propertyDescriptor.getName())) {
                    try {
                        Object value = PropertyUtils.getProperty(object, propertyDescriptor.getName());
                        beanValueList.add(value);
                    } catch (Exception ex) {
                        logger.error(ex.getMessage());
                        beanValueList.add("");
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return beanValueList;
    }

    /**
     * 根據各個型別進行轉換。
     *
     * @param objectList
     * @return
     */
    private static List<String> objectListToStringList(List<Object> objectList) {
        List<String> resultList = new ArrayList<>();
        for (Object object : objectList) {

            if (object instanceof BigDecimal) {
                //去掉多餘的零
                BigDecimal decimalValue = (BigDecimal) object;
                resultList.add(decimalValue.stripTrailingZeros().toPlainString());
            } else if (object == null) {
                resultList.add("");
            } else if (object instanceof Class) {
                //過瀘裡頭包含的Class參數，不加進參數中。
            } else {
                resultList.add(String.valueOf(object));
            }
        }
        return resultList;
    }

    /**
     * 將dataList轉換成為一行csv資料
     *
     * @param valueList
     * @param separator
     * @return
     */
    private static String dataListToCsvLineData(List<String> valueList, String separator) {
        return StringUtils.join(valueList.toArray(new String[]{}), separator);
    }

}
