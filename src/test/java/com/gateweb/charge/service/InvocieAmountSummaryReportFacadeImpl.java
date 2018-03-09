package com.gateweb.charge.service;

import com.gateweb.charge.service.InvoiceAmountSummaryReportFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by Eason on 2/13/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class InvocieAmountSummaryReportFacadeImpl {

    @Autowired
    InvoiceAmountSummaryReportFacade invoiceAmountSummaryReportFacade;

    @Test
    @Transactional
    @Rollback(false)
    public void copyDataFromEinvDatabase(){
        invoiceAmountSummaryReportFacade.transactionInsertDataFromEinvDatabase();
    }
}
