package com.gateweb.charge.chargePolicy.cycle.builder;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;

import java.time.LocalDateTime;
import java.util.List;

public interface ChargeCycle {
    CycleCommon cycleCommon = new CycleCommon();

    LocalDateTime getNextValidTimeAfter(LocalDateTime dateTime);

    LocalDateTime getPrevFireTime(LocalDateTime dateTime);

    /**
     * 是否保留最後一個未滿執行日期的區間
     *
     * @return
     */
    boolean addGlobalEndDateTimeAsValidTimeAfter();

    List<CustomInterval> doPartitioning(CustomInterval customInterval);
}
