package com.gateweb.einv.dao;

import com.gateweb.einv.model.InvoiceAmountSummaryReportEntity;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Eason on 1/30/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class InvoiceAmountSummaryReportDaoTest {

    @Autowired
    @Qualifier("einvInvoiceAmountSummaryReportDao")
    InvoiceAmountSummaryReportDao einvInvoiceAmountSummaryReportDao;

    Gson gson = new Gson();

    @Test
    @Transactional
    public void invoiceAmountSummaryReportQueryTest() {
        List<InvoiceAmountSummaryReportEntity> invoiceAmountSummaryReportEntityList =
                einvInvoiceAmountSummaryReportDao.getAllDistinct();
        for (InvoiceAmountSummaryReportEntity invoiceAmountSummaryReportEntity : invoiceAmountSummaryReportEntityList) {
            System.out.println(gson.toJson(invoiceAmountSummaryReportEntity));
        }
    }



}
