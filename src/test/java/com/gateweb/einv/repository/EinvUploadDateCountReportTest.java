package com.gateweb.einv.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.einv.repository.EinvInvoiceMainRepository;
import com.google.gson.Gson;
import org.eclipse.core.runtime.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class EinvUploadDateCountReportTest {
    @Autowired
    EinvInvoiceMainRepository einvInvoiceMainRepository;

    @Test
    public void einvInvoiceDateCountReportRepositoryTest() {
        Gson gson = new Gson();
        LocalDateTime from = LocalDateTime.now().minusMonths(2);
        LocalDateTime to = LocalDateTime.now().minusMonths(1);
        String cYearMonth = "11004";
        String seller = "24549210";
        Optional<BigInteger> usageCountOpt = einvInvoiceMainRepository.findUsageCountByUploadDate(
                from, to, cYearMonth, seller
        );
        Assert.isTrue(usageCountOpt.isPresent());
        System.out.println(usageCountOpt.get());
    }
}
