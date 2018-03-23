package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.ChargeModeCycleVO;
import com.gate.web.displaybeans.ChargeModeGradeVO;
import com.gate.web.formbeans.ChargeModeCycleBean;
import com.gate.web.formbeans.ChargeModeGradeBean;
import com.gateweb.charge.model.ChargeModeCycleEntity;
import com.gateweb.charge.model.GradeEntity;

import dao.ChargeDAO;

@Service("chargeService")
public class ChargeServiceImp implements ChargeService{
	
	@Autowired
    ChargeDAO chargeDAO;


    public Map getChargeList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = chargeDAO.getChargeList(querySettingVO);
        return returnMap;
    }


    @Override
    public Integer insertChargeModeCycle(ChargeModeCycleBean bean, Long userId) throws Exception {
        ChargeModeCycleEntity entity = new ChargeModeCycleEntity();
        BeanUtils.copyProperties(entity, bean);
        entity.setCreatorId(userId.intValue());
        chargeDAO.saveEntity(entity);
        return null;
    }
    @Override
    public void updateChargeModeCycle(ChargeModeCycleBean bean, Long userId) throws Exception {
        ChargeModeCycleEntity entity = new ChargeModeCycleEntity();
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern("yyyy/MM/dd");
        BeanUtils.copyProperties(entity,bean);
        entity.setCreatorId(userId.intValue());
        chargeDAO.updateEntity(entity, entity.getChargeId());
    }

    public ChargeModeCycleVO findChargeModeCycleByChargeId(Integer chargeId) throws Exception {
        ChargeModeCycleEntity chargeEntity = (ChargeModeCycleEntity) chargeDAO.getEntity(ChargeModeCycleEntity.class,chargeId);
        ChargeModeCycleVO chargeVO = new ChargeModeCycleVO();
        BeanUtils.copyProperties(chargeVO,chargeEntity);
        Map map = chargeDAO.getCreatorAndModifier(chargeVO.getCreatorId(),chargeVO.getModifierId());
        chargeVO.setCreator((String) map.get("creator"));
        chargeVO.setModifier((String) map.get("modifier"));
        return chargeVO;
    }

    //新增或修改經銷商和經銷商業務員資訊
    @Override
    public Integer transactionInsertChargeModeGrade(ChargeModeGradeBean bean,Long userId) throws Exception {
        return chargeDAO.insertChargeModeGrade(bean,userId);
    }

    //找到級距型方案的資料
    public ChargeModeGradeVO findChargeModeGradeByChargeId(Integer chargeId) throws Exception {
        return chargeDAO.findChargeModeGradeByChargeId(chargeId);
    }

    //取得某級距方案的級距清單
    public List<GradeEntity> getGradeList(Integer chargeId) throws Exception {
        return chargeDAO.getGradeList(chargeId);
    }

    public void changeChargeModeStatus(String type, Integer chargeId, Integer status) throws Exception {
        if("1".equals(type)){
            chargeDAO.changeChargeModeCycleStatus(chargeId, status);
        } else if("2".equals(type)){
            chargeDAO.changeChargeModeGradeStatus(chargeId, status);
        }
    }

}
