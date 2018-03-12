package com.gateweb.charge.service.impl;

import com.gateweb.charge.model.InvoiceMainEntity;
import com.gateweb.charge.repository.InvoiceMainRepository;
import com.gateweb.charge.service.SyncInvoiceDataFacade;
import com.gateweb.einv.model.InvoiceMain;
import com.gateweb.einv.repository.EinvInvoiceMainRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Eason on 3/12/2018.
 */
@Service
public class SyncInvoiceDataFacadeImpl implements SyncInvoiceDataFacade {
    protected static final Logger logger = LogManager.getLogger(SyncInvoiceDataFacadeImpl.class);

    @Autowired
    InvoiceMainRepository invoiceMainRepository;

    @Autowired
    EinvInvoiceMainRepository einvInvoiceMainRepository;

    @Autowired


    @Override
    //應該根據修改日期才可以得到完全正確的資料，因為發票資料包括折讓及註銷的部份。
    public void syncInvoiceMainDataFromEinvDatabase(Timestamp previousTime) throws InvocationTargetException, IllegalAccessException {
        List<InvoiceMain> einvInvoiceMainEntityList = einvInvoiceMainRepository.findByModifyDateIsGreaterThan(previousTime);
        for(InvoiceMain einvInvoiceMain: einvInvoiceMainEntityList){
            InvoiceMainEntity existsInvoiceMainEntity
                    = invoiceMainRepository.findByInvoiceIdAndCYearMonth(einvInvoiceMain.getInvoiceId(),einvInvoiceMain.getcYearMonth());
            if(existsInvoiceMainEntity!=null){
                transactionUpdateInvoiceMainDataFromEinvDatabase(existsInvoiceMainEntity, einvInvoiceMain);
            }else{
                transactionInsertInvoiceMainDataFromEinvDatabase(einvInvoiceMain);
            }
        }
    }

    public void transactionUpdateInvoiceMainDataFromEinvDatabase(
            InvoiceMainEntity existsInvoiceMainEntity
            , InvoiceMain einvInvoiceMainEntity) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(existsInvoiceMainEntity,einvInvoiceMainEntity);
        logger.info("Update exists invoice data");
        invoiceMainRepository.save(existsInvoiceMainEntity);
    }

    public void transactionInsertInvoiceMainDataFromEinvDatabase(InvoiceMain einvInvoiceMainEntity) throws InvocationTargetException, IllegalAccessException {
        InvoiceMainEntity newInvoiceMainEntity = new InvoiceMainEntity();
        BeanUtils.copyProperties(newInvoiceMainEntity,einvInvoiceMainEntity);
        logger.info("Insert new invoice data");
        invoiceMainRepository.save(newInvoiceMainEntity);
    }
}
