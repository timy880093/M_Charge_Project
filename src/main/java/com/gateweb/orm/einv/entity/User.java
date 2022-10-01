/*
 * $Header: $
 * This java source file is generated by pkliu on Thu Aug 03 11:22:57 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.orm.einv.entity;

import com.meshinnovation.db.model.BaseObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author pkliu
 */
@Entity
@Table(name = "public.user")
public class User extends BaseObject implements UserDetails {

//long serialVersionUID jdk tool: serialver.exe 

    /**
     * company_id
     */
    @Column(name = "company_id")
    protected java.lang.Long companyId;

    /**
     * auth_key
     */
    @Column(name = "auth_key")
    protected java.lang.String authKey;

    /**
     * default_tax_type
     */
    @Column(name = "default_tax_type")
    protected java.lang.String defaultTaxType;

    /**
     * default_b2b_flag
     */
    @Column(name = "default_b2b_flag")
    protected java.lang.String defaultB2bFlag;

    /**
     * password
     */
    @Column(name = "password")
    protected java.lang.String password;

    /**
     * user_id java.lang.Long , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    protected java.lang.Long userId;

    /**
     * role_id
     */
    @Column(name = "role_id")
    protected java.lang.Long roleId;

    /**
     * name
     */
    @Column(name = "name")
    protected java.lang.String name;

    /**
     * creator_id
     */
    @Column(name = "creator_id")
    protected java.lang.Long creatorId;

    /**
     * modifier_id
     */
    @Column(name = "modifier_id")
    protected java.lang.Long modifierId;

    /**
     * logout_time
     */
    @Column(name = "logout_time")
    protected java.lang.Long logoutTime;

    /**
     * printer_id
     */
    @Column(name = "printer_id")
    protected java.lang.Long printerId;

    @Column(name = "auth_url")
    protected java.lang.String authUrl;

    @Column(name = "create_date")
    protected LocalDateTime createDate;

    /**
     * close
     */
    @Column(name = "close")
    protected java.lang.Boolean close;

    @Column(name = "modify_date")
    protected LocalDateTime modifyDate;

    /**
     * account
     */
    @Column(name = "account")
    protected java.lang.String account;

    /**
     * email
     */
    @Column(name = "email")
    protected java.lang.String email;

    //@Column(name = "grantedAuthorities")
    @Transient
    protected GrantedAuthority[] grantedAuthorities = new GrantedAuthority[0];

    public GrantedAuthority[] getGrantedAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
        if (grantedAuthorities != null) {
            for (Integer i = 0; i < grantedAuthorities.length; i++) {
                result.add(grantedAuthorities[i]);
            }
        }

        if (getRoleId() != null) {
            GrantedAuthority gau = new SimpleGrantedAuthority(User.convRoleName(getRoleId()));
            result.add(gau);
        }

