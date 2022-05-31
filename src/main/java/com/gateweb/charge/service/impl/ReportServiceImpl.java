package com.gateweb.charge.service.impl;

import com.gate.web.beans.CashDetailBean;
import com.gate.web.beans.CashMasterBean;
import com.gate.web.beans.InvoiceExcelBean;
import com.gate.web.beans.InvoiceExcelPrepareDataBean;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.enumeration.ReportType;
import com.gateweb.charge.report.bean.InvoiceBatchRecord;
import com.gateweb.charge.report.bean.ScsbConvenientStoreMain;
import com.gateweb.charge.report.bean.ScsbConvenientStoreSlave;
import com.gateweb.charge.report.component.CurrentlyNotExpireContractReportDataGenerator;
import com.gateweb.charge.service.ProductService;
import com.gateweb.charge.service.ReportService;
import com.gateweb.orm.charge.entity.*;
import com.gateweb.orm.charge.repository.*;
import com.gateweb.utils.CsvUtils;
import com.gateweb.utils.JxlsUtils;
import org.apache.commons.lang3.StringUtils;
import org.jxls.common.CellRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    BillRepository billRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    BillFetchViewRepository billFetchViewRepository;
    @Autowired
    ChargePackageRepository chargePackageRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    PackageRefRepository modeReferenceRepository;
    @Autowired
    DeductRepository deductPurchaseRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    ProductService productService;
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    BillReportRelationRepository billReportRelationRepository;
    @Autowired
    CurrentlyNotExpireContractReportDataGenerator currentlyNotExpireContractReportDataGenerator;

    private String SCSB_JXLS_TEMPLATE = "template/jxls/out_jxls_template.xls";
    private String SCSB_JXLS_TEMPLATE_CONFIG = "template/jxls/out_jxls_template_configuration.xml";
    private static final String TEMPLATE_EXCEL_DOWNLOAD_INVOICE = "template/jxls/invoice_jxls_template.xls";

    @Override
    public void genScsbConvenientStoreFile(OutputStream outputStream, Collection<Bill> billList) throws IOException {
        String batchId = UUID.randomUUID().toString();
        Resource templateResource = new ClassPathResource(SCSB_JXLS_TEMPLATE);
        try (FileInputStream templateInputStream = new FileInputStream(templateResource.getFile())) {
            try (FileInputStream configurationXmlInputStream
                         = new FileInputStream(new ClassPathResource(SCSB_JXLS_TEMPLATE_CONFIG).getFile())) {
                List<ScsbConvenientStoreMain> scsbConvenientStoreMainList = new ArrayList<>();
                billList.stream().forEach(bill -> {
                    scsbConvenientStoreMainList.addAll(generateScsbConvenientStoreBean(bill));
                });
                Map<String, Object> excelDataMap = genExcelDataMap(scsbConvenientStoreMainList);
                Map<String, Object> contextMap = new HashMap<>();
                contextMap.put("headers", excelDataMap.get("header"));
                contextMap.put("rows", excelDataMap.get("data"));
                JxlsUtils.processTemplate(contextMap, templateInputStream, outputStream, configurationXmlInputStream, new CellRef("Template!A1"));
                //更新這bill的convenient_store_collect_report_id
                Report report = new Report();
                report.setReportId(batchId);
                report.setCreateDate(LocalDateTime.now());
                report.setReportType(ReportType.SCSB_CS_REPORT);
                reportRepository.save(report);
                billList.stream().forEach(bill -> {
                    billRepository.save(bill);
                });
            } catch (Exception ex) {
                logger.error("configurationXmlInputStream:{}", ex.getMessage());
            }
        } catch (Exception ex) {
            logger.error("templateInputStream:{}", ex.getMessage());
        }
    }

    /**
     * 根據telecom billing 的定義，只有bill會進行payment_request
     * 客戶於日期區間要繳費用為零元，不處理
     */
    private List<ScsbConvenientStoreMain> generateScsbConvenientStoreBean(Bill bill) {
        List<ScsbConvenientStoreMain> resultList = new ArrayList<>();
        Set<BillingItem> billingItemSet = new HashSet<>(billingItemRepository.findByBillId(bill.getBillId()));
        Optional<Company> companyOptional = companyRepository.findByCompanyId(bill.getCompanyId().intValue());
        if (bill.getTaxExcludedAmount().compareTo(BigDecimal.ZERO) > 0
                && companyOptional.isPresent()) {
            ScsbConvenientStoreMain scsbConvenientStoreMain = new ScsbConvenientStoreMain();
            scsbConvenientStoreMain.setCompanyName(companyOptional.get().getName());
            scsbConvenientStoreMain.setBusinessNo(companyOptional.get().getBusinessNo());
            for (BillingItem billingItem : billingItemSet) {
                String itemName = productService.getProductName(billingItem);
                ScsbConvenientStoreSlave scsbConvenientStoreSlave = new ScsbConvenientStoreSlave();
                scsbConvenientStoreSlave.setItemName(itemName);
                scsbConvenientStoreSlave.setTaxInclusivePrice(billingItem.getTaxIncludedAmount());
                scsbConvenientStoreMain.getSlaveList().add(scsbConvenientStoreSlave);
            }
            resultList.add(scsbConvenientStoreMain);
        }
        return resultList;
    }

    private Map<String, Object> genExcelDataMap(List<ScsbConvenientStoreMain> scsbConvenientStoreMainList) {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> headerList = new ArrayList<>();
        List<List<Object>> dataList = new ArrayList<>();

        //寫死固定欄位
        headerList.add("*編號");
        headerList.add("");
        headerList.add("");
        headerList.add("");
        headerList.add("");
        headerList.add("姓名");
        headerList.add("客戶辨識碼");

        //加入動態欄位
        //divide and conquer
        //先跑header的部份
        List<String> packageList = new ArrayList<>();
        for (ScsbConvenientStoreMain scsbConvenientStoreMain : scsbConvenientStoreMainList) {
            for (ScsbConvenientStoreSlave scsbConvenientStoreSlave : scsbConvenientStoreMain.getSlaveList()) {
                //分兩次，先加入欄位的map
                if (!packageList.contains(scsbConvenientStoreSlave.getItemName())) {
                    packageList.add(scsbConvenientStoreSlave.getItemName());
                    //寫入header
                    headerList.add(scsbConvenientStoreSlave.getItemName());
                }
            }
        }

        for (ScsbConvenientStoreMain scsbConvenientStoreMain : scsbConvenientStoreMainList) {
            List<Object> detailValueList = new ArrayList<>();
            detailValueList.add(scsbConvenientStoreMain.getBusinessNo());
            detailValueList.add(scsbConvenientStoreMain.getTaxInclusivePrice());
            detailValueList.add("");
            detailValueList.add("");
            detailValueList.add("");
            detailValueList.add(scsbConvenientStoreMain.getCompanyName());
            detailValueList.add(scsbConvenientStoreMain.getBusinessNo());
            //寫value
            BigDecimal taxInclusiveAmount = BigDecimal.ZERO;
            String currentPackageName = "";
            for (ScsbConvenientStoreSlave scsbConvenientStoreSlave : scsbConvenientStoreMain.getSlaveList()) {
                currentPackageName = scsbConvenientStoreSlave.getItemName();
                taxInclusiveAmount = taxInclusiveAmount.add(scsbConvenientStoreSlave.getTaxInclusivePrice());
            }
            for (String packageName : packageList) {
                if (currentPackageName.equals(packageName)) {
                    detailValueList.add(taxInclusiveAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
                } else {
                    detailValueList.add(0);
                }
            }
            dataList.add(detailValueList);
        }
        resultMap.put("header", headerList);
        resultMap.put("data", dataList);
        return resultMap;
    }

    public String genCurrentInvoiceDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
        return sdf.format(new Date());
    }

    public InvoiceExcelBean genInvoiceExcelBean(int invoiceIndex, int itemIndex, String businessNo, BillingItem billingItem) {
        InvoiceExcelBean bean = new InvoiceExcelBean();
        bean.setInvoiceIndex(invoiceIndex); //發票張數
        bean.setInvoiceDate(genCurrentInvoiceDateString()); //發票日期
        bean.setItemIndex(itemIndex); //品序號
        bean.setItemName(productService.getProductName(billingItem)); //發票品名
        bean.setItemCnt(1); //數量
        bean.setUnitPrice(billingItem.getTaxIncludedAmount()); //單價
        bean.setTaxType(1); //課稅別
        bean.setTaxRate(0.05d); //稅率
        bean.setBusinessNo(businessNo); //買方統編
        return bean;
    }

    public List<InvoiceExcelPrepareDataBean> genInvoiceExcelPrepareDataBean(List<Bill> billList) {
        List<InvoiceExcelPrepareDataBean> invoiceExcelPrepareDataBeanList = new ArrayList<>();
        HashMap<Long, Company> companyMap = new HashMap<>();
        billList.stream().forEach(bill -> {
            InvoiceExcelPrepareDataBean invoiceExcelPrepareDataBean = new InvoiceExcelPrepareDataBean();
            if (!companyMap.containsKey(bill.getCompanyId())) {
                Optional<Company> companyOptional = companyRepository.findByCompanyId(bill.getCompanyId().intValue());
                if (companyOptional.isPresent()) {
                    companyMap.put(bill.getCompanyId(), companyOptional.get());
                }
                Set<BillingItem> billingItemSet = new HashSet<>(billingItemRepository.findByBillId(bill.getBillId()));
                invoiceExcelPrepareDataBean.getBillingItemList().addAll(billingItemSet);
            }
            invoiceExcelPrepareDataBean.setCompany(companyMap.get(bill.getCompanyId()));
            invoiceExcelPrepareDataBean.setBill(bill);
            invoiceExcelPrepareDataBeanList.add(invoiceExcelPrepareDataBean);
        });
        return invoiceExcelPrepareDataBeanList;
    }

    public List<InvoiceExcelBean> genInvoiceExcelBeanByPrepareDataBean(List<InvoiceExcelPrepareDataBean> prepareDataBeanList) {
        List<InvoiceExcelBean> invoiceExcelBeanList = new ArrayList<>();
        int invoiceIndex = 1;
        for (InvoiceExcelPrepareDataBean invoiceExcelPrepareDataBean : prepareDataBeanList) {
            int itemIndex = 1;
            HashMap<String, InvoiceExcelBean> mergeMap = new HashMap<>();
            for (BillingItem billingItem : invoiceExcelPrepareDataBean.getBillingItemList()) {
                InvoiceExcelBean bean = genInvoiceExcelBean(
                        invoiceIndex
                        , itemIndex
                        , invoiceExcelPrepareDataBean.getCompany().getBusinessNo()
                        , billingItem
                );
                if (mergeMap.containsKey(bean.getItemName())) {
                    //合併packageRef相同的預繳項目
                    InvoiceExcelBean invoiceExcelBean =
                            mergeInvoiceExcelBean(mergeMap.get(bean.getItemName()), bean);
                    mergeMap.put(bean.getItemName(), invoiceExcelBean);
                } else {
                    mergeMap.put(bean.getItemName(), bean);
                    itemIndex++;
                }
            }
            invoiceExcelBeanList.addAll(
                    mergeMap.values().stream().sorted(
                            Comparator.comparing(InvoiceExcelBean::getItemIndex)).collect(Collectors.toList()
                    )
            );
            //四捨五入
            invoiceExcelBeanList.stream().forEach(invoiceExcelBean -> {
                invoiceExcelBean.setUnitPrice(
                        invoiceExcelBean.getUnitPrice().setScale(2, RoundingMode.HALF_UP)
                );
            });
            invoiceIndex++;
        }
        return invoiceExcelBeanList;
    }

    public InvoiceExcelBean mergeInvoiceExcelBean(InvoiceExcelBean invoiceExcelBean1, InvoiceExcelBean invoiceExcelBean2) {
        InvoiceExcelBean invoiceExcelBean = new InvoiceExcelBean();
        invoiceExcelBean.setBusinessNo(invoiceExcelBean1.getBusinessNo());
        invoiceExcelBean.setInvoiceDate(invoiceExcelBean1.getInvoiceDate());
        invoiceExcelBean.setInvoiceIndex(invoiceExcelBean1.getInvoiceIndex());
        invoiceExcelBean.setItemCnt(1);
        invoiceExcelBean.setUnitPrice(
                invoiceExcelBean1.getUnitPrice().add(invoiceExcelBean2.getUnitPrice())
        );
        invoiceExcelBean.setTaxRate(invoiceExcelBean1.getTaxRate());
        invoiceExcelBean.setTaxType(invoiceExcelBean1.getTaxType());
        invoiceExcelBean.setItemName(invoiceExcelBean1.getItemName());
        invoiceExcelBean.setItemIndex(invoiceExcelBean1.getItemIndex());
        return invoiceExcelBean;
    }

    @Override
    public void genInvoiceExcelFile(OutputStream outputStream, List<Bill> billList) throws FileNotFoundException {
        UUID uuid = UUID.randomUUID();
        List<InvoiceExcelPrepareDataBean> invoiceExcelPrepareDataBeanList = genInvoiceExcelPrepareDataBean(billList);
        List<InvoiceExcelBean> invoiceExcelBeanList = genInvoiceExcelBeanByPrepareDataBean(invoiceExcelPrepareDataBeanList);
        List<InvoiceBatchRecord> invoiceBatchRecordList = genInvoiceBatchRecordList(invoiceExcelBeanList);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("invoiceBatchRecordList", invoiceBatchRecordList);
        FileInputStream templateFileInputStream = new FileInputStream(this.getClass().getResource("/").getPath() + TEMPLATE_EXCEL_DOWNLOAD_INVOICE);
        JxlsUtils.processTemplate(parameterMap, templateFileInputStream, outputStream);
        //更新這bill的convenient_store_collect_report_id
        Report report = new Report();
        report.setReportId(uuid.toString());
        report.setCreateDate(LocalDateTime.now());
        report.setReportType(ReportType.INVOICE_IMPORT_REPORT);
        reportRepository.save(report);
        billList.parallelStream().forEach(bill -> {
            billRepository.save(bill);
            BillReportRelation billReportRelation = new BillReportRelation();
            billReportRelation.setBillId(bill.getBillId());
            billReportRelation.setReportId(uuid.toString());
            billReportRelationRepository.save(billReportRelation);
        });
    }

    @Override
    public void genCurrentlyNotExpireContractReport(OutputStream outputStream) {
        HashSet<Contract> contractHashSet = new HashSet<>(
                contractRepository.findByStatusAndExpirationDateIsNotNullAndExpirationDateAfter(
                        ContractStatus.E, LocalDateTime.now()
                )
        );
        //根據結果取得CompanyMap
        HashMap<Long, Company> companyHashMap = new HashMap<>();
        //根據結果取得PackageMap
        HashMap<Long, ChargePackage> packageHashMap = new HashMap<>();
        contractHashSet.stream().forEach(contract -> {
            if (contract.getCompanyId() != null && !companyHashMap.containsKey(contract.getCompanyId())) {
                Optional<Company> companyOptional = companyRepository.findByCompanyId(contract.getCompanyId().intValue());
                if (companyOptional.isPresent()) {
                    companyHashMap.put(contract.getCompanyId(), companyOptional.get());
                }
            }
            if (contract.getPackageId() != null && !packageHashMap.containsKey(contract.getPackageId())) {
                Optional<ChargePackage> packageOptional = chargePackageRepository.findById(contract.getPackageId());
                if (packageOptional.isPresent()) {
                    packageHashMap.put(contract.getPackageId(), packageOptional.get());
                }
            }
        });
        List<HashMap<String, String>> mapDataList = currentlyNotExpireContractReportDataGenerator.reportDataGen(
                companyHashMap
                , packageHashMap
                , contractHashSet
        );
        List<String> resultList = new ArrayList<>();
        List<String> detailList = CsvUtils.mapListToCsvLineData(
                currentlyNotExpireContractReportDataGenerator.headerListGen()
                , mapDataList
                , ",");
        //add header
        String header = StringUtils.join(
                currentlyNotExpireContractReportDataGenerator.headerListGen()
                , ","
        );
        resultList.add(header);
        resultList.addAll(detailList);

        CsvUtils.stringListToCsvOutputStream(resultList, outputStream);
    }

    @Deprecated
    public Map<String, Object> genCashDataExcelDataMap(List<CashMasterBean> cashMasterBeanList) {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> headerList = new ArrayList<>();
        List<List<Object>> dataList = new ArrayList<>();

        //寫死固定欄位
        headerList.add("*編號");
        headerList.add("");
        headerList.add("");
        headerList.add("");
        headerList.add("");
        headerList.add("姓名");
        headerList.add("客戶辨識碼");

        //加入動態欄位
        //divide and conquer
        //先跑header的部份
        List<String> packageList = new ArrayList<>();
        for (CashMasterBean cashMasterBean : cashMasterBeanList) {
            for (CashDetailBean cashDetailBean : cashMasterBean.getCashDetailList()) {
                Integer chargeId = cashDetailBean.getChargeId();
                Integer cashType = cashDetailBean.getCashType(); //1.月租 2.超額 3.代印代寄 4.加值型 5.儲值 6.預繳
                Integer billType = cashDetailBean.getBillType(); //1.月租 2.級距

                //分兩次，先加入欄位的map
                String packageName = getPackageName(cashType, cashDetailBean.getPackageName());
                if (!packageList.contains(packageName)) {
                    packageList.add(packageName);
                    //寫入header
                    headerList.add(packageName);
                }
            }
        }

        for (CashMasterBean cashMasterBean : cashMasterBeanList) {
            List<Object> detailValueList = new ArrayList<>();
            detailValueList.add(cashMasterBean.getBusinessNo());
            detailValueList.add(cashMasterBean.getInAmount());
            detailValueList.add("");
            detailValueList.add("");
            detailValueList.add("");
            detailValueList.add(cashMasterBean.getCompanyName());
            detailValueList.add(cashMasterBean.getBusinessNo());
            //寫value
            String currentPackageName = "";
            BigDecimal taxInclusiveAmount = BigDecimal.ZERO;
            for (CashDetailBean cashDetailBean : cashMasterBean.getCashDetailList()) {
                currentPackageName = getPackageName(cashDetailBean.getCashType(), cashDetailBean.getPackageName());
                taxInclusiveAmount = taxInclusiveAmount.add(cashDetailBean.getTaxInclusivePrice().setScale(0, BigDecimal.ROUND_HALF_UP));
            }
            for (String packageName : packageList) {
                if (currentPackageName.equals(packageName)) {
                    detailValueList.add(taxInclusiveAmount);
                } else {
                    detailValueList.add(0);
                }
            }
            dataList.add(detailValueList);
        }
        resultMap.put("header", headerList);
        resultMap.put("data", dataList);
        return resultMap;
    }

    public String getPackageName(
            Integer cashType
            , String originalPackageName) {
        String packageName = "";
        if (cashType == 1) { //月租
            packageName = originalPackageName;
        } else if (cashType == 2) { //超額
            packageName = originalPackageName + "(超額)";
        } else if (cashType == 6) { //預繳
            packageName = originalPackageName + "預繳";
        } else if (cashType == 7) { //7.扣抵
            packageName = originalPackageName + "扣抵";
        }
        return packageName;
    }

    public List<InvoiceBatchRecord> genInvoiceBatchRecordList(List<InvoiceExcelBean> invoiceExcelBeanList) {
        List<InvoiceBatchRecord> resultList = new ArrayList<>();
        for (InvoiceExcelBean invoiceExcelBean : invoiceExcelBeanList) {
            InvoiceBatchRecord invoiceBatchRecord = new InvoiceBatchRecord();
            invoiceBatchRecord.setInvoiceSequence(invoiceExcelBean.getInvoiceIndex());
            invoiceBatchRecord.setInvoiceDate(invoiceExcelBean.getInvoiceDate());
            invoiceBatchRecord.setProductItemSequence(invoiceExcelBean.getItemIndex());
            invoiceBatchRecord.setProductItemDescription(invoiceExcelBean.getItemName());
            invoiceBatchRecord.setProductItemQuantity(invoiceExcelBean.getItemCnt());
            invoiceBatchRecord.setProductItemUnitPrice(invoiceExcelBean.getUnitPrice());
            invoiceBatchRecord.setTaxType(invoiceExcelBean.getTaxType());
            invoiceBatchRecord.setTaxRate(invoiceExcelBean.getTaxRate());
            invoiceBatchRecord.setBuyerIdentifier(invoiceExcelBean.getBusinessNo());
            invoiceBatchRecord.setPrintOrEmailRemark(2);
            invoiceBatchRecord.setNpoBan("");
            invoiceBatchRecord.setCustomsRemark("");
            invoiceBatchRecord.setPhoneCarrierId("");
            invoiceBatchRecord.setProductItemRemark("");
            invoiceBatchRecord.setCitizenPersonalCertificate("");
            invoiceBatchRecord.setCustomCarrierId("");
            invoiceBatchRecord.setCustomCarrierType("");
            invoiceBatchRecord.setUpc("");
            invoiceBatchRecord.setInvoiceRemark("");
            invoiceBatchRecord.setItemNo("");
            resultList.add(invoiceBatchRecord);
        }
        return resultList;
    }
}
