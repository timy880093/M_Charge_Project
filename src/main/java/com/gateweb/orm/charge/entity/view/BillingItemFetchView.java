/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity.view;


import com.gateweb.charge.enumeration.BillingItemType;
import com.gateweb.charge.enumeration.ChargePlan;
import com.gateweb.charge.enumeration.PaidPlan;
import com.gateweb.orm.charge.entity.Deduct;
import com.gateweb.orm.charge.entity.ProductPurchase;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(name = "billing_item", schema = "public", indexes = {
        @Index(name = "billing_item_pkey", unique = true, columnList = "billing_item_id ASC")
})
@Immutable
public class BillingItemFetchView implements Serializable {

    private static final long serialVersionUID = -766860968;

    private Long billingItemId;
    private Integer count;
    private BigDecimal taxExcludedAmount;
    private Timestamp calculateFromDate;
    private Timestamp calculateToDate;
    private Timestamp createDate;
    private LocalDateTime expectedOutDate;
    private ChargePlan chargePlan;
    private BillingItemType billingItemType;
    private PaidPlan paidPlan;
    private Long billId;
    private Boolean isMemo;
    private SimpleCompanyFetchView company;
    private ProductPurchase productPurchase;
    private PackageRefFetchView packageRef;
    private Deduct deduct;
    private String remark;

    public BillingItemFetchView() {
    }

    public BillingItemFetchView(Long billingItemId, Integer count, BigDecimal taxExcludedAmount, Timestamp calculateFromDate, Timestamp calculateToDate, Timestamp createDate, LocalDateTime expectedOutDate, ChargePlan chargePlan, BillingItemType billingItemType, PaidPlan paidPlan, Long billId, Boolean isMemo, SimpleCompanyFetchView company, ProductPurchase productPurchase, PackageRefFetchView packageRef, Deduct deduct, String remark) {
        this.billingItemId = billingItemId;
        this.count = count;
        this.taxExcludedAmount = taxExcludedAmount;
        this.calculateFromDate = calculateFromDate;
        this.calculateToDate = calculateToDate;
        this.createDate = createDate;
        this.expectedOutDate = expectedOutDate;
        this.chargePlan = chargePlan;
        this.billingItemType = billingItemType;
        this.paidPlan = paidPlan;
        this.billId = billId;
        this.isMemo = isMemo;
        this.company = company;
        this.productPurchase = productPurchase;
        this.packageRef = packageRef;
        this.deduct = deduct;
        this.remark = remark;
    }

    @Id
    @Column(name = "billing_item_id", unique = true, nullable = false, precision = 64)
    public Long getBillingItemId() {
        return billingItemId;
    }

    public void setBillingItemId(Long billingItemId) {
        this.billingItemId = billingItemId;
    }

    @Column(name = "count", precision = 32)
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Column(name = "tax_excluded_amount", precision = 23, scale = 4)
    public BigDecimal getTaxExcludedAmount() {
        return taxExcludedAmount;
    }

    public void setTaxExcludedAmount(BigDecimal taxExcludedAmount) {
        this.taxExcludedAmount = taxExcludedAmount;
    }

    @Column(name = "calculate_from_date")
    public Timestamp getCalculateFromDate() {
        return calculateFromDate;
    }

    public void setCalculateFromDate(Timestamp calculateFromDate) {
        this.calculateFromDate = calculateFromDate;
    }

    @Column(name = "calculate_to_date")
    public Timestamp getCalculateToDate() {
        return calculateToDate;
    }

    public void setCalculateToDate(Timestamp calculateToDate) {
        this.calculateToDate = calculateToDate;
    }

    @Column(name = "create_date")
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_item_type")
    public BillingItemType getBillingItemType() {
        return billingItemType;
    }

    public void setBillingItemType(BillingItemType billingItemType) {
        this.billingItemType = billingItemType;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "paid_plan")
    public PaidPlan getPaidPlan() {
        return this.paidPlan;
    }

    public void setPaidPlan(PaidPlan paidPlan) {
        this.paidPlan = paidPlan;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "charge_plan")
    public ChargePlan getChargePlan() {
        return this.chargePlan;
    }

    public void setChargePlan(ChargePlan chargePlan) {
        this.chargePlan = chargePlan;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Column(name = "bill_id", precision = 64)
    public Long getBillId() {
        return this.billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    @Column(name = "is_memo")
    public Boolean getMemo() {
        return isMemo;
    }

    public void setMemo(Boolean memo) {
        isMemo = memo;
    }

    @Column(name = "expected_out_date")
    public LocalDateTime getExpectedOutDate() {
        return expectedOutDate;
    }

    public void setExpectedOutDate(LocalDateTime expectedOutDate) {
        this.expectedOutDate = expectedOutDate;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    public SimpleCompanyFetchView getCompany() {
        return company;
    }

    public void setCompany(SimpleCompanyFetchView company) {
        this.company = company;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "package_ref_id")
    public PackageRefFetchView getPackageRef() {
        return packageRef;
    }

    public void setPackageRef(PackageRefFetchView packageRef) {
        this.packageRef = packageRef;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_purchase_id")
    public ProductPurchase getProductPurchase() {
        return productPurchase;
    }

    public void setProductPurchase(ProductPurchase productPurchase) {
        this.productPurchase = productPurchase;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "deduct_id")
    public Deduct getDeduct() {
        return deduct;
    }

    public void setDeduct(Deduct deduct) {
        this.deduct = deduct;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "BillingItemFetchView{" +
                "billingItemId=" + billingItemId +
                ", count=" + count +
                ", taxExcludedAmount=" + taxExcludedAmount +
                ", calculateFromDate=" + calculateFromDate +
                ", calculateToDate=" + calculateToDate +
                ", createDate=" + createDate +
                ", expectedOutDate=" + expectedOutDate +
                ", chargePlan=" + chargePlan +
                ", billingItemType=" + billingItemType +
                ", paidPlan=" + paidPlan +
                ", billId=" + billId +
                ", isMemo=" + isMemo +
                ", company=" + company +
                ", productPurchase=" + productPurchase +
                ", packageRef=" + packageRef +
                ", deduct=" + deduct +
                ", remark='" + remark + '\'' +
                '}';
    }
}
