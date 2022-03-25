package com.gateweb.einv.bridge;

import com.gateweb.bridge.service.SyncIasrDataService;
import com.gateweb.charge.config.SpringWebMvcConfig;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
@TestPropertySource("classpath:application.properties")
@Category(SyncIasrDataServiceTest.class)
public class SyncIasrDataServiceTest {
    @Autowired
    SyncIasrDataService syncIasrDataServiceImpl;

    @Test
    public void syncIasrTest1() throws InterruptedException {
        syncIasrDataServiceImpl.regenIasrCount("10910", "09");
    }
}
