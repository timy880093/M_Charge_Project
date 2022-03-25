package com.gateweb.charge.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.view.ChargePackageFetchView;
import com.gateweb.orm.charge.repository.ChargePackageFetchViewRepository;
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
public class ChargePackageFetchViewRepositoryTest {

    @Autowired
    ChargePackageFetchViewRepository chargePackageFetchViewRepository;

    Gson gson = new Gson();

    @Test
    public void findAllChargePackageView() {
        List<ChargePackageFetchView> chargePackageFetchViewList = chargePackageFetchViewRepository.findAll();
        chargePackageFetchViewList.stream().forEach(chargePackageFetchView -> {
            System.out.println(gson.toJson(chargePackageFetchView));
        });
    }

}
