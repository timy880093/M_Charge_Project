package com.gate.web.beans;

/**
 * Created by emily on 2016/4/7.
 */
public class FirstCompanyPackageBean {

    String businesscode; //統編
    String dealerCmpName; //經銷商
    String dealerName; //經銷商業務
    String realStartDate; //起帳日
    String chargeName; //計費型態
    Integer freeMonth; //客製化:加贈免費月份
    Double giftPrice; //客製化:贈送金額
    String broker2CpName; //介紹公司
    String broker2Name; //介紹公司的介紹人
    String broker3CpName; //裝機公司
    String broker3Name; // 裝機公司的裝機人
    String warrantyNo; //發票機序號
    String startDate; //保固起日
    String extend; //是否延長保固

    public String getBusinesscode() {
        return businesscode;
    }

    public void setBusinesscode(String businesscode) {
        this.businesscode = businesscode;
    }

    public String getDealerCmpName() {
        return dealerCmpName;
    }

    public void setDealerCmpName(String dealerCmpName) {
        this.dealerCmpName = dealerCmpName;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getRealStartDate() {
        return realStartDate;
    }

    public void setRealStartDate(String realStartDate) {
        this.realStartDate = realStartDate;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public Integer getFreeMonth() {
        return freeMonth;
    }

    public void setFreeMonth(Integer freeMonth) {
        this.freeMonth = freeMonth;
    }

    public Double getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(Double giftPrice) {
        this.giftPrice = giftPrice;
    }

    public String getBroker2CpName() {
        return broker2CpName;
    }

    public void setBroker2CpName(String broker2CpName) {
        this.broker2CpName = broker2CpName;
    }

    public String getBroker2Name() {
        return broker2Name;
    }

    public void setBroker2Name(String broker2Name) {
        this.broker2Name = broker2Name;
    }

    public String getBroker3CpName() {
        return broker3CpName;
    }

    public void setBroker3CpName(String broker3CpName) {
        this.broker3CpName = broker3CpName;
    }

    public String getBroker3Name() {
        return broker3Name;
    }

    public void setBroker3Name(String broker3Name) {
        this.broker3Name = broker3Name;
    }

    public String getWarrantyNo() {
        return warrantyNo;
    }

    public void setWarrantyNo(String warrantyNo) {
        this.warrantyNo = warrantyNo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
