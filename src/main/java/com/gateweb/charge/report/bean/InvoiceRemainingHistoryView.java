package com.gateweb.charge.report.bean;

public class InvoiceRemainingHistoryView {
    Long invoiceRemainingId;
    String businessNo;
    String chargePackageName;
    Integer usage;
    Integer remaining;
    String calculateStartDate;
    String calculateEndDate;
    String updateTimestampDesc;

    public InvoiceRemainingHistoryView() {
    }

    public InvoiceRemainingHistoryView(Long invoiceRemainingId, String businessNo, String chargePackageName, Integer usage, Integer remaining, String calculateStartDate, String calculateEndDate, String updateTimestampDesc) {
        this.invoiceRemainingId = invoiceRemainingId;
        this.businessNo = businessNo;
        this.chargePackageName = chargePackageName;
        this.usage = usage;
        this.remaining = remaining;
        this.calculateStartDate = calculateStartDate;
        this.calculateEndDate = calculateEndDate;
        this.updateTimestampDesc = updateTimestampDesc;
    }

    public Long getInvoiceRemainingId() {
        return invoiceRemainingId;
    }

    public void setInvoiceRemainingId(Long invoiceRemainingId) {
        this.invoiceRemainingId = invoiceRemainingId;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getChargePackageName() {
        return chargePackageName;
    }

    public void setChargePackageName(String chargePackageName) {
        this.chargePackageName = chargePackageName;
    }

    public Integer getUsage() {
        return usage;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    public String getCalculateStartDate() {
        return calculateStartDate;
    }

    public void setCalculateStartDate(String calculateStartDate) {
        this.calculateStartDate = calculateStartDate;
    }

    public String getCalculateEndDate() {
        return calculateEndDate;
    }

    public void setCalculateEndDate(String calculateEndDate) {
        this.calculateEndDate = calculateEndDate;
    }

    public String getUpdateTimestampDesc() {
        return updateTimestampDesc;
    }

    public void setUpdateTimestampDesc(String updateTimestampDesc) {
        this.updateTimestampDesc = updateTimestampDesc;
    }

    @Override
    public String toString() {
        return "InvoiceRemainingHistoryView{" +
                "invoiceRemainingId=" + invoiceRemainingId +
                ", businessNo='" + businessNo + '\'' +
                ", chargePackageName='" + chargePackageName + '\'' +
                ", usage=" + usage +
                ", remaining=" + remaining +
                ", calculateStartDate='" + calculateStartDate + '\'' +
                ", calculateEndDate='" + calculateEndDate + '\'' +
                ", updateTimestampDesc='" + updateTimestampDesc + '\'' +
                '}';
    }
}
