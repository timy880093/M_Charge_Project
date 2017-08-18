package dao;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by emily on 2015/12/17.
 */
public class DealerEntity implements Serializable{
    private Integer dealerId;
    private String dealerName;
    private String dealerPhone;
    private String dealerEmail;
    private Integer creatorId;
    private Timestamp createDate;
    private Integer modifierId;
    private Timestamp modifyDate;
    private Integer dealerCompanyId;

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerPhone() {
        return dealerPhone;
    }

    public void setDealerPhone(String dealerPhone) {
        this.dealerPhone = dealerPhone;
    }

    public String getDealerEmail() {
        return dealerEmail;
    }

    public void setDealerEmail(String dealerEmail) {
        this.dealerEmail = dealerEmail;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DealerEntity that = (DealerEntity) o;

        if (dealerId != that.dealerId) return false;
        if (dealerName != null ? !dealerName.equals(that.dealerName) : that.dealerName != null) return false;
        if (dealerPhone != null ? !dealerPhone.equals(that.dealerPhone) : that.dealerPhone != null) return false;
        if (dealerEmail != null ? !dealerEmail.equals(that.dealerEmail) : that.dealerEmail != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (dealerCompanyId != null ? !dealerCompanyId.equals(that.dealerCompanyId) : that.dealerCompanyId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dealerId != null ? dealerId.hashCode() : 0;
        result = 31 * result + (dealerName != null ? dealerName.hashCode() : 0);
        result = 31 * result + (dealerPhone != null ? dealerPhone.hashCode() : 0);
        result = 31 * result + (dealerEmail != null ? dealerEmail.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (dealerCompanyId != null ? dealerCompanyId.hashCode() : 0);
        return result;
    }
}
