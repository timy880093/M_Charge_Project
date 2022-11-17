package com.gateweb.charge.chargeSource.iasr.bean;

public class MaxIasrInvoiceDatePeriod {
    String startInvoiceDate;
    String endInvoiceDate;

    public MaxIasrInvoiceDatePeriod() {
    }

    public MaxIasrInvoiceDatePeriod(String startInvoiceDate, String endInvoiceDate) {
        this.startInvoiceDate = startInvoiceDate;
        this.endInvoiceDate = endInvoiceDate;
    }

    public String getStartInvoiceDate() {
        return startInvoiceDate;
    }

    public void setStartInvoiceDate(String startInvoiceDate) {
        this.startInvoiceDate = startInvoiceDate;
    }

    public String getEndInvoiceDate() {
        return endInvoiceDate;
    }

    public void setEndInvoiceDate(String endInvoiceDate) {
        this.endInvoiceDate = endInvoiceDate;
    }
}
