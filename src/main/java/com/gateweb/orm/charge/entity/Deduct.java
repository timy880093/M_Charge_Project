/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import com.gateweb.charge.enumeration.ChargeBaseType;
import com.gateweb.charge.enumeration.DeductStatus;
import com.gateweb.charge.enumeration.DeductType;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(name = "deduct", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "deduct_pkey", columnNames = {"deduct_id"})
})
public class Deduct implements Serializable {

    private static final long serialVersionUID = 1620475523;

    private Long deductId;
    private Long companyId;
    private Long contractId;
    private Long productId;
    private Long packageRefId;
    private Long chargeRuleId;
    private Long productCategoryId;
    private Long targetProductCategoryId;
    private DeductType deductType;
    private DeductStatus deductStatus;
    private ChargeBaseType chargeBaseType;
    private BigDecimal quota;
    private Boolean deductByFee;
    private BigDecimal salesPrice;
    private LocalDateTime effectiveDate;
    private LocalDateTime expirationDate;
    private Long creatorId;
    private LocalDateTime createDate;
    private Long modifierId;
    private LocalDateTime modifyDate;

    public Deduct() {
    }

    public Deduct(Long deductId, Long companyId, Long contractId, Long productId, Long packageRefId, Long chargeRuleId, Long productCategoryId, Long targetProductCategoryId, DeductType deductType, DeductStatus deductStatus, ChargeBaseType chargeBaseType, BigDecimal quota, Boolean deductByFee, BigDecimal salesPrice, LocalDateTime effectiveDate, LocalDateTime expirationDate, Long creatorId, LocalDateTime createDate, Long modifierId, LocalDateTime modifyDate) {
        this.deductId = deductId;
        this.companyId = companyId;
        this.contractId = contractId;
        this.productId = productId;
        this.packageRefId = packageRefId;
        this.chargeRuleId = chargeRuleId;
        this.productCategoryId = productCategoryId;
        this.targetProductCategoryId = targetProductCategoryId;
        this.deductType = deductType;
        this.deductStatus = deductStatus;
        this.chargeBaseType = chargeBaseType;
        this.quota = quota;
        this.deductByFee = deductByFee;
        this.salesPrice = salesPrice;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.creatorId = creatorId;
        this.createDate = createDate;
        this.modifierId = modifierId;
        this.modifyDate = modifyDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deduct_id", nullable = false, precision = 64)
    public Long getDeductId() {
        return this.deductId;
    }

    public void setDeductId(Long deductId) {
        this.deductId = deductId;
    }

    @Column(name = "company_id", precision = 64)
    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Column(name = "contract_id", precision = 64)
    public Long getContractId() {
        return this.contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    @Column(name = "product_id", precision = 64)
    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "package_ref_id", precision = 64)
    public Long getPackageRefId() {
        return this.packageRefId;
    }

    public void setPackageRefId(Long packageRefId) {
        this.packageRefId = packageRefId;
    }

    @Column(name = "charge_rule_id", precision = 64)
    public Long getChargeRuleId() {
        return chargeRuleId;
    }

    public void setChargeRuleId(Long chargeRuleId) {
        this.chargeRuleId = chargeRuleId;
    }

    @Column(name = "product_category_id", precision = 64)
    public Long getProductCategoryId() {
        return this.productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    @Column(name = "target_product_category_id", precision = 64)
    public Long getTargetProductCategoryId() {
        return targetProductCategoryId;
    }

    public void setTargetProductCategoryId(Long targetProductCategoryId) {
        this.targetProductCategoryId = targetProductCategoryId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "deduct_type", length = 30)
    public DeductType getDeductType() {
        return this.deductType;
    }

    public void setDeductType(DeductType deductType) {
        this.deductType = deductType;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "deduct_status", length = 30)
    public DeductStatus getDeductStatus() {
        return this.deductStatus;
    }

    public void setDeductStatus(DeductStatus deductStatus) {
        this.deductStatus = deductStatus;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "charge_base_type")
    public ChargeBaseType getChargeBaseType() {
        return this.chargeBaseType;
    }

    public void setChargeBaseType(ChargeBaseType chargeBaseType) {
        this.chargeBaseType = chargeBaseType;
    }

    @Column(name = "quota", precision = 23, scale = 4)
    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    @Column(name = "deduct_by_fee")
    public Boolean getDeductByFee() {
        return this.deductByFee;
    }

    public void setDeductByFee(Boolean deductByFee) {
        this.deductByFee = deductByFee;
    }

    @Column(name = "sales_price", precision = 23, scale = 4)
    public BigDecimal getSalesPrice() {
        return this.salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    @Column(name = "effective_date")
    public LocalDateTime getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Column(name = "expiration_date")
    public LocalDateTime getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Column(name = "creator_id", precision = 64)
    public Long getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Column(name = "create_date")
    public LocalDateTime getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Column(name = "modifier_id", precision = 64)
    public Long getModifierId() {
        return this.modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    @UpdateTimestamp
    @Column(name = "modify_date")
    public LocalDateTime getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public String toString() {
        return "Deduct{" +
                "deductId=" + deductId +
                ", companyId=" + companyId +
                ", contractId=" + contractId +
                ", productId=" + productId +
                ", packageRefId=" + packageRefId +
                ", chargeRuleId=" + chargeRuleId +
                ", productCategoryId=" + productCategoryId +
                ", targetProductCategoryId=" + targetProductCategoryId +
                ", deductType=" + deductType +
                ", deductStatus=" + deductStatus +
                ", chargeBaseType=" + chargeBaseType +
                ", quota=" + quota +
                ", deductByFee=" + deductByFee +
                ", salesPrice=" + salesPrice +
                ", effectiveDate=" + effectiveDate +
                ", expirationDate=" + expirationDate +
                ", creatorId=" + creatorId +
                ", createDate=" + createDate +
                ", modifierId=" + modifierId +
                ", modifyDate=" + modifyDate +
                '}';
    }
}