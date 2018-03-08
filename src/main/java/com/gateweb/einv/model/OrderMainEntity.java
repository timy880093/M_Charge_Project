/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Feb 27 11:12:44 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.einv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pkliu
 *
 */
@Entity
@Table(name = "order_main")
public class OrderMainEntity {

//long serialVersionUID jdk tool: serialver.exe 

	/**
	 * seller
	 */
	@Column(name = "seller")
	protected String seller;

	/**
	 * tax_amount
	 */
	@Column(name = "tax_amount")
	protected java.math.BigDecimal taxAmount;

	/**
	 * close_date
	 */
	@Column(name = "close_date")
	protected String closeDate;

	/**
	 * discount_amount
	 */
	@Column(name = "discount_amount")
	protected java.math.BigDecimal discountAmount;

	/**
	 * main_remark
	 */
	@Column(name = "main_remark")
	protected String mainRemark;

	/**
	 * buyer_name
	 */
	@Column(name = "buyer_name")
	protected String buyerName;

	/**
	 * order_time
	 */
	@Column(name = "order_time")
	protected String orderTime;

	/**
	 * original_currency_amount
	 */
	@Column(name = "original_currency_amount")
	protected java.math.BigDecimal originalCurrencyAmount;

	/**
	 * order_modifiable
	 */
	@Column(name = "order_modifiable")
	protected String orderModifiable;

	/**
	 * tax_rate
	 */
	@Column(name = "tax_rate")
	protected Float taxRate;

	/**
	 * relate_number
	 */
	@Column(name = "relate_number")
	protected String relateNumber;

	/**
	 * carrier_id2
	 */
	@Column(name = "carrier_id2")
	protected String carrierId2;

	/**
	 * carrier_id1
	 */
	@Column(name = "carrier_id1")
	protected String carrierId1;

	/**
	 * order_status
	 */
	@Column(name = "order_status")
	protected String orderStatus;

	/**
	 * buyer_role_remark
	 */
	@Column(name = "buyer_role_remark")
	protected String buyerRoleRemark;

	/**
	 * allowance_count
	 */
	@Column(name = "allowance_count")
	protected Integer allowanceCount;

	/**
	 * npoban
	 */
	@Column(name = "npoban")
	protected String npoban;

	/**
	 * carrier_type
	 */
	@Column(name = "carrier_type")
	protected String carrierType;

	/**
	 * seller_name
	 */
	@Column(name = "seller_name")
	protected String sellerName;

	/**
	 * free_tax_sales_amount
	 */
	@Column(name = "free_tax_sales_amount")
	protected java.math.BigDecimal freeTaxSalesAmount;

	/**
	 * c_printer_id
	 */
	@Column(name = "c_printer_id")
	protected Integer cPrinterId;

    /**
     * id java.lang.Long , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	protected Long id;

	/**
	 * seller_facsimile_number
	 */
	@Column(name = "seller_facsimile_number")
	protected String sellerFacsimileNumber;

	/**
	 * create_date
	 */
	@Column(name = "create_date")
	protected java.sql.Timestamp createDate;

	/**
	 * donate_mark
	 */
	@Column(name = "donate_mark")
	protected String donateMark;

	/**
	 * order_type
	 */
	@Column(name = "order_type")
	protected String orderType;

	/**
	 * year_month
	 */
	@Column(name = "year_month")
	protected String yearMonth;

	/**
	 * seller_role_remark
	 */
	@Column(name = "seller_role_remark")
	protected String sellerRoleRemark;

	/**
	 * buyer_email_address
	 */
	@Column(name = "buyer_email_address")
	protected String buyerEmailAddress;

	/**
	 * exchange_rate
	 */
	@Column(name = "exchange_rate")
	protected Float exchangeRate;

	/**
	 * sales_amount
	 */
	@Column(name = "sales_amount")
	protected java.math.BigDecimal salesAmount;

	/**
	 * buyer_facsimile_number
	 */
	@Column(name = "buyer_facsimile_number")
	protected String buyerFacsimileNumber;

	/**
	 * mig_type
	 */
	@Column(name = "mig_type")
	protected String migType;

	/**
	 * buyer
	 */
	@Column(name = "buyer")
	protected String buyer;

	/**
	 * c_pos_remark2
	 */
	@Column(name = "c_pos_remark2")
	protected String cPosRemark2;

	/**
	 * c_pos_remark1
	 */
	@Column(name = "c_pos_remark1")
	protected String cPosRemark1;

	/**
	 * store_identifier
	 */
	@Column(name = "store_identifier")
	protected String storeIdentifier;

	/**
	 * tax_type
	 */
	@Column(name = "tax_type")
	protected String taxType;

	/**
	 * total_amount
	 */
	@Column(name = "total_amount")
	protected java.math.BigDecimal totalAmount;

	/**
	 * delivery_start_date
	 */
	@Column(name = "delivery_start_date")
	protected String deliveryStartDate;

	/**
	 * random_number
	 */
	@Column(name = "random_number")
	protected String randomNumber;

	/**
	 * creator_id
	 */
	@Column(name = "creator_id")
	protected Integer creatorId;

