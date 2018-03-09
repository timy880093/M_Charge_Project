package com.gateweb.einv.repository;

import com.gateweb.einv.model.InvoiceDetails;
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
public class EinvInvoiceDetailsRepositoryTest {

    Gson gson = new Gson();

    @Autowired
    EinvInvoiceMainRepository einvInvoiceMainRepository;

    @Autowired
    EinvInvoiceDetailsRepository einvInvoiceDetailsRepository;

    @Test
    public void repositoryTest(){
        List<InvoiceMain> einvInvoiceList = einvInvoiceMainRepository.findTop100ByCYearMonth("10702");
        for(InvoiceMain invoiceMain: einvInvoiceList){
            InvoiceDetails conditionEntity = new InvoiceDetails();
            conditionEntity.setInvoiceNumber(invoiceMain.getInvoiceNumber());
            conditionEntity.setCYearMonth(invoiceMain.getCYearMonth());
            List<InvoiceDetails> einvInvoiceDetailsList = einvInvoiceDetailsRepository.searchLikeVo(conditionEntity);
            for(InvoiceDetails invoiceDetails : einvInvoiceDetailsList){
                System.out.println(gson.toJson(invoiceDetails));
            }
        }
    }
}
