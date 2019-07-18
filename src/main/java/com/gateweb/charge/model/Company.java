/*
 * This file is generated by jOOQ.
 */
package com.gateweb.charge.model;


import javax.annotation.Generated;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "company", schema = "public", indexes = {
    @Index(name = "company_pkey", unique = true, columnList = "company_id ASC")
})
public class Company implements Serializable {

    private static final long serialVersionUID = 2142170046;

    private Integer   companyId;
    private Integer   parentId;
    private String    name;
    private String    businessNo;
    private Integer   companyType;
    private Integer   verifyStatus;
    private String    codeName;
    private String    phone;
    private String    fax;
    private String    contact1;
    private String    contactPhone1;
    private String    contact2;
    private String    contactPhone2;
    private Integer   cityId;
    private String    companyAddress;
    private String    mailingAddress;
    private String    taxNo;
    private String    taxOffice;
    private Integer   creatorId;
    private Timestamp createDate;
    private Integer   modifierId;
    private Timestamp modifyDate;
    private Integer   transferType;
    private String    topBanner;
    private String    bottomBanner;
    private String    email_1;
    private String    email_2;
    private String    companyKey;

    public Company() {}

    public Company(Company value) {
        this.companyId = value.companyId;
        this.parentId = value.parentId;
        this.name = value.name;
        this.businessNo = value.businessNo;
        this.companyType = value.companyType;
        this.verifyStatus = value.verifyStatus;
        this.codeName = value.codeName;
        this.phone = value.phone;
        this.fax = value.fax;
        this.contact1 = value.contact1;
        this.contactPhone1 = value.contactPhone1;
        this.contact2 = value.contact2;
        this.contactPhone2 = value.contactPhone2;
        this.cityId = value.cityId;
        this.companyAddress = value.companyAddress;
        this.mailingAddress = value.mailingAddress;
        this.taxNo = value.taxNo;
        this.taxOffice = value.taxOffice;
        this.creatorId = value.creatorId;
        this.createDate = value.createDate;
        this.modifierId = value.modifierId;
        this.modifyDate = value.modifyDate;
        this.transferType = value.transferType;
        this.topBanner = value.topBanner;
        this.bottomBanner = value.bottomBanner;
        this.email_1 = value.email_1;
        this.email_2 = value.email_2;
        this.companyKey = value.companyKey;
    }

