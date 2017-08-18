package com.gate.web.formbeans;


public class CompanyChargeCycleBean {
    private Integer companyId;
    private Integer chargeId;
    private Integer packageType; //收費方式1.月租型 2.級距型
    private Integer additionQuantity;
    private Integer freeMonth;
    private String realStartDate;
    private String realEndDate;
    private Integer giftPrice; //加贈金額
    private Integer dealerCompanyId; //經銷商公司
    private Integer dealerId; //經銷商公司業務只
    private String brokerCp2; //介紹公司
    private String broker2; //介紹公司的介紹人
    private String brokerCp3; //裝機公司
    private String broker3; //裝機公司的裝機人
    private String isFirst; //該用戶是否為首次綁方案

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public Integer getPackageType() {
        return packageType;
    }

    public void setPackageType(Integer packageType) {
        this.packageType = packageType;
    }

    public Integer getAdditionQuantity() {
        return additionQuantity;
    }

    public void setAdditionQuantity(Integer additionQuantity) {
        this.additionQuantity = additionQuantity;
    }

    public Integer getFreeMonth() {
        return freeMonth;
    }

    public void setFreeMonth(Integer freeMonth) {
        this.freeMonth = freeMonth;
    }

    public String getRealStartDate() {
        return realStartDate;
    }

    public void setRealStartDate(String realStartDate) {
        this.realStartDate = realStartDate;
    }

    public String getRealEndDate() {
        return realEndDate;
    }

    public void setRealEndDate(String realEndDate) {
        this.realEndDate = realEndDate;
    }

    public Integer getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(Integer giftPrice) {
        this.giftPrice = giftPrice;
    }

    public Integer getDealerCompanyId() {
        return dealerCompanyId;
    }

    public void setDealerCompanyId(Integer dealerCompanyId) {
        this.dealerCompanyId = dealerCompanyId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getBrokerCp2() {
        return brokerCp2;
    }

    public void setBrokerCp2(String brokerCp2) {
        this.brokerCp2 = brokerCp2;
    }

    public String getBroker2() {
        return broker2;
    }

    public void setBroker2(String broker2) {
        this.broker2 = broker2;
    }

    public String getBrokerCp3() {
        return brokerCp3;
    }

    public void setBrokerCp3(String brokerCp3) {
        this.brokerCp3 = brokerCp3;
    }

    public String getBroker3() {
        return broker3;
    }

    public void setBroker3(String broker3) {
        this.broker3 = broker3;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String first) {
        isFirst = first;
    }
}