	/**
	 * customs_clearance_mark
	 */
	@Column(name = "customs_clearance_mark")
	protected String customsClearanceMark;

	/**
	 * seller_email_address
	 */
	@Column(name = "seller_email_address")
	protected String sellerEmailAddress;

	/**
	 * order_number
	 */
	@Column(name = "order_number")
	protected String orderNumber;

	/**
	 * order_man
	 */
	@Column(name = "order_man")
	protected String orderMan;

	/**
	 * buyer_person_in_charge
	 */
	@Column(name = "buyer_person_in_charge")
	protected String buyerPersonInCharge;

	/**
	 * store_address
	 */
	@Column(name = "store_address")
	protected String storeAddress;

	/**
	 * c_printer_no
	 */
	@Column(name = "c_printer_no")
	protected String cPrinterNo;

	/**
	 * zero_tax_sales_amount
	 */
	@Column(name = "zero_tax_sales_amount")
	protected java.math.BigDecimal zeroTaxSalesAmount;

	/**
	 * buyer_telephone_number
	 */
	@Column(name = "buyer_telephone_number")
	protected String buyerTelephoneNumber;

	/**
	 * delivery_end_date
	 */
	@Column(name = "delivery_end_date")
	protected String deliveryEndDate;

	/**
	 * buyer_customer_number
	 */
	@Column(name = "buyer_customer_number")
	protected String buyerCustomerNumber;

	/**
	 * seller_telephone_number
	 */
	@Column(name = "seller_telephone_number")
	protected String sellerTelephoneNumber;

	/**
	 * currency
	 */
	@Column(name = "currency")
	protected String currency;

	/**
	 * invoice_type
	 */
	@Column(name = "invoice_type")
	protected String invoiceType;

	/**
	 * print_mark
	 */
	@Column(name = "print_mark")
	protected String printMark;

	/**
	 * seller_person_in_charge
	 */
	@Column(name = "seller_person_in_charge")
	protected String sellerPersonInCharge;

	/**
	 * buyer_address
	 */
	@Column(name = "buyer_address")
	protected String buyerAddress;

	/**
	 * seller_address
	 */
	@Column(name = "seller_address")
	protected String sellerAddress;

	/**
	 * confirm_status
	 */
	@Column(name = "confirm_status")
	protected Integer confirmStatus;

	/**
	 * accept_status
	 */
	@Column(name = "accept_status")
	protected Integer acceptStatus;

	/**
	 * c_invoice_status
	 */
	@Column(name = "c_invoice_status")
	protected Integer cInvoiceStatus;

	/**
	 * order_date
	 */
	@Column(name = "order_date")
	protected String orderDate;

	/**
	 * modifier_id
	 */
	@Column(name = "modifier_id")
	protected Integer modifierId;

	/**
	 * seller_customer_number
	 */
	@Column(name = "seller_customer_number")
	protected String sellerCustomerNumber;

	/**
	 * modify_date
	 */
	@Column(name = "modify_date")
	protected java.sql.Timestamp modifyDate;

	/**
	 * upload_status
	 */
	@Column(name = "upload_status")
	protected String uploadStatus;

	/**
	 * 002
	 * @return java.lang.String seller
	 */
	public String getSeller() {
		return this.seller;
	}

	/** 0001
	 * @param data Set the seller
	 */
	public void setSeller(String data) {
		this.seller = data;
	}
	/**
	 * 002
	 * @return java.math.BigDecimal taxAmount
	 */
	public java.math.BigDecimal getTaxAmount() {
		return this.taxAmount;
	}

	/** 0001
	 * @param data Set the taxAmount
	 */
	public void setTaxAmount(java.math.BigDecimal data) {
		this.taxAmount = data;
	}
	/**
	 * 002
	 * @return java.lang.String closeDate
	 */
	public String getCloseDate() {
		return this.closeDate;
	}

	/** 0001
	 * @param data Set the closeDate
	 */
	public void setCloseDate(String data) {
		this.closeDate = data;
	}
	/**
	 * 002
	 * @return java.math.BigDecimal discountAmount
	 */
	public java.math.BigDecimal getDiscountAmount() {
		return this.discountAmount;
	}

	/** 0001
	 * @param data Set the discountAmount
	 */
	public void setDiscountAmount(java.math.BigDecimal data) {
		this.discountAmount = data;
	}
	/**
	 * 002
	 * @return java.lang.String mainRemark
	 */
	public String getMainRemark() {
		return this.mainRemark;
	}

	/** 0001
	 * @param data Set the mainRemark
	 */
	public void setMainRemark(String data) {
		this.mainRemark = data;
	}
	/**
	 * 002
	 * @return java.lang.String buyerName
	 */
	public String getBuyerName() {
		return this.buyerName;
	}

	/** 0001
	 * @param data Set the buyerName
	 */
	public void setBuyerName(String data) {
		this.buyerName = data;
	}
	/**
	 * 002
	 * @return java.lang.String orderTime
	 */
	public String getOrderTime() {
		return this.orderTime;
	}

	/** 0001
	 * @param data Set the orderTime
	 */
	public void setOrderTime(String data) {
		this.orderTime = data;
	}
	/**
	 * 002
	 * @return java.math.BigDecimal originalCurrencyAmount
	 */
	public java.math.BigDecimal getOriginalCurrencyAmount() {
		return this.originalCurrencyAmount;
	}

