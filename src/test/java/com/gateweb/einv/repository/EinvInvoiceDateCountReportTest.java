package com.gateweb.einv.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.einv.entity.report.EinvInvoiceDateCountReport;
import com.gateweb.orm.einv.repository.EinvInvoiceDateCountReportRepository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class EinvInvoiceDateCountReportTest {

    @Autowired
    EinvInvoiceDateCountReportRepository einvInvoiceDateCountReportRepository;

    @Test
    public void einvInvoiceDateCountReportRepositoryTest() {
        Gson gson = new Gson();
        Set<EinvInvoiceDateCountReport> invoiceCountReportSet
                = einvInvoiceDateCountReportRepository.findEinvInvoiceDateCountReport("10912", "202011%");
        invoiceCountReportSet.stream().forEach(invoiceCountReport -> {
            System.out.println(gson.toJson(invoiceCountReport));
        });
    }
}
