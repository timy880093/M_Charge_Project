package com.gateweb.charge.report.bean;

import java.math.BigDecimal;

public class BillingItemReport {
    String companyName;
    String businessNo;
    String chargePlan;
    String paidPlan;
    String billingItemType;
    String expectedOutDate;
    String packageName;
    String chargeModeName;
    BigDecimal taxExcludedAmount;

    public BillingItemReport() {
    }

    public BillingItemReport(String companyName, String businessNo, String chargePlan, String paidPlan, String billingItemType, String expectedOutDate, String packageName, String chargeModeName, BigDecimal taxExcludedAmount) {
        this.companyName = companyName;
        this.businessNo = businessNo;
        this.chargePlan = chargePlan;
        this.paidPlan = paidPlan;
        this.billingItemType = billingItemType;
        this.expectedOutDate = expectedOutDate;
        this.packageName = packageName;
        this.chargeModeName = chargeModeName;
        this.taxExcludedAmount = taxExcludedAmount;
    }

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

    public String getChargePlan() {
        return chargePlan;
    }

    public void setChargePlan(String chargePlan) {
        this.chargePlan = chargePlan;
    }

    public String getPaidPlan() {
        return paidPlan;
    }

    public void setPaidPlan(String paidPlan) {
        this.paidPlan = paidPlan;
    }

    public String getBillingItemType() {
        return billingItemType;
    }

    public void setBillingItemType(String billingItemType) {
        this.billingItemType = billingItemType;
    }

    public String getExpectedOutDate() {
        return expectedOutDate;
    }

    public void setExpectedOutDate(String expectedOutDate) {
        this.expectedOutDate = expectedOutDate;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getChargeModeName() {
        return chargeModeName;
    }

    public void setChargeModeName(String chargeModeName) {
        this.chargeModeName = chargeModeName;
    }

    public BigDecimal getTaxExcludedAmount() {
        return taxExcludedAmount;
    }

    public void setTaxExcludedAmount(BigDecimal taxExcludedAmount) {
        this.taxExcludedAmount = taxExcludedAmount;
    }
}
