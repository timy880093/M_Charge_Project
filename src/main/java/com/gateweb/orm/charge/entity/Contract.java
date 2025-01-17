/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import com.gateweb.charge.enumeration.ContractStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(name = "contract", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "contract_pkey", columnNames = {"contract_id"})
})
public class Contract implements Serializable {

    private static final long serialVersionUID = 991731094;

    private Long contractId;
    private String name;
    private Long companyId;
    private Long packageId;
    private Boolean autoRenew;
    private LocalDateTime effectiveDate;
    private LocalDateTime expirationDate;
    private LocalDateTime settleDate;
    private Integer periodMonth;
    private Long renewPackageId;
    private Long creatorId;
    private LocalDateTime createDate;
    private Boolean firstInvoiceDateAsEffectiveDate;
    private Boolean isFirstContract;
    private Boolean isMonopoly;
    private Boolean allowPartialBilling;
    private LocalDateTime installationDate;
    private ContractStatus status;
    private String remark;

    public Contract() {
    }

    public Contract(Long contractId, String name, Long companyId, Long packageId, Boolean autoRenew, LocalDateTime effectiveDate, LocalDateTime expirationDate, LocalDateTime settleDate, Integer periodMonth, Long renewPackageId, Long creatorId, LocalDateTime createDate, Boolean firstInvoiceDateAsEffectiveDate, Boolean isFirstContract, Boolean isMonopoly, Boolean allowPartialBilling, LocalDateTime installationDate, ContractStatus status, String remark) {
        this.contractId = contractId;
        this.name = name;
        this.companyId = companyId;
        this.packageId = packageId;
        this.autoRenew = autoRenew;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.settleDate = settleDate;
        this.periodMonth = periodMonth;
        this.renewPackageId = renewPackageId;
        this.creatorId = creatorId;
        this.createDate = createDate;
        this.firstInvoiceDateAsEffectiveDate = firstInvoiceDateAsEffectiveDate;
        this.isFirstContract = isFirstContract;
        this.isMonopoly = isMonopoly;
        this.allowPartialBilling = allowPartialBilling;
        this.installationDate = installationDate;
        this.status = status;
        this.remark = remark;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id", nullable = false, precision = 64)
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

    @Column(name = "package_id", precision = 64)
    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    @Column(name = "auto_renew")
    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
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

    @Column(name = "settle_date")
    public LocalDateTime getSettleDate() {
        return this.settleDate;
    }

    public void setSettleDate(LocalDateTime settleDate) {
        this.settleDate = settleDate;
    }

    @Column(name = "period_month", precision = 32)
    public Integer getPeriodMonth() {
        return this.periodMonth;
    }

    public void setPeriodMonth(Integer periodMonth) {
        this.periodMonth = periodMonth;
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
        return this.firstInvoiceDateAsEffectiveDate;
    }

    public void setFirstInvoiceDateAsEffectiveDate(Boolean firstInvoiceDateAsEffectiveDate) {
        this.firstInvoiceDateAsEffectiveDate = firstInvoiceDateAsEffectiveDate;
    }

    @Column(name = "is_first_contract")
    public Boolean getIsFirstContract() {
        return this.isFirstContract;
    }

    public void setIsFirstContract(Boolean isFirstContract) {
        this.isFirstContract = isFirstContract;
    }

    @Column(name = "is_monopoly")
    public Boolean getIsMonopoly() {
        return this.isMonopoly;
    }

    public void setIsMonopoly(Boolean isMonopoly) {
        this.isMonopoly = isMonopoly;
    }

    @Column(name = "allow_partial_billing")
    public Boolean getAllowPartialBilling() {
        return this.allowPartialBilling;
    }

    public void setAllowPartialBilling(Boolean allowPartialBilling) {
        this.allowPartialBilling = allowPartialBilling;
    }

    @Column(name = "installation_date")
    public LocalDateTime getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDateTime installationDate) {
        this.installationDate = installationDate;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public ContractStatus getStatus() {
        return status;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    @Column(name = "renew_package_id")
    public Long getRenewPackageId() {
        return renewPackageId;
    }

    public void setRenewPackageId(Long renewPackageId) {
        this.renewPackageId = renewPackageId;
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
        return "Contract{" +
                "contractId=" + contractId +
                ", name='" + name + '\'' +
                ", companyId=" + companyId +
                ", packageId=" + packageId +
                ", autoRenew=" + autoRenew +
                ", effectiveDate=" + effectiveDate +
                ", expirationDate=" + expirationDate +
                ", settleDate=" + settleDate +
                ", periodMonth=" + periodMonth +
                ", renewPackageId=" + renewPackageId +
                ", creatorId=" + creatorId +
                ", createDate=" + createDate +
                ", firstInvoiceDateAsEffectiveDate=" + firstInvoiceDateAsEffectiveDate +
                ", isFirstContract=" + isFirstContract +
                ", isMonopoly=" + isMonopoly +
                ", allowPartialBilling=" + allowPartialBilling +
                ", installationDate=" + installationDate +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                '}';
    }
}
