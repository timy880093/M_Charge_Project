package com.gateweb.charge.frontEndIntegration.bean;

import com.fasterxml.jackson.databind.JsonNode;

public class OperationObject {
    JsonNode condition;
    String idList;
    JsonNode custom;

    public OperationObject() {
    }

    public OperationObject(JsonNode condition, String idList, JsonNode custom) {
        this.condition = condition;
        this.idList = idList;
        this.custom = custom;
    }

    public JsonNode getCondition() {
        return condition;
    }

    public void setCondition(JsonNode condition) {
        this.condition = condition;
    }

    public String getIdList() {
        return idList;
    }

    public void setIdList(String idList) {
        this.idList = idList;
    }

    public JsonNode getCustom() {
        return custom;
    }

    public void setCustom(JsonNode custom) {
        this.custom = custom;
    }

    @Override
    public String toString() {
        return "OperationObject{" +
                "condition=" + condition +
                ", idList='" + idList + '\'' +
                ", custom=" + custom +
                '}';
    }
}
