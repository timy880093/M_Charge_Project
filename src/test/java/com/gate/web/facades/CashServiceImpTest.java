package com.gate.web.facades;

import com.gateweb.charge.vo.CashVO;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Eason on 3/13/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class CashServiceImpTest {
    Gson gson = new Gson();

    @Autowired
    CashService cashService;

    //測試抓取CashVO
    @Test
    public void CashVOFindByOutYmTest(){
        List<CashVO> cashVOList = cashService.findCashVoByOutYm("201712");
        for(CashVO cashVO : cashVOList){
            System.out.println(gson.toJson(cashVO));
        }
    }

}
