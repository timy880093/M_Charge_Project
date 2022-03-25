package com.gateweb.charge.enumeration;

public enum ChargePolicyType {
    RENTAL("月租預繳"), OVERAGE("超額");

    public final String description;

    ChargePolicyType(String description) {
        this.description = description;
    }
}
