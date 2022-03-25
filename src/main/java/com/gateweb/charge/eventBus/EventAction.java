package com.gateweb.charge.eventBus;

public enum EventAction {
    CREATE,
    CANCEL,
    INITIAL,
    BILLING,
    CANCEL_BILLING,
    ENABLE,
    PAID,
    SEND_NOTICE,
    FEE_CAL,
    FULFILL_EFFECTIVE_DATE,
    TERMINATE,
    DELETE,
    REFRESH
}
