package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.CashDetailVO;
import com.gate.web.displaybeans.CashMasterVO;
import com.gateweb.charge.model.BillCycleEntity;

public interface CashService extends Service {

    public Map getCashMaster(QuerySettingVO querySettingVO) throws Exception ;

    public List getYM() throws Exception ;
    public List getCompany() throws Exception ;
    public Integer out(String masterIdAry) throws Exception;

    public Integer outYM(String outYM, Integer userCompanyId) throws Exception;

    public String excelSumIn(String businesscode, String inDate, String bankYM, Double inMoney) throws Exception;

    public List<CashDetailVO> getCashDetailListByMasterId(Integer cashMasterId) throws Exception;

    public List<BillCycleEntity> getOverListByDetailId(Integer cashDetailId) throws Exception;

    public CashMasterVO getCashMasterByMasterId(Integer cashMasterId) throws  Exception;

    public boolean updateCashDetail(Integer cashDetailId, Double diffPrice, String diffPriceNote) throws Exception;

    public boolean in(Integer cashMasterId, Double inAmount, String inDate, String inNote) throws Exception;

    public List getCashMasterDetail(String ym) throws Exception;

    public List getCashMasterDetail(String ym, String destJson) throws Exception;

    public boolean cancelOver(Integer cashDetailId) throws Exception;

    public Integer cancelOutYM(String outYM) throws Exception ;

    public Integer cancelOut(String masterIdAry) throws Exception ;

    public Integer sendBillMailYM(String outYM) throws Exception;

    public Integer sendBillMail(String masterIdAry) throws Exception;
    public Integer sendBillMail1(String masterIdAry) throws Exception;

    public List getInvoiceItem(String ym) throws Exception;

    public List getInvoiceItem(String ym, String destJson) throws Exception;

    public Integer transactionCancelIn(String strCashMasterId) throws Exception;

    public boolean cancelPrepay(Integer cashDetailId) throws Exception;

    public boolean delCashMaster(Integer cashMasterId)throws Exception;
}
