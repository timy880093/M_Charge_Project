package com.gateweb.charge.repository;

import com.gateweb.charge.model.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Eason on 3/8/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class CompanyRepositoryTest {

    @Autowired
    CompanyRepository companyRepository;

    @Test
    public void getCompanyEntityById(){
        Company companyEntity = companyRepository.findByCompanyId(2);
        System.out.println(companyEntity.getName());
    }
}
