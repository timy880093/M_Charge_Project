package com.gateweb.charge.repository;

import com.gateweb.charge.model.InvoiceAmountSummaryReportEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Eason on 5/7/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class InvoiceAmountSummaryReportRepositoryTest {

    @Autowired
    InvoiceAmountSummaryReportRepository chargeInvoiceAmountSummaryReportRepository;

    @Test
    @Transactional
    public void findTop1InvoiceAmountSummaryReportRecordOrderByModifyDate(){
        InvoiceAmountSummaryReportEntity invoiceAmountSummaryReportEntity
                = chargeInvoiceAmountSummaryReportRepository.findTop1ByOrderByModifyDateAsc();
        System.out.println("Last ModifyDate:"+ invoiceAmountSummaryReportEntity.getModifyDate());
        System.out.println("Last CreateDate:"+ invoiceAmountSummaryReportEntity.getCreateDate());
    }
}
