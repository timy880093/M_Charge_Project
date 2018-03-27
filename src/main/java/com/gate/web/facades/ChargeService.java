package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.ChargeModeCycleVO;
import com.gate.web.displaybeans.ChargeModeGradeVO;
import com.gate.web.formbeans.ChargeModeCycleBean;
import com.gate.web.formbeans.ChargeModeGradeBean;
import com.gateweb.charge.model.ChargeModeCycleEntity;
import com.gateweb.charge.model.GradeEntity;

/**
 * Created by simon on 2014/7/11.
 */
public interface ChargeService extends Service {

	public Map getChargeList(QuerySettingVO querySettingVO) throws Exception ;

    Integer insertChargeModeCycle(ChargeModeCycleBean bean, Long userId) throws Exception;

    void updateChargeModeCycle(ChargeModeCycleBean bean, Long userId) throws Exception;

    public ChargeModeCycleVO findChargeModeCycleByChargeId(Integer chargeId) throws Exception ;

    //新增或修改經銷商和經銷商業務員資訊
    Integer transactionInsertChargeModeGrade(ChargeModeGradeBean bean, Long userId) throws Exception;

    //找到級距型方案的資料
    public ChargeModeGradeVO findChargeModeGradeByChargeId(Integer chargeId) throws Exception ;

    //取得某級距方案的級距清單
    public List<GradeEntity> getGradeList(Integer chargeId) throws Exception ;
    public void changeChargeModeStatus(String type, Integer chargeId, Integer status) throws Exception ;

}
