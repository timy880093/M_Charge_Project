package com.gate.web.beans;

import com.gateweb.charge.model.CashDetailEntity;

/**
 * Created by emily on 2016/1/25.
 */
public class CashDetailBean extends CashDetailEntity{
    private String packageName;
    private Integer chargeId;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }
}
