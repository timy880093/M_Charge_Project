package com.gateweb.charge.deduct.bean;

import com.gateweb.charge.enumeration.DeductType;
import com.gateweb.charge.enumeration.PaymentMethod;

import java.math.BigDecimal;

public class PurchaseDeductVo {
    Long companyId;
    Long productCategoryId;
    Long targetProductCategoryId;
    BigDecimal quota;
    BigDecimal salesPrice;
    Boolean deductByFee;
    DeductType deductType;
    Long callerId;
    PaymentMethod paymentMethod;
    Long payDateMillis;
    String paymentRemark;

    public PurchaseDeductVo() {
    }

    public PurchaseDeductVo(Long companyId, Long productCategoryId, Long targetProductCategoryId, BigDecimal quota, BigDecimal salesPrice, Boolean deductByFee, DeductType deductType, Long callerId, PaymentMethod paymentMethod, Long payDateMillis, String paymentRemark) {
        this.companyId = companyId;
        this.productCategoryId = productCategoryId;
        this.targetProductCategoryId = targetProductCategoryId;
        this.quota = quota;
        this.salesPrice = salesPrice;
        this.deductByFee = deductByFee;
        this.deductType = deductType;
        this.callerId = callerId;
        this.paymentMethod = paymentMethod;
        this.payDateMillis = payDateMillis;
        this.paymentRemark = paymentRemark;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Long getTargetProductCategoryId() {
        return targetProductCategoryId;
    }

    public void setTargetProductCategoryId(Long targetProductCategoryId) {
        this.targetProductCategoryId = targetProductCategoryId;
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Boolean getDeductByFee() {
        return deductByFee;
    }

    public void setDeductByFee(Boolean deductByFee) {
        this.deductByFee = deductByFee;
    }

    public DeductType getDeductType() {
        return deductType;
    }

    public void setDeductType(DeductType deductType) {
        this.deductType = deductType;
    }

    public Long getCallerId() {
        return callerId;
    }

    public void setCallerId(Long callerId) {
        this.callerId = callerId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getPayDateMillis() {
        return payDateMillis;
    }

    public void setPayDateMillis(Long payDateMillis) {
        this.payDateMillis = payDateMillis;
    }

    public String getPaymentRemark() {
        return paymentRemark;
    }

    public void setPaymentRemark(String paymentRemark) {
        this.paymentRemark = paymentRemark;
    }
}
