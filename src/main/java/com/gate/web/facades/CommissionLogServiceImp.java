package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.QuerySettingVO;

import dao.CommissionLogDAO;

@Service("commissionLogService")
public class CommissionLogServiceImp implements CommissionLogService {
	
	@Autowired
    CommissionLogDAO commissionLogDAO;

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

}
