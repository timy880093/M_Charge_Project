package com.gateweb.reportModel;

import java.math.BigDecimal;

/**
 * Created by Eason on 3/20/2018.
 */
public class OutRecord {
    String customNumber;
    BigDecimal inAmount;
    String companyName;
    String businessNo;

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomNumber() {
        return customNumber;
    }

    public void setCustomNumber(String customNumber) {
        this.customNumber = customNumber;
    }

    public BigDecimal getInAmount() {
        return inAmount;
    }

    public void setInAmount(BigDecimal inAmount) {
        this.inAmount = inAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutRecord outRecord = (OutRecord) o;

        if (customNumber != null ? !customNumber.equals(outRecord.customNumber) : outRecord.customNumber != null)
            return false;
        if (inAmount != null ? !inAmount.equals(outRecord.inAmount) : outRecord.inAmount != null) return false;
        if (companyName != null ? !companyName.equals(outRecord.companyName) : outRecord.companyName != null)
            return false;
        return businessNo != null ? businessNo.equals(outRecord.businessNo) : outRecord.businessNo == null;

    }

    @Override
    public int hashCode() {
        int result = customNumber != null ? customNumber.hashCode() : 0;
        result = 31 * result + (inAmount != null ? inAmount.hashCode() : 0);
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (businessNo != null ? businessNo.hashCode() : 0);
        return result;
    }
}
