package com.gateweb.einv.dao;

import dao.CashDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Eason on 3/8/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class CashDAOTest {
    @Autowired
    CashDAO cashDAO;

    @Test
    public void getYearMonthTest() throws Exception {
        List ym = cashDAO.getYM();
        for(Object ymStr: ym){
            System.out.println(ymStr);
        }
    }
}
