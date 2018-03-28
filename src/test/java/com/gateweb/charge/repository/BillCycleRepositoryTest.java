package com.gateweb.charge.repository;

import com.gateweb.charge.model.BillCycleEntity;
import com.gateweb.charge.model.CompanyEntity;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Eason on 3/28/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class BillCycleRepositoryTest {

    @Autowired
    BillCycleRepository billCycleRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Test
    public void findByYearMonthAndCompanyIdTest(){
        List<CompanyEntity> companyEntityList = companyRepository.findAll();
        for(CompanyEntity companyEntity:companyEntityList){
            List<BillCycleEntity> billCycleEntityList = billCycleRepository.findByYearMonthIsAndCompanyIdIs("201803",companyEntity.getCompanyId());
            for(BillCycleEntity billCycleEntity : billCycleEntityList){
                System.out.println(billCycleEntity.toString());
            }
        }
    }
}
