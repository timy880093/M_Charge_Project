/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.einv.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "invoice_main", schema = "public", uniqueConstraints = {
    @UniqueConstraint(name = "invoice_main_pkey_1", columnNames = {"invoice_id"}),
    @UniqueConstraint(name = "invoice_main_invoice_number_key_1", columnNames = {"invoice_number"})
}, indexes = {
    @Index(name = "invoice_main_cym_date_idx_1", columnList = "c_year_month ASC, invoice_date ASC"),
    @Index(name = "invoice_main_index_01", columnList = "seller ASC, buyer ASC, b2b_flag ASC"),
    @Index(name = "invoice_main_inv_num_idx_01", columnList = "invoice_number ASC"),
    @Index(name = "invoice_main_invoice_date_idx_01", columnList = "invoice_date ASC"),
    @Index(name = "invoice_main_modify_date_idx_01", columnList = "modify_date ASC"),
    @Index(name = "invoice_main_modify_date_idx_1", columnList = "modify_date ASC"),
    @Index(name = "invoice_main_seller_unique_number_index", columnList = "seller ASC, unique_number ASC")
})
public class InvoiceMain implements Serializable {

    private static final long serialVersionUID = -489724687;

    private Long          invoiceId;
    private String        cPrinterNo;
    private String        invoiceNumber;
    private String        invoiceDate;
    private String        invoiceTime;
    private String        seller;
    private String        buyer;
    private String        checkNumber;
    private String        buyerRemark;
    private String        mainRemark;
    private String        customsClearanceMark;
    private String        category;
    private String        relateNumber;
    private String        invoiceType;
    private String        groupMark;
    private String        donateMark;
    private String        carrierType;
    private String        carrierId1;
    private String        carrierId2;
    private String        printMark;
    private String        npoban;
    private String        randomNumber;
    private BigDecimal    salesAmount;
    private BigDecimal    freeTaxSalesAmount;
    private BigDecimal    zeroTaxSalesAmount;
    private String        taxType;
    private Float         taxRate;
    private BigDecimal    taxAmount;
    private BigDecimal    totalAmount;
    private BigDecimal    discountAmount;
    private BigDecimal    originalCurrencyAmount;
    private Float         exchangeRate;
    private String        currency;
    private String        cPhoneCode;
    private String        cNaturalPerson;
    private String        cMemberType;
    private String        cMemberNumber;
    private Integer       cInvoiceStatus;
    private String        cPosRemark1;
    private String        cPosRemark2;
    private String        cKey;
    private Integer       creatorId;
    private LocalDateTime createDate;
    private Integer       modifierId;
    private LocalDateTime modifyDate;
    private String        cYearMonth;
    private Integer       assignId;
    private String        cEmail;
    private String        sellerName;
    private Integer       cPrinterId;
    private String        buyerName;
    private String        cPosRemark_3;
    private String        cPosRemark_4;
    private String        sellerCustomerNumber;
    private String        buyerCustomerNumber;
    private String        buyerAddress;
    private String        sellerPersonInCharge;
    private String        buyerPersonInCharge;
    private String        bondedAreaConfirm;
    private String        b2bFlag;
    private String        sellerAddress;
    private String        sellerTelephoneNumber;
    private String        sellerFacsimileNumber;
    private String        sellerEmailAddress;
    private String        sellerRoleRemark;
    private String        buyerTelephoneNumber;
    private String        buyerFacsimileNumber;
    private String        buyerEmailAddress;
    private String        buyerRoleRemark;
    private String        syncCompanyKey;
    private Boolean       syncPrintMark;
    private Integer       syncPrintUser;
    private LocalDateTime syncPrintDate;
    private Integer       confirmStatus;
    private Integer       allowanceCount;
    private Integer       acceptStatus;
    private String        migType;
    private String        uploadStatus;
    private String        uniqueNumber;
    private String        source;

    public InvoiceMain() {}

