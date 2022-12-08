package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean;

import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component.RemainingContractRenewDataCollectorDispatcher;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrame;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;

public class RemainingContractRenewReq {
    Company company;
    RemainingContractRenewDataCollectorDispatcher remainingContractRenewDataCollector;
    Contract contract;
    RemainingRecordFrame remainingRecordFrame;

    public RemainingContractRenewReq(Company company, RemainingContractRenewDataCollectorDispatcher remainingContractRenewDataCollector, Contract contract, RemainingRecordFrame remainingRecordFrame) {
        this.company = company;
        this.remainingContractRenewDataCollector = remainingContractRenewDataCollector;
        this.contract = contract;
        this.remainingRecordFrame = remainingRecordFrame;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public RemainingContractRenewDataCollectorDispatcher getRemainingContractRenewDataCollector() {
        return remainingContractRenewDataCollector;
    }

    public void setRemainingContractRenewDataCollector(RemainingContractRenewDataCollectorDispatcher remainingContractRenewDataCollector) {
        this.remainingContractRenewDataCollector = remainingContractRenewDataCollector;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public RemainingRecordFrame getRemainingRecordFrame() {
        return remainingRecordFrame;
    }

    public void setRemainingRecordFrame(RemainingRecordFrame remainingRecordFrame) {
        this.remainingRecordFrame = remainingRecordFrame;
    }
}
