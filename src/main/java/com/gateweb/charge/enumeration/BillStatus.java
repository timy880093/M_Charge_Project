package com.gateweb.charge.enumeration;

public enum BillStatus {
    C("未付款"), P("已付款");

    public final String description;

    BillStatus(String description) {
        this.description = description;
    }
}
