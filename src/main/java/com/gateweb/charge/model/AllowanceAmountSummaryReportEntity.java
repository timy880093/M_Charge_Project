/*
 * $Header: $
 * This java source file is generated by pkliu on Mon Jan 29 10:38:25 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author pkliu
 *
 */
@Entity(name = "chargeAllowanceAmountSummaryReportEntity")
@Table(name = "allowance_amount_summary_report")
public class AllowanceAmountSummaryReportEntity implements Serializable {

//long serialVersionUID jdk tool: serialver.exe 

	/**
	 * seller
	 */
	@Column(name = "seller")
	protected String seller;

	/**
	 * amount
	 */
	@Column(name = "amount")
	protected Integer amount;

	/**
	 * total
	 */
	@Column(name = "total")
	protected java.math.BigDecimal total;

	/**
	 * creator_id
	 */
	@Column(name = "creator_id")
	protected Integer creatorId;

	/**
	 * modifier_id
	 */
	@Column(name = "modifier_id")
	protected Integer modifierId;

    /**
     * id java.lang.Long , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	protected Long id;

	/**
	 * create_date
	 */
	@Column(name = "create_date")
	protected java.sql.Timestamp createDate;

	/**
	 * modify_date
	 */
	@Column(name = "modify_date")
	protected java.sql.Timestamp modifyDate;

	/**
	 * allowance_date
	 */
	@Column(name = "allowance_date")
	protected String allowanceDate;

	/**
	 * buyer
	 */
	@Column(name = "buyer")
	protected String buyer;

	/**
	 * allowance_status
	 */
	@Column(name = "allowance_status")
	protected Integer allowanceStatus;





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
	 * @return java.lang.Integer amount
	 */
	public Integer getAmount() {
		return this.amount;
	}

	/** 0001
	 * @param data Set the amount
	 */
	public void setAmount(Integer data) {
		this.amount = data;
	}
	/**
	 * 002
	 * @return java.math.BigDecimal total
	 */
	public java.math.BigDecimal getTotal() {
		return this.total;
	}

	/** 0001
	 * @param data Set the total
	 */
	public void setTotal(java.math.BigDecimal data) {
		this.total = data;
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
	 * @return java.lang.String allowanceDate
	 */
	public String getAllowanceDate() {
		return this.allowanceDate;
	}

	/** 0001
	 * @param data Set the allowanceDate
	 */
	public void setAllowanceDate(String data) {
		this.allowanceDate = data;
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
	 * @return java.lang.Integer allowanceStatus
	 */
	public Integer getAllowanceStatus() {
		return this.allowanceStatus;
	}

	/** 0001
	 * @param data Set the allowanceStatus
	 */
	public void setAllowanceStatus(Integer data) {
		this.allowanceStatus = data;
	}



	/**
	 *
	 */
	public AllowanceAmountSummaryReportEntity(){
	}

	/**
	 * full constructor
	 * @param seller
	 * @param amount
	 * @param total
	 * @param creatorId
	 * @param modifierId
	 * @param id
	 * @param createDate
	 * @param modifyDate
	 * @param allowanceDate
	 * @param buyer
	 * @param allowanceStatus
	 */
	public AllowanceAmountSummaryReportEntity (
		 String seller
		, Integer amount
		, java.math.BigDecimal total
		, Integer creatorId
		, Integer modifierId
		, Long id
		, java.sql.Timestamp createDate
		, java.sql.Timestamp modifyDate
		, String allowanceDate
		, String buyer
		, Integer allowanceStatus
        ) {
		this.setSeller(seller);
		this.setAmount(amount);
		this.setTotal(total);
		this.setCreatorId(creatorId);
		this.setModifierId(modifierId);
		this.setId(id);
		this.setCreateDate(createDate);
		this.setModifyDate(modifyDate);
		this.setAllowanceDate(allowanceDate);
		this.setBuyer(buyer);
		this.setAllowanceStatus(allowanceStatus);
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
		.append("amount", this.amount)
		.append("total", this.total)
		.append("creatorId", this.creatorId)
		.append("modifierId", this.modifierId)
		.append("id", this.id)
		.append("createDate", this.createDate)
		.append("modifyDate", this.modifyDate)
		.append("allowanceDate", this.allowanceDate)
		.append("buyer", this.buyer)
		.append("allowanceStatus", this.allowanceStatus)
		.toString();
	}	 

	/*
    @Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof AllowanceAmountSummaryReportEntity))
			return false;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(this.id, ((AllowanceAmountSummaryReportEntity) object).id);
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

		if (obj == null || !(obj instanceof AllowanceAmountSummaryReportEntity))
			return false;

		AllowanceAmountSummaryReportEntity key = (AllowanceAmountSummaryReportEntity) obj;
		if (this.id != key.id ) 
        	return false;

		return true;
    }

}