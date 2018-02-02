package com.gate.web.authority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserInfo implements Serializable {

    private String userId;
    private String roleId;
    private String roleName;
    private String companyId;
    private String printerId;
    private String loginName;
    private String account;
    private String email;
    private String referenceCompanyId;

    public String referenceCompanyBusinessNo;

    public List<String> referenceCompanyIds;

    public List<String> referenceCompanyBusinessNos;

    private String companyName;
    private int logout_time;
    private String defaultTaxType;
    private String defaultB2bFlag;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReferenceCompanyId() {
        return referenceCompanyId;
    }

    public void setReferenceCompanyId(String referenceCompanyId) {
        this.referenceCompanyId = referenceCompanyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(int logout_time) {
        this.logout_time = logout_time;
    }

    public String getDefaultTaxType() {
        return defaultTaxType;
    }

    public void setDefaultTaxType(String defaultTaxType) {
        this.defaultTaxType = defaultTaxType;
    }

    public String getDefaultB2bFlag() {
        return defaultB2bFlag;
    }

    public void setDefaultB2bFlag(String defaultB2bFlag) {
        this.defaultB2bFlag = defaultB2bFlag;
    }

    /**
     * @return the referenceCompanyBusinessNo
     */
    public String getReferenceCompanyBusinessNo() {
        return referenceCompanyBusinessNo;
    }

    /**
     * @param referenceCompanyBusinessNo the referenceCompanyBusinessNo to set
     */
    public void setReferenceCompanyBusinessNo(String referenceCompanyBusinessNo) {
        this.referenceCompanyBusinessNo = referenceCompanyBusinessNo;
    }

    /**
     * @return the referenceCompanyIds
     */
    public List<String> getReferenceCompanyIds() {
        //Arrays.asList(referenceCompanyId.split("\\s*,\\s*"));    // zero whitespace comma
        if (referenceCompanyId != null) {
            return Arrays.asList(referenceCompanyId.split(","));
        }
        return new ArrayList<String>();
    }

    /**
     * @param referenceCompanyIds the referenceCompanyIds to set
     */
    public void setReferenceCompanyIds(List<String> referenceCompanyIds) {
        this.referenceCompanyIds = referenceCompanyIds;
    }

    /**
     * @return the referenceCompanyBusinessNos
     */
    public List<String> getReferenceCompanyBusinessNos() {
        if (referenceCompanyBusinessNo != null) {
            return Arrays.asList(referenceCompanyBusinessNo.split(","));
        }
        return new ArrayList<String>();
    }

    /**
     * @param referenceCompanyBusinessNos the referenceCompanyBusinessNos to set
     */
    public void setReferenceCompanyBusinessNos(List<String> referenceCompanyBusinessNos) {
        this.referenceCompanyBusinessNos = referenceCompanyBusinessNos;
    }

}
