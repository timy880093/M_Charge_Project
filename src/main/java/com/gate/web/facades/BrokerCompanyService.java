package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import com.gate.web.beans.QuerySettingVO;

/**
 * Created by emily on 2016/2/4.
 */
public interface BrokerCompanyService extends Service {

	/**
	 * @return
	 */
	List getBrokerCp2List() throws Exception;

	/**
	 * @param brokerType
	 * @param brokerCompany
	 * @return
	 */
	List<Map> getExcelBrokerCompanyList(String brokerType, String brokerCompany) throws Exception;

	/**
	 * @return
	 */
	List getBrokerCp3List() throws Exception;

	/**
	 * @param querySettingVO
	 * @return
	 */
	Map getBrokerCompanyList(QuerySettingVO querySettingVO) throws Exception;
}
