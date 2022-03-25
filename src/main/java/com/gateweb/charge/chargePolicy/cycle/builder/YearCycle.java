package com.gateweb.charge.chargePolicy.cycle.builder;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;

import java.time.LocalDateTime;
import java.util.List;

public class YearCycle implements ChargeCycle {
    int period = 1;

    public YearCycle(int period) {
        this.period = period;
    }

    @Override
    public LocalDateTime getNextValidTimeAfter(LocalDateTime dateTime) {
        return dateTime.plusYears(period);
    }

    @Override
    public LocalDateTime getPrevFireTime(LocalDateTime dateTime) {
        return dateTime.minusYears(period);
    }

    @Override
    public boolean addGlobalEndDateTimeAsValidTimeAfter() {
        return true;
    }

    @Override
    public List<CustomInterval> doPartitioning(CustomInterval customInterval) {
        return cycleCommon.partitioning(customInterval, this);
    }

}
