package com.gateweb.charge.service;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Deduct;
import com.gateweb.orm.charge.entity.view.DeductFetchView;
import com.gateweb.orm.charge.entity.view.DeductHistoryFetchView;

import java.util.List;

public interface DeductService {

    List<DeductHistoryFetchView> findDeductHistoryFetchViewByDeductId(Long deductId);

    List<DeductFetchView> serverSideProcessingSearchByPageInfo(PageInfo pageInfo);

    void transactionBillingDeduct(Deduct deduct);

    List<CustomInterval> filterExistsInterval(List<Deduct> deductList, List<CustomInterval> customIntervalList);

    void enableDeduct(Deduct deduct);

    void executeDeduct(BillingItem billingItem);

    void executeDeduct(Company company, BillingItem billingItem);

    void removeDeduct(Deduct deduct);

    void refreshDeduct(long deductId);
}
