/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import com.gateweb.charge.enumeration.NoticeStatus;
import com.gateweb.charge.enumeration.NoticeType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(name = "notice", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "notice_pkey", columnNames = {"notice_id"})
})
public class Notice implements Serializable {

    private static final long serialVersionUID = 2103404378;

    private Long noticeId;
    private NoticeType noticeType;
    private Long companyId;
    private Long billId;
    private Long contractId;
    private Boolean requestBySystem;
    private NoticeStatus noticeStatus;
    private String recipient;
    private Long creatorId;
    private LocalDateTime createDate;
    private Long modifierId;
    private LocalDateTime modifyDate;
    private Long invoiceRemainingId;
    private String customJson;

    public Notice() {
    }

    public Notice(Notice value) {
        this.noticeId = value.noticeId;
        this.noticeType = value.noticeType;
        this.companyId = value.companyId;
        this.billId = value.billId;
        this.contractId = value.contractId;
        this.requestBySystem = value.requestBySystem;
        this.noticeStatus = value.noticeStatus;
        this.recipient = value.recipient;
        this.creatorId = value.creatorId;
        this.createDate = value.createDate;
        this.modifierId = value.modifierId;
        this.modifyDate = value.modifyDate;
        this.invoiceRemainingId = value.invoiceRemainingId;
        this.customJson = value.customJson;
    }

    public Notice(
            Long noticeId,
            NoticeType noticeType,
            Long companyId,
            Long billId,
            Long contractId,
            Boolean requestBySystem,
            NoticeStatus noticeStatus,
            String recipient,
            Long creatorId,
            LocalDateTime createDate,
            Long modifierId,
            LocalDateTime modifyDate,
            Long invoiceRemainingId,
            String customJson
    ) {
        this.noticeId = noticeId;
        this.noticeType = noticeType;
        this.companyId = companyId;
        this.billId = billId;
        this.contractId = contractId;
        this.requestBySystem = requestBySystem;
        this.noticeStatus = noticeStatus;
        this.recipient = recipient;
        this.creatorId = creatorId;
        this.createDate = createDate;
        this.modifierId = modifierId;
        this.modifyDate = modifyDate;
        this.invoiceRemainingId = invoiceRemainingId;
        this.customJson = customJson;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", nullable = false, precision = 64)
    public Long getNoticeId() {
        return this.noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "notice_type")
    public NoticeType getNoticeType() {
        return this.noticeType;
    }

    public void setNoticeType(NoticeType noticeType) {
        this.noticeType = noticeType;
    }

    @Column(name = "company_id", precision = 64)
    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Column(name = "bill_id", precision = 64)
    public Long getBillId() {
        return this.billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    @Column(name = "contract_id", precision = 64)
    public Long getContractId() {
        return this.contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    @Column(name = "request_by_system")
    public Boolean getRequestBySystem() {
        return this.requestBySystem;
    }

    public void setRequestBySystem(Boolean requestBySystem) {
        this.requestBySystem = requestBySystem;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "notice_status")
    public NoticeStatus getNoticeStatus() {
        return this.noticeStatus;
    }

    public void setNoticeStatus(NoticeStatus noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    @Column(name = "recipient", length = 200)
    @Size(max = 200)
    public String getRecipient() {
        return this.recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
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

    @Column(name = "invoice_remaining_id", precision = 64)
    public Long getInvoiceRemainingId() {
        return this.invoiceRemainingId;
    }

    public void setInvoiceRemainingId(Long invoiceRemainingId) {
        this.invoiceRemainingId = invoiceRemainingId;
    }

    @Column(name = "custom_json")
    public String getCustomJson() {
        return customJson;
    }

    public void setCustomJson(String customJson) {
        this.customJson = customJson;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Notice (");

        sb.append(noticeId);
        sb.append(", ").append(noticeType);
        sb.append(", ").append(companyId);
        sb.append(", ").append(billId);
        sb.append(", ").append(contractId);
        sb.append(", ").append(requestBySystem);
        sb.append(", ").append(noticeStatus);
        sb.append(", ").append(recipient);
        sb.append(", ").append(creatorId);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifierId);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(invoiceRemainingId);
        sb.append(", ").append(customJson);

        sb.append(")");
        return sb.toString();
    }
}