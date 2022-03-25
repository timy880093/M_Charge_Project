package com.gateweb.charge.notice.bean;

public class PaymentRequestMailGradeTableItem {
    String from;
    String to;
    String fixPrice;
    String unitPrice;

    public PaymentRequestMailGradeTableItem() {
    }

    public String getFixPrice() {
        return fixPrice;
    }

    public void setFixPrice(String fixPrice) {
        this.fixPrice = fixPrice;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "PaymentRequestMailGradeTableItem{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", fixPrice='" + fixPrice + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                '}';
    }
}
