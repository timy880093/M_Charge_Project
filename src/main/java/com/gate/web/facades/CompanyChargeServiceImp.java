package com.gate.web.facades;

import com.gate.utils.TimeUtils;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.ChargeModeCycleVO;
import com.gate.web.displaybeans.ChargeModeGradeVO;
import com.gate.web.formbeans.ChargeModeCycleBean;
import com.gate.web.formbeans.CompanyChargeCycleBean;
import dao.ChargeModeCycleEntity;
import dao.ChargeModeGradeEntity;
import dao.CompanyChargeDAO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class CompanyChargeServiceImp implements CompanyChargeService{
    CompanyChargeDAO dao = new CompanyChargeDAO();


    public ChargeModeCycleVO findChargeModeCycleByChargeId(Integer chargeId) throws Exception {
        ChargeModeCycleEntity chargeEntity = (ChargeModeCycleEntity) dao.getEntity(ChargeModeCycleEntity.class,chargeId);
        ChargeModeCycleVO chargeVO = new ChargeModeCycleVO();
        BeanUtils.copyProperties(chargeVO,chargeEntity);
        Map map = dao.getCreatorAndModifier(chargeVO.getCreatorId(),chargeVO.getModifierId());
        chargeVO.setCreator((String) map.get("creator"));
        chargeVO.setModifier((String) map.get("modifier"));
        return chargeVO;
    }

    public ChargeModeGradeVO findChargeModeGradeByChargeId(Integer chargeId) throws Exception {
        ChargeModeGradeEntity chargeEntity = (ChargeModeGradeEntity) dao.getEntity(ChargeModeGradeEntity.class,chargeId);
        ChargeModeGradeVO chargeVO = new ChargeModeGradeVO();
        BeanUtils.copyProperties(chargeVO,chargeEntity);
        Map map = dao.getCreatorAndModifier(chargeVO.getCreatorId(),chargeVO.getModifierId());
        chargeVO.setCreator((String) map.get("creator"));
        chargeVO.setModifier((String) map.get("modifier"));
        return chargeVO;
    }

    public List getDealerList(Integer dealerComplayId) throws Exception {
        List dealerList = dao.getDealerList(dealerComplayId);
        return dealerList;
    }

    public List getChargeMonthList() throws Exception {
        return dao.getChargeMonthList();
    }

    public List getChargeGradeList() throws Exception {
        return dao.getChargeGradeList();
    }

    public void insertCompanyChargeCycle(CompanyChargeCycleBean bean) throws Exception {
        dao.transactionInsertCompanyChargeCycle(bean);
    }

    public List<Map> getChargeCycleHisByCompany(Integer companyId) throws Exception {
        return dao.getChargeCycleHisByCompany(companyId);
    }

    public Map getSettleInfo(Integer companyId,Integer packageId) throws Exception{
        return dao.getSettleInfo(companyId, packageId);
    }

    public Map getCycleTryCalSettle(Integer packageId, String endDate) throws Exception{
        return dao.getCycleTryCalSettle(packageId, endDate);
    }

    public Map doSettle(Integer packageId, String endDate, String realEndDate) throws Exception{
        return dao.transactionDoSettle(packageId, endDate, realEndDate);
    }

    public Map continuePackage(String almostOut) throws Exception{
        return dao.transactionContinuePackage(almostOut);
    }

    public Map getCyclePackageInfoByPackageId(Integer packageId) throws Exception {
        Map infoMap = dao.getCyclePackageInfoByPackageId(packageId);
        Map map = dao.getCreatorAndModifier(Integer.parseInt(infoMap.get("creator_id").toString()),Integer.parseInt(infoMap.get("modifier_id").toString()));
        infoMap.put("creator",map.get("creator"));
        infoMap.put("modifier",map.get("modifier"));
        return infoMap;
    }

    public Map getGradePackageInfoByPackageId(Integer packageId) throws Exception {
        Map infoMap = dao.getGradePackageInfoByPackageId(packageId);
        Map map = dao.getCreatorAndModifier(Integer.parseInt(infoMap.get("creator_id").toString()),Integer.parseInt(infoMap.get("modifier_id").toString()));
        infoMap.put("creator",map.get("creator"));
        infoMap.put("modifier",map.get("modifier"));
        return infoMap;
    }
}
