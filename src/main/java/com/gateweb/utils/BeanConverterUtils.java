package com.gateweb.utils;

import com.gate.utils.TimeUtils;
import com.gate.web.formbeans.CompanyChargeCycleBean;
import com.gateweb.charge.model.ChargeModeCycleAddEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Eason on 3/8/2018.
 */
@Component("beanConverterUtils")
public class BeanConverterUtils {
    public ChargeModeCycleAddEntity companyChargeCycleBeanToChargeModeCycleEntity(CompanyChargeCycleBean companyChargeCycleBean){
        ChargeModeCycleAddEntity addEntity = new ChargeModeCycleAddEntity();
        addEntity.setCreateDate(TimeUtils.getCurrentTimestamp());
        addEntity.setModifyDate(TimeUtils.getCurrentTimestamp());
        addEntity.setRealEndDate(TimeUtils.stringToDate(companyChargeCycleBean.getRealEndDate(),"yyyy-MM-dd"));
        addEntity.setRealStartDate(TimeUtils.stringToDate(companyChargeCycleBean.getRealStartDate(),"yyyy-MM-dd"));
        addEntity.setFreeMonth(companyChargeCycleBean.getFreeMonth());
        addEntity.setGiftPrice(new BigDecimal(companyChargeCycleBean.getGiftPrice()));
        return addEntity;
    }
}
