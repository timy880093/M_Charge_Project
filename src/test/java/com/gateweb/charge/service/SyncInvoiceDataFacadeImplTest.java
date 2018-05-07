package com.gateweb.charge.service;

import com.gate.utils.TimeUtils;
import com.gateweb.charge.model.CompanyEntity;
import com.gateweb.charge.repository.CompanyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Eason on 3/12/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class SyncInvoiceDataFacadeImplTest {

    @Autowired
    TimeUtils timeUtils;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    @Qualifier("syncInvoiceDataFacade")
    SyncInvoiceDataFacade syncInvoiceDataFacade;

    @Test
    @Transactional
    public void syncInvoiceMainDataTest() throws InvocationTargetException, IllegalAccessException {
        Calendar from = Calendar.getInstance();
        from.setTime(new Date());
        from.add(Calendar.DATE,-32);

        Calendar to = Calendar.getInstance();
        to.setTime(new Date());
        to.add(Calendar.DATE,-2);

        //資料太多，分公司查詢。
        List<CompanyEntity> companyEntityList = companyRepository.findAll();
        for(CompanyEntity companyEntity: companyEntityList){
            syncInvoiceDataFacade.syncInvoiceDataFromEinvDatabaseByCompany(
                    timeUtils.date2Timestamp(from.getTime())
                    , timeUtils.date2Timestamp(to.getTime())
                    , companyEntity.getBusinessNo()
            );
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void syncInvoiceMainReportData(){
        syncInvoiceDataFacade.syncInvoiceDataFromEinvDatabaseByDate(60);
    }
}
