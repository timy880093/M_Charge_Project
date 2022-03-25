package com.gateweb.charge.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.ChargeIasrCountReport;
import com.gateweb.orm.charge.repository.ChargeIasrCountReportRepository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class ChargeIasrCountReportRepositoryTest {
    @Autowired
    ChargeIasrCountReportRepository chargeIasrCountReportRepository;

    @Test
    public void findChargeIasrReportTest() {
        Gson gson = new Gson();
        Set<ChargeIasrCountReport> chargeIasrCountReportSet = new HashSet<>();
        chargeIasrCountReportRepository.findChargeIasrCountReport("202011%");
        chargeIasrCountReportSet.stream().forEach(invoiceCountReport -> {
            System.out.println(gson.toJson(invoiceCountReport));
        });
    }
}

