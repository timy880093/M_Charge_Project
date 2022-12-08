package com.gateweb.charge.contract.remainingCount.scheduleJob.updateRecord;

import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrame;
import com.gateweb.orm.charge.entity.InvoiceRemaining;

public class RemainingRecordUpdateReq {
    String businessNo;
    RemainingRecordFrame remainingRecordFrame;
    int newUsage;
    int newRemaining;

    public RemainingRecordUpdateReq(String businessNo, RemainingRecordFrame remainingRecordFrame, int newUsage, int newRemaining) {
        this.businessNo = businessNo;
        this.remainingRecordFrame = remainingRecordFrame;
        this.newUsage = newUsage;
        this.newRemaining = newRemaining;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public RemainingRecordFrame getRemainingRecordFrame() {
        return remainingRecordFrame;
    }

    public void setRemainingRecordFrame(RemainingRecordFrame remainingRecordFrame) {
        this.remainingRecordFrame = remainingRecordFrame;
    }

    public int getNewUsage() {
        return newUsage;
    }

    public void setNewUsage(int newUsage) {
        this.newUsage = newUsage;
    }

    public int getNewRemaining() {
        return newRemaining;
    }

    public void setNewRemaining(int newRemaining) {
        this.newRemaining = newRemaining;
    }
}
