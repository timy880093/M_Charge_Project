/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(name = "user", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "user_pkey", columnNames = {"user_id"}),
        @UniqueConstraint(name = "user_account_key", columnNames = {"account"})
})
public class ChargeUser implements Serializable {

    private static final long serialVersionUID = 347245216;

    private Integer userId;
    private Integer companyId;
    private Integer roleId;
    private String account;
    private String password;
    private String name;
    private String email;
    private Boolean close;
    private Integer creatorId;
    private LocalDateTime createDate;
    private Integer modifierId;
    private LocalDateTime modifyDate;
    private Integer printerId;
    private String authUrl;
    private String authKey;
    private Integer logoutTime;
    private String defaultTaxType;
    private String defaultB2bFlag;
    private String roles;
    private LocalDateTime lastModifiedPassword;
    private LocalDateTime expiredDate;
    private LocalDateTime allowCookies;
    private Integer registrationId;
    private String storeIdentifier;
    private String automaticOpenInvoice;

    public ChargeUser() {
    }

    public ChargeUser(ChargeUser value) {
        this.userId = value.userId;
        this.companyId = value.companyId;
        this.roleId = value.roleId;
        this.account = value.account;
        this.password = value.password;
        this.name = value.name;
        this.email = value.email;
        this.close = value.close;
        this.creatorId = value.creatorId;
        this.createDate = value.createDate;
        this.modifierId = value.modifierId;
        this.modifyDate = value.modifyDate;
        this.printerId = value.printerId;
        this.authUrl = value.authUrl;
        this.authKey = value.authKey;
        this.logoutTime = value.logoutTime;
        this.defaultTaxType = value.defaultTaxType;
        this.defaultB2bFlag = value.defaultB2bFlag;
        this.roles = value.roles;
        this.lastModifiedPassword = value.lastModifiedPassword;
        this.expiredDate = value.expiredDate;
        this.allowCookies = value.allowCookies;
        this.registrationId = value.registrationId;
        this.storeIdentifier = value.storeIdentifier;
        this.automaticOpenInvoice = value.automaticOpenInvoice;
    }

    public ChargeUser(
            Integer userId,
            Integer companyId,
            Integer roleId,
            String account,
            String password,
            String name,
            String email,
            Boolean close,
            Integer creatorId,
            LocalDateTime createDate,
            Integer modifierId,
            LocalDateTime modifyDate,
            Integer printerId,
            String authUrl,
            String authKey,
            Integer logoutTime,
            String defaultTaxType,
            String defaultB2bFlag,
            String roles,
            LocalDateTime lastModifiedPassword,
            LocalDateTime expiredDate,
            LocalDateTime allowCookies,
            Integer registrationId,
            String storeIdentifier,
            String automaticOpenInvoice
    ) {
        this.userId = userId;
        this.companyId = companyId;
        this.roleId = roleId;
        this.account = account;
        this.password = password;
        this.name = name;
        this.email = email;
        this.close = close;
        this.creatorId = creatorId;
        this.createDate = createDate;
        this.modifierId = modifierId;
        this.modifyDate = modifyDate;
        this.printerId = printerId;
        this.authUrl = authUrl;
        this.authKey = authKey;
        this.logoutTime = logoutTime;
        this.defaultTaxType = defaultTaxType;
        this.defaultB2bFlag = defaultB2bFlag;
        this.roles = roles;
        this.lastModifiedPassword = lastModifiedPassword;
        this.expiredDate = expiredDate;
        this.allowCookies = allowCookies;
        this.registrationId = registrationId;
        this.storeIdentifier = storeIdentifier;
        this.automaticOpenInvoice = automaticOpenInvoice;
    }

