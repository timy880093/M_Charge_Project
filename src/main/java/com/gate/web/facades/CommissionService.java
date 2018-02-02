package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.DealerCompanyVO;
import com.gate.web.displaybeans.DealerVO;
import com.gate.web.formbeans.DealerCompanyBean;
import com.gateweb.charge.model.DealerCompanyEntity;

public interface CommissionService extends Service {

	public Map getDealerCompanyList(QuerySettingVO querySettingVO) throws Exception ;

    public void insertDealerCompany(DealerCompanyBean bean) throws Exception ;

    public DealerCompanyVO getDealerCompanyByDealerCompanyId(Integer dealerCompanyId) throws Exception ;

    public List<DealerVO> getDealerByDealerCompanyId(Integer dealerCompanyId) throws Exception;

    public void updateCommissionStatus(Integer packageId,Integer status) throws Exception ;

    public List<DealerCompanyEntity> getDealerCompanyListForDropBox() throws Exception ;

}
