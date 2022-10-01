/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import javax.annotation.Generated;
import javax.persistence.*;
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
@Table(name = "product_pricing", schema = "public", indexes = {
    @Index(name = "product_pricing_pkey", unique = true, columnList = "product_pricing_id ASC")
})
public class ProductPricing implements Serializable {

    private static final long serialVersionUID = -1631981206;

    private Long       productPricingId;
    private BigDecimal basePrice;
    private Timestamp  createDate;
    private Boolean    inActive;

    public ProductPricing() {}

    public ProductPricing(ProductPricing value) {
        this.productPricingId = value.productPricingId;
        this.basePrice = value.basePrice;
        this.createDate = value.createDate;
        this.inActive = value.inActive;
    }

    public ProductPricing(
        Long       productPricingId,
        BigDecimal basePrice,
        Timestamp  createDate,
        Boolean    inActive
    ) {
        this.productPricingId = productPricingId;
        this.basePrice = basePrice;
        this.createDate = createDate;
        this.inActive = inActive;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_pricing_id", unique = true, nullable = false, precision = 64)
    public Long getProductPricingId() {
        return this.productPricingId;
    }

    public void setProductPricingId(Long productPricingId) {
        this.productPricingId = productPricingId;
    }

    @Column(name = "base_price", precision = 23, scale = 4)
    public BigDecimal getBasePrice() {
        return this.basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    @Column(name = "create_date")
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Column(name = "in_active")
    public Boolean getInActive() {
        return this.inActive;
    }

    public void setInActive(Boolean inActive) {
        this.inActive = inActive;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ProductPricing (");

        sb.append(productPricingId);
        sb.append(", ").append(basePrice);
        sb.append(", ").append(createDate);
        sb.append(", ").append(inActive);

        sb.append(")");
        return sb.toString();
    }
}