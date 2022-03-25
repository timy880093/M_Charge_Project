package com.gateweb.charge.model.nonMapped;

import com.gateweb.orm.charge.entity.ChargeUser;
import com.gateweb.orm.charge.entity.Company;

import java.time.LocalDateTime;

public class CallerInfo {
    Company company;
    ChargeUser userEntity;
    LocalDateTime currentLocalDateTime = LocalDateTime.now();

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ChargeUser getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(ChargeUser userEntity) {
        this.userEntity = userEntity;
    }

    public LocalDateTime getCurrentLocalDateTime() {
        return currentLocalDateTime;
    }

    public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
        this.currentLocalDateTime = currentLocalDateTime;
    }
}
