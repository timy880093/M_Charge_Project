package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.DealerCompanyVO;
import com.gate.web.displaybeans.DealerVO;
import com.gate.web.formbeans.DealerCompanyBean;
import com.gateweb.charge.model.DealerCompanyEntity;

import dao.CommissionDAO;


@Service("commissionService")
public class CommissionServiceImp implements CommissionService{
	
	@Autowired
    CommissionDAO commissionDAO;

    public Map getDealerCompanyList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = commissionDAO.getDealerCompanyList(querySettingVO);
        return returnMap;
    }

    public void insertDealerCompany(DealerCompanyBean bean) throws Exception {
        commissionDAO.transactionInsertDealerCompany(bean);
    }

    public DealerCompanyVO getDealerCompanyByDealerCompanyId(Integer dealerCompanyId) throws Exception {
        return commissionDAO.getDealerCompanyByDealerCompanyId(dealerCompanyId);
    }

    public List<DealerVO> getDealerByDealerCompanyId(Integer dealerCompanyId) throws Exception{
        return commissionDAO.getDealerByDealerCompanyId(dealerCompanyId);
    }

    public void updateCommissionStatus(Integer packageId,Integer status) throws Exception {
        commissionDAO.updateCommissionStatus(packageId, status);
    }

    public List<DealerCompanyEntity> getDealerCompanyListForDropBox() throws Exception {
        return commissionDAO.getDealerCompanyListForDropBox();
    }

}
