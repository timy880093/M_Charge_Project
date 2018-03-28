package com.gateweb.charge.repository;

import com.gateweb.charge.model.DealerCompanyEntity;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Eason on 3/27/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class DealerCompanyRepositoryTest {
    @Autowired
    DealerCompanyRepository dealerCompanyRepository;

    Gson gson = new Gson();

    @Test
    public void findDealerCompanyByStatus(){
        List<DealerCompanyEntity> dealerCompanyEntityList = dealerCompanyRepository.findByStatus(1);
        for(DealerCompanyEntity dealerCompanyEntity: dealerCompanyEntityList){
            System.out.println(gson.toJson(dealerCompanyEntity));
        }
    }

    @Test
    public void findDealerCompanyByDealerCompanyId(){
        DealerCompanyEntity dealerCompanyEntity = dealerCompanyRepository.findByDealerCompanyId(2);
        System.out.println(gson.toJson(dealerCompanyEntity));
    }
}
