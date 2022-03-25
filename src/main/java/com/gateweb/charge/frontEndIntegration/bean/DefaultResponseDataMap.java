package com.gateweb.charge.frontEndIntegration.bean;

import java.util.HashMap;
import java.util.Map;

public class DefaultResponseDataMap {
    public static Map invalidUserResponseMap() {
        Map dataMap = new HashMap();
        dataMap.put("status", "error");
        dataMap.put("title", "錯誤的使用者");
        dataMap.put("message", "未查詢到使用者的基本資料");
        return dataMap;
    }
}
