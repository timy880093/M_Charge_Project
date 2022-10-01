/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "deduct_history", schema = "public", uniqueConstraints = {
    @UniqueConstraint(name = "deduct_history_pkey", columnNames = {"deduct_history_id"})
})
public class DeductHistory implements Serializable {

    private static final long serialVersionUID = 834753090;

    private Long          deductHistoryId;
    private Long          deductId;
    private Long          billId;
    private Long          deductBillingItemId;
    private BigDecimal    amount;
    private LocalDateTime createDate;

    public DeductHistory() {}

    public DeductHistory(DeductHistory value) {
        this.deductHistoryId = value.deductHistoryId;
        this.deductId = value.deductId;
        this.billId = value.billId;
        this.deductBillingItemId = value.deductBillingItemId;
        this.amount = value.amount;
        this.createDate = value.createDate;
    }

    public DeductHistory(
        Long          deductHistoryId,
        Long          deductId,
        Long          billId,
        Long          deductBillingItemId,
        BigDecimal    amount,
        LocalDateTime createDate
    ) {
        this.deductHistoryId = deductHistoryId;
        this.deductId = deductId;
        this.billId = billId;
        this.deductBillingItemId = deductBillingItemId;
        this.amount = amount;
        this.createDate = createDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deduct_history_id", nullable = false, precision = 64)
    public Long getDeductHistoryId() {
        return this.deductHistoryId;
    }

    public void setDeductHistoryId(Long deductHistoryId) {
        this.deductHistoryId = deductHistoryId;
    }

    @Column(name = "deduct_id", precision = 64)
    public Long getDeductId() {
        return this.deductId;
    }

    public void setDeductId(Long deductId) {
        this.deductId = deductId;
    }

    @Column(name = "bill_id", precision = 64)
    public Long getBillId() {
        return this.billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    @Column(name = "deduct_billing_item_id", precision = 64)
    public Long getDeductBillingItemId() {
        return this.deductBillingItemId;
    }

    public void setDeductBillingItemId(Long deductBillingItemId) {
        this.deductBillingItemId = deductBillingItemId;
    }

    @Column(name = "amount", precision = 23, scale = 4)
    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "create_date")
    public LocalDateTime getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DeductHistory (");

        sb.append(deductHistoryId);
        sb.append(", ").append(deductId);
        sb.append(", ").append(billId);
        sb.append(", ").append(deductBillingItemId);
        sb.append(", ").append(amount);
        sb.append(", ").append(createDate);

        sb.append(")");
        return sb.toString();
    }
}