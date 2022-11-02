package com.gateweb.charge.contract.remainingCount.scheduleJob.updateRecord;

import com.gateweb.orm.charge.entity.InvoiceRemaining;

public class RemainingRecordUpdateReq {
    String businessNo;
    InvoiceRemaining targetRecord;
    InvoiceRemaining prevRecord;

    public RemainingRecordUpdateReq() {
    }

    public RemainingRecordUpdateReq(String businessNo, InvoiceRemaining targetRecord, InvoiceRemaining prevRecord) {
        this.businessNo = businessNo;
        this.targetRecord = targetRecord;
        this.prevRecord = prevRecord;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public InvoiceRemaining getTargetRecord() {
        return targetRecord;
    }

    public void setTargetRecord(InvoiceRemaining targetRecord) {
        this.targetRecord = targetRecord;
    }

    public InvoiceRemaining getPrevRecord() {
        return prevRecord;
    }

    public void setPrevRecord(InvoiceRemaining prevRecord) {
        this.prevRecord = prevRecord;
    }
}
