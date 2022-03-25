package com.gateweb.utils;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.google.gson.Gson;
import org.apache.commons.beanutils.BeanMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class BeanMapUnitTest {
    @Autowired
    ContractRepository contractRepository;

    @Test
    public void beanMapTest() {
        Gson gson = new Gson();
        contractRepository.findAll().stream().forEach(contract -> {
            BeanMap beanMap = new BeanMap();
            beanMap.setBean(contract);
            HashMap map = new HashMap();
            map.putAll(beanMap);
            map.remove("class");
            gson.toJson(map);
        });
    }

}
