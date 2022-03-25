package com.gateweb.orm.charge.entity.view;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "deduct_purchase", schema = "public", indexes = {
        @Index(name = "deduct_purchase_pkey", unique = true, columnList = "purchase_id ASC")
})
public class DeductPurchaseView {

    private Long purchaseId;
    private Long companyId;
    private String deductBase;
    private String deductType;
    private String targetType;
    private Long targetId;
    private String chargeType;
    private BigDecimal amount;
    private BigDecimal salesPrice;
    private Timestamp createDate;
    private String status;
    private SimpleUserView creator;
    private Timestamp modifyDate;
    private SimpleUserView modifier;

    public DeductPurchaseView() {
    }

    public DeductPurchaseView(DeductPurchaseView value) {
        this.purchaseId = value.purchaseId;
        this.companyId = value.companyId;
        this.deductType = value.deductType;
        this.targetType = value.targetType;
        this.targetId = value.targetId;
        this.chargeType = value.chargeType;
        this.amount = value.amount;
        this.salesPrice = value.salesPrice;
        this.createDate = value.createDate;
        this.status = value.status;
        this.creator = value.creator;
        this.modifyDate = value.modifyDate;
        this.modifier = value.modifier;
        this.deductBase = value.deductBase;
    }

    public DeductPurchaseView(
            Long purchaseId,
            Long companyId,
            String deductType,
            String targetType,
            Long targetId,
            String chargeType,
            BigDecimal amount,
            BigDecimal salesPrice,
            Timestamp createDate,
            String status,
            SimpleUserView creator,
            Timestamp modifyDate,
            SimpleUserView modifier,
            String deductBase
    ) {
        this.purchaseId = purchaseId;
        this.companyId = companyId;
        this.deductType = deductType;
        this.targetType = targetType;
        this.targetId = targetId;
        this.chargeType = chargeType;
        this.amount = amount;
        this.salesPrice = salesPrice;
        this.createDate = createDate;
        this.status = status;
        this.creator = creator;
        this.modifyDate = modifyDate;
        this.modifier = modifier;
        this.deductBase = deductBase;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id", unique = true, nullable = false, precision = 64)
    public Long getPurchaseId() {
        return this.purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    @Column(name = "company_id", nullable = false, precision = 64)
    @NotNull
    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Column(name = "deduct_type", length = 30)
    @Size(max = 30)
    public String getDeductType() {
        return this.deductType;
    }

    public void setDeductType(String deductType) {
        this.deductType = deductType;
    }

    @Column(name = "target_type", length = 30)
    @Size(max = 30)
    public String getTargetType() {
        return this.targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    @Column(name = "target_id", precision = 64)
    public Long getTargetId() {
        return this.targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    @Column(name = "charge_type", length = 30)
    @Size(max = 30)
    public String getChargeType() {
        return this.chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    @Column(name = "amount", precision = 23, scale = 4)
    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "sales_price", precision = 23, scale = 4)
    public BigDecimal getSalesPrice() {
        return this.salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    @Column(name = "create_date", nullable = false)
    @NotNull
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Column(name = "status", nullable = false, length = 1)
    @NotNull
    @Size(max = 1)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @OneToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    public SimpleUserView getCreator() {
        return creator;
    }

    public void setCreator(SimpleUserView creator) {
        this.creator = creator;
    }

    @Column(name = "modify_date")
    public Timestamp getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    @OneToOne
    @JoinColumn(name = "modifier_id", referencedColumnName = "user_id")
    public SimpleUserView getModifier() {
        return modifier;
    }

    public void setModifier(SimpleUserView modifier) {
        this.modifier = modifier;
    }

    @Column(name = "deduct_base", length = 35)
    @Size(max = 35)
    public String getDeductBase() {
        return this.deductBase;
    }

    public void setDeductBase(String deductBase) {
        this.deductBase = deductBase;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DeductPurchases (");

        sb.append(purchaseId);
        sb.append(", ").append(companyId);
        sb.append(", ").append(deductType);
        sb.append(", ").append(targetType);
        sb.append(", ").append(targetId);
        sb.append(", ").append(chargeType);
        sb.append(", ").append(amount);
        sb.append(", ").append(salesPrice);
        sb.append(", ").append(createDate);
        sb.append(", ").append(status);
        sb.append(", ").append(creator);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(modifier);
        sb.append(", ").append(deductBase);

        sb.append(")");
        return sb.toString();
    }
}
