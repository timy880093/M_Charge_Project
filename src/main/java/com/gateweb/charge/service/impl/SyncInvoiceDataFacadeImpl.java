package com.gateweb.charge.service.impl;

import com.gate.utils.TimeUtils;
import com.gateweb.charge.model.InvoiceAmountSummaryReportEntity;
import com.gateweb.charge.model.InvoiceMainEntity;
import com.gateweb.charge.repository.InvoiceAmountSummaryReportRepository;
import com.gateweb.charge.repository.InvoiceMainRepository;

import com.gateweb.charge.service.InvoiceAmountSummaryReportFacade;
import com.gateweb.charge.service.SyncInvoiceDataFacade;
import com.gateweb.einv.model.InvoiceMain;
import com.gateweb.einv.repository.EinvInvoiceMainRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Eason on 3/12/2018.
 */
@Service("syncInvoiceDataFacade")
public class SyncInvoiceDataFacadeImpl implements SyncInvoiceDataFacade {
    protected static final Logger logger = LogManager.getLogger(SyncInvoiceDataFacadeImpl.class);

    @Autowired
    TimeUtils timeUtils;

    @Autowired
    InvoiceMainRepository invoiceMainRepository;

    @Autowired
    EinvInvoiceMainRepository einvInvoiceMainRepository;

    @Autowired
    InvoiceAmountSummaryReportFacade invoiceAmountSummaryReportFacade;

    @Autowired
    InvoiceAmountSummaryReportRepository chargeInvoiceAmountSummaryReportRepository;

    @Override
    //應該根據修改日期才可以得到完全正確的資料，因為發票資料包括折讓及註銷的部份。
    public void syncInvoiceDataFromEinvDatabase(Timestamp previousTime) throws InvocationTargetException, IllegalAccessException {
        List<InvoiceMain> einvInvoiceMainEntityList = einvInvoiceMainRepository.findByModifyDateIsGreaterThan(previousTime);
        syncInvoiceDataByInvoiceMainEntityList(einvInvoiceMainEntityList);
    }

    @Override
    public void syncInvoiceDataFromEinvDatabase(Timestamp from, Timestamp to) throws InvocationTargetException, IllegalAccessException {
        List<InvoiceMain> einvInvoiceMainEntityList = einvInvoiceMainRepository.findByCreateDateIsGreaterThanAndModifyDateIsLessThan(from,to);
        syncInvoiceDataByInvoiceMainEntityList(einvInvoiceMainEntityList);
    }

    @Override
    public void syncInvoiceDataFromEinvDatabaseByCompany(Timestamp from, Timestamp to,String sellerIdentifier) throws InvocationTargetException, IllegalAccessException {
        List<InvoiceMain> einvInvoiceMainEntityList = einvInvoiceMainRepository.findByCreateDateIsGreaterThanAndModifyDateIsLessThanAndSellerIs(from,to,sellerIdentifier);
        syncInvoiceDataByInvoiceMainEntityList(einvInvoiceMainEntityList);
    }

    public void syncInvoiceDataByInvoiceMainEntityList(List<InvoiceMain> einvInvoiceMainEntityList) throws InvocationTargetException, IllegalAccessException {
        for(InvoiceMain einvInvoiceMain: einvInvoiceMainEntityList){
            InvoiceMainEntity existsInvoiceMainEntity
                    = invoiceMainRepository.findByCYearMonthAndInvoiceNumber(
                    einvInvoiceMain.getcYearMonth()
                    ,einvInvoiceMain.getInvoiceNumber()
            );
            try{
                if(existsInvoiceMainEntity!=null){
                    transactionUpdateInvoiceMainDataFromEinvDatabase(existsInvoiceMainEntity, einvInvoiceMain);
                }else{
                    transactionInsertInvoiceMainDataFromEinvDatabase(einvInvoiceMain);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void transactionUpdateInvoiceMainDataFromEinvDatabase(
            InvoiceMainEntity existsInvoiceMainEntity
            , InvoiceMain einvInvoiceMainEntity) throws InvocationTargetException, IllegalAccessException {
        if(existsInvoiceMainEntity.getInvoiceId().equals(einvInvoiceMainEntity.getInvoiceId())){
            BeanUtils.copyProperties(existsInvoiceMainEntity,einvInvoiceMainEntity);
            logger.info("Update exists invoice data");
        }else{
            invoiceMainRepository.delete(existsInvoiceMainEntity);
            logger.info("Remove exists invoice data");
        }
        invoiceMainRepository.save(existsInvoiceMainEntity);
    }

    public void transactionInsertInvoiceMainDataFromEinvDatabase(InvoiceMain einvInvoiceMainEntity) throws InvocationTargetException, IllegalAccessException {
        InvoiceMainEntity newInvoiceMainEntity = new InvoiceMainEntity();
        BeanUtils.copyProperties(newInvoiceMainEntity,einvInvoiceMainEntity);
        logger.info("Insert new invoice data");
        invoiceMainRepository.save(newInvoiceMainEntity);
    }

    @Override
    public void syncYesterdaysInvoiceDataFromEinvDatabase() throws InvocationTargetException, IllegalAccessException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,-1);
        syncInvoiceDataFromEinvDatabase(timeUtils.date2Timestamp(calendar.getTime()));
    }

    @Override
    public void syncInvoiceDataFromEinvDatabaseByDate(int date) {
        for(int i =1;i<=date;i++){
            System.out.println("from:"+(i-(date+1))+" to "+ (i-date) );
            invoiceAmountSummaryReportFacade.transactionGenerateAndInsertSummaryReport(i-(date+1),i-date);
        }
    }

    @Override
    public void syncYesterdaysInvoiceDataFromEinvDatabaseDirectly(){
        invoiceAmountSummaryReportFacade.transactionGenerateAndInsertSummaryReport(-2,-1);
    }

    @Override
    public void syncByCurrentLastModifyDate(){
        //取得當前最後一個modifyDate
        InvoiceAmountSummaryReportEntity invoiceAmountSummaryReportEntity
                = chargeInvoiceAmountSummaryReportRepository.findTop1ByOrderByModifyDateDesc();
        if(invoiceAmountSummaryReportEntity!=null){
            Date lastModifyDate = new Date(invoiceAmountSummaryReportEntity.getModifyDate().getTime());
            Date currentDate = new Date();
            DateTime lastModifyDateTime = new DateTime(lastModifyDate);
            DateTime currentDateTime = new DateTime(currentDate);
            int lastModifyDaysBetween = Days.daysBetween(lastModifyDateTime,currentDateTime).getDays();
            syncInvoiceDataFromEinvDatabaseByDate(lastModifyDaysBetween);
        }else{
            //沒資料就抓一年前
            syncInvoiceDataFromEinvDatabaseByDate(365);
        }
    }

}
