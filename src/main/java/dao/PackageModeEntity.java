package dao;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by emily on 2015/12/17.
 */
public class PackageModeEntity implements Serializable{
    private Integer packageId;
    private Integer packageType;
    private Integer chargeId;
    private Integer additionId;
    private Integer companyId;
    private Integer dealerId;
    private String status;
    private Integer creatorId;
    private Timestamp createDate;
    private Integer modifierId;
    private Timestamp modifyDate;
    private Integer dealerCompanyId;
    private String brokerCp2;
    private String broker2;
    private String brokerCp3;
    private String broker3;

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getPackageType() {
        return packageType;
    }

    public void setPackageType(Integer packageType) {
        this.packageType = packageType;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public Integer getAdditionId() {
        return additionId;
    }

    public void setAdditionId(Integer additionId) {
        this.additionId = additionId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getBrokerCp2() {
        return brokerCp2;
    }

    public void setBrokerCp2(String brokerCp2) {
        this.brokerCp2 = brokerCp2;
    }

    public String getBroker2() {
        return broker2;
    }

    public void setBroker2(String broker2) {
        this.broker2 = broker2;
    }

    public String getBrokerCp3() {
        return brokerCp3;
    }

    public void setBrokerCp3(String brokerCp3) {
        this.brokerCp3 = brokerCp3;
    }

    public String getBroker3() {
        return broker3;
    }

    public void setBroker3(String broker3) {
        this.broker3 = broker3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PackageModeEntity that = (PackageModeEntity) o;

        if (packageId != that.packageId) return false;
        if (packageType != null ? !packageType.equals(that.packageType) : that.packageType != null) return false;
        if (chargeId != null ? !chargeId.equals(that.chargeId) : that.chargeId != null) return false;
        if (additionId != null ? !additionId.equals(that.additionId) : that.additionId != null) return false;
        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (dealerId != null ? !dealerId.equals(that.dealerId) : that.dealerId != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (dealerCompanyId != null ? !dealerCompanyId.equals(that.dealerCompanyId) : that.dealerCompanyId != null)
            return false;
        if (brokerCp2 != null ? !brokerCp2.equals(that.brokerCp2) : that.brokerCp2 != null) return false;
        if (broker2 != null ? !broker2.equals(that.broker2) : that.broker2 != null) return false;
        if (brokerCp3 != null ? !brokerCp3.equals(that.brokerCp3) : that.brokerCp3 != null) return false;
        if (broker3 != null ? !broker3.equals(that.broker3) : that.broker3 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = packageId != null ? packageId.hashCode() : 0;
        result = 31 * result + (packageType != null ? packageType.hashCode() : 0);
        result = 31 * result + (chargeId != null ? chargeId.hashCode() : 0);
        result = 31 * result + (additionId != null ? additionId.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (dealerId != null ? dealerId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (dealerCompanyId != null ? dealerCompanyId.hashCode() : 0);
        result = 31 * result + (brokerCp2 != null ? brokerCp2.hashCode() : 0);
        result = 31 * result + (broker2 != null ? broker2.hashCode() : 0);
        result = 31 * result + (brokerCp3 != null ? brokerCp3.hashCode() : 0);
        result = 31 * result + (broker3 != null ? broker3.hashCode() : 0);
        return result;
    }
}
