package com.gate.web.formbeans;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/10/3
 * Time: 上午 9:10
 * To change this template use File | Settings | File Templates.
 */
public class CompanyChargeSingleBean {
    private Integer companyId;
    private Integer chargeId;
    private Integer additionQuantity;
    private String buyDate;
    private Integer brokerId;
    private Integer brokerCompany;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public Integer getAdditionQuantity() {
        return additionQuantity;
    }

    public void setAdditionQuantity(Integer additionQuantity) {
        this.additionQuantity = additionQuantity;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public Integer getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(Integer brokerId) {
        this.brokerId = brokerId;
    }

    public Integer getBrokerCompany() {
        return brokerCompany;
    }

    public void setBrokerCompany(Integer brokerCompany) {
        this.brokerCompany = brokerCompany;
    }

    @Override
    public String toString() {
        return "CompanyChargeSingleBean{" +
                "companyId=" + companyId +
                ", chargeId=" + chargeId +
                ", additionQuantity=" + additionQuantity +
                ", buyDate='" + buyDate + '\'' +
                ", brokerId=" + brokerId +
                ", brokerCompany=" + brokerCompany +
                '}';
    }
}
