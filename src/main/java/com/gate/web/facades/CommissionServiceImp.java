package com.gate.web.facades;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.DealerCompanyVO;
import com.gate.web.formbeans.DealerCompanyBean;
import dao.*;
import com.gate.web.displaybeans.DealerVO;

import java.util.List;
import java.util.Map;

public class CommissionServiceImp implements CommissionService{
    CommissionDAO dao = new CommissionDAO();


    public Map getDealerCompanyList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = dao.getDealerCompanyList(querySettingVO);
        return returnMap;
    }

    public void insertDealerCompany(DealerCompanyBean bean) throws Exception {
        dao.transactionInsertDealerCompany(bean);
    }

    public DealerCompanyVO getDealerCompanyByDealerCompanyId(Integer dealerCompanyId) throws Exception {
        return dao.getDealerCompanyByDealerCompanyId(dealerCompanyId);
    }

    public List<DealerVO> getDealerByDealerCompanyId(Integer dealerCompanyId) throws Exception{
        return dao.getDealerByDealerCompanyId(dealerCompanyId);
    }

    public void updateCommissionStatus(Integer packageId,Integer status) throws Exception {
        dao.updateCommissionStatus(packageId, status);
    }

    public List<DealerCompanyEntity> getDealerCompanyListForDropBox() throws Exception {
        return dao.getDealerCompanyListForDropBox();
    }

}
