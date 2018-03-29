package com.gateweb.charge.repository;

import com.gateweb.charge.model.CashDetailEntity;
import com.gateweb.charge.model.CompanyEntity;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eason on 3/29/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
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
        List<CompanyEntity> companyEntityList = companyRepository.findAll();
        for(CompanyEntity companyEntity: companyEntityList){
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
