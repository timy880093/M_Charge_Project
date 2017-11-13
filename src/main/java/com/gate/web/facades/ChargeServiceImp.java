package com.gate.web.facades;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.ChargeModeCycleVO;
import com.gate.web.displaybeans.ChargeModeGradeVO;
import com.gate.web.formbeans.*;
import dao.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ChargeServiceImp implements ChargeService{
    ChargeDAO dao = new ChargeDAO();


    public Map getChargeList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = dao.getChargeList(querySettingVO);
        return returnMap;
    }



    public Integer insertChargeModeCycle(ChargeModeCycleBean bean) throws Exception {
        return insertChargeModeCycle(bean);
    }


    public void updateChargeModeCycle(ChargeModeCycleBean bean) throws Exception {
        updateChargeModeCycle(bean);
    }

    public ChargeModeCycleVO findChargeModeCycleByChargeId(Integer chargeId) throws Exception {
        ChargeModeCycleEntity chargeEntity = (ChargeModeCycleEntity) dao.getEntity(ChargeModeCycleEntity.class,chargeId);
        ChargeModeCycleVO chargeVO = new ChargeModeCycleVO();
        BeanUtils.copyProperties(chargeVO,chargeEntity);
        Map map = dao.getCreatorAndModifier(chargeVO.getCreatorId(),chargeVO.getModifierId());
        chargeVO.setCreator((String) map.get("creator"));
        chargeVO.setModifier((String) map.get("modifier"));
        return chargeVO;
    }

    //新增或修改經銷商和經銷商業務員資訊
    public Integer transactionInsertChargeModeGrade(ChargeModeGradeBean bean) throws Exception {
        return dao.insertChargeModeGrade(bean);
    }

    //找到級距型方案的資料
    public ChargeModeGradeVO findChargeModeGradeByChargeId(Integer chargeId) throws Exception {
        return dao.findChargeModeGradeByChargeId(chargeId);
    }

    //取得某級距方案的級距清單
    public List<GradeEntity> getGradeList(Integer chargeId) throws Exception {
        return dao.getGradeList(chargeId);
    }

    public void changeChargeModeStatus(String type, Integer chargeId, Integer status) throws Exception {
        if("1".equals(type)){
            dao.changeChargeModeCycleStatus(chargeId, status);
        } else if("2".equals(type)){
            dao.changeChargeModeGradeStatus(chargeId, status);
        }
    }

}
