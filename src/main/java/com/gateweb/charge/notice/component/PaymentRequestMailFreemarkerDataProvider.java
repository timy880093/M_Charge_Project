package com.gateweb.charge.notice.component;

import com.gateweb.charge.enumeration.ChargePolicyType;
import com.gateweb.charge.notice.bean.*;
import com.gateweb.charge.report.ctbc.CtbcVirtualAccountGenerator;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.PackageRef;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PaymentRequestMailFreemarkerDataProvider {
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    CtbcVirtualAccountGenerator ctbcVirtualAccountGenerator;
    @Autowired
    DeductBillingItemMailDataProvider deductBillingItemMailDataProvider;
    @Autowired
    PackageRefPaymentRequestMailDataProvider packageRefPaymentRequestMailDataProvider;

    public PaymentRequestMailFreemarkerData genPaymentRequestCorrectionMailData(PaymentRequestMailData paymentRequestMailData) {
        PaymentRequestMailFreemarkerData paymentRequestMailFreemarkerData = genPaymentRequestMailData(paymentRequestMailData);
        paymentRequestMailFreemarkerData.setCorrection(true);
        return paymentRequestMailFreemarkerData;
    }

    public PaymentRequestMailFreemarkerData genPaymentRequestMailData(PaymentRequestMailData paymentRequestMailData) {
        PaymentRequestMailFreemarkerData paymentRequestMailFreemarkerData = new PaymentRequestMailFreemarkerData();
        Set<BillingItem> billingItemSet = new HashSet<>(billingItemRepository.findByBillId(
                paymentRequestMailData.getBill().getBillId())
        );
        Optional<Company> companyOptional = companyRepository.findByCompanyId(
                paymentRequestMailData.getCompany().getCompanyId().intValue()
        );
        if (companyOptional.isPresent() && !billingItemSet.isEmpty()) {
            Optional<String> ctbcVirtualAccountOpt = ctbcVirtualAccountGenerator.getVirtualAccount(companyOptional.get().getBusinessNo());
            if (ctbcVirtualAccountOpt.isPresent()) {
                paymentRequestMailFreemarkerData.setCtbcVirtualAccount(
                        ctbcVirtualAccountGenerator.customFormatVirtualAccount(
                                ctbcVirtualAccountOpt.get()
                        )
                );
            }
            paymentRequestMailFreemarkerData.setCompanyName(companyOptional.get().getName());
            Optional<YearMonth> yearMonthOptional = LocalDateTimeUtils.parseYearMonthFromString(
                    paymentRequestMailData.getBill().getBillYm(), "yyyyMM"
            );
            if (yearMonthOptional.isPresent()) {
                paymentRequestMailFreemarkerData.setPaymentExpirationDate(
                        yearMonthOptional.get().atDay(20).plusMonths(1).format(
                                DateTimeFormatter.ofPattern("yyyy/MM/dd")
                        )
                );
                paymentRequestMailFreemarkerData.setYmString(
                        yearMonthOptional.get().plusMonths(1).format(
                                DateTimeFormatter.ofPattern("yyyyMM")
                        )
                );
            }

            Set<BillingItem> packageRefIdBillingItem = billingItemSet.stream().filter(billingItem -> {
                return billingItem.getPackageRefId() != null;
            }).collect(Collectors.toSet());

            genPackageRefBillingItemMailData(companyOptional.get(), packageRefIdBillingItem, paymentRequestMailFreemarkerData);

            Set<PaymentRequestMailBillingItem> deductPaymentRequestBillingItem
                    = deductBillingItemMailDataProvider.genDeductBillingItemMailData(billingItemSet);
            paymentRequestMailFreemarkerData.getPaymentRequestMailBillingItemSet().addAll(deductPaymentRequestBillingItem);

            //統計總計費用
            long paymentRequestMailBillingItemTotalAmount
                    = paymentRequestMailFreemarkerData.getPaymentRequestMailBillingItemSet().stream()
                    .mapToInt(paymentRequestMailBillingItem -> {
                        return paymentRequestMailBillingItem.getTaxIncludedAmount()
                                .setScale(0, RoundingMode.HALF_UP).intValue();
                    })
                    .summaryStatistics().getSum();
            paymentRequestMailFreemarkerData.setPaymentRequestTotalAmount(new BigDecimal(paymentRequestMailBillingItemTotalAmount));

            if (!paymentRequestMailFreemarkerData.getPaymentRequestMailOverageItemList().isEmpty()) {
                paymentRequestMailFreemarkerData.setHaveOverage(true);
            }
        }
        if (paymentRequestMailData.getNoticeCustomOpt().isPresent()
                && StringUtils.isNotEmpty(paymentRequestMailData.getNoticeCustomOpt().get().getExtraNoticeMessage())) {
            paymentRequestMailFreemarkerData.setExtraNotice(true);
            paymentRequestMailFreemarkerData.setExtraNoticeMessage(
                    paymentRequestMailData.getNoticeCustomOpt().get().getExtraNoticeMessage()
            );
        } else {
            paymentRequestMailFreemarkerData.setExtraNotice(false);
            paymentRequestMailFreemarkerData.setExtraNoticeMessage("");
        }
        return paymentRequestMailFreemarkerData;
    }

    private void genPackageRefBillingItemMailData(Company company, Set<BillingItem> billingItemSet, PaymentRequestMailFreemarkerData paymentRequestMailFreemarkerData) {

        //產生rental billingItem group map
        MultiValueMap keyRentalBillingItemMap = packageRefPaymentRequestMailDataProvider.getGroupKeyRentalBillingItemMap(billingItemSet);
        //產生overage billingItem group map
        MultiValueMap keyOverageBillingItemMap = packageRefPaymentRequestMailDataProvider.getGroupKeyOverageBillingItemMap(billingItemSet);
        //產生contract group map
        HashMap<String, Contract> keyContractMap = packageRefPaymentRequestMailDataProvider.getGroupKeyContractMap(billingItemSet);
        //產生packageRef map
        HashMap<String, PackageRef> keyPackageRefMap = packageRefPaymentRequestMailDataProvider.getGroupKeyPackageRefMap(
                keyContractMap, billingItemSet
        );

        Map<String, PaymentRequestMailBillingItem> paymentRequestMailOverageItemHashMap =
                packageRefPaymentRequestMailDataProvider.genPackageRefOverageBillingItemMap(
                        keyContractMap
                        , keyPackageRefMap
                        , keyOverageBillingItemMap
                );

        Collection<PaymentRequestMailBillingItem> paymentRequestMailBillingItemCollection
                = new HashSet<>(paymentRequestMailOverageItemHashMap.values());

        packageRefPaymentRequestMailDataProvider.mergePaymentRequestMailOverageItem(paymentRequestMailBillingItemCollection);
        paymentRequestMailFreemarkerData.getPaymentRequestMailBillingItemSet().addAll(paymentRequestMailBillingItemCollection);

        paymentRequestMailFreemarkerData.getPaymentRequestMailOverageItemList().addAll(
                packageRefPaymentRequestMailDataProvider.genPaymentRequestOverageItemSet(
                        company
                        , keyOverageBillingItemMap
                        , paymentRequestMailOverageItemHashMap
                )
        );

        Collection<PaymentRequestMailGradeTable> paymentRequestMailGradeTables =
                packageRefPaymentRequestMailDataProvider.genPaymentRequestMailGradeTableSet(keyPackageRefMap, keyOverageBillingItemMap);

        paymentRequestMailFreemarkerData.getPaymentRequestMailGradeTableList().addAll(paymentRequestMailGradeTables);

        BigDecimal paymentRequestOverageItemTotalAmount =
                packageRefPaymentRequestMailDataProvider.getPaymentRequestOverageItemTotalAmount(
                        paymentRequestMailOverageItemHashMap.values()
                );

        paymentRequestMailFreemarkerData.setPaymentRequestTotalAmount(paymentRequestOverageItemTotalAmount);

        Set<PaymentRequestMailBillingItem> paymentRequestMailRentalBillingItemSet
                = packageRefPaymentRequestMailDataProvider.genPackageRefRentalBillingItemMailData(
                keyRentalBillingItemMap
                , keyContractMap
                , keyPackageRefMap
        );

        //排序項目，月租預繳優先
        paymentRequestMailFreemarkerData.getPaymentRequestMailBillingItemSet().addAll(paymentRequestMailRentalBillingItemSet);
        paymentRequestMailFreemarkerData.setPaymentRequestMailBillingItemSet(
                paymentRequestMailFreemarkerData.getPaymentRequestMailBillingItemSet().stream().sorted(
                        (o1, o2) -> {
                            if (o1.getChargeType().equals(ChargePolicyType.RENTAL.description)
                                    && o2.getChargeType().equals(ChargePolicyType.OVERAGE.description)) {
                                return -1;
                            } else if (o1.getChargeType().equals(ChargePolicyType.OVERAGE.description)
                                    && o2.getChargeType().equals(ChargePolicyType.RENTAL.description)) {
                                return 1;
                            } else {
                                return o1.getChargeType().compareTo(o2.getChargeType());
                            }
                        }
                ).collect(Collectors.toList())
        );

        //排序項目
        paymentRequestMailFreemarkerData.setPaymentRequestMailOverageItemList(
                paymentRequestMailFreemarkerData.getPaymentRequestMailOverageItemList().stream().sorted(
                        Comparator.comparing(PaymentRequestMailOverageItem::getCalYm)
                ).collect(Collectors.toList())
        );
        paymentRequestMailFreemarkerData.setPaymentRequestMailBillingItemSet(
                paymentRequestMailFreemarkerData.getPaymentRequestMailBillingItemSet().stream().collect(Collectors.toList())
        );

        //統計超額費用
        Double paymentRequestMailOverageItemTotalAmountDouble =
                paymentRequestMailFreemarkerData.getPaymentRequestMailOverageItemList().stream()
                        .mapToDouble(paymentRequestMailBillingItem -> {
                            return paymentRequestMailBillingItem.getTaxIncludedAmount().doubleValue();
                        })
                        .summaryStatistics().getSum();
        BigDecimal paymentRequestMailOverageItemTotalAmountDecimal =
                new BigDecimal(paymentRequestMailOverageItemTotalAmountDouble);
        paymentRequestMailFreemarkerData.setPaymentRequestOverageTotalAmount(paymentRequestMailOverageItemTotalAmountDecimal);
    }
}
