/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import com.gateweb.charge.enumeration.ProductStatus;
import com.gateweb.charge.enumeration.ProductType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(name = "product", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "product_pkey", columnNames = {"product_id"})
})
public class Product implements Serializable {

    private static final long serialVersionUID = -898434824;

    private Long productId;
    private String productName;
    private Long productSourceId;
    private Long productCategoryId;
    private ProductType productType;
    private ProductStatus productStatus;
    private Long productPricingId;
    private Long productDiscountId;
    private Long creatorId;
    private LocalDateTime createDate;
    private Long modifierId;
    private LocalDateTime modifyDate;

    public Product() {
    }

    public Product(Product value) {
        this.productId = value.productId;
        this.productName = value.productName;
        this.productSourceId = value.productSourceId;
        this.productCategoryId = value.productCategoryId;
        this.productType = value.productType;
        this.productStatus = value.productStatus;
        this.productPricingId = value.productPricingId;
        this.productDiscountId = value.productDiscountId;
        this.creatorId = value.creatorId;
        this.createDate = value.createDate;
        this.modifierId = value.modifierId;
        this.modifyDate = value.modifyDate;
    }

    public Product(
            Long productId,
            String productName,
            Long productSourceId,
            Long productCategoryId,
            ProductType productType,
            ProductStatus productStatus,
            Long productPricingId,
            Long productDiscountId,
            Long creatorId,
            LocalDateTime createDate,
            Long modifierId,
            LocalDateTime modifyDate
    ) {
        this.productId = productId;
        this.productName = productName;
        this.productSourceId = productSourceId;
        this.productCategoryId = productCategoryId;
        this.productType = productType;
        this.productStatus = productStatus;
        this.productPricingId = productPricingId;
        this.productDiscountId = productDiscountId;
        this.creatorId = creatorId;
        this.createDate = createDate;
        this.modifierId = modifierId;
        this.modifyDate = modifyDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false, precision = 64)
    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "product_name", length = 40)
    @Size(max = 40)
    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Column(name = "product_source_id", precision = 64)
    public Long getProductSourceId() {
        return this.productSourceId;
    }

    public void setProductSourceId(Long productSourceId) {
        this.productSourceId = productSourceId;
    }

    @Column(name = "product_category_id", precision = 64)
    public Long getProductCategoryId() {
        return this.productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    public ProductType getProductType() {
        return this.productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    public ProductStatus getProductStatus() {
        return this.productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    @Column(name = "product_pricing_id", precision = 64)
    public Long getProductPricingId() {
        return this.productPricingId;
    }

    public void setProductPricingId(Long productPricingId) {
        this.productPricingId = productPricingId;
    }

    @Column(name = "product_discount_id", precision = 64)
    public Long getProductDiscountId() {
        return this.productDiscountId;
    }

    public void setProductDiscountId(Long productDiscountId) {
        this.productDiscountId = productDiscountId;
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

    @Column(name = "modify_date")
    public LocalDateTime getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Product (");

        sb.append(productId);
        sb.append(", ").append(productName);
        sb.append(", ").append(productSourceId);
        sb.append(", ").append(productCategoryId);
        sb.append(", ").append(productType);
        sb.append(", ").append(productStatus);
        sb.append(", ").append(productPricingId);
        sb.append(", ").append(productDiscountId);
        sb.append(", ").append(creatorId);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifierId);
        sb.append(", ").append(modifyDate);

        sb.append(")");
        return sb.toString();
    }
}