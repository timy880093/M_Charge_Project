package com.gateweb.orm.charge.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_remaining", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "invoice_remaining_pkey", columnNames = {"invoice_remaining_id"})
})
@SqlResultSetMapping(
        name = "invoiceRemainingData",
        classes = @ConstructorResult(
                targetClass = InvoiceRemainingData.class,
                columns = {
                        @ColumnResult(name = "invoice_remaining_id", type = Long.class),
                        @ColumnResult(name = "business_no", type = String.class),
                        @ColumnResult(name = "package_name", type = String.class),
                        @ColumnResult(name = "usage", type = Integer.class),
                        @ColumnResult(name = "remaining", type = Integer.class),
                        @ColumnResult(name = "create_date", type = LocalDateTime.class),
                        @ColumnResult(name = "modify_date", type = LocalDateTime.class),
                        @ColumnResult(name = "invoice_date", type = String.class),
                        @ColumnResult(name = "contract_effective_date", type = LocalDateTime.class)
                }))
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "InvoiceRemainingData.findInvoiceRemainingData",
                query = "SELECT ir.invoice_remaining_id " +
                        " ,com.business_no " +
                        " ,cp.name as package_name" +
                        " ,ir.usage " +
                        " ,ir.remaining " +
                        " ,ir.create_date as create_date" +
                        " ,ir.modify_date as modify_date" +
                        " ,ir.invoice_date as invoice_date " +
                        " ,con.effective_date as contract_effective_date " +
                        "FROM invoice_remaining ir " +
                        "JOIN contract con ON (ir.contract_id = con.contract_id) " +
                        "JOIN company com ON (com.company_id = con.company_id) " +
                        "JOIN charge_package cp ON (cp.package_id = con.package_id) " +
                        " WHERE con.company_id = :companyId ;",
                resultSetMapping = "invoiceRemainingData"
        )
})
public class InvoiceRemainingData implements Serializable {
    private Long invoiceRemainingId;
    private String packageName;
    private String businessNo;
    private Integer usage;
    private Integer remaining;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String invoiceDate;
    private LocalDateTime contractEffectiveDate;

    public InvoiceRemainingData() {
    }

    public InvoiceRemainingData(Long invoiceRemainingId, String packageName, String businessNo, Integer usage, Integer remaining, LocalDateTime createDate, LocalDateTime modifyDate, String invoiceDate, LocalDateTime contractEffectiveDate) {
        this.invoiceRemainingId = invoiceRemainingId;
        this.packageName = packageName;
        this.businessNo = businessNo;
        this.usage = usage;
        this.remaining = remaining;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.invoiceDate = invoiceDate;
        this.contractEffectiveDate = contractEffectiveDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_remaining_id", nullable = false, precision = 64)
    public Long getInvoiceRemainingId() {
        return invoiceRemainingId;
    }

    public void setInvoiceRemainingId(Long invoiceRemainingId) {
        this.invoiceRemainingId = invoiceRemainingId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public Integer getUsage() {
        return usage;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDateTime getContractEffectiveDate() {
        return contractEffectiveDate;
    }

    public void setContractEffectiveDate(LocalDateTime contractEffectiveDate) {
        this.contractEffectiveDate = contractEffectiveDate;
    }

    @Override
    public String toString() {
        return "InvoiceRemainingView{" +
                "invoiceRemainingId=" + invoiceRemainingId +
                ", packageName='" + packageName + '\'' +
                ", businessNo='" + businessNo + '\'' +
                ", usage=" + usage +
                ", remaining=" + remaining +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", contractEffectiveDate=" + contractEffectiveDate +
                '}';
    }
}
