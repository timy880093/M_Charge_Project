package com.gateweb.charge.chargePolicy.cycle.builder;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;

import java.time.LocalDateTime;
import java.util.List;

public class MonthCycle implements ChargeCycle {
    Integer period;

    public MonthCycle(Integer period) {
        this.period = period;
    }

    @Override
    public LocalDateTime getNextValidTimeAfter(LocalDateTime dateTime) {
        return dateTime.plusMonths(period).withDayOfMonth(1);
    }

    @Override
    public LocalDateTime getPrevFireTime(LocalDateTime dateTime) {
        return dateTime.minusMonths(period).withDayOfMonth(1);
    }

    @Override
    public boolean addGlobalEndDateTimeAsValidTimeAfter() {
        return true;
    }

    @Override
    public List<CustomInterval> doPartitioning(CustomInterval customInterval) {
        CustomInterval modifiedInterval = cycleCommon.commonIntervalModifier(customInterval);
        return cycleCommon.partitioning(modifiedInterval, this);
    }

}
