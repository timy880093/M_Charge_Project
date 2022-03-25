package com.gateweb.einv.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.einv.entity.Company;
import com.gateweb.orm.einv.repository.EinvCompanyRepository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by Eason on 3/9/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class EinvCompanyRepositoryTest {
    Gson gson = new Gson();

    @Autowired
    EinvCompanyRepository einvCompanyRepository;

    @Test
    public void repositoryTest(){
        List<Company> companyList = einvCompanyRepository.findAll();
        for(Company company : companyList){
            System.out.println(gson.toJson(company));
        }
    }
}
