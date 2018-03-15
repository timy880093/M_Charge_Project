package com.gate.web.facades;

        import java.util.List;
        import java.util.Map;

        import com.gate.web.beans.QuerySettingVO;

public interface CommissionLogService extends Service {

    public Map getCommissionMasterList(QuerySettingVO querySettingVO)throws Exception;

    public List getDealerCompanyList() throws Exception ;

    public boolean calCommission(String dealerCompany, String inDateS, String inDateE)throws Exception;

    public List getCommissionLogDetailList(String commissionLogId) throws Exception;

    public boolean updateNote(Integer commissionLogId, String note) throws Exception;

    public boolean payCommission(String commissionLog)throws Exception;

    public List<Map> exportCom(Integer[] commissionLog)throws Exception;
    public boolean delCommissionLog(Integer commissionLogId)throws Exception;

    Map getcommissionLogList(QuerySettingVO querySettingVO, Map otherMap) throws Exception;

    List getDownloadcommissionLogList(QuerySettingVO querySettingVO ) throws Exception;
}
