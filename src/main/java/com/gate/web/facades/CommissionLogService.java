package com.gate.web.facades;

        import java.util.List;
        import java.util.Map;

        import com.gate.utils.ExcelPoiWrapper;
        import com.gate.web.beans.QuerySettingVO;
        import com.gateweb.reportModel.CommissionRecord;

public interface CommissionLogService extends Service {

    public Map getCommissionMasterList(QuerySettingVO querySettingVO)throws Exception;

    public List getDealerCompanyList() throws Exception ;

    public boolean calCommission(String dealerCompany, String inDateS, String inDateE)throws Exception;

    public List getCommissionLogDetailList(String commissionLogId) throws Exception;

    public boolean updateNote(Integer commissionLogId, String note) throws Exception;

    public boolean payCommission(String commissionLog)throws Exception;

    public List<Map> exportCom(Integer[] commissionLog)throws Exception;

    public boolean delCommissionLog(Integer commissionLogId)throws Exception;

    Map getCommissionLogList(QuerySettingVO querySettingVO, Map otherMap) throws Exception;

    List getDownloadCommissionLogList(QuerySettingVO querySettingVO) throws Exception;

    //取得匯出發票資料的物件
    List<CommissionRecord> genCommissionRecordList(List<Map> dataMapList);

    //匯出發票資料Excel
    ExcelPoiWrapper genCommissionLogToExcel(List<Map> excelList, String tempPath) throws Exception;
}
