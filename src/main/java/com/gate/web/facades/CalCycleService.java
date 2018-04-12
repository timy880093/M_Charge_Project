package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import com.gateweb.charge.model.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.GiftVO;
import com.gate.web.formbeans.GiftBean;

public interface CalCycleService extends Service {

	public Map getBillCycleList(QuerySettingVO querySettingVO) throws Exception;

    public List getYM() throws Exception;

    public List getUserCompanyList() throws Exception;

    //計算超額-by年月
    Integer batchCalOverByYearMonthAndCompanyId(Integer companyId, String yearMonth, Integer modifierId);

    //計算超額-多筆
    Integer batchCalOver(List<Integer> billIdList, Integer modifierId) throws Exception;

    CashMasterEntity isHaveCashMaster(String outYm, Integer companyId, Integer modifierId) throws Exception;

    void transactionWriteBillCycleOverData(
            List<BillCycleEntity> billCycleEntityList
            , boolean isSum
            , PackageModeEntity packageModeEntity
            , Integer modifierId) throws Exception;

    public boolean calOverToCash(String calYM, Integer companyId, String calOverAry, Integer modifierId) throws Exception;
//    public List calOver(String calYM, String companyId) throws Exception {
//        return dao.transactionCalOver(calYM, companyId);
//    }

    public void updateGift(GiftBean bean) throws Exception;

    public Integer insertGift(GiftBean bean) throws Exception;

    public GiftVO findGiftByBillId(Integer billId) throws Exception;

    public boolean updateCntGiftByBillId(Integer billId, Integer cntGift) throws Exception;

    public boolean deleteGiftByGiftId(Integer giftId) throws Exception;
    
    public Integer sendOverMailYM(String calYM) throws Exception;
    
    public Integer sendOverMail(String calOverAry) throws Exception;
}