    public InvoiceMain(InvoiceMain value) {
        this.invoiceId = value.invoiceId;
        this.cPrinterNo = value.cPrinterNo;
        this.invoiceNumber = value.invoiceNumber;
        this.invoiceDate = value.invoiceDate;
        this.invoiceTime = value.invoiceTime;
        this.seller = value.seller;
        this.buyer = value.buyer;
        this.checkNumber = value.checkNumber;
        this.buyerRemark = value.buyerRemark;
        this.mainRemark = value.mainRemark;
        this.customsClearanceMark = value.customsClearanceMark;
        this.category = value.category;
        this.relateNumber = value.relateNumber;
        this.invoiceType = value.invoiceType;
        this.groupMark = value.groupMark;
        this.donateMark = value.donateMark;
        this.carrierType = value.carrierType;
        this.carrierId1 = value.carrierId1;
        this.carrierId2 = value.carrierId2;
        this.printMark = value.printMark;
        this.npoban = value.npoban;
        this.randomNumber = value.randomNumber;
        this.salesAmount = value.salesAmount;
        this.freeTaxSalesAmount = value.freeTaxSalesAmount;
        this.zeroTaxSalesAmount = value.zeroTaxSalesAmount;
        this.taxType = value.taxType;
        this.taxRate = value.taxRate;
        this.taxAmount = value.taxAmount;
        this.totalAmount = value.totalAmount;
        this.discountAmount = value.discountAmount;
        this.originalCurrencyAmount = value.originalCurrencyAmount;
        this.exchangeRate = value.exchangeRate;
        this.currency = value.currency;
        this.cPhoneCode = value.cPhoneCode;
        this.cNaturalPerson = value.cNaturalPerson;
        this.cMemberType = value.cMemberType;
        this.cMemberNumber = value.cMemberNumber;
        this.cInvoiceStatus = value.cInvoiceStatus;
        this.cPosRemark1 = value.cPosRemark1;
        this.cPosRemark2 = value.cPosRemark2;
        this.cKey = value.cKey;
        this.creatorId = value.creatorId;
        this.createDate = value.createDate;
        this.modifierId = value.modifierId;
        this.modifyDate = value.modifyDate;
        this.cYearMonth = value.cYearMonth;
        this.assignId = value.assignId;
        this.cEmail = value.cEmail;
        this.sellerName = value.sellerName;
        this.cPrinterId = value.cPrinterId;
        this.buyerName = value.buyerName;
        this.cPosRemark_3 = value.cPosRemark_3;
        this.cPosRemark_4 = value.cPosRemark_4;
        this.sellerCustomerNumber = value.sellerCustomerNumber;
        this.buyerCustomerNumber = value.buyerCustomerNumber;
        this.buyerAddress = value.buyerAddress;
        this.sellerPersonInCharge = value.sellerPersonInCharge;
        this.buyerPersonInCharge = value.buyerPersonInCharge;
        this.bondedAreaConfirm = value.bondedAreaConfirm;
        this.b2bFlag = value.b2bFlag;
        this.sellerAddress = value.sellerAddress;
        this.sellerTelephoneNumber = value.sellerTelephoneNumber;
        this.sellerFacsimileNumber = value.sellerFacsimileNumber;
        this.sellerEmailAddress = value.sellerEmailAddress;
        this.sellerRoleRemark = value.sellerRoleRemark;
        this.buyerTelephoneNumber = value.buyerTelephoneNumber;
        this.buyerFacsimileNumber = value.buyerFacsimileNumber;
        this.buyerEmailAddress = value.buyerEmailAddress;
        this.buyerRoleRemark = value.buyerRoleRemark;
        this.syncCompanyKey = value.syncCompanyKey;
        this.syncPrintMark = value.syncPrintMark;
        this.syncPrintUser = value.syncPrintUser;
        this.syncPrintDate = value.syncPrintDate;
        this.confirmStatus = value.confirmStatus;
        this.allowanceCount = value.allowanceCount;
        this.acceptStatus = value.acceptStatus;
        this.migType = value.migType;
        this.uploadStatus = value.uploadStatus;
        this.uniqueNumber = value.uniqueNumber;
        this.source = value.source;
    }

