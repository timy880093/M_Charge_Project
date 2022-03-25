package com.gateweb.charge.chargePolicy.cycle.builder;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AnyCycle implements ChargeCycle {

    @Override
    public LocalDateTime getNextValidTimeAfter(LocalDateTime dateTime) {
        return dateTime;
    }

    @Override
    public LocalDateTime getPrevFireTime(LocalDateTime dateTime) {
        return dateTime;
    }

    @Override
    public boolean addGlobalEndDateTimeAsValidTimeAfter() {
        return false;
    }

    @Override
    public List<CustomInterval> doPartitioning(CustomInterval customInterval) {
        List<CustomInterval> resultList = new ArrayList<>();
        resultList.add(customInterval);
        return resultList;
    }
}
