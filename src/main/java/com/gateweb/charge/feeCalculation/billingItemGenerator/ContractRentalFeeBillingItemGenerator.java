package com.gateweb.charge.feeCalculation.billingItemGenerator;

import com.gateweb.charge.enumeration.ChargePlan;
import com.gateweb.charge.enumeration.PaidPlan;
import com.gateweb.charge.feeCalculation.bean.ContractRentalFeeBillingData;
import com.gateweb.charge.feeCalculation.dataGateway.ContractRentalFeeBillingDataCollector;
import com.gateweb.orm.charge.entity.BillingItem;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class ContractRentalFeeBillingItemGenerator {

    protected final Logger logger = LogManager.getLogger(getClass());

    public Collection<BillingItem> gen(ContractRentalFeeBillingDataCollector contractRentalFeeBillingDataCollector, Long sourceId) {
        Set<BillingItem> billingItemSet = new HashSet<>();
        Set<ContractRentalFeeBillingData> billingDataSet = new HashSet<>(contractRentalFeeBillingDataCollector.collect(sourceId));
        billingDataSet.stream().forEach(billingData -> {
            BigDecimal chargeFeeByFixPrice = billingData.getChargePolicy().getCalculateRule().calculateFee(
                    0
                    , billingData.getChargePolicy().getGradeTableLinkedList()
            );
            BillingItem templateItem = new BillingItem();
            templateItem.setCompanyId(billingData.getCompanyId());
            templateItem.setPackageRefId(billingData.getPackageRefId());
            templateItem.setChargePlan(ChargePlan.INITIATION);
            templateItem.setPaidPlan(PaidPlan.PRE_PAID);
            templateItem.setTaxRate(new BigDecimal(0.05).setScale(4, RoundingMode.DOWN));
            templateItem.setTaxExcludedAmount(chargeFeeByFixPrice);
            templateItem.setIsMemo(false);
            templateItem.setCreateDate(LocalDateTime.now());
            templateItem.setContractId(billingData.getContractId());
            billingData.getCalculateIntervalList().stream().forEach(calculateInterval -> {
                try {
                    BillingItem billingItem = SerializationUtils.clone(templateItem);
                    billingItem.setCalculateFromDate(calculateInterval.getStartLocalDateTime());
                    billingItem.setCalculateToDate(calculateInterval.getEndLocalDateTime());
                    billingItem.setExpectedOutDate(billingData.getExpectedOutDate());
                    billingItem.setProductCategoryId(billingData.getChargePolicy().getProductCategoryId());
                    billingItem.setCount(1);
                    billingItemSet.add(billingItem);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            });
        });
        return billingItemSet;
    }

}
