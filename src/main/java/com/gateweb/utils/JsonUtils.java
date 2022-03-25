package com.gateweb.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eason on 3/29/2018.
 */
public class JsonUtils {
    public <T> List<T> parseMultiSelectedValueJsonArray(String json, String columnName, Class clazz) {
        Gson gson = new Gson();
        List<T> resultList = new ArrayList<>();
        JsonArray jsonArray = gson.fromJson(json, JsonArray.class);
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            if (clazz.equals(String.class)) {
                resultList.add((T) jsonObject.get(columnName).getAsString());
            }
            if (clazz.equals(BigDecimal.class)) {
                resultList.add((T) jsonObject.get(columnName).getAsBigDecimal());
            }
            if (clazz.equals(Integer.class)) {
                resultList.add((T) new Integer(jsonObject.get(columnName).getAsInt()));
            }
            if (clazz.equals(BigInteger.class)) {
                resultList.add((T) jsonObject.get(columnName).getAsBigInteger());
            }
            if (clazz.equals(Double.class)) {
                resultList.add((T) new Double(jsonObject.get(columnName).getAsDouble()));
            }
        }
        return resultList;
    }

    public static String gsonToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static <T> T gsonFromJson(String json, Class<T> returnType) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.fromJson(json, returnType);
    }
}
