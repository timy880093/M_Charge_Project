package com.gateweb.charge.contract.bean;

import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;

import java.util.ArrayList;
import java.util.List;

public class ChargeRemainingCountRenewData {
    Contract originalContract;
    Contract renewContract;
    RemainingRecordModel remainingRecordModel;
    List<InvoiceRemaining> updateRecordList = new ArrayList<>();

    public ChargeRemainingCountRenewData() {
    }

    public ChargeRemainingCountRenewData(Contract originalContract, Contract renewContract, RemainingRecordModel remainingRecordModel, List<InvoiceRemaining> updateRecordList) {
        this.originalContract = originalContract;
        this.renewContract = renewContract;
        this.remainingRecordModel = remainingRecordModel;
        this.updateRecordList = updateRecordList;
    }

    public Contract getOriginalContract() {
        return originalContract;
    }

    public void setOriginalContract(Contract originalContract) {
        this.originalContract = originalContract;
    }

    public Contract getRenewContract() {
        return renewContract;
    }

    public void setRenewContract(Contract renewContract) {
        this.renewContract = renewContract;
    }

    public RemainingRecordModel getRemainingRecordModel() {
        return remainingRecordModel;
    }

    public void setRemainingRecordModel(RemainingRecordModel remainingRecordModel) {
        this.remainingRecordModel = remainingRecordModel;
    }

    public List<InvoiceRemaining> getUpdateRecordList() {
        return updateRecordList;
    }

    public void setUpdateRecordList(List<InvoiceRemaining> updateRecordList) {
        this.updateRecordList = updateRecordList;
    }
}
