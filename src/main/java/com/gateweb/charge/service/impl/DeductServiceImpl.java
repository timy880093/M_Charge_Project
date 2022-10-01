package com.gateweb.charge.service.impl;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.deduct.builder.DeductHistoryBuilder;
import com.gateweb.charge.deduct.component.DeductibleAmountComponent;
import com.gateweb.charge.deduct.component.DeductibleItemFilterComponent;
import com.gateweb.charge.deduct.component.PurchaseDeductVoConverter;
import com.gateweb.charge.enumeration.*;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.charge.service.DeductService;
import com.gateweb.charge.service.dataGateway.DeductDataGateway;
import com.gateweb.orm.charge.entity.*;
import com.gateweb.orm.charge.entity.view.DeductFetchView;
import com.gateweb.orm.charge.entity.view.DeductHistoryFetchView;
import com.gateweb.orm.charge.repository.*;
import com.google.common.eventbus.EventBus;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class DeductServiceImpl implements DeductService {
    Logger logger = LoggerFactory.getLogger(DeductServiceImpl.class);

    @Autowired
    DeductRepository deductRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    DeductHistoryRepository deductHistoryRepository;
    @Autowired
    DeductHistoryFetchViewRepository deductHistoryFetchViewRepository;
    @Autowired
    DeductFetchViewRepository deductFetchViewRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    DeductHistoryBuilder deductHistoryBuilder;
    @Autowired
    DeductDataGateway deductDataGatewayImpl;
    @Autowired
    BillRepository billRepository;
    @Autowired
    @Qualifier("chargeSystemEventBus")
    EventBus chargeSystemEventBus;

    @Autowired
    DeductibleItemFilterComponent deductibleItemFilterComponent;
    @Autowired
    DeductibleAmountComponent deductibleAmountComponent;
    @Autowired
    PurchaseDeductVoConverter purchaseDeductVoConverter;

    @Override
    public List<DeductHistoryFetchView> findDeductHistoryFetchViewByDeductId(Long deductId) {
        List<DeductHistoryFetchView> deductHistoryFetchViewList = new ArrayList<>();
        List<DeductHistory> deductHistoryList = deductHistoryRepository.findByDeductId(deductId);
        List<CompletableFuture<Optional<DeductHistoryFetchView>>> completableFutureList = new ArrayList<>();
        deductHistoryList.stream().forEach(deductHistory -> {
            completableFutureList.add(CompletableFuture.supplyAsync(() -> {
                return deductHistoryFetchViewRepository.findById(deductHistory.getDeductHistoryId());
            }));
        });
        completableFutureList.stream().forEach(completableFuture -> {
            try {
                deductHistoryFetchViewList.add(completableFuture.get().get());
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        });
        return deductHistoryFetchViewList;
    }

    @Override
    public List<DeductFetchView> serverSideProcessingSearchByPageInfo(PageInfo pageInfo) {
        List<DeductFetchView> deductFetchViewList = new ArrayList<>();
        //依照客製的搜尋條件進行過濾

        //開始過濾billingItem
        List<Deduct> deductList = deductDataGatewayImpl.searchListByPageInfo(pageInfo);
        deductList.stream().forEach(deduct -> {
            Optional<DeductFetchView> billingItemFetchViewOptional = deductFetchViewRepository.findById(deduct.getDeductId());
            if (billingItemFetchViewOptional.isPresent()) {
                deductFetchViewList.add(billingItemFetchViewOptional.get());
            }
        });
        return deductFetchViewList;
    }

    @Override
    public void transactionBillingDeduct(Deduct deduct) {
        //直接進行結帳
        deduct.setDeductStatus(DeductStatus.B);
        deductRepository.save(deduct);

        //結帳扣抵本身。
        BillingItem billingItem = new BillingItem();
        billingItem.setDeductId(deduct.getDeductId());
        billingItem.setCalculateFromDate(deduct.getEffectiveDate());
        billingItem.setCalculateToDate(deduct.getExpirationDate());
        billingItem.setChargePlan(ChargePlan.INITIATION);
        billingItem.setPaidPlan(PaidPlan.PRE_PAID);
        billingItem.setCount(1);
        billingItem.setExpectedOutDate(LocalDateTime.now());
        billingItem.setTaxExcludedAmount(deduct.getSalesPrice());
        billingItem.setTaxRate(BigDecimal.valueOf(0.05).setScale(4, RoundingMode.HALF_UP));
        billingItem.setIsMemo(false);
        billingItem.setProductCategoryId(deduct.getProductCategoryId());
        billingItem.setCompanyId(deduct.getCompanyId());
        billingItemRepository.save(billingItem);
    }

    @Override
    public List<CustomInterval> filterExistsInterval(List<Deduct> deductList, List<CustomInterval> customIntervalList) {
        List<CustomInterval> overlapList = new ArrayList<>();
        customIntervalList.stream().forEach(customInterval -> {
            deductList.stream().forEach(billingItem -> {
                Interval existsInterval = new Interval(
                        Timestamp.valueOf(billingItem.getEffectiveDate()).getTime()
                        , Timestamp.valueOf(billingItem.getExpirationDate()).getTime()
                );
                Interval calculateInterval = new Interval(customInterval.getStartDateTime(), customInterval.getEndDateTime());
                if (existsInterval.overlaps(calculateInterval)) {
                    overlapList.add(customInterval);
                }
            });
        });
        try {
            customIntervalList.removeAll(overlapList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customIntervalList;
    }

    @Override
    public void enableDeduct(Deduct deduct) {
        //確認該Deduct已繳費完畢
        Optional<BillingItem> billingItemOptional = billingItemRepository.findByDeductId(deduct.getDeductId());
        if (billingItemOptional.isPresent() && billingItemOptional.get().getBillId() != null) {
            Optional<Bill> billOptional = billRepository.findById(billingItemOptional.get().getBillId());
            if (billOptional.isPresent() && billOptional.get().getBillStatus().equals(BillStatus.P)) {
                deduct.setDeductStatus(DeductStatus.E);
                deductRepository.save(deduct);
                //根據商品找出當初的可扣抵數量
                DeductHistory deductHistory = new DeductHistory();
                //查詢此項目產生的結帳項目，預繳產生的結帳項目，billingSource為contract_id,package_ref_id
                //正常情況下只會有一筆
                deductHistory.setDeductId(deduct.getDeductId());
                deductHistory.setAmount(deduct.getQuota());
                deductHistory.setBillId(billOptional.get().getBillId());
                deductHistory.setCreateDate(LocalDateTime.now());
                deductHistoryRepository.save(deductHistory);
            }
        }

    }

    /**
     * 這出來是負值
     *
     * @param fee
     * @param deductibleAmount
     * @return
     */
    private BigDecimal calculateNegativeDeductAmount(BigDecimal fee, BigDecimal deductibleAmount) {
        BigDecimal deductAmount = BigDecimal.ZERO;
        BigDecimal deductResult = deductibleAmount.subtract(fee);
        if (deductResult.compareTo(BigDecimal.ZERO) >= 0) {
            //完全扣抵
            deductAmount = fee.negate();
        } else if (deductResult.compareTo(BigDecimal.ZERO) < 0) {
            //部份扣抵
            deductAmount = deductibleAmount.negate();
        }
        return deductAmount;
    }

    @Override
    public void executeDeduct(BillingItem billingItem) {
        Optional<Company> companyOptional = companyRepository.findByCompanyId(billingItem.getCompanyId().intValue());
        if (companyOptional.isPresent()) {
            executeDeduct(companyOptional.get(), billingItem);
        }
    }

    /**
     * 已經出帳的billingItem不能參與扣抵
     *
     * @param company
     * @param billingItem
     */
    @Override
    public void executeDeduct(Company company, BillingItem billingItem) {
        if (billingItem.getBillId() == null) {
            Set<Deduct> usableDeductList = new HashSet<>(deductibleItemFilterComponent.findUsableDeductCollection(billingItem));
            if (!usableDeductList.isEmpty()) {
                for (Deduct deduct : usableDeductList) {
                    BigDecimal deductibleAmount = deductibleAmountComponent.getDeductibleAmount(deduct.getDeductId());
                    if (deduct.getDeductType().equals(DeductType.FEE_ONLY)) {
                        //取得扣抵後的資料
                        BigDecimal deductAmount = calculateNegativeDeductAmount(billingItem.getTaxExcludedAmount(), deductibleAmount);
                        //寫入歷史記錄
                        DeductHistory deductHistory = deductHistoryBuilder.getBuilder()
                                .withAmount(deductAmount)
                                .withDefaultTimestamp()
                                .withDeduct(deduct)
                                .build();

                        billingItem.setTaxExcludedAmount(billingItem.getTaxExcludedAmount().add(deductAmount));
                        if (billingItem.getTaxExcludedAmount().compareTo(BigDecimal.ZERO) == 0) {
                            billingItem.setIsMemo(true);
                        } else {
                            billingItem.setIsMemo(false);
                        }
                        billingItemRepository.save(billingItem);
                        //真的有扣抵再寫deductHistory
                        if (deductHistory.getAmount().compareTo(BigDecimal.ZERO) != 0) {
                            deductHistory.setDeductBillingItemId(billingItem.getBillingItemId());
                            deductHistoryRepository.save(deductHistory);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void removeDeduct(Deduct deduct) {
        Optional<BillingItem> billingItemOptional = billingItemRepository.findByDeductId(deduct.getDeductId());
        if (billingItemOptional.isPresent() && billingItemOptional.get().getBillId() == null) {
            billingItemRepository.delete(billingItemOptional.get());
            deductRepository.delete(deduct);
        }
    }

    @Override
    public void refreshDeduct(long deductId) {
        Optional<Deduct> deductOptional = deductRepository.findById(deductId);
        if (deductOptional.isPresent()) {
            Set<DeductHistory> restoreDeductHistorySet = new HashSet<>();
            //尋找該DeductId於deductHistory的記錄, 再查詢這些deductHistory是否真的存在於BillingItem中
            Collection<DeductHistory> deductHistoryCollection
                    = deductHistoryRepository.findByDeductIdAndDeductBillingItemIdIsNotNull(deductId);
            deductHistoryCollection.stream().forEach(deductHistory -> {
                Optional<BillingItem> billingItemOpt = billingItemRepository.findById(deductHistory.getDeductBillingItemId());
                if (billingItemOpt.isPresent()) {
                    restoreDeductHistorySet.add(deductHistory);
                }
            });
            restoreDeductHistorySet.stream().forEach(deductHistory -> {
                //刪除這些DeductHistory
                deductHistoryRepository.delete(deductHistory);
            });
            //更新deduct的數值
            BigDecimal deductibleAmount = deductibleAmountComponent.getDeductibleAmount(deductId);
            if (deductibleAmount.compareTo(BigDecimal.ZERO) > 0) {
                deductOptional.get().setDeductStatus(DeductStatus.E);
            }
            deductRepository.save(deductOptional.get());
            //發送訊息到eventBus中
            ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                    EventSource.DEDUCT
                    , EventAction.REFRESH
                    , deductOptional.get().getDeductId()
                    , null
            );
            chargeSystemEventBus.post(chargeSystemEvent);
        }
    }


}
