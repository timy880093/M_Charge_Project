package com.gate.web.facades;

import com.gate.web.beans.QuerySettingVO;
import dao.CommissionLogDAO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class CommissionLogServiceImp implements CommissionLogService {
    CommissionLogDAO dao = new CommissionLogDAO();

    public Map getCommissionMasterList(QuerySettingVO querySettingVO)throws Exception{
        return dao.getCommissionMasterList(querySettingVO);
    }

    public List getDealerCompanyList() throws Exception {
        return dao.getDealerCompanyList();
    }

    public boolean calCommission(String dealerCompany, String inDateS, String inDateE)throws Exception{
        return dao.transactionCalCommission(dealerCompany, inDateS, inDateE);
    }

    public List getCommissionLogDetailList(String commissionLogId) throws Exception{
        return dao.getCommissionLogDetailList(commissionLogId);
    }

    public boolean updateNote(Integer commissionLogId, String note) throws Exception{
        return dao.updateNote(commissionLogId, note);
    }

    public boolean payCommission(String commissionLog)throws Exception{
        return dao.updatePayCommission(commissionLog);
    }

    public List<Map> exportCom(String commissionLog)throws Exception{
        return dao.exportCom(commissionLog);
            }
    public boolean delCommissionLog(Integer commissionLogId)throws Exception{
        return dao.delCommissionLog(commissionLogId);
    }

}
