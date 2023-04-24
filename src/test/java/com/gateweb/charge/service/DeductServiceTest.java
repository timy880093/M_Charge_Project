package com.gateweb.charge.service;


import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.view.DeductHistoryFetchView;
import com.gateweb.orm.charge.repository.*;
import com.gateweb.orm.charge.entity.Deduct;
import com.gateweb.charge.infrastructure.propertyProvider.ContextProvider;
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
public class DeductServiceTest {
    @Autowired
    ContextProvider contextProvider;
    @Autowired
    SimpleUserViewRepository simpleUserViewRepository;
    @Autowired
    ChargeUserRepository userRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    ChargePackageRepository chargePackageRepository;
    @Autowired
    PackageRefRepository modeReferenceRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    NewGradeRepository newGradeRepository;
    @Autowired
    DeductHistoryFetchViewRepository deductHistoryFetchViewRepository;
    @Autowired
    DeductFetchViewRepository deductFetchViewRepository;
    @Autowired
    DeductRepository deductRepository;
    @Autowired
    DeductService deductService;

    Gson gson = new Gson();

    @Test
    public void searchDeductHistoryFetchViewTest() {
        List<DeductHistoryFetchView> deductHistoryFetchViewList = deductHistoryFetchViewRepository.findAll();
        deductHistoryFetchViewList.stream().forEach(deductHistoryFetchView -> {
            System.out.println(gson.toJson(deductHistoryFetchView));
        });
    }

    @Test
    public void searchDeductHistoryFetchViewByDeductIdTest() {
        List<Deduct> deductList = deductRepository.findAll();
        deductList.stream().forEach(deduct -> {
            List<DeductHistoryFetchView> deductHistoryFetchViewList = deductService.findDeductHistoryFetchViewByDeductId(deduct.getDeductId());
            deductHistoryFetchViewList.stream().forEach(deductHistoryFetchView -> {
                System.out.println(gson.toJson(deductHistoryFetchView));
            });
        });
    }

}
