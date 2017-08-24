/*
 * $Header: $
 * This java source file is generated by pkliu on Thu Aug 03 11:22:57 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.AUTO;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.meshinnovation.db.model.BaseObject;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * @author pkliu
 *
 */
@Entity
@Table(name = "invoice_details")
public class InvoiceDetails extends BaseObject{

//long serialVersionUID jdk tool: serialver.exe 

	/**
	 * c_remark1
	 */
	@Column(name = "c_remark1")
	protected java.lang.String cRemark1;
	
	/**
	 * c_remark2
	 */
	@Column(name = "c_remark2")
	protected java.lang.String cRemark2;
	
	/**
	 * description
	 */
	@Column(name = "description")
	protected java.lang.String description;
	
	/**
	 * not_tax_inclusive_price
	 */
	@Column(name = "not_tax_inclusive_price")
	protected java.math.BigDecimal notTaxInclusivePrice;
	
	/**
	 * remark
	 */
	@Column(name = "remark")
	protected java.lang.String remark;
	
	/**
	 * tax_rate
	 */
	@Column(name = "tax_rate")
	protected java.lang.Float taxRate;
	
	/**
	 * relate_number
	 */
	@Column(name = "relate_number")
	protected java.lang.String relateNumber;
	
	/**
	 * item_no
	 */
	@Column(name = "item_no")
	protected java.lang.String itemNo;
	
    /**
     * invoice_details_id java.lang.Long , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_details_id", unique = true, nullable = false)
	protected java.lang.Long invoiceDetailsId;
	
	/**
	 * create_date
	 */
	@Column(name = "create_date")
	protected java.sql.Timestamp createDate;
	
	/**
	 * invoice_number
	 */
	@Column(name = "invoice_number")
	protected java.lang.String invoiceNumber;
	
	/**
	 * barcode
	 */
	@Column(name = "barcode")
	protected java.lang.String barcode;
	
	/**
	 * allowance_tax
	 */
	@Column(name = "allowance_tax")
	protected java.lang.Long allowanceTax;
	
	/**
	 * amount
	 */
	@Column(name = "amount")
	protected java.math.BigDecimal amount;
	
	/**
	 * quantity
	 */
	@Column(name = "quantity")
	protected java.math.BigDecimal quantity;
	
	/**
	 * allowance_amount
	 */
	@Column(name = "allowance_amount")
	protected java.lang.Long allowanceAmount;
	
	/**
	 * c_year_month
	 */
	@Column(name = "c_year_month")
	protected java.lang.String cYearMonth;
	
	/**
	 * unit_price
	 */
	@Column(name = "unit_price")
	protected java.math.BigDecimal unitPrice;
	
	/**
	 * buyer
	 */
	@Column(name = "buyer")
	protected java.lang.String buyer;
	
	/**
	 * sequence_number
	 */
	@Column(name = "sequence_number")
	protected java.lang.Long sequenceNumber;
	
	/**
	 * unit
	 */
	@Column(name = "unit")
	protected java.lang.String unit;
	
	/**
	 * tax_type
	 */
	@Column(name = "tax_type")
	protected java.lang.String taxType;
	
	/**
	 * creator_id
	 */
	@Column(name = "creator_id")
	protected java.lang.Long creatorId;
	
	/**
	 * modifier_id
	 */
	@Column(name = "modifier_id")
	protected java.lang.Long modifierId;
	
	/**
	 * allowance_quantity
	 */
	@Column(name = "allowance_quantity")
	protected java.lang.Long allowanceQuantity;
	
	/**
	 * modify_date
	 */
	@Column(name = "modify_date")
	protected java.sql.Timestamp modifyDate;
	



    
	/**
	 * 002
	 * @return java.lang.String cRemark1
	 */
	public java.lang.String getCRemark1() {
		return this.cRemark1;
	}
	
	/** 0001
	 * @param data Set the cRemark1
	 */	
	public void setCRemark1(java.lang.String data) {
		this.cRemark1 = data;
	}	
	/**
	 * 002
	 * @return java.lang.String cRemark2
	 */
	public java.lang.String getCRemark2() {
		return this.cRemark2;
	}
	
	/** 0001
	 * @param data Set the cRemark2
	 */	
	public void setCRemark2(java.lang.String data) {
		this.cRemark2 = data;
	}	
	/**
	 * 002
	 * @return java.lang.String description
	 */
	public java.lang.String getDescription() {
		return this.description;
	}
	
	/** 0001
	 * @param data Set the description
	 */	
	public void setDescription(java.lang.String data) {
		this.description = data;
	}	
	/**
	 * 002
	 * @return java.math.BigDecimal notTaxInclusivePrice
	 */
	public java.math.BigDecimal getNotTaxInclusivePrice() {
		return this.notTaxInclusivePrice;
	}
	
	/** 0001
	 * @param data Set the notTaxInclusivePrice
	 */	
	public void setNotTaxInclusivePrice(java.math.BigDecimal data) {
		this.notTaxInclusivePrice = data;
	}	
	/**
	 * 002
	 * @return java.lang.String remark
	 */
	public java.lang.String getRemark() {
		return this.remark;
	}
	
