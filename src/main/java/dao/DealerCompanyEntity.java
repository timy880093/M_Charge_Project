package dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by emily on 2015/12/17.
 */
public class DealerCompanyEntity implements Serializable {
    private Integer dealerCompanyId;
    private BigDecimal mainAmount;
    private BigDecimal mainPercent;
    private BigDecimal collectMoney;
    private String commissionType;
    private Integer status;
    private Integer creatorId;
    private Timestamp createDate;
    private Integer modifierId;
    private Timestamp modifyDate;
    private BigDecimal additionPercent;
    private String dealerCompanyName;
    private String businessNo;
    private String phone;
    private String fax;
    private String companyAddress;
    private String email;

    public Integer getDealerCompanyId() {
        return dealerCompanyId;
    }

    public void setDealerCompanyId(Integer dealerCompanyId) {
        this.dealerCompanyId = dealerCompanyId;
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

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
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

    public BigDecimal getAdditionPercent() {
        return additionPercent;
    }

    public void setAdditionPercent(BigDecimal additionPercent) {
        this.additionPercent = additionPercent;
    }

    public String getDealerCompanyName() {
        return dealerCompanyName;
    }

    public void setDealerCompanyName(String dealerCompanyName) {
        this.dealerCompanyName = dealerCompanyName;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DealerCompanyEntity that = (DealerCompanyEntity) o;

        if (dealerCompanyId != that.dealerCompanyId) return false;
        if (mainAmount != null ? !mainAmount.equals(that.mainAmount) : that.mainAmount != null) return false;
        if (mainPercent != null ? !mainPercent.equals(that.mainPercent) : that.mainPercent != null) return false;
        if (collectMoney != null ? !collectMoney.equals(that.collectMoney) : that.collectMoney != null) return false;
        if (commissionType != null ? !commissionType.equals(that.commissionType) : that.commissionType != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (additionPercent != null ? !additionPercent.equals(that.additionPercent) : that.additionPercent != null)
            return false;
        if (dealerCompanyName != null ? !dealerCompanyName.equals(that.dealerCompanyName) : that.dealerCompanyName != null)
            return false;
        if (businessNo != null ? !businessNo.equals(that.businessNo) : that.businessNo != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (fax != null ? !fax.equals(that.fax) : that.fax != null) return false;
        if (companyAddress != null ? !companyAddress.equals(that.companyAddress) : that.companyAddress != null)
            return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dealerCompanyId != null ? dealerCompanyId.hashCode() : 0;
        result = 31 * result + (mainAmount != null ? mainAmount.hashCode() : 0);
        result = 31 * result + (mainPercent != null ? mainPercent.hashCode() : 0);
        result = 31 * result + (collectMoney != null ? collectMoney.hashCode() : 0);
        result = 31 * result + (commissionType != null ? commissionType.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (additionPercent != null ? additionPercent.hashCode() : 0);
        result = 31 * result + (dealerCompanyName != null ? dealerCompanyName.hashCode() : 0);
        result = 31 * result + (businessNo != null ? businessNo.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (fax != null ? fax.hashCode() : 0);
        result = 31 * result + (companyAddress != null ? companyAddress.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
