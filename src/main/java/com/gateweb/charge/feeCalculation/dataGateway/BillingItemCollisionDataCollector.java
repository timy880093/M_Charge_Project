package com.gateweb.charge.feeCalculation.dataGateway;

import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BillingItemCollisionDataCollector {
    @Autowired
    BillingItemRepository billingItemRepository;

    @Deprecated
    public Collection<BillingItem> getCollisionBillingItem(Long company, Long packageRefId) {
        //過濾已經存在的時間
        List<BillingItem> existsBillingItemList = billingItemRepository
                .findByCompanyIdAndPackageRefIdAndDeductIdIsNull(company, packageRefId);

        Set<BillingItem> removableBillingItemSet = existsBillingItemList.stream().filter(billingItem -> {
            if (billingItem.getBillId() != null) {
                return false;
            } else {
                return true;
            }
        }).collect(Collectors.toSet());
        return removableBillingItemSet;
    }

    public Set<BillingItem> getCollisionBillingItem(BillingItem billingItem) {
        Set<BillingItem> billingItemOptional = new HashSet<>(billingItemRepository.findByCompanyIdAndPackageRefIdAndCalculateFromDateAndCalculateToDate(
                billingItem.getCompanyId()
                , billingItem.getPackageRefId()
                , billingItem.getCalculateFromDate()
                , billingItem.getCalculateToDate()
        ));
        return billingItemOptional;
    }
}
