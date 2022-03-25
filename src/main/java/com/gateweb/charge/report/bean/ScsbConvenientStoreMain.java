package com.gateweb.charge.report.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ScsbConvenientStoreMain {
    String companyName;
    String businessNo;
    BigDecimal taxInclusivePrice;
    List<ScsbConvenientStoreSlave> slaveList = new ArrayList<>();

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public List<ScsbConvenientStoreSlave> getSlaveList() {
        return slaveList;
    }

    public void setSlaveList(List<ScsbConvenientStoreSlave> slaveList) {
        this.slaveList = slaveList;
    }

    public BigDecimal getTaxInclusivePrice() {
        return taxInclusivePrice;
    }

    public void setTaxInclusivePrice(BigDecimal taxInclusivePrice) {
        this.taxInclusivePrice = taxInclusivePrice;
    }
}
