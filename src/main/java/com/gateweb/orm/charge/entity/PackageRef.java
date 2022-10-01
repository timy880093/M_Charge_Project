/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import javax.persistence.*;
import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(name = "package_ref", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "package_ref_pkey", columnNames = {"package_ref_id"})
})
public class PackageRef implements Serializable {

    private static final long serialVersionUID = 1240006332;

    private Long packageRefId;
    private Long toProductId;
    private Integer toProductQuantity;
    private Long toChargeRuleId;
    private Long fromPackageId;

    public PackageRef() {
    }

    public PackageRef(Long packageRefId, Long toProductId, Integer toProductQuantity, Long toChargeRuleId, Long fromPackageId) {
        this.packageRefId = packageRefId;
        this.toProductId = toProductId;
        this.toProductQuantity = toProductQuantity;
        this.toChargeRuleId = toChargeRuleId;
        this.fromPackageId = fromPackageId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_ref_id", nullable = false, precision = 64)
    public Long getPackageRefId() {
        return this.packageRefId;
    }

    public void setPackageRefId(Long packageRefId) {
        this.packageRefId = packageRefId;
    }

    @Column(name = "to_product_id", precision = 64)
    public Long getToProductId() {
        return this.toProductId;
    }

    public void setToProductId(Long toProductId) {
        this.toProductId = toProductId;
    }

    @Column(name = "to_product_quantity", precision = 32)
    public Integer getToProductQuantity() {
        return this.toProductQuantity;
    }

    public void setToProductQuantity(Integer toProductQuantity) {
        this.toProductQuantity = toProductQuantity;
    }

    @Column(name = "to_charge_rule_id", precision = 64)
    public Long getToChargeRuleId() {
        return toChargeRuleId;
    }

    public void setToChargeRuleId(Long toChargeRuleId) {
        this.toChargeRuleId = toChargeRuleId;
    }

    @Column(name = "from_package_id", precision = 64)
    public Long getFromPackageId() {
        return this.fromPackageId;
    }

    public void setFromPackageId(Long fromPackageId) {
        this.fromPackageId = fromPackageId;
    }

    @Override
    public String toString() {
        return "PackageRef{" +
                "packageRefId=" + packageRefId +
                ", toProductId=" + toProductId +
                ", toProductQuantity=" + toProductQuantity +
                ", toChargeRuleId=" + toChargeRuleId +
                ", fromPackageId=" + fromPackageId +
                '}';
    }
}