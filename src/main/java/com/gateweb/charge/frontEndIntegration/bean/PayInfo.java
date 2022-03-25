package com.gateweb.charge.frontEndIntegration.bean;

public class PayInfo {
    Long billId;
    Long payTimestamp;
    String payAmount;
    String paymentRemark;
    String bankCode;
    String paymentMethod;

    public PayInfo() {
    }

    public PayInfo(Long billId, Long payTimestamp, String payAmount, String paymentRemark, String bankCode, String paymentMethod) {
        this.billId = billId;
        this.payTimestamp = payTimestamp;
        this.payAmount = payAmount;
        this.paymentRemark = paymentRemark;
        this.bankCode = bankCode;
        this.paymentMethod = paymentMethod;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getPayTimestamp() {
        return payTimestamp;
    }

    public void setPayTimestamp(Long payTimestamp) {
        this.payTimestamp = payTimestamp;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPaymentRemark() {
        return paymentRemark;
    }

    public void setPaymentRemark(String paymentRemark) {
        this.paymentRemark = paymentRemark;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "PayInfo{" +
                "billId=" + billId +
                ", payTimestamp=" + payTimestamp +
                ", payAmount='" + payAmount + '\'' +
                ", paymentRemark='" + paymentRemark + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
