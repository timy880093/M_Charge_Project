package com.gateweb.charge.contract.bean.request;

import java.time.LocalDateTime;

public class ContractSaveReq {
    Long contractId;
    Long companyId;
    Long packageId;
    Long renewPackageId;
    String contractName;
    Boolean autoRenew;
    Boolean firstContract;
    Boolean firstInvoiceDateAsEffectiveDate;
    Integer periodMonth;
    LocalDateTime effectiveDate;
    LocalDateTime expirationDate;
    LocalDateTime installationDate;
    Long callerId;

    public ContractSaveReq() {
    }

    public ContractSaveReq(Long contractId, Long companyId, Long packageId, Long renewPackageId, String contractName, Boolean autoRenew, Boolean firstContract, Boolean firstInvoiceDateAsEffectiveDate, Integer periodMonth, LocalDateTime effectiveDate, LocalDateTime expirationDate, LocalDateTime installationDate, Long callerId) {
        this.contractId = contractId;
        this.companyId = companyId;
        this.packageId = packageId;
        this.renewPackageId = renewPackageId;
        this.contractName = contractName;
        this.autoRenew = autoRenew;
        this.firstContract = firstContract;
        this.firstInvoiceDateAsEffectiveDate = firstInvoiceDateAsEffectiveDate;
        this.periodMonth = periodMonth;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.installationDate = installationDate;
        this.callerId = callerId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Long getRenewPackageId() {
        return renewPackageId;
    }

    public void setRenewPackageId(Long renewPackageId) {
        this.renewPackageId = renewPackageId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public Boolean getFirstContract() {
        return firstContract;
    }

    public void setFirstContract(Boolean firstContract) {
        this.firstContract = firstContract;
    }

    public Boolean getFirstInvoiceDateAsEffectiveDate() {
        return firstInvoiceDateAsEffectiveDate;
    }

    public void setFirstInvoiceDateAsEffectiveDate(Boolean firstInvoiceDateAsEffectiveDate) {
        this.firstInvoiceDateAsEffectiveDate = firstInvoiceDateAsEffectiveDate;
    }

    public Integer getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(Integer periodMonth) {
        this.periodMonth = periodMonth;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDateTime getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDateTime installationDate) {
        this.installationDate = installationDate;
    }

    public Long getCallerId() {
        return callerId;
    }

    public void setCallerId(Long callerId) {
        this.callerId = callerId;
    }
}

