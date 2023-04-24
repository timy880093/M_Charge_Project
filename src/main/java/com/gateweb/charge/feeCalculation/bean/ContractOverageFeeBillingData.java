package com.gateweb.charge.feeCalculation.bean;

import com.gateweb.charge.chargePolicy.bean.ChargePolicy;
import com.gateweb.charge.feeCalculation.dataCounter.DataCounter;
import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.orm.charge.entity.InvoiceRemaining;

import java.util.Optional;

public class ContractOverageFeeBillingData {
    Long companyId;
    Long contractId;
    String businessNo;
    Long packageRefId;
    ChargePolicy chargePolicy;
    CustomInterval calculateInterval;
    DataCounter dataCounter;
    Optional<InvoiceRemaining> previousInvoiceRemaining;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getPackageRefId() {
        return packageRefId;
    }

    public void setPackageRefId(Long packageRefId) {
        this.packageRefId = packageRefId;
    }

    public ChargePolicy getChargePolicy() {
        return chargePolicy;
    }

    public void setChargePolicy(ChargePolicy chargePolicy) {
        this.chargePolicy = chargePolicy;
    }

    public CustomInterval getCalculateInterval() {
        return calculateInterval;
    }

    public void setCalculateInterval(CustomInterval calculateInterval) {
        this.calculateInterval = calculateInterval;
    }

    public DataCounter getDataCounter() {
        return dataCounter;
    }

    public void setDataCounter(DataCounter dataCounter) {
        this.dataCounter = dataCounter;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Optional<InvoiceRemaining> getPreviousInvoiceRemaining() {
        return previousInvoiceRemaining;
    }

    public void setPreviousInvoiceRemaining(Optional<InvoiceRemaining> previousInvoiceRemaining) {
        this.previousInvoiceRemaining = previousInvoiceRemaining;
    }
}
