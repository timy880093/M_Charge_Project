package com.gate.web.formbeans;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/9/17
 * Time: 上午 10:33
 * To change this template use File | Settings | File Templates.
 */
public class ChargeModeSingleBean {
    private String chargeId;
    private String packageName;
    private String baseQuantity;
    private String salesPrice;
    private String maxPrice;
    private String availableStart;
    private String availableEnd;
    private String status;
    private String creatorId;
    private String modifierId;
    private Double pointPrice;
    private Integer contractLimit;

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getBaseQuantity() {
        return baseQuantity;
    }

    public void setBaseQuantity(String baseQuantity) {
        this.baseQuantity = baseQuantity;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getAvailableStart() {
        return availableStart;
    }

    public void setAvailableStart(String availableStart) {
        this.availableStart = availableStart;
    }

    public String getAvailableEnd() {
        return availableEnd;
    }

    public void setAvailableEnd(String availableEnd) {
        this.availableEnd = availableEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    public Double getPointPrice(){
        return pointPrice;
    }

    public void setPointPrice(Double pointPrice){
        this.pointPrice = pointPrice;
    }

    public Integer getContractLimit(){
        return contractLimit;
    }

    public void setContractLimit(Integer contractLimit){
        this.contractLimit = contractLimit;
    }
}
