package com.gateweb.charge.contract.remainingCount.bean;

import com.gateweb.charge.contract.remainingCount.component.RemainingContractRenewDataCollector;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;

public class RemainingContractRenewReq {
    Company company;
    RemainingContractRenewDataCollector remainingContractRenewDataCollector;
    Contract contract;
    RemainingRecordModel remainingRecordModel;

    public RemainingContractRenewReq(Company company, RemainingContractRenewDataCollector remainingContractRenewDataCollector, Contract contract, RemainingRecordModel remainingRecordModel) {
        this.company = company;
        this.remainingContractRenewDataCollector = remainingContractRenewDataCollector;
        this.contract = contract;
        this.remainingRecordModel = remainingRecordModel;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public RemainingContractRenewDataCollector getRemainingContractRenewDataCollector() {
        return remainingContractRenewDataCollector;
    }

    public void setRemainingContractRenewDataCollector(RemainingContractRenewDataCollector remainingContractRenewDataCollector) {
        this.remainingContractRenewDataCollector = remainingContractRenewDataCollector;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public RemainingRecordModel getRemainingRecordModel() {
        return remainingRecordModel;
    }

    public void setRemainingRecordModel(RemainingRecordModel remainingRecordModel) {
        this.remainingRecordModel = remainingRecordModel;
    }
}