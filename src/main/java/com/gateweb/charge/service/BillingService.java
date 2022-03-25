package com.gateweb.charge.service;

import com.gateweb.charge.exception.InvalidOperationException;
import com.gateweb.charge.report.bean.BillingItemReport;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.view.BillingItemFetchView;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface BillingService {

    List<BillingItem> findNotYetCheckBillingItemList();

    Optional<Long> getLastSequenceNumber();

    void deleteBillingItemById(Long billingItemId, Long callerId) throws InvalidOperationException;

    void deleteBillingItemByMap(HashMap<String, Object> requestMap, Long callerId);

    @Transactional
    void transactionDeleteBillingItem(BillingItem billingItem, Long callerId) throws InvalidOperationException;

    BillingItemReport reportDataTransformer(BillingItemFetchView billingItemFetchView);

    List<BillingItemReport> reportDataTransformer(List<BillingItemFetchView> billingItemFetchViewList);
}
