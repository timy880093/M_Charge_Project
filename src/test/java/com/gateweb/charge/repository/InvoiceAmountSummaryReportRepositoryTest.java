package com.gateweb.charge.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.ChargeIasrEntity;
import com.gateweb.orm.charge.repository.ChargeIasrRepository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import unitTest.ContractPeriodicFeeCalculatorTest;

/**
 * Created by Eason on 5/7/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
@Category(ContractPeriodicFeeCalculatorTest.class)
@TestPropertySource(properties = {"spring.profiles.active=uat"})
public class InvoiceAmountSummaryReportRepositoryTest {
    Gson gson = new Gson();

    @Autowired
    ChargeIasrRepository chargeInvoiceAmountSummaryReportRepository;

    @Test
    @Transactional
    public void findTop1InvoiceAmountSummaryReportRecordOrderByModifyDate() {
        ChargeIasrEntity invoiceAmountSummaryReportEntity
                = chargeInvoiceAmountSummaryReportRepository.findTop1ByCreateDateIsNotNullAndModifyDateIsNotNullOrderByModifyDateDesc();
        System.out.println("Last ModifyDate:" + invoiceAmountSummaryReportEntity.getModifyDate());
        System.out.println("Last CreateDate:" + invoiceAmountSummaryReportEntity.getCreateDate());
        System.out.println(invoiceAmountSummaryReportEntity.getModifyDate());
    }

    @Test
    public void findMinAndMaxTest() {
        String dateString = chargeInvoiceAmountSummaryReportRepository.findMinAndMaxInvoiceDate("42636341");
        System.out.println(dateString);
    }

}
