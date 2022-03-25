package com.gateweb.charge.service;

import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.exception.ItemHaveBeenOccupiedException;
import com.gateweb.charge.exception.MissingRequiredPropertiesException;

import java.util.Map;

public interface ChargeRuleService {

    void saveOrUpdateChargeRuleByMap(Map<String, Object> map, CallerInfo callerInfo);

    void transactionDeleteChargeRule(Long chargeRuleId, CallerInfo callerInfo) throws MissingRequiredPropertiesException, ItemHaveBeenOccupiedException;
}
