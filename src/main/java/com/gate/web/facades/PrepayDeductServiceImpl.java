package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.PrepayDetailVO;
import com.gateweb.charge.model.PrepayDeductMasterEntity;
import com.gateweb.charge.model.PrepayDetailEntity;

import dao.PrepayDeductDAO;

/**
 * Created by emily on 2017/5/19.
 */
@Service("prepayDeductService")
public class PrepayDeductServiceImpl implements PrepayDeductService{
	
	@Autowired
    PrepayDeductDAO prepayDeductDAO;

    public Map getPrepayDeductCompanyList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = prepayDeductDAO.getPrepayDeductCompanyList(querySettingVO);
        return returnMap;
    }

    public Integer transactionCreatePdm(Integer strCompanyId) throws Exception {
        return prepayDeductDAO.transactionCreatePdm(strCompanyId);
    }

    public PrepayDeductMasterEntity getPrepayDeductMaster(Integer companyId) throws Exception{
        return prepayDeductDAO.getPrepayDeductMaster(companyId);
    }

    public List<PrepayDetailVO> getPrepayDetail(Integer companyId) throws Exception{
        return prepayDeductDAO.getPrepayDetail(companyId);
    }

    //變更用戶預用金是否啟用超額狀態(Y/N)
    public void updateMasterStatus(Integer masterId, String status) throws Exception {
    	prepayDeductDAO.updateMasterStatus(masterId, status);
    }

    //新增一筆預繳清單
    public Integer transactionInsertPrepayDetail(PrepayDetailEntity entity, Integer modifierId) throws Exception {
        return prepayDeductDAO.transactionInsertPrepayDetail(entity, modifierId);
    }

    //檢視預繳方案歷史紀錄
    public List<Map> getPrepayDetailHisByCompany(Integer companyId) throws Exception {
        return prepayDeductDAO.getPrepayDetailHisByCompany(companyId);
    }

    //檢視扣抵歷史紀錄
    public List<Map> getDeductDetailHisByCompany(Integer companyId) throws Exception {
        return prepayDeductDAO.getDeductDetailHisByCompany(companyId);
    }
}
