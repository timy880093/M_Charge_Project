package com.gateweb.charge.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.CashDetailEntity;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.CashDetailRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
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
public class CashDetailRepositoryTest {
    @Autowired
    CashDetailRepository cashDetailRepository;

    @Autowired
    CompanyRepository companyRepository;

    Gson gson = new Gson();

    @Test
    public void findByCashDetailIdTest() throws Exception {
        List<CashDetailEntity> cashDetailEntityList = cashDetailRepository.findAll();
        for(CashDetailEntity cashDetailEntity: cashDetailEntityList){
            CashDetailEntity result = cashDetailRepository.findByCashDetailId(cashDetailEntity.getCashDetailId());
            if(result !=null){
                System.out.println(gson.toJson(result));
            }else{
                throw new  Exception("you shall not pass");
            }
        }
    }

    @Test
    public void findByCompanyIdIsAndCalYmIsAndCashTypeIs() throws Exception{
        List<String> messageList = new ArrayList<>();
        List<Company> companyEntityList = companyRepository.findAll();
        for(Company companyEntity: companyEntityList){
            List<CashDetailEntity> cashDetailEntityList
                    = cashDetailRepository.findByCompanyIdIsAndCalYmIsAndCashTypeIs(companyEntity.getCompanyId(),"201802",7);
            for(CashDetailEntity cashDetailEntity : cashDetailEntityList){
                messageList.add(gson.toJson(cashDetailEntity));
            }
        }
        for(String message: messageList){
            System.out.println(message);
        }
    }
}
