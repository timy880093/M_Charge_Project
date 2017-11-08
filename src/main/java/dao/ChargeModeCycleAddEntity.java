package dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by emily on 2015/12/17.
 */
public class ChargeModeCycleAddEntity implements Serializable {
    public Integer additionId;
    public Date startDate;
    public Date endDate;
    public Date realStartDate;
    public Date realEndDate;
    public Integer additionQuantity;
    public Integer freeMonth;
    public Integer creatorId;
    public Integer modifierId;
    public Timestamp createDate;
    public Timestamp modifyDate;
    public BigDecimal giftPrice;

    public Integer getAdditionId() {
        return additionId;
    }

    public void setAdditionId(Integer additionId) {
        this.additionId = additionId;
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

    public Date getRealStartDate() {
        return realStartDate;
    }

    public void setRealStartDate(Date realStartDate) {
        this.realStartDate = realStartDate;
    }

    public Date getRealEndDate() {
        return realEndDate;
    }

    public void setRealEndDate(Date realEndDate) {
        this.realEndDate = realEndDate;
    }

    public Integer getAdditionQuantity() {
        return additionQuantity;
    }

    public void setAdditionQuantity(Integer additionQuantity) {
        this.additionQuantity = additionQuantity;
    }

    public Integer getFreeMonth() {
        return freeMonth;
    }

    public void setFreeMonth(Integer freeMonth) {
        this.freeMonth = freeMonth;
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

    public BigDecimal getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(BigDecimal giftPrice) {
        this.giftPrice = giftPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChargeModeCycleAddEntity that = (ChargeModeCycleAddEntity) o;

        if (additionId != that.additionId) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (realStartDate != null ? !realStartDate.equals(that.realStartDate) : that.realStartDate != null)
            return false;
        if (realEndDate != null ? !realEndDate.equals(that.realEndDate) : that.realEndDate != null) return false;
        if (additionQuantity != null ? !additionQuantity.equals(that.additionQuantity) : that.additionQuantity != null)
            return false;
        if (freeMonth != null ? !freeMonth.equals(that.freeMonth) : that.freeMonth != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (giftPrice != null ? !giftPrice.equals(that.giftPrice) : that.giftPrice != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = additionId != null ? additionId.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (realStartDate != null ? realStartDate.hashCode() : 0);
        result = 31 * result + (realEndDate != null ? realEndDate.hashCode() : 0);
        result = 31 * result + (additionQuantity != null ? additionQuantity.hashCode() : 0);
        result = 31 * result + (freeMonth != null ? freeMonth.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (giftPrice != null ? giftPrice.hashCode() : 0);
        return result;
    }
}
