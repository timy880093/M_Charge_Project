package com.gateweb.charge.chargePolicy.cycle.builder;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;

import java.util.List;

public class QuarterlyCycle extends MonthCycle {

    public QuarterlyCycle(Integer period) {
        super(period * 3);
    }

    @Override
    public List<CustomInterval> doPartitioning(CustomInterval customInterval) {
        List<CustomInterval> resultList = cycleCommon.partitioning(customInterval, this);
        //需要處理例外清況
        if (resultList.size() > 4) {
            Integer minIndex = null;
            Integer minDayCount = null;
            //合併天數最少的項目到最接近的時間
            for (int i = 0; i < resultList.size(); i++) {
                Integer targetDayCount = resultList.get(i).getIntervalByDays(1).size();
                if (minDayCount == null || targetDayCount < minDayCount) {
                    minIndex = i;
                    minDayCount = targetDayCount;
                }
            }
            //如果是第一個就跟第二個合併
            if (minIndex == 0) {
                //跟第二個合併
                CustomInterval newCustomInterval = new CustomInterval(
                        resultList.get(0).getStartLocalDateTime()
                        , resultList.get(1).getEndLocalDateTime()
                );
                resultList.set(1, newCustomInterval);
                resultList.remove(0);
            }
            //如果是最後一個，就跟倒數第二個合併
            if (minIndex == resultList.size() - 1) {
                //跟倒數第二個合併
                CustomInterval newCustomInterval = new CustomInterval(
                        resultList.get(resultList.size() - 2).getStartLocalDateTime()
                        , resultList.get(resultList.size() - 1).getEndLocalDateTime()
                );
                resultList.set(resultList.size() - 2, newCustomInterval);
                resultList.remove(resultList.size() - 1);
            }
        }
        return resultList;
    }
}