	/** 0001
	 * @param data Set the originalCurrencyAmount
	 */
	public void setOriginalCurrencyAmount(java.math.BigDecimal data) {
		this.originalCurrencyAmount = data;
	}
	/**
	 * 002
	 * @return java.lang.String orderModifiable
	 */
	public String getOrderModifiable() {
		return this.orderModifiable;
	}

	/** 0001
	 * @param data Set the orderModifiable
	 */
	public void setOrderModifiable(String data) {
		this.orderModifiable = data;
	}
	/**
	 * 002
	 * @return java.lang.Float taxRate
	 */
	public Float getTaxRate() {
		return this.taxRate;
	}

	/** 0001
	 * @param data Set the taxRate
	 */
	public void setTaxRate(Float data) {
		this.taxRate = data;
	}
	/**
	 * 002
	 * @return java.lang.String relateNumber
	 */
	public String getRelateNumber() {
		return this.relateNumber;
	}

	/** 0001
	 * @param data Set the relateNumber
	 */
	public void setRelateNumber(String data) {
		this.relateNumber = data;
	}
	/**
	 * 002
	 * @return java.lang.String carrierId2
	 */
	public String getCarrierId2() {
		return this.carrierId2;
	}

	/** 0001
	 * @param data Set the carrierId2
	 */
	public void setCarrierId2(String data) {
		this.carrierId2 = data;
	}
	/**
	 * 002
	 * @return java.lang.String carrierId1
	 */
	public String getCarrierId1() {
		return this.carrierId1;
	}

	/** 0001
	 * @param data Set the carrierId1
	 */
	public void setCarrierId1(String data) {
		this.carrierId1 = data;
	}
	/**
	 * 002
	 * @return java.lang.String orderStatus
	 */
	public String getOrderStatus() {
		return this.orderStatus;
	}

	/** 0001
	 * @param data Set the orderStatus
	 */
	public void setOrderStatus(String data) {
		this.orderStatus = data;
	}
	/**
	 * 002
	 * @return java.lang.String buyerRoleRemark
	 */
	public String getBuyerRoleRemark() {
		return this.buyerRoleRemark;
	}

	/** 0001
	 * @param data Set the buyerRoleRemark
	 */
	public void setBuyerRoleRemark(String data) {
		this.buyerRoleRemark = data;
	}
	/**
	 * 002
	 * @return java.lang.Integer allowanceCount
	 */
	public Integer getAllowanceCount() {
		return this.allowanceCount;
	}

	/** 0001
	 * @param data Set the allowanceCount
	 */
	public void setAllowanceCount(Integer data) {
		this.allowanceCount = data;
	}
	/**
	 * 002
	 * @return java.lang.String npoban
	 */
	public String getNpoban() {
		return this.npoban;
	}

	/** 0001
	 * @param data Set the npoban
	 */
	public void setNpoban(String data) {
		this.npoban = data;
	}
	/**
	 * 002
	 * @return java.lang.String carrierType
	 */
	public String getCarrierType() {
		return this.carrierType;
	}

	/** 0001
	 * @param data Set the carrierType
	 */
	public void setCarrierType(String data) {
		this.carrierType = data;
	}
	/**
	 * 002
	 * @return java.lang.String sellerName
	 */
	public String getSellerName() {
		return this.sellerName;
	}

	/** 0001
	 * @param data Set the sellerName
	 */
	public void setSellerName(String data) {
		this.sellerName = data;
	}
	/**
	 * 002
	 * @return java.math.BigDecimal freeTaxSalesAmount
	 */
	public java.math.BigDecimal getFreeTaxSalesAmount() {
		return this.freeTaxSalesAmount;
	}

	/** 0001
	 * @param data Set the freeTaxSalesAmount
	 */
	public void setFreeTaxSalesAmount(java.math.BigDecimal data) {
		this.freeTaxSalesAmount = data;
	}
	/**
	 * 002
	 * @return java.lang.Integer cPrinterId
	 */
	public Integer getCPrinterId() {
		return this.cPrinterId;
	}

	/** 0001
	 * @param data Set the cPrinterId
	 */
	public void setCPrinterId(Integer data) {
		this.cPrinterId = data;
	}
	/**
	 * 002
	 * @return java.lang.Long id
	 */
	public Long getId() {
		return this.id;
	}

	/** 0001
	 * @param data Set the id
	 */
	public void setId(Long data) {
		this.id = data;    //zzz
	}
	/**
	 * 002
	 * @return java.lang.String sellerFacsimileNumber
	 */
	public String getSellerFacsimileNumber() {
		return this.sellerFacsimileNumber;
	}

