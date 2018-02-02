package com.gateweb.charge.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by emily on 2015/12/17.
 */
public class ChargeModeSingleEntity implements Serializable {
    public Integer chargeId;
    public String packageName;
    public Integer baseQuantity;
    public Integer salesPrice;
    public Integer maxPrice;
    public Date availableStart;
    public Date availableEnd;
    public Integer status;
    public Integer creatorId;
    public Timestamp createDate;
    public Integer modifierId;
    public Timestamp modifyDate;
    public Double pointPrice;
    public Integer contractLimit;

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getBaseQuantity() {
        return baseQuantity;
    }

    public void setBaseQuantity(Integer baseQuantity) {
        this.baseQuantity = baseQuantity;
    }

    public Integer getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Integer salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Date getAvailableStart() {
        return availableStart;
    }

    public void setAvailableStart(Date availableStart) {
        this.availableStart = availableStart;
    }

    public Date getAvailableEnd() {
        return availableEnd;
    }

    public void setAvailableEnd(Date availableEnd) {
        this.availableEnd = availableEnd;
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

    public Double getPointPrice() {
        return pointPrice;
    }

    public void setPointPrice(Double pointPrice) {
        this.pointPrice = pointPrice;
    }

    public Integer getContractLimit() {
        return contractLimit;
    }

    public void setContractLimit(Integer contractLimit) {
        this.contractLimit = contractLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChargeModeSingleEntity that = (ChargeModeSingleEntity) o;

        if (chargeId != that.chargeId) return false;
        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) return false;
        if (baseQuantity != null ? !baseQuantity.equals(that.baseQuantity) : that.baseQuantity != null) return false;
        if (salesPrice != null ? !salesPrice.equals(that.salesPrice) : that.salesPrice != null) return false;
        if (maxPrice != null ? !maxPrice.equals(that.maxPrice) : that.maxPrice != null) return false;
        if (availableStart != null ? !availableStart.equals(that.availableStart) : that.availableStart != null)
            return false;
        if (availableEnd != null ? !availableEnd.equals(that.availableEnd) : that.availableEnd != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (pointPrice != null ? !pointPrice.equals(that.pointPrice) : that.pointPrice != null) return false;
        if (contractLimit != null ? !contractLimit.equals(that.contractLimit) : that.contractLimit != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargeId != null ? chargeId.hashCode() : 0;
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (baseQuantity != null ? baseQuantity.hashCode() : 0);
        result = 31 * result + (salesPrice != null ? salesPrice.hashCode() : 0);
        result = 31 * result + (maxPrice != null ? maxPrice.hashCode() : 0);
        result = 31 * result + (availableStart != null ? availableStart.hashCode() : 0);
        result = 31 * result + (availableEnd != null ? availableEnd.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (pointPrice != null ? pointPrice.hashCode() : 0);
        result = 31 * result + (contractLimit != null ? contractLimit.hashCode() : 0);
        return result;
    }
}
