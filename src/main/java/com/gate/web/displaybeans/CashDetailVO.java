package com.gate.web.displaybeans;

import com.gateweb.charge.model.CashDetailEntity;

/**
 * Created by emily on 2016/1/6.
 */
public class CashDetailVO extends CashDetailEntity{
    private String detailStatus;
    private String companyName;
    private String cashTypeName;
    private String packageName;
    private String period;
    private String taxTypeName;
    private Integer masterStatus;

    public String getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(String detailStatus) {
        this.detailStatus = detailStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCashTypeName() {
        return cashTypeName;
    }

    public void setCashTypeName(String cashTypeName) {
        this.cashTypeName = cashTypeName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTaxTypeName() {
        return taxTypeName;
    }

    public void setTaxTypeName(String taxTypeName) {
        this.taxTypeName = taxTypeName;
    }

    public Integer getMasterStatus() {
        return masterStatus;
    }

    public void setMasterStatus(Integer masterStatus) {
        this.masterStatus = masterStatus;
    }
}
