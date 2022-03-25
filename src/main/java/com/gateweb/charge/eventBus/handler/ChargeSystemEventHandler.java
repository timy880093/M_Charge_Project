package com.gateweb.charge.eventBus.handler;

import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.google.common.eventbus.EventBus;

public interface ChargeSystemEventHandler {
    void onMessage(EventBus eventBus, ChargeSystemEvent chargeSystemEvent);
}
