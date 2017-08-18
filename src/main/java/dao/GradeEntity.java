package dao;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by emily on 2017/5/11.
 */
public class GradeEntity implements Serializable{
    private Integer gradeId;
    private Integer chargeId;
    private Integer cntStart;
    private Integer cntEnd;
    private Integer price;
    private Integer creatorId;
    private Timestamp createDate;
    private Integer modifierId;
    private Timestamp modifyDate;

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public Integer getCntStart() {
        return cntStart;
    }

    public void setCntStart(Integer cntStart) {
        this.cntStart = cntStart;
    }

    public Integer getCntEnd() {
        return cntEnd;
    }

    public void setCntEnd(Integer cntEnd) {
        this.cntEnd = cntEnd;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

        GradeEntity that = (GradeEntity) o;

        if (gradeId != null ? !gradeId.equals(that.gradeId) : that.gradeId != null) return false;
        if (chargeId != null ? !chargeId.equals(that.chargeId) : that.chargeId != null)
            return false;
        if (cntStart != null ? !cntStart.equals(that.cntStart) : that.cntStart != null) return false;
        if (cntEnd != null ? !cntEnd.equals(that.cntEnd) : that.cntEnd != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = gradeId != null ? gradeId.hashCode() : 0;
        result = 31 * result + (chargeId != null ? chargeId.hashCode() : 0);
        result = 31 * result + (cntStart != null ? cntStart.hashCode() : 0);
        result = 31 * result + (cntEnd != null ? cntEnd.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        return result;
    }
}
