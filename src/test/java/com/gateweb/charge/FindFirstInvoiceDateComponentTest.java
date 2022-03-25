package com.gateweb.charge;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.charge.scheduleJob.component.FindFirstInvoiceDateComponent;
import com.gateweb.utils.LocalDateTimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class FindFirstInvoiceDateComponentTest {
    @Autowired
    FindFirstInvoiceDateComponent findFirstInvoiceDateComponent;

    @Test
    public void test() {
        Optional<LocalDateTime> companyCreateDate = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-05-28T17:11:53.789"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );

//        CustomInterval result = findFirstInvoiceDateComponent.getAcceptableInvoiceDateRangeByLocalDateTime(companyCreateDate.get());
//        Optional<String> result2 = findFirstInvoiceDateComponent.findFirstInvoiceDateFromCompanyCreateDate("83176320", companyCreateDate.get());
//        System.out.println(result.toString());
//        System.out.println(result2.get());
    }
}
