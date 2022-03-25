package com.gateweb.charge.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.PrepayDeductMasterEntity;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.PrepayDeductMasterRepository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eason on 3/29/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class PrepayDeductMasterRepositoryTest {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    PrepayDeductMasterRepository prepayDeductMasterRepository;

    Gson gson = new Gson();

    @Test
    public void findByCompanyIdTest(){
        List<Company> companyEntityList = companyRepository.findAll();

        List<String> messageList = new ArrayList<>();
        for(Company companyEntity : companyEntityList){
            List<PrepayDeductMasterEntity> prepayDeductMasterEntityList
                    = prepayDeductMasterRepository.findByCompanyId(companyEntity.getCompanyId());
            for(PrepayDeductMasterEntity prepayDeductMasterEntity: prepayDeductMasterEntityList){
                messageList.add(gson.toJson(prepayDeductMasterEntityList));
            }
        }
        for(String message: messageList){
            System.out.println(message);
        }
    }

}
