package com.gateweb.utils;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.google.gson.Gson;
import org.apache.logging.log4j.core.util.CronExpression;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.ParseException;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class CronTest {

    Gson gson = new Gson();

    @Test
    public void cronTestYear() throws ParseException {
        String pattern = "0 0 0 L 04 ? %/1";
        pattern = pattern.replace("%", "2020");
        CronExpression cronExpression = new CronExpression(pattern);
        Date nextDate = new Date();
        for (int i = 0; i < 10; i++) {
            nextDate = cronExpression.getNextValidTimeAfter(nextDate);
            System.out.println(gson.toJson(nextDate));
        }
    }

    @Test
    public void cronTestSeason() throws ParseException {
        String pattern = "0 0 0 16 JAN,JUN,SEP,DEC ?";
        CronExpression cronExpression = new CronExpression(pattern);
        Date nextDate = new Date();
        for (int i = 0; i < 10; i++) {
            nextDate = cronExpression.getNextValidTimeAfter(nextDate);
            System.out.println(gson.toJson(nextDate));
        }
    }

    @Test
    public void cronTestEveryWeekAtSunday() throws ParseException {
        String pattern = "0 0 0 ? * SUN";
        CronExpression cronExpression = new CronExpression(pattern);
        Date nextDate = new Date();
        for (int i = 0; i < 10; i++) {
            nextDate = cronExpression.getNextValidTimeAfter(nextDate);
            System.out.println(gson.toJson(nextDate));
        }
    }

    @Test
    public void cronEvery5Minutes() throws ParseException {
        String pattern = "0 0/5 * * * ?";
        CronExpression cronExpression = new CronExpression(pattern);
        Date nextDate = new Date();
        for (int i = 0; i < 10; i++) {
            nextDate = cronExpression.getNextValidTimeAfter(nextDate);
            System.out.println(gson.toJson(nextDate));
        }
    }

    @Test
    public void cron151617181920() throws ParseException {
        String pattern = "0 0 0 15,16,17,18,19,20 * ? ";
        CronExpression cronExpression = new CronExpression(pattern);
        Date nextDate = new Date();
        for (int i = 0; i < 10; i++) {
            nextDate = cronExpression.getNextValidTimeAfter(nextDate);
            System.out.println(gson.toJson(nextDate));
        }
    }
}
