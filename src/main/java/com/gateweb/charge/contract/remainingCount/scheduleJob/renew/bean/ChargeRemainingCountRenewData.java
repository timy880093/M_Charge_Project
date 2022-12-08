package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean;

import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrame;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;

import java.util.ArrayList;
import java.util.List;

public class ChargeRemainingCountRenewData {
    Contract originalContract;
    Contract renewContract;
    RemainingRecordFrame remainingRecordFrame;
    List<InvoiceRemaining> updateRecordList = new ArrayList<>();

    public ChargeRemainingCountRenewData() {
    }

    public ChargeRemainingCountRenewData(Contract originalContract, Contract renewContract, RemainingRecordFrame remainingRecordFrame, List<InvoiceRemaining> updateRecordList) {
        this.originalContract = originalContract;
        this.renewContract = renewContract;
        this.remainingRecordFrame = remainingRecordFrame;
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

    public RemainingRecordFrame getRemainingRecordModel() {
        return remainingRecordFrame;
    }

    public void setRemainingRecordModel(RemainingRecordFrame remainingRecordFrame) {
        this.remainingRecordFrame = remainingRecordFrame;
    }

    public List<InvoiceRemaining> getUpdateRecordList() {
        return updateRecordList;
    }

    public void setUpdateRecordList(List<InvoiceRemaining> updateRecordList) {
        this.updateRecordList = updateRecordList;
    }
}
