package com.gateweb.charge.contract.bean;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.orm.charge.entity.InvoiceRemaining;

import java.util.ArrayList;
import java.util.List;

public class RemainingCountRecordUpdateData {
    Integer diff;
    InvoiceRemaining targetInvoiceRemaining;
    CustomInterval searchInterval;
    List<InvoiceRemaining> relatedList = new ArrayList<>();

    public RemainingCountRecordUpdateData(Integer diff, InvoiceRemaining targetInvoiceRemaining, CustomInterval searchInterval, List<InvoiceRemaining> relatedList) {
        this.diff = diff;
        this.targetInvoiceRemaining = targetInvoiceRemaining;
        this.searchInterval = searchInterval;
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

    public CustomInterval getSearchInterval() {
        return searchInterval;
    }

    public void setSearchInterval(CustomInterval searchInterval) {
        this.searchInterval = searchInterval;
    }

    public List<InvoiceRemaining> getRelatedList() {
        return relatedList;
    }

    public void setRelatedList(List<InvoiceRemaining> relatedList) {
        this.relatedList = relatedList;
    }
}