    public Company(
        Integer   companyId,
        Integer   parentId,
        String    name,
        String    businessNo,
        Integer   companyType,
        Integer   verifyStatus,
        String    codeName,
        String    phone,
        String    fax,
        String    contact1,
        String    contactPhone1,
        String    contact2,
        String    contactPhone2,
        Integer   cityId,
        String    companyAddress,
        String    mailingAddress,
        String    taxNo,
        String    taxOffice,
        Integer   creatorId,
        Timestamp createDate,
        Integer   modifierId,
        Timestamp modifyDate,
        Integer   transferType,
        String    topBanner,
        String    bottomBanner,
        String    email_1,
        String    email_2,
        String    companyKey
    ) {
        this.companyId = companyId;
        this.parentId = parentId;
        this.name = name;
        this.businessNo = businessNo;
        this.companyType = companyType;
        this.verifyStatus = verifyStatus;
        this.codeName = codeName;
        this.phone = phone;
        this.fax = fax;
        this.contact1 = contact1;
        this.contactPhone1 = contactPhone1;
        this.contact2 = contact2;
        this.contactPhone2 = contactPhone2;
        this.cityId = cityId;
        this.companyAddress = companyAddress;
        this.mailingAddress = mailingAddress;
        this.taxNo = taxNo;
        this.taxOffice = taxOffice;
        this.creatorId = creatorId;
        this.createDate = createDate;
        this.modifierId = modifierId;
        this.modifyDate = modifyDate;
        this.transferType = transferType;
        this.topBanner = topBanner;
        this.bottomBanner = bottomBanner;
        this.email_1 = email_1;
        this.email_2 = email_2;
        this.companyKey = companyKey;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id", unique = true, nullable = false, precision = 32)
    public Integer getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @Column(name = "parent_id", precision = 32)
    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Column(name = "name", nullable = false, length = 40)
    @NotNull
    @Size(max = 40)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "business_no", nullable = false, length = 8)
    @NotNull
    @Size(max = 8)
    public String getBusinessNo() {
        return this.businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    @Column(name = "company_type", precision = 32)
    public Integer getCompanyType() {
        return this.companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    @Column(name = "verify_status", precision = 32)
    public Integer getVerifyStatus() {
        return this.verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    @Column(name = "code_name", length = 10)
    @Size(max = 10)
    public String getCodeName() {
        return this.codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    @Column(name = "phone", length = 50)
    @Size(max = 50)
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "fax", length = 50)
    @Size(max = 50)
    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Column(name = "contact1", length = 40)
    @Size(max = 40)
    public String getContact1() {
        return this.contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    @Column(name = "contact_phone1", length = 50)
    @Size(max = 50)
    public String getContactPhone1() {
        return this.contactPhone1;
    }

    public void setContactPhone1(String contactPhone1) {
        this.contactPhone1 = contactPhone1;
    }

    @Column(name = "contact2", length = 40)
    @Size(max = 40)
    public String getContact2() {
        return this.contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    @Column(name = "contact_phone2", length = 50)
    @Size(max = 50)
    public String getContactPhone2() {
        return this.contactPhone2;
    }

    public void setContactPhone2(String contactPhone2) {
        this.contactPhone2 = contactPhone2;
    }

    @Column(name = "city_id", precision = 32)
    public Integer getCityId() {
        return this.cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    @Column(name = "company_address", nullable = false, length = 100)
    @NotNull
    @Size(max = 100)
    public String getCompanyAddress() {
        return this.companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    @Column(name = "mailing_address", length = 100)
    @Size(max = 100)
    public String getMailingAddress() {
        return this.mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    @Column(name = "tax_no", length = 9)
    @Size(max = 9)
    public String getTaxNo() {
        return this.taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    @Column(name = "tax_office", length = 50)
    @Size(max = 50)
    public String getTaxOffice() {
        return this.taxOffice;
    }

    public void setTaxOffice(String taxOffice) {
        this.taxOffice = taxOffice;
    }

    @Column(name = "creator_id", precision = 32)
    public Integer getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    @Column(name = "create_date")
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Column(name = "modifier_id", precision = 32)
    public Integer getModifierId() {
        return this.modifierId;
    }

    public void setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
    }

    @Column(name = "modify_date")
    public Timestamp getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Column(name = "transfer_type", precision = 32)
    public Integer getTransferType() {
        return this.transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    @Column(name = "top_banner", length = 40)
    @Size(max = 40)
    public String getTopBanner() {
        return this.topBanner;
    }

    public void setTopBanner(String topBanner) {
        this.topBanner = topBanner;
    }

    @Column(name = "bottom_banner", length = 40)
    @Size(max = 40)
    public String getBottomBanner() {
        return this.bottomBanner;
    }

    public void setBottomBanner(String bottomBanner) {
        this.bottomBanner = bottomBanner;
    }

    @Column(name = "email_1", length = 100)
    @Size(max = 100)
    public String getEmail_1() {
        return this.email_1;
    }

    public void setEmail_1(String email_1) {
        this.email_1 = email_1;
    }

    @Column(name = "email_2", length = 100)
    @Size(max = 100)
    public String getEmail_2() {
        return this.email_2;
    }

    public void setEmail_2(String email_2) {
        this.email_2 = email_2;
    }

    @Column(name = "company_key", length = 50)
    @Size(max = 50)
    public String getCompanyKey() {
        return this.companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Company (");

        sb.append(companyId);
        sb.append(", ").append(parentId);
        sb.append(", ").append(name);
        sb.append(", ").append(businessNo);
        sb.append(", ").append(companyType);
        sb.append(", ").append(verifyStatus);
        sb.append(", ").append(codeName);
        sb.append(", ").append(phone);
        sb.append(", ").append(fax);
        sb.append(", ").append(contact1);
        sb.append(", ").append(contactPhone1);
        sb.append(", ").append(contact2);
        sb.append(", ").append(contactPhone2);
        sb.append(", ").append(cityId);
        sb.append(", ").append(companyAddress);
        sb.append(", ").append(mailingAddress);
        sb.append(", ").append(taxNo);
        sb.append(", ").append(taxOffice);
        sb.append(", ").append(creatorId);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifierId);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(transferType);
        sb.append(", ").append(topBanner);
        sb.append(", ").append(bottomBanner);
        sb.append(", ").append(email_1);
        sb.append(", ").append(email_2);
        sb.append(", ").append(companyKey);

        sb.append(")");
        return sb.toString();
    }
}
