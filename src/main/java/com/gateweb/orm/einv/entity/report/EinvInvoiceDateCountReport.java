package com.gateweb.orm.einv.entity.report;

import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.persistence.*;

@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity(name = "einvInvoiceDateCountReport")
@Immutable
@Table(name = "invoice_main", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "invoice_main_pkey", columnNames = {"invoice_id"})
})
@SqlResultSetMapping(
        name = "einvInvoiceDateCountReport",
        classes = @ConstructorResult(
                targetClass = EinvInvoiceDateCountReport.class,
                columns = {
                        @ColumnResult(name = "invoice_date", type = String.class),
                        @ColumnResult(name = "seller", type = String.class),
                        @ColumnResult(name = "count", type = Long.class)
                }))
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "EinvInvoiceDateCountReport.findEinvInvoiceDateCountReport",
                query = "SELECT invoice_date" +
                        " , seller " +
                        " , count(invoice_id) as count" +
                        " FROM invoice_main " +
                        " WHERE invoice_date LIKE :invoiceDate " +
                        " AND c_year_month = :yearMonth " +
                        " AND source is null " +
                        " GROUP BY invoice_date " +
                        " ,seller; ",
                resultSetMapping = "einvInvoiceDateCountReport"
        ),
        @NamedNativeQuery(
                name = "EinvInvoiceDateCountReport.findEinvInvoiceDateCountReportWithSeller",
                query = "SELECT invoice_date" +
                        " , seller " +
                        " , count(invoice_id) as count" +
                        " FROM invoice_main " +
                        " WHERE invoice_date LIKE :invoiceDate " +
                        " AND c_year_month = :yearMonth " +
                        " AND seller = :seller " +
                        " AND source is null " +
                        " GROUP BY invoice_date " +
                        " ,seller; ",
                resultSetMapping = "einvInvoiceDateCountReport"
        )
})
public class EinvInvoiceDateCountReport {
    private Long id;
    private String invoiceDate;
    private String seller;
    private Long count;

    public EinvInvoiceDateCountReport() {
    }

    public EinvInvoiceDateCountReport(String invoiceDate, String seller, Long count) {
        this.invoiceDate = invoiceDate;
        this.seller = seller;
        this.count = count;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, precision = 64)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "invoice_date")
    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @Column(name = "seller")
    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    @Column(name = "count")
    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "EinvInvoiceDateCountReport{" +
                "id=" + id +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", seller='" + seller + '\'' +
                ", count=" + count +
                '}';
    }
}
