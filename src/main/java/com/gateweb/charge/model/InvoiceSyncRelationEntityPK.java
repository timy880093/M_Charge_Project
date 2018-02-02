package com.gateweb.charge.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by LOKI on 2016/4/22.
 */
@Embeddable
public class InvoiceSyncRelationEntityPK implements Serializable {
	
	/**
     * upload_type java.lang.String , PK     
     */
	@Column(name = "upload_type", unique = true, nullable = false)
	public String uploadType;
	
    /**
     * seller_identifier java.lang.String , PK     
     */
	@Column(name = "seller_identifier", unique = true, nullable = false)
	public String sellerIdentifier;
	
    /**
     * c_year_month java.lang.String , PK     
     */
	@Column(name = "c_year_month", unique = true, nullable = false)
	public String cYearMonth;
	
	/**
     * invoice_number java.lang.String , PK     
     */
	@Column(name = "invoice_number", unique = true, nullable = false)
	public String invoiceNumber;
	
	
    /**
	 * 
	 */
	public InvoiceSyncRelationEntityPK() {
		super();
	}

	/**
	 * @param uploadType
	 * @param sellerIdentifier
	 * @param cYearMonth
	 * @param invoiceNumber
	 */
	public InvoiceSyncRelationEntityPK(String uploadType, String sellerIdentifier, String cYearMonth,
					String invoiceNumber) {
		super();
		this.uploadType = uploadType;
		this.sellerIdentifier = sellerIdentifier;
		this.cYearMonth = cYearMonth;
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getcYearMonth() {
        return cYearMonth;
    }

    public void setcYearMonth(String cYearMonth) {
        this.cYearMonth = cYearMonth;
    }

    public String getSellerIdentifier() {
        return sellerIdentifier;
    }

    public void setSellerIdentifier(String sellerIdentifier) {
        this.sellerIdentifier = sellerIdentifier;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvoiceSyncRelationEntityPK that = (InvoiceSyncRelationEntityPK) o;

        if (invoiceNumber != null ? !invoiceNumber.equals(that.invoiceNumber) : that.invoiceNumber != null)
            return false;
        if (cYearMonth != null ? !cYearMonth.equals(that.cYearMonth) : that.cYearMonth != null) return false;
        if (sellerIdentifier != null ? !sellerIdentifier.equals(that.sellerIdentifier) : that.sellerIdentifier != null)
            return false;
        if (uploadType != null ? !uploadType.equals(that.uploadType) : that.uploadType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = invoiceNumber != null ? invoiceNumber.hashCode() : 0;
        result = 31 * result + (cYearMonth != null ? cYearMonth.hashCode() : 0);
        result = 31 * result + (sellerIdentifier != null ? sellerIdentifier.hashCode() : 0);
        result = 31 * result + (uploadType != null ? uploadType.hashCode() : 0);
        return result;
    }
}
