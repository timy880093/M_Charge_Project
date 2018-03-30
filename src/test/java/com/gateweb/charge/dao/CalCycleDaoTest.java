package com.gateweb.charge.dao;

import com.gateweb.charge.model.CompanyEntity;
import com.gateweb.charge.repository.CompanyRepository;
import dao.CalCycleDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Eason on 3/28/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class CalCycleDaoTest {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CalCycleDAO calCycleDAO;

    @Test
    public void calOverByCompanyTest(){
        List<String> messageList = new ArrayList<>();
        List<CompanyEntity> companyEntityList = companyRepository.findAll();
        for(CompanyEntity companyEntity: companyEntityList){
            Integer usedCount = calCycleDAO.calOverByCompany(companyEntity.getCompanyId(),"201803");
            if(usedCount>0){
                messageList.add(companyEntity.getName() +":"+ usedCount);
            }
        }
        for(String message:messageList){
            System.out.println(message);
        }
    }

    @Test
    public void calOverByCompanyAndInvoiceAmountSummaryReport(){

        Calendar from = Calendar.getInstance();
        from.setTime(new Date());
        from.add(Calendar.DATE,-16);

        Calendar to = Calendar.getInstance();
        to.setTime(new Date());
        to.add(Calendar.DATE,-1);

        List<String> messageList = new ArrayList<>();
        List<CompanyEntity> companyEntityList = companyRepository.findAll();
        for(CompanyEntity companyEntity: companyEntityList){
            Integer usedCount
                    = calCycleDAO.calOverByCompanyWithFromInvoiceAmountSummaryReport(companyEntity,from.getTime(),to.getTime());
            if(usedCount>0){
                messageList.add(companyEntity.getName() +":"+ usedCount);
            }
        }
        for(String message:messageList){
            System.out.println(message);
        }
    }
}
