package com.gateweb.charge.report.bean;

import java.math.BigDecimal;

public class PartsOfIasrReport {
    String invoiceDate;
    String seller;
    String buyer;
    Integer status;
    Integer amount;
    BigDecimal total;

    public PartsOfIasrReport(String invoiceDate, String seller, String buyer, Integer status, Integer amount, BigDecimal total) {
        this.invoiceDate = invoiceDate;
        this.seller = seller;
        this.buyer = buyer;
        this.status = status;
        this.amount = amount;
        this.total = total;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
