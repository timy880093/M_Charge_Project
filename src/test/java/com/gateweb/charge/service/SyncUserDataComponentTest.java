package com.gateweb.charge.service;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.charge.scheduleJob.component.SyncUserDataComponent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Eason on 3/9/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
@TestPropertySource("classpath:application-uat.properties")
public class SyncUserDataComponentTest {
    @Autowired
    SyncUserDataComponent syncUserDataComponent;

    @Test
    public void syncUserDataTest() {
        syncUserDataComponent.syncUserDataFromEinvDatabase();
    }

}
