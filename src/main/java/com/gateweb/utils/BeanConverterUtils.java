package com.gateweb.utils;

import com.gate.utils.TimeUtils;
import com.gate.web.formbeans.CompanyChargeCycleBean;
import com.gateweb.charge.model.ChargeModeCycleAddEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Eason on 3/8/2018.
 */
@Component("beanConverterUtils")
public class BeanConverterUtils {
    @Autowired
    TimeUtils timeUtils;

    public ChargeModeCycleAddEntity companyChargeCycleBeanToChargeModeCycleEntity(CompanyChargeCycleBean companyChargeCycleBean){
        ChargeModeCycleAddEntity addEntity = new ChargeModeCycleAddEntity();
        addEntity.setCreateDate(timeUtils.getCurrentTimestamp());
        addEntity.setModifyDate(timeUtils.getCurrentTimestamp());
        addEntity.setRealEndDate(timeUtils.stringToDate(companyChargeCycleBean.getRealEndDate(),"yyyy-MM-dd"));
        addEntity.setRealStartDate(timeUtils.stringToDate(companyChargeCycleBean.getRealStartDate(),"yyyy-MM-dd"));
        addEntity.setFreeMonth(companyChargeCycleBean.getFreeMonth());
        addEntity.setGiftPrice(new BigDecimal(companyChargeCycleBean.getGiftPrice()));
        return addEntity;
    }
}
