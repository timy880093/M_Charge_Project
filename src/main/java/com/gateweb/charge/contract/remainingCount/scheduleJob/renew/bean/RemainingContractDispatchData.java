package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean;

import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component.RemainingContractRenewDataCollectorDispatcher;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;

public class RemainingContractDispatchData {
    Company company;
    Contract contract;
    InvoiceRemaining targetInvoiceRemaining;
    RemainingContractRenewDataCollectorDispatcher remainingContractRenewDataCollector;

    public RemainingContractDispatchData(Company company, Contract contract, InvoiceRemaining targetInvoiceRemaining, RemainingContractRenewDataCollectorDispatcher remainingContractRenewDataCollector) {
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

    public RemainingContractRenewDataCollectorDispatcher getRemainingContractRenewDataCollector() {
        return remainingContractRenewDataCollector;
    }

    public void setRemainingContractRenewDataCollector(RemainingContractRenewDataCollectorDispatcher remainingContractRenewDataCollector) {
        this.remainingContractRenewDataCollector = remainingContractRenewDataCollector;
    }
}
