package com.gateweb.charge.eventBus.handler;

import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.service.impl.NoticeService;
import com.gateweb.orm.charge.repository.NoticeRepository;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NoticeEventHandler implements ChargeSystemEventHandler {
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    NoticeService noticeService;

    @Override
    public void onMessage(EventBus eventBus, ChargeSystemEvent chargeSystemEvent) {
        switch (chargeSystemEvent.getEventAction()) {

        }
    }

}
