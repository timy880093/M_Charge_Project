package com.gateweb.orm.charge.entity;

import javax.persistence.*;

@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity(name = "chargeIasrCountReport")
@Table(name = "invoice_amount_summary_report", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "invoice_amount_summary_report_pkey", columnNames = {"id"})
})
@SqlResultSetMapping(
        name = "chargeIasrCountReport",
        classes = @ConstructorResult(
                targetClass = ChargeIasrCountReport.class,
                columns = {
                        @ColumnResult(name = "invoice_date", type = String.class),
                        @ColumnResult(name = "seller", type = String.class),
                        @ColumnResult(name = "count", type = Long.class),
                }))
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "ChargeIasrCountReport.findChargeIasrCountReport",
                query = " SELECT iasr.invoice_date, " +
                        " iasr.seller,sum(iasr.amount) as count " +
                        " FROM invoice_amount_summary_report iasr " +
                        " WHERE iasr.invoice_date LIKE :invoiceDate " +
                        " GROUP BY invoice_date,seller;",
                resultSetMapping = "chargeIasrCountReport"
        ),
        @NamedNativeQuery(
                name = "ChargeIasrCountReport.findChargeIasrCountReportWithSeller",
                query = " SELECT iasr.invoice_date, " +
                        " iasr.seller,sum(iasr.amount) as count " +
                        " FROM invoice_amount_summary_report iasr " +
                        " WHERE iasr.invoice_date LIKE :invoiceDate " +
                        " AND iasr.seller = :seller " +
                        " GROUP BY invoice_date,seller;",
                resultSetMapping = "chargeIasrCountReport"
        )
})
public class ChargeIasrCountReport {
    private Long id;
    private String invoiceDate;
    private String seller;
    private Long count;

    public ChargeIasrCountReport() {
    }

    public ChargeIasrCountReport(String invoiceDate, String seller, Long count) {
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
        return "ChargeIasrCountReport{" +
                "id=" + id +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", seller='" + seller + '\'' +
                ", count=" + count +
                '}';
    }
}
