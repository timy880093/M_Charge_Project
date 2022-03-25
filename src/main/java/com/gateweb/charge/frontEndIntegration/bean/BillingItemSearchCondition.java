package com.gateweb.charge.frontEndIntegration.bean;

public class BillingItemSearchCondition {
    String calYearMonth;
    String gwBillingRuleYm;
    Long companyId;
    Long productCategoryId;
    String billingItemIdList;
    String calFeeYearMonth;
    Boolean includeMemo;
    Boolean includeBilledItem;

    public BillingItemSearchCondition() {
    }

    public BillingItemSearchCondition(String calYearMonth, String gwBillingRuleYm, Long companyId, Long productCategoryId, String billingItemIdList, String calFeeYearMonth, Boolean includeMemo, Boolean includeBilledItem) {
        this.calYearMonth = calYearMonth;
        this.gwBillingRuleYm = gwBillingRuleYm;
        this.companyId = companyId;
        this.productCategoryId = productCategoryId;
        this.billingItemIdList = billingItemIdList;
        this.calFeeYearMonth = calFeeYearMonth;
        this.includeMemo = includeMemo;
        this.includeBilledItem = includeBilledItem;
    }

    public String getCalYearMonth() {
        return calYearMonth;
    }

    public void setCalYearMonth(String calYearMonth) {
        this.calYearMonth = calYearMonth;
    }

    public String getGwBillingRuleYm() {
        return gwBillingRuleYm;
    }

    public void setGwBillingRuleYm(String gwBillingRuleYm) {
        this.gwBillingRuleYm = gwBillingRuleYm;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getBillingItemIdList() {
        return billingItemIdList;
    }

    public void setBillingItemIdList(String billingItemIdList) {
        this.billingItemIdList = billingItemIdList;
    }

    public String getCalFeeYearMonth() {
        return calFeeYearMonth;
    }

    public void setCalFeeYearMonth(String calFeeYearMonth) {
        this.calFeeYearMonth = calFeeYearMonth;
    }

    public Boolean getIncludeMemo() {
        return includeMemo;
    }

    public void setIncludeMemo(Boolean includeMemo) {
        this.includeMemo = includeMemo;
    }

    public Boolean getIncludeBilledItem() {
        return includeBilledItem;
    }

    public void setIncludeBilledItem(Boolean includeBilledItem) {
        this.includeBilledItem = includeBilledItem;
    }

}
