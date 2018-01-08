package com.gate.web.facades;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.CashDetailVO;
import com.gate.web.displaybeans.CashMasterVO;
import com.gate.web.displaybeans.GiftVO;
import com.gate.web.formbeans.GiftBean;
import dao.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import java.util.List;
import java.util.Map;


public class CashServiceImp implements CashService {
    CashDAO dao = new CashDAO();

    public Map getCashMaster(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = dao.getCashMaster(querySettingVO);
        return returnMap;
    }

    public List getYM() throws Exception {
        return dao.getYM();
    }

    public List getCompany() throws Exception {
        return dao.getCompnay();
    }

    public Integer out(String masterIdAry) throws Exception{
        return dao.transactionSumOut(masterIdAry);
    }

    public Integer outYM(String outYM, Integer userCompanyId) throws Exception{
        return dao.transactionSumOutYM(outYM, userCompanyId);
    }

    public String excelSumIn(String businesscode, String inDate, String bankYM, Double inMoney) throws Exception{
        return dao.transactionExcelSumIn(businesscode, inDate, bankYM, inMoney);
    }

    public List<CashDetailVO> getCashDetailListByMasterId(Integer cashMasterId) throws Exception{
        return dao.getCashDetailListByMasterId(cashMasterId);
    }

    public List<BillCycleEntity> getOverListByDetailId(Integer cashDetailId) throws Exception{
        return dao.getOverListByDetailId(cashDetailId);
    }

    public CashMasterVO getCashMasterByMasterId(Integer cashMasterId) throws  Exception{
        return dao.getCashMasterByMasterId(cashMasterId);
    }

    public boolean updateCashDetail(Integer cashDetailId, Double diffPrice, String diffPriceNote) throws Exception{
        return dao.transactionUpdateCashDetail(cashDetailId, diffPrice, diffPriceNote);
    }

    public boolean in(Integer cashMasterId, Double inAmount, String inDate, String inNote) throws Exception{
        return dao.transactionIn(cashMasterId, inAmount, inDate, inNote);
    }

    public List getCashMasterDetail(String ym) throws Exception{
        return dao.getCashMasterDetail(ym);
    }

    public List getCashMasterDetail(String ym, String destJson) throws Exception{
        return dao.getCashMasterDetail(ym, destJson);
    }

    public boolean cancelOver(Integer cashDetailId) throws Exception{
        return dao.transactionCancelOver(cashDetailId);
    }

    public Integer cancelOutYM(String outYM) throws Exception {
        return dao.transactionCancelOutYM(outYM);
    }

    public Integer cancelOut(String masterIdAry) throws Exception {
        return dao.transactionCancelOut(masterIdAry);
    }

    public Integer sendBillMailYM(String outYM) throws Exception{
        return dao.transactionSendBillMailYM(outYM);
    }

    public Integer sendBillMail(String masterIdAry) throws Exception{
        return dao.transactionSendBillMail(masterIdAry);
    }
    public Integer sendBillMail1(String masterIdAry) throws Exception{
        return dao.transactionSendBillMail1(masterIdAry);
    }


    public List getInvoiceItem(String ym) throws Exception{
        return dao.getInvoiceItem(ym);
    }

    public List getInvoiceItem(String ym, String destJson) throws Exception{
        return dao.getInvoiceItem(ym, destJson);
    }

    public Integer transactionCancelIn(String strCashMasterId) throws Exception{
        return dao.transactionCancelIn(strCashMasterId);
    }

    public boolean cancelPrepay(Integer cashDetailId) throws Exception{
        return dao.transactionCancelPrepay(cashDetailId);
    }

    public boolean delCashMaster(Integer cashMasterId)throws Exception{
        return dao.delCashMaster(cashMasterId);
    }
}
