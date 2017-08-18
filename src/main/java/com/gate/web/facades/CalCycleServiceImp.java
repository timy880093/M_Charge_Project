package com.gate.web.facades;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.GiftVO;
import com.gate.web.formbeans.GiftBean;
import dao.BillCycleEntity;
import dao.CalCycleDAO;
import dao.CompanyEntity;
import dao.GiftEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import java.util.List;
import java.util.Map;


public class CalCycleServiceImp implements CalCycleService {
    CalCycleDAO dao = new CalCycleDAO();

    public Map getBillCycleList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = dao.getBillCycleList(querySettingVO);
        return returnMap;
    }

    public List getYM() throws Exception {
        return dao.getYM();
    }

    public List getUserCompanyList() throws Exception{
        return dao.getUserCompanyList();
    }

    public Integer calBatchOver(String calYM) throws Exception {
        return dao.transactionCalBatchOver(calYM);
    }

    public Integer calOver(String calOverAry) throws Exception {
        return dao.transactionCalOver(calOverAry);
    }

    public boolean calOverToCash(String calYM, Integer companyId, String calOverAry) throws Exception {
        return dao.transactionCalOverToCash(calYM, companyId, calOverAry);
    }

//    public List calOver(String calYM, String companyId) throws Exception {
//        return dao.transactionCalOver(calYM, companyId);
//    }

    public void updateGift(GiftBean bean) throws Exception {
        GiftEntity entity = new GiftEntity();
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern("yyyy/MM/dd");
        BeanUtils.copyProperties(entity, bean);
        dao.updateEntity(entity, entity.getGiftId());
    }

    public Integer insertGift(GiftBean bean) throws Exception {
        GiftEntity entity = new GiftEntity();
        BeanUtils.copyProperties(entity, bean);
        dao.saveEntity(entity);
        return null;
    }

    public GiftVO findGiftByBillId(Integer billId) throws Exception {
        BillCycleEntity billCycleEntity = (BillCycleEntity)dao.getEntity(BillCycleEntity.class, billId);
        GiftVO giftVO = new GiftVO();
        giftVO.setBillId(billCycleEntity.getBillId());
        giftVO.setCompanyId(billCycleEntity.getCompanyId());
        giftVO.setCalYM(billCycleEntity.getYearMonth());
        giftVO.setCntGift(billCycleEntity.getCntGift());
        giftVO.setIsCalculated(true); //超額已經計算的話，不能再修改贈送點數了。
        if(null == billCycleEntity.getCashOutOverId()){
            giftVO.setIsCalculated(false); //超額已經計算的話，不能再修改贈送點數了。
        }
        CompanyEntity cpEntity = (CompanyEntity)dao.getEntity(CompanyEntity.class, billCycleEntity.getCompanyId());
        giftVO.setCompanyName(cpEntity.getName());

        return giftVO;
    }

    public boolean updateCntGiftByBillId(Integer billId, Integer cntGift) throws Exception {
        BillCycleEntity billCycleEntity = (BillCycleEntity)dao.getEntity(BillCycleEntity.class, billId);
        billCycleEntity.setCntGift(cntGift);
        dao.saveOrUpdateEntity(billCycleEntity, billId);
        return true;
    }

    public boolean deleteGiftByGiftId(Integer giftId) throws Exception {
        GiftEntity giftEntity = (GiftEntity) dao.getEntity(GiftEntity.class,giftId);
        dao.deleteEntity(giftEntity);
        return true;
    }

    public Integer sendOverMailYM(String calYM) throws Exception{
        return dao.sendOverMailYM(calYM);
    }

    public Integer sendOverMail(String calOverAry) throws Exception{
        return dao.sendOverMail(calOverAry);
    }
}
