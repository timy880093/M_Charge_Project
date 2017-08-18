package dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by emily on 2015/12/25.
 */
public class CashMasterEntity  implements Serializable {

    private Integer cashMasterId;
    private String outYm;
    private String bankYm;
    private Integer companyId;
    private BigDecimal noTaxInclusiveAmount;
    private String taxType;
    private Float taxRate;
    private BigDecimal taxAmount;
    private BigDecimal taxInclusiveAmount;
    private Timestamp emailSentDate;
    private Timestamp outDate;
    private Timestamp excelOutDate;
    private BigDecimal inAmount;
    private String inNote;
    private Timestamp inDate;
    private Integer status;
    private String isFirst;
    private String isInoutMoneyUnmatch;
    private Integer creatorId;
    private Timestamp createDate;
    private Integer modifierId;
    private Timestamp modifyDate;

    public Integer getCashMasterId() {
        return cashMasterId;
    }

    public void setCashMasterId(Integer cashMasterId) {
        this.cashMasterId = cashMasterId;
    }

    public String getOutYm() {
        return outYm;
    }

    public void setOutYm(String outYm) {
        this.outYm = outYm;
    }

    public String getBankYm() {
        return bankYm;
    }

    public void setBankYm(String bankYm) {
        this.bankYm = bankYm;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public BigDecimal getNoTaxInclusiveAmount() {
        return noTaxInclusiveAmount;
    }

    public void setNoTaxInclusiveAmount(BigDecimal noTaxInclusiveAmount) {
        this.noTaxInclusiveAmount = noTaxInclusiveAmount;
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

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTaxInclusiveAmount() {
        return taxInclusiveAmount;
    }

    public void setTaxInclusiveAmount(BigDecimal taxInclusiveAmount) {
        this.taxInclusiveAmount = taxInclusiveAmount;
    }

    public Timestamp getEmailSentDate() {
        return emailSentDate;
    }

    public void setEmailSentDate(Timestamp emailSentDate) {
        this.emailSentDate = emailSentDate;
    }

    public Timestamp getOutDate() {
        return outDate;
    }

    public void setOutDate(Timestamp outDate) {
        this.outDate = outDate;
    }

    public Timestamp getExcelOutDate() {
        return excelOutDate;
    }

    public void setExcelOutDate(Timestamp excelOutDate) {
        this.excelOutDate = excelOutDate;
    }

    public BigDecimal getInAmount() {
        return inAmount;
    }

    public void setInAmount(BigDecimal inAmount) {
        this.inAmount = inAmount;
    }

    public String getInNote() {
        return inNote;
    }

    public void setInNote(String inNote) {
        this.inNote = inNote;
    }

    public Timestamp getInDate() {
        return inDate;
    }

    public void setInDate(Timestamp inDate) {
        this.inDate = inDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
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

    public String getIsInoutMoneyUnmatch() {
        return isInoutMoneyUnmatch;
    }

    public void setIsInoutMoneyUnmatch(String isInoutMoneyUnmatch) {
        this.isInoutMoneyUnmatch = isInoutMoneyUnmatch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CashMasterEntity that = (CashMasterEntity) o;

        if (cashMasterId != null ? !cashMasterId.equals(that.cashMasterId) : that.cashMasterId != null) return false;
        if (outYm != null ? !outYm.equals(that.outYm) : that.outYm != null) return false;
        if (bankYm != null ? !bankYm.equals(that.bankYm) : that.bankYm != null) return false;
        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (noTaxInclusiveAmount != null ? !noTaxInclusiveAmount.equals(that.noTaxInclusiveAmount) : that.noTaxInclusiveAmount != null)
            return false;
        if (taxType != null ? !taxType.equals(that.taxType) : that.taxType != null) return false;
        if (taxRate != null ? !taxRate.equals(that.taxRate) : that.taxRate != null) return false;
        if (taxAmount != null ? !taxAmount.equals(that.taxAmount) : that.taxAmount != null) return false;
        if (taxInclusiveAmount != null ? !taxInclusiveAmount.equals(that.taxInclusiveAmount) : that.taxInclusiveAmount != null)
            return false;
        if (emailSentDate != null ? !emailSentDate.equals(that.emailSentDate) : that.emailSentDate != null)
            return false;
        if (outDate != null ? !outDate.equals(that.outDate) : that.outDate != null) return false;
        if (excelOutDate != null ? !excelOutDate.equals(that.excelOutDate) : that.excelOutDate != null) return false;
        if (inAmount != null ? !inAmount.equals(that.inAmount) : that.inAmount != null) return false;
        if (inNote != null ? !inNote.equals(that.inNote) : that.inNote != null) return false;
        if (inDate != null ? !inDate.equals(that.inDate) : that.inDate != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (isFirst != null ? !isFirst.equals(that.isFirst) : that.isFirst != null) return false;
        if (isInoutMoneyUnmatch != null ? !isInoutMoneyUnmatch.equals(that.isInoutMoneyUnmatch) : that.isInoutMoneyUnmatch != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cashMasterId != null ? cashMasterId.hashCode() : 0;
        result = 31 * result + (outYm != null ? outYm.hashCode() : 0);
        result = 31 * result + (bankYm != null ? bankYm.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (noTaxInclusiveAmount != null ? noTaxInclusiveAmount.hashCode() : 0);
        result = 31 * result + (taxType != null ? taxType.hashCode() : 0);
        result = 31 * result + (taxRate != null ? taxRate.hashCode() : 0);
        result = 31 * result + (taxAmount != null ? taxAmount.hashCode() : 0);
        result = 31 * result + (taxInclusiveAmount != null ? taxInclusiveAmount.hashCode() : 0);
        result = 31 * result + (emailSentDate != null ? emailSentDate.hashCode() : 0);
        result = 31 * result + (outDate != null ? outDate.hashCode() : 0);
        result = 31 * result + (excelOutDate != null ? excelOutDate.hashCode() : 0);
        result = 31 * result + (inAmount != null ? inAmount.hashCode() : 0);
        result = 31 * result + (inNote != null ? inNote.hashCode() : 0);
        result = 31 * result + (inDate != null ? inDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (isFirst != null ? isFirst.hashCode() : 0);
        result = 31 * result + (isInoutMoneyUnmatch != null ? isInoutMoneyUnmatch.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        return result;
    }
}
