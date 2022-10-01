package com.gateweb.charge.contract.remainingCount.bean;

import com.gateweb.charge.contract.remainingCount.component.RemainingContractRenewDataCollector;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;

public class RemainingContractDispatchData {
    Company company;
    Contract contract;
    InvoiceRemaining targetInvoiceRemaining;
    RemainingContractRenewDataCollector remainingContractRenewDataCollector;

    public RemainingContractDispatchData(Company company, Contract contract, InvoiceRemaining targetInvoiceRemaining, RemainingContractRenewDataCollector remainingContractRenewDataCollector) {
        this.company = company;
        this.contract = contract;
        this.targetInvoiceRemaining = targetInvoiceRemaining;
        this.remainingContractRenewDataCollector = remainingContractRenewDataCollector;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public InvoiceRemaining getTargetInvoiceRemaining() {
        return targetInvoiceRemaining;
    }

    public void setTargetInvoiceRemaining(InvoiceRemaining targetInvoiceRemaining) {
        this.targetInvoiceRemaining = targetInvoiceRemaining;
    }

    public RemainingContractRenewDataCollector getRemainingContractRenewDataCollector() {
        return remainingContractRenewDataCollector;
    }

    public void setRemainingContractRenewDataCollector(RemainingContractRenewDataCollector remainingContractRenewDataCollector) {
        this.remainingContractRenewDataCollector = remainingContractRenewDataCollector;
    }
}
