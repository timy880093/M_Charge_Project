package com.gateweb.charge.notice.bean;

import java.math.BigDecimal;

public class PaymentRequestMailOverageItem {
    String calYm;
    int quota;
    int usage;
    int overageCount;
    BigDecimal unitPrice = BigDecimal.ZERO;
    BigDecimal taxExcludedAmount = BigDecimal.ZERO;
    BigDecimal taxIncludedAmount = BigDecimal.ZERO;

    public PaymentRequestMailOverageItem() {
    }

    public String getCalYm() {
        return calYm;
    }

    public void setCalYm(String calYm) {
        this.calYm = calYm;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTaxExcludedAmount() {
        return taxExcludedAmount;
    }

    public void setTaxExcludedAmount(BigDecimal taxExcludedAmount) {
        this.taxExcludedAmount = taxExcludedAmount;
    }

    public BigDecimal getTaxIncludedAmount() {
        return taxIncludedAmount;
    }

    public void setTaxIncludedAmount(BigDecimal taxIncludedAmount) {
        this.taxIncludedAmount = taxIncludedAmount;
    }

    public int getOverageCount() {
        return overageCount;
    }

    public void setOverageCount(int overageCount) {
        this.overageCount = overageCount;
    }

    @Override
    public String toString() {
        return "PaymentRequestMailOverageItem{" +
                "calYm='" + calYm + '\'' +
                ", quota=" + quota +
                ", usage=" + usage +
                ", overageCount=" + overageCount +
                ", unitPrice=" + unitPrice +
                ", taxExcludedAmount=" + taxExcludedAmount +
                ", taxIncludedAmount=" + taxIncludedAmount +
                '}';
    }
}
