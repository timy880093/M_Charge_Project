package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.GiftVO;
import com.gate.web.formbeans.GiftBean;
import com.gateweb.charge.model.BillCycleEntity;
import com.gateweb.charge.model.CompanyEntity;
import com.gateweb.charge.model.GiftEntity;

import dao.CalCycleDAO;

@Service("calCycleService")
public class CalCycleServiceImp implements CalCycleService {
	
	@Autowired
    CalCycleDAO calCycleDAO;

    public Map getBillCycleList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = calCycleDAO.getBillCycleList(querySettingVO);
        return returnMap;
    }

    public List getYM() throws Exception {
        return calCycleDAO.getYM();
    }

    public List getUserCompanyList() throws Exception{
        return calCycleDAO.getUserCompanyList();
    }

    public Integer calBatchOver(String calYM, Integer modifierId) throws Exception {
        return calCycleDAO.transactionCalBatchOver(calYM, modifierId);
    }

    public Integer calOver(String calOverAry, Integer modifierId) throws Exception {
        return calCycleDAO.transactionCalOver(calOverAry, modifierId);
    }

    public boolean calOverToCash(String calYM, Integer companyId, String calOverAry, Integer modifierId) throws Exception {
        return calCycleDAO.transactionCalOverToCash(calYM, companyId, calOverAry, modifierId);
    }

//    public List calOver(String calYM, String companyId) throws Exception {
//        return dao.transactionCalOver(calYM, companyId);
//    }

    public void updateGift(GiftBean bean) throws Exception {
        GiftEntity entity = new GiftEntity();
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern("yyyy/MM/dd");
        BeanUtils.copyProperties(entity, bean);
        calCycleDAO.updateEntity(entity, entity.getGiftId());
    }

    public Integer insertGift(GiftBean bean) throws Exception {
        GiftEntity entity = new GiftEntity();
        BeanUtils.copyProperties(entity, bean);
        calCycleDAO.saveEntity(entity);
        return null;
    }

    public GiftVO findGiftByBillId(Integer billId) throws Exception {
        BillCycleEntity billCycleEntity = (BillCycleEntity)calCycleDAO.getEntity(BillCycleEntity.class, billId);
        GiftVO giftVO = new GiftVO();
        giftVO.setBillId(billCycleEntity.getBillId());
        giftVO.setCompanyId(billCycleEntity.getCompanyId());
        giftVO.setCalYM(billCycleEntity.getYearMonth());
        giftVO.setCntGift(billCycleEntity.getCntGift());
        giftVO.setIsCalculated(true); //超額已經計算的話，不能再修改贈送點數了。
        if(null == billCycleEntity.getCashOutOverId()){
            giftVO.setIsCalculated(false); //超額已經計算的話，不能再修改贈送點數了。
        }
        CompanyEntity cpEntity = (CompanyEntity)calCycleDAO.getEntity(CompanyEntity.class, billCycleEntity.getCompanyId());
        giftVO.setCompanyName(cpEntity.getName());

        return giftVO;
    }

    public boolean updateCntGiftByBillId(Integer billId, Integer cntGift) throws Exception {
        BillCycleEntity billCycleEntity = (BillCycleEntity)calCycleDAO.getEntity(BillCycleEntity.class, billId);
        billCycleEntity.setCntGift(cntGift);
        calCycleDAO.saveOrUpdateEntity(billCycleEntity, billId);
        return true;
    }

    public boolean deleteGiftByGiftId(Integer giftId) throws Exception {
        GiftEntity giftEntity = (GiftEntity) calCycleDAO.getEntity(GiftEntity.class,giftId);
        calCycleDAO.deleteEntity(giftEntity);
        return true;
    }

    public Integer sendOverMailYM(String calYM) throws Exception{
        return calCycleDAO.sendOverMailYM(calYM);
    }

    public Integer sendOverMail(String calOverAry) throws Exception{
        return calCycleDAO.sendOverMail(calOverAry);
    }
}
