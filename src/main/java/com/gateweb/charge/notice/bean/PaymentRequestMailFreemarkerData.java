package com.gateweb.charge.notice.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaymentRequestMailFreemarkerData {
    String companyName;
    String ymString;
    String paymentExpirationDate;
    BigDecimal paymentRequestTotalAmount = BigDecimal.ZERO;
    BigDecimal paymentRequestOverageTotalAmount = BigDecimal.ZERO;
    int maximumCharge;
    int overageThreshold;
    boolean haveOverage;
    boolean correction;
    boolean extraNotice;
    boolean oBankAdvert;
    String extraNoticeMessage;
    String ctbcVirtualAccount;
    List<PaymentRequestMailBillingItem> paymentRequestMailBillingItemSet = new ArrayList<>();
    List<PaymentRequestMailOverageItem> paymentRequestMailOverageItemList = new ArrayList<>();
    List<PaymentRequestMailGradeTable> paymentRequestMailGradeTableList = new ArrayList<>();

    public PaymentRequestMailFreemarkerData() {
    }

    public PaymentRequestMailFreemarkerData(String companyName, String ymString, String paymentExpirationDate, BigDecimal paymentRequestTotalAmount, BigDecimal paymentRequestOverageTotalAmount, int maximumCharge, int overageThreshold, boolean haveOverage, boolean correction, boolean extraNotice, boolean oBankAdvert, String extraNoticeMessage, String ctbcVirtualAccount, List<PaymentRequestMailBillingItem> paymentRequestMailBillingItemSet, List<PaymentRequestMailOverageItem> paymentRequestMailOverageItemList, List<PaymentRequestMailGradeTable> paymentRequestMailGradeTableList) {
        this.companyName = companyName;
        this.ymString = ymString;
        this.paymentExpirationDate = paymentExpirationDate;
        this.paymentRequestTotalAmount = paymentRequestTotalAmount;
        this.paymentRequestOverageTotalAmount = paymentRequestOverageTotalAmount;
        this.maximumCharge = maximumCharge;
        this.overageThreshold = overageThreshold;
        this.haveOverage = haveOverage;
        this.correction = correction;
        this.extraNotice = extraNotice;
        this.oBankAdvert = oBankAdvert;
        this.extraNoticeMessage = extraNoticeMessage;
        this.ctbcVirtualAccount = ctbcVirtualAccount;
        this.paymentRequestMailBillingItemSet = paymentRequestMailBillingItemSet;
        this.paymentRequestMailOverageItemList = paymentRequestMailOverageItemList;
        this.paymentRequestMailGradeTableList = paymentRequestMailGradeTableList;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getYmString() {
        return ymString;
    }

    public void setYmString(String ymString) {
        this.ymString = ymString;
    }

    public String getPaymentExpirationDate() {
        return paymentExpirationDate;
    }

    public void setPaymentExpirationDate(String paymentExpirationDate) {
        this.paymentExpirationDate = paymentExpirationDate;
    }

    public BigDecimal getPaymentRequestTotalAmount() {
        return paymentRequestTotalAmount;
    }

    public void setPaymentRequestTotalAmount(BigDecimal paymentRequestTotalAmount) {
        this.paymentRequestTotalAmount = paymentRequestTotalAmount;
    }

    public BigDecimal getPaymentRequestOverageTotalAmount() {
        return paymentRequestOverageTotalAmount;
    }

    public void setPaymentRequestOverageTotalAmount(BigDecimal paymentRequestOverageTotalAmount) {
        this.paymentRequestOverageTotalAmount = paymentRequestOverageTotalAmount;
    }

    public int getMaximumCharge() {
        return maximumCharge;
    }

    public void setMaximumCharge(int maximumCharge) {
        this.maximumCharge = maximumCharge;
    }

    public int getOverageThreshold() {
        return overageThreshold;
    }

    public void setOverageThreshold(int overageThreshold) {
        this.overageThreshold = overageThreshold;
    }

    public boolean isHaveOverage() {
        return haveOverage;
    }

    public void setHaveOverage(boolean haveOverage) {
        this.haveOverage = haveOverage;
    }

    public boolean isCorrection() {
        return correction;
    }

    public void setCorrection(boolean correction) {
        this.correction = correction;
    }

    public boolean isExtraNotice() {
        return extraNotice;
    }

    public void setExtraNotice(boolean extraNotice) {
        this.extraNotice = extraNotice;
    }

    public boolean isoBankAdvert() {
        return oBankAdvert;
    }

    public void setoBankAdvert(boolean oBankAdvert) {
        this.oBankAdvert = oBankAdvert;
    }

    public String getExtraNoticeMessage() {
        return extraNoticeMessage;
    }

    public void setExtraNoticeMessage(String extraNoticeMessage) {
        this.extraNoticeMessage = extraNoticeMessage;
    }

    public String getCtbcVirtualAccount() {
        return ctbcVirtualAccount;
    }

    public void setCtbcVirtualAccount(String ctbcVirtualAccount) {
        this.ctbcVirtualAccount = ctbcVirtualAccount;
    }

    public List<PaymentRequestMailBillingItem> getPaymentRequestMailBillingItemSet() {
        return paymentRequestMailBillingItemSet;
    }

    public void setPaymentRequestMailBillingItemSet(List<PaymentRequestMailBillingItem> paymentRequestMailBillingItemSet) {
        this.paymentRequestMailBillingItemSet = paymentRequestMailBillingItemSet;
    }

    public List<PaymentRequestMailOverageItem> getPaymentRequestMailOverageItemList() {
        return paymentRequestMailOverageItemList;
    }

    public void setPaymentRequestMailOverageItemList(List<PaymentRequestMailOverageItem> paymentRequestMailOverageItemList) {
        this.paymentRequestMailOverageItemList = paymentRequestMailOverageItemList;
    }

    public List<PaymentRequestMailGradeTable> getPaymentRequestMailGradeTableList() {
        return paymentRequestMailGradeTableList;
    }

    public void setPaymentRequestMailGradeTableList(List<PaymentRequestMailGradeTable> paymentRequestMailGradeTableList) {
        this.paymentRequestMailGradeTableList = paymentRequestMailGradeTableList;
    }
}
