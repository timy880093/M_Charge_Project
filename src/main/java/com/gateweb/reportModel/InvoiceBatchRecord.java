package com.gateweb.reportModel;

import java.math.BigDecimal;

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

        if (invoiceSequence != null ? !invoiceSequence.equals(that.invoiceSequence) : that.invoiceSequence != null)
            return false;
        if (invoiceDate != null ? !invoiceDate.equals(that.invoiceDate) : that.invoiceDate != null) return false;
        if (productItemSequence != null ? !productItemSequence.equals(that.productItemSequence) : that.productItemSequence != null)
            return false;
        if (productItemDescription != null ? !productItemDescription.equals(that.productItemDescription) : that.productItemDescription != null)
            return false;
        if (productItemQuantity != null ? !productItemQuantity.equals(that.productItemQuantity) : that.productItemQuantity != null)
            return false;
        if (productItemUnitPrice != null ? !productItemUnitPrice.equals(that.productItemUnitPrice) : that.productItemUnitPrice != null)
            return false;
        if (taxType != null ? !taxType.equals(that.taxType) : that.taxType != null) return false;
        if (taxRate != null ? !taxRate.equals(that.taxRate) : that.taxRate != null) return false;
        if (customsRemark != null ? !customsRemark.equals(that.customsRemark) : that.customsRemark != null)
            return false;
        if (buyerIdentifier != null ? !buyerIdentifier.equals(that.buyerIdentifier) : that.buyerIdentifier != null)
            return false;
        if (printOrEmailRemark != null ? !printOrEmailRemark.equals(that.printOrEmailRemark) : that.printOrEmailRemark != null)
            return false;
        if (phoneCarrierId != null ? !phoneCarrierId.equals(that.phoneCarrierId) : that.phoneCarrierId != null)
            return false;
        if (citizenPersonalCertificate != null ? !citizenPersonalCertificate.equals(that.citizenPersonalCertificate) : that.citizenPersonalCertificate != null)
            return false;
        if (npoBan != null ? !npoBan.equals(that.npoBan) : that.npoBan != null) return false;
        if (customCarrierType != null ? !customCarrierType.equals(that.customCarrierType) : that.customCarrierType != null)
            return false;
        if (customCarrierId != null ? !customCarrierId.equals(that.customCarrierId) : that.customCarrierId != null)
            return false;
        if (itemNo != null ? !itemNo.equals(that.itemNo) : that.itemNo != null) return false;
        if (upc != null ? !upc.equals(that.upc) : that.upc != null) return false;
        if (invoiceRemark != null ? !invoiceRemark.equals(that.invoiceRemark) : that.invoiceRemark != null)
            return false;
        return productItemRemark != null ? productItemRemark.equals(that.productItemRemark) : that.productItemRemark == null;

    }

    @Override
    public int hashCode() {
        int result = invoiceSequence != null ? invoiceSequence.hashCode() : 0;
        result = 31 * result + (invoiceDate != null ? invoiceDate.hashCode() : 0);
        result = 31 * result + (productItemSequence != null ? productItemSequence.hashCode() : 0);
        result = 31 * result + (productItemDescription != null ? productItemDescription.hashCode() : 0);
        result = 31 * result + (productItemQuantity != null ? productItemQuantity.hashCode() : 0);
        result = 31 * result + (productItemUnitPrice != null ? productItemUnitPrice.hashCode() : 0);
        result = 31 * result + (taxType != null ? taxType.hashCode() : 0);
        result = 31 * result + (taxRate != null ? taxRate.hashCode() : 0);
        result = 31 * result + (customsRemark != null ? customsRemark.hashCode() : 0);
        result = 31 * result + (buyerIdentifier != null ? buyerIdentifier.hashCode() : 0);
        result = 31 * result + (printOrEmailRemark != null ? printOrEmailRemark.hashCode() : 0);
        result = 31 * result + (phoneCarrierId != null ? phoneCarrierId.hashCode() : 0);
        result = 31 * result + (citizenPersonalCertificate != null ? citizenPersonalCertificate.hashCode() : 0);
        result = 31 * result + (npoBan != null ? npoBan.hashCode() : 0);
        result = 31 * result + (customCarrierType != null ? customCarrierType.hashCode() : 0);
        result = 31 * result + (customCarrierId != null ? customCarrierId.hashCode() : 0);
        result = 31 * result + (itemNo != null ? itemNo.hashCode() : 0);
        result = 31 * result + (upc != null ? upc.hashCode() : 0);
        result = 31 * result + (invoiceRemark != null ? invoiceRemark.hashCode() : 0);
        result = 31 * result + (productItemRemark != null ? productItemRemark.hashCode() : 0);
        return result;
    }
}
