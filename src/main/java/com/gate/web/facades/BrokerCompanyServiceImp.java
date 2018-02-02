package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.QuerySettingVO;

import dao.BrokerCompanyDAO;

/**
 * Created by emily on 2016/2/4.
 */
@Service("brokerCompanyService")
public class BrokerCompanyServiceImp implements BrokerCompanyService {
	
	@Autowired
    BrokerCompanyDAO brokerCompanyDAO;


    public Map getBrokerCompanyList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = brokerCompanyDAO.getBrokerCompanyList(querySettingVO);
        return returnMap;
    }

    public List getExcelBrokerCompanyList(String brokerType, String brokerCompany) throws Exception{
        return brokerCompanyDAO.getExcelBrokerCompanyList(brokerType, brokerCompany);
    }

    //介紹人公司
    public List getBrokerCp2List() throws Exception {
        return brokerCompanyDAO.getBrokerCp2List();
    }

    //裝機公司
    public List getBrokerCp3List() throws Exception {
        return brokerCompanyDAO.getBrokerCp3List();
    }
}
