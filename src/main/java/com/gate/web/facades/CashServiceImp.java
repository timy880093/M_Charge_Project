package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.CashDetailVO;
import com.gate.web.displaybeans.CashMasterVO;
import com.gateweb.charge.model.BillCycleEntity;

import dao.CashDAO;

@Service("cashService")
public class CashServiceImp implements CashService {
	
	@Autowired
    CashDAO cashDAO;

    public Map getCashMaster(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = cashDAO.getCashMaster(querySettingVO);
        return returnMap;
    }

    public List getYM() throws Exception {
        return cashDAO.getYM();
    }

    public List getCompany() throws Exception {
        return cashDAO.getCompnay();
    }

    public Integer out(String masterIdAry) throws Exception{
        return cashDAO.transactionSumOut(masterIdAry);
    }

    public Integer outYM(String outYM, Integer userCompanyId) throws Exception{
        return cashDAO.transactionSumOutYM(outYM, userCompanyId);
    }

    public String excelSumIn(String businesscode, String inDate, String bankYM, Double inMoney) throws Exception{
        return cashDAO.transactionExcelSumIn(businesscode, inDate, bankYM, inMoney);
    }

    public List<CashDetailVO> getCashDetailListByMasterId(Integer cashMasterId) throws Exception{
        return cashDAO.getCashDetailListByMasterId(cashMasterId);
    }

    public List<BillCycleEntity> getOverListByDetailId(Integer cashDetailId) throws Exception{
        return cashDAO.getOverListByDetailId(cashDetailId);
    }

    public CashMasterVO getCashMasterByMasterId(Integer cashMasterId) throws  Exception{
        return cashDAO.getCashMasterByMasterId(cashMasterId);
    }

    public boolean updateCashDetail(Integer cashDetailId, Double diffPrice, String diffPriceNote) throws Exception{
        return cashDAO.transactionUpdateCashDetail(cashDetailId, diffPrice, diffPriceNote);
    }

    public boolean in(Integer cashMasterId, Double inAmount, String inDate, String inNote) throws Exception{
        return cashDAO.transactionIn(cashMasterId, inAmount, inDate, inNote);
    }

    public List getCashMasterDetail(String ym) throws Exception{
        return cashDAO.getCashMasterDetail(ym);
    }

    public List getCashMasterDetail(String ym, String destJson) throws Exception{
        return cashDAO.getCashMasterDetail(ym, destJson);
    }

    public boolean cancelOver(Integer cashDetailId) throws Exception{
        return cashDAO.transactionCancelOver(cashDetailId);
    }

    public Integer cancelOutYM(String outYM) throws Exception {
        return cashDAO.transactionCancelOutYM(outYM);
    }

    public Integer cancelOut(String masterIdAry) throws Exception {
        return cashDAO.transactionCancelOut(masterIdAry);
    }

    public Integer sendBillMailYM(String outYM) throws Exception{
        return cashDAO.transactionSendBillMailYM(outYM);
    }

    public Integer sendBillMail(String masterIdAry) throws Exception{
        return cashDAO.transactionSendBillMail(masterIdAry);
    }
    public Integer sendBillMail1(String masterIdAry) throws Exception{
        return cashDAO.transactionSendBillMail1(masterIdAry);
    }


    public List getInvoiceItem(String ym) throws Exception{
        return cashDAO.getInvoiceItem(ym);
    }

    public List getInvoiceItem(String ym, String destJson) throws Exception{
        return cashDAO.getInvoiceItem(ym, destJson);
    }

    public Integer transactionCancelIn(String strCashMasterId) throws Exception{
        return cashDAO.transactionCancelIn(strCashMasterId);
    }

    public boolean cancelPrepay(Integer cashDetailId) throws Exception{
        return cashDAO.transactionCancelPrepay(cashDetailId);
    }

    public boolean delCashMaster(Integer cashMasterId)throws Exception{
        return cashDAO.delCashMaster(cashMasterId);
    }
}
