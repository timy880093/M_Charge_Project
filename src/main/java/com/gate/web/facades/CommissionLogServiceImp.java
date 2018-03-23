package com.gate.web.facades;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gate.utils.ExcelPoiWrapper;
import com.gate.web.beans.CommissionLog;
import com.gateweb.reportModel.CommissionRecord;
import com.gateweb.utils.CommissionLogReportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.QuerySettingVO;

import dao.CommissionLogDAO;

@Service("commissionLogService")
public class CommissionLogServiceImp implements CommissionLogService {
	
	@Autowired
    CommissionLogDAO commissionLogDAO;

    @Autowired
    CommissionLogReportUtils commissionLogReportUtils;

    public Map getCommissionMasterList(QuerySettingVO querySettingVO)throws Exception{
        return commissionLogDAO.getCommissionMasterList(querySettingVO);
    }

    public List getDealerCompanyList() throws Exception {
        return commissionLogDAO.getDealerCompanyList();
    }

    public boolean calCommission(String dealerCompany, String inDateS, String inDateE)throws Exception{
        return commissionLogDAO.transactionCalCommission(dealerCompany, inDateS, inDateE);
    }

    public List getCommissionLogDetailList(String commissionLogId) throws Exception{
        return commissionLogDAO.getCommissionLogDetailList(commissionLogId);
    }

    public boolean updateNote(Integer commissionLogId, String note) throws Exception{
        return commissionLogDAO.updateNote(commissionLogId, note);
    }

    public boolean payCommission(String commissionLog)throws Exception{
        return commissionLogDAO.updatePayCommission(commissionLog);
    }

    public List<Map> exportCom(Integer[] commissionLog)throws Exception{
        return commissionLogDAO.exportCom(commissionLog);
    }

    public boolean delCommissionLog(Integer commissionLogId)throws Exception{
        return commissionLogDAO.delCommissionLog(commissionLogId);
    }

    @Override
    public Map getCommissionLogList(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        return commissionLogDAO.getCommissionMasterList(querySettingVO);
    }

    @Override
    public List getDownloadCommissionLogList(QuerySettingVO querySettingVO) throws Exception {
        return null;
    }

    //取得匯出發票資料的物件
    @Override
    public List<CommissionRecord> genCommissionRecordList(List<Map> dataMapList){
        List<CommissionRecord> commissionRecordList = new ArrayList<>();
        for(Map dataMap : dataMapList){
            CommissionLog master = (CommissionLog) dataMap.get("master");
            List details = (List)dataMap.get("detail");
            for(int i=0; i<details.size(); i++){

                Map detailMap = (Map)details.get(i);
                CommissionRecord  commissionRecord = new CommissionRecord();
                commissionRecord.setDealerCompanyName(master.getDealerCompanyName());
                commissionRecord.setInDateStart(master.getInDateStart());
                commissionRecord.setInDateEnd(master.getInDateEnd());
                commissionRecord.setCommissionType(
                        commissionLogReportUtils.parseCommissionType(master.getCommissionType())
                );
                commissionRecord.setCommissionPercentage(master.getMainPercent().stripTrailingZeros().toPlainString()+"%");
                commissionRecord.setIsPaid(
                        commissionLogReportUtils.parseIsPaid(master.getIsPaid())
                );
                commissionRecord.setCustomName(detailMap.get("name").toString());
                commissionRecord.setIsFirst(
                        commissionLogReportUtils.parseIsFirst(String.valueOf(detailMap.get("is_first")))
                );
                commissionRecord.setBusinessNo(detailMap.get("business_no").toString());
                commissionRecord.setCashType(
                        commissionLogReportUtils.formatCashType(
                                Integer.parseInt(detailMap.get("cash_type").toString())
                        )
                );
                commissionRecord.setPackageName(detailMap.get("package_name").toString());
                commissionRecord.setCalculateYearMonth(detailMap.get("cal_ym").toString());
                commissionRecord.setInDate(detailMap.get("in_date").toString());
                commissionRecord.setTaxInclusivePrice(new BigDecimal(detailMap.get("tax_inclusive_price").toString()));
                commissionRecord.setIsInoutMoneyUnmatch(
                        commissionLogReportUtils.parseIsInoutMoneyUnmatch(
                                String.valueOf(detailMap.get("is_inout_money_unmatch"))
                        )
                );
                commissionRecord.setCommissionAmount(new BigDecimal(detailMap.get("commission_amount").toString()));
                commissionRecordList.add(commissionRecord);
            }
        }
        return commissionRecordList;
    }

