/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@Entity(name = "chargeInvoiceAmountSummaryReport")
@Table(name = "invoice_amount_summary_report", schema = "public")
public class ChargeIasrEntity implements Serializable {

    private static final long serialVersionUID = -272534461;

    private Long id;
    private String invoiceDate;
    private String seller;
    private String buyer;
    private Integer invoiceStatus;
    private Integer amount;
    private BigDecimal total;
    private Integer creatorId;
    private Integer modifierId;
    private LocalDateTime modifyDate;
    private LocalDateTime createDate;
    private String month;
    private String source;

    public ChargeIasrEntity() {
    }

    public ChargeIasrEntity(ChargeIasrEntity value) {
        this.id = value.id;
        this.invoiceDate = value.invoiceDate;
        this.seller = value.seller;
        this.buyer = value.buyer;
        this.invoiceStatus = value.invoiceStatus;
        this.amount = value.amount;
        this.total = value.total;
        this.creatorId = value.creatorId;
        this.modifierId = value.modifierId;
        this.modifyDate = value.modifyDate;
        this.createDate = value.createDate;
        this.month = value.month;
        this.source = value.source;
    }

    public ChargeIasrEntity(
            Long id,
            String invoiceDate,
            String seller,
            String buyer,
            Integer invoiceStatus,
            Integer amount,
            BigDecimal total,
            Integer creatorId,
            Integer modifierId,
            LocalDateTime modifyDate,
            LocalDateTime createDate,
            String month,
            String source
    ) {
        this.id = id;
        this.invoiceDate = invoiceDate;
        this.seller = seller;
        this.buyer = buyer;
        this.invoiceStatus = invoiceStatus;
        this.amount = amount;
        this.total = total;
        this.creatorId = creatorId;
        this.modifierId = modifierId;
        this.modifyDate = modifyDate;
        this.createDate = createDate;
        this.month = month;
        this.source = source;
    }

    @Id
    @SequenceGenerator(name = "invoice_amount_summary_report_id_seq",
            sequenceName = "invoice_amount_summary_report_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "invoice_amount_summary_report_id_seq")
    @Column(name = "id", precision = 64)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "invoice_date", length = 8)
    @Size(max = 8)
    public String getInvoiceDate() {
        return this.invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @Column(name = "seller", length = 8)
    @Size(max = 8)
    public String getSeller() {
        return this.seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    @Column(name = "buyer", length = 10)
    @Size(max = 10)
    public String getBuyer() {
        return this.buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    @Column(name = "invoice_status", precision = 32)
    public Integer getInvoiceStatus() {
        return this.invoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    @Column(name = "amount", precision = 32)
    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Column(name = "total", precision = 53, scale = 4)
    public BigDecimal getTotal() {
        return this.total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Column(name = "creator_id", precision = 32)
    public Integer getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    @Column(name = "modifier_id", precision = 32)
    public Integer getModifierId() {
        return this.modifierId;
    }

    public void setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
    }

    @Column(name = "modify_date")
    public LocalDateTime getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Column(name = "create_date")
    public LocalDateTime getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Column(name = "month", length = 8)
    @Size(max = 8)
    public String getMonth() {
        return this.month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Column(name = "source", length = 30)
    @Size(max = 30)
    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("InvoiceAmountSummaryReport (");

        sb.append(id);
        sb.append(", ").append(invoiceDate);
        sb.append(", ").append(seller);
        sb.append(", ").append(buyer);
        sb.append(", ").append(invoiceStatus);
        sb.append(", ").append(amount);
        sb.append(", ").append(total);
        sb.append(", ").append(creatorId);
        sb.append(", ").append(modifierId);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(createDate);
        sb.append(", ").append(month);
        sb.append(", ").append(source);

        sb.append(")");
        return sb.toString();
    }
}
