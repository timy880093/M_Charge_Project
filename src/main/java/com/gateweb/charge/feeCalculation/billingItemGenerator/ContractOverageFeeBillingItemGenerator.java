package com.gateweb.charge.feeCalculation.billingItemGenerator;

import com.gateweb.charge.enumeration.ChargePlan;
import com.gateweb.charge.enumeration.PaidPlan;
import com.gateweb.charge.feeCalculation.bean.ContractOverageFeeBillingData;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.utils.ConcurrentUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ContractOverageFeeBillingItemGenerator {

    ExecutorService pool = Executors.newFixedThreadPool(2);
    protected final Logger logger = LogManager.getLogger(getClass());

    public Collection<BillingItem> gen(Collection<ContractOverageFeeBillingData> contractOverageFeeBillingDataCollection) {
        Set<CompletableFuture<Void>> billingItemGenCompletableFutureSet = new HashSet<>();
        Collection<BillingItem> billingItemCollection = new HashSet<>();
        billingItemGenCompletableFutureSet.add(CompletableFuture.runAsync(() -> {
            contractOverageFeeBillingDataCollection.parallelStream().forEach(contractOverageFeeBillingData -> {
                billingItemCollection.addAll(gen(contractOverageFeeBillingData));
            });
        }, pool));
        ConcurrentUtils.completableGet(billingItemGenCompletableFutureSet);
        return billingItemCollection;
    }

    public Collection<BillingItem> gen(ContractOverageFeeBillingData billingData) {
        Set<BillingItem> billingItemSet = new HashSet<>();
        BillingItem template = new BillingItem();
        template.setCompanyId(billingData.getCompanyId());
        template.setPackageRefId(billingData.getPackageRefId());
        template.setPaidPlan(PaidPlan.POST_PAID);
        template.setChargePlan(ChargePlan.PERIODIC);
        template.setTaxRate(new BigDecimal(0.05).setScale(2, RoundingMode.DOWN));
        template.setIsMemo(false);
        template.setContractId(billingData.getContractId());
        LocalDateTime expectedOutDate = billingData.getCalculateInterval().getEndLocalDateTime().withHour(0).withMinute(0).withSecond(0);

        BillingItem billingItem = new BillingItem();
        try {
            BeanUtils.copyProperties(billingItem, template);
            billingItem.setExpectedOutDate(expectedOutDate);
            Optional<Integer> countResultOpt = billingData.getDataCounter().count(billingData.getBusinessNo(), billingData.getCalculateInterval());
            int realAmount = 0;
            //取得實際張數
            if (countResultOpt.isPresent()) {
                realAmount = countResultOpt.get();
            }
            if (billingData.getPreviousInvoiceRemaining().isPresent()) {
                int prevOverage = Math.abs(billingData.getPreviousInvoiceRemaining().get().getRemaining());
                realAmount = realAmount + prevOverage;
                //記錄曾經處理過
                billingItem.setPrevInvoiceRemainingId(billingData.getPreviousInvoiceRemaining().get().getInvoiceRemainingId());
            }

            //計算金額
            BigDecimal amount = billingData.getChargePolicy().getCalculateRule().calculateFee(
                    realAmount
                    , billingData.getChargePolicy().getGradeTableLinkedList()
            );
            //取得有效張數
            int count = billingData.getChargePolicy().getCalculateRule().chargeableCount(
                    realAmount
                    , billingData.getChargePolicy().getGradeTableLinkedList()
            );
            billingItem.setCalculateFromDate(billingData.getCalculateInterval().getSqlStartTimestamp().toLocalDateTime());
            billingItem.setCalculateToDate(billingData.getCalculateInterval().getSqlEndTimestamp().toLocalDateTime());
            billingItem.setCount(count);
            if (billingData.getChargePolicy().getMaximumCharge().isPresent()
                    && amount.compareTo(billingData.getChargePolicy().getMaximumCharge().get()) > 0) {
                billingItem.setTaxExcludedAmount(billingData.getChargePolicy().getMaximumCharge().get());
            } else {
                billingItem.setTaxExcludedAmount(amount);
            }
            if (billingItem.getTaxExcludedAmount().compareTo(BigDecimal.ZERO) == 0) {
                //零的話寫memo
                billingItem.setIsMemo(true);
            }
            billingItem.setCreateDate(LocalDateTime.now());
            billingItem.setProductCategoryId(billingData.getChargePolicy().getProductCategoryId());
            billingItemSet.add(billingItem);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return billingItemSet;
    }
}
