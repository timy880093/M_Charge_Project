package com.gateweb.charge.contract.component;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.charge.feeCalculation.bean.ContractOverageFeeBillingData;
import com.gateweb.charge.feeCalculation.billingItemGenerator.ContractOverageFeeBillingItemGenerator;
import com.gateweb.charge.feeCalculation.dataGateway.BillingItemCollisionDataCollector;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.utils.ConcurrentUtils;
import com.gateweb.utils.LocalDateTimeUtils;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.gateweb.utils.ConcurrentUtils.pool;

@Component
public class ContractPeriodicFeeCalculator {
    @Autowired
    BillingItemCollisionDataCollector billingItemCollisionDataCollector;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    ContractOverageFeeBillingItemGenerator contractOverageFeeBillingItemGenerator;


    EventBus chargeSystemEventBus;

    @Autowired
    @Qualifier("chargeSystemEventBus")
    public void setChargeSystemEventBus(EventBus chargeSystemEventBus) {
        this.chargeSystemEventBus = chargeSystemEventBus;
    }

    public Set<CustomInterval> getOverageCalculateIntervalByYmStr(String ymStr) {
        Set<CustomInterval> customIntervalSet = new HashSet<>();
        Optional<YearMonth> yearMonthOpt = LocalDateTimeUtils.parseYearMonthFromString(ymStr, "yyyyMM");
        if (yearMonthOpt.isPresent() && yearMonthOpt.get().getMonthValue() % 2 != 0) {
            CustomInterval targetInterval1 = new CustomInterval(
                    yearMonthOpt.get().atDay(1).atStartOfDay().minusMonths(1)
                    , yearMonthOpt.get().minusMonths(1).atEndOfMonth().plusDays(1).atStartOfDay().minusSeconds(1)
            );
            CustomInterval targetInterval2 = new CustomInterval(
                    yearMonthOpt.get().atDay(1).atStartOfDay().minusMonths(2)
                    , yearMonthOpt.get().minusMonths(2).atEndOfMonth().plusDays(1).atStartOfDay().minusSeconds(1)
            );
            customIntervalSet.add(targetInterval1);
            customIntervalSet.add(targetInterval2);
        }

        return customIntervalSet;
    }

    /**
     * 可能可以寫的更好
     *
     * @param billingItemCollection
     * @param callerId
     * @return
     */
    public Set<BillingItem> cancelCollisionBillingItem(Collection<BillingItem> billingItemCollection, Long callerId) {
        //檢查重覆並刪除可以刪除的部份
        Set<BillingItem> removableCollisionExistsBillingItemSet = Collections.synchronizedSet(new HashSet<>());
        Set<BillingItem> unRemovableCollisionGeneratedBillingItemSet = Collections.synchronizedSet(new HashSet<>());
        Collection<BillingItem> synchronizedCollection = Collections.synchronizedCollection(billingItemCollection);
        List<CompletableFuture<Void>> completableFutureList = Collections.synchronizedList(new ArrayList<>());
        synchronizedCollection.parallelStream().forEach(billingItem -> {
            completableFutureList.add(CompletableFuture.runAsync(() -> {
                Set<BillingItem> existBillingItemSet =
                        billingItemCollisionDataCollector.getCollisionBillingItem(billingItem);
                existBillingItemSet.stream().forEach(existBillingItem -> {
                    if (existBillingItem.getBillId() == null) {
                        removableCollisionExistsBillingItemSet.add(existBillingItem);
                    } else {
                        unRemovableCollisionGeneratedBillingItemSet.add(billingItem);
                    }
                });
            }, pool));
        });
        ConcurrentUtils.completableGet(completableFutureList);
        //移除已經存在，但未結帳的部份
        billingItemRepository.deleteInBatch(removableCollisionExistsBillingItemSet);
        removableCollisionExistsBillingItemSet.stream().forEach(billingItem -> {
            ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                    EventSource.BILLING_ITEM
                    , EventAction.CANCEL
                    , billingItem.getBillingItemId()
                    , callerId
            );
            chargeSystemEventBus.post(chargeSystemEvent);
        });
        //已經出帳不能重產，移除未來將要處理的部份
        Set<BillingItem> resultSet = new HashSet<>(billingItemCollection);
        resultSet.removeAll(unRemovableCollisionGeneratedBillingItemSet);
        return resultSet;
    }

    public void executePostPaidPeriodicBilling(
            Set<ContractOverageFeeBillingData> contractOverageFeeBillingData, Long callerId) {
        Collection<BillingItem> billingItemSet =
                contractOverageFeeBillingItemGenerator.gen(contractOverageFeeBillingData);
        Collection<BillingItem> writableBillingItem = cancelCollisionBillingItem(billingItemSet, callerId);
        billingItemRepository.saveAll(writableBillingItem);
        billingItemRepository.flush();
        writableBillingItem.stream().forEach(billingItem -> {
            //trigger event listener
            ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                    EventSource.BILLING_ITEM
                    , EventAction.CREATE
                    , billingItem.getBillingItemId()
                    , callerId
            );
            chargeSystemEventBus.post(chargeSystemEvent);
        });
    }
}
