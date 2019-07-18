package com.gate.web.facades;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.CompanyVO;
import com.gate.web.formbeans.CompanyBean;
import com.gateweb.charge.model.Company;

import java.util.Map;

/**
 * Created by simon on 2014/7/11.
 */
public interface CompanyService extends Service {
    public Integer insertCompany(CompanyBean companyBean) throws Exception;
    public void updateCompany(CompanyBean companyBean) throws Exception;
    public void deleteCompany(Integer companyId);
    public CompanyVO findCompanyByCompanyId(Integer companyId) throws Exception;
    public Map getCompanyList(QuerySettingVO querySettingVO) throws Exception;
    public Boolean checkBusinessNo(String businessNo, String companyId) throws Exception;
    public Map getCompanyByBusinessNo(String businessNo) throws Exception;
	/**
	 * @param parseInt
	 * @return
	 */
	public Boolean checkIfCompanyKeyExist(Integer parseInt) throws Exception;
	/**
	 * @param parseInt
	 * @return
	 */
	public Map getCompanyInfoByCompanyId(Integer parseInt)throws Exception;
}
