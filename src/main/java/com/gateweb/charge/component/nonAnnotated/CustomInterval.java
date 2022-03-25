package com.gateweb.charge.component.nonAnnotated;

import com.gateweb.charge.enumeration.ChargeIntervalType;
import com.gateweb.charge.exception.UnsupportedChargeIntervalException;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomInterval {
    DateTime startDateTime;
    DateTime endDateTime;

    public CustomInterval(CustomInterval customInterval) {
        this.startDateTime = customInterval.getStartDateTime();
        this.endDateTime = customInterval.getEndDateTime();
    }

    public CustomInterval() {
        this.startDateTime = new DateTime();
        this.endDateTime = new DateTime();
    }

    public CustomInterval(@NotNull DateTime startDateTime, @NotNull DateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public CustomInterval(@NotNull Date startDate, @NotNull Date endDate) {
        DateTime startDateTime = new DateTime(startDate);
        DateTime endDateTime = new DateTime(endDate);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public CustomInterval(@NotNull java.util.Date startDate, @NotNull java.util.Date endDate) {
        DateTime startDateTime = new DateTime(startDate);
        DateTime endDateTime = new DateTime(endDate);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public CustomInterval(@NotNull Timestamp startTimestamp, @NotNull Timestamp endTimestamp) {
        DateTime startDateTime = new DateTime(startTimestamp.getTime());
        DateTime endDateTime = new DateTime(endTimestamp.getTime());
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public CustomInterval(@NotNull LocalDateTime startLocalDateTime, @NotNull LocalDateTime endLocalDateTime) {
        this.startDateTime = new DateTime(Timestamp.valueOf(startLocalDateTime).getTime());
        this.endDateTime = new DateTime(Timestamp.valueOf(endLocalDateTime).getTime());
    }

    /**
     * 在這裡實作將時間切片的方法
     *
     * @param chargeIntervalType
     * @param counts
     * @return
     */

    public List<CustomInterval> getInterval(ChargeIntervalType chargeIntervalType, int counts) throws UnsupportedChargeIntervalException {
        if (counts <= 0) {
            throw new UnsupportedChargeIntervalException("不支援的日期類型");
        }
        switch (chargeIntervalType) {
            case DAYS:
                return getIntervalByDays(counts);
            case YEARS:
                return getIntervalByYears(counts);
            case MONTHS:
                return getIntervalByMonths(counts);
            default:
                throw new UnsupportedChargeIntervalException("不支援的日期類型");
        }
    }

    /**
     * 將日期切分
     *
     * @param counts
     * @return
     */
    public List<CustomInterval> getIntervalByDays(int counts) {
        List<CustomInterval> resultList = new ArrayList<>();
        DateTime newStartDateTime = startDateTime;
        for (DateTime newEndDateTime = startDateTime.plusDays(counts); newEndDateTime.isBefore(endDateTime.getMillis()); newEndDateTime = newEndDateTime.plusDays(counts)) {
            resultList.add(new CustomInterval(newStartDateTime, newEndDateTime));
            newStartDateTime = newEndDateTime;
        }
        return resultList;
    }

    public List<CustomInterval> getIntervalByMonths(int counts) {
        List<CustomInterval> resultList = new ArrayList<>();
        DateTime newStartDateTime = startDateTime;
        for (DateTime newEndDateTime = startDateTime.plusMonths(counts); newEndDateTime.isBefore(endDateTime.getMillis()); newEndDateTime = newEndDateTime.plusMonths(counts)) {
            resultList.add(new CustomInterval(newStartDateTime, newEndDateTime));
        }
        return resultList;
    }

    public List<CustomInterval> getIntervalByYears(int counts) {
        List<CustomInterval> resultList = new ArrayList<>();
        DateTime newStartDateTime = startDateTime;
        for (DateTime newEndDateTime = startDateTime.plusYears(counts); newEndDateTime.isBefore(endDateTime.getMillis()); newEndDateTime = newEndDateTime.plusMonths(counts)) {
            resultList.add(new CustomInterval(newStartDateTime, newEndDateTime));
        }
        return resultList;
    }

    /**
     * [09:00 to 10:00) contains [09:00 to 10:00)  = true
     * [09:00 to 10:00) contains [09:00 to 09:30)  = true
     * [09:00 to 10:00) contains [09:30 to 10:00)  = true
     * [09:00 to 10:00) contains [09:15 to 09:45)  = true
     * [09:00 to 10:00) contains [09:00 to 09:00)  = true
     * <p>
     * [09:00 to 10:00) contains [08:59 to 10:00)  = false (otherStart before thisStart)
     * [09:00 to 10:00) contains [09:00 to 10:01)  = false (otherEnd after thisEnd)
     * [09:00 to 10:00) contains [10:00 to 10:00)  = false (otherStart equals thisEnd)
     * <p>
     * [14:00 to 14:00) contains [14:00 to 14:00)  = false (zero duration contains nothing)
     *
     * @param customInterval
     * @return
     */
    public boolean contains(CustomInterval customInterval) {
        Interval parentInterval = new Interval(startDateTime.getMillis(), endDateTime.getMillis());
        Interval childInterval = new Interval(customInterval.startDateTime.getMillis(), customInterval.endDateTime.getMillis());
        return parentInterval.contains(childInterval);
    }

    public boolean overlaps(CustomInterval customInterval) {
        Interval parentInterval = new Interval(startDateTime.getMillis(), endDateTime.getMillis());
        Interval childInterval = new Interval(customInterval.startDateTime.getMillis(), customInterval.endDateTime.getMillis());
        return parentInterval.overlaps(childInterval);
    }

    /**
     * [09:00 to 10:00) contains 08:59  = false (before start)
     * [09:00 to 10:00) contains 09:00  = true
     * [09:00 to 10:00) contains 09:59  = true
     * [09:00 to 10:00) contains 10:00  = false (equals end)
     * [09:00 to 10:00) contains 10:01  = false (after end)
     *
     * @param dateTime
     * @return
     */
    public boolean contains(DateTime dateTime) {
        Interval parentInterval = new Interval(startDateTime.getMillis(), endDateTime.getMillis());
        return parentInterval.contains(dateTime.getMillis());
    }

    public boolean contains(long milliseconds) {
        DateTime targetDateTime = new DateTime(milliseconds);
        return contains(targetDateTime);
    }

    public boolean contains(LocalDateTime localDateTime) {
        Interval parentInterval = new Interval(startDateTime.getMillis(), endDateTime.getMillis());
        return parentInterval.contains(new DateTime(Timestamp.valueOf(localDateTime).getTime()));
    }

    public Date getSqlStartDate() {
        return new Date(startDateTime.getMillis());
    }

    public Date getSqlEndDate() {
        return new Date(endDateTime.getMillis());
    }

    public java.util.Date getUtilStartDate() {
        return new java.util.Date(startDateTime.getMillis());
    }

    public java.util.Date getUtilEndDate() {
        return new java.util.Date(endDateTime.getMillis());
    }

    public Timestamp getSqlStartTimestamp() {
        return new Timestamp(startDateTime.getMillis());
    }

    public Timestamp getSqlEndTimestamp() {
        return new Timestamp(endDateTime.getMillis());
    }

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getStartLocalDateTime() {
        return new Timestamp(startDateTime.getMillis()).toLocalDateTime();
    }

    public void setStartLocalDateTime(LocalDateTime localDateTime) {
        this.startDateTime = new DateTime(Timestamp.valueOf(localDateTime).getTime());
    }

    public void setEndLocalDateTime(LocalDateTime localDateTime) {
        this.endDateTime = new DateTime(Timestamp.valueOf(localDateTime).getTime());
    }

    public void setStartDateTime(DateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setStartDateTime(Long milliseconds) {
        this.startDateTime = new DateTime(milliseconds);
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public LocalDateTime getEndLocalDateTime() {
        return new Timestamp(endDateTime.getMillis()).toLocalDateTime();
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setEndDateTime(Long milliseconds) {
        this.endDateTime = new DateTime(milliseconds);
    }

    public boolean valid() {
        return getStartLocalDateTime().isBefore(getEndLocalDateTime());
    }

    @Override
    public String toString() {
        return "CustomInterval{" +
                "startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
