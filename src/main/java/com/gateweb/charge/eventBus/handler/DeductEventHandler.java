package com.gateweb.charge.eventBus.handler;

import com.gateweb.charge.deduct.component.DeductibleAmountComponent;
import com.gateweb.charge.enumeration.DeductStatus;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.service.DeductService;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Deduct;
import com.gateweb.orm.charge.entity.DeductHistory;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.DeductHistoryRepository;
import com.gateweb.orm.charge.repository.DeductRepository;
import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DeductEventHandler implements ChargeSystemEventHandler {
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    DeductRepository deductRepository;
    @Autowired
    DeductHistoryRepository deductHistoryRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    DeductibleAmountComponent deductibleAmountComponent;

    DeductService deductService;

    @Autowired
    public void setDeductService(DeductService deductService) {
        this.deductService = deductService;
    }

    @Override
    public void onMessage(EventBus eventBus, ChargeSystemEvent chargeSystemEvent) {
        switch (chargeSystemEvent.getEventAction()) {
            case CREATE:
                onDeductCreate(chargeSystemEvent.getSourceId());
                break;
        }
    }

    /**
     * 於在預繳建立時產生billingItem
     *
     * @param deductId
     */
    public void onDeductCreate(long deductId) {
        Optional<Deduct> deductOptional = deductRepository.findById(deductId);
        if (deductOptional.isPresent()) {
            deductService.transactionBillingDeduct(deductOptional.get());
        }
    }

}