        return result;
    }

    public void setGrantedAuthorities(GrantedAuthority[] authorities) {
        this.grantedAuthorities = authorities;
    }


    public static String convRoleName(Long roleId) {
        Map<Long, String> roleMap = new HashMap<Long, String>();
        roleMap.put(0L, "ROLE_USER"); //一般使用者
        roleMap.put(100L, "ROLE_ADMIN");//管理者
        roleMap.put(200L, "ROLE_COMPANY_ADMIN"); //一般公司管理者
        roleMap.put(201L, "ROLE_COMPANY_ACCOUNTER");  //一般公司會計者
        roleMap.put(202L, "ROLE_COMPANY_USER"); //一般公司使用者
        roleMap.put(203L, "ROLE_COMPANY_EINV"); //一般公司開立發票人員
        roleMap.put(250L, "ROLE_COMPANY_LOTTERY"); //一般公司中獎清冊使用者
        roleMap.put(300L, "ROLE_FIRM_USER"); //會計公司使用者
        roleMap.put(400L, "ROLE_POS_USER"); //POS公司使用者
        roleMap.put(500L, "ROLE_SYNCAGENT_USER"); //一般介接公司使用者
        roleMap.put(550L, "ROLE_SYNCAGENT_LOTTERY"); //一般介接公司中獎清冊使用者
        roleMap.put(600L, "ROLE_ROOT"); //帳務管理者
        roleMap.put(1000L, "ROLE_STOP"); //停用
        roleMap.put(1001L, "ROLE_PASSWORD_ERROR"); //密碼錯誤
        return (String) roleMap.get(roleId);
    }

    /**
     * 002
     *
     * @return java.lang.Long companyId
     */
    public java.lang.Long getCompanyId() {
        return this.companyId;
    }

    /**
     * 0001
     *
     * @param data Set the companyId
     */
    public void setCompanyId(java.lang.Long data) {
        this.companyId = data;
    }

    /**
     * 002
     *
     * @return java.lang.String authKey
     */
    public java.lang.String getAuthKey() {
        return this.authKey;
    }

    /**
     * 0001
     *
     * @param data Set the authKey
     */
    public void setAuthKey(java.lang.String data) {
        this.authKey = data;
    }

    /**
     * 002
     *
     * @return java.lang.String defaultTaxType
     */
    public java.lang.String getDefaultTaxType() {
        return this.defaultTaxType;
    }

    /**
     * 0001
     *
     * @param data Set the defaultTaxType
     */
    public void setDefaultTaxType(java.lang.String data) {
        this.defaultTaxType = data;
    }

    /**
     * 002
     *
     * @return java.lang.String defaultB2bFlag
     */
    public java.lang.String getDefaultB2bFlag() {
        return this.defaultB2bFlag;
    }

    /**
     * 0001
     *
     * @param data Set the defaultB2bFlag
     */
    public void setDefaultB2bFlag(java.lang.String data) {
        this.defaultB2bFlag = data;
    }

    /**
     * 002
     *
     * @return java.lang.String password
     */
    public java.lang.String getPassword() {
        return this.password;
    }

    /**
     * 0001
     *
     * @param data Set the password
     */
    public void setPassword(java.lang.String data) {
        this.password = data;
    }

    /**
     * 002
     *
     * @return java.lang.Long userId
     */
    public java.lang.Long getUserId() {
        return this.userId;
    }

    /**
     * 0001
     *
     * @param data Set the userId
     */
    public void setUserId(java.lang.Long data) {
        this.userId = data;    //zzz
    }

    /**
     * 002
     *
     * @return java.lang.Long roleId
     */
    public java.lang.Long getRoleId() {
        return this.roleId;
    }

    /**
     * 0001
     *
     * @param data Set the roleId
     */
    public void setRoleId(java.lang.Long data) {
        this.roleId = data;
    }

    /**
     * 002
     *
     * @return java.lang.String name
     */
    public java.lang.String getName() {
        return this.name;
    }

    /**
     * 0001
     *
     * @param data Set the name
     */
    public void setName(java.lang.String data) {
        this.name = data;
    }

    /**
     * 002
     *
     * @return java.lang.Long creatorId
     */
    public java.lang.Long getCreatorId() {
        return this.creatorId;
    }

    /**
     * 0001
     *
     * @param data Set the creatorId
     */
    public void setCreatorId(java.lang.Long data) {
        this.creatorId = data;
    }

    /**
     * 002
     *
     * @return java.lang.Long modifierId
     */
    public java.lang.Long getModifierId() {
        return this.modifierId;
    }

    /**
     * 0001
     *
     * @param data Set the modifierId
     */
    public void setModifierId(java.lang.Long data) {
        this.modifierId = data;
    }

    /**
     * 002
     *
     * @return java.lang.Long logoutTime
     */
    public java.lang.Long getLogoutTime() {
        return this.logoutTime;
    }

    /**
     * 0001
     *
     * @param data Set the logoutTime
     */
    public void setLogoutTime(java.lang.Long data) {
        this.logoutTime = data;
    }

    /**
     * 002
     *
     * @return java.lang.Long printerId
     */
    public java.lang.Long getPrinterId() {
        return this.printerId;
    }

    /**
     * 0001
     *
     * @param data Set the printerId
     */
    public void setPrinterId(java.lang.Long data) {
        this.printerId = data;
    }

    /**
     * 002
     *
     * @return java.lang.String authUrl
     */
    public java.lang.String getAuthUrl() {
        return this.authUrl;
    }

    /**
     * 0001
     *
     * @param data Set the authUrl
     */
    public void setAuthUrl(java.lang.String data) {
        this.authUrl = data;
    }


    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * 002
     *
     * @return java.lang.Boolean close
     */
    public java.lang.Boolean getClose() {
        return this.close;
    }

    /**
     * 0001
     *
     * @param data Set the close
     */
    public void setClose(java.lang.Boolean data) {
        this.close = data;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * 002
     *
     * @return java.lang.String account
     */
    public java.lang.String getAccount() {
        return this.account;
    }

    /**
     * 0001
     *
     * @param data Set the account
     */
    public void setAccount(java.lang.String data) {
        this.account = data;
    }

    /**
     * 002
     *
     * @return java.lang.String email
     */
    public java.lang.String getEmail() {
        return this.email;
    }

    /**
     * 0001
     *
     * @param data Set the email
     */
    public void setEmail(java.lang.String data) {
        this.email = data;
    }


    /**
     *
     */
    public User() {
    }

    /**
     * full constructor
     *
     * @param companyId
     * @param authKey
     * @param defaultTaxType
     * @param defaultB2bFlag
     * @param password
     * @param userId
     * @param roleId
     * @param name
     * @param creatorId
     * @param modifierId
     * @param logoutTime
     * @param printerId
     * @param authUrl
     * @param createDate
     * @param close
     * @param modifyDate
     * @param account
     * @param email
     */
    public User(
            java.lang.Long companyId
            , java.lang.String authKey
            , java.lang.String defaultTaxType
            , java.lang.String defaultB2bFlag
            , java.lang.String password
            , java.lang.Long userId
            , java.lang.Long roleId
            , java.lang.String name
            , java.lang.Long creatorId
            , java.lang.Long modifierId
            , java.lang.Long logoutTime
            , java.lang.Long printerId
            , java.lang.String authUrl
            , LocalDateTime createDate
            , java.lang.Boolean close
            , LocalDateTime modifyDate
            , java.lang.String account
            , java.lang.String email
    ) {
        this.setCompanyId(companyId);
        this.setAuthKey(authKey);
        this.setDefaultTaxType(defaultTaxType);
        this.setDefaultB2bFlag(defaultB2bFlag);
        this.setPassword(password);
        this.setUserId(userId);
        this.setRoleId(roleId);
        this.setName(name);
        this.setCreatorId(creatorId);
        this.setModifierId(modifierId);
        this.setLogoutTime(logoutTime);
        this.setPrinterId(printerId);
        this.setAuthUrl(authUrl);
        this.setCreateDate(createDate);
        this.setClose(close);
        this.setModifyDate(modifyDate);
        this.setAccount(account);
        this.setEmail(email);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(companyId, authKey, defaultTaxType, defaultB2bFlag, password, userId, roleId, name, creatorId, modifierId, logoutTime, printerId, authUrl, createDate, close, modifyDate, account, email);
        result = 31 * result + Arrays.hashCode(grantedAuthorities);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "companyId=" + companyId +
                ", authKey='" + authKey + '\'' +
                ", defaultTaxType='" + defaultTaxType + '\'' +
                ", defaultB2bFlag='" + defaultB2bFlag + '\'' +
                ", password='" + password + '\'' +
                ", userId=" + userId +
                ", roleId=" + roleId +
                ", name='" + name + '\'' +
                ", creatorId=" + creatorId +
                ", modifierId=" + modifierId +
                ", logoutTime=" + logoutTime +
                ", printerId=" + printerId +
                ", authUrl='" + authUrl + '\'' +
                ", createDate=" + createDate +
                ", close=" + close +
                ", modifyDate=" + modifyDate +
                ", account='" + account + '\'' +
                ", email='" + email + '\'' +
                ", grantedAuthorities=" + Arrays.toString(grantedAuthorities) +
                '}';
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @return true is equal, false is not equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || !(obj instanceof User))
            return false;

        User key = (User) obj;
        if (this.userId != key.userId)
            return false;

        return true;
    }


    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
     */
    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
     */
    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
     */
    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

}