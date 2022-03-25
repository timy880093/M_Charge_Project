package com.gateweb.bridge.service.impl;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.enumeration.ChargeIntervalType;
import com.gateweb.charge.exception.UnsupportedChargeIntervalException;
import com.gateweb.bridge.service.SyncMigDataFacade;
import com.gateweb.orm.charge.entity.MigEventRecordEntity;
import com.gateweb.orm.charge.repository.MigEventRecordRepository;
import com.gateweb.orm.einv.entity.Company;
import com.gateweb.orm.einv.entity.MigStateRecordEntity;
import com.gateweb.orm.einv.repository.EinvCompanyRepository;
import com.gateweb.orm.einv.repository.EinvMigStateRecordRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("syncMigDataFacade")
public class SyncMigDataFacadeImpl implements SyncMigDataFacade {
    protected static final Logger logger = LogManager.getLogger(SyncMigDataFacadeImpl.class);

    @Autowired
    EinvMigStateRecordRepository einvMigStateRecordRepository;

    @Autowired
    MigEventRecordRepository migEventRecordRepository;

    @Autowired
    EinvCompanyRepository einvCompanyRepository;

    @Autowired
    SyncMigDataFacade syncMigDataFacade;


    /**
     * 因為發票資料有可能是空的，若不額外查詢CompanyKey的資訊則會變成無用資料，因此加入了uploadBusinessNo做處理。
     *
     * @param migStateRecordEntity
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Override
    public MigEventRecordEntity generateMigEventData(MigStateRecordEntity migStateRecordEntity, CustomInterval chargeInterval) throws InvocationTargetException, IllegalAccessException {
        MigEventRecordEntity newMigEventRecordEntity = new MigEventRecordEntity();
        BeanUtils.copyProperties(newMigEventRecordEntity, migStateRecordEntity);
        //區分介接及一般web開的客戶
        if (migStateRecordEntity.getUploadCompanyKey() != null) {
            //查詢上傳的公司
            Company uploadCompany = einvCompanyRepository.findByCompanyKeyEquals(migStateRecordEntity.getUploadCompanyKey());
            newMigEventRecordEntity.setUploadBusinessNo(uploadCompany.getBusinessNo());
        } else {
            //WEB開的客戶
            newMigEventRecordEntity.setUploadBusinessNo(migStateRecordEntity.getSellerIdentifier());
        }
        return newMigEventRecordEntity;
    }

    @Override
    public void transactionSyncMigEventDataFromEinvDatabase(CustomInterval chargeInterval) {
        List<MigStateRecordEntity> migStateRecordEntityList = einvMigStateRecordRepository.findByCreateDateAfterAndCreateDateBefore(chargeInterval.getSqlStartTimestamp(), chargeInterval.getSqlEndTimestamp());
        logger.info("Sync MigEventData from :" + chargeInterval.getSqlStartTimestamp().toString() + ", to:" + chargeInterval.getSqlEndTimestamp());
        for (MigStateRecordEntity einvMigStateRecordEntity : migStateRecordEntityList) {
            try {
                MigEventRecordEntity migEventRecordEntity = generateMigEventData(einvMigStateRecordEntity, chargeInterval);
                migEventRecordRepository.save(migEventRecordEntity);
                logger.info("Insert MigEventData :" + migEventRecordEntity.getId());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void transactionSyncMigEventDataFromEinvDatabaseByDaySlice(CustomInterval chargeInterval) throws UnsupportedChargeIntervalException, CannotAcquireLockException {
        List<CustomInterval> chargeIntervals = chargeInterval.getInterval(ChargeIntervalType.DAYS, 1);
        for (CustomInterval chargeIntervalSlice : chargeIntervals) {
            //會遇到查詢時間過長無法查出的問題，所以做成時間切片一天一天查。
            syncMigDataFacade.transactionSyncMigEventDataFromEinvDatabase(chargeIntervalSlice);
        }
    }

    @Override
    public boolean isMigEventRecordExists(Long eventId) {
        MigEventRecordEntity migEventRecordEntity = migEventRecordRepository.findById(eventId).get();
        if (migEventRecordEntity != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * default 一年前
     *
     * @return
     */
    @Override
    public CustomInterval getLastSyncInterval() {
        Optional<Timestamp> maximumSyncTimestamp = migEventRecordRepository.findMaximumSyncDate();
        DateTime lastSyncDateTime;
        if (maximumSyncTimestamp.isPresent() && maximumSyncTimestamp.isPresent()) {
            lastSyncDateTime = new DateTime(maximumSyncTimestamp.get());
        } else {
            lastSyncDateTime = new DateTime(new Date().getTime());
            lastSyncDateTime = lastSyncDateTime.plusDays(365);
        }
        CustomInterval resultChargeInterval = null;
        resultChargeInterval = new CustomInterval(lastSyncDateTime, lastSyncDateTime);
        return resultChargeInterval;
    }

}
