package com.gateweb.charge.notice.component;

import com.gateweb.charge.chargeSource.service.ChargeSourceService;
import com.gateweb.charge.chargePolicy.ChargePolicyProvider;
import com.gateweb.charge.enumeration.ChargePolicyType;
import com.gateweb.charge.notice.bean.PaymentRequestMailBillingItem;
import com.gateweb.charge.notice.bean.PaymentRequestMailGradeTable;
import com.gateweb.charge.notice.bean.PaymentRequestMailGradeTableItem;
import com.gateweb.charge.notice.bean.PaymentRequestMailOverageItem;
import com.gateweb.charge.report.bean.*;
import com.gateweb.orm.charge.entity.*;
import com.gateweb.orm.charge.repository.*;
import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class PackageRefPaymentRequestMailDataProvider {
    @Autowired
    ChargePackageRepository chargePackageRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    ChargeSourceService chargeSourceService;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    ChargeRuleRepository chargeRuleRepository;
    @Autowired
    NewGradeRepository newGradeRepository;
    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @Autowired
    ChargePolicyProvider chargePolicyProvider;

    public Map<String, PaymentRequestMailBillingItem> genPackageRefOverageBillingItemMap(
            HashMap<String, Contract> keyContractMap
            , HashMap<String, PackageRef> keyPackageRefMap
            , MultiValueMap keyOverageBillingItemMap) {
        Collection<BillingItem> overageBillingItemSet = new HashSet<>(keyOverageBillingItemMap.values());
        //過濾相同的級距表
        Set<PackageRef> packageRefSet = new HashSet<>();
        Set<Long> packageRefIdSet = new HashSet<>();
        keyOverageBillingItemMap.values().stream().forEach(billingItemObj -> {
            BillingItem billingItem = (BillingItem) billingItemObj;
            String key = genClassifyKey(billingItem);
            if (keyPackageRefMap.containsKey(key)) {
                PackageRef packageRef = keyPackageRefMap.get(key);
                if (!packageRefIdSet.contains(packageRef.getPackageRefId())) {
                    packageRefIdSet.add(packageRef.getPackageRefId());
                    packageRefSet.add(packageRef);
                }
            }
        });
        HashMap<String, BigDecimal> packageRefTaxIncludedAmountMap = getTaxIncludedSummaryMap(
                overageBillingItemSet
        );
        //產生超額費用表
        HashMap<String, PaymentRequestMailBillingItem> paymentRequestMailBillingItemMap
                = genPaymentRequestMailBillingItemMap(
                packageRefTaxIncludedAmountMap, keyContractMap, keyPackageRefMap
        );
        return paymentRequestMailBillingItemMap;
    }

    public Collection<PaymentRequestMailGradeTable> genPaymentRequestMailGradeTableSet(
            HashMap<String, PackageRef> keyPackageRefMap
            , MultiValueMap keyOverageBillingItemMap) {
        Set<PaymentRequestMailGradeTable> paymentRequestMailGradeTableSet = new HashSet<>();
        //過濾相同的級距表
        Set<PackageRef> packageRefSet = new HashSet<>();
        Set<Long> packageRefIdSet = new HashSet<>();
        keyOverageBillingItemMap.values().stream().forEach(billingItemObj -> {
            BillingItem billingItem = (BillingItem) billingItemObj;
            String key = genClassifyKey(billingItem);
            if (keyPackageRefMap.containsKey(key)) {
                PackageRef packageRef = keyPackageRefMap.get(key);
                if (!packageRefIdSet.contains(packageRef.getPackageRefId())) {
                    packageRefIdSet.add(packageRef.getPackageRefId());
                    packageRefSet.add(packageRef);
                }
            }
        });
        for (PackageRef packageRef : packageRefSet) {
            //產生級距表
            PaymentRequestMailGradeTable paymentRequestMailGradeTable = genPaymentRequestMailGradeTableItem(
                    packageRef
            );
            paymentRequestMailGradeTableSet.add(paymentRequestMailGradeTable);
        }
        return paymentRequestMailGradeTableSet;
    }

    void mergePaymentRequestMailOverageItem(
            Collection<PaymentRequestMailBillingItem> paymentRequestMailBillingItemCollection) {
        MultiValueMap multiValueMap = new MultiValueMap();
        paymentRequestMailBillingItemCollection.stream().forEach(paymentRequestMailBillingItem -> {
            multiValueMap.put(paymentRequestMailBillingItem.getItemName(), paymentRequestMailBillingItem);
        });

        multiValueMap.keySet().stream().forEach(keyObj -> {
            String key = (String) keyObj;
            HashSet<PaymentRequestMailBillingItem> targetSet = new HashSet<PaymentRequestMailBillingItem>(
                    multiValueMap.getCollection(key)
            );
            PaymentRequestMailBillingItem paymentRequestMailBillingItemSummary = new PaymentRequestMailBillingItem();
            for (PaymentRequestMailBillingItem paymentRequestMailBillingItem : targetSet) {
                if (paymentRequestMailBillingItemSummary.getItemName() == null) {
                    paymentRequestMailBillingItemSummary.setItemName(paymentRequestMailBillingItem.getItemName());
                }
                if (paymentRequestMailBillingItemSummary.getTaxIncludedAmount() == null) {
                    paymentRequestMailBillingItemSummary.setTaxIncludedAmount(
                            paymentRequestMailBillingItem.getTaxIncludedAmount()
                    );
                } else {
                    paymentRequestMailBillingItemSummary.setTaxIncludedAmount(
                            paymentRequestMailBillingItemSummary.getTaxIncludedAmount().add(
                                    paymentRequestMailBillingItem.getTaxIncludedAmount()
                            )
                    );
                }
                if (paymentRequestMailBillingItemSummary.getQuota() == null ||
                        paymentRequestMailBillingItemSummary.getQuota().equals("0")) {
                    paymentRequestMailBillingItemSummary.setQuota(paymentRequestMailBillingItem.getQuota());
                }
                if (paymentRequestMailBillingItemSummary.getChargeType() == null) {
                    paymentRequestMailBillingItemSummary.setChargeType(paymentRequestMailBillingItem.getChargeType());
                }
            }
            paymentRequestMailBillingItemCollection.removeAll(targetSet);
            //先行四捨五入
            paymentRequestMailBillingItemSummary.setTaxIncludedAmount(
                    paymentRequestMailBillingItemSummary.getTaxIncludedAmount()
                            .setScale(0, BigDecimal.ROUND_HALF_UP)
            );
            paymentRequestMailBillingItemCollection.add(paymentRequestMailBillingItemSummary);
        });
    }

    public Set<PaymentRequestMailOverageItem> genPaymentRequestOverageItemSet(
            Company company
            , MultiValueMap keyOverageBillingItemMap
            , Map<String, PaymentRequestMailBillingItem> paymentRequestMailBillingItemMap) {
        Set<PaymentRequestMailOverageItem> paymentRequestMailOverageItemSet = new HashSet<>();
        Collection<BillingItem> overageBillingItemSet = new HashSet<>(keyOverageBillingItemMap.values());
        HashMap<Long, BigDecimal> packageRefOverageFeePerInvoiceMap = getOverageFeePerInvoice(
                overageBillingItemSet
        );
        keyOverageBillingItemMap.values().stream().forEach(billingItemObj -> {
            BillingItem billingItem = (BillingItem) billingItemObj;
            String key = genClassifyKey(billingItem);
            PaymentRequestMailOverageItem paymentRequestMailOverageItem = genPaymentRequestMailOverageItem(
                    billingItem,
                    company.getBusinessNo()
            );
            paymentRequestMailOverageItem.setUnitPrice(
                    packageRefOverageFeePerInvoiceMap.get(billingItem.getPackageRefId())
            );
            paymentRequestMailOverageItem.setQuota(
                    Integer.valueOf(
                            paymentRequestMailBillingItemMap.get(key).getQuota()
                    )
            );
            paymentRequestMailOverageItemSet.add(paymentRequestMailOverageItem);
        });
        return paymentRequestMailOverageItemSet;
    }

    public BigDecimal getPaymentRequestOverageItemTotalAmount(
            Collection<PaymentRequestMailBillingItem> paymentRequestMailBillingItems) {
        BigDecimal paymentRequestOverageItemTotalAmount = BigDecimal.ZERO;
        paymentRequestMailBillingItems.stream().forEach(billingItem -> {
            paymentRequestOverageItemTotalAmount.add(billingItem.getTaxIncludedAmount());
        });
        return paymentRequestOverageItemTotalAmount;
    }

    HashMap<String, Contract> getGroupKeyContractMap(Set<BillingItem> billingItemSet) {
        HashMap<String, Contract> resultMap = new HashMap<>();
        billingItemSet.stream().forEach(billingItem -> {
            String key = genClassifyKey(billingItem);
            if (billingItem.getContractId() != null) {
                Optional<Contract> contractOptional = contractRepository.findById(billingItem.getContractId());
                if (contractOptional.isPresent()) {
                    resultMap.put(key, contractOptional.get());
                }
            }
        });
        return resultMap;
    }

    HashMap<String, PackageRef> getGroupKeyPackageRefMap(
            HashMap<String, Contract> contractHashMap, Set<BillingItem> billingItemSet) {
        HashMap<String, PackageRef> resultMap = new HashMap<>();
        billingItemSet.stream().forEach(billingItem -> {
            String key = genClassifyKey(billingItem);
            if (contractHashMap.containsKey(key)) {
                Contract contract = contractHashMap.get(key);
                Optional<PackageRef> packageRefOptional = packageRefRepository.findByFromPackageIdAndPackageRefId(
                        contract.getPackageId()
                        , billingItem.getPackageRefId()
                );
                if (packageRefOptional.isPresent()) {
                    resultMap.put(key, packageRefOptional.get());
                }
            }
        });
        return resultMap;
    }


    private PaymentRequestMailOverageItem genPaymentRequestMailOverageItem(
            BillingItem billingItem
            , String seller) {
        PaymentRequestMailOverageItem paymentRequestMailOverageItem = new PaymentRequestMailOverageItem();
        String calYm = billingItem.getCalculateFromDate().format(DateTimeFormatter.ofPattern("yyyyMM"));
        List<ChargeSourceInvoiceCountDiffReport> chargeSourceInvoiceCountDiffReportList =
                chargeSourceService.getChargeSourceInvoiceCountDiffReport(calYm.substring(0, 6), seller, false);
        Long usage = chargeSourceInvoiceCountDiffReportList.stream().mapToLong(
                ChargeSourceInvoiceCountDiffReport::getOriginalCount
        ).sum();
        if (billingItem.getPrevInvoiceRemainingId() != null) {
            Optional<InvoiceRemaining> invoiceRemainingOptional
                    = invoiceRemainingRepository.findById(billingItem.getPrevInvoiceRemainingId());
            if (invoiceRemainingOptional.isPresent() && invoiceRemainingOptional.get().getRemaining() < 0) {
                int prevOverageCount = Math.abs(invoiceRemainingOptional.get().getRemaining());
                usage += prevOverageCount;
            }
        }
        paymentRequestMailOverageItem.setCalYm(calYm);
        paymentRequestMailOverageItem.setUsage(usage.intValue());
        paymentRequestMailOverageItem.setTaxExcludedAmount(billingItem.getTaxExcludedAmount());
        paymentRequestMailOverageItem.setTaxIncludedAmount(billingItem.getTaxIncludedAmount());
        paymentRequestMailOverageItem.setOverageCount(billingItem.getCount());
        return paymentRequestMailOverageItem;
    }

    public String genClassifyKey(BillingItem billingItem) {
        StringBuilder stringBuilder = new StringBuilder();
        if (billingItem.getContractId() != null) {
            stringBuilder.append(billingItem.getContractId());
        }
        if (billingItem.getPackageRefId() != null) {
            stringBuilder.append(billingItem.getPackageRefId());
        }
        return stringBuilder.toString();
    }

    public MultiValueMap getGroupKeyBillingItemMap(Set<BillingItem> billingItemSet) {
        MultiValueMap multiValueMap = new MultiValueMap();
        billingItemSet.stream().forEach(billingItem -> {
            String key = genClassifyKey(billingItem);
            multiValueMap.put(key, billingItem);
        });
        return multiValueMap;
    }

    public MultiValueMap getGroupKeyOverageBillingItemMap(Set<BillingItem> billingItemSet) {
        MultiValueMap multiValueMap = new MultiValueMap();
        billingItemSet.stream().forEach(billingItem -> {
            String key = genClassifyKey(billingItem);
            //單獨區分超額項目
            if (isOverage(billingItem.getPackageRefId())) {
                multiValueMap.put(key, billingItem);
            }
        });
        return multiValueMap;
    }

    private PaymentRequestMailGradeTable genPaymentRequestMailGradeTableItem(PackageRef packageRef) {
        PaymentRequestMailGradeTable paymentRequestMailGradeTable = new PaymentRequestMailGradeTable();
        Optional<ChargeRule> chargeModeOptional = chargeRuleRepository.findByChargeRuleId(packageRef.getToChargeRuleId());
        if (chargeModeOptional.isPresent()) {
            if (chargeModeOptional.get().getMaximumCharge() != null) {
                paymentRequestMailGradeTable.setMaximumCharge(
                        chargeModeOptional.get().getMaximumCharge().stripTrailingZeros().toPlainString()
                );
            } else {
                paymentRequestMailGradeTable.setMaximumCharge("");
            }
            if (chargeModeOptional.get().getAccumulation()) {
                paymentRequestMailGradeTable.setAccumulateAnnouncement("*總費用為各層級費用加總");
            } else {
                paymentRequestMailGradeTable.setAccumulateAnnouncement("");
            }
            List<NewGrade> newGradeSet = new ArrayList<>(
                    newGradeRepository.findByRootIdIs(chargeModeOptional.get().getGradeId())
            );
            newGradeSet.stream().forEach(newGrade -> {
                PaymentRequestMailGradeTableItem paymentRequestMailGradeTableItem = new PaymentRequestMailGradeTableItem();
                if (newGrade.getGradeId().equals(newGrade.getRootId())) {
                    paymentRequestMailGradeTable.setName(newGrade.getName());
                }
                if (newGrade.getFixPrice() != null) {
                    paymentRequestMailGradeTableItem.setFixPrice(
                            newGrade.getFixPrice().setScale(4, RoundingMode.DOWN).stripTrailingZeros().toPlainString()
                    );
                } else {
                    paymentRequestMailGradeTableItem.setFixPrice("");
                }
                if (newGrade.getUnitPrice() != null) {
                    paymentRequestMailGradeTableItem.setUnitPrice(
                            newGrade.getUnitPrice().setScale(4, RoundingMode.DOWN).stripTrailingZeros().toPlainString()
                    );
                } else {
                    paymentRequestMailGradeTableItem.setUnitPrice("");
                }
                paymentRequestMailGradeTableItem.setFrom(String.valueOf(newGrade.getCntStart()));
                paymentRequestMailGradeTableItem.setTo(String.valueOf(newGrade.getCntEnd()));
                paymentRequestMailGradeTable.getPaymentRequestMailGradeTableItemList().add(paymentRequestMailGradeTableItem);
            });
        }
        return paymentRequestMailGradeTable;
    }

    private HashMap<Long, BigDecimal> getOverageFeePerInvoice(Collection<BillingItem> billingItemSet) {
        HashMap<Long, BigDecimal> overageFeePerInvoiceMap = new HashMap<>();
        billingItemSet.stream().forEach(billingItem -> {
            Optional<PackageRef> packageRefOptional = packageRefRepository.findById(billingItem.getPackageRefId());
            if (packageRefOptional.isPresent()) {
                Optional<ChargeRule> chargeModeOptional = chargeRuleRepository.findByChargeRuleId(packageRefOptional.get().getToChargeRuleId());
                if (chargeModeOptional.isPresent()) {
                    BigDecimal overageFeePerInvoice = getOverageFeePerInvoice(chargeModeOptional.get());
                    if (!overageFeePerInvoiceMap.containsKey(billingItem.getPackageRefId())) {
                        overageFeePerInvoiceMap.put(
                                billingItem.getPackageRefId()
                                , overageFeePerInvoice
                        );
                    }
                }
            }
        });
        return overageFeePerInvoiceMap;
    }

    MultiValueMap getGroupKeyRentalBillingItemMap(Set<BillingItem> billingItemSet) {
        MultiValueMap multiValueMap = new MultiValueMap();
        billingItemSet.stream().forEach(billingItem -> {
            String key = genClassifyKey(billingItem);
            if (!isOverage(billingItem.getPackageRefId())) {
                multiValueMap.put(key, billingItem);
            }
        });
        return multiValueMap;
    }

    public BigDecimal getOverageFeePerInvoice(ChargeRule chargeRule) {
        BigDecimal overageFeePerInvoice = BigDecimal.ZERO;
        Optional<ChargeRule> chargeRuleOptional = chargeRuleRepository.findByChargeRuleId(chargeRule.getChargeRuleId());
        if (chargeRuleOptional.isPresent() && chargeRuleOptional.get().getGradeId() != null) {
            Optional<NewGrade> gradeOptional = newGradeRepository.findByGradeId(chargeRuleOptional.get().getGradeId());
            if (gradeOptional.isPresent() && gradeOptional.get().getUnitPrice() != null) {
                overageFeePerInvoice = gradeOptional.get().getUnitPrice();
            }
        }
        return overageFeePerInvoice;
    }


    /**
     * 不是從0開始的視為超額
     *
     * @param packageRef
     * @return
     */
    public boolean isOverage(PackageRef packageRef) {
        boolean isOverage = false;
        Optional<ChargeRule> chargeRefOptional = chargeRuleRepository.findByChargeRuleId(packageRef.getToChargeRuleId());
        if (chargeRefOptional.isPresent()) {
            Optional<NewGrade> newGradeOptional = newGradeRepository.findById(chargeRefOptional.get().getGradeId());
            if (newGradeOptional.isPresent()) {
                if (newGradeOptional.get().getCntStart() != 0) {
                    isOverage = true;
                }
            }
        }
        return isOverage;
    }

    boolean isOverage(Long packageRefId) {
        Optional<PackageRef> packageRefOptional = packageRefRepository.findById(packageRefId);
        if (packageRefOptional.isPresent()) {
            return isOverage(packageRefOptional.get());
        } else {
            return false;
        }
    }

    public String getChargeType(ChargeRule chargeRule) {
        Optional<ChargePolicyType> chargePolicyTypeOpt = chargePolicyProvider.getChargePolicyType(chargeRule);
        if (chargePolicyTypeOpt.isPresent()) {
            return chargePolicyTypeOpt.get().description;
        } else {
            return "";
        }
    }

    private HashMap<String, BigDecimal> getTaxIncludedSummaryMap(Collection<BillingItem> billingItemSet) {
        HashMap<String, BigDecimal> keyTaxIncludedAmountMap = new HashMap<>();
        billingItemSet.stream().forEach(billingItem -> {
            String key = genClassifyKey(billingItem);
            if (keyTaxIncludedAmountMap.keySet().contains(key)) {
                keyTaxIncludedAmountMap.put(
                        key, keyTaxIncludedAmountMap.get(key).add(billingItem.getTaxIncludedAmount())
                );
            } else {
                keyTaxIncludedAmountMap.put(
                        key, billingItem.getTaxIncludedAmount()
                );
            }
        });
        return keyTaxIncludedAmountMap;
    }

    /**
     * 月租billingItem處理
     * 只有主要的item沒有超額的部份
     */
    Set<PaymentRequestMailBillingItem> genPackageRefRentalBillingItemMailData(
            MultiValueMap keyRentalBillingItemMap
            , HashMap<String, Contract> keyContractMap
            , HashMap<String, PackageRef> keyPackageRefMap) {
        Set<PaymentRequestMailBillingItem> paymentRequestMailBillingItemSet = new HashSet<>();
        HashSet<BillingItem> rentalBillingItemSet = new HashSet<>(keyRentalBillingItemMap.values());
        //加總費用表
        HashMap<String, BigDecimal> packageRefTaxIncludedAmountMap = getTaxIncludedSummaryMap(
                rentalBillingItemSet
        );
        //產生月租項目
        HashMap<String, PaymentRequestMailBillingItem> paymentRequestMailBillingItemMap = genPaymentRequestMailBillingItemMap(
                packageRefTaxIncludedAmountMap, keyContractMap, keyPackageRefMap
        );
        paymentRequestMailBillingItemSet.addAll(paymentRequestMailBillingItemMap.values());
        return paymentRequestMailBillingItemSet;
    }

    PaymentRequestMailBillingItem genPaymentRequestMailBillingItem(
            Contract contract
            , String key
            , PackageRef packageRef
            , HashMap<String, BigDecimal> taxIncludedAmountMap) {
        PaymentRequestMailBillingItem paymentRequestMailBillingItem = new PaymentRequestMailBillingItem();
        Optional<ChargeRule> chargeModeOptional = chargeRuleRepository.findByChargeRuleId(packageRef.getToChargeRuleId());
        boolean isOverage = isOverage(packageRef);
        if (chargeModeOptional.isPresent()) {
            paymentRequestMailBillingItem.setItemName(
                    getPackageDesc(contract, isOverage, chargeModeOptional.get())
            );
            paymentRequestMailBillingItem.setQuota(
                    String.valueOf(getInvoiceQuota(chargeModeOptional.get(), isOverage))
            );
            paymentRequestMailBillingItem.setChargeType(getChargeType(chargeModeOptional.get()));
            paymentRequestMailBillingItem.setTaxIncludedAmount(taxIncludedAmountMap.get(key));
        }
        return paymentRequestMailBillingItem;
    }

    public Integer getInvoiceQuota(ChargeRule chargeRule, boolean isOverage) {
        Integer quota = null;
        Optional<ChargeRule> chargeRefOptional = chargeRuleRepository.findByChargeRuleId(chargeRule.getChargeRuleId());
        if (chargeRefOptional.isPresent() && chargeRefOptional.get().getGradeId() != null) {
            Optional<NewGrade> gradeOptional = newGradeRepository.findByGradeId(chargeRefOptional.get().getGradeId());
            if (gradeOptional.isPresent()) {
                if (isOverage) {
                    quota = gradeOptional.get().getCntStart() - 1;
                } else {
                    quota = gradeOptional.get().getCntEnd();
                }
            }
        }
        return quota;
    }

    String getPackageDesc(
            Contract contract
            , boolean isOverage
            , ChargeRule chargeRule) {
        StringBuffer packageDescBuffer = new StringBuffer();
        Optional<ChargePackage> chargePackageOptional = chargePackageRepository.findById(contract.getPackageId());
        if (chargePackageOptional.isPresent()) {
            packageDescBuffer.append(chargePackageOptional.get().getName());
        } else {
            packageDescBuffer.append(chargeRule.getName());
        }
        if (!isOverage) {
            packageDescBuffer.append("(");
            packageDescBuffer.append(contract.getEffectiveDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            packageDescBuffer.append("~");
            packageDescBuffer.append(contract.getExpirationDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            packageDescBuffer.append(")");
        }
        return packageDescBuffer.toString();
    }

    private HashMap<String, PaymentRequestMailBillingItem> genPaymentRequestMailBillingItemMap(
            HashMap<String, BigDecimal> keyTaxIncludedAmountMap
            , HashMap<String, Contract> keyContractMap
            , HashMap<String, PackageRef> keyPackageRefMap) {
        HashMap<String, PaymentRequestMailBillingItem> paymentRequestMailBillingItemHashMap = new HashMap<>();
        for (String key : keyTaxIncludedAmountMap.keySet()) {
            if (keyPackageRefMap.containsKey(key)) {
                Contract contract = keyContractMap.get(key);
                PackageRef packageRef = keyPackageRefMap.get(key);
                PaymentRequestMailBillingItem paymentRequestMailBillingItem =
                        genPaymentRequestMailBillingItem(
                                contract
                                , key
                                , packageRef
                                , keyTaxIncludedAmountMap
                        );
                paymentRequestMailBillingItemHashMap.put(key, paymentRequestMailBillingItem);
            }
        }
        return paymentRequestMailBillingItemHashMap;
    }
}
