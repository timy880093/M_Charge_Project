package dao;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by emily on 2017/5/19.
 */
public class PrepayDeductMasterEntity implements Serializable{
    private Integer prepayDeductMasterId;
    private Integer companyId;
    private Integer amount;
    private String isEnableOver;
    private Integer creatorId;
    private Timestamp createDate;
    private Integer modifierId;
    private Timestamp modifyDate;

    public Integer getPrepayDeductMasterId() {
        return prepayDeductMasterId;
    }

    public void setPrepayDeductMasterId(Integer prepayDeductMasterId) {
        this.prepayDeductMasterId = prepayDeductMasterId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getIsEnableOver() {
        return isEnableOver;
    }

    public void setIsEnableOver(String isEnableOver) {
        this.isEnableOver = isEnableOver;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrepayDeductMasterEntity that = (PrepayDeductMasterEntity) o;

        if (prepayDeductMasterId != null ? !prepayDeductMasterId.equals(that.prepayDeductMasterId) : that.prepayDeductMasterId != null)
            return false;
        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (isEnableOver != null ? !isEnableOver.equals(that.isEnableOver) : that.isEnableOver != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = prepayDeductMasterId != null ? prepayDeductMasterId.hashCode() : 0;
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (isEnableOver != null ? isEnableOver.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        return result;
    }
}
