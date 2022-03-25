package com.gateweb.charge.report.bean;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by Eason on 3/23/2018.
 */
public class InvoiceBatchRecord {
    Integer invoiceSequence;
    String invoiceDate;
    Integer productItemSequence;
    String productItemDescription;
    Integer productItemQuantity;
    BigDecimal productItemUnitPrice;
    Integer taxType;
    Double taxRate;
    String customsRemark;
    String buyerIdentifier;
    Integer printOrEmailRemark;
    String phoneCarrierId;
    String citizenPersonalCertificate;
    String npoBan;
    String customCarrierType;
    String customCarrierId;
    String itemNo;
    String upc;
    String invoiceRemark;
    String productItemRemark;

    public String getBuyerIdentifier() {
        return buyerIdentifier;
    }

    public void setBuyerIdentifier(String buyerIdentifier) {
        this.buyerIdentifier = buyerIdentifier;
    }

    public String getCitizenPersonalCertificate() {
        return citizenPersonalCertificate;
    }

    public void setCitizenPersonalCertificate(String citizenPersonalCertificate) {
        this.citizenPersonalCertificate = citizenPersonalCertificate;
    }

    public String getCustomCarrierId() {
        return customCarrierId;
    }

    public void setCustomCarrierId(String customCarrierId) {
        this.customCarrierId = customCarrierId;
    }

    public String getCustomCarrierType() {
        return customCarrierType;
    }

    public void setCustomCarrierType(String customCarrierType) {
        this.customCarrierType = customCarrierType;
    }

    public String getCustomsRemark() {
        return customsRemark;
    }

    public void setCustomsRemark(String customsRemark) {
        this.customsRemark = customsRemark;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceRemark() {
        return invoiceRemark;
    }

    public void setInvoiceRemark(String invoiceRemark) {
        this.invoiceRemark = invoiceRemark;
    }

    public Integer getInvoiceSequence() {
        return invoiceSequence;
    }

    public void setInvoiceSequence(Integer invoiceSequence) {
        this.invoiceSequence = invoiceSequence;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getNpoBan() {
        return npoBan;
    }

    public void setNpoBan(String npoBan) {
        this.npoBan = npoBan;
    }

    public String getPhoneCarrierId() {
        return phoneCarrierId;
    }

    public void setPhoneCarrierId(String phoneCarrierId) {
        this.phoneCarrierId = phoneCarrierId;
    }

    public Integer getPrintOrEmailRemark() {
        return printOrEmailRemark;
    }

    public void setPrintOrEmailRemark(Integer printOrEmailRemark) {
        this.printOrEmailRemark = printOrEmailRemark;
    }

    public String getProductItemDescription() {
        return productItemDescription;
    }

    public void setProductItemDescription(String productItemDescription) {
        this.productItemDescription = productItemDescription;
    }

    public Integer getProductItemQuantity() {
        return productItemQuantity;
    }

    public void setProductItemQuantity(Integer productItemQuantity) {
        this.productItemQuantity = productItemQuantity;
    }

    public String getProductItemRemark() {
        return productItemRemark;
    }

    public void setProductItemRemark(String productItemRemark) {
        this.productItemRemark = productItemRemark;
    }

    public Integer getProductItemSequence() {
        return productItemSequence;
    }

    public void setProductItemSequence(Integer productItemSequence) {
        this.productItemSequence = productItemSequence;
    }

    public BigDecimal getProductItemUnitPrice() {
        return productItemUnitPrice;
    }

    public void setProductItemUnitPrice(BigDecimal productItemUnitPrice) {
        this.productItemUnitPrice = productItemUnitPrice;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getTaxType() {
        return taxType;
    }

    public void setTaxType(Integer taxType) {
        this.taxType = taxType;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceBatchRecord that = (InvoiceBatchRecord) o;
        return Objects.equals(invoiceSequence, that.invoiceSequence) && Objects.equals(invoiceDate, that.invoiceDate) && Objects.equals(productItemSequence, that.productItemSequence) && Objects.equals(productItemDescription, that.productItemDescription) && Objects.equals(productItemQuantity, that.productItemQuantity) && Objects.equals(productItemUnitPrice, that.productItemUnitPrice) && Objects.equals(taxType, that.taxType) && Objects.equals(taxRate, that.taxRate) && Objects.equals(customsRemark, that.customsRemark) && Objects.equals(buyerIdentifier, that.buyerIdentifier) && Objects.equals(printOrEmailRemark, that.printOrEmailRemark) && Objects.equals(phoneCarrierId, that.phoneCarrierId) && Objects.equals(citizenPersonalCertificate, that.citizenPersonalCertificate) && Objects.equals(npoBan, that.npoBan) && Objects.equals(customCarrierType, that.customCarrierType) && Objects.equals(customCarrierId, that.customCarrierId) && Objects.equals(itemNo, that.itemNo) && Objects.equals(upc, that.upc) && Objects.equals(invoiceRemark, that.invoiceRemark) && Objects.equals(productItemRemark, that.productItemRemark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceSequence, invoiceDate, productItemSequence, productItemDescription, productItemQuantity, productItemUnitPrice, taxType, taxRate, customsRemark, buyerIdentifier, printOrEmailRemark, phoneCarrierId, citizenPersonalCertificate, npoBan, customCarrierType, customCarrierId, itemNo, upc, invoiceRemark, productItemRemark);
    }
}
