package com.gateweb.charge.service;

import com.gate.web.facades.UserService;
import com.gateweb.utils.LocalDateTimeUtils;
import com.gateweb.charge.feeCalculation.dataGateway.ContractOverageFeeBillingDataCollector;
import com.gateweb.charge.scheduleJob.quartzJobs.ContractAutomationJob;
import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.view.ChargePackageFetchView;
import com.gateweb.orm.charge.repository.ChargePackageFetchViewRepository;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
@Component
public class ContractServiceTest {
    @Autowired
    ContractService contractService;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    UserService userService;
    @Autowired
    ChargePackageFetchViewRepository chargePackageFetchViewRepository;
    @Autowired
    ContractAutomationJob contractAutomationJob;
    @Autowired
    ContractOverageFeeBillingDataCollector contractOverageFeeBillingDataCollector;

    Gson gson = new Gson();

    @Test
    public void chargePackageFetchViewSearchTest() {
        Optional<ChargePackageFetchView> chargePackageFetchView = chargePackageFetchViewRepository.findById(new Long(8));
        if (chargePackageFetchView.isPresent()) {
            System.out.println(gson.toJson(chargePackageFetchView.get()));
        }
    }

    @Test
    public void contractAutomationTest() {
        contractAutomationJob.contractAutoFulfillmentByInvoiceDate(new Long(348));
        contractAutomationJob.contractAutoFulfillmentByInstallationDate(new Long(348));
    }

    @Test
    public void contractInitialTest(){
        contractService.initialContract(new Long(10265),new Long(348));
    }

    @Test
    public void setContractOverageFeeBillingDataCollectorTest() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2019-01-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2019-01-31T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval customInterval = new CustomInterval(
                fromLocalDateTime.get()
                , toLocalDateTime.get()
        );
        contractOverageFeeBillingDataCollector.collect(customInterval);
    }

}
