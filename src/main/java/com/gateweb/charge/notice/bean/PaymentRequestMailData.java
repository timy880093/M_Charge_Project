package com.gateweb.charge.notice.bean;

import com.gateweb.charge.enumeration.NoticeType;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.Company;

import java.util.Optional;
import java.util.Set;

public class PaymentRequestMailData {
    NoticeType noticeType;
    Bill bill;
    Company company;
    Optional<NoticeCustom> noticeCustomOpt;
    Set<String> recipient;

    public PaymentRequestMailData() {
    }

    public PaymentRequestMailData(NoticeType noticeType, Bill bill, Company company, Optional<NoticeCustom> noticeCustomOpt, Set<String> recipient) {
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

    public Set<String> getRecipient() {
        return recipient;
    }

    public void setRecipient(Set<String> recipient) {
        this.recipient = recipient;
    }
}
