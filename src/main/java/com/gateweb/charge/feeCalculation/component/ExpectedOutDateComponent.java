package com.gateweb.charge.feeCalculation.component;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExpectedOutDateComponent {

    public LocalDateTime getExpectedOutDate(LocalDateTime chargeStartDateTime) {
        return chargeStartDateTime.minusMonths(3).withHour(0).withMinute(0).withSecond(0);
    }
}
