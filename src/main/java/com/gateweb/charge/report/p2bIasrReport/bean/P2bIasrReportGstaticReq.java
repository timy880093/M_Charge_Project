package com.gateweb.charge.report.p2bIasrReport.bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class P2bIasrReportGstaticReq {
    String sellerIdentifier;
    LocalDateTime startLocalDateTime;
    LocalDateTime endLocalDateTime;
    List<String> monthStrList = new ArrayList<>();

    public P2bIasrReportGstaticReq() {
    }

    public P2bIasrReportGstaticReq(String sellerIdentifier, LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime, List<String> monthStrList) {
        this.sellerIdentifier = sellerIdentifier;
        this.startLocalDateTime = startLocalDateTime;
        this.endLocalDateTime = endLocalDateTime;
        this.monthStrList = monthStrList;
    }

    public String getSellerIdentifier() {
        return sellerIdentifier;
    }

    public void setSellerIdentifier(String sellerIdentifier) {
        this.sellerIdentifier = sellerIdentifier;
    }

    public LocalDateTime getStartLocalDateTime() {
        return startLocalDateTime;
    }

    public void setStartLocalDateTime(LocalDateTime startLocalDateTime) {
        this.startLocalDateTime = startLocalDateTime;
    }

    public LocalDateTime getEndLocalDateTime() {
        return endLocalDateTime;
    }

    public void setEndLocalDateTime(LocalDateTime endLocalDateTime) {
        this.endLocalDateTime = endLocalDateTime;
    }

    public List<String> getMonthStrList() {
        return monthStrList;
    }

    public void setMonthStrList(List<String> monthStrList) {
        this.monthStrList = monthStrList;
    }

    @Override
    public String toString() {
        return "P2bIasrReportGstaticReq{" +
                "sellerIdentifier='" + sellerIdentifier + '\'' +
                ", startLocalDateTime=" + startLocalDateTime +
                ", endLocalDateTime=" + endLocalDateTime +
                ", monthStrList=" + monthStrList +
                '}';
    }
}
