package com.gateweb.charge.chargePolicy.cycle.builder;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.utils.CustomIntervalUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 這個物件需要額外的初始化
 */
public class GwRentalCalculateCycle extends MonthCycle {
    Integer limitPartition;

    public GwRentalCalculateCycle(int period, int limitPartition) {
        super(period);
        this.period = period;
        this.limitPartition = limitPartition;
    }

    @Override
    public List<CustomInterval> doPartitioning(CustomInterval customInterval) {
        if (super.period != null && limitPartition != null) {
            return partitionResultModifier(
                    cycleCommon.partitioning(customInterval, this)
            );
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 合併超出partitionLimit的部份
     *
     * @param customIntervalList
     */
    public List<CustomInterval> partitionResultModifier(List<CustomInterval> customIntervalList) {
        if (customIntervalList.size() > limitPartition) {
            //合併最後一個
            CustomInterval last1st = customIntervalList.get(customIntervalList.size() - 1);
            CustomInterval last2nd = customIntervalList.get(customIntervalList.size() - 2);
            customIntervalList.remove(last1st);
            customIntervalList.remove(last2nd);
            CustomInterval resultInterval = CustomIntervalUtils.mergeCustomInterval(
                    last1st, last2nd
            );
            customIntervalList.add(resultInterval);
            return partitionResultModifier(customIntervalList);
        } else {
            return customIntervalList;
        }
    }
}
