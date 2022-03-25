package com.gateweb.charge.service;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.charge.enumeration.BankCode;
import com.gateweb.charge.exception.IllegalStateException;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
@TestPropertySource("classpath:application.properties")
/**
 * 僅以此註解紀念這位大大的貢獻：https://stackoverflow.com/questions/58505726/unit-testing-failed-to-load-applicationcontext
 * 當你import與webMvcConfig相關的@Configuration, class的時候應加放@WebAppConfiguration在UnitTest的類別中
 */
public class MigrationTest {

    @Autowired
    MigrationService migrationService;

    @Test
    public void singleTest() throws IllegalStateException, ParseException {
        migrationService.migrationProcessTest();
    }

    @Test
    public void test2(){
        migrationService.backup11();
    }

    @Test
    public void test1() {
        BigDecimal testD = new BigDecimal(-8.5).setScale(0, BigDecimal.ROUND_HALF_UP);
        System.out.println(testD.toString());
    }
}
