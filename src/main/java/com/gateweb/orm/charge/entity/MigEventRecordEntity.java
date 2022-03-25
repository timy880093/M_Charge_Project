package com.gateweb.orm.charge.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "mig_event_record")
public class MigEventRecordEntity implements Serializable {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    protected java.lang.Long id;

    @Column(name = "mig_number", unique = true)
    protected java.lang.String migNumber;

    @Column(name = "mig_type", unique = true, nullable = false)
    protected java.lang.String migType;

    @Column(name = "year_month", unique = true)
    protected java.lang.String yearMonth;

    @Column(name = "upload_business_no", unique = true, nullable = false)
    protected java.lang.String uploadBusinessNo;

    @Column(name = "seller_identifier", unique = true)
    protected java.lang.String sellerIdentifier;

    @Column(name = "buyer_identifier", unique = true)
    protected java.lang.String buyerIdentifier;

    @Column(name = "in_out_bound", unique = true, nullable = false)
    protected java.lang.String inOutBound;

    @Column(name = "einv_upload_status", unique = true)
    protected java.lang.String einvUploadStatus;

    @Column(name = "einv_upload_reply_download_status", unique = true)
    protected java.lang.String einvUploadReplyDownloadStatus;

    @Column(name = "create_date", unique = true, nullable = false)
    protected Timestamp createDate;

    @Column(name = "modify_date", unique = true, nullable = false)
    protected Timestamp modifyDate;

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

    public String getUploadBusinessNo() {
        return uploadBusinessNo;
    }

    public void setUploadBusinessNo(String uploadBusinessNo) {
        this.uploadBusinessNo = uploadBusinessNo;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MigEventRecordEntity that = (MigEventRecordEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(migNumber, that.migNumber) &&
                Objects.equals(migType, that.migType) &&
                Objects.equals(yearMonth, that.yearMonth) &&
                Objects.equals(uploadBusinessNo, that.uploadBusinessNo) &&
                Objects.equals(sellerIdentifier, that.sellerIdentifier) &&
                Objects.equals(buyerIdentifier, that.buyerIdentifier) &&
                Objects.equals(inOutBound, that.inOutBound) &&
                Objects.equals(einvUploadStatus, that.einvUploadStatus) &&
                Objects.equals(einvUploadReplyDownloadStatus, that.einvUploadReplyDownloadStatus) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(modifyDate, that.modifyDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, migNumber, migType, yearMonth, uploadBusinessNo, sellerIdentifier, buyerIdentifier, inOutBound, einvUploadStatus, einvUploadReplyDownloadStatus, createDate, modifyDate);
    }

    @Override
    public String toString() {
        return "MigEventRecordEntity{" +
                "id=" + id +
                ", migNumber='" + migNumber + '\'' +
                ", migType='" + migType + '\'' +
                ", yearMonth='" + yearMonth + '\'' +
                ", uploadBusinessNo='" + uploadBusinessNo + '\'' +
                ", sellerIdentifier='" + sellerIdentifier + '\'' +
                ", buyerIdentifier='" + buyerIdentifier + '\'' +
                ", inOutBound='" + inOutBound + '\'' +
                ", einvUploadStatus='" + einvUploadStatus + '\'' +
                ", einvUploadReplyDownloadStatus='" + einvUploadReplyDownloadStatus + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                '}';
    }
}
