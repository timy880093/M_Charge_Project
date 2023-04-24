package com.gateweb.utils;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class CustomIntervalUtils {

    public static CustomInterval generateCustomIntervalByYearMonth(String yearMonth) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        DateTime fromDateTime = new DateTime(simpleDateFormat.parse(yearMonth));
        org.joda.time.LocalDateTime fromLocalDateTime = fromDateTime.toLocalDateTime().withDayOfMonth(1).withHourOfDay(0);
        org.joda.time.LocalDateTime toLocalDateTime = fromDateTime.toLocalDateTime().plusMonths(1).plusSeconds(-1);
        return new CustomInterval(fromLocalDateTime.toDateTime(), toLocalDateTime.toDateTime());
    }

    public static Interval getJodaInterval(LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime) {
        return new Interval(
                Timestamp.valueOf(startLocalDateTime).getTime()
                , Timestamp.valueOf(endLocalDateTime).getTime()
        );
    }

    public void sortCustomIntervalListAsc(List<CustomInterval> intervalList) {
        intervalList.stream()
                .sorted(new Comparator<CustomInterval>() {
                    @Override
                    public int compare(CustomInterval o1, CustomInterval o2) {
                        if (o1.getStartDateTime().isAfter(o2.getStartDateTime())) {
                            return 1;
                        } else if (o1.getStartDateTime().isBefore(o2.getStartDateTime())) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
    }

    public static CustomInterval mergeCustomInterval(CustomInterval customInterval1, CustomInterval customInterval2) {
        if (customInterval2.getStartLocalDateTime().isAfter(customInterval1.getStartLocalDateTime())) {
            return new CustomInterval(customInterval1.getStartLocalDateTime(), customInterval2.getEndLocalDateTime());
        } else {
            return new CustomInterval(customInterval2.getStartLocalDateTime(), customInterval1.getEndLocalDateTime());
        }
    }

}
