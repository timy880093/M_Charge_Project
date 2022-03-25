package com.gateweb.charge.frontEndIntegration.bean;

import java.util.HashMap;

public class AjaxRequest {
    HashMap<String, Object> condition = new HashMap<>();
    HashMap<String, Object> action = new HashMap<>();

    public AjaxRequest() {
    }

    public AjaxRequest(HashMap<String, Object> condition, HashMap<String, Object> action) {
        this.condition = condition;
        this.action = action;
    }

    public HashMap<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(HashMap<String, Object> condition) {
        this.condition = condition;
    }

    public HashMap<String, Object> getAction() {
        return action;
    }

    public void setAction(HashMap<String, Object> action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "AjaxRequest{" +
                "condition=" + condition +
                ", action=" + action +
                '}';
    }
}
