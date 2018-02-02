package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.displaybeans.ChargeModeCycleVO;
import com.gate.web.displaybeans.ChargeModeGradeVO;
import com.gate.web.formbeans.CompanyChargeCycleBean;
import com.gateweb.charge.model.ChargeModeCycleEntity;
import com.gateweb.charge.model.ChargeModeGradeEntity;

import dao.CompanyChargeDAO;

@Service("companyChargeService")
public class CompanyChargeServiceImp implements CompanyChargeService{
	
	@Autowired
    CompanyChargeDAO companyChargeDAO;

    public ChargeModeCycleVO findChargeModeCycleByChargeId(Integer chargeId) throws Exception {
        ChargeModeCycleEntity chargeEntity = (ChargeModeCycleEntity) companyChargeDAO.getEntity(ChargeModeCycleEntity.class,chargeId);
        ChargeModeCycleVO chargeVO = new ChargeModeCycleVO();
        BeanUtils.copyProperties(chargeVO,chargeEntity);
        Map map = companyChargeDAO.getCreatorAndModifier(chargeVO.getCreatorId(),chargeVO.getModifierId());
        chargeVO.setCreator((String) map.get("creator"));
        chargeVO.setModifier((String) map.get("modifier"));
        return chargeVO;
    }

    public ChargeModeGradeVO findChargeModeGradeByChargeId(Integer chargeId) throws Exception {
        ChargeModeGradeEntity chargeEntity = (ChargeModeGradeEntity) companyChargeDAO.getEntity(ChargeModeGradeEntity.class,chargeId);
        ChargeModeGradeVO chargeVO = new ChargeModeGradeVO();
        BeanUtils.copyProperties(chargeVO,chargeEntity);
        Map map = companyChargeDAO.getCreatorAndModifier(chargeVO.getCreatorId(),chargeVO.getModifierId());
        chargeVO.setCreator((String) map.get("creator"));
        chargeVO.setModifier((String) map.get("modifier"));
        return chargeVO;
    }

    public List getDealerList(Integer dealerComplayId) throws Exception {
        List dealerList = companyChargeDAO.getDealerList(dealerComplayId);
        return dealerList;
    }

    public List getChargeMonthList() throws Exception {
        return companyChargeDAO.getChargeMonthList();
    }

    public List getChargeGradeList() throws Exception {
        return companyChargeDAO.getChargeGradeList();
    }

    public void insertCompanyChargeCycle(CompanyChargeCycleBean bean) throws Exception {
        companyChargeDAO.transactionInsertCompanyChargeCycle(bean);
    }

    public List<Map> getChargeCycleHisByCompany(Integer companyId) throws Exception {
        return companyChargeDAO.getChargeCycleHisByCompany(companyId);
    }

    public Map getSettleInfo(Integer companyId,Integer packageId) throws Exception{
        return companyChargeDAO.getSettleInfo(companyId, packageId);
    }

    public Map getCycleTryCalSettle(Integer packageId, String endDate) throws Exception{
        return companyChargeDAO.getCycleTryCalSettle(packageId, endDate);
    }

    public Map doSettle(Integer packageId, String endDate, String realEndDate) throws Exception{
        return companyChargeDAO.transactionDoSettle(packageId, endDate, realEndDate);
    }

    public Map continuePackage(String almostOut) throws Exception{
        return companyChargeDAO.transactionContinuePackage(almostOut);
    }

    public Map getCyclePackageInfoByPackageId(Integer packageId) throws Exception {
        Map infoMap = companyChargeDAO.getCyclePackageInfoByPackageId(packageId);
        Map map = companyChargeDAO.getCreatorAndModifier(Integer.parseInt(infoMap.get("creator_id").toString()),Integer.parseInt(infoMap.get("modifier_id").toString()));
        infoMap.put("creator",map.get("creator"));
        infoMap.put("modifier",map.get("modifier"));
        return infoMap;
    }

    public Map getGradePackageInfoByPackageId(Integer packageId) throws Exception {
        Map infoMap = companyChargeDAO.getGradePackageInfoByPackageId(packageId);
        Map map = companyChargeDAO.getCreatorAndModifier(Integer.parseInt(infoMap.get("creator_id").toString()),Integer.parseInt(infoMap.get("modifier_id").toString()));
        infoMap.put("creator",map.get("creator"));
        infoMap.put("modifier",map.get("modifier"));
        return infoMap;
    }
}
