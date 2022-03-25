package com.gateweb.charge.chargePolicy.cycle.builder;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 例1：群益期貨：20200320~20200319
 * 因為202001,202002的計算責任在202003身上，因此需要往前推
 * 同樣原因202101,202102並不在下一個合約的計算範圍內
 * <p>
 * 例2:網裕:20190211~20200210
 * 雖然為偶數月，但因為201901,201902的計算責任在201903身上，所以也要往前推
 * 同樣原因，其結尾的202001並不能出現在範圍內
 */
public class GwOverageBillingCycle extends CronCycle {

    public GwOverageBillingCycle() {
        super.buildCycle("0 0 0 1 JAN,MAR,MAY,JUL,SEP,NOV ?");
    }

    @Override
    public List<CustomInterval> doPartitioning(CustomInterval customInterval) {
        CustomInterval modifiedInterval = calResponsibilityModifier(customInterval);
        List<CustomInterval> resultIntervalList = cycleCommon.partitioning(modifiedInterval, this);
        return resultIntervalList;
    }

    public CustomInterval calResponsibilityModifier(CustomInterval customInterval) {
        LocalDateTime newStart;
        LocalDateTime newEnd;
        if (customInterval.getStartLocalDateTime().getMonth().getValue() % 2 != 0) {
            newStart = customInterval.getStartLocalDateTime().minusMonths(2);
            newEnd = customInterval.getEndLocalDateTime().minusMonths(2);
        } else {
            newStart = customInterval.getStartLocalDateTime().minusMonths(1);
            newEnd = customInterval.getEndLocalDateTime().minusMonths(1);
        }
        CustomInterval resultInterval = cycleCommon.commonIntervalModifier(new CustomInterval(newStart, newEnd));
        return resultInterval;
    }

}
