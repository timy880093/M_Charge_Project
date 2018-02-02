package com.gateweb.charge.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by LOKI on 2016/1/28.
 */
@Embeddable
public class InvoiceMainUniqueEntityPK implements Serializable {
	/**
	 * year_month
	 */
	//@Column(name = "year_month", nullable = false)
	@Column(name = "year_month")
	protected String yearMonth;
	
	/**
	 * invoice_number
	 */
	//@Column(name = "invoice_number", nullable = false)
	@Column(name = "invoice_number")
	protected String invoiceNumber;

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvoiceMainUniqueEntityPK that = (InvoiceMainUniqueEntityPK) o;

        if (yearMonth != null ? !yearMonth.equals(that.yearMonth) : that.yearMonth != null) return false;
        if (invoiceNumber != null ? !invoiceNumber.equals(that.invoiceNumber) : that.invoiceNumber != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = yearMonth != null ? yearMonth.hashCode() : 0;
        result = 31 * result + (invoiceNumber != null ? invoiceNumber.hashCode() : 0);
        return result;
    }
}
