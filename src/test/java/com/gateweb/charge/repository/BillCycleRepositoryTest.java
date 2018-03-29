package com.gateweb.charge.repository;

import com.gateweb.charge.model.BillCycleEntity;
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
 * Created by Eason on 3/28/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class BillCycleRepositoryTest {

    @Autowired
    BillCycleRepository billCycleRepository;

    @Autowired
    CashDetailRepository cashDetailRepository;

    @Autowired
    CompanyRepository companyRepository;

    Gson gson = new Gson();

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

    @Test
    public void findByCashOutOverIdTest(){
        List<String> messageList = new ArrayList<>();
        List<CashDetailEntity> cashDetailEntityList = cashDetailRepository.findAll();
        for(CashDetailEntity cashDetailEntity: cashDetailEntityList){
            List<BillCycleEntity> billCycleEntityList = billCycleRepository.findByCashOutOverId(cashDetailEntity.getCashDetailId());
            if(billCycleEntityList.size()==1){
                messageList.add(gson.toJson(billCycleEntityList.get(0)));
            }else{
                messageList.add("strange data :" + cashDetailEntity.getCashDetailId());
            }
        }
        for(String message: messageList){
            System.out.println(message);
        }
    }
}