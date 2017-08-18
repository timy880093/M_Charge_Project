package com.gate.web.displaybeans;


public class GiftVO {

    private Integer billId;
    private Integer companyId;
    private String companyName;
    private String calYM;
    private Integer cntGift;
    private boolean isCalculated;

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCalYM() {
        return calYM;
    }

    public void setCalYM(String calYM) {
        this.calYM = calYM;
    }

    public Integer getCntGift() {
        return cntGift;
    }

    public void setCntGift(Integer cntGift) {
        this.cntGift = cntGift;
    }

    public boolean isCalculated() {
        return isCalculated;
    }

    public void setIsCalculated(boolean isCalculated) {
        this.isCalculated = isCalculated;
    }
}
