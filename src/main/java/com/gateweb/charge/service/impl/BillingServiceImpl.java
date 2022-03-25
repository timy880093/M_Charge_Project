package com.gateweb.charge.service.impl;

import com.gateweb.charge.enumeration.DeductStatus;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.charge.exception.InvalidOperationException;
import com.gateweb.charge.report.bean.BillingItemReport;
import com.gateweb.charge.service.BillingService;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.ChargePackage;
import com.gateweb.orm.charge.entity.Deduct;
import com.gateweb.orm.charge.entity.DeductHistory;
import com.gateweb.orm.charge.entity.view.BillingItemFetchView;
import com.gateweb.orm.charge.repository.*;
import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BillingServiceImpl implements BillingService {
    private Logger logger = LogManager.getLogger(this.getClass().getName());
    Gson gson = new Gson();

    @Autowired
    ChargePackageRepository chargePackageRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    DeductHistoryRepository deductHistoryRepository;
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    DeductRepository deductRepository;

    EventBus chargeSystemEventBus;

    @Autowired
    @Qualifier("chargeSystemEventBus")
    public void setChargeSystemEventBus(EventBus chargeSystemEventBus) {
        this.chargeSystemEventBus = chargeSystemEventBus;
    }

    @Override
    public List<BillingItem> findNotYetCheckBillingItemList() {
        return billingItemRepository.findByBillIdIsNull();
    }

    @Override
    public Optional<Long> getLastSequenceNumber() {
        return billingItemRepository.findMaximumItemId();
    }

    /**
     * 刪除條件有兩個，不能有billId，在在於db中
     * 一般來說如果他有出現在billingItemList中，他就不會有billId
     */
    @Override
    public void deleteBillingItemById(Long billingItemId, Long callerId) throws InvalidOperationException {
        Optional<BillingItem> billingItemOptional = billingItemRepository.findByBillIdIsNullAndBillingItemIdIs(billingItemId);
        if (billingItemOptional.isPresent()) {
            transactionDeleteBillingItem(billingItemOptional.get(), callerId);
        }
    }

    @Override
    public void deleteBillingItemByMap(HashMap<String, Object> requestMap, Long callerId) {
        List<Double> billingItemIdList = (List<Double>) requestMap.get("billingItemIdList");
        billingItemIdList.stream().forEach(billingItemIdDouble -> {
            Long billingItemId = billingItemIdDouble.longValue();
            try {
                deleteBillingItemById(billingItemId, callerId);
            } catch (InvalidOperationException e) {
                logger.error(e.getMessage());
            }
        });
    }

    /**
     * @param billingItem
     * @throws InvalidOperationException
     */
    @Override
    @Transactional
    public void transactionDeleteBillingItem(BillingItem billingItem, Long callerId) throws InvalidOperationException {
        if (billingItem.getBillId() != null) {
            throw new InvalidOperationException("不允許刪除已出帳的待結項目");
        }

        Optional<Deduct> deductOptional = Optional.empty();
        if (billingItem.getDeductId() != null) {
            deductRepository.findById(billingItem.getDeductId());
        }
        //處理扣抵項目的部份，扣抵項還未被使用，退回deduct的狀態為C
        if (deductOptional.isPresent() && deductOptional.get().getDeductStatus().equals(DeductStatus.B)) {
            deductOptional.get().setDeductStatus(DeductStatus.C);
            deductOptional.get().setModifyDate(LocalDateTime.now());
            deductRepository.save(deductOptional.get());
        }

        Set<DeductHistory> deductHistorySet = new HashSet<>(
                deductHistoryRepository.findByDeductBillingItemId(billingItem.getBillingItemId())
        );
        if (!deductHistorySet.isEmpty()) {
            deductHistorySet.stream().forEach(deductHistory -> {
                deductHistoryRepository.delete(deductHistory);
            });
        }
        billingItemRepository.delete(billingItem);
        ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                EventSource.BILLING_ITEM
                , EventAction.CANCEL
                , billingItem.getBillingItemId()
                , callerId
        );
        chargeSystemEventBus.post(chargeSystemEvent);
    }

    @Override
    public BillingItemReport reportDataTransformer(BillingItemFetchView billingItemFetchView) {
        BillingItemReport billingItemReport = new BillingItemReport();
        billingItemReport.setCompanyName(billingItemFetchView.getCompany().getName());
        billingItemReport.setBusinessNo(billingItemFetchView.getCompany().getBusinessNo());
        billingItemReport.setChargePlan(billingItemFetchView.getChargePlan().name());
        billingItemReport.setPaidPlan(billingItemFetchView.getPaidPlan().name());
        //billingItemType只有migration過來的資料會有，因此要處理
        if (billingItemFetchView.getBillingItemType() != null) {
            billingItemReport.setBillingItemType(billingItemFetchView.getBillingItemType().name());
        } else {
            billingItemReport.setBillingItemType("");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = billingItemFetchView.getExpectedOutDate().format(dateTimeFormatter);
        billingItemReport.setExpectedOutDate(formattedDateTime);
        billingItemReport.setTaxExcludedAmount(billingItemFetchView.getTaxExcludedAmount());
        if (billingItemFetchView.getPackageRef() != null) {
            if (billingItemFetchView.getPackageRef().getFromPackageId() != null) {
                Optional<ChargePackage> chargePackageOptional = chargePackageRepository.findById(billingItemFetchView.getPackageRef().getFromPackageId());
                if (chargePackageOptional.isPresent()) {
                    billingItemReport.setPackageName(chargePackageOptional.get().getName());
                }
            }
            if (billingItemFetchView.getPackageRef().getChargeRule() != null) {
                billingItemReport.setChargeModeName(billingItemFetchView.getPackageRef().getChargeRule().getName());
            }
        }
        return billingItemReport;
    }

    @Override
    public List<BillingItemReport> reportDataTransformer(List<BillingItemFetchView> billingItemFetchViewList) {
        List<BillingItemReport> billingItemReportList = new ArrayList<>();
        billingItemFetchViewList.stream().forEach(billingItemFetchView -> {
            billingItemReportList.add(reportDataTransformer(billingItemFetchView));
        });
        return billingItemReportList;
    }

}

