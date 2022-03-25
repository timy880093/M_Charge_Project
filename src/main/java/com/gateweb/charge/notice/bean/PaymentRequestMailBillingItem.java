package com.gateweb.charge.notice.bean;

import java.math.BigDecimal;

public class PaymentRequestMailBillingItem {
    String itemName;
    String chargeType;
    String quota;
    BigDecimal taxIncludedAmount;

    public PaymentRequestMailBillingItem() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public BigDecimal getTaxIncludedAmount() {
        return taxIncludedAmount;
    }

    public void setTaxIncludedAmount(BigDecimal taxIncludedAmount) {
        this.taxIncludedAmount = taxIncludedAmount;
    }

    @Override
    public String toString() {
        return "PaymentRequestMailBillingItem{" +
                "itemName='" + itemName + '\'' +
                ", chargeType='" + chargeType + '\'' +
                ", quota=" + quota +
                ", taxIncludedAmount=" + taxIncludedAmount +
                '}';
    }
}
