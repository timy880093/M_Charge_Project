/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:14 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.model;

import java.io.Serializable;
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

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 *
 * @author pkliu
 *
 */
@Entity
@Table(name = "deduct_detail")
public class DeductDetailEntity implements Serializable {

//long serialVersionUID jdk tool: serialver.exe 

	/**
	 * company_id
	 */
	@Column(name = "company_id")
	protected java.lang.Integer companyId;
	
	/**
	 * money
	 */
	@Column(name = "money")
	protected java.lang.Integer money;
	
	/**
	 * prepay_deduct_master_id
	 */
	@Column(name = "prepay_deduct_master_id")
	protected java.lang.Integer prepayDeductMasterId;
	
	/**
	 * deduct_type
	 */
	@Column(name = "deduct_type")
	protected java.lang.Integer deductType;
	
    /**
     * deduct_detail_id java.lang.Integer , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "deduct_detail_id", unique = true, nullable = false)
	protected java.lang.Integer deductDetailId;
	
	/**
	 * creator_id
	 */
	@Column(name = "creator_id")
	protected java.lang.Integer creatorId;
	
	/**
	 * modifier_id
	 */
	@Column(name = "modifier_id")
	protected java.lang.Integer modifierId;
	
	/**
	 * cash_detail_id
	 */
	@Column(name = "cash_detail_id")
	protected java.lang.Integer cashDetailId;
	
	/**
	 * cal_ym
	 */
	@Column(name = "cal_ym")
	protected java.lang.String calYm;
	
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
	 * 002
	 * @return java.lang.Integer companyId
	 */
	public java.lang.Integer getCompanyId() {
		return this.companyId;
	}
	
	/** 0001
	 * @param data Set the companyId
	 */	
	public void setCompanyId(java.lang.Integer data) {
		this.companyId = data;
	}	
	/**
	 * 002
	 * @return java.lang.Integer money
	 */
	public java.lang.Integer getMoney() {
		return this.money;
	}
	
	/** 0001
	 * @param data Set the money
	 */	
	public void setMoney(java.lang.Integer data) {
		this.money = data;
	}	
	/**
	 * 002
	 * @return java.lang.Integer prepayDeductMasterId
	 */
	public java.lang.Integer getPrepayDeductMasterId() {
		return this.prepayDeductMasterId;
	}
	
	/** 0001
	 * @param data Set the prepayDeductMasterId
	 */	
	public void setPrepayDeductMasterId(java.lang.Integer data) {
		this.prepayDeductMasterId = data;
	}	
	/**
	 * 002
	 * @return java.lang.Integer deductType
	 */
	public java.lang.Integer getDeductType() {
		return this.deductType;
	}
	
	/** 0001
	 * @param data Set the deductType
	 */	
	public void setDeductType(java.lang.Integer data) {
		this.deductType = data;
	}	
	/**
	 * 002
	 * @return java.lang.Integer deductDetailId
	 */
	public java.lang.Integer getDeductDetailId() {
		return this.deductDetailId;
	}
	
	/** 0001
	 * @param data Set the deductDetailId
	 */	
	public void setDeductDetailId(java.lang.Integer data) {
		this.deductDetailId = data;    //zzz
	}	
	/**
	 * 002
	 * @return java.lang.Integer creatorId
	 */
	public java.lang.Integer getCreatorId() {
		return this.creatorId;
	}
	
	/** 0001
	 * @param data Set the creatorId
	 */	
	public void setCreatorId(java.lang.Integer data) {
		this.creatorId = data;
	}	
	/**
	 * 002
	 * @return java.lang.Integer modifierId
	 */
	public java.lang.Integer getModifierId() {
		return this.modifierId;
	}
	
	/** 0001
	 * @param data Set the modifierId
	 */	
	public void setModifierId(java.lang.Integer data) {
		this.modifierId = data;
	}	
	/**
	 * 002
	 * @return java.lang.Integer cashDetailId
	 */
	public java.lang.Integer getCashDetailId() {
		return this.cashDetailId;
	}
	
	/** 0001
	 * @param data Set the cashDetailId
	 */	
	public void setCashDetailId(java.lang.Integer data) {
		this.cashDetailId = data;
	}	
	/**
	 * 002
	 * @return java.lang.String calYm
	 */
	public java.lang.String getCalYm() {
		return this.calYm;
	}
	
	/** 0001
	 * @param data Set the calYm
	 */	
	public void setCalYm(java.lang.String data) {
		this.calYm = data;
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
	 *
	 */
	public DeductDetailEntity(){
	}

	/**
	 * full constructor 
	 * @param companyId 
	 * @param money 
	 * @param prepayDeductMasterId 
	 * @param deductType 
	 * @param deductDetailId 
	 * @param creatorId 
	 * @param modifierId 
	 * @param cashDetailId 
	 * @param calYm 
	 * @param createDate 
	 * @param modifyDate 
	 */
	public DeductDetailEntity (
		 java.lang.Integer companyId 
		, java.lang.Integer money 
		, java.lang.Integer prepayDeductMasterId 
		, java.lang.Integer deductType 
		, java.lang.Integer deductDetailId 
		, java.lang.Integer creatorId 
		, java.lang.Integer modifierId 
		, java.lang.Integer cashDetailId 
		, java.lang.String calYm 
		, java.sql.Timestamp createDate 
		, java.sql.Timestamp modifyDate 
        ) {
		this.setCompanyId(companyId);
		this.setMoney(money);
		this.setPrepayDeductMasterId(prepayDeductMasterId);
		this.setDeductType(deductType);
		this.setDeductDetailId(deductDetailId);
		this.setCreatorId(creatorId);
		this.setModifierId(modifierId);
		this.setCashDetailId(cashDetailId);
		this.setCalYm(calYm);
		this.setCreateDate(createDate);
		this.setModifyDate(modifyDate);
    }

	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		 //return ToStringBuilder.reflectionToString(this , ToStringStyle.MULTI_LINE_STYLE);  
		 return new ToStringBuilder(this)
		.append("companyId", this.companyId)
		.append("money", this.money)
		.append("prepayDeductMasterId", this.prepayDeductMasterId)
		.append("deductType", this.deductType)
		.append("deductDetailId", this.deductDetailId)
		.append("creatorId", this.creatorId)
		.append("modifierId", this.modifierId)
		.append("cashDetailId", this.cashDetailId)
		.append("calYm", this.calYm)
		.append("createDate", this.createDate)
		.append("modifyDate", this.modifyDate)
		.toString();
	}	 

	/*
    @Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof DeductDetailEntity))
			return false;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(this.id, ((DeductDetailEntity) object).id);
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

		if (obj == null || !(obj instanceof DeductDetailEntity))
			return false;

		DeductDetailEntity key = (DeductDetailEntity) obj;
		if (this.deductDetailId != key.deductDetailId ) 
        	return false;

		return true;
    }

}