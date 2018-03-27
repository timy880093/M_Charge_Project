package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import com.gateweb.charge.repository.DealerCompanyRepository;
import com.gateweb.utils.CommissionLogReportUtils;
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

    @Autowired
    CommissionLogReportUtils commissionLogReportUtils;
    @Autowired
    DealerCompanyRepository dealerCompanyRepository;
    public Map getDealerCompanyList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = commissionDAO.getDealerCompanyList(querySettingVO);
        return returnMap;
    }

    public boolean insertDealerCompany(DealerCompanyBean bean , Long userId) throws Exception {
        return commissionDAO.transactionInsertDealerCompany(bean,userId);
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
        return dealerCompanyRepository.findByStatus(1);
    }

}
