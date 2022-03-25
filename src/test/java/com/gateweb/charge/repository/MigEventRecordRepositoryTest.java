package com.gateweb.charge.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.MigEventRecordEntity;
import com.gateweb.orm.charge.repository.MigEventRecordRepository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * User: se01
 * Date: 7/4/2019 4:08 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class MigEventRecordRepositoryTest {

    Gson gson = new Gson();

    @Autowired
    MigEventRecordRepository migEventRecordRepository;

    @Test
    public void migEventRecordRepositoryQueryTest(){
        List<MigEventRecordEntity> migEventRecordEntityList = migEventRecordRepository.findAll();
        for(MigEventRecordEntity migEventRecordEntity: migEventRecordEntityList){
            System.out.println(gson.toJson(migEventRecordEntity));
        }
    }

}
