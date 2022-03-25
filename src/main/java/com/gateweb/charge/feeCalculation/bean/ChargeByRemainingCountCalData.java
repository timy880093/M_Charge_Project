package com.gateweb.charge.feeCalculation.bean;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChargeByRemainingCountCalData {
    Company company;
    LocalDateTime executionDateTime;
    List<CustomInterval> nextCalculateIntervalList = new ArrayList<>();
    Contract contract;
    InvoiceRemaining previousInvoiceRemaining;
    Boolean renewByChargeRemainingContract;

    public LocalDateTime getExecutionDateTime() {
        return executionDateTime;
    }

    public void setExecutionDateTime(LocalDateTime executionDateTime) {
        this.executionDateTime = executionDateTime;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public InvoiceRemaining getPreviousInvoiceRemaining() {
        return previousInvoiceRemaining;
    }

    public void setPreviousInvoiceRemaining(InvoiceRemaining previousInvoiceRemaining) {
        this.previousInvoiceRemaining = previousInvoiceRemaining;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Boolean getRenewByChargeRemainingContract() {
        return renewByChargeRemainingContract;
    }

    public void setRenewByChargeRemainingContract(Boolean renewByChargeRemainingContract) {
        this.renewByChargeRemainingContract = renewByChargeRemainingContract;
    }

    public List<CustomInterval> getNextCalculateIntervalList() {
        return nextCalculateIntervalList;
    }

    public void setNextCalculateIntervalList(List<CustomInterval> nextCalculateIntervalList) {
        this.nextCalculateIntervalList = nextCalculateIntervalList;
    }
}
