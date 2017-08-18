package dao;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by emily on 2017/5/19.
 */
public class DeductDetailEntity  implements Serializable {
    private Integer deductDetailId;
    private Integer prepayDeductMasterId;
    private Integer cashDetailId;
    private Integer companyId;
    private String calYm;
    private Integer deductType;
    private Integer money;
    private Integer creatorId;
    private Timestamp createDate;
    private Integer modifierId;
    private Timestamp modifyDate;

    public Integer getDeductDetailId() {
        return deductDetailId;
    }

    public void setDeductDetailId(Integer deductDetailId) {
        this.deductDetailId = deductDetailId;
    }

    public Integer getPrepayDeductMasterId() {
        return prepayDeductMasterId;
    }

    public void setPrepayDeductMasterId(Integer prepayDeductMasterId) {
        this.prepayDeductMasterId = prepayDeductMasterId;
    }

    public Integer getCashDetailId() {
        return cashDetailId;
    }

    public void setCashDetailId(Integer cashDetailId) {
        this.cashDetailId = cashDetailId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCalYm() {
        return calYm;
    }

    public void setCalYm(String calYm) {
        this.calYm = calYm;
    }

    public Integer getDeductType() {
        return deductType;
    }

    public void setDeductType(Integer deductType) {
        this.deductType = deductType;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
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

        DeductDetailEntity that = (DeductDetailEntity) o;

        if (deductDetailId != null ? !deductDetailId.equals(that.deductDetailId) : that.deductDetailId != null)
            return false;
        if (prepayDeductMasterId != null ? !prepayDeductMasterId.equals(that.prepayDeductMasterId) : that.prepayDeductMasterId != null)
            return false;
        if (cashDetailId != null ? !cashDetailId.equals(that.cashDetailId) : that.cashDetailId != null) return false;
        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (calYm != null ? !calYm.equals(that.calYm) : that.calYm != null) return false;
        if (deductType != null ? !deductType.equals(that.deductType) : that.deductType != null) return false;
        if (money != null ? !money.equals(that.money) : that.money != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = deductDetailId != null ? deductDetailId.hashCode() : 0;
        result = 31 * result + (prepayDeductMasterId != null ? prepayDeductMasterId.hashCode() : 0);
        result = 31 * result + (cashDetailId != null ? cashDetailId.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (calYm != null ? calYm.hashCode() : 0);
        result = 31 * result + (deductType != null ? deductType.hashCode() : 0);
        result = 31 * result + (money != null ? money.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        return result;
    }
}
