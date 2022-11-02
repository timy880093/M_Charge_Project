package com.gateweb.charge.contract.remainingCount.remainingRecordFrame;

import com.gateweb.orm.charge.entity.InvoiceRemaining;

public class RemainingRecordFrame {
    InvoiceRemaining prevRecord;
    InvoiceRemaining targetRecord;

    public RemainingRecordFrame(InvoiceRemaining prevRecord, InvoiceRemaining targetRecord) {
        this.prevRecord = prevRecord;
        this.targetRecord = targetRecord;
    }

    public InvoiceRemaining getPrevRecord() {
        return prevRecord;
    }

    public void setPrevRecord(InvoiceRemaining prevRecord) {
        this.prevRecord = prevRecord;
    }

    public InvoiceRemaining getTargetRecord() {
        return targetRecord;
    }

    public void setTargetRecord(InvoiceRemaining targetRecord) {
        this.targetRecord = targetRecord;
    }
}
