package com.gateweb.charge.report.p2bIasrReport.bean;

public class P2bIasrReportGstaticBean {
    String month;
    Double total;
    Double median;
    Double avgPre3Month;
    Double avgPre12Month;
    Double m2m;
    Double q2q;
    Double avg3MinusAvg12;

    public P2bIasrReportGstaticBean() {
    }

    public P2bIasrReportGstaticBean(String month, Double total, Double median, Double avgPre3Month, Double avgPre12Month, Double m2m, Double q2q, Double avg3MinusAvg12) {
        this.month = month;
        this.total = total;
        this.median = median;
        this.avgPre3Month = avgPre3Month;
        this.avgPre12Month = avgPre12Month;
        this.m2m = m2m;
        this.q2q = q2q;
        this.avg3MinusAvg12 = avg3MinusAvg12;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getMedian() {
        return median;
    }

    public void setMedian(Double median) {
        this.median = median;
    }

    public Double getAvgPre3Month() {
        return avgPre3Month;
    }

    public void setAvgPre3Month(Double avgPre3Month) {
        this.avgPre3Month = avgPre3Month;
    }

    public Double getAvgPre12Month() {
        return avgPre12Month;
    }

    public void setAvgPre12Month(Double avgPre12Month) {
        this.avgPre12Month = avgPre12Month;
    }

    public Double getM2m() {
        return m2m;
    }

    public void setM2m(Double m2m) {
        this.m2m = m2m;
    }

    public Double getQ2q() {
        return q2q;
    }

    public void setQ2q(Double q2q) {
        this.q2q = q2q;
    }

    public Double getAvg3MinusAvg12() {
        return avg3MinusAvg12;
    }

    public void setAvg3MinusAvg12(Double avg3MinusAvg12) {
        this.avg3MinusAvg12 = avg3MinusAvg12;
    }
}
