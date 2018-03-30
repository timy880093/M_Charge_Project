package com.gate.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eason on 3/29/2018.
 */
@Component
public class JsonUtils {
    Gson gson = new Gson();
    public <T> List<T> parseMultiSelectedValueJsonArray(String json, String columnName,Class clazz){
        List<T> resultList = new ArrayList<>();
        JsonArray jsonArray = gson.fromJson(json,JsonArray.class);
        for(int i=0;i<jsonArray.size();i++){
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            if(clazz.equals(String.class)){
                resultList.add((T) jsonObject.get(columnName).getAsString());
            }
            if(clazz.equals(BigDecimal.class)){
                resultList.add((T) jsonObject.get(columnName).getAsBigDecimal());
            }
            if(clazz.equals(Integer.class)){
                resultList.add((T) new Integer(jsonObject.get(columnName).getAsInt()));
            }
            if(clazz.equals(BigInteger.class)){
                resultList.add((T) jsonObject.get(columnName).getAsBigInteger());
            }
            if(clazz.equals(Double.class)){
                resultList.add((T) new Double(jsonObject.get(columnName).getAsDouble()));
            }
        }
        return resultList;
    }

}
