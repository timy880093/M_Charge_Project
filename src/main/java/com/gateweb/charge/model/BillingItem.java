/*
 * This file is generated by jOOQ.
 */
package com.gateweb.charge.model;


import javax.annotation.Generated;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
@Table(name = "billing_item", schema = "public", indexes = {
    @Index(name = "billing_item_id_constraint", unique = true, columnList = "billing_item_id ASC"),
    @Index(name = "billing_item_pkey", unique = true, columnList = "billing_item_id ASC")
})
public class BillingItem implements Serializable {

    private static final long serialVersionUID = 18843898;

    private Long       billingItemId;
    private String     productType;
    private Long       productId;
    private BigDecimal totalPrice;
    private Timestamp  calculateDate;
    private Integer    count;
    private BigDecimal unitPrice;

    public BillingItem() {}

    public BillingItem(BillingItem value) {
        this.billingItemId = value.billingItemId;
        this.productType = value.productType;
        this.productId = value.productId;
        this.totalPrice = value.totalPrice;
        this.calculateDate = value.calculateDate;
        this.count = value.count;
        this.unitPrice = value.unitPrice;
    }

    public BillingItem(
        Long       billingItemId,
        String     productType,
        Long       productId,
        BigDecimal totalPrice,
        Timestamp  calculateDate,
        Integer    count,
        BigDecimal unitPrice
    ) {
        this.billingItemId = billingItemId;
        this.productType = productType;
        this.productId = productId;
        this.totalPrice = totalPrice;
        this.calculateDate = calculateDate;
        this.count = count;
        this.unitPrice = unitPrice;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "billing_item_id", unique = true, nullable = false, precision = 64)
    public Long getBillingItemId() {
        return this.billingItemId;
    }

    public void setBillingItemId(Long billingItemId) {
        this.billingItemId = billingItemId;
    }

    @Column(name = "product_type", nullable = false, length = 20)
    @NotNull
    @Size(max = 20)
    public String getProductType() {
        return this.productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @Column(name = "product_id", nullable = false, precision = 64)
    @NotNull
    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "total_price", nullable = false, precision = 53, scale = 4)
    @NotNull
    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Column(name = "calculate_date")
    public Timestamp getCalculateDate() {
        return this.calculateDate;
    }

    public void setCalculateDate(Timestamp calculateDate) {
        this.calculateDate = calculateDate;
    }

    @Column(name = "count", precision = 32)
    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Column(name = "unit_price", precision = 23, scale = 4)
    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BillingItem (");

        sb.append(billingItemId);
        sb.append(", ").append(productType);
        sb.append(", ").append(productId);
        sb.append(", ").append(totalPrice);
        sb.append(", ").append(calculateDate);
        sb.append(", ").append(count);
        sb.append(", ").append(unitPrice);

        sb.append(")");
        return sb.toString();
    }
}
