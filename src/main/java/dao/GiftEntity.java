package dao;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by emily on 2015/12/17.
 */
public class GiftEntity implements Serializable{
    private Integer giftId;
    private Integer companyId;
    private Date giftDate;
    private Integer giftCnt;
    private Integer creatorId;
    private Timestamp createDate;
    private Integer modifierId;
    private Timestamp modifyDate;
    private Integer calId;
    private Integer outId;
    private Integer inId;

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Date getGiftDate() {
        return giftDate;
    }

    public void setGiftDate(Date giftDate) {
        this.giftDate = giftDate;
    }

    public Integer getGiftCnt() {
        return giftCnt;
    }

    public void setGiftCnt(Integer giftCnt) {
        this.giftCnt = giftCnt;
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

    public Integer getCalId() {
        return calId;
    }

    public void setCalId(Integer calId) {
        this.calId = calId;
    }

    public Integer getOutId() {
        return outId;
    }

    public void setOutId(Integer outId) {
        this.outId = outId;
    }

    public Integer getInId() {
        return inId;
    }

    public void setInId(Integer inId) {
        this.inId = inId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GiftEntity that = (GiftEntity) o;

        if (giftId != that.giftId) return false;
        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (giftDate != null ? !giftDate.equals(that.giftDate) : that.giftDate != null) return false;
        if (giftCnt != null ? !giftCnt.equals(that.giftCnt) : that.giftCnt != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (calId != null ? !calId.equals(that.calId) : that.calId != null) return false;
        if (outId != null ? !outId.equals(that.outId) : that.outId != null) return false;
        if (inId != null ? !inId.equals(that.inId) : that.inId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = giftId != null ? giftId.hashCode() : 0;
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (giftDate != null ? giftDate.hashCode() : 0);
        result = 31 * result + (giftCnt != null ? giftCnt.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (calId != null ? calId.hashCode() : 0);
        result = 31 * result + (outId != null ? outId.hashCode() : 0);
        result = 31 * result + (inId != null ? inId.hashCode() : 0);
        return result;
    }
}
