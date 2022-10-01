package com.gateweb.charge.contract.remainingCount.bean;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.orm.charge.entity.InvoiceRemaining;

public class RemainingRecordModel {
    InvoiceRemaining prevRecord;
    InvoiceRemaining targetRecord;
    CustomInterval invoiceDateInterval;

    public RemainingRecordModel(InvoiceRemaining prevRecord, InvoiceRemaining targetRecord, CustomInterval invoiceDateInterval) {
        this.prevRecord = prevRecord;
        this.targetRecord = targetRecord;
        this.invoiceDateInterval = invoiceDateInterval;
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

    public CustomInterval getInvoiceDateInterval() {
        return invoiceDateInterval;
    }

    public void setInvoiceDateInterval(CustomInterval invoiceDateInterval) {
        this.invoiceDateInterval = invoiceDateInterval;
    }
}
