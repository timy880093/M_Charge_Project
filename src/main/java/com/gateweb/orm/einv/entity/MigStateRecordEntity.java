package com.gateweb.orm.einv.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "einvMigStateRecordEntity")
@Table(name = "mig_state_record")
public class MigStateRecordEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    protected Long id;

    @Column(name = "mig_number")
    protected String migNumber;

    @Column(name = "mig_type")
    protected String migType;

    @Column(name = "year_month")
    protected String yearMonth;

    @Column(name = "seller_identifier")
    protected String sellerIdentifier;

    @Column(name = "buyer_identifier")
    protected String buyerIdentifier;

    @Column(name = "in_out_bound")
    protected String inOutBound;

    @Column(name = "create_date")
    protected Timestamp createDate;

    @Column(name = "modify_date")
    protected Timestamp modifyDate;

    @Column(name = "einv_upload_status")
    protected String einvUploadStatus;

    @Column(name = "einv_upload_reply_download_status")
    protected String einvUploadReplyDownloadStatus;

    @Column(name = "company_key")
    protected String uploadCompanyKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMigNumber() {
        return migNumber;
    }

    public void setMigNumber(String migNumber) {
        this.migNumber = migNumber;
    }

    public String getMigType() {
        return migType;
    }

    public void setMigType(String migType) {
        this.migType = migType;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getSellerIdentifier() {
        return sellerIdentifier;
    }

    public void setSellerIdentifier(String sellerIdentifier) {
        this.sellerIdentifier = sellerIdentifier;
    }

    public String getBuyerIdentifier() {
        return buyerIdentifier;
    }

    public void setBuyerIdentifier(String buyerIdentifier) {
        this.buyerIdentifier = buyerIdentifier;
    }

    public String getInOutBound() {
        return inOutBound;
    }

    public void setInOutBound(String inOutBound) {
        this.inOutBound = inOutBound;
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

    public String getEinvUploadStatus() {
        return einvUploadStatus;
    }

    public void setEinvUploadStatus(String einvUploadStatus) {
        this.einvUploadStatus = einvUploadStatus;
    }

    public String getEinvUploadReplyDownloadStatus() {
        return einvUploadReplyDownloadStatus;
    }

    public void setEinvUploadReplyDownloadStatus(String einvUploadReplyDownloadStatus) {
        this.einvUploadReplyDownloadStatus = einvUploadReplyDownloadStatus;
    }

    public String getUploadCompanyKey() {
        return uploadCompanyKey;
    }

    public void setUploadCompanyKey(String uploadCompanyKey) {
        this.uploadCompanyKey = uploadCompanyKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MigStateRecordEntity that = (MigStateRecordEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(migNumber, that.migNumber) &&
                Objects.equals(migType, that.migType) &&
                Objects.equals(yearMonth, that.yearMonth) &&
                Objects.equals(sellerIdentifier, that.sellerIdentifier) &&
                Objects.equals(buyerIdentifier, that.buyerIdentifier) &&
                Objects.equals(inOutBound, that.inOutBound) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(modifyDate, that.modifyDate) &&
                Objects.equals(einvUploadStatus, that.einvUploadStatus) &&
                Objects.equals(einvUploadReplyDownloadStatus, that.einvUploadReplyDownloadStatus) &&
                Objects.equals(uploadCompanyKey, that.uploadCompanyKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, migNumber, migType, yearMonth, sellerIdentifier, buyerIdentifier, inOutBound, createDate, modifyDate, einvUploadStatus, einvUploadReplyDownloadStatus, uploadCompanyKey);
    }

    @Override
    public String toString() {
        return "MigStateRecordEntity{" +
                "id=" + id +
                ", migNumber='" + migNumber + '\'' +
                ", migType='" + migType + '\'' +
                ", yearMonth='" + yearMonth + '\'' +
                ", sellerIdentifier='" + sellerIdentifier + '\'' +
                ", buyerIdentifier='" + buyerIdentifier + '\'' +
                ", inOutBound='" + inOutBound + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", einvUploadStatus='" + einvUploadStatus + '\'' +
                ", einvUploadReplyDownloadStatus='" + einvUploadReplyDownloadStatus + '\'' +
                ", uploadCompanyKey='" + uploadCompanyKey + '\'' +
                '}';
    }
}
