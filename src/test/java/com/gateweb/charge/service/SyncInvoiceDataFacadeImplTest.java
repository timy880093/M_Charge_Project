package com.gateweb.charge.service;

import com.gate.utils.TimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Eason on 3/12/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class SyncInvoiceDataFacadeImplTest {

    @Autowired
    SyncInvoiceDataFacade syncInvoiceDataFacade;

    @Test
    @Transactional
    public void syncInvoiceMainDataTest() throws InvocationTargetException, IllegalAccessException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,-1);

        syncInvoiceDataFacade.syncInvoiceMainDataFromEinvDatabase(TimeUtils.date2Timestamp(calendar.getTime()));
    }

}
