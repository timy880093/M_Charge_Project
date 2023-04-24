package com.gateweb.charge.service;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.charge.scheduleJob.component.FindFirstInvoiceDateComponent;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class FindFirstInvoiceDateComponentTest {

    @Autowired
    FindFirstInvoiceDateComponent findFirstInvoiceDateComponent;
    @Autowired
    CompanyRepository companyRepository;

    @Test
    public void test() {
        Optional<Company> companyOptional = companyRepository.findById(7809);
        if (companyOptional.isPresent()) {
            CustomInterval customInterval = findFirstInvoiceDateComponent.getAcceptableInvoiceDateRangeByLocalDateTime(
                    companyOptional.get().getCreateDate().toLocalDateTime()
            );
            Optional<String> stringOptional = findFirstInvoiceDateComponent.findFirstInvoiceDateByBusinessNoWithRetry(
                    companyOptional.get().getBusinessNo(),
                    customInterval.getStartLocalDateTime(),
                    customInterval.getEndLocalDateTime()
            );
            Set<String> ymStrSet = LocalDateTimeUtils.localDateTimeRangeToTwYmSet(
                    customInterval.getStartLocalDateTime(), LocalDateTime.now()
            );
            ymStrSet.stream().forEach(str->{
                System.out.println(str);
            });
        }
    }
}
