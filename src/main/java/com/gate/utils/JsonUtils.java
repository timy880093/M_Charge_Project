package com.gate.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eason on 3/29/2018.
 */
@Component
public class JsonUtils {
    Gson gson = new Gson();
    public <T> List<T> parseMultiSelectedValueJsonArray(String json, String columnName){
        List<T> resultList = new ArrayList<>();
        JsonArray jsonArray = gson.fromJson(json,JsonArray.class);
        for(int i=0;i<jsonArray.size();i++){
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            try{
                T value = (T)jsonObject.get(columnName).getAsString();
                resultList.add(value);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return resultList;
    }
}
