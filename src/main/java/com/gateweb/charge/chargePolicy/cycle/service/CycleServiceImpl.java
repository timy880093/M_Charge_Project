package com.gateweb.charge.chargePolicy.cycle.service;

import com.gateweb.charge.chargePolicy.cycle.ChargeCycleInstanceProvider;
import com.gateweb.charge.chargePolicy.cycle.builder.ChargeCycle;
import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.enumeration.PaidPlan;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.CashDetailRepository;
import com.gateweb.orm.charge.repository.SimpleUserViewRepository;
import com.gateweb.utils.bean.typeConverter.LocalDateTimeConverter;
import org.apache.logging.log4j.core.util.CronExpression;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CycleServiceImpl implements CycleService {
    final ChargeCycleInstanceProvider chargeCycleInstanceProvider = new ChargeCycleInstanceProvider();

    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    CashDetailRepository cashDetailRepository;
    @Autowired
    SimpleUserViewRepository simpleUserViewRepository;


    @Override
    public Date getNextExecuteDate(Date startDateTime, String cronPattern) {
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cronPattern);
        Date nextExecuteDateTime = cronSequenceGenerator.next(startDateTime);
        return nextExecuteDateTime;
    }

    @Override
    public CustomInterval getContractIntervalByContract(Contract contract) {
        CustomInterval contractInterval = new CustomInterval(
                contract.getEffectiveDate()
                , contract.getExpirationDate()
        );
        return contractInterval;
    }

    @Override
    public DateTime getNextCalculateStartDate(Long contractId, Long modeReferenceId, long effectiveDateMillis) {
        DateTime resultDateTime;
        Optional<BillingItem> lastCalculateItem = billingItemRepository.findTopByContractIdAndModeReferenceIdOrderByCalculateToDateDesc(contractId, modeReferenceId);
        if (lastCalculateItem.isPresent()) {
            resultDateTime = new DateTime(lastCalculateItem.get().getCalculateToDate());
        } else {
            resultDateTime = new DateTime(effectiveDateMillis);
        }
        return resultDateTime;
    }

    @Override
    public Optional<CronExpression> getCronExpressionByCronPattern(String pattern) {
        CronExpression result = null;
        try {
            result = new CronExpression(pattern);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return Optional.of(result);
        }
    }

    @Override
    public List<CustomInterval> chargeIntervalListPreviewByMap(Map parameterMap) {
        List<CustomInterval> resultList = new ArrayList<>();
        Optional<ChargeCycle> chargeCycleOptional = Optional.empty();
        Optional<LocalDateTime> effectiveDateOpt = Optional.empty();
        Optional<LocalDateTime> expirationDateOpt = Optional.empty();
        Optional<PaidPlan> paidPlanOpt = Optional.empty();
        Optional<Integer> periodMonthOpt = Optional.empty();
        if (parameterMap.containsKey("periodMonth")) {
            Double periodMonth = (Double) parameterMap.get("periodMonth");
            periodMonthOpt = Optional.of(periodMonth.intValue());
        }
        if (parameterMap.containsKey("paidPlan")) {
            paidPlanOpt = Optional.of(
                    PaidPlan.valueOf(String.valueOf(parameterMap.get("paidPlan")))
            );
        }
        if (parameterMap.containsKey("cycleType")) {
            if (periodMonthOpt.isPresent() && paidPlanOpt.isPresent()) {
                if (paidPlanOpt.get().equals(PaidPlan.POST_PAID)) {
                    chargeCycleOptional = chargeCycleInstanceProvider.genGeneralChargeCycleInstance(
                            String.valueOf(parameterMap.get("cycleType"))
                    );
                } else if (paidPlanOpt.get().equals(PaidPlan.PRE_PAID)) {
                    chargeCycleOptional = chargeCycleInstanceProvider.genRentalChargeCycleInstance(
                            String.valueOf(parameterMap.get("cycleType")), periodMonthOpt.get()
                    );
                }
            }
        }
        if (parameterMap.containsKey("effectiveDateMillis")) {
            LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();
            effectiveDateOpt = Optional.of(
                    localDateTimeConverter.convert(LocalDateTime.class, parameterMap.get("effectiveDateMillis"))
            );
        }
        if (parameterMap.containsKey("expirationDateMillis")) {
            LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();
            expirationDateOpt = Optional.of(
                    localDateTimeConverter.convert(LocalDateTime.class, parameterMap.get("expirationDateMillis"))
            );
        }
        if (chargeCycleOptional.isPresent()
                && effectiveDateOpt.isPresent()
                && expirationDateOpt.isPresent()) {
            resultList = chargeCycleOptional.get().doPartitioning(
                    new CustomInterval(effectiveDateOpt.get(), expirationDateOpt.get())
            );
        }
        return resultList;
    }

}
