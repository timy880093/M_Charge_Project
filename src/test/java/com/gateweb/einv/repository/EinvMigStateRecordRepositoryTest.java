package com.gateweb.einv.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.einv.entity.MigStateRecordEntity;
import com.gateweb.orm.einv.repository.EinvMigStateRecordRepository;
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
 * Date: 7/3/2019 4:36 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class EinvMigStateRecordRepositoryTest {
    Gson gson = new Gson();

    @Autowired
    EinvMigStateRecordRepository einvMigStateRecordRepository;

    @Test
    public void repositoryTest() {
        List<MigStateRecordEntity> migStateRecordEntityList = einvMigStateRecordRepository.findTop100By();
        for (MigStateRecordEntity migStateRecordEntity : migStateRecordEntityList) {
            System.out.println(gson.toJson(migStateRecordEntity));
        }
    }


}
