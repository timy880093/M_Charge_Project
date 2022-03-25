package com.gateweb.charge.chargePolicy.cycle.builder;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.utils.LocalDateTimeUtils;
import org.apache.logging.log4j.core.util.CronExpression;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * cronCycle於超出時不將剩餘的時間納為區間
 * 註，不要再使用 cron pattern了，全部用手寫客製化來處理, pattern可以用來帶參數
 * 這樣也比較容易模擬
 */
public class CronCycle implements ChargeCycle {
    CronExpression cronExpression = null;

    public Optional<ChargeCycle> buildCycle(String... pattern) {
        try {
            cronExpression = new CronExpression(pattern[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (cronExpression == null) {
            return Optional.empty();
        } else {
            return Optional.of(this);
        }
    }

    @Override
    public LocalDateTime getNextValidTimeAfter(LocalDateTime dateTime) {
        Date date = LocalDateTimeUtils.toDate(dateTime);
        Date result = cronExpression.getNextValidTimeAfter(date);
        return LocalDateTimeUtils.fromDate(result);
    }

    @Override
    public LocalDateTime getPrevFireTime(LocalDateTime dateTime) {
        Date date = LocalDateTimeUtils.toDate(dateTime);
        Date result = cronExpression.getPrevFireTime(date);
        return LocalDateTimeUtils.fromDate(result);
    }

    @Override
    public boolean addGlobalEndDateTimeAsValidTimeAfter() {
        return false;
    }

    @Override
    public List<CustomInterval> doPartitioning(CustomInterval customInterval) {
        CustomInterval modifiedInterval = cycleCommon.cronIntervalModifier(customInterval, this);
        return cycleCommon.partitioning(modifiedInterval, this);
    }
}
