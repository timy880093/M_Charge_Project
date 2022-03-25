package com.gateweb.charge.chargePolicy.cycle.builder;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;

import java.util.List;

public class GwOverageBillingCalCycle extends GwOverageBillingCycle {

    /**
     * 他的分割邏輯比較不一樣，並非依照原來的時間進行分割，他還要把時間改為一個月一個月
     * 因為往前追溯的關系，要頭不要尾
     * 若結束日期剛好為奇數月，也就是計算超額的那一個月，則放棄該月份
     * 因為該月份的超額計算應交由下一個合約進行
     * <p>
     * 而若起始日期為奇數月，則應往前追兩個月計算超額
     *
     * @param customInterval
     * @return
     */
    @Override
    public List<CustomInterval> doPartitioning(CustomInterval customInterval) {
        CustomInterval modifiedInterval = calResponsibilityModifier(customInterval);
        MonthCycle monthCycle = new MonthCycle(1);
        List<CustomInterval> resultPartitionList = cycleCommon.nonLastDateFilter(
                monthCycle.doPartitioning(modifiedInterval)
        );
        return resultPartitionList;
    }

}
