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
@Table(name = "product_purchase", schema = "public", indexes = {
    @Index(name = "product_purchase_pkey", unique = true, columnList = "product_purchase_id ASC")
})
public class ProductPurchase implements Serializable {

    private static final long serialVersionUID = 1534587519;

    private Long       productPurchaseId;
    private Long       companyId;
    private Long       productId;
    private BigDecimal salesPrice;
    private Timestamp  purchaseDate;
    private Integer    purchaseQuantity;

    public ProductPurchase() {}

    public ProductPurchase(ProductPurchase value) {
        this.productPurchaseId = value.productPurchaseId;
        this.companyId = value.companyId;
        this.productId = value.productId;
        this.salesPrice = value.salesPrice;
        this.purchaseDate = value.purchaseDate;
        this.purchaseQuantity = value.purchaseQuantity;
    }

    public ProductPurchase(
        Long       productPurchaseId,
        Long       companyId,
        Long       productId,
        BigDecimal salesPrice,
        Timestamp  purchaseDate,
        Integer    purchaseQuantity
    ) {
        this.productPurchaseId = productPurchaseId;
        this.companyId = companyId;
        this.productId = productId;
        this.salesPrice = salesPrice;
        this.purchaseDate = purchaseDate;
        this.purchaseQuantity = purchaseQuantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_purchase_id", unique = true, nullable = false, precision = 64)
    public Long getProductPurchaseId() {
        return this.productPurchaseId;
    }

    public void setProductPurchaseId(Long productPurchaseId) {
        this.productPurchaseId = productPurchaseId;
    }

    @Column(name = "company_id", precision = 64)
    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Column(name = "product_id", precision = 64)
    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "sales_price", precision = 23, scale = 4)
    public BigDecimal getSalesPrice() {
        return this.salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    @Column(name = "purchase_date")
    public Timestamp getPurchaseDate() {
        return this.purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Column(name = "purchase_quantity", precision = 32)
    public Integer getPurchaseQuantity() {
        return this.purchaseQuantity;
    }

    public void setPurchaseQuantity(Integer purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ProductPurchase (");

        sb.append(productPurchaseId);
        sb.append(", ").append(companyId);
        sb.append(", ").append(productId);
        sb.append(", ").append(salesPrice);
        sb.append(", ").append(purchaseDate);
        sb.append(", ").append(purchaseQuantity);

        sb.append(")");
        return sb.toString();
    }
}