	/** 0001
	 * @param data Set the sellerFacsimileNumber
	 */
	public void setSellerFacsimileNumber(String data) {
		this.sellerFacsimileNumber = data;
	}
	/**
	 * 002
	 * @return java.sql.Timestamp createDate
	 */
	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}

	/** 0001
	 * @param data Set the createDate
	 */
	public void setCreateDate(java.sql.Timestamp data) {
		this.createDate = data;
	}
	/**
	 * 002
	 * @return java.lang.String donateMark
	 */
	public String getDonateMark() {
		return this.donateMark;
	}

	/** 0001
	 * @param data Set the donateMark
	 */
	public void setDonateMark(String data) {
		this.donateMark = data;
	}
	/**
	 * 002
	 * @return java.lang.String orderType
	 */
	public String getOrderType() {
		return this.orderType;
	}

	/** 0001
	 * @param data Set the orderType
	 */
	public void setOrderType(String data) {
		this.orderType = data;
	}
	/**
	 * 002
	 * @return java.lang.String yearMonth
	 */
	public String getYearMonth() {
		return this.yearMonth;
	}

	/** 0001
	 * @param data Set the yearMonth
	 */
	public void setYearMonth(String data) {
		this.yearMonth = data;
	}
	/**
	 * 002
	 * @return java.lang.String sellerRoleRemark
	 */
	public String getSellerRoleRemark() {
		return this.sellerRoleRemark;
	}

	/** 0001
	 * @param data Set the sellerRoleRemark
	 */
	public void setSellerRoleRemark(String data) {
		this.sellerRoleRemark = data;
	}
	/**
	 * 002
	 * @return java.lang.String buyerEmailAddress
	 */
	public String getBuyerEmailAddress() {
		return this.buyerEmailAddress;
	}

	/** 0001
	 * @param data Set the buyerEmailAddress
	 */
	public void setBuyerEmailAddress(String data) {
		this.buyerEmailAddress = data;
	}
	/**
	 * 002
	 * @return java.lang.Float exchangeRate
	 */
	public Float getExchangeRate() {
		return this.exchangeRate;
	}

	/** 0001
	 * @param data Set the exchangeRate
	 */
	public void setExchangeRate(Float data) {
		this.exchangeRate = data;
	}
	/**
	 * 002
	 * @return java.math.BigDecimal salesAmount
	 */
	public java.math.BigDecimal getSalesAmount() {
		return this.salesAmount;
	}

	/** 0001
	 * @param data Set the salesAmount
	 */
	public void setSalesAmount(java.math.BigDecimal data) {
		this.salesAmount = data;
	}
	/**
	 * 002
	 * @return java.lang.String buyerFacsimileNumber
	 */
	public String getBuyerFacsimileNumber() {
		return this.buyerFacsimileNumber;
	}

	/** 0001
	 * @param data Set the buyerFacsimileNumber
	 */
	public void setBuyerFacsimileNumber(String data) {
		this.buyerFacsimileNumber = data;
	}
	/**
	 * 002
	 * @return java.lang.String migType
	 */
	public String getMigType() {
		return this.migType;
	}

	/** 0001
	 * @param data Set the migType
	 */
	public void setMigType(String data) {
		this.migType = data;
	}
	/**
	 * 002
	 * @return java.lang.String buyer
	 */
	public String getBuyer() {
		return this.buyer;
	}

	/** 0001
	 * @param data Set the buyer
	 */
	public void setBuyer(String data) {
		this.buyer = data;
	}
	/**
	 * 002
	 * @return java.lang.String cPosRemark2
	 */
	public String getCPosRemark2() {
		return this.cPosRemark2;
	}

	/** 0001
	 * @param data Set the cPosRemark2
	 */
	public void setCPosRemark2(String data) {
		this.cPosRemark2 = data;
	}
	/**
	 * 002
	 * @return java.lang.String cPosRemark1
	 */
	public String getCPosRemark1() {
		return this.cPosRemark1;
	}

	/** 0001
	 * @param data Set the cPosRemark1
	 */
	public void setCPosRemark1(String data) {
		this.cPosRemark1 = data;
	}
	/**
	 * 002
	 * @return java.lang.String storeIdentifier
	 */
	public String getStoreIdentifier() {
		return this.storeIdentifier;
	}

	/** 0001
	 * @param data Set the storeIdentifier
	 */
	public void setStoreIdentifier(String data) {
		this.storeIdentifier = data;
	}
	/**
	 * 002
	 * @return java.lang.String taxType
	 */
	public String getTaxType() {
		return this.taxType;
	}

	/** 0001
	 * @param data Set the taxType
	 */
	public void setTaxType(String data) {
		this.taxType = data;
	}
	/**
	 * 002
	 * @return java.math.BigDecimal totalAmount
	 */
	public java.math.BigDecimal getTotalAmount() {
		return this.totalAmount;
	}

	/** 0001
	 * @param data Set the totalAmount
	 */
	public void setTotalAmount(java.math.BigDecimal data) {
		this.totalAmount = data;
	}
	/**
	 * 002
	 * @return java.lang.String deliveryStartDate
	 */
	public String getDeliveryStartDate() {
		return this.deliveryStartDate;
	}

	/** 0001
	 * @param data Set the deliveryStartDate
	 */
	public void setDeliveryStartDate(String data) {
		this.deliveryStartDate = data;
	}
	/**
	 * 002
	 * @return java.lang.String randomNumber
	 */
	public String getRandomNumber() {
		return this.randomNumber;
	}

	/** 0001
	 * @param data Set the randomNumber
	 */
	public void setRandomNumber(String data) {
		this.randomNumber = data;
	}
	/**
	 * 002
	 * @return java.lang.Integer creatorId
	 */
	public Integer getCreatorId() {
		return this.creatorId;
	}

	/** 0001
	 * @param data Set the creatorId
	 */
	public void setCreatorId(Integer data) {
		this.creatorId = data;
	}
	/**
	 * 002
	 * @return java.lang.String customsClearanceMark
	 */
	public String getCustomsClearanceMark() {
		return this.customsClearanceMark;
	}

	/** 0001
	 * @param data Set the customsClearanceMark
	 */
	public void setCustomsClearanceMark(String data) {
		this.customsClearanceMark = data;
	}
	/**
	 * 002
	 * @return java.lang.String sellerEmailAddress
	 */
	public String getSellerEmailAddress() {
		return this.sellerEmailAddress;
	}

	/** 0001
	 * @param data Set the sellerEmailAddress
	 */
	public void setSellerEmailAddress(String data) {
		this.sellerEmailAddress = data;
	}
	/**
	 * 002
	 * @return java.lang.String orderNumber
	 */
	public String getOrderNumber() {
		return this.orderNumber;
	}

	/** 0001
	 * @param data Set the orderNumber
	 */
	public void setOrderNumber(String data) {
		this.orderNumber = data;
	}
	/**
	 * 002
	 * @return java.lang.String orderMan
	 */
	public String getOrderMan() {
		return this.orderMan;
	}

	/** 0001
	 * @param data Set the orderMan
	 */
	public void setOrderMan(String data) {
		this.orderMan = data;
	}
	/**
	 * 002
	 * @return java.lang.String buyerPersonInCharge
	 */
	public String getBuyerPersonInCharge() {
		return this.buyerPersonInCharge;
	}

	/** 0001
	 * @param data Set the buyerPersonInCharge
	 */
	public void setBuyerPersonInCharge(String data) {
		this.buyerPersonInCharge = data;
	}
	/**
	 * 002
	 * @return java.lang.String storeAddress
	 */
	public String getStoreAddress() {
		return this.storeAddress;
	}

	/** 0001
	 * @param data Set the storeAddress
	 */
	public void setStoreAddress(String data) {
		this.storeAddress = data;
	}
	/**
	 * 002
	 * @return java.lang.String cPrinterNo
	 */
	public String getCPrinterNo() {
		return this.cPrinterNo;
	}

	/** 0001
	 * @param data Set the cPrinterNo
	 */
	public void setCPrinterNo(String data) {
		this.cPrinterNo = data;
	}
	/**
	 * 002
	 * @return java.math.BigDecimal zeroTaxSalesAmount
	 */
	public java.math.BigDecimal getZeroTaxSalesAmount() {
		return this.zeroTaxSalesAmount;
	}

	/** 0001
	 * @param data Set the zeroTaxSalesAmount
	 */
	public void setZeroTaxSalesAmount(java.math.BigDecimal data) {
		this.zeroTaxSalesAmount = data;
	}
	/**
	 * 002
	 * @return java.lang.String buyerTelephoneNumber
	 */
	public String getBuyerTelephoneNumber() {
		return this.buyerTelephoneNumber;
	}

	/** 0001
	 * @param data Set the buyerTelephoneNumber
	 */
	public void setBuyerTelephoneNumber(String data) {
		this.buyerTelephoneNumber = data;
	}
	/**
	 * 002
	 * @return java.lang.String deliveryEndDate
	 */
	public String getDeliveryEndDate() {
		return this.deliveryEndDate;
	}

	/** 0001
	 * @param data Set the deliveryEndDate
	 */
	public void setDeliveryEndDate(String data) {
		this.deliveryEndDate = data;
	}
	/**
	 * 002
	 * @return java.lang.String buyerCustomerNumber
	 */
	public String getBuyerCustomerNumber() {
		return this.buyerCustomerNumber;
	}

	/** 0001
	 * @param data Set the buyerCustomerNumber
	 */
	public void setBuyerCustomerNumber(String data) {
		this.buyerCustomerNumber = data;
	}
	/**
	 * 002
	 * @return java.lang.String sellerTelephoneNumber
	 */
	public String getSellerTelephoneNumber() {
		return this.sellerTelephoneNumber;
	}

	/** 0001
	 * @param data Set the sellerTelephoneNumber
	 */
	public void setSellerTelephoneNumber(String data) {
		this.sellerTelephoneNumber = data;
	}
	/**
	 * 002
	 * @return java.lang.String currency
	 */
	public String getCurrency() {
		return this.currency;
	}

	/** 0001
	 * @param data Set the currency
	 */
	public void setCurrency(String data) {
		this.currency = data;
	}
	/**
	 * 002
	 * @return java.lang.String invoiceType
	 */
	public String getInvoiceType() {
		return this.invoiceType;
	}

	/** 0001
	 * @param data Set the invoiceType
	 */
	public void setInvoiceType(String data) {
		this.invoiceType = data;
	}
	/**
	 * 002
	 * @return java.lang.String printMark
	 */
	public String getPrintMark() {
		return this.printMark;
	}

	/** 0001
	 * @param data Set the printMark
	 */
	public void setPrintMark(String data) {
		this.printMark = data;
	}
	/**
	 * 002
	 * @return java.lang.String sellerPersonInCharge
	 */
	public String getSellerPersonInCharge() {
		return this.sellerPersonInCharge;
	}

	/** 0001
	 * @param data Set the sellerPersonInCharge
	 */
	public void setSellerPersonInCharge(String data) {
		this.sellerPersonInCharge = data;
	}
	/**
	 * 002
	 * @return java.lang.String buyerAddress
	 */
	public String getBuyerAddress() {
		return this.buyerAddress;
	}

	/** 0001
	 * @param data Set the buyerAddress
	 */
	public void setBuyerAddress(String data) {
		this.buyerAddress = data;
	}
	/**
	 * 002
	 * @return java.lang.String sellerAddress
	 */
	public String getSellerAddress() {
		return this.sellerAddress;
	}

	/** 0001
	 * @param data Set the sellerAddress
	 */
	public void setSellerAddress(String data) {
		this.sellerAddress = data;
	}
	/**
	 * 002
	 * @return java.lang.Integer confirmStatus
	 */
	public Integer getConfirmStatus() {
		return this.confirmStatus;
	}

	/** 0001
	 * @param data Set the confirmStatus
	 */
	public void setConfirmStatus(Integer data) {
		this.confirmStatus = data;
	}
	/**
	 * 002
	 * @return java.lang.Integer acceptStatus
	 */
	public Integer getAcceptStatus() {
		return this.acceptStatus;
	}

	/** 0001
	 * @param data Set the acceptStatus
	 */
	public void setAcceptStatus(Integer data) {
		this.acceptStatus = data;
	}
	/**
	 * 002
	 * @return java.lang.Integer cInvoiceStatus
	 */
	public Integer getCInvoiceStatus() {
		return this.cInvoiceStatus;
	}

	/** 0001
	 * @param data Set the cInvoiceStatus
	 */
	public void setCInvoiceStatus(Integer data) {
		this.cInvoiceStatus = data;
	}
	/**
	 * 002
	 * @return java.lang.String orderDate
	 */
	public String getOrderDate() {
		return this.orderDate;
	}

	/** 0001
	 * @param data Set the orderDate
	 */
	public void setOrderDate(String data) {
		this.orderDate = data;
	}
	/**
	 * 002
	 * @return java.lang.Integer modifierId
	 */
	public Integer getModifierId() {
		return this.modifierId;
	}

	/** 0001
	 * @param data Set the modifierId
	 */
	public void setModifierId(Integer data) {
		this.modifierId = data;
	}
	/**
	 * 002
	 * @return java.lang.String sellerCustomerNumber
	 */
	public String getSellerCustomerNumber() {
		return this.sellerCustomerNumber;
	}

	/** 0001
	 * @param data Set the sellerCustomerNumber
	 */
	public void setSellerCustomerNumber(String data) {
		this.sellerCustomerNumber = data;
	}
	/**
	 * 002
	 * @return java.sql.Timestamp modifyDate
	 */
	public java.sql.Timestamp getModifyDate() {
		return this.modifyDate;
	}

	/** 0001
	 * @param data Set the modifyDate
	 */
	public void setModifyDate(java.sql.Timestamp data) {
		this.modifyDate = data;
	}
	/**
	 * 002
	 * @return java.lang.String uploadStatus
	 */
	public String getUploadStatus() {
		return this.uploadStatus;
	}

	/** 0001
	 * @param data Set the uploadStatus
	 */
	public void setUploadStatus(String data) {
		this.uploadStatus = data;
	}



	/**
	 *
	 */
	public OrderMainEntity(){
	}

	/**
	 * full constructor
	 * @param seller
	 * @param taxAmount
	 * @param closeDate
	 * @param discountAmount
	 * @param mainRemark
	 * @param buyerName
	 * @param orderTime
	 * @param originalCurrencyAmount
	 * @param orderModifiable
	 * @param taxRate
	 * @param relateNumber
	 * @param carrierId2
	 * @param carrierId1
	 * @param orderStatus
	 * @param buyerRoleRemark
	 * @param allowanceCount
	 * @param npoban
	 * @param carrierType
	 * @param sellerName
	 * @param freeTaxSalesAmount
	 * @param cPrinterId
	 * @param id
	 * @param sellerFacsimileNumber
	 * @param createDate
	 * @param donateMark
	 * @param orderType
	 * @param yearMonth
	 * @param sellerRoleRemark
	 * @param buyerEmailAddress
	 * @param exchangeRate
	 * @param salesAmount
	 * @param buyerFacsimileNumber
	 * @param migType
	 * @param buyer
	 * @param cPosRemark2
	 * @param cPosRemark1
	 * @param storeIdentifier
	 * @param taxType
	 * @param totalAmount
	 * @param deliveryStartDate
	 * @param randomNumber
	 * @param creatorId
	 * @param customsClearanceMark
	 * @param sellerEmailAddress
	 * @param orderNumber
	 * @param orderMan
	 * @param buyerPersonInCharge
	 * @param storeAddress
	 * @param cPrinterNo
	 * @param zeroTaxSalesAmount
	 * @param buyerTelephoneNumber
	 * @param deliveryEndDate
	 * @param buyerCustomerNumber
	 * @param sellerTelephoneNumber
	 * @param currency
	 * @param invoiceType
	 * @param printMark
	 * @param sellerPersonInCharge
	 * @param buyerAddress
	 * @param sellerAddress
	 * @param confirmStatus
	 * @param acceptStatus
	 * @param cInvoiceStatus
	 * @param orderDate
	 * @param modifierId
	 * @param sellerCustomerNumber
	 * @param modifyDate
	 * @param uploadStatus
	 */
	public OrderMainEntity (
		 String seller
		, java.math.BigDecimal taxAmount
		, String closeDate
		, java.math.BigDecimal discountAmount
		, String mainRemark
		, String buyerName
		, String orderTime
		, java.math.BigDecimal originalCurrencyAmount
		, String orderModifiable
		, Float taxRate
		, String relateNumber
		, String carrierId2
		, String carrierId1
		, String orderStatus
		, String buyerRoleRemark
		, Integer allowanceCount
		, String npoban
		, String carrierType
		, String sellerName
		, java.math.BigDecimal freeTaxSalesAmount
		, Integer cPrinterId
		, Long id
		, String sellerFacsimileNumber
		, java.sql.Timestamp createDate
		, String donateMark
		, String orderType
		, String yearMonth
		, String sellerRoleRemark
		, String buyerEmailAddress
		, Float exchangeRate
		, java.math.BigDecimal salesAmount
		, String buyerFacsimileNumber
		, String migType
		, String buyer
		, String cPosRemark2
		, String cPosRemark1
		, String storeIdentifier
		, String taxType
		, java.math.BigDecimal totalAmount
		, String deliveryStartDate
		, String randomNumber
		, Integer creatorId
		, String customsClearanceMark
		, String sellerEmailAddress
		, String orderNumber
		, String orderMan
		, String buyerPersonInCharge
		, String storeAddress
		, String cPrinterNo
		, java.math.BigDecimal zeroTaxSalesAmount
		, String buyerTelephoneNumber
		, String deliveryEndDate
		, String buyerCustomerNumber
		, String sellerTelephoneNumber
		, String currency
		, String invoiceType
		, String printMark
		, String sellerPersonInCharge
		, String buyerAddress
		, String sellerAddress
		, Integer confirmStatus
		, Integer acceptStatus
		, Integer cInvoiceStatus
		, String orderDate
		, Integer modifierId
		, String sellerCustomerNumber
		, java.sql.Timestamp modifyDate
		, String uploadStatus
        ) {
		this.setSeller(seller);
		this.setTaxAmount(taxAmount);
		this.setCloseDate(closeDate);
		this.setDiscountAmount(discountAmount);
		this.setMainRemark(mainRemark);
		this.setBuyerName(buyerName);
		this.setOrderTime(orderTime);
		this.setOriginalCurrencyAmount(originalCurrencyAmount);
		this.setOrderModifiable(orderModifiable);
		this.setTaxRate(taxRate);
		this.setRelateNumber(relateNumber);
		this.setCarrierId2(carrierId2);
		this.setCarrierId1(carrierId1);
		this.setOrderStatus(orderStatus);
		this.setBuyerRoleRemark(buyerRoleRemark);
		this.setAllowanceCount(allowanceCount);
		this.setNpoban(npoban);
		this.setCarrierType(carrierType);
		this.setSellerName(sellerName);
		this.setFreeTaxSalesAmount(freeTaxSalesAmount);
		this.setCPrinterId(cPrinterId);
		this.setId(id);
		this.setSellerFacsimileNumber(sellerFacsimileNumber);
		this.setCreateDate(createDate);
		this.setDonateMark(donateMark);
		this.setOrderType(orderType);
		this.setYearMonth(yearMonth);
		this.setSellerRoleRemark(sellerRoleRemark);
		this.setBuyerEmailAddress(buyerEmailAddress);
		this.setExchangeRate(exchangeRate);
		this.setSalesAmount(salesAmount);
		this.setBuyerFacsimileNumber(buyerFacsimileNumber);
		this.setMigType(migType);
		this.setBuyer(buyer);
		this.setCPosRemark2(cPosRemark2);
		this.setCPosRemark1(cPosRemark1);
		this.setStoreIdentifier(storeIdentifier);
		this.setTaxType(taxType);
		this.setTotalAmount(totalAmount);
		this.setDeliveryStartDate(deliveryStartDate);
		this.setRandomNumber(randomNumber);
		this.setCreatorId(creatorId);
		this.setCustomsClearanceMark(customsClearanceMark);
		this.setSellerEmailAddress(sellerEmailAddress);
		this.setOrderNumber(orderNumber);
		this.setOrderMan(orderMan);
		this.setBuyerPersonInCharge(buyerPersonInCharge);
		this.setStoreAddress(storeAddress);
		this.setCPrinterNo(cPrinterNo);
		this.setZeroTaxSalesAmount(zeroTaxSalesAmount);
		this.setBuyerTelephoneNumber(buyerTelephoneNumber);
		this.setDeliveryEndDate(deliveryEndDate);
		this.setBuyerCustomerNumber(buyerCustomerNumber);
		this.setSellerTelephoneNumber(sellerTelephoneNumber);
		this.setCurrency(currency);
		this.setInvoiceType(invoiceType);
		this.setPrintMark(printMark);
		this.setSellerPersonInCharge(sellerPersonInCharge);
		this.setBuyerAddress(buyerAddress);
		this.setSellerAddress(sellerAddress);
		this.setConfirmStatus(confirmStatus);
		this.setAcceptStatus(acceptStatus);
		this.setCInvoiceStatus(cInvoiceStatus);
		this.setOrderDate(orderDate);
		this.setModifierId(modifierId);
		this.setSellerCustomerNumber(sellerCustomerNumber);
		this.setModifyDate(modifyDate);
		this.setUploadStatus(uploadStatus);
    }

	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		 //return ToStringBuilder.reflectionToString(this , ToStringStyle.MULTI_LINE_STYLE);  
		 return new ToStringBuilder(this)
		.append("seller", this.seller)
		.append("taxAmount", this.taxAmount)
		.append("closeDate", this.closeDate)
		.append("discountAmount", this.discountAmount)
		.append("mainRemark", this.mainRemark)
		.append("buyerName", this.buyerName)
		.append("orderTime", this.orderTime)
		.append("originalCurrencyAmount", this.originalCurrencyAmount)
		.append("orderModifiable", this.orderModifiable)
		.append("taxRate", this.taxRate)
		.append("relateNumber", this.relateNumber)
		.append("carrierId2", this.carrierId2)
		.append("carrierId1", this.carrierId1)
		.append("orderStatus", this.orderStatus)
		.append("buyerRoleRemark", this.buyerRoleRemark)
		.append("allowanceCount", this.allowanceCount)
		.append("npoban", this.npoban)
		.append("carrierType", this.carrierType)
		.append("sellerName", this.sellerName)
		.append("freeTaxSalesAmount", this.freeTaxSalesAmount)
		.append("cPrinterId", this.cPrinterId)
		.append("id", this.id)
		.append("sellerFacsimileNumber", this.sellerFacsimileNumber)
		.append("createDate", this.createDate)
		.append("donateMark", this.donateMark)
		.append("orderType", this.orderType)
		.append("yearMonth", this.yearMonth)
		.append("sellerRoleRemark", this.sellerRoleRemark)
		.append("buyerEmailAddress", this.buyerEmailAddress)
		.append("exchangeRate", this.exchangeRate)
		.append("salesAmount", this.salesAmount)
		.append("buyerFacsimileNumber", this.buyerFacsimileNumber)
		.append("migType", this.migType)
		.append("buyer", this.buyer)
		.append("cPosRemark2", this.cPosRemark2)
		.append("cPosRemark1", this.cPosRemark1)
		.append("storeIdentifier", this.storeIdentifier)
		.append("taxType", this.taxType)
		.append("totalAmount", this.totalAmount)
		.append("deliveryStartDate", this.deliveryStartDate)
		.append("randomNumber", this.randomNumber)
		.append("creatorId", this.creatorId)
		.append("customsClearanceMark", this.customsClearanceMark)
		.append("sellerEmailAddress", this.sellerEmailAddress)
		.append("orderNumber", this.orderNumber)
		.append("orderMan", this.orderMan)
		.append("buyerPersonInCharge", this.buyerPersonInCharge)
		.append("storeAddress", this.storeAddress)
		.append("cPrinterNo", this.cPrinterNo)
		.append("zeroTaxSalesAmount", this.zeroTaxSalesAmount)
		.append("buyerTelephoneNumber", this.buyerTelephoneNumber)
		.append("deliveryEndDate", this.deliveryEndDate)
		.append("buyerCustomerNumber", this.buyerCustomerNumber)
		.append("sellerTelephoneNumber", this.sellerTelephoneNumber)
		.append("currency", this.currency)
		.append("invoiceType", this.invoiceType)
		.append("printMark", this.printMark)
		.append("sellerPersonInCharge", this.sellerPersonInCharge)
		.append("buyerAddress", this.buyerAddress)
		.append("sellerAddress", this.sellerAddress)
		.append("confirmStatus", this.confirmStatus)
		.append("acceptStatus", this.acceptStatus)
		.append("cInvoiceStatus", this.cInvoiceStatus)
		.append("orderDate", this.orderDate)
		.append("modifierId", this.modifierId)
		.append("sellerCustomerNumber", this.sellerCustomerNumber)
		.append("modifyDate", this.modifyDate)
		.append("uploadStatus", this.uploadStatus)
		.toString();
	}	 

	/*
    @Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof OrderMainEntity))
			return false;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(this.id, ((OrderMainEntity) object).id);
		return builder.isEquals();
	}*/
	
	/**
	 * Indicates whether some other object is "equal to" this one.
	 * @return true is equal, false is not equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null || !(obj instanceof OrderMainEntity))
			return false;

		OrderMainEntity key = (OrderMainEntity) obj;
		if (this.id != key.id ) 
        	return false;

		return true;
    }

}