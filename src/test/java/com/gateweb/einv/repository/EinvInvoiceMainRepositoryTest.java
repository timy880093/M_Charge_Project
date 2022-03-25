package com.gateweb.einv.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.einv.entity.InvoiceMain;
import com.gateweb.orm.einv.repository.EinvInvoiceMainRepository;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Eason on 3/9/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class EinvInvoiceMainRepositoryTest {

    Gson gson = new Gson();

    @Autowired
    EinvInvoiceMainRepository einvInvoiceMainRepository;

    @Test
    public void repositoryTest() {
        List<InvoiceMain> einvInvoiceList = einvInvoiceMainRepository.findTop100ByCYearMonth("10702");
        for (InvoiceMain invoiceMain : einvInvoiceList) {
            System.out.println(gson.toJson(invoiceMain));
        }
    }

    @Test
    public void topLocalDateTimeRepositoryTest() {
        Optional<Timestamp> localDateTimeOptional = einvInvoiceMainRepository.findTopLocalDateTimeBySellerAndYearMonth(
                "24549210"
                , "10812"
                , 200
        );
        Assert.assertTrue(localDateTimeOptional.isPresent());
    }
}
