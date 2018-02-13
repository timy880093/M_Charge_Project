/*
 * $Header: $
 * This java source file is generated by pkliu on Fri Aug 25 15:16:56 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author pkliu
 *
 */
@Entity
@Table(name = "invoice_sync_relation")
public class InvoiceSyncRelationEntity implements Serializable {

//long serialVersionUID jdk tool: serialver.exe 

	@EmbeddedId
    public InvoiceSyncRelationEntityPK id;
	
	/**
	 * company_key
	 */
	@Column(name = "company_key")
	public String companyKey;
	
	/**
	 * send_mail
	 */
	@Column(name = "send_mail")
	public Boolean sendMail;
	

	/**
	 * 002
	 * @return java.lang.String companyKey
	 */
	public String getCompanyKey() {
		return this.companyKey;
	}
	
	/** 0001
	 * @param data Set the companyKey
	 */	
	public void setCompanyKey(String data) {
		this.companyKey = data;
	}	
	/**
	 * 002
	 * @return java.lang.Boolean sendMail
	 */
	public Boolean getSendMail() {
		return this.sendMail;
	}
	
	/** 0001
	 * @param data Set the sendMail
	 */	
	public void setSendMail(Boolean data) {
		this.sendMail = data;
	}	


	/**
	 *
	 */
	public InvoiceSyncRelationEntity(){
	}

	/**
	 * full constructor 
	 * @param uploadType 
	 * @param sellerIdentifier 
	 * @param cYearMonth 
	 * @param companyKey 
	 * @param sendMail 
	 * @param invoiceNumber 
	 */
	public InvoiceSyncRelationEntity (
		InvoiceSyncRelationEntityPK id 
		, String companyKey
		, Boolean sendMail
        ) {
		
		this.setCompanyKey(companyKey);
		this.setSendMail(sendMail);
		this.setId(id);
    }

	
	/**
	 * @return the id
	 */
	public InvoiceSyncRelationEntityPK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(InvoiceSyncRelationEntityPK id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		 //return ToStringBuilder.reflectionToString(this , ToStringStyle.MULTI_LINE_STYLE);  
		 return new ToStringBuilder(this)
		.append("uploadType", this.id.uploadType)
		.append("sellerIdentifier", this.id.sellerIdentifier)
		.append("cYearMonth", this.id.cYearMonth)
		.append("companyKey", this.companyKey)
		.append("sendMail", this.sendMail)
		.append("invoiceNumber", this.id.invoiceNumber)
		.toString();
	}	 

	/*
    @Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof InvoiceSyncRelationEntity))
			return false;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(this.id, ((InvoiceSyncRelationEntity) object).id);
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

		if (obj == null || !(obj instanceof InvoiceSyncRelationEntity))
			return false;

		InvoiceSyncRelationEntity key = (InvoiceSyncRelationEntity) obj;
		if (this.id.uploadType != key.id.uploadType ) 
        	return false;
		if (this.id.sellerIdentifier != key.id.sellerIdentifier ) 
        	return false;
		if (this.id.cYearMonth != key.id.cYearMonth ) 
        	return false;
		if (this.id.invoiceNumber != key.id.invoiceNumber ) 
        	return false;

		return true;
    }

}