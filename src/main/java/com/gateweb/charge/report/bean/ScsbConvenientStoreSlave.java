package com.gateweb.charge.report.bean;

import java.math.BigDecimal;

public class ScsbConvenientStoreSlave {
    String itemName;
    BigDecimal taxInclusivePrice;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getTaxInclusivePrice() {
        return taxInclusivePrice;
    }

    public void setTaxInclusivePrice(BigDecimal taxInclusivePrice) {
        this.taxInclusivePrice = taxInclusivePrice;
    }
}
