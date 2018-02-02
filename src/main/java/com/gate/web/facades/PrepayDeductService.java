package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.PrepayDetailVO;
import com.gateweb.charge.model.PrepayDeductMasterEntity;
import com.gateweb.charge.model.PrepayDetailEntity;

/**
 * Created by emily on 2017/5/19.
 */
public interface PrepayDeductService extends Service{

    public Map getPrepayDeductCompanyList(QuerySettingVO querySettingVO) throws Exception;

    public Integer transactionCreatePdm(String calYM) throws Exception;

    public PrepayDeductMasterEntity getPrepayDeductMaster(Integer companyId) throws Exception;

    public List<PrepayDetailVO> getPrepayDetail(Integer companyId) throws Exception;
    //變更用戶預用金是否啟用超額狀態(Y/N)
    public void updateMasterStatus(Integer masterId, String status) throws Exception;

    //新增一筆預繳清單
    public Integer transactionInsertPrepayDetail(PrepayDetailEntity entity) throws Exception;
    //檢視預繳方案歷史紀錄
    public List<Map> getPrepayDetailHisByCompany(Integer companyId) throws Exception;
    //檢視扣抵歷史紀錄
    public List<Map> getDeductDetailHisByCompany(Integer companyId) throws Exception;
}
