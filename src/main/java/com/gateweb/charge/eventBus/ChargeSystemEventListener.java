package com.gateweb.charge.eventBus;

import com.gateweb.charge.eventBus.handler.*;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ChargeSystemEventListener {
    EventBus eventBus = new EventBus();

    @Autowired
    BillEventHandler billEventHandler;
    @Autowired
    ContractEventHandler contractEventHandler;
    @Autowired
    DeductEventHandler deductEventHandler;
    @Autowired
    BillingItemEventHandler billingItemEventHandler;
    @Autowired
    NoticeEventHandler noticeEventHandler;

    @PostConstruct
    EventBus initialEventBus() {
        eventBus.register(this);
        return eventBus;
    }

    @Bean
    EventBus chargeSystemEventBus() {
        return eventBus;
    }

    protected final Logger logger = LogManager.getLogger(getClass());

    @Subscribe
    public void chargeSystemEventHandler(ChargeSystemEvent chargeSystemEvent) {
        logger.info(chargeSystemEvent.toString());
        switch (chargeSystemEvent.getEventSource()) {
            case BILL:
                billEventHandler.onMessage(eventBus, chargeSystemEvent);
                break;
            case CONTRACT:
                contractEventHandler.onMessage(eventBus, chargeSystemEvent);
                break;
            case DEDUCT:
                deductEventHandler.onMessage(eventBus, chargeSystemEvent);
                break;
            case BILLING_ITEM:
                billingItemEventHandler.onMessage(eventBus, chargeSystemEvent);
                break;
            case NOTICE:
                noticeEventHandler.onMessage(eventBus, chargeSystemEvent);
                break;
            default:
                logger.error("unknown event:{}", chargeSystemEvent);
                break;
        }
    }

}
