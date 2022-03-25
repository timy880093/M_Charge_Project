package com.gateweb.charge.contract.bean.request;

public class ExpirationDateFulfillRes {

    Long expirationDateMillis;

    public ExpirationDateFulfillRes() {
    }

    public ExpirationDateFulfillRes(Long expirationDateMillis) {
        this.expirationDateMillis = expirationDateMillis;
    }

    public Long getExpirationDateMillis() {
        return expirationDateMillis;
    }

    public void setExpirationDateMillis(Long expirationDateMillis) {
        this.expirationDateMillis = expirationDateMillis;
    }
}
