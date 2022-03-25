package com.gateweb.charge.notice.bean;

import com.gateweb.charge.enumeration.NoticeType;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.Company;

import java.util.Optional;

public class PaymentRequestMailData {
    NoticeType noticeType;
    Bill bill;
    Company company;
    Optional<NoticeCustom> noticeCustomOpt;
    String recipient;

    public PaymentRequestMailData() {
    }

    public PaymentRequestMailData(NoticeType noticeType, Bill bill, Company company, Optional<NoticeCustom> noticeCustomOpt, String recipient) {
        this.noticeType = noticeType;
        this.bill = bill;
        this.company = company;
        this.noticeCustomOpt = noticeCustomOpt;
        this.recipient = recipient;
    }

    public NoticeType getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(NoticeType noticeType) {
        this.noticeType = noticeType;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Optional<NoticeCustom> getNoticeCustomOpt() {
        return noticeCustomOpt;
    }

    public void setNoticeCustomOpt(Optional<NoticeCustom> noticeCustomOpt) {
        this.noticeCustomOpt = noticeCustomOpt;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "PaymentRequestMailData{" +
                "noticeType=" + noticeType +
                ", bill=" + bill +
                ", company=" + company +
                ", noticeCustomOpt=" + noticeCustomOpt +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}
