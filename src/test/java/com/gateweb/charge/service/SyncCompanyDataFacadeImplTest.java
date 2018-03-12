package com.gateweb.charge.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Eason on 3/9/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class SyncCompanyDataFacadeImplTest {

    @Autowired
    SyncCompanyDataFacade syncCompanyDataFacade;

    @Test
    public void syncCompanyDataTest() throws InvocationTargetException, IllegalAccessException {
        syncCompanyDataFacade.transactionSyncCompanyDataFromEinvDatabase();
    }

}