	/** 0001
	 * @param data Set the remark
	 */	
	public void setRemark(java.lang.String data) {
		this.remark = data;
	}	
	/**
	 * 002
	 * @return java.lang.Float taxRate
	 */
	public java.lang.Float getTaxRate() {
		return this.taxRate;
	}
	
	/** 0001
	 * @param data Set the taxRate
	 */	
	public void setTaxRate(java.lang.Float data) {
		this.taxRate = data;
	}	
	/**
	 * 002
	 * @return java.lang.String relateNumber
	 */
	public java.lang.String getRelateNumber() {
		return this.relateNumber;
	}
	
	/** 0001
	 * @param data Set the relateNumber
	 */	
	public void setRelateNumber(java.lang.String data) {
		this.relateNumber = data;
	}	
	/**
	 * 002
	 * @return java.lang.String itemNo
	 */
	public java.lang.String getItemNo() {
		return this.itemNo;
	}
	
	/** 0001
	 * @param data Set the itemNo
	 */	
	public void setItemNo(java.lang.String data) {
		this.itemNo = data;
	}	
	/**
	 * 002
	 * @return java.lang.Long invoiceDetailsId
	 */
	public java.lang.Long getInvoiceDetailsId() {
		return this.invoiceDetailsId;
	}
	
	/** 0001
	 * @param data Set the invoiceDetailsId
	 */	
	public void setInvoiceDetailsId(java.lang.Long data) {
		this.invoiceDetailsId = data;    //zzz
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
	 * @return java.lang.String invoiceNumber
	 */
	public java.lang.String getInvoiceNumber() {
		return this.invoiceNumber;
	}
	
	/** 0001
	 * @param data Set the invoiceNumber
	 */	
	public void setInvoiceNumber(java.lang.String data) {
		this.invoiceNumber = data;
	}	
	/**
	 * 002
	 * @return java.lang.String barcode
	 */
	public java.lang.String getBarcode() {
		return this.barcode;
	}
	
	/** 0001
	 * @param data Set the barcode
	 */	
	public void setBarcode(java.lang.String data) {
		this.barcode = data;
	}	
	/**
	 * 002
	 * @return java.lang.Long allowanceTax
	 */
	public java.lang.Long getAllowanceTax() {
		return this.allowanceTax;
	}
	
	/** 0001
	 * @param data Set the allowanceTax
	 */	
	public void setAllowanceTax(java.lang.Long data) {
		this.allowanceTax = data;
	}	
	/**
	 * 002
	 * @return java.math.BigDecimal amount
	 */
	public java.math.BigDecimal getAmount() {
		return this.amount;
	}
	
	/** 0001
	 * @param data Set the amount
	 */	
	public void setAmount(java.math.BigDecimal data) {
		this.amount = data;
	}	
	/**
	 * 002
	 * @return java.math.BigDecimal quantity
	 */
	public java.math.BigDecimal getQuantity() {
		return this.quantity;
	}
	
	/** 0001
	 * @param data Set the quantity
	 */	
	public void setQuantity(java.math.BigDecimal data) {
		this.quantity = data;
	}	
	/**
	 * 002
	 * @return java.lang.Long allowanceAmount
	 */
	public java.lang.Long getAllowanceAmount() {
		return this.allowanceAmount;
	}
	
	/** 0001
	 * @param data Set the allowanceAmount
	 */	
	public void setAllowanceAmount(java.lang.Long data) {
		this.allowanceAmount = data;
	}	
	/**
	 * 002
	 * @return java.lang.String cYearMonth
	 */
	public java.lang.String getCYearMonth() {
		return this.cYearMonth;
	}
	
	/** 0001
	 * @param data Set the cYearMonth
	 */	
	public void setCYearMonth(java.lang.String data) {
		this.cYearMonth = data;
	}	
	/**
	 * 002
	 * @return java.math.BigDecimal unitPrice
	 */
	public java.math.BigDecimal getUnitPrice() {
		return this.unitPrice;
	}
	
	/** 0001
	 * @param data Set the unitPrice
	 */	
	public void setUnitPrice(java.math.BigDecimal data) {
		this.unitPrice = data;
	}	
	/**
	 * 002
	 * @return java.lang.String buyer
	 */
	public java.lang.String getBuyer() {
		return this.buyer;
	}
	
	/** 0001
	 * @param data Set the buyer
	 */	
	public void setBuyer(java.lang.String data) {
		this.buyer = data;
	}	
	/**
	 * 002
	 * @return java.lang.Long sequenceNumber
	 */
	public java.lang.Long getSequenceNumber() {
		return this.sequenceNumber;
	}
	
	/** 0001
	 * @param data Set the sequenceNumber
	 */	
	public void setSequenceNumber(java.lang.Long data) {
		this.sequenceNumber = data;
	}	
	/**
	 * 002
	 * @return java.lang.String unit
	 */
	public java.lang.String getUnit() {
		return this.unit;
	}
	
	/** 0001
	 * @param data Set the unit
	 */	
	public void setUnit(java.lang.String data) {
		this.unit = data;
	}	
	/**
	 * 002
	 * @return java.lang.String taxType
	 */
	public java.lang.String getTaxType() {
		return this.taxType;
	}
	
	/** 0001
	 * @param data Set the taxType
	 */	
	public void setTaxType(java.lang.String data) {
		this.taxType = data;
	}	
	/**
	 * 002
	 * @return java.lang.Long creatorId
	 */
	public java.lang.Long getCreatorId() {
		return this.creatorId;
	}
	
	/** 0001
	 * @param data Set the creatorId
	 */	
	public void setCreatorId(java.lang.Long data) {
		this.creatorId = data;
	}	
	/**
	 * 002
	 * @return java.lang.Long modifierId
	 */
	public java.lang.Long getModifierId() {
		return this.modifierId;
	}
	
	/** 0001
	 * @param data Set the modifierId
	 */	
	public void setModifierId(java.lang.Long data) {
		this.modifierId = data;
	}	
	/**
	 * 002
	 * @return java.lang.Long allowanceQuantity
	 */
	public java.lang.Long getAllowanceQuantity() {
		return this.allowanceQuantity;
	}
	
	/** 0001
	 * @param data Set the allowanceQuantity
	 */	
	public void setAllowanceQuantity(java.lang.Long data) {
		this.allowanceQuantity = data;
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
	 *
	 */
	public InvoiceDetails(){
	}

	/**
	 * full constructor 
	 * @param cRemark1 
	 * @param cRemark2 
	 * @param description 
	 * @param notTaxInclusivePrice 
	 * @param remark 
	 * @param taxRate 
	 * @param relateNumber 
	 * @param itemNo 
	 * @param invoiceDetailsId 
	 * @param createDate 
	 * @param invoiceNumber 
	 * @param barcode 
	 * @param allowanceTax 
	 * @param amount 
	 * @param quantity 
	 * @param allowanceAmount 
	 * @param cYearMonth 
	 * @param unitPrice 
	 * @param buyer 
	 * @param sequenceNumber 
	 * @param unit 
	 * @param taxType 
	 * @param creatorId 
	 * @param modifierId 
	 * @param allowanceQuantity 
	 * @param modifyDate 
	 */
	public InvoiceDetails (
		 java.lang.String cRemark1 
		, java.lang.String cRemark2 
		, java.lang.String description 
		, java.math.BigDecimal notTaxInclusivePrice 
		, java.lang.String remark 
		, java.lang.Float taxRate 
		, java.lang.String relateNumber 
		, java.lang.String itemNo 
		, java.lang.Long invoiceDetailsId 
		, java.sql.Timestamp createDate 
		, java.lang.String invoiceNumber 
		, java.lang.String barcode 
		, java.lang.Long allowanceTax 
		, java.math.BigDecimal amount 
		, java.math.BigDecimal quantity 
		, java.lang.Long allowanceAmount 
		, java.lang.String cYearMonth 
		, java.math.BigDecimal unitPrice 
		, java.lang.String buyer 
		, java.lang.Long sequenceNumber 
		, java.lang.String unit 
		, java.lang.String taxType 
		, java.lang.Long creatorId 
		, java.lang.Long modifierId 
		, java.lang.Long allowanceQuantity 
		, java.sql.Timestamp modifyDate 
        ) {
		this.setCRemark1(cRemark1);
		this.setCRemark2(cRemark2);
		this.setDescription(description);
		this.setNotTaxInclusivePrice(notTaxInclusivePrice);
		this.setRemark(remark);
		this.setTaxRate(taxRate);
		this.setRelateNumber(relateNumber);
		this.setItemNo(itemNo);
		this.setInvoiceDetailsId(invoiceDetailsId);
		this.setCreateDate(createDate);
		this.setInvoiceNumber(invoiceNumber);
		this.setBarcode(barcode);
		this.setAllowanceTax(allowanceTax);
		this.setAmount(amount);
		this.setQuantity(quantity);
		this.setAllowanceAmount(allowanceAmount);
		this.setCYearMonth(cYearMonth);
		this.setUnitPrice(unitPrice);
		this.setBuyer(buyer);
		this.setSequenceNumber(sequenceNumber);
		this.setUnit(unit);
		this.setTaxType(taxType);
		this.setCreatorId(creatorId);
		this.setModifierId(modifierId);
		this.setAllowanceQuantity(allowanceQuantity);
		this.setModifyDate(modifyDate);
    }

	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		 return ToStringBuilder.reflectionToString(this , ToStringStyle.MULTI_LINE_STYLE);  
	}	 

	/*
    @Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof InvoiceDetails))
			return false;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(this.id, ((InvoiceDetails) object).id);
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

		if (obj == null || !(obj instanceof InvoiceDetails))
			return false;

		InvoiceDetails key = (InvoiceDetails) obj;
		if (this.invoiceDetailsId != key.invoiceDetailsId ) 
        	return false;

		return true;
    }

}