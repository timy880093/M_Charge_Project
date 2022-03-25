package com.gateweb.charge.notice.bean;

import java.util.ArrayList;
import java.util.List;

public class PaymentRequestMailGradeTable {
    String name;
    String maximumCharge;
    String accumulateAnnouncement;
    List<PaymentRequestMailGradeTableItem> paymentRequestMailGradeTableItemList = new ArrayList<>();

    public PaymentRequestMailGradeTable() {
    }

    public PaymentRequestMailGradeTable(String name, String maximumCharge, String accumulateAnnouncement, List<PaymentRequestMailGradeTableItem> paymentRequestMailGradeTableItemList) {
        this.name = name;
        this.maximumCharge = maximumCharge;
        this.accumulateAnnouncement = accumulateAnnouncement;
        this.paymentRequestMailGradeTableItemList = paymentRequestMailGradeTableItemList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaximumCharge() {
        return maximumCharge;
    }

    public void setMaximumCharge(String maximumCharge) {
        this.maximumCharge = maximumCharge;
    }

    public List<PaymentRequestMailGradeTableItem> getPaymentRequestMailGradeTableItemList() {
        return paymentRequestMailGradeTableItemList;
    }

    public void setPaymentRequestMailGradeTableItemList(List<PaymentRequestMailGradeTableItem> paymentRequestMailGradeTableItemList) {
        this.paymentRequestMailGradeTableItemList = paymentRequestMailGradeTableItemList;
    }

    public String getAccumulateAnnouncement() {
        return accumulateAnnouncement;
    }

    public void setAccumulateAnnouncement(String accumulateAnnouncement) {
        this.accumulateAnnouncement = accumulateAnnouncement;
    }

    @Override
    public String toString() {
        return "PaymentRequestMailGradeTable{" +
                "name='" + name + '\'' +
                ", maximumCharge='" + maximumCharge + '\'' +
                ", accumulateAnnouncement='" + accumulateAnnouncement + '\'' +
                ", paymentRequestMailGradeTableItemList=" + paymentRequestMailGradeTableItemList +
                '}';
    }
}
