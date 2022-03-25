package com.gateweb.charge.chargePolicy.cycle.builder;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.utils.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CycleCommon {
    /**
     * 將起始日推至一天剛開始，將結束日推至一天的最後一秒
     *
     * @param customInterval
     * @return
     */
    public CustomInterval commonIntervalModifier(final CustomInterval customInterval) {
        CustomInterval resultInterval = new CustomInterval(customInterval);
        resultInterval.setStartLocalDateTime(customInterval.getStartLocalDateTime().withDayOfMonth(1));
        resultInterval.setEndLocalDateTime(customInterval.getEndLocalDateTime().plusDays(1).withHour(0).withMinute(0).withSecond(0).minusSeconds(1));
        return resultInterval;
    }

    public CustomInterval cronIntervalModifier(final CustomInterval customInterval, ChargeCycle cycle) {
        CustomInterval resultInterval = commonIntervalModifier(customInterval);
        //根據不同的cycle會有不一樣的情況。
        //cronCycle要從第一次執行條件開始
        //若該時間等於他的下次執行時間，應該要納入
        LocalDateTime tempStartLocalDateTime = cycle.getNextValidTimeAfter(resultInterval.getStartLocalDateTime());
        if (tempStartLocalDateTime.isEqual(resultInterval.getStartLocalDateTime())) {
            LocalDateTime resultStartLocalDateTime = resultInterval.getStartLocalDateTime().minusSeconds(1);
            resultInterval.setStartLocalDateTime(resultStartLocalDateTime);
        }
        //結尾也有同樣的情況
        LocalDateTime tempEndLocalDateTime = cycle.getNextValidTimeAfter(resultInterval.getEndLocalDateTime()).minusSeconds(1);
        if (tempEndLocalDateTime.isEqual(resultInterval.getEndLocalDateTime())) {
            LocalDateTime resultEndLocalDateTime = resultInterval.getEndLocalDateTime().plusSeconds(1);
            resultInterval.setEndLocalDateTime(resultEndLocalDateTime);
        }
        return resultInterval;
    }

    /**
     * 移除未滿的天數
     *
     * @param customIntervalList
     * @return
     */
    public List<CustomInterval> nonLastDateFilter(List<CustomInterval> customIntervalList) {
        List<CustomInterval> resultList = new ArrayList<>();
        customIntervalList.stream().forEach(resultInterval -> {
            int month = resultInterval.getEndLocalDateTime().getDayOfMonth();
            int maxMonthDay = resultInterval.getEndDateTime().plusMonths(1)
                    .withDayOfMonth(1).withTimeAtStartOfDay().minusSeconds(1).getDayOfMonth();
            if (month == maxMonthDay) {
                resultList.add(resultInterval);
            }
        });
        return resultList;
    }

    /**
     * 依據時間切分區間
     * 使用當前的CustomInterval做為最大區間，不能大於該區間的結束日期。
     * 跑完為排序好的結果。
     *
     * @return
     */
    public List<CustomInterval> partitioning(final CustomInterval globalInterval, final ChargeCycle cycle) {
        List<CustomInterval> resultIntervalList = new ArrayList<>();
        LocalDateTime startLocalDateTime = LocalDateTimeUtils.fromJodaDateTime(globalInterval.getStartDateTime());
        LocalDateTime nextValidLocalDateTime = cycle.getNextValidTimeAfter(startLocalDateTime);
        LocalDateTime endLocalDateTime = LocalDateTimeUtils.fromJodaDateTime(globalInterval.getEndDateTime());
        CustomInterval restInterval = new CustomInterval(nextValidLocalDateTime, endLocalDateTime);
        //下次執行時間於範圍內
        if (nextValidLocalDateTime.isBefore(endLocalDateTime) || nextValidLocalDateTime.isEqual(endLocalDateTime)) {
            CustomInterval resultInterval = new CustomInterval(startLocalDateTime, nextValidLocalDateTime.plusSeconds(-1));
            resultIntervalList.add(resultInterval);
            resultIntervalList.addAll(partitioning(restInterval, cycle));
        } else if (nextValidLocalDateTime.isAfter(endLocalDateTime) && cycle.addGlobalEndDateTimeAsValidTimeAfter()) {
            resultIntervalList.add(globalInterval);
        }
        return resultIntervalList;
    }
}
