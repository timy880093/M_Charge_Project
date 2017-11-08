package dao;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by emily on 2017/6/20.
 */
public class WarrantyEntity implements Serializable{
    public Integer warrantyId;
    public String warrantyNo;
    public Integer companyId;
    public Date startDate;
    public Date endDate;
    public Boolean extend;
    public String model;
    public String note;
    public Integer status;
    public Integer creatorId;
    public Timestamp createDate;
    public Integer modifierId;
    public Timestamp modifyDate;
    public Integer dealerCompanyId;
    public Integer onlyShip;

    public Integer getWarrantyId() {
        return warrantyId;
    }

    public void setWarrantyId(Integer warrantyId) {
        this.warrantyId = warrantyId;
    }

    public String getWarrantyNo() {
        return warrantyNo;
    }

    public void setWarrantyNo(String warrantyNo) {
        this.warrantyNo = warrantyNo;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getExtend() {
        return extend;
    }

    public void setExtend(Boolean extend) {
        this.extend = extend;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getDealerCompanyId() {
        return dealerCompanyId;
    }

    public void setDealerCompanyId(Integer dealerCompanyId) {
        this.dealerCompanyId = dealerCompanyId;
    }

    public Integer getOnlyShip() {
        return onlyShip;
    }

    public void setOnlyShip(Integer onlyShip) {
        this.onlyShip = onlyShip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WarrantyEntity that = (WarrantyEntity) o;

        if (warrantyId != null ? !warrantyId.equals(that.warrantyId) : that.warrantyId != null) return false;
        if (warrantyNo != null ? !warrantyNo.equals(that.warrantyNo) : that.warrantyNo != null) return false;
        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (extend != null ? !extend.equals(that.extend) : that.extend != null) return false;
        if (model != null ? !model.equals(that.model) : that.model != null) return false;
        if (note != null ? !note.equals(that.note) : that.note != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (dealerCompanyId != null ? !dealerCompanyId.equals(that.dealerCompanyId) : that.dealerCompanyId != null)
            return false;
        if (onlyShip != null ? !onlyShip.equals(that.onlyShip) : that.onlyShip != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = warrantyId != null ? warrantyId.hashCode() : 0;
        result = 31 * result + (warrantyNo != null ? warrantyNo.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (extend != null ? extend.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (dealerCompanyId != null ? dealerCompanyId.hashCode() : 0);
        result = 31 * result + (onlyShip != null ? onlyShip.hashCode() : 0);
        return result;
    }
}
