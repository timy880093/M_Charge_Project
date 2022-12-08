package com.gateweb.orm.charge.entity.view;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "chargeIasrP2bTotalAmountView")
@Table(name = "invoice_amount_summary_report", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "invoice_amount_summary_report_pkey", columnNames = {"id"})
})
@SqlResultSetMapping(
        name = "invoiceAmountForP2b",
        classes = @ConstructorResult(
                targetClass = ChargeIasrP2bTotalAmountView.class,
                columns = {
                        @ColumnResult(name = "month", type = String.class),
                        @ColumnResult(name = "seller", type = String.class),
                        @ColumnResult(name = "amount", type = Integer.class),
                        @ColumnResult(name = "total", type = BigDecimal.class)})
)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "ChargeIasrP2bTotalAmountView.findInvoiceAmountForP2b",
                query = "select im.month as month, im.seller as seller, sum(im.amount) as amount, sum(im.total) as total "
                        + " from invoice_amount_summary_report im where im.seller=:sellerTaxNumber "
                        + "	and im.invoice_status='2' and im.invoice_date >= :startDate and im.invoice_date<=:endDate "
                        + "	GROUP BY (im.month, im.seller) "
                        + " ORDER BY im.month",
                resultSetMapping = "invoiceAmountForP2b"
        )
})
public class ChargeIasrP2bTotalAmountView {
    private Long id;
    private String month;
    private String seller;
    private Integer amount;
    private BigDecimal total;

    public ChargeIasrP2bTotalAmountView() {
    }

    public ChargeIasrP2bTotalAmountView(String month, String seller, Integer amount, BigDecimal total) {
        this.month = month;
        this.seller = seller;
        this.amount = amount;
        this.total = total;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "IasrAmountSummaryView{" +
                "id=" + id +
                ", month='" + month + '\'' +
                ", seller='" + seller + '\'' +
                ", amount=" + amount +
                ", total=" + total +
                '}';
    }
}
