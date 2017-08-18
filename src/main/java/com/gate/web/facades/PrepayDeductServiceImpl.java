package com.gate.web.facades;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.PrepayDeductMasterVO;
import com.gate.web.displaybeans.PrepayDetailVO;
import dao.PrepayDeductDAO;
import dao.PrepayDeductMasterEntity;
import dao.PrepayDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by emily on 2017/5/19.
 */
public class PrepayDeductServiceImpl {
    PrepayDeductDAO dao = new PrepayDeductDAO();

    public Map getPrepayDeductCompanyList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = dao.getPrepayDeductCompanyList(querySettingVO);
        return returnMap;
    }

    public Integer transactionCreatePdm(String calYM) throws Exception {
        return dao.transactionCreatePdm(calYM);
    }

    public PrepayDeductMasterEntity getPrepayDeductMaster(Integer companyId) throws Exception{
        return dao.getPrepayDeductMaster(companyId);
    }

    public List<PrepayDetailVO> getPrepayDetail(Integer companyId) throws Exception{
        return dao.getPrepayDetail(companyId);
    }

    //變更用戶預用金是否啟用超額狀態(Y/N)
    public void updateMasterStatus(Integer masterId, String status) throws Exception {
        dao.updateMasterStatus(masterId, status);
    }

    //新增一筆預繳清單
    public Integer transactionInsertPrepayDetail(PrepayDetailEntity entity) throws Exception {
        return dao.transactionInsertPrepayDetail(entity);
    }

    //檢視預繳方案歷史紀錄
    public List<Map> getPrepayDetailHisByCompany(Integer companyId) throws Exception {
        return dao.getPrepayDetailHisByCompany(companyId);
    }

    //檢視扣抵歷史紀錄
    public List<Map> getDeductDetailHisByCompany(Integer companyId) throws Exception {
        return dao.getDeductDetailHisByCompany(companyId);
    }
}
