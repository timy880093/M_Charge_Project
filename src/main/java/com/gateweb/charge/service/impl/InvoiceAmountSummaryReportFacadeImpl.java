package com.gateweb.charge.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.gateweb.charge.dao.InvoiceAmountSummaryReportDao;
import com.gateweb.charge.service.InvoiceAmountSummaryReportFacade;
import com.gateweb.einv.model.InvoiceAmountSummaryReportEntity;

/**
 * Created by Eason on 2/13/2018.
 */
@Service("invoiceAmountSummaryReportFacade")
public class InvoiceAmountSummaryReportFacadeImpl implements InvoiceAmountSummaryReportFacade{

    protected static final Logger logger = LogManager.getLogger(InvoiceAmountSummaryReportFacadeImpl.class);

    @Autowired
    @Qualifier("einvInvoiceAmountSummaryReportDao")
    com.gateweb.einv.dao.InvoiceAmountSummaryReportDao einvInvoiceAmountSummaryReportDao;

    @Autowired
    @Qualifier("chargeInvoiceAmountSummaryReportDao")
    InvoiceAmountSummaryReportDao chargeInvoiceAmountSummaryReportDao;

    @Override
    public void transactionInsertDataFromEinvDatabase(){
        List<InvoiceAmountSummaryReportEntity> invoiceAmountSummaryReportEntityList
                = einvInvoiceAmountSummaryReportDao.getAllDistinct();
        List<com.gateweb.charge.model.InvoiceAmountSummaryReportEntity> resultList = new ArrayList<>();
        //取得資料集
        for(com.gateweb.einv.model.InvoiceAmountSummaryReportEntity targetInvoiceAmountSummaryReportEntity : invoiceAmountSummaryReportEntityList){
            com.gateweb.charge.model.InvoiceAmountSummaryReportEntity result = new com.gateweb.charge.model.InvoiceAmountSummaryReportEntity();
            try{
                BeanUtils.copyProperties(result,targetInvoiceAmountSummaryReportEntity);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                resultList.add(result);
            }
        }
        //寫入資料
        for(com.gateweb.charge.model.InvoiceAmountSummaryReportEntity result : resultList){
            com.gateweb.charge.model.InvoiceAmountSummaryReportEntity existsInvoiceAmountSummaryReportEntity
                    =  chargeInvoiceAmountSummaryReportDao.get(result.getId());
            if(existsInvoiceAmountSummaryReportEntity!=null){
                logger.info(existsInvoiceAmountSummaryReportEntity.getId()+", 此筆記錄已存在。");
            }else{
                chargeInvoiceAmountSummaryReportDao.save(result);
            }
        }
    }
}
