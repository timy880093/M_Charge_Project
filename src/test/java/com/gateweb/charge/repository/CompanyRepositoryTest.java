package com.gateweb.charge.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.CompanyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

/**
 * Created by Eason on 3/8/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class CompanyRepositoryTest {

    @Autowired
    CompanyRepository companyRepository;

    @Test
    public void getCompanyEntityById() {
        Optional<Company> companyEntity = companyRepository.findByCompanyId(2);
        if (companyEntity.isPresent()) {
            System.out.println(companyEntity.get().getName());
        }
    }
}