    public InvoiceMain(
        Long          invoiceId,
        String        cPrinterNo,
        String        invoiceNumber,
        String        invoiceDate,
        String        invoiceTime,
        String        seller,
        String        buyer,
        String        checkNumber,
        String        buyerRemark,
        String        mainRemark,
        String        customsClearanceMark,
        String        category,
        String        relateNumber,
        String        invoiceType,
        String        groupMark,
        String        donateMark,
        String        carrierType,
        String        carrierId1,
        String        carrierId2,
        String        printMark,
        String        npoban,
        String        randomNumber,
        BigDecimal    salesAmount,
        BigDecimal    freeTaxSalesAmount,
        BigDecimal    zeroTaxSalesAmount,
        String        taxType,
        Float         taxRate,
        BigDecimal    taxAmount,
        BigDecimal    totalAmount,
        BigDecimal    discountAmount,
        BigDecimal    originalCurrencyAmount,
        Float         exchangeRate,
        String        currency,
        String        cPhoneCode,
        String        cNaturalPerson,
        String        cMemberType,
        String        cMemberNumber,
        Integer       cInvoiceStatus,
        String        cPosRemark1,
        String        cPosRemark2,
        String        cKey,
        Integer       creatorId,
        LocalDateTime createDate,
        Integer       modifierId,
        LocalDateTime modifyDate,
        String        cYearMonth,
        Integer       assignId,
        String        cEmail,
        String        sellerName,
        Integer       cPrinterId,
        String        buyerName,
        String        cPosRemark_3,
        String        cPosRemark_4,
        String        sellerCustomerNumber,
        String        buyerCustomerNumber,
        String        buyerAddress,
        String        sellerPersonInCharge,
        String        buyerPersonInCharge,
        String        bondedAreaConfirm,
        String        b2bFlag,
        String        sellerAddress,
        String        sellerTelephoneNumber,
        String        sellerFacsimileNumber,
        String        sellerEmailAddress,
        String        sellerRoleRemark,
        String        buyerTelephoneNumber,
        String        buyerFacsimileNumber,
        String        buyerEmailAddress,
        String        buyerRoleRemark,
        String        syncCompanyKey,
        Boolean       syncPrintMark,
        Integer       syncPrintUser,
        LocalDateTime syncPrintDate,
        Integer       confirmStatus,
        Integer       allowanceCount,
        Integer       acceptStatus,
        String        migType,
        String        uploadStatus,
        String        uniqueNumber,
        String        source
    ) {
        this.invoiceId = invoiceId;
        this.cPrinterNo = cPrinterNo;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.invoiceTime = invoiceTime;
        this.seller = seller;
        this.buyer = buyer;
        this.checkNumber = checkNumber;
        this.buyerRemark = buyerRemark;
        this.mainRemark = mainRemark;
        this.customsClearanceMark = customsClearanceMark;
        this.category = category;
        this.relateNumber = relateNumber;
        this.invoiceType = invoiceType;
        this.groupMark = groupMark;
        this.donateMark = donateMark;
        this.carrierType = carrierType;
        this.carrierId1 = carrierId1;
        this.carrierId2 = carrierId2;
        this.printMark = printMark;
        this.npoban = npoban;
        this.randomNumber = randomNumber;
        this.salesAmount = salesAmount;
        this.freeTaxSalesAmount = freeTaxSalesAmount;
        this.zeroTaxSalesAmount = zeroTaxSalesAmount;
        this.taxType = taxType;
        this.taxRate = taxRate;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.originalCurrencyAmount = originalCurrencyAmount;
        this.exchangeRate = exchangeRate;
        this.currency = currency;
        this.cPhoneCode = cPhoneCode;
        this.cNaturalPerson = cNaturalPerson;
        this.cMemberType = cMemberType;
        this.cMemberNumber = cMemberNumber;
        this.cInvoiceStatus = cInvoiceStatus;
        this.cPosRemark1 = cPosRemark1;
        this.cPosRemark2 = cPosRemark2;
        this.cKey = cKey;
        this.creatorId = creatorId;
        this.createDate = createDate;
        this.modifierId = modifierId;
        this.modifyDate = modifyDate;
        this.cYearMonth = cYearMonth;
        this.assignId = assignId;
        this.cEmail = cEmail;
        this.sellerName = sellerName;
        this.cPrinterId = cPrinterId;
        this.buyerName = buyerName;
        this.cPosRemark_3 = cPosRemark_3;
        this.cPosRemark_4 = cPosRemark_4;
        this.sellerCustomerNumber = sellerCustomerNumber;
        this.buyerCustomerNumber = buyerCustomerNumber;
        this.buyerAddress = buyerAddress;
        this.sellerPersonInCharge = sellerPersonInCharge;
        this.buyerPersonInCharge = buyerPersonInCharge;
        this.bondedAreaConfirm = bondedAreaConfirm;
        this.b2bFlag = b2bFlag;
        this.sellerAddress = sellerAddress;
        this.sellerTelephoneNumber = sellerTelephoneNumber;
        this.sellerFacsimileNumber = sellerFacsimileNumber;
        this.sellerEmailAddress = sellerEmailAddress;
        this.sellerRoleRemark = sellerRoleRemark;
        this.buyerTelephoneNumber = buyerTelephoneNumber;
        this.buyerFacsimileNumber = buyerFacsimileNumber;
        this.buyerEmailAddress = buyerEmailAddress;
        this.buyerRoleRemark = buyerRoleRemark;
        this.syncCompanyKey = syncCompanyKey;
        this.syncPrintMark = syncPrintMark;
        this.syncPrintUser = syncPrintUser;
        this.syncPrintDate = syncPrintDate;
        this.confirmStatus = confirmStatus;
        this.allowanceCount = allowanceCount;
        this.acceptStatus = acceptStatus;
        this.migType = migType;
        this.uploadStatus = uploadStatus;
        this.uniqueNumber = uniqueNumber;
        this.source = source;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id", nullable = false, precision = 64)
    public Long getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Column(name = "c_printer_no", length = 30)
    @Size(max = 30)
    public String getCPrinterNo() {
        return this.cPrinterNo;
    }

    public void setCPrinterNo(String cPrinterNo) {
        this.cPrinterNo = cPrinterNo;
    }

    @Column(name = "invoice_number", length = 10)
    @Size(max = 10)
    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @Column(name = "invoice_date", length = 8)
    @Size(max = 8)
    public String getInvoiceDate() {
        return this.invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @Column(name = "invoice_time", length = 8)
    @Size(max = 8)
    public String getInvoiceTime() {
        return this.invoiceTime;
    }

    public void setInvoiceTime(String invoiceTime) {
        this.invoiceTime = invoiceTime;
    }

    @Column(name = "seller", length = 8)
    @Size(max = 8)
    public String getSeller() {
        return this.seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    @Column(name = "buyer", length = 10)
    @Size(max = 10)
    public String getBuyer() {
        return this.buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    @Column(name = "check_number", length = 1)
    @Size(max = 1)
    public String getCheckNumber() {
        return this.checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    @Column(name = "buyer_remark", length = 1)
    @Size(max = 1)
    public String getBuyerRemark() {
        return this.buyerRemark;
    }

    public void setBuyerRemark(String buyerRemark) {
        this.buyerRemark = buyerRemark;
    }

    @Column(name = "main_remark", length = 200)
    @Size(max = 200)
    public String getMainRemark() {
        return this.mainRemark;
    }

    public void setMainRemark(String mainRemark) {
        this.mainRemark = mainRemark;
    }

    @Column(name = "customs_clearance_mark", length = 1)
    @Size(max = 1)
    public String getCustomsClearanceMark() {
        return this.customsClearanceMark;
    }

    public void setCustomsClearanceMark(String customsClearanceMark) {
        this.customsClearanceMark = customsClearanceMark;
    }

    @Column(name = "category", length = 2)
    @Size(max = 2)
    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "relate_number", length = 20)
    @Size(max = 20)
    public String getRelateNumber() {
        return this.relateNumber;
    }

    public void setRelateNumber(String relateNumber) {
        this.relateNumber = relateNumber;
    }

    @Column(name = "invoice_type", length = 2)
    @Size(max = 2)
    public String getInvoiceType() {
        return this.invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    @Column(name = "group_mark", length = 1)
    @Size(max = 1)
    public String getGroupMark() {
        return this.groupMark;
    }

    public void setGroupMark(String groupMark) {
        this.groupMark = groupMark;
    }

    @Column(name = "donate_mark", length = 1)
    @Size(max = 1)
    public String getDonateMark() {
        return this.donateMark;
    }

    public void setDonateMark(String donateMark) {
        this.donateMark = donateMark;
    }

    @Column(name = "carrier_type", length = 6)
    @Size(max = 6)
    public String getCarrierType() {
        return this.carrierType;
    }

    public void setCarrierType(String carrierType) {
        this.carrierType = carrierType;
    }

    @Column(name = "carrier_id1", length = 64)
    @Size(max = 64)
    public String getCarrierId1() {
        return this.carrierId1;
    }

    public void setCarrierId1(String carrierId1) {
        this.carrierId1 = carrierId1;
    }

    @Column(name = "carrier_id2", length = 64)
    @Size(max = 64)
    public String getCarrierId2() {
        return this.carrierId2;
    }

    public void setCarrierId2(String carrierId2) {
        this.carrierId2 = carrierId2;
    }

    @Column(name = "print_mark", length = 1)
    @Size(max = 1)
    public String getPrintMark() {
        return this.printMark;
    }

    public void setPrintMark(String printMark) {
        this.printMark = printMark;
    }

    @Column(name = "npoban", length = 10)
    @Size(max = 10)
    public String getNpoban() {
        return this.npoban;
    }

    public void setNpoban(String npoban) {
        this.npoban = npoban;
    }

    @Column(name = "random_number", length = 4)
    @Size(max = 4)
    public String getRandomNumber() {
        return this.randomNumber;
    }

    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }

    @Column(name = "sales_amount", precision = 53, scale = 4)
    public BigDecimal getSalesAmount() {
        return this.salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount) {
        this.salesAmount = salesAmount;
    }

    @Column(name = "free_tax_sales_amount", precision = 53, scale = 4)
    public BigDecimal getFreeTaxSalesAmount() {
        return this.freeTaxSalesAmount;
    }

    public void setFreeTaxSalesAmount(BigDecimal freeTaxSalesAmount) {
        this.freeTaxSalesAmount = freeTaxSalesAmount;
    }

    @Column(name = "zero_tax_sales_amount", precision = 53, scale = 4)
    public BigDecimal getZeroTaxSalesAmount() {
        return this.zeroTaxSalesAmount;
    }

    public void setZeroTaxSalesAmount(BigDecimal zeroTaxSalesAmount) {
        this.zeroTaxSalesAmount = zeroTaxSalesAmount;
    }

    @Column(name = "tax_type", length = 1)
    @Size(max = 1)
    public String getTaxType() {
        return this.taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    @Column(name = "tax_rate", precision = 24)
    public Float getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(Float taxRate) {
        this.taxRate = taxRate;
    }

    @Column(name = "tax_amount", precision = 53, scale = 4)
    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    @Column(name = "total_amount", precision = 53, scale = 4)
    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Column(name = "discount_amount", precision = 53, scale = 4)
    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Column(name = "original_currency_amount", precision = 53, scale = 4)
    public BigDecimal getOriginalCurrencyAmount() {
        return this.originalCurrencyAmount;
    }

    public void setOriginalCurrencyAmount(BigDecimal originalCurrencyAmount) {
        this.originalCurrencyAmount = originalCurrencyAmount;
    }

    @Column(name = "exchange_rate", precision = 24)
    public Float getExchangeRate() {
        return this.exchangeRate;
    }

    public void setExchangeRate(Float exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Column(name = "currency", length = 3)
    @Size(max = 3)
    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Column(name = "c_phone_code", length = 8)
    @Size(max = 8)
    public String getCPhoneCode() {
        return this.cPhoneCode;
    }

    public void setCPhoneCode(String cPhoneCode) {
        this.cPhoneCode = cPhoneCode;
    }

    @Column(name = "c_natural_person", length = 16)
    @Size(max = 16)
    public String getCNaturalPerson() {
        return this.cNaturalPerson;
    }

    public void setCNaturalPerson(String cNaturalPerson) {
        this.cNaturalPerson = cNaturalPerson;
    }

    @Column(name = "c_member_type", length = 6)
    @Size(max = 6)
    public String getCMemberType() {
        return this.cMemberType;
    }

    public void setCMemberType(String cMemberType) {
        this.cMemberType = cMemberType;
    }

    @Column(name = "c_member_number", length = 20)
    @Size(max = 20)
    public String getCMemberNumber() {
        return this.cMemberNumber;
    }

    public void setCMemberNumber(String cMemberNumber) {
        this.cMemberNumber = cMemberNumber;
    }

    @Column(name = "c_invoice_status", precision = 32)
    public Integer getCInvoiceStatus() {
        return this.cInvoiceStatus;
    }

    public void setCInvoiceStatus(Integer cInvoiceStatus) {
        this.cInvoiceStatus = cInvoiceStatus;
    }

    @Column(name = "c_pos_remark1", length = 200)
    @Size(max = 200)
    public String getCPosRemark1() {
        return this.cPosRemark1;
    }

    public void setCPosRemark1(String cPosRemark1) {
        this.cPosRemark1 = cPosRemark1;
    }

    @Column(name = "c_pos_remark2", length = 200)
    @Size(max = 200)
    public String getCPosRemark2() {
        return this.cPosRemark2;
    }

    public void setCPosRemark2(String cPosRemark2) {
        this.cPosRemark2 = cPosRemark2;
    }

    @Column(name = "c_key", length = 50)
    @Size(max = 50)
    public String getCKey() {
        return this.cKey;
    }

    public void setCKey(String cKey) {
        this.cKey = cKey;
    }

    @Column(name = "creator_id", precision = 32)
    public Integer getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    @Column(name = "create_date")
    public LocalDateTime getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Column(name = "modifier_id", precision = 32)
    public Integer getModifierId() {
        return this.modifierId;
    }

    public void setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
    }

    @Column(name = "modify_date")
    public LocalDateTime getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Column(name = "c_year_month", nullable = false, length = 5)
    @NotNull
    @Size(max = 5)
    public String getCYearMonth() {
        return this.cYearMonth;
    }

    public void setCYearMonth(String cYearMonth) {
        this.cYearMonth = cYearMonth;
    }

    @Column(name = "assign_id", precision = 32)
    public Integer getAssignId() {
        return this.assignId;
    }

    public void setAssignId(Integer assignId) {
        this.assignId = assignId;
    }

    @Column(name = "c_email", length = 50)
    @Size(max = 50)
    public String getCEmail() {
        return this.cEmail;
    }

    public void setCEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    @Column(name = "seller_name", length = 60)
    @Size(max = 60)
    public String getSellerName() {
        return this.sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    @Column(name = "c_printer_id", precision = 32)
    public Integer getCPrinterId() {
        return this.cPrinterId;
    }

    public void setCPrinterId(Integer cPrinterId) {
        this.cPrinterId = cPrinterId;
    }

    @Column(name = "buyer_name", length = 60)
    @Size(max = 60)
    public String getBuyerName() {
        return this.buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    @Column(name = "c_pos_remark_3", length = 200)
    @Size(max = 200)
    public String getCPosRemark_3() {
        return this.cPosRemark_3;
    }

    public void setCPosRemark_3(String cPosRemark_3) {
        this.cPosRemark_3 = cPosRemark_3;
    }

    @Column(name = "c_pos_remark_4", length = 200)
    @Size(max = 200)
    public String getCPosRemark_4() {
        return this.cPosRemark_4;
    }

    public void setCPosRemark_4(String cPosRemark_4) {
        this.cPosRemark_4 = cPosRemark_4;
    }

    @Column(name = "seller_customer_number", length = 20)
    @Size(max = 20)
    public String getSellerCustomerNumber() {
        return this.sellerCustomerNumber;
    }

    public void setSellerCustomerNumber(String sellerCustomerNumber) {
        this.sellerCustomerNumber = sellerCustomerNumber;
    }

    @Column(name = "buyer_customer_number", length = 20)
    @Size(max = 20)
    public String getBuyerCustomerNumber() {
        return this.buyerCustomerNumber;
    }

    public void setBuyerCustomerNumber(String buyerCustomerNumber) {
        this.buyerCustomerNumber = buyerCustomerNumber;
    }

    @Column(name = "buyer_address", length = 200)
    @Size(max = 200)
    public String getBuyerAddress() {
        return this.buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    @Column(name = "seller_person_in_charge", length = 60)
    @Size(max = 60)
    public String getSellerPersonInCharge() {
        return this.sellerPersonInCharge;
    }

    public void setSellerPersonInCharge(String sellerPersonInCharge) {
        this.sellerPersonInCharge = sellerPersonInCharge;
    }

    @Column(name = "buyer_person_in_charge", length = 60)
    @Size(max = 60)
    public String getBuyerPersonInCharge() {
        return this.buyerPersonInCharge;
    }

    public void setBuyerPersonInCharge(String buyerPersonInCharge) {
        this.buyerPersonInCharge = buyerPersonInCharge;
    }

    @Column(name = "bonded_area_confirm", length = 1)
    @Size(max = 1)
    public String getBondedAreaConfirm() {
        return this.bondedAreaConfirm;
    }

    public void setBondedAreaConfirm(String bondedAreaConfirm) {
        this.bondedAreaConfirm = bondedAreaConfirm;
    }

    @Column(name = "b2b_flag", length = 1)
    @Size(max = 1)
    public String getB2bFlag() {
        return this.b2bFlag;
    }

    public void setB2bFlag(String b2bFlag) {
        this.b2bFlag = b2bFlag;
    }

    @Column(name = "seller_address", length = 200)
    @Size(max = 200)
    public String getSellerAddress() {
        return this.sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    @Column(name = "seller_telephone_number", length = 26)
    @Size(max = 26)
    public String getSellerTelephoneNumber() {
        return this.sellerTelephoneNumber;
    }

    public void setSellerTelephoneNumber(String sellerTelephoneNumber) {
        this.sellerTelephoneNumber = sellerTelephoneNumber;
    }

    @Column(name = "seller_facsimile_number", length = 26)
    @Size(max = 26)
    public String getSellerFacsimileNumber() {
        return this.sellerFacsimileNumber;
    }

    public void setSellerFacsimileNumber(String sellerFacsimileNumber) {
        this.sellerFacsimileNumber = sellerFacsimileNumber;
    }

    @Column(name = "seller_email_address", length = 80)
    @Size(max = 80)
    public String getSellerEmailAddress() {
        return this.sellerEmailAddress;
    }

    public void setSellerEmailAddress(String sellerEmailAddress) {
        this.sellerEmailAddress = sellerEmailAddress;
    }

    @Column(name = "seller_role_remark", length = 40)
    @Size(max = 40)
    public String getSellerRoleRemark() {
        return this.sellerRoleRemark;
    }

    public void setSellerRoleRemark(String sellerRoleRemark) {
        this.sellerRoleRemark = sellerRoleRemark;
    }

    @Column(name = "buyer_telephone_number", length = 26)
    @Size(max = 26)
    public String getBuyerTelephoneNumber() {
        return this.buyerTelephoneNumber;
    }

    public void setBuyerTelephoneNumber(String buyerTelephoneNumber) {
        this.buyerTelephoneNumber = buyerTelephoneNumber;
    }

    @Column(name = "buyer_facsimile_number", length = 26)
    @Size(max = 26)
    public String getBuyerFacsimileNumber() {
        return this.buyerFacsimileNumber;
    }

    public void setBuyerFacsimileNumber(String buyerFacsimileNumber) {
        this.buyerFacsimileNumber = buyerFacsimileNumber;
    }

    @Column(name = "buyer_email_address", length = 80)
    @Size(max = 80)
    public String getBuyerEmailAddress() {
        return this.buyerEmailAddress;
    }

    public void setBuyerEmailAddress(String buyerEmailAddress) {
        this.buyerEmailAddress = buyerEmailAddress;
    }

    @Column(name = "buyer_role_remark", length = 40)
    @Size(max = 40)
    public String getBuyerRoleRemark() {
        return this.buyerRoleRemark;
    }

    public void setBuyerRoleRemark(String buyerRoleRemark) {
        this.buyerRoleRemark = buyerRoleRemark;
    }

    @Column(name = "sync_company_key", length = 50)
    @Size(max = 50)
    public String getSyncCompanyKey() {
        return this.syncCompanyKey;
    }

    public void setSyncCompanyKey(String syncCompanyKey) {
        this.syncCompanyKey = syncCompanyKey;
    }

    @Column(name = "sync_print_mark")
    public Boolean getSyncPrintMark() {
        return this.syncPrintMark;
    }

    public void setSyncPrintMark(Boolean syncPrintMark) {
        this.syncPrintMark = syncPrintMark;
    }

    @Column(name = "sync_print_user", precision = 32)
    public Integer getSyncPrintUser() {
        return this.syncPrintUser;
    }

    public void setSyncPrintUser(Integer syncPrintUser) {
        this.syncPrintUser = syncPrintUser;
    }

    @Column(name = "sync_print_date")
    public LocalDateTime getSyncPrintDate() {
        return this.syncPrintDate;
    }

    public void setSyncPrintDate(LocalDateTime syncPrintDate) {
        this.syncPrintDate = syncPrintDate;
    }

    @Column(name = "confirm_status", precision = 32)
    public Integer getConfirmStatus() {
        return this.confirmStatus;
    }

    public void setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    @Column(name = "allowance_count", precision = 32)
    public Integer getAllowanceCount() {
        return this.allowanceCount;
    }

    public void setAllowanceCount(Integer allowanceCount) {
        this.allowanceCount = allowanceCount;
    }

    @Column(name = "accept_status", precision = 32)
    public Integer getAcceptStatus() {
        return this.acceptStatus;
    }

    public void setAcceptStatus(Integer acceptStatus) {
        this.acceptStatus = acceptStatus;
    }

    @Column(name = "mig_type", length = 8)
    @Size(max = 8)
    public String getMigType() {
        return this.migType;
    }

    public void setMigType(String migType) {
        this.migType = migType;
    }

    @Column(name = "upload_status", length = 1)
    @Size(max = 1)
    public String getUploadStatus() {
        return this.uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    @Column(name = "unique_number", length = 60)
    @Size(max = 60)
    public String getUniqueNumber() {
        return this.uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    @Column(name = "source", length = 60)
    @Size(max = 60)
    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("InvoiceMain (");

        sb.append(invoiceId);
        sb.append(", ").append(cPrinterNo);
        sb.append(", ").append(invoiceNumber);
        sb.append(", ").append(invoiceDate);
        sb.append(", ").append(invoiceTime);
        sb.append(", ").append(seller);
        sb.append(", ").append(buyer);
        sb.append(", ").append(checkNumber);
        sb.append(", ").append(buyerRemark);
        sb.append(", ").append(mainRemark);
        sb.append(", ").append(customsClearanceMark);
        sb.append(", ").append(category);
        sb.append(", ").append(relateNumber);
        sb.append(", ").append(invoiceType);
        sb.append(", ").append(groupMark);
        sb.append(", ").append(donateMark);
        sb.append(", ").append(carrierType);
        sb.append(", ").append(carrierId1);
        sb.append(", ").append(carrierId2);
        sb.append(", ").append(printMark);
        sb.append(", ").append(npoban);
        sb.append(", ").append(randomNumber);
        sb.append(", ").append(salesAmount);
        sb.append(", ").append(freeTaxSalesAmount);
        sb.append(", ").append(zeroTaxSalesAmount);
        sb.append(", ").append(taxType);
        sb.append(", ").append(taxRate);
        sb.append(", ").append(taxAmount);
        sb.append(", ").append(totalAmount);
        sb.append(", ").append(discountAmount);
        sb.append(", ").append(originalCurrencyAmount);
        sb.append(", ").append(exchangeRate);
        sb.append(", ").append(currency);
        sb.append(", ").append(cPhoneCode);
        sb.append(", ").append(cNaturalPerson);
        sb.append(", ").append(cMemberType);
        sb.append(", ").append(cMemberNumber);
        sb.append(", ").append(cInvoiceStatus);
        sb.append(", ").append(cPosRemark1);
        sb.append(", ").append(cPosRemark2);
        sb.append(", ").append(cKey);
        sb.append(", ").append(creatorId);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifierId);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(cYearMonth);
        sb.append(", ").append(assignId);
        sb.append(", ").append(cEmail);
        sb.append(", ").append(sellerName);
        sb.append(", ").append(cPrinterId);
        sb.append(", ").append(buyerName);
        sb.append(", ").append(cPosRemark_3);
        sb.append(", ").append(cPosRemark_4);
        sb.append(", ").append(sellerCustomerNumber);
        sb.append(", ").append(buyerCustomerNumber);
        sb.append(", ").append(buyerAddress);
        sb.append(", ").append(sellerPersonInCharge);
        sb.append(", ").append(buyerPersonInCharge);
        sb.append(", ").append(bondedAreaConfirm);
        sb.append(", ").append(b2bFlag);
        sb.append(", ").append(sellerAddress);
        sb.append(", ").append(sellerTelephoneNumber);
        sb.append(", ").append(sellerFacsimileNumber);
        sb.append(", ").append(sellerEmailAddress);
        sb.append(", ").append(sellerRoleRemark);
        sb.append(", ").append(buyerTelephoneNumber);
        sb.append(", ").append(buyerFacsimileNumber);
        sb.append(", ").append(buyerEmailAddress);
        sb.append(", ").append(buyerRoleRemark);
        sb.append(", ").append(syncCompanyKey);
        sb.append(", ").append(syncPrintMark);
        sb.append(", ").append(syncPrintUser);
        sb.append(", ").append(syncPrintDate);
        sb.append(", ").append(confirmStatus);
        sb.append(", ").append(allowanceCount);
        sb.append(", ").append(acceptStatus);
        sb.append(", ").append(migType);
        sb.append(", ").append(uploadStatus);
        sb.append(", ").append(uniqueNumber);
        sb.append(", ").append(source);

        sb.append(")");
        return sb.toString();
    }
}
