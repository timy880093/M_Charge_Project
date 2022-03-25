package com.gateweb.orm.einv.entity;


import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "invoice_main")
@Immutable
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "invoiceAmountEntity",
                classes = @ConstructorResult(
                        targetClass = InvoiceAmountEntity.class,
                        columns = {
                                @ColumnResult(name = "businessNo", type = String.class),
                                @ColumnResult(name = "amount", type = Long.class)}))
})
@NamedNativeQuery(
        name = "InvoiceAmountEntity.findInvoiceAmountByYearMonth",
        query = "SELECT im.seller as businessNo \n" +
                "\t,count(invoice_id) AS amount \n" +
                "FROM invoice_main im\n" +
                "WHERE (\n" +
                "\t\tseller_customer_number NOT IN (\n" +
                "\t\t\tSELECT account\n" +
                "\t\t\tFROM PUBLIC.user\n" +
                "\t\t\tWHERE role_id = 710\n" +
                "\t\t\t)\n" +
                "\t\tOR seller_customer_number IS NULL\n" +
                "\t\t)\n" +
                "\tAND invoice_date >= :fromInvoiceDate \n" +
                "\tAND invoice_date <= :toInvoiceDate \n" +
                "\tAND c_year_month = :yearMonth\n" +
                "GROUP BY im.seller\n" +
                "ORDER BY im.seller\n",
        resultSetMapping = "invoiceAmountEntity"
)
public class InvoiceAmountEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public java.lang.Long id;

    @Column(name = "business_no", nullable = false)
    String businessNo;

    @Column(name = "amount", nullable = false)
    Long amount;

    public InvoiceAmountEntity() {
    }

    public InvoiceAmountEntity(String businessNo, Long amount) {
        this.businessNo = businessNo;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "InvoiceAmountEntity{" +
                "id=" + id +
                ", businessNo='" + businessNo + '\'' +
                ", amount=" + amount +
                '}';
    }
}
