package com.gateweb.charge.frontEndIntegration.bean;

import com.gateweb.charge.enumeration.BillStatus;

public class BillSearchCondition {
    Long companyId;
    BillStatus billStatus;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public BillStatus getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(BillStatus billStatus) {
        this.billStatus = billStatus;
    }
}
