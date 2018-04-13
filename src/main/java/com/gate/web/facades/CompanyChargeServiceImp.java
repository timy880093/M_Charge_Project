package com.gate.web.facades;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gate.utils.TimeUtils;
import com.gateweb.charge.model.ChargeModeCycleAddEntity;
import com.gateweb.charge.model.PackageModeEntity;
import com.gateweb.charge.repository.ChargeModeCycleAddRepository;
import com.gateweb.charge.repository.ChargeModeCycleRepository;
import com.gateweb.charge.repository.ChargeModeGradeRepository;
import com.gateweb.charge.repository.PackageModeRepository;
import com.gateweb.utils.BeanConverterUtils;
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

    @Autowired
    ChargeModeCycleRepository chargeModeCycleRepository;

    @Autowired
    ChargeModeGradeRepository chargeModeGradeRepository;

    @Autowired
    BeanConverterUtils beanConverterUtils;

    @Autowired
    ChargeModeCycleAddRepository chargeModeCycleAddRepository;

    @Autowired
    PackageModeRepository packageModeRepository;

    @Autowired
    CompanyChargeDAO companyCHargeDAO;


    @Autowired
    TimeUtils timeUtils;

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

    /**
     * 新增月租/級距合約(用戶綁合約)
     * @param bean
     * @throws Exception
     */
    @Override
    public void transactionInsertCompanyChargeCycle(CompanyChargeCycleBean bean, Integer modifierId) throws Exception {
        Integer chargeType = bean.getPackageType(); //收費方式 1.月租 2.級距

        //ChargeModeCycleEntity cycleEntity = (ChargeModeCycleEntity) getEntity(ChargeModeCycleEntity.class, bean.getChargeId());
        ChargeModeCycleEntity cycleEntity = null;
        ChargeModeGradeEntity gradeEntity = null;
        if(1 == chargeType){
            //月租型
            cycleEntity = chargeModeCycleRepository.findByChargeId(bean.getChargeId());
        }else if(2 == chargeType){
            //級距型
            gradeEntity = chargeModeGradeRepository.findByChargeId(bean.getChargeId());
        }

        //新增charge_mode_cycle_add
        ChargeModeCycleAddEntity addEntity = beanConverterUtils.companyChargeCycleBeanToChargeModeCycleEntity(bean);
        java.sql.Date sqlStartDate = new java.sql.Date(timeUtils.getMonthOneDay(addEntity.getRealStartDate()).getTime()); //實際計算日起的當月的第一日，就是計算日起的日期

        java.util.Date sqlEndDate = addEntity.getRealEndDate();
        Calendar calEndDate = Calendar.getInstance();
        calEndDate.setTime(sqlEndDate);
        Date monthLastDay = timeUtils.getMonthLastDay(calEndDate.getTime());
        Integer realEndDateMaxDay = monthLastDay.getDate();
        if(calEndDate.get(Calendar.DATE) != realEndDateMaxDay){ //如果(實際計算日迄)的當月最後一天不等於(實際計算日迄)日期，那麼(計算日迄日期)應該是(實際計算日迄)的上個月的最後一天
            calEndDate.add(Calendar.MONTH, -1);
        }
        calEndDate.getTime();
        java.sql.Date endDate = new java.sql.Date(timeUtils.getMonthLastDay(calEndDate.getTime()).getTime());

        addEntity.setStartDate(sqlStartDate); //計算日起(某月的第一日)
        addEntity.setEndDate(endDate); //計算日迄(某月的最後一日)
        chargeModeCycleAddRepository.save(addEntity);

        //新增package_mode
        PackageModeEntity packageModeEntity = new PackageModeEntity();
        BeanUtils.copyProperties(packageModeEntity, bean);
        packageModeEntity.setAdditionId(addEntity.getAdditionId());
        packageModeEntity.setPackageType(chargeType);
        packageModeEntity.setStatus(String.valueOf(1) ); //0.未生效 1.生效 2.作廢
        packageModeRepository.save(packageModeEntity);

        //計算....................................................................
        //月租收費區間 1年繳 2.季繳 3.月繳.
        //合約生效後，產生月租帳單(bill_cycle和cash_flow的資料)
        //int feePeriod = cycleEntity.getFeePeriod();
        int feePeriod = 0;
        if(1 == chargeType){
            //月租型
            feePeriod = cycleEntity.getFeePeriod();
        } else if(2 == chargeType){
            //級距型
            feePeriod = gradeEntity.getFeePeriod();
        }

        if(1 == feePeriod){ //1.年繳
            companyChargeDAO.genBillCashData(true, 0, -3, cycleEntity, packageModeEntity, bean, addEntity, false, true, chargeType, gradeEntity, modifierId);
        }else if(2 == feePeriod){ //2.季繳
            companyChargeDAO.genBillCashData(false, 3, -3, cycleEntity, packageModeEntity, bean, addEntity, false, true, chargeType, gradeEntity, modifierId);
        }else if(3 == feePeriod){ //3.月繳
            companyChargeDAO.genBillCashData(false, 1, -3, cycleEntity, packageModeEntity, bean, addEntity, false, true, chargeType, gradeEntity, modifierId);
        }

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

    public Map continuePackage(String almostOut, Integer modifierId) throws Exception{
        return companyChargeDAO.transactionContinuePackage(almostOut, modifierId);
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
