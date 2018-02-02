package com.gate.realms;

/**
 * Created by LOKI on 2015/03/20.
 */
public class LoginUser {
    private int userId;
    private int roleId;
    private String currentRoleName;
    private int companyId;
    private String name;
    private String email;
    private String companyName;
    private String referenceCompanyId;
    private int logout_time;
    private String defaultTaxType;
    private String defaultB2bFlag;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getCurrentRoleName() {
        return currentRoleName;
    }

    public void setCurrentRoleName(String currentRoleName) {
        this.currentRoleName = currentRoleName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getReferenceCompanyId() {
        return referenceCompanyId;
    }

    public void setReferenceCompanyId(String referenceCompanyId) {
        this.referenceCompanyId = referenceCompanyId;
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
}
