package com.gate.web.beans;

import java.util.List;

import com.gateweb.charge.model.CashMasterEntity;

/**
 * Created by emily on 2016/1/18.
 */
public class CashMasterBean extends CashMasterEntity{
    private String companyName;
    private String businessNo;
    private String codeName;
    private List<CashDetailBean> cashDetailList;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public List<CashDetailBean> getCashDetailList() {
        return cashDetailList;
    }

    public void setCashDetailList(List<CashDetailBean> cashDetailList) {
        this.cashDetailList = cashDetailList;
    }
}
