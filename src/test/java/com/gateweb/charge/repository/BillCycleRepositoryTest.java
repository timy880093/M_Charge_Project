package com.gateweb.charge.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.BillCycleEntity;
import com.gateweb.orm.charge.entity.CashDetailEntity;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.BillCycleRepository;
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
import java.util.Optional;

/**
 * Created by Eason on 3/28/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class BillCycleRepositoryTest {

    @Autowired
    BillCycleRepository billCycleRepository;

    @Autowired
    CashDetailRepository cashDetailRepository;

    @Autowired
    CompanyRepository companyRepository;

    Gson gson = new Gson();

    @Test
    public void findByYearMonthAndCompanyIdTest() {
        List<Company> companyEntityList = companyRepository.findAll();
        for (Company companyEntity : companyEntityList) {
            List<BillCycleEntity> billCycleEntityList = billCycleRepository.findByYearMonthAndCompanyIdIs("201803", companyEntity.getCompanyId());
            for (BillCycleEntity billCycleEntity : billCycleEntityList) {
                System.out.println(billCycleEntity.toString());
            }
        }
    }

    @Test
    public void findByCashOutOverIdTest() {
        List<String> messageList = new ArrayList<>();
        List<CashDetailEntity> cashDetailEntityList = cashDetailRepository.findAll();
        for (CashDetailEntity cashDetailEntity : cashDetailEntityList) {
            List<BillCycleEntity> billCycleEntityList = billCycleRepository.findByCashOutOverId(cashDetailEntity.getCashDetailId());
            if (billCycleEntityList.size() == 1) {
                messageList.add(gson.toJson(billCycleEntityList.get(0)));
            } else {
                messageList.add("strange data :" + cashDetailEntity.getCashDetailId());
            }
        }
        for (String message : messageList) {
            System.out.println(message);
        }
    }
}
