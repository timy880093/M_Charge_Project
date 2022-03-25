package com.gateweb.charge.service;

import com.gateweb.charge.exception.ItemHaveBeenOccupiedException;
import com.gateweb.charge.exception.MissingRequiredPropertiesException;
import com.gateweb.charge.model.nonMapped.CallerInfo;

import java.util.HashMap;

public interface ChargePackageService {

    void transactionSaveChargePackageByMap(HashMap<String, Object> map, CallerInfo callerInfo);

    void deleteChargePackage(Long packageId, CallerInfo callerInfo) throws MissingRequiredPropertiesException, ItemHaveBeenOccupiedException;
}
