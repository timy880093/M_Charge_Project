/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import javax.annotation.Generated;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;


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
@Table(name = "product_category", schema = "public", indexes = {
    @Index(name = "product_category_pkey", unique = true, columnList = "product_category_id ASC")
})
public class ProductCategory implements Serializable {

    private static final long serialVersionUID = 518426948;

    private Long   productCategoryId;
    private String categoryName;
    private Long   parentCategoryId;

    public ProductCategory() {}

    public ProductCategory(ProductCategory value) {
        this.productCategoryId = value.productCategoryId;
        this.categoryName = value.categoryName;
        this.parentCategoryId = value.parentCategoryId;
    }

    public ProductCategory(
        Long   productCategoryId,
        String categoryName,
        Long   parentCategoryId
    ) {
        this.productCategoryId = productCategoryId;
        this.categoryName = categoryName;
        this.parentCategoryId = parentCategoryId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_id", unique = true, nullable = false, precision = 64)
    public Long getProductCategoryId() {
        return this.productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    @Column(name = "category_name", length = 30)
    @Size(max = 30)
    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Column(name = "parent_category_id", precision = 64)
    public Long getParentCategoryId() {
        return this.parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ProductCategory (");

        sb.append(productCategoryId);
        sb.append(", ").append(categoryName);
        sb.append(", ").append(parentCategoryId);

        sb.append(")");
        return sb.toString();
    }
}