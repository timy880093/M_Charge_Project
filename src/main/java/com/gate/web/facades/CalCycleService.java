package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.GiftVO;
import com.gate.web.formbeans.GiftBean;
import com.gateweb.charge.model.BillCycleEntity;
import com.gateweb.charge.model.CompanyEntity;
import com.gateweb.charge.model.GiftEntity;

public interface CalCycleService extends Service {

	public Map getBillCycleList(QuerySettingVO querySettingVO) throws Exception;

    public List getYM() throws Exception;

    public List getUserCompanyList() throws Exception;

    public Integer calBatchOver(String calYM, Integer modifierId) throws Exception;

    public Integer calOver(String calOverAry, Integer modifierId) throws Exception;

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
