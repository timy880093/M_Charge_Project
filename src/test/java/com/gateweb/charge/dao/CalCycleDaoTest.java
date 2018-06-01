package com.gateweb.charge.dao;

import com.gate.utils.TimeUtils;
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

    @Autowired
    TimeUtils timeUtils;

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
//
//        Calendar from = Calendar.getInstance();
//        from.setTime(timeUtils.stringToDate("20180101","yyyyMMdd"));
//        from.add(Calendar.DATE,-1);
//
//        Calendar to = Calendar.getInstance();
//        to.setTime(timeUtils.stringToDate("20180301","yyyyMMdd"));
//        to.add(Calendar.MONTH,1);

        List<String> messageList = new ArrayList<>();
        List<CompanyEntity> companyEntityList = companyRepository.findAll();
        for(CompanyEntity companyEntity: companyEntityList){
            if(!companyEntity.getBusinessNo().equals("22099548")){
                continue;
            }
            Integer usedCount
                    = calCycleDAO.calOverByCompanyWithFromInvoiceAmountSummaryReport(companyEntity.getCompanyId(),"201801");
            if(usedCount>0){
                messageList.add(companyEntity.getName() +":"+ usedCount);
            }
        }

        for(String message:messageList){
            System.out.println(message);
        }
    }

    @Test
    public void calOverByCompany72665011(){
//        Calendar from = Calendar.getInstance();
//        from.setTime(timeUtils.stringToDate("20180101","yyyyMMdd"));
//
//        Calendar to = Calendar.getInstance();
//        to.setTime(timeUtils.stringToDate("20180201","yyyyMMdd"));
        Integer usedCount = calCycleDAO.calOverByCompanyWithFromInvoiceAmountSummaryReport(1535,"201801");
        System.out.println(usedCount);
    }

    @Test
    public void calOverCompare(){
        List<CompanyEntity> companyEntityList = companyRepository.findAll();
        List<String> messageList = new ArrayList<>();
        for(CompanyEntity companyEntity: companyEntityList){
            Integer usedCount1 = calCycleDAO.calOverByCompanyWithFromInvoiceAmountSummaryReport(companyEntity.getCompanyId(),"201801");
            Integer usedCount2 = calCycleDAO.calOverByCompany(companyEntity.getCompanyId(),"201801");
            if(usedCount1.compareTo(usedCount2)!=0){
                messageList.add("CompanyId:" + companyEntity.getCompanyId()+",usedCount1:"+usedCount1+",usedCount2:"+usedCount2);
            }
        }
        for(String message:messageList){
            System.out.println(message);
        }
    }
}
