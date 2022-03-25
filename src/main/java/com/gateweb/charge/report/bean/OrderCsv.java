package com.gateweb.charge.report.bean;

import java.math.BigDecimal;

/**
 * Created by Eason on 3/15/2018.
 * 用以代表Export Csv所有欄位的bean
 */
public class OrderCsv {
    Integer orderSequenceNumber;
    String orderNumber;
    String sellerIdentifier;
    String sellerName;
    String sellerAddress;
    String sellerPersonInCharge;
    String sellerTelephoneNumber;
    String sellerFacsimileNumber;
    String sellerEmailAddress;
    String sellerCustomerNumber;
    String sellerRoleRemark;
    String buyerIdentifier;
    String buyerName;
    String buyerAddress;
    String buyerPersonInCharge;
    String buyerTelephoneNumber;
    String buyerFacsimileNumber;
    String buyerEmailAddress;
    String buyerCustomerNumber;
    String buyerRoleRemark;
    String orderMan;
    String mainRemark;
    String orderType;
    String orderModifiable;
    BigDecimal salesAmount;
    BigDecimal freeTaxSalesAmount;
    BigDecimal zeroTaxSalesAmount;
    String taxType;
    Float taxRate;
    BigDecimal taxAmount;
    BigDecimal totalAmount;
    String relateNumber;
    String yearMonth;
    String invoiceType;
    String migType;
    String closeDate;
    String detailDescription;
    BigDecimal detailQuantity;
    BigDecimal detailAmount;
    Integer detailSequenceNumber;
    BigDecimal detailUnitPrice;

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getBuyerCustomerNumber() {
        return buyerCustomerNumber;
    }

    public void setBuyerCustomerNumber(String buyerCustomerNumber) {
        this.buyerCustomerNumber = buyerCustomerNumber;
    }

    public String getBuyerEmailAddress() {
        return buyerEmailAddress;
    }

    public void setBuyerEmailAddress(String buyerEmailAddress) {
        this.buyerEmailAddress = buyerEmailAddress;
    }

    public String getBuyerFacsimileNumber() {
        return buyerFacsimileNumber;
    }

    public void setBuyerFacsimileNumber(String buyerFacsimileNumber) {
        this.buyerFacsimileNumber = buyerFacsimileNumber;
    }

    public String getBuyerIdentifier() {
        return buyerIdentifier;
    }

    public void setBuyerIdentifier(String buyerIdentifier) {
        this.buyerIdentifier = buyerIdentifier;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerPersonInCharge() {
        return buyerPersonInCharge;
    }

    public void setBuyerPersonInCharge(String buyerPersonInCharge) {
        this.buyerPersonInCharge = buyerPersonInCharge;
    }

    public String getBuyerRoleRemark() {
        return buyerRoleRemark;
    }

    public void setBuyerRoleRemark(String buyerRoleRemark) {
        this.buyerRoleRemark = buyerRoleRemark;
    }

    public String getBuyerTelephoneNumber() {
        return buyerTelephoneNumber;
    }

    public void setBuyerTelephoneNumber(String buyerTelephoneNumber) {
        this.buyerTelephoneNumber = buyerTelephoneNumber;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public BigDecimal getDetailAmount() {
        return detailAmount;
    }

    public void setDetailAmount(BigDecimal detailAmount) {
        this.detailAmount = detailAmount;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public BigDecimal getDetailQuantity() {
        return detailQuantity;
    }

    public void setDetailQuantity(BigDecimal detailQuantity) {
        this.detailQuantity = detailQuantity;
    }

    public Integer getDetailSequenceNumber() {
        return detailSequenceNumber;
    }

    public void setDetailSequenceNumber(Integer detailSequenceNumber) {
        this.detailSequenceNumber = detailSequenceNumber;
    }

    public BigDecimal getDetailUnitPrice() {
        return detailUnitPrice;
    }

    public void setDetailUnitPrice(BigDecimal detailUnitPrice) {
        this.detailUnitPrice = detailUnitPrice;
    }

    public BigDecimal getFreeTaxSalesAmount() {
        return freeTaxSalesAmount;
    }

    public void setFreeTaxSalesAmount(BigDecimal freeTaxSalesAmount) {
        this.freeTaxSalesAmount = freeTaxSalesAmount;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getMainRemark() {
        return mainRemark;
    }

    public void setMainRemark(String mainRemark) {
        this.mainRemark = mainRemark;
    }

    public String getMigType() {
        return migType;
    }

    public void setMigType(String migType) {
        this.migType = migType;
    }

    public String getOrderMan() {
        return orderMan;
    }

    public void setOrderMan(String orderMan) {
        this.orderMan = orderMan;
    }

    public String getOrderModifiable() {
        return orderModifiable;
    }

    public void setOrderModifiable(String orderModifiable) {
        this.orderModifiable = orderModifiable;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getOrderSequenceNumber() {
        return orderSequenceNumber;
    }

    public void setOrderSequenceNumber(Integer orderSequenceNumber) {
        this.orderSequenceNumber = orderSequenceNumber;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getRelateNumber() {
        return relateNumber;
    }

    public void setRelateNumber(String relateNumber) {
        this.relateNumber = relateNumber;
    }

    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount) {
        this.salesAmount = salesAmount;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getSellerCustomerNumber() {
        return sellerCustomerNumber;
    }

    public void setSellerCustomerNumber(String sellerCustomerNumber) {
        this.sellerCustomerNumber = sellerCustomerNumber;
    }

    public String getSellerEmailAddress() {
        return sellerEmailAddress;
    }

    public void setSellerEmailAddress(String sellerEmailAddress) {
        this.sellerEmailAddress = sellerEmailAddress;
    }

    public String getSellerFacsimileNumber() {
        return sellerFacsimileNumber;
    }

    public void setSellerFacsimileNumber(String sellerFacsimileNumber) {
        this.sellerFacsimileNumber = sellerFacsimileNumber;
    }

    public String getSellerIdentifier() {
        return sellerIdentifier;
    }

    public void setSellerIdentifier(String sellerIdentifier) {
        this.sellerIdentifier = sellerIdentifier;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPersonInCharge() {
        return sellerPersonInCharge;
    }

    public void setSellerPersonInCharge(String sellerPersonInCharge) {
        this.sellerPersonInCharge = sellerPersonInCharge;
    }

    public String getSellerRoleRemark() {
        return sellerRoleRemark;
    }

    public void setSellerRoleRemark(String sellerRoleRemark) {
        this.sellerRoleRemark = sellerRoleRemark;
    }

    public String getSellerTelephoneNumber() {
        return sellerTelephoneNumber;
    }

    public void setSellerTelephoneNumber(String sellerTelephoneNumber) {
        this.sellerTelephoneNumber = sellerTelephoneNumber;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Float getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Float taxRate) {
        this.taxRate = taxRate;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public BigDecimal getZeroTaxSalesAmount() {
        return zeroTaxSalesAmount;
    }

    public void setZeroTaxSalesAmount(BigDecimal zeroTaxSalesAmount) {
        this.zeroTaxSalesAmount = zeroTaxSalesAmount;
    }
}
