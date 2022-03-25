package com.gateweb.charge.contract.utils;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.utils.LocalDateTimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class ContractRenewIntervalGenerator {

    public static CustomInterval genGeneralRenewInterval(Contract original) {
        // 根據上一個合約的結束時間計算新的合約結束時間
        //推到該天一開始
        LocalDateTime newEffectiveLocalDateTime = original.getExpirationDate().toLocalDate().plusDays(1).atStartOfDay();
        LocalDateTime newExpirationLocalDateTime = newEffectiveLocalDateTime.plusMonths(original.getPeriodMonth()).minusSeconds(1);
        CustomInterval newContractInterval = new CustomInterval(newEffectiveLocalDateTime, newExpirationLocalDateTime);
        return newContractInterval;
    }

    public static Optional<CustomInterval> genRemainingTypeRenewInterval(final Contract contract, String prevInvoiceDate) {
        Optional<LocalDate> renewEffectiveDate = LocalDateTimeUtils.parseLocalDateFromString(
                prevInvoiceDate
                , "yyyyMMdd"
        );
        LocalDateTime renewEffectiveDateTime = renewEffectiveDate.get().plusDays(1).atStartOfDay();
        //從上一個invoiceDate開始
        Optional<LocalDateTime> renewExpirationDateOpt = getContractExpirationDate(
                renewEffectiveDateTime, contract.getPeriodMonth()
        );
        if (renewExpirationDateOpt.isPresent()) {
            return Optional.of(new CustomInterval(renewEffectiveDateTime, renewExpirationDateOpt.get()));
        } else {
            return Optional.empty();
        }
    }

    public static Optional<LocalDateTime> getContractExpirationDate(LocalDateTime effectiveDate, Integer periodMonth) {
        Optional<LocalDateTime> expirationDateOptional = Optional.empty();
        if (effectiveDate != null && periodMonth != null) {
            //目前只有計算結束日
            Optional<LocalDateTime> effectiveLocalDateTimeOptional = Optional.ofNullable(effectiveDate);
            Optional<Integer> periodMonthOptional = Optional.of(periodMonth);
            if (effectiveLocalDateTimeOptional.isPresent() && periodMonthOptional.isPresent()) {
                expirationDateOptional = Optional.ofNullable(
                        effectiveLocalDateTimeOptional.get().plusMonths(
                                periodMonthOptional.get()
                        ).withHour(0).withMinute(0).withSecond(0).minusSeconds(1)
                );
            }
        }
        return expirationDateOptional;
    }

}
