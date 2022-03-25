package com.gateweb.charge.enumeration;

public enum PaidPlan {
    POST_PAID("後繳"), PRE_PAID("預繳");
    public final String description;

    PaidPlan(String description) {
        this.description = description;
    }
}
