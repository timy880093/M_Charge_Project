package dao;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by emily on 2017/5/12.
 */
public class ChargeModeGradeEntity implements Serializable{
    private Integer chargeId;
    private String packageName;
    private Integer chargeCycle;
    private Integer status;
    private Integer freeMonth;
    private Integer contractLimit;
    private Integer feePeriod;
    private Integer baseQuantity;
    private Integer salesPrice;
    private String hasGrade;
    private Integer gradePrice;
    private Integer gradeCnt;
    private Integer creatorId;
    private Timestamp createDate;
    private Integer modifierId;
    private Timestamp modifyDate;
    private Integer prePayment;

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

    public Integer getChargeCycle() {
        return chargeCycle;
    }

    public void setChargeCycle(Integer chargeCycle) {
        this.chargeCycle = chargeCycle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFreeMonth() {
        return freeMonth;
    }

    public void setFreeMonth(Integer freeMonth) {
        this.freeMonth = freeMonth;
    }

    public Integer getContractLimit() {
        return contractLimit;
    }

    public void setContractLimit(Integer contractLimit) {
        this.contractLimit = contractLimit;
    }

    public Integer getFeePeriod() {
        return feePeriod;
    }

    public void setFeePeriod(Integer feePeriod) {
        this.feePeriod = feePeriod;
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

    public String getHasGrade() {
        return hasGrade;
    }

    public void setHasGrade(String hasGrade) {
        this.hasGrade = hasGrade;
    }

    public Integer getGradePrice() {
        return gradePrice;
    }

    public void setGradePrice(Integer gradePrice) {
        this.gradePrice = gradePrice;
    }

    public Integer getGradeCnt() {
        return gradeCnt;
    }

    public void setGradeCnt(Integer gradeCnt) {
        this.gradeCnt = gradeCnt;
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

    public Integer getPrePayment() {
        return prePayment;
    }

    public void setPrePayment(Integer prePayment) {
        this.prePayment = prePayment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChargeModeGradeEntity that = (ChargeModeGradeEntity) o;

        if (chargeId != null ? !chargeId.equals(that.chargeId) : that.chargeId != null)
            return false;
        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) return false;
        if (chargeCycle != null ? !chargeCycle.equals(that.chargeCycle) : that.chargeCycle != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (freeMonth != null ? !freeMonth.equals(that.freeMonth) : that.freeMonth != null) return false;
        if (contractLimit != null ? !contractLimit.equals(that.contractLimit) : that.contractLimit != null)
            return false;
        if (feePeriod != null ? !feePeriod.equals(that.feePeriod) : that.feePeriod != null) return false;
        if (baseQuantity != null ? !baseQuantity.equals(that.baseQuantity) : that.baseQuantity != null) return false;
        if (salesPrice != null ? !salesPrice.equals(that.salesPrice) : that.salesPrice != null) return false;
        if (hasGrade != null ? !hasGrade.equals(that.hasGrade) : that.hasGrade != null) return false;
        if (gradePrice != null ? !gradePrice.equals(that.gradePrice) : that.gradePrice != null) return false;
        if (gradeCnt != null ? !gradeCnt.equals(that.gradeCnt) : that.gradeCnt != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (prePayment != null ? !prePayment.equals(that.prePayment) : that.prePayment != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargeId != null ? chargeId.hashCode() : 0;
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (chargeCycle != null ? chargeCycle.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (freeMonth != null ? freeMonth.hashCode() : 0);
        result = 31 * result + (contractLimit != null ? contractLimit.hashCode() : 0);
        result = 31 * result + (feePeriod != null ? feePeriod.hashCode() : 0);
        result = 31 * result + (baseQuantity != null ? baseQuantity.hashCode() : 0);
        result = 31 * result + (salesPrice != null ? salesPrice.hashCode() : 0);
        result = 31 * result + (hasGrade != null ? hasGrade.hashCode() : 0);
        result = 31 * result + (gradePrice != null ? gradePrice.hashCode() : 0);
        result = 31 * result + (gradeCnt != null ? gradeCnt.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (prePayment != null ? prePayment.hashCode() : 0);
        return result;
    }
}
