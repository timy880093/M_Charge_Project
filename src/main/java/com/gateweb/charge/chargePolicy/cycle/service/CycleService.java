package com.gateweb.charge.chargePolicy.cycle.service;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.orm.charge.entity.Contract;
import org.apache.logging.log4j.core.util.CronExpression;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CycleService {

    Date getNextExecuteDate(Date startDateTime, String cronPattern);

    CustomInterval getContractIntervalByContract(Contract contract);

    DateTime getNextCalculateStartDate(Long contractId, Long modeReferenceId, long effectiveDateMillis);

    Optional<CronExpression> getCronExpressionByCronPattern(String pattern);

    List<CustomInterval> chargeIntervalListPreviewByMap(Map parameterMap);
}
