package com.gateweb.charge.contract.bean.request;

import java.time.LocalDateTime;

public class ExpirationDateFulfillReq {
    LocalDateTime effectiveDate;
    Integer periodMonth;

    public ExpirationDateFulfillReq() {
    }

    public ExpirationDateFulfillReq(LocalDateTime effectiveDate, Integer periodMonth) {
        this.effectiveDate = effectiveDate;
        this.periodMonth = periodMonth;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Integer getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(Integer periodMonth) {
        this.periodMonth = periodMonth;
    }
}
