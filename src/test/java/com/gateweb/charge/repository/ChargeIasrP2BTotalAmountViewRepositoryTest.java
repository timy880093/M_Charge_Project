package com.gateweb.charge.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.view.ChargeIasrP2bTotalAmountView;
import com.gateweb.orm.charge.repository.ChargeIasrP2bTotalAmountViewRepository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import unitTest.ContractPeriodicFeeCalculatorTest;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
@Category(ContractPeriodicFeeCalculatorTest.class)
@TestPropertySource(properties = {"spring.profiles.active=uat"})
public class ChargeIasrP2BTotalAmountViewRepositoryTest {
    @Autowired
    ChargeIasrP2bTotalAmountViewRepository chargeIasrP2bTotalAmountViewRepository;

    @Test
    public void findChargeIasrAmountSummaryRepositoryTest() {
        Gson gson = new Gson();
        List<ChargeIasrP2bTotalAmountView> chargeIasrP2BTotalAmountViewSet
                = chargeIasrP2bTotalAmountViewRepository.findInvoiceAmountForP2b("24549210", "20080101", "20220921");
        chargeIasrP2BTotalAmountViewSet.stream().forEach(invoiceCountReport -> {
            System.out.println(gson.toJson(invoiceCountReport));
        });
    }
}
