package com.gateweb.reportModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Eason on 3/19/2018.
 */
public class CommissionRecord {
    String dealerCompanyName;
    Date inDateStart;
    Date inDateEnd;
    String commissionType;
    String commissionPercentage;
    String isPaid;
    String customName;
    String isFirst;
    String businessNo;
    String cashType;
    String packageName;
    String calculateYearMonth;
    String outYearMonth;
    String inDate;
    BigDecimal taxInclusivePrice;
    String isInoutMoneyUnmatch;
    BigDecimal commissionAmount;

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getCalculateYearMonth() {
        return calculateYearMonth;
    }

    public void setCalculateYearMonth(String calculateYearMonth) {
        this.calculateYearMonth = calculateYearMonth;
    }

    public String getCashType() {
        return cashType;
    }

    public void setCashType(String cashType) {
        this.cashType = cashType;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getCommissionPercentage() {
        return commissionPercentage;
    }

    public void setCommissionPercentage(String commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getDealerCompanyName() {
        return dealerCompanyName;
    }

    public void setDealerCompanyName(String dealerCompanyName) {
        this.dealerCompanyName = dealerCompanyName;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public Date getInDateEnd() {
        return inDateEnd;
    }

    public void setInDateEnd(Date inDateEnd) {
        this.inDateEnd = inDateEnd;
    }

    public Date getInDateStart() {
        return inDateStart;
    }

    public void setInDateStart(Date inDateStart) {
        this.inDateStart = inDateStart;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }

    public String getIsInoutMoneyUnmatch() {
        return isInoutMoneyUnmatch;
    }

    public void setIsInoutMoneyUnmatch(String isInoutMoneyUnmatch) {
        this.isInoutMoneyUnmatch = isInoutMoneyUnmatch;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getOutYearMonth() {
        return outYearMonth;
    }

    public void setOutYearMonth(String outYearMonth) {
        this.outYearMonth = outYearMonth;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public BigDecimal getTaxInclusivePrice() {
        return taxInclusivePrice;
    }

    public void setTaxInclusivePrice(BigDecimal taxInclusivePrice) {
        this.taxInclusivePrice = taxInclusivePrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommissionRecord that = (CommissionRecord) o;

        if (dealerCompanyName != null ? !dealerCompanyName.equals(that.dealerCompanyName) : that.dealerCompanyName != null)
            return false;
        if (inDateStart != null ? !inDateStart.equals(that.inDateStart) : that.inDateStart != null) return false;
        if (inDateEnd != null ? !inDateEnd.equals(that.inDateEnd) : that.inDateEnd != null) return false;
        if (commissionType != null ? !commissionType.equals(that.commissionType) : that.commissionType != null)
            return false;
        if (commissionPercentage != null ? !commissionPercentage.equals(that.commissionPercentage) : that.commissionPercentage != null)
            return false;
        if (isPaid != null ? !isPaid.equals(that.isPaid) : that.isPaid != null) return false;
        if (customName != null ? !customName.equals(that.customName) : that.customName != null) return false;
        if (isFirst != null ? !isFirst.equals(that.isFirst) : that.isFirst != null) return false;
        if (businessNo != null ? !businessNo.equals(that.businessNo) : that.businessNo != null) return false;
        if (cashType != null ? !cashType.equals(that.cashType) : that.cashType != null) return false;
        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) return false;
        if (calculateYearMonth != null ? !calculateYearMonth.equals(that.calculateYearMonth) : that.calculateYearMonth != null)
            return false;
        if (outYearMonth != null ? !outYearMonth.equals(that.outYearMonth) : that.outYearMonth != null) return false;
        if (inDate != null ? !inDate.equals(that.inDate) : that.inDate != null) return false;
        if (taxInclusivePrice != null ? !taxInclusivePrice.equals(that.taxInclusivePrice) : that.taxInclusivePrice != null)
            return false;
        if (isInoutMoneyUnmatch != null ? !isInoutMoneyUnmatch.equals(that.isInoutMoneyUnmatch) : that.isInoutMoneyUnmatch != null)
            return false;
        return commissionAmount != null ? commissionAmount.equals(that.commissionAmount) : that.commissionAmount == null;

    }

    @Override
    public int hashCode() {
        int result = dealerCompanyName != null ? dealerCompanyName.hashCode() : 0;
        result = 31 * result + (inDateStart != null ? inDateStart.hashCode() : 0);
        result = 31 * result + (inDateEnd != null ? inDateEnd.hashCode() : 0);
        result = 31 * result + (commissionType != null ? commissionType.hashCode() : 0);
        result = 31 * result + (commissionPercentage != null ? commissionPercentage.hashCode() : 0);
        result = 31 * result + (isPaid != null ? isPaid.hashCode() : 0);
        result = 31 * result + (customName != null ? customName.hashCode() : 0);
        result = 31 * result + (isFirst != null ? isFirst.hashCode() : 0);
        result = 31 * result + (businessNo != null ? businessNo.hashCode() : 0);
        result = 31 * result + (cashType != null ? cashType.hashCode() : 0);
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (calculateYearMonth != null ? calculateYearMonth.hashCode() : 0);
        result = 31 * result + (outYearMonth != null ? outYearMonth.hashCode() : 0);
        result = 31 * result + (inDate != null ? inDate.hashCode() : 0);
        result = 31 * result + (taxInclusivePrice != null ? taxInclusivePrice.hashCode() : 0);
        result = 31 * result + (isInoutMoneyUnmatch != null ? isInoutMoneyUnmatch.hashCode() : 0);
        result = 31 * result + (commissionAmount != null ? commissionAmount.hashCode() : 0);
        return result;
    }
}
