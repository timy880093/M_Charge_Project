package com.gateweb.charge.eventBus.handler;

import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.service.DeductService;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.DeductHistory;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.DeductHistoryRepository;
import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class BillingItemEventHandler implements ChargeSystemEventHandler {
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    DeductHistoryRepository deductHistoryRepository;

    DeductService deductService;

    @Autowired
    public void setDeductService(DeductService deductService) {
        this.deductService = deductService;
    }

    @Override
    public void onMessage(EventBus eventBus, ChargeSystemEvent chargeSystemEvent) {
        switch (chargeSystemEvent.getEventAction()) {
            case CREATE:
                onBillingItemCreate(chargeSystemEvent.getSourceId());
                break;
            case DELETE:
                onBillingItemDelete(chargeSystemEvent.getSourceId());
                break;
            default:
                logger.error("Unknown Event Action:{}", chargeSystemEvent);
                break;
        }
    }

    /**
     * 預期所有預繳都應於費用產生前建立完成，因此費用一產生就會自動跑扣抵
     *
     * @param billingItemId
     */
    public void onBillingItemCreate(long billingItemId) {
        Optional<BillingItem> billingItemOptional = billingItemRepository.findById(billingItemId);
        if (billingItemOptional.isPresent()) {
            deductService.executeDeduct(billingItemOptional.get());
        }
    }

    public void onBillingItemDelete(long billingItemId) {
        Set<Long> deductIdSet = new HashSet<>();
        //查看是否有存在在DeductHistory當中
        Collection<DeductHistory> deductHistorySet = deductHistoryRepository.findByDeductBillingItemId(billingItemId);
        deductHistorySet.stream().forEach(deductHistory -> {
            deductIdSet.add(deductHistory.getDeductId());
            deductHistoryRepository.delete(deductHistory);
        });
        deductIdSet.stream().forEach(deductId -> {
            deductService.refreshDeduct(deductId);
        });
    }

}
