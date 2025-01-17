/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity.view;

import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.orm.hibernateExtension.PostgreSQLEnumType;
import org.hibernate.annotations.*;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.11.9"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(name = "contract", schema = "public", indexes = {
        @Index(name = "contract_pkey", unique = true, columnList = "contract_id ASC")
})
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@Immutable
public class ContractFetchView implements Serializable {

    private static final long serialVersionUID = 288687382;

    private Long contractId;
    private String name;
    private SimpleCompanyFetchView company;
    private ContractStatus status;
    private Integer periodMonth;
    private Boolean autoRenew;
    private LocalDateTime effectiveDate;
    private LocalDateTime expirationDate;
    private LocalDateTime installationDate;
    private Boolean firstInvoiceDateAsEffectiveDate;
    private Boolean isFirstContract;
    private Boolean allowPartialBilling;
    private Long renewPackageId;
    private ChargePackageFetchView chargePackage;

    public ContractFetchView() {
    }

    public ContractFetchView(Long contractId, String name, SimpleCompanyFetchView company, ContractStatus status, Integer periodMonth, Boolean autoRenew, LocalDateTime effectiveDate, LocalDateTime expirationDate, LocalDateTime installationDate, Boolean firstInvoiceDateAsEffectiveDate, Boolean isFirstContract, Boolean allowPartialBilling, Long renewPackageId, ChargePackageFetchView chargePackage) {
        this.contractId = contractId;
        this.name = name;
        this.company = company;
        this.status = status;
        this.periodMonth = periodMonth;
        this.autoRenew = autoRenew;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.installationDate = installationDate;
        this.firstInvoiceDateAsEffectiveDate = firstInvoiceDateAsEffectiveDate;
        this.isFirstContract = isFirstContract;
        this.allowPartialBilling = allowPartialBilling;
        this.renewPackageId = renewPackageId;
        this.chargePackage = chargePackage;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id", unique = true, nullable = false, precision = 64)
    public Long getContractId() {
        return this.contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    @Column(name = "name", length = 50)
    @Size(max = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    public SimpleCompanyFetchView getCompany() {
        return company;
    }

    public void setCompany(SimpleCompanyFetchView company) {
        this.company = company;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Type(type = "pgsql_enum")
    public ContractStatus getStatus() {
        return this.status;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    @Column(name = "period_month", precision = 32)
    public Integer getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(Integer periodMonth) {
        this.periodMonth = periodMonth;
    }

    @Column(name = "auto_renew")
    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    @Column(name = "effective_date")
    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Column(name = "expiration_date")
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Column(name = "allow_partial_billing")
    public Boolean getAllowPartialBilling() {
        return allowPartialBilling;
    }

    public void setAllowPartialBilling(Boolean allowPartialBilling) {
        this.allowPartialBilling = allowPartialBilling;
    }

    @Column(name = "first_invoice_date_as_effective_date")
    public Boolean getFirstInvoiceDateAsEffectiveDate() {
        return firstInvoiceDateAsEffectiveDate;
    }

    public void setFirstInvoiceDateAsEffectiveDate(Boolean firstInvoiceDateAsEffectiveDate) {
        this.firstInvoiceDateAsEffectiveDate = firstInvoiceDateAsEffectiveDate;
    }

    @Column(name = "is_first_contract")
    public Boolean getFirstContract() {
        return isFirstContract;
    }

    public void setFirstContract(Boolean firstContract) {
        isFirstContract = firstContract;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "package_id")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 10)
    public ChargePackageFetchView getChargePackage() {
        return chargePackage;
    }

    public void setChargePackage(ChargePackageFetchView chargePackage) {
        this.chargePackage = chargePackage;
    }

    @Column(name = "installation_date")
    public LocalDateTime getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDateTime installationDate) {
        this.installationDate = installationDate;
    }

    @Column(name = "renew_package_id")
    public Long getRenewPackageId() {
        return renewPackageId;
    }

    public void setRenewPackageId(Long renewPackageId) {
        this.renewPackageId = renewPackageId;
    }

    @Override
    public String toString() {
        return "ContractFetchView{" +
                "contractId=" + contractId +
                ", name='" + name + '\'' +
                ", company=" + company +
                ", status=" + status +
                ", periodMonth=" + periodMonth +
                ", autoRenew=" + autoRenew +
                ", effectiveDate=" + effectiveDate +
                ", expirationDate=" + expirationDate +
                ", installationDate=" + installationDate +
                ", firstInvoiceDateAsEffectiveDate=" + firstInvoiceDateAsEffectiveDate +
                ", isFirstContract=" + isFirstContract +
                ", allowPartialBilling=" + allowPartialBilling +
                ", renewPackageId=" + renewPackageId +
                ", chargePackage=" + chargePackage +
                '}';
    }
}
