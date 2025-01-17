/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "contract_history", schema = "public", uniqueConstraints = {
    @UniqueConstraint(name = "contract_history_pkey", columnNames = {"contract_history_id"})
})
public class ContractHistory implements Serializable {

    private static final long serialVersionUID = 1465612394;

    private Long          contractHistoryId;
    private Long          contractId;
    private String        name;
    private Long          companyId;
    private Boolean       autoRecreate;
    private Long          packageId;
    private LocalDateTime effectiveDate;
    private LocalDateTime expirationDate;
    private LocalDateTime realStartDate;
    private LocalDateTime realEndDate;
    private Long          creatorId;
    private LocalDateTime createDate;
    private Boolean       firstInvoiceDateAsEffectiveDate;
    private String        status;

    public ContractHistory() {}

    public ContractHistory(ContractHistory value) {
        this.contractHistoryId = value.contractHistoryId;
        this.contractId = value.contractId;
        this.name = value.name;
        this.companyId = value.companyId;
        this.autoRecreate = value.autoRecreate;
        this.packageId = value.packageId;
        this.effectiveDate = value.effectiveDate;
        this.expirationDate = value.expirationDate;
        this.realStartDate = value.realStartDate;
        this.realEndDate = value.realEndDate;
        this.creatorId = value.creatorId;
        this.createDate = value.createDate;
        this.firstInvoiceDateAsEffectiveDate = value.firstInvoiceDateAsEffectiveDate;
        this.status = value.status;
    }

    public ContractHistory(
        Long          contractHistoryId,
        Long          contractId,
        String        name,
        Long          companyId,
        Boolean       autoRecreate,
        Long          packageId,
        LocalDateTime effectiveDate,
        LocalDateTime expirationDate,
        LocalDateTime realStartDate,
        LocalDateTime realEndDate,
        Long          creatorId,
        LocalDateTime createDate,
        Boolean       firstInvoiceDateAsEffectiveDate,
        String        status
    ) {
        this.contractHistoryId = contractHistoryId;
        this.contractId = contractId;
        this.name = name;
        this.companyId = companyId;
        this.autoRecreate = autoRecreate;
        this.packageId = packageId;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.realStartDate = realStartDate;
        this.realEndDate = realEndDate;
        this.creatorId = creatorId;
        this.createDate = createDate;
        this.firstInvoiceDateAsEffectiveDate = firstInvoiceDateAsEffectiveDate;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_history_id", nullable = false, precision = 64)
    public Long getContractHistoryId() {
        return this.contractHistoryId;
    }

    public void setContractHistoryId(Long contractHistoryId) {
        this.contractHistoryId = contractHistoryId;
    }

    @Column(name = "contract_id", precision = 64)
    public Long getContractId() {
        return this.contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    @Column(name = "name", length = 50)
    @Size(max = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "company_id", precision = 64)
    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Column(name = "auto_renew")
    public Boolean getAutoRecreate() {
        return this.autoRecreate;
    }

    public void setAutoRecreate(Boolean autoRecreate) {
        this.autoRecreate = autoRecreate;
    }

    @Column(name = "package_id", precision = 64)
    public Long getPackageId() {
        return this.packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    @Column(name = "effective_date")
    public LocalDateTime getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Column(name = "expiration_date")
    public LocalDateTime getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Column(name = "real_start_date")
    public LocalDateTime getRealStartDate() {
        return this.realStartDate;
    }

    public void setRealStartDate(LocalDateTime realStartDate) {
        this.realStartDate = realStartDate;
    }

    @Column(name = "real_end_date")
    public LocalDateTime getRealEndDate() {
        return this.realEndDate;
    }

    public void setRealEndDate(LocalDateTime realEndDate) {
        this.realEndDate = realEndDate;
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

    @Column(name = "first_invoice_date_as_effective_date")
    public Boolean getFirstInvoiceDateAsEffectiveDate() {
        return firstInvoiceDateAsEffectiveDate;
    }

    public void setFirstInvoiceDateAsEffectiveDate(Boolean firstInvoiceDateAsEffectiveDate) {
        this.firstInvoiceDateAsEffectiveDate = firstInvoiceDateAsEffectiveDate;
    }

    @Column(name = "status", length = 30)
    @Size(max = 30)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ContractHistory (");

        sb.append(contractHistoryId);
        sb.append(", ").append(contractId);
        sb.append(", ").append(name);
        sb.append(", ").append(companyId);
        sb.append(", ").append(autoRecreate);
        sb.append(", ").append(packageId);
        sb.append(", ").append(effectiveDate);
        sb.append(", ").append(expirationDate);
        sb.append(", ").append(realStartDate);
        sb.append(", ").append(realEndDate);
        sb.append(", ").append(creatorId);
        sb.append(", ").append(createDate);
        sb.append(", ").append(firstInvoiceDateAsEffectiveDate);
        sb.append(", ").append(status);

        sb.append(")");
        return sb.toString();
    }
}
