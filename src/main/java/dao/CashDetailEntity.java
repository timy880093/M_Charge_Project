package dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by emily on 2015/12/17.
 */
public class CashDetailEntity implements Serializable{

    public Integer cashDetailId;
    public Integer cashMasterId;
    public String calYm;
    public Integer companyId;
    public Integer cashType;
    public Integer billType;
    public BigDecimal orgPrice;
    public BigDecimal diffPrice;
    public String diffPriceNote;
    public BigDecimal noTaxInclusivePrice;
    public String taxType;
    public Float taxRate;
    public BigDecimal taxPrice;
    public BigDecimal taxInclusivePrice;
    public Integer creatorId;
    public Timestamp createDate;
    public Integer modifierId;
    public Timestamp modifyDate;
    public Integer billId;
    public Integer packageId;
    public Integer status;
    public String outYm;
    public Integer commissionLogId;
    public BigDecimal commissionAmount;
    public String isFirst;

    public Integer getCashDetailId() {
        return cashDetailId;
    }

    public void setCashDetailId(Integer cashDetailId) {
        this.cashDetailId = cashDetailId;
    }

    public Integer getCashMasterId() {
        return cashMasterId;
    }

    public void setCashMasterId(Integer cashMasterId) {
        this.cashMasterId = cashMasterId;
    }

    public String getCalYm() {
        return calYm;
    }

    public void setCalYm(String calYm) {
        this.calYm = calYm;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCashType() {
        return cashType;
    }

    public void setCashType(Integer cashType) {
        this.cashType = cashType;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public BigDecimal getOrgPrice() {
        return orgPrice;
    }

    public void setOrgPrice(BigDecimal orgPrice) {
        this.orgPrice = orgPrice;
    }

    public BigDecimal getDiffPrice() {
        return diffPrice;
    }

    public void setDiffPrice(BigDecimal diffPrice) {
        this.diffPrice = diffPrice;
    }

    public String getDiffPriceNote() {
        return diffPriceNote;
    }

    public void setDiffPriceNote(String diffPriceNote) {
        this.diffPriceNote = diffPriceNote;
    }

    public BigDecimal getNoTaxInclusivePrice() {
        return noTaxInclusivePrice;
    }

    public void setNoTaxInclusivePrice(BigDecimal noTaxInclusivePrice) {
        this.noTaxInclusivePrice = noTaxInclusivePrice;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public Float getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Float taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(BigDecimal taxPrice) {
        this.taxPrice = taxPrice;
    }

    public BigDecimal getTaxInclusivePrice() {
        return taxInclusivePrice;
    }

    public void setTaxInclusivePrice(BigDecimal taxInclusivePrice) {
        this.taxInclusivePrice = taxInclusivePrice;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Integer getModifierId() {
        return modifierId;
    }

    public void setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOutYm() {
        return outYm;
    }

    public void setOutYm(String outYm) {
        this.outYm = outYm;
    }

    public Integer getCommissionLogId() {
        return commissionLogId;
    }

    public void setCommissionLogId(Integer commissionLogId) {
        this.commissionLogId = commissionLogId;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CashDetailEntity that = (CashDetailEntity) o;

        if (cashDetailId != null ? !cashDetailId.equals(that.cashDetailId) : that.cashDetailId != null) return false;
        if (cashMasterId != null ? !cashMasterId.equals(that.cashMasterId) : that.cashMasterId != null) return false;
        if (calYm != null ? !calYm.equals(that.calYm) : that.calYm != null) return false;
        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (cashType != null ? !cashType.equals(that.cashType) : that.cashType != null) return false;
        if (billType != null ? !billType.equals(that.billType) : that.billType != null) return false;
        if (orgPrice != null ? !orgPrice.equals(that.orgPrice) : that.orgPrice != null) return false;
        if (diffPrice != null ? !diffPrice.equals(that.diffPrice) : that.diffPrice != null) return false;
        if (diffPriceNote != null ? !diffPriceNote.equals(that.diffPriceNote) : that.diffPriceNote != null)
            return false;
        if (noTaxInclusivePrice != null ? !noTaxInclusivePrice.equals(that.noTaxInclusivePrice) : that.noTaxInclusivePrice != null)
            return false;
        if (taxType != null ? !taxType.equals(that.taxType) : that.taxType != null) return false;
        if (taxRate != null ? !taxRate.equals(that.taxRate) : that.taxRate != null) return false;
        if (taxPrice != null ? !taxPrice.equals(that.taxPrice) : that.taxPrice != null) return false;
        if (taxInclusivePrice != null ? !taxInclusivePrice.equals(that.taxInclusivePrice) : that.taxInclusivePrice != null)
            return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (billId != null ? !billId.equals(that.billId) : that.billId != null) return false;
        if (packageId != null ? !packageId.equals(that.packageId) : that.packageId != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (outYm != null ? !outYm.equals(that.outYm) : that.outYm != null) return false;
        if (commissionLogId != null ? !commissionLogId.equals(that.commissionLogId) : that.commissionLogId != null)
            return false;
        if (commissionAmount != null ? !commissionAmount.equals(that.commissionAmount) : that.commissionAmount != null)
            return false;
        if (isFirst != null ? !isFirst.equals(that.isFirst) : that.isFirst != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cashDetailId != null ? cashDetailId.hashCode() : 0;
        result = 31 * result + (cashMasterId != null ? cashMasterId.hashCode() : 0);
        result = 31 * result + (calYm != null ? calYm.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (cashType != null ? cashType.hashCode() : 0);
        result = 31 * result + (billType != null ? billType.hashCode() : 0);
        result = 31 * result + (orgPrice != null ? orgPrice.hashCode() : 0);
        result = 31 * result + (diffPrice != null ? diffPrice.hashCode() : 0);
        result = 31 * result + (diffPriceNote != null ? diffPriceNote.hashCode() : 0);
        result = 31 * result + (noTaxInclusivePrice != null ? noTaxInclusivePrice.hashCode() : 0);
        result = 31 * result + (taxType != null ? taxType.hashCode() : 0);
        result = 31 * result + (taxRate != null ? taxRate.hashCode() : 0);
        result = 31 * result + (taxPrice != null ? taxPrice.hashCode() : 0);
        result = 31 * result + (taxInclusivePrice != null ? taxInclusivePrice.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (billId != null ? billId.hashCode() : 0);
        result = 31 * result + (packageId != null ? packageId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (outYm != null ? outYm.hashCode() : 0);
        result = 31 * result + (commissionLogId != null ? commissionLogId.hashCode() : 0);
        result = 31 * result + (commissionAmount != null ? commissionAmount.hashCode() : 0);
        result = 31 * result + (isFirst != null ? isFirst.hashCode() : 0);
        return result;
    }
}
