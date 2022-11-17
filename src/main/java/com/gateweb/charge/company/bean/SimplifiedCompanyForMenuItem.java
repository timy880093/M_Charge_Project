package com.gateweb.charge.company.bean;

public class SimplifiedCompanyForMenuItem {
    Long companyId;
    String name;
    String businessNo;
    
    public SimplifiedCompanyForMenuItem() {
    }

    public SimplifiedCompanyForMenuItem(Long companyId, String name, String businessNo) {
        this.companyId = companyId;
        this.name = name;
        this.businessNo = businessNo;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }
}
