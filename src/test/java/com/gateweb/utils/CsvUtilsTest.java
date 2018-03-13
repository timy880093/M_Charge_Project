package com.gateweb.utils;

import com.gate.utils.CsvUtils;
import com.gate.web.facades.CashService;
import com.gateweb.charge.vo.CashVO;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eason on 3/13/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class CsvUtilsTest {
    Gson gson = new Gson();

    @Autowired
    CashService cashService;

    @Autowired
    CsvUtils csvUtils;

    @Test
    public void CsvUtilsTestByCashVO(){
        List<String> cashMasterEntityExcludeParameterList = new ArrayList<>();
        cashMasterEntityExcludeParameterList.add("companyId");
        cashMasterEntityExcludeParameterList.add("createDate");
        cashMasterEntityExcludeParameterList.add("creatorId");
        cashMasterEntityExcludeParameterList.add("modifierId");
        cashMasterEntityExcludeParameterList.add("modifyDate");
        List<String> cashDetailEntityExcludeParameterList = new ArrayList<>();
        cashDetailEntityExcludeParameterList.add("companyId");
        cashDetailEntityExcludeParameterList.add("createDate");
        cashDetailEntityExcludeParameterList.add("creatorId");
        cashDetailEntityExcludeParameterList.add("modifierId");
        cashDetailEntityExcludeParameterList.add("modifyDate");
        List<CashVO> cashVOList = cashService.findCashVoByOutYm("201712");
        for(CashVO cashVO : cashVOList){
            List<String> lineDataList = csvUtils.combineBeanToList(
                    cashVO.getCashMasterEntity()
                    , cashMasterEntityExcludeParameterList
                    , cashVO.getCashDetailEntityList()
                    , cashDetailEntityExcludeParameterList
                    , ","
            );
            for(String lineData : lineDataList){
                System.out.println(lineData);
            }
        }
    }

}
