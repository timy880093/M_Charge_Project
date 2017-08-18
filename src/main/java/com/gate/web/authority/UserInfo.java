package com.gate.web.authority;

import java.io.Serializable;

public class UserInfo implements Serializable{

    private String userId;
    private String roleId;
    private String roleName;
    private String companyId;
    private String loginName;
    private String email;
    private String referenceCompanyId;
    private String companyName;
    private int logout_time;

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

}
