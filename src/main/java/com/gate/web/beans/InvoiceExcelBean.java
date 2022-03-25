package com.gate.web.beans;

import java.math.BigDecimal;

/**
 * Created by emily on 2016/2/3.
 */
public class InvoiceExcelBean {

    private Integer invoiceIndex; //發票張數
    private String invoiceDate; //發票日期
    private Integer itemIndex; //品名序號
    private String itemName; //發票品名
    private Integer itemCnt; //數量
    private BigDecimal unitPrice; //單價
    private Integer taxType; //課稅別 1.應稅 2.零稅率 3.免稅 4.應稅(特種稅率)
    private Double taxRate; //稅率
    private String businessNo; //買方統編

    public Integer getInvoiceIndex() {
        return invoiceIndex;
    }

    public void setInvoiceIndex(Integer invoiceIndex) {
        this.invoiceIndex = invoiceIndex;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Integer getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(Integer itemIndex) {
        this.itemIndex = itemIndex;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemCnt() {
        return itemCnt;
    }

    public void setItemCnt(Integer itemCnt) {
        this.itemCnt = itemCnt;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getTaxType() {
        return taxType;
    }

    public void setTaxType(Integer taxType) {
        this.taxType = taxType;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }
}
