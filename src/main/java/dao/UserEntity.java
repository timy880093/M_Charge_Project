package dao;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/8/15
 * Time: 下午 4:41
 * To change this template use File | Settings | File Templates.
 */
public class UserEntity implements Serializable {
    public Integer userId;
    public Integer companyId;
    public Integer roleId;
    public String account;
    public String password;
    public String name;
    public String email;
    public Boolean close;
    public Integer creatorId;
    public Timestamp createDate;
    public Integer modifierId;
    public Timestamp modifyDate;
    public Integer printerId;
    public String authUrl;
    public String authKey;
    public Integer logoutTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Boolean getClose() {
        return close;
    }

    public void setClose(Boolean close) {
        this.close = close;
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

    public Integer getPrinterId() {
        return printerId;
    }

    public void setPrinterId(Integer printerId) {
        this.printerId = printerId;
    }

    public String getAuthUrl() {

        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public Integer getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Integer logoutTime) {
        this.logoutTime = logoutTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (account != null ? !account.equals(that.account) : that.account != null) return false;
        if (authKey != null ? !authKey.equals(that.authKey) : that.authKey != null) return false;
        if (authUrl != null ? !authUrl.equals(that.authUrl) : that.authUrl != null) return false;
        if (close != null ? !close.equals(that.close) : that.close != null) return false;
        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (printerId != null ? !printerId.equals(that.printerId) : that.printerId != null) return false;
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (close != null ? close.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (printerId != null ? printerId.hashCode() : 0);
        result = 31 * result + (authUrl != null ? authUrl.hashCode() : 0);
        result = 31 * result + (authKey != null ? authKey.hashCode() : 0);
        return result;
    }
}
