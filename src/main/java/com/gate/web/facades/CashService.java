package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import com.gate.utils.ExcelPoiWrapper;
import com.gate.web.beans.CashMasterBean;
import com.gate.web.beans.InvoiceExcelBean;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.CashDetailVO;
import com.gate.web.displaybeans.CashMasterVO;
import com.gateweb.charge.model.BillCycleEntity;
import com.gateweb.charge.vo.CashVO;
import com.gateweb.reportModel.InvoiceBatchRecord;
import com.gateweb.reportModel.OrderCsv;

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

    //使用CashVO的資料建立訂單。
    List<OrderCsv> genOrderCsvListByCashVO(Long sellerCompanyId, CashVO cashVO);

    public List getInvoiceItem(String ym) throws Exception;

    public List getInvoiceItem(String ym, String destJson) throws Exception;

    public Integer transactionCancelIn(String strCashMasterId) throws Exception;

    public boolean cancelPrepay(Integer cashDetailId) throws Exception;

    public boolean delCashMaster(Integer cashMasterId)throws Exception;

    //多筆-寄帳單明細表
    Integer transactionSendBillMail1(String masterIdAry) throws Exception;

    //輸入自行要重寄的Email(帳單明細表)
    Integer reSendBillEmail(String strCashMasterId, String strReSendBillMail) throws Exception;

    List<CashVO> findCashVoByOutYm(String yearMonth);

    Map<String,Object> genCashDataExcelDataMap(List<CashMasterBean> cashMasterBeanList);

    ExcelPoiWrapper genCashDataToExcel(List<CashMasterBean> cashMasterList, String tempPath) throws Exception;

    //匯出發票資料Excel
    ExcelPoiWrapper genInvoiceItemToExcel(List<InvoiceExcelBean> InvoiceExcelList, String tempPath) throws Exception;

    List<InvoiceBatchRecord> genInvoiceBatchRecordList(List<InvoiceExcelBean> invoiceExcelBeanList);
}
