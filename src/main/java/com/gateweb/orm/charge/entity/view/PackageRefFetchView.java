/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity.view;


import com.gateweb.orm.hibernateExtension.PostgreSQLEnumType;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.hibernate.annotations.TypeDef;

import javax.annotation.Generated;
import javax.persistence.*;
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
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(name = "package_ref", schema = "public", indexes = {
        @Index(name = "package_ref_pkey", unique = true, columnList = "package_ref_id ASC")
})
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@Immutable
public class PackageRefFetchView implements Serializable {

    private static final long serialVersionUID = -196121216;

    private Long packageRefId;
    private ProductFetchView product;
    private ChargeRuleFetchView chargeRule;
    private Long fromPackageId;

    public PackageRefFetchView() {
    }

    public PackageRefFetchView(Long packageRefId, ProductFetchView product, ChargeRuleFetchView chargeRule, Long fromPackageId) {
        this.packageRefId = packageRefId;
        this.product = product;
        this.chargeRule = chargeRule;
        this.fromPackageId = fromPackageId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_ref_id", unique = true, nullable = false, precision = 64)
    public Long getPackageRefId() {
        return packageRefId;
    }

    public void setPackageRefId(Long packageRefId) {
        this.packageRefId = packageRefId;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_charge_rule_id")
    public ChargeRuleFetchView getChargeRule() {
        return chargeRule;
    }

    public void setChargeRule(ChargeRuleFetchView chargeRule) {
        this.chargeRule = chargeRule;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_product_id")
    public ProductFetchView getProduct() {
        return product;
    }

    public void setProduct(ProductFetchView product) {
        this.product = product;
    }

    @Column(name = "from_package_id", precision = 64)
    public Long getFromPackageId() {
        return fromPackageId;
    }

    public void setFromPackageId(Long fromPackageId) {
        this.fromPackageId = fromPackageId;
    }

    @Override
    public String toString() {
        return "PackageRefFetchView{" +
                "packageRefId=" + packageRefId +
                ", product=" + product +
                ", chargeRule=" + chargeRule +
                ", fromPackageId=" + fromPackageId +
                '}';
    }
}