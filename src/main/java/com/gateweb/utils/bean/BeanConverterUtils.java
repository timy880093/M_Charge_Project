package com.gateweb.utils.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateweb.charge.enumeration.*;
import com.gateweb.utils.LocalDateTimeUtils;
import com.gateweb.utils.bean.typeConverter.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Eason on 3/8/2018.
 */
public class BeanConverterUtils {
    protected final Logger logger = LogManager.getLogger(getClass());
    protected Gson gson = new Gson();

    /**
     * remove null value
     *
     * @param object
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public Map beanToMap(Object object) throws IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        Map resultMap = BeanUtils.describe(object);
        Set<Object> removableKeySet = new HashSet<>();
        resultMap.keySet().stream().forEach(key -> {
            if (resultMap.get(key) == null) {
                removableKeySet.add(key);
            }
        });
        removableKeySet.stream().forEach(removableKey -> {
            resultMap.remove(removableKey);
        });
        return resultMap;
    }

    /**
     * todo: null check
     *
     * @param json
     * @param returnType
     * @param <T>
     * @return
     */
    public <T> Optional<T> convertJsonToObj(String json, Class<T> returnType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return Optional.of(objectMapper.readValue(json, returnType));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> Optional<T> convertJsonToObjWithTypeCast(String json, Class<T> returnType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Map map = convertJsonToMap(json);
            T result = (T) mapToBean(map, returnType);
            return Optional.of(result);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public HashMap<String, Object> convertJsonToMap(String json) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        json = StringEscapeUtils.unescapeJava(json);
        return gson.fromJson(json, type);
    }

    public List<Object> convertJsonToList(String json) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        Type type = new TypeToken<List<Object>>() {
        }.getType();
        json = clearEscapeChar(json);
        return gson.fromJson(json, type);
    }

    @Deprecated
    public String clearEscapeChar(String json) {
        String result = json;
        result = result.replace("\\\"", "");
        result = result.replace("\"", "");
        return result;
    }

    /**
     * Map轉換層Bean，使用泛型免去了型別轉換的麻煩。
     *
     * @param <T>
     * @param map
     * @param clazz
     * @return
     */
    public <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        T bean = null;
        try {
            bean = clazz.newInstance();
            ConvertUtils.register(new ContractStatusEnumConverter(), ContractStatus.class);
            ConvertUtils.register(new PaidPlanEnumConverter(), PaidPlan.class);
            ConvertUtils.register(new ChargePlanEnumConverter(), ChargePlan.class);
            ConvertUtils.register(new ChargeBaseTypeEnumConverter(), ChargeBaseType.class);
            ConvertUtils.register(new CycleTypeEnumConverter(), CycleType.class);
            ConvertUtils.register(new BillingItemTypeEnumConverter(), BillingItemType.class);
            ConvertUtils.register(new BillStatusEnumConverter(), BillStatus.class);
            ConvertUtils.register(new DeductStatusEnumConverter(), DeductStatus.class);
            ConvertUtils.register(new DeductTypeEnumConverter(), DeductType.class);
            ConvertUtils.register(new PaymentMethodEnumConverter(), PaymentMethod.class);
            ConvertUtils.register(new LocalDateTimeConverter(), LocalDateTime.class);
            BeanUtils.populate(bean, map);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return bean;
    }

    @Deprecated
    public <T> T typeProcessor(Class<T> targetClass, Object obj) {
        try {
            if (!Optional.ofNullable(obj).isPresent()) {
                return null;
            }
            Class sourceClass = obj.getClass();
            if (sourceClass.equals(targetClass)) return (T) obj;
            if (targetClass.isEnum()) {
                obj = enumTypeProcessor(targetClass, sourceClass, obj);
            } else {
                if (sourceClass.equals(String.class)) {
                    String str = String.valueOf(obj);
                    obj = stringTypeProcessor(targetClass, str);
                }
                if (sourceClass.equals(Double.class)) {
                    Double doubleValue = (Double) obj;
                    obj = doubleTypeProcessor(targetClass, doubleValue);
                }
                if (sourceClass.equals(Long.class)) {
                    Long longValue = (Long) obj;
                    obj = longTypeProcessor(targetClass, longValue);
                }
                if (sourceClass.equals(Integer.class) && targetClass.equals(String.class)) {
                    Integer intValue = (Integer) obj;
                    obj = intValue.toString();
                }
            }
            return targetClass.cast(obj);
        } catch (ClassCastException e) {
            logger.info(e.getMessage() + ",data:" + obj.toString());
            return null;
        }
    }

    @Deprecated
    public <T> T stringTypeProcessor(Class<T> targetClass, String str) {
        Object result = null;
        if (!str.isEmpty()) {
            if (targetClass.equals(BigDecimal.class)) {
                result = new BigDecimal(str);
            }
            if (targetClass.equals(Boolean.class)) {
                result = Boolean.valueOf(str);
            }
            if (targetClass.equals(Long.class)) {
                result = Long.valueOf(str);
            }
            if (targetClass.equals(Integer.class)) {
                result = Integer.valueOf(str);
            }
            if (targetClass.equals(LocalDate.class)) {
                Optional<LocalDate> localDateOptional = Optional.empty();
                //use / as separator
                if (str.indexOf('/') > 0) {
                    localDateOptional = LocalDateTimeUtils.parseLocalDateFromString(str, "yyyy/MM/dd");
                }
                if (localDateOptional.isPresent()) {
                    result = localDateOptional.get();
                }
            }
            if (targetClass.equals(LocalDateTime.class)) {
                //不處理這種類型
            }
        }
        return (T) result;
    }

    public <T> T enumTypeProcessor(Class<T> targetClass, Class sourceClass, Object obj) {
        Object result = null;
        //轉enum
        for (T to : targetClass.getEnumConstants()) {
            if (sourceClass.equals(String.class)) {
                //string相關的轉法
                String str = String.valueOf(obj);
                if (str.equals(to.toString())) {
                    result = to;
                    break;
                }
            }
        }
        return (T) result;
    }

    public <T> T doubleTypeProcessor(Class<T> targetClass, Double doubleValue) {
        Object result = null;
        if (targetClass.equals(Long.class)) {
            result = doubleValue.longValue();
        }
        if (targetClass.equals(Integer.class)) {
            result = doubleValue.intValue();
        }
        if (targetClass.equals(BigDecimal.class)) {
            result = BigDecimal.valueOf(doubleValue);
        }
        if (targetClass.equals(LocalDateTime.class)) {
            Timestamp t = new Timestamp(doubleValue.longValue());
            result = t.toLocalDateTime();
        }
        return (T) result;
    }

    public <T> T longTypeProcessor(Class<T> targetClass, Long longValue) {
        Object result = null;
        if (targetClass.equals(Double.class)) {
            result = longValue.doubleValue();
        }
        return (T) result;
    }
}
