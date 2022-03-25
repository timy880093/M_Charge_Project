package com.gateweb.charge.enumeration;

public enum ContractPrepayType {
    PREPAY_BY_MONTH("依月份預繳"),
    PREPAY_BY_REMAINING_COUNT("張數固定預繳");

    public final String description;

    ContractPrepayType(String desc) {
        this.description = desc;
    }
}
