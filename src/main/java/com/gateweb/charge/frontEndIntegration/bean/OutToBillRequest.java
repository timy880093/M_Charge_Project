package com.gateweb.charge.frontEndIntegration.bean;

import java.io.Serializable;

public class OutToBillRequest implements Serializable {
    boolean minimumFeeCheck = true;
    String billRemark;
    String billYm;

    BillingItemSearchCondition condition;

    public OutToBillRequest() {
    }

    public OutToBillRequest(boolean minimumFeeCheck, String billRemark, String billYm, BillingItemSearchCondition condition) {
        this.minimumFeeCheck = minimumFeeCheck;
        this.billRemark = billRemark;
        this.billYm = billYm;
        this.condition = condition;
    }

    public BillingItemSearchCondition getCondition() {
        return condition;
    }

    public void setCondition(BillingItemSearchCondition condition) {
        this.condition = condition;
    }

    public String getBillRemark() {
        return billRemark;
    }

    public void setBillRemark(String billRemark) {
        this.billRemark = billRemark;
    }

    public String getBillYm() {
        return billYm;
    }

    public void setBillYm(String billYm) {
        this.billYm = billYm;
    }

    public boolean isMinimumFeeCheck() {
        return minimumFeeCheck;
    }

    public void setMinimumFeeCheck(boolean minimumFeeCheck) {
        this.minimumFeeCheck = minimumFeeCheck;
    }

}
