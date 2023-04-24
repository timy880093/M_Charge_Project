package com.gateweb.charge.feeCalculation.bean;

import com.gateweb.charge.chargePolicy.bean.ChargePolicy;
import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ContractRentalFeeBillingData {
    Long companyId;
    Long contractId;
    String businessNo;
    Long packageRefId;
    CustomInterval contractInterval;
    LocalDateTime expectedOutDate;
    List<CustomInterval> calculateIntervalList = new ArrayList<>();
    ChargePolicy chargePolicy;

    public ContractRentalFeeBillingData() {
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getPackageRefId() {
        return packageRefId;
    }

    public void setPackageRefId(Long packageRefId) {
        this.packageRefId = packageRefId;
    }

    public CustomInterval getContractInterval() {
        return contractInterval;
    }

    public void setContractInterval(CustomInterval contractInterval) {
        this.contractInterval = contractInterval;
    }

    public List<CustomInterval> getCalculateIntervalList() {
        return calculateIntervalList;
    }

    public void setCalculateIntervalList(List<CustomInterval> calculateIntervalList) {
        this.calculateIntervalList = calculateIntervalList;
    }

    public ChargePolicy getChargePolicy() {
        return chargePolicy;
    }

    public void setChargePolicy(ChargePolicy chargePolicy) {
        this.chargePolicy = chargePolicy;
    }

    public LocalDateTime getExpectedOutDate() {
        return expectedOutDate;
    }

    public void setExpectedOutDate(LocalDateTime expectedOutDate) {
        this.expectedOutDate = expectedOutDate;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    @Override
    public String toString() {
        return "ContractRentalFeeBillingData{" +
                "companyId=" + companyId +
                ", contractId=" + contractId +
                ", businessNo='" + businessNo + '\'' +
                ", packageRefId=" + packageRefId +
                ", contractInterval=" + contractInterval +
                ", expectedOutDate=" + expectedOutDate +
                ", calculateIntervalList=" + calculateIntervalList +
                ", chargePolicy=" + chargePolicy +
                '}';
    }
}
