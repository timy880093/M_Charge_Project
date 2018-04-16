package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import com.gate.web.displaybeans.ChargeModeCycleVO;
import com.gate.web.displaybeans.ChargeModeGradeVO;
import com.gate.web.formbeans.CompanyChargeCycleBean;

public interface CompanyChargeService extends Service {

	public ChargeModeCycleVO findChargeModeCycleByChargeId(Integer chargeId) throws Exception ;

    public ChargeModeGradeVO findChargeModeGradeByChargeId(Integer chargeId) throws Exception ;
	public List getDealerList(Integer dealerComplayId) throws Exception ;

	public List getChargeMonthList() throws Exception ;
	public List getChargeGradeList() throws Exception ;

	void transactionInsertCompanyChargeCycle(CompanyChargeCycleBean bean, Integer modifierId) throws Exception;

	public List<Map> getChargeCycleHisByCompany(Integer companyId) throws Exception ;

	public Map getSettleInfo(Integer companyId,Integer packageId) throws Exception;

	public Map getCycleTryCalSettle(Integer packageId, String endDate) throws Exception;

	public Map doSettle(Integer packageId, String endDate, String realEndDate) throws Exception;

	Map transactionContinuePackage(String almostOut, Integer modifierId) throws Exception;

	public Map getCyclePackageInfoByPackageId(Integer packageId) throws Exception ;

	public Map getGradePackageInfoByPackageId(Integer packageId) throws Exception ;
}
