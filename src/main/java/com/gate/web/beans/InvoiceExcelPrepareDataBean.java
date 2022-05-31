package com.gate.web.beans;

import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Company;

import java.util.ArrayList;
import java.util.List;

public class InvoiceExcelPrepareDataBean {
    Company company;
    Bill bill;
    List<BillingItem> billingItemList = new ArrayList<>();

    public InvoiceExcelPrepareDataBean() {
    }

    public InvoiceExcelPrepareDataBean(Company company, Bill bill, List<BillingItem> billingItemList) {
        this.company = company;
        this.bill = bill;
        this.billingItemList = billingItemList;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public List<BillingItem> getBillingItemList() {
        return billingItemList;
    }

    public void setBillingItemList(List<BillingItem> billingItemList) {
        this.billingItemList = billingItemList;
    }
}
