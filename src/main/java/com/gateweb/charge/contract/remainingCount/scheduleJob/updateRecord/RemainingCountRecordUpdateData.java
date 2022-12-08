package com.gateweb.charge.contract.remainingCount.scheduleJob.updateRecord;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.orm.charge.entity.InvoiceRemaining;

import java.util.ArrayList;
import java.util.List;

public class RemainingCountRecordUpdateData {
    Integer diff;
    InvoiceRemaining targetInvoiceRemaining;
    InvoiceRemaining previousInvoiceRemaining;
    List<InvoiceRemaining> relatedList = new ArrayList<>();

    public RemainingCountRecordUpdateData(Integer diff, InvoiceRemaining targetInvoiceRemaining, InvoiceRemaining previousInvoiceRemaining, List<InvoiceRemaining> relatedList) {
        this.diff = diff;
        this.targetInvoiceRemaining = targetInvoiceRemaining;
        this.previousInvoiceRemaining = previousInvoiceRemaining;
        this.relatedList = relatedList;
    }

    public Integer getDiff() {
        return diff;
    }

    public void setDiff(Integer diff) {
        this.diff = diff;
    }

    public InvoiceRemaining getTargetInvoiceRemaining() {
        return targetInvoiceRemaining;
    }

    public void setTargetInvoiceRemaining(InvoiceRemaining targetInvoiceRemaining) {
        this.targetInvoiceRemaining = targetInvoiceRemaining;
    }

    public InvoiceRemaining getPreviousInvoiceRemaining() {
        return previousInvoiceRemaining;
    }

    public void setPreviousInvoiceRemaining(InvoiceRemaining previousInvoiceRemaining) {
        this.previousInvoiceRemaining = previousInvoiceRemaining;
    }

    public List<InvoiceRemaining> getRelatedList() {
        return relatedList;
    }

    public void setRelatedList(List<InvoiceRemaining> relatedList) {
        this.relatedList = relatedList;
    }
}
