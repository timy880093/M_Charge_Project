package com.gateweb.bridge.service;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.exception.UnsupportedChargeIntervalException;
import com.gateweb.orm.charge.entity.MigEventRecordEntity;
import com.gateweb.orm.einv.entity.MigStateRecordEntity;
import org.springframework.dao.CannotAcquireLockException;

import java.lang.reflect.InvocationTargetException;

public interface SyncMigDataFacade {

    MigEventRecordEntity generateMigEventData(MigStateRecordEntity migStateRecordEntity, CustomInterval chargeInterval) throws InvocationTargetException, IllegalAccessException;

    void transactionSyncMigEventDataFromEinvDatabase(CustomInterval chargeInterval);

    void transactionSyncMigEventDataFromEinvDatabaseByDaySlice(CustomInterval chargeInterval) throws UnsupportedChargeIntervalException, CannotAcquireLockException;

    boolean isMigEventRecordExists(Long eventId);

    CustomInterval getLastSyncInterval();
}
