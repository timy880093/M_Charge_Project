/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity.view;


import com.gateweb.orm.charge.entity.Deduct;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


@SqlResultSetMapping(name = "DeductHistoryFetchViewMapping",
        classes = {
                @ConstructorResult(targetClass = DeductHistoryFetchView.class,
                        columns = {@ColumnResult(name = "name"), @ColumnResult(name = "street")}
                )}
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(name = "deduct_history", schema = "public", indexes = {
        @Index(name = "deduct_history_pkey", unique = true, columnList = "deduct_history_id ASC")
})
@Immutable
public class DeductHistoryFetchView implements Serializable {

    private static final long serialVersionUID = -967486316;

    private Long deductHistoryId;
    private Deduct deduct;
    private BigDecimal amount;
    private BillingItemFetchView billingItem;
    private Timestamp createDate;

    public DeductHistoryFetchView() {
    }

    public DeductHistoryFetchView(Long deductHistoryId, Deduct deduct, BigDecimal amount, BillingItemFetchView billingItem, Timestamp createDate) {
        this.deductHistoryId = deductHistoryId;
        this.deduct = deduct;
        this.amount = amount;
        this.billingItem = billingItem;
        this.createDate = createDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deduct_history_id", unique = true, nullable = false, precision = 64)
    public Long getDeductHistoryId() {
        return this.deductHistoryId;
    }

    public void setDeductHistoryId(Long deductHistoryId) {
        this.deductHistoryId = deductHistoryId;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "deduct_id")
    public Deduct getDeduct() {
        return deduct;
    }

    public void setDeduct(Deduct deduct) {
        this.deduct = deduct;
    }

    @Column(name = "amount", precision = 23, scale = 4)
    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "create_date")
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "deduct_billing_item_id", referencedColumnName = "billing_item_id", insertable = false, updatable = false)
    @BatchSize(size = 10)
    @NotFound(action = NotFoundAction.IGNORE)
    public BillingItemFetchView getBillingItem() {
        return billingItem;
    }

    public void setBillingItem(BillingItemFetchView billingItem) {
        this.billingItem = billingItem;
    }

    @Override
    public String toString() {
        return "DeductHistoryFetchView{" +
                "deductHistoryId=" + deductHistoryId +
                ", deduct=" + deduct +
                ", amount=" + amount +
                ", billingItem=" + billingItem +
                ", createDate=" + createDate +
                '}';
    }
}