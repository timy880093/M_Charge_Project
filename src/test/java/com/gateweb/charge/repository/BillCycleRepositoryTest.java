package com.gateweb.charge.repository;

import com.gateweb.charge.model.BillCycleEntity;
import com.gateweb.charge.model.CashDetailEntity;
import com.gateweb.charge.model.CompanyEntity;
import com.google.gson.Gson;
import dao.CalCycleDAO;
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

    @Autowired
    CalCycleDAO calCycleDAO;

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

    @Test
    public void compareBillCycleAndCntCount(){
        List<BillCycleEntity> billCycleEntityList = billCycleRepository.findAll();
        for(BillCycleEntity billCycleEntity: billCycleEntityList){
            if(billCycleEntity.getStatus().equals("1")){
                if(billCycleEntity.getYearMonth().contains("201803")||billCycleEntity.getYearMonth().contains("201804")){
                    CompanyEntity companyEntity = companyRepository.findByCompanyId(billCycleEntity.getCompanyId());
                    Integer usedCount = calCycleDAO.calOverByCompany(companyEntity.getCompanyId(),billCycleEntity.getYearMonth());
                    if(usedCount!=billCycleEntity.getCnt()){
                        System.out.println("Wrong:"+billCycleEntity.getBillId()+", usedCount:"+usedCount+",current cnt:"+billCycleEntity.getCnt());
                    }
                }
            }

        }
    }
}
