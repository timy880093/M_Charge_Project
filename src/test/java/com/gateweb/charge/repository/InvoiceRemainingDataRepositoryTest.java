package com.gateweb.charge.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.InvoiceRemainingData;
import com.gateweb.orm.charge.repository.InvoiceRemainingDataRepository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class InvoiceRemainingDataRepositoryTest {
    @Autowired
    InvoiceRemainingDataRepository invoiceRemainingDataRepository;

    @Test
    public void searchTest() {
        List<InvoiceRemainingData> invoiceRemainingDataSet = invoiceRemainingDataRepository.findInvoiceRemainingData(new Long(7894));
        Gson gson = new Gson();
        invoiceRemainingDataSet.stream().forEach(invoiceRemainingData -> {
            System.out.println(gson.toJson(invoiceRemainingData));
        });
    }
}
