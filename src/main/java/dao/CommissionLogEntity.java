package dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by emily on 2015/12/17.
 */
public class CommissionLogEntity implements Serializable {

    public Integer commissionLogId;
    public Integer commissionCpId;
    public String commissionYmd;
    public Integer userCpId;
    public Date inDateStart;
    public Date inDateEnd;
    public String commissionType;
    public BigDecimal mainAmount;
    public BigDecimal mainPercent;
    public BigDecimal collectMoney;
    public BigDecimal addtionPercent;
    public BigDecimal inAmount;
    public BigDecimal commissionAmount;
    public String isPaid;
    public String note;
    public Timestamp createDate;
    public Timestamp modifyDate;
    public Integer creatorId;
    public Integer modifierId;

    public Integer getCommissionLogId() {
        return commissionLogId;
    }

    public void setCommissionLogId(Integer commissionLogId) {
        this.commissionLogId = commissionLogId;
    }

    public Integer getCommissionCpId() {
        return commissionCpId;
    }

    public void setCommissionCpId(Integer commissionCpId) {
        this.commissionCpId = commissionCpId;
    }

    public String getCommissionYmd() {
        return commissionYmd;
    }

    public void setCommissionYmd(String commissionYmd) {
        this.commissionYmd = commissionYmd;
    }

    public Integer getUserCpId() {
        return userCpId;
    }

    public void setUserCpId(Integer userCpId) {
        this.userCpId = userCpId;
    }

    public Date getInDateStart() {
        return inDateStart;
    }

    public void setInDateStart(Date inDateStart) {
        this.inDateStart = inDateStart;
    }

    public Date getInDateEnd() {
        return inDateEnd;
    }

    public void setInDateEnd(Date inDateEnd) {
        this.inDateEnd = inDateEnd;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public BigDecimal getMainAmount() {
        return mainAmount;
    }

    public void setMainAmount(BigDecimal mainAmount) {
        this.mainAmount = mainAmount;
    }

    public BigDecimal getMainPercent() {
        return mainPercent;
    }

    public void setMainPercent(BigDecimal mainPercent) {
        this.mainPercent = mainPercent;
    }

    public BigDecimal getCollectMoney() {
        return collectMoney;
    }

    public void setCollectMoney(BigDecimal collectMoney) {
        this.collectMoney = collectMoney;
    }

    public BigDecimal getAddtionPercent() {
        return addtionPercent;
    }

    public void setAddtionPercent(BigDecimal addtionPercent) {
        this.addtionPercent = addtionPercent;
    }

    public BigDecimal getInAmount() {
        return inAmount;
    }

    public void setInAmount(BigDecimal inAmount) {
        this.inAmount = inAmount;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getModifierId() {
        return modifierId;
    }

    public void setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommissionLogEntity that = (CommissionLogEntity) o;

        if (commissionLogId != null ? !commissionLogId.equals(that.commissionLogId) : that.commissionLogId != null)
            return false;
        if (commissionCpId != null ? !commissionCpId.equals(that.commissionCpId) : that.commissionCpId != null)
            return false;
        if (commissionYmd != null ? !commissionYmd.equals(that.commissionYmd) : that.commissionYmd != null)
            return false;
        if (userCpId != null ? !userCpId.equals(that.userCpId) : that.userCpId != null) return false;
        if (inDateStart != null ? !inDateStart.equals(that.inDateStart) : that.inDateStart != null) return false;
        if (inDateEnd != null ? !inDateEnd.equals(that.inDateEnd) : that.inDateEnd != null) return false;
        if (commissionType != null ? !commissionType.equals(that.commissionType) : that.commissionType != null)
            return false;
        if (mainAmount != null ? !mainAmount.equals(that.mainAmount) : that.mainAmount != null) return false;
        if (mainPercent != null ? !mainPercent.equals(that.mainPercent) : that.mainPercent != null) return false;
        if (collectMoney != null ? !collectMoney.equals(that.collectMoney) : that.collectMoney != null) return false;
        if (addtionPercent != null ? !addtionPercent.equals(that.addtionPercent) : that.addtionPercent != null)
            return false;
        if (inAmount != null ? !inAmount.equals(that.inAmount) : that.inAmount != null)
            return false;
        if (commissionAmount != null ? !commissionAmount.equals(that.commissionAmount) : that.commissionAmount != null)
            return false;
        if (isPaid != null ? !isPaid.equals(that.isPaid) : that.isPaid != null) return false;
        if (note != null ? !note.equals(that.note) : that.note != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = commissionLogId != null ? commissionLogId.hashCode() : 0;
        result = 31 * result + (commissionCpId != null ? commissionCpId.hashCode() : 0);
        result = 31 * result + (commissionYmd != null ? commissionYmd.hashCode() : 0);
        result = 31 * result + (userCpId != null ? userCpId.hashCode() : 0);
        result = 31 * result + (inDateStart != null ? inDateStart.hashCode() : 0);
        result = 31 * result + (inDateEnd != null ? inDateEnd.hashCode() : 0);
        result = 31 * result + (commissionType != null ? commissionType.hashCode() : 0);
        result = 31 * result + (mainAmount != null ? mainAmount.hashCode() : 0);
        result = 31 * result + (mainPercent != null ? mainPercent.hashCode() : 0);
        result = 31 * result + (collectMoney != null ? collectMoney.hashCode() : 0);
        result = 31 * result + (addtionPercent != null ? addtionPercent.hashCode() : 0);
        result = 31 * result + (inAmount != null ? inAmount.hashCode() : 0);
        result = 31 * result + (commissionAmount != null ? commissionAmount.hashCode() : 0);
        result = 31 * result + (isPaid != null ? isPaid.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        return result;
    }
}
