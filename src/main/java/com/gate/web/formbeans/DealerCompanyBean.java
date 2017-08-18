package com.gate.web.formbeans;

/**
 * Created by emily on 2015/12/14.
 */
public class DealerCompanyBean {
    private Integer dealerCompanyId;
    private String dealerCompanyName;
    private String businessNo;
    private String phone;
    private String fax;
    private String companyAddress;
    private String email;
    private String commissionType;
    private String mainPercent;
    private String mainAmount;
    private String collectMoney;
    private String additionPercent;
    private String status;

    private Integer dealerId[];
    private String dealerName[];
    private String dealerPhone[];
    private String dealerEmail[];

    public Integer getDealerCompanyId() {
        return dealerCompanyId;
    }

    public void setDealerCompanyId(Integer dealerCompanyId) {
        this.dealerCompanyId = dealerCompanyId;
    }

    public String getDealerCompanyName() {
        return dealerCompanyName;
    }

    public void setDealerCompanyName(String dealerCompanyName) {
        this.dealerCompanyName = dealerCompanyName;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public String getMainPercent() {
        return mainPercent;
    }

    public void setMainPercent(String mainPercent) {
        this.mainPercent = mainPercent;
    }

    public String getMainAmount() {
        return mainAmount;
    }

    public void setMainAmount(String mainAmount) {
        this.mainAmount = mainAmount;
    }

    public String getCollectMoney() {
        return collectMoney;
    }

    public void setCollectMoney(String collectMoney) {
        this.collectMoney = collectMoney;
    }

    public String getAdditionPercent() {
        return additionPercent;
    }

    public void setAdditionPercent(String additionPercent) {
        this.additionPercent = additionPercent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer[] getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer[] dealerId) {
        this.dealerId = dealerId;
    }

    public String[] getDealerName() {
        return dealerName;
    }

    public void setDealerName(String[] dealerName) {
        this.dealerName = dealerName;
    }

    public String[] getDealerPhone() {
        return dealerPhone;
    }

    public void setDealerPhone(String[] dealerPhone) {
        this.dealerPhone = dealerPhone;
    }

    public String[] getDealerEmail() {
        return dealerEmail;
    }

    public void setDealerEmail(String[] dealerEmail) {
        this.dealerEmail = dealerEmail;
    }
}
