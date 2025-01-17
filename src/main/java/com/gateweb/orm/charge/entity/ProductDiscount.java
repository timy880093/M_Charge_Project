/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import javax.annotation.Generated;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "product_discount", schema = "public", indexes = {
    @Index(name = "product_discount_pkey", unique = true, columnList = "product_discount_id ASC")
})
public class ProductDiscount implements Serializable {

    private static final long serialVersionUID = -263951554;

    private Long       productDiscountId;
    private Integer    discountValue;
    private String     discountUnit;
    private Timestamp  createDate;
    private Timestamp  validFrom;
    private Timestamp  validUntil;
    private BigDecimal maximumDiscountAmount;

    public ProductDiscount() {}

    public ProductDiscount(ProductDiscount value) {
        this.productDiscountId = value.productDiscountId;
        this.discountValue = value.discountValue;
        this.discountUnit = value.discountUnit;
        this.createDate = value.createDate;
        this.validFrom = value.validFrom;
        this.validUntil = value.validUntil;
        this.maximumDiscountAmount = value.maximumDiscountAmount;
    }

    public ProductDiscount(
        Long       productDiscountId,
        Integer    discountValue,
        String     discountUnit,
        Timestamp  createDate,
        Timestamp  validFrom,
        Timestamp  validUntil,
        BigDecimal maximumDiscountAmount
    ) {
        this.productDiscountId = productDiscountId;
        this.discountValue = discountValue;
        this.discountUnit = discountUnit;
        this.createDate = createDate;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.maximumDiscountAmount = maximumDiscountAmount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_discount_id", unique = true, nullable = false, precision = 64)
    public Long getProductDiscountId() {
        return this.productDiscountId;
    }

    public void setProductDiscountId(Long productDiscountId) {
        this.productDiscountId = productDiscountId;
    }

    @Column(name = "discount_value", precision = 32)
    public Integer getDiscountValue() {
        return this.discountValue;
    }

    public void setDiscountValue(Integer discountValue) {
        this.discountValue = discountValue;
    }

    @Column(name = "discount_unit", length = 10)
    @Size(max = 10)
    public String getDiscountUnit() {
        return this.discountUnit;
    }

    public void setDiscountUnit(String discountUnit) {
        this.discountUnit = discountUnit;
    }

    @Column(name = "create_date")
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Column(name = "valid_from")
    public Timestamp getValidFrom() {
        return this.validFrom;
    }

    public void setValidFrom(Timestamp validFrom) {
        this.validFrom = validFrom;
    }

    @Column(name = "valid_until")
    public Timestamp getValidUntil() {
        return this.validUntil;
    }

    public void setValidUntil(Timestamp validUntil) {
        this.validUntil = validUntil;
    }

    @Column(name = "maximum_discount_amount", precision = 23, scale = 4)
    public BigDecimal getMaximumDiscountAmount() {
        return this.maximumDiscountAmount;
    }

    public void setMaximumDiscountAmount(BigDecimal maximumDiscountAmount) {
        this.maximumDiscountAmount = maximumDiscountAmount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ProductDiscount (");

        sb.append(productDiscountId);
        sb.append(", ").append(discountValue);
        sb.append(", ").append(discountUnit);
        sb.append(", ").append(createDate);
        sb.append(", ").append(validFrom);
        sb.append(", ").append(validUntil);
        sb.append(", ").append(maximumDiscountAmount);

        sb.append(")");
        return sb.toString();
    }
}