    //匯出發票資料Excel
    @Override
    public ExcelPoiWrapper genCommissionLogToExcel(List<Map> excelList, String tempPath) throws Exception {
        ExcelPoiWrapper excel = new ExcelPoiWrapper(tempPath);
        HashMap packageMap = new HashMap();
        excel.setWorkSheet(1);
        int baseRow=2;
        int index = 0;
        for(int j=0; j<excelList.size(); j++){

            Map map = excelList.get(j);

            CommissionLog master = (CommissionLog)map.get("master");
            List details = (List)map.get("detail");

            if(j != 0){
                excel.copyRows(1, 17, 1, baseRow);
                excel.setValue(baseRow, index + 1, "所屬經銷商");
                excel.setValue(baseRow, index + 2, "入帳時間起");
                excel.setValue(baseRow, index + 3, "入帳時間迄");
                excel.setValue(baseRow, index + 4, "佣金類型");
                excel.setValue(baseRow, index + 5, "佣金比例");
                excel.setValue(baseRow, index + 6, "佣金付款狀態");
                excel.setValue(baseRow, index + 7, "用戶名稱");
                excel.setValue(baseRow, index + 8, "是否為首次申請");
                excel.setValue(baseRow, index + 9, "統編");
                excel.setValue(baseRow, index + 10, "繳費類型");
                excel.setValue(baseRow, index + 11, "方案名稱");
                excel.setValue(baseRow, index + 12, "計算年月");
                excel.setValue(baseRow, index + 13, "出帳年月");
                excel.setValue(baseRow, index + 14, "入帳時間");
                excel.setValue(baseRow, index + 15, "入帳金額(含稅)");
                excel.setValue(baseRow, index + 16, "出入帳金額是否相同");
                excel.setValue(baseRow, index + 17, "佣金金額");
                baseRow++;
            }

            for(int i=0; i<details.size(); i++){
                Map detailMap = (Map)details.get(i);

                excel.copyRows(2, 17, 1, baseRow);
                excel.setValue(baseRow, index + 1, master.getDealerCompanyName()); //所屬經銷商
                excel.setValue(baseRow, index + 2, master.getInDateStart()); //入帳時間起
                excel.setValue(baseRow, index + 3, master.getInDateEnd()); //入帳時間迄
                excel.setValue(baseRow, index + 4, master.getStrCommissionType());//佣金類型
                excel.setValue(baseRow, index + 5, master.getStrMainPercent());//佣金比例
                excel.setValue(baseRow, index + 6, master.getStrIsPaid());//佣金付款狀態
                excel.setValue(baseRow, index + 7, detailMap.get("name")); //用戶名稱
                excel.setValue(baseRow, index + 8, detailMap.get("is_first")); //是否為首次申請
                excel.setValue(baseRow, index + 9, detailMap.get("business_no")); //統編
                excel.setValue(
                        baseRow
                        , index + 10
                        , commissionLogReportUtils.formatCashType((Integer)detailMap.get("cash_type"))
                ); //繳費類型
                excel.setValue(baseRow, index + 11, detailMap.get("package_name")); //方案名稱
                excel.setValue(baseRow, index + 12, detailMap.get("cal_ym")); //計算年月
                excel.setValue(baseRow, index + 13, detailMap.get("out_ym")); //出帳年月
                excel.setValue(baseRow, index + 14, detailMap.get("in_date")); //入帳時間
                excel.setValue(baseRow, index + 15, ((BigDecimal)detailMap.get("tax_inclusive_price")).doubleValue()); //入帳金額(含稅)
                excel.setValue(
                        baseRow
                        , index + 16
                        , commissionLogReportUtils.isInoutMoneyUnmatch((String) detailMap.get("is_inout_money_unmatch"))
                ); //cash_master的出入帳金額是否相同
                excel.setValue(baseRow, index + 17, ((BigDecimal)detailMap.get("commission_amount")).doubleValue());//佣金金額
                baseRow++;
            }

            excel.copyRows(2, 17, 1, baseRow);
            excel.setValue(baseRow, index + 15, master.getInAmount()); //入帳總金額(含稅)
            excel.setValue(baseRow, index + 17, master.getCommissionAmount());//佣金總金額
            baseRow++;

        }

        return excel;
    }
}