    @Id
    @Column(name = "user_id", nullable = false, precision = 32)
    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "company_id", precision = 32)
    public Integer getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @Column(name = "role_id", nullable = false, precision = 32)
    @NotNull
    public Integer getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Column(name = "account", nullable = false, length = 30)
    @NotNull
    @Size(max = 30)
    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Column(name = "password", nullable = false, length = 256)
    @NotNull
    @Size(max = 256)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "name", length = 30)
    @Size(max = 30)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "email", length = 50)
    @Size(max = 50)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "close", nullable = false)
    public Boolean getClose() {
        return this.close;
    }

    public void setClose(Boolean close) {
        this.close = close;
    }

    @Column(name = "creator_id", precision = 32)
    public Integer getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    @Column(name = "create_date")
    public LocalDateTime getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
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
    public LocalDateTime getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Column(name = "printer_id", precision = 32)
    public Integer getPrinterId() {
        return this.printerId;
    }

    public void setPrinterId(Integer printerId) {
        this.printerId = printerId;
    }

    @Column(name = "auth_url", length = 150)
    @Size(max = 150)
    public String getAuthUrl() {
        return this.authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    @Column(name = "auth_key", length = 50)
    @Size(max = 50)
    public String getAuthKey() {
        return this.authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    @Column(name = "logout_time", precision = 32)
    public Integer getLogoutTime() {
        return this.logoutTime;
    }

    public void setLogoutTime(Integer logoutTime) {
        this.logoutTime = logoutTime;
    }

    @Column(name = "default_tax_type", length = 1)
    @Size(max = 1)
    public String getDefaultTaxType() {
        return this.defaultTaxType;
    }

    public void setDefaultTaxType(String defaultTaxType) {
        this.defaultTaxType = defaultTaxType;
    }

    @Column(name = "default_b2b_flag", length = 1)
    @Size(max = 1)
    public String getDefaultB2bFlag() {
        return this.defaultB2bFlag;
    }

    public void setDefaultB2bFlag(String defaultB2bFlag) {
        this.defaultB2bFlag = defaultB2bFlag;
    }

    @Column(name = "roles", length = 45)
    @Size(max = 45)
    public String getRoles() {
        return this.roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Column(name = "last_modified_password")
    public LocalDateTime getLastModifiedPassword() {
        return this.lastModifiedPassword;
    }

    public void setLastModifiedPassword(LocalDateTime lastModifiedPassword) {
        this.lastModifiedPassword = lastModifiedPassword;
    }

    @Column(name = "expired_date")
    public LocalDateTime getExpiredDate() {
        return this.expiredDate;
    }

    public void setExpiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    @Column(name = "allow_cookies")
    public LocalDateTime getAllowCookies() {
        return this.allowCookies;
    }

    public void setAllowCookies(LocalDateTime allowCookies) {
        this.allowCookies = allowCookies;
    }

    @Column(name = "registration_id", precision = 32)
    public Integer getRegistrationId() {
        return this.registrationId;
    }

    public void setRegistrationId(Integer registrationId) {
        this.registrationId = registrationId;
    }

    @Column(name = "store_identifier", length = 20)
    @Size(max = 20)
    public String getStoreIdentifier() {
        return this.storeIdentifier;
    }

    public void setStoreIdentifier(String storeIdentifier) {
        this.storeIdentifier = storeIdentifier;
    }

    @Column(name = "automatic_open_invoice", length = 1)
    @Size(max = 1)
    public String getAutomaticOpenInvoice() {
        return this.automaticOpenInvoice;
    }

    public void setAutomaticOpenInvoice(String automaticOpenInvoice) {
        this.automaticOpenInvoice = automaticOpenInvoice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("User (");

        sb.append(userId);
        sb.append(", ").append(companyId);
        sb.append(", ").append(roleId);
        sb.append(", ").append(account);
        sb.append(", ").append(password);
        sb.append(", ").append(name);
        sb.append(", ").append(email);
        sb.append(", ").append(close);
        sb.append(", ").append(creatorId);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifierId);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(printerId);
        sb.append(", ").append(authUrl);
        sb.append(", ").append(authKey);
        sb.append(", ").append(logoutTime);
        sb.append(", ").append(defaultTaxType);
        sb.append(", ").append(defaultB2bFlag);
        sb.append(", ").append(roles);
        sb.append(", ").append(lastModifiedPassword);
        sb.append(", ").append(expiredDate);
        sb.append(", ").append(allowCookies);
        sb.append(", ").append(registrationId);
        sb.append(", ").append(storeIdentifier);
        sb.append(", ").append(automaticOpenInvoice);

        sb.append(")");
        return sb.toString();
    }
}
