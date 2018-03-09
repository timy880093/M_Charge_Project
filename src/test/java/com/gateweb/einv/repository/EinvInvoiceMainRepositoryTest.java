package com.gateweb.einv.repository;

import com.gateweb.einv.model.InvoiceMain;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Eason on 3/9/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class EinvInvoiceMainRepositoryTest {

    Gson gson = new Gson();

    @Autowired
    EinvInvoiceMainRepository einvInvoiceMainRepository;

    @Test
    public void repositoryTest(){
        List<InvoiceMain> einvInvoiceList = einvInvoiceMainRepository.findTop100ByCYearMonth("10702");
        for(InvoiceMain invoiceMain: einvInvoiceList){
            System.out.println(gson.toJson(invoiceMain));
        }
    }
}
