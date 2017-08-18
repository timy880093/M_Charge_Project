package com.gate.web.formbeans;

import com.gate.core.bean.BaseFormBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emily on 2016/6/15.
 */
public class WarrantyBean extends BaseFormBean {

    private String warrantyId;
    private String warrantyNo;
    private String companyId;
    private String extend;
    private String startDate;
    private String endDate;
    private String model;
    private String note;
    private String status;
    private String dealerCompanyId;
    private String onlyShip;

    public String getWarrantyId() {
        return warrantyId;
    }

    public void setWarrantyId(String warrantyId) {
        this.warrantyId = warrantyId;
    }

    public String getWarrantyNo() {
        return warrantyNo;
    }

    public void setWarrantyNo(String warrantyNo) {
        this.warrantyNo = warrantyNo;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDealerCompanyId() {
        return dealerCompanyId;
    }

    public void setDealerCompanyId(String dealerCompanyId) {
        this.dealerCompanyId = dealerCompanyId;
    }

    public String getOnlyShip() {
        return onlyShip;
    }

    public void setOnlyShip(String onlyShip) {
        this.onlyShip = onlyShip;
    }

    @Override
    public List<String> validateData() {
        System.out.println("In validation!!");
        //如果沒有錯誤回傳空的ArrayList
        return new ArrayList();
    }
}
