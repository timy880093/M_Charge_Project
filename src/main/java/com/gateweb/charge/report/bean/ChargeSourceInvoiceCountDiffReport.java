package com.gateweb.charge.report.bean;

public class ChargeSourceInvoiceCountDiffReport {
    String seller;
    String invoiceDate;
    Long originalCount;
    Long mediumCount;

    public ChargeSourceInvoiceCountDiffReport() {
    }

    public ChargeSourceInvoiceCountDiffReport(String seller, String invoiceDate, Long originalCount, Long mediumCount) {
        this.seller = seller;
        this.invoiceDate = invoiceDate;
        this.originalCount = originalCount;
        this.mediumCount = mediumCount;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Long getOriginalCount() {
        return originalCount;
    }

    public void setOriginalCount(Long originalCount) {
        this.originalCount = originalCount;
    }

    public Long getMediumCount() {
        return mediumCount;
    }

    public void setMediumCount(Long mediumCount) {
        this.mediumCount = mediumCount;
    }
}
