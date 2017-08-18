package com.gate.web.facades;

import com.gate.web.beans.QuerySettingVO;
import dao.BrokerCompanyDAO;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by emily on 2016/2/4.
 */
public class BrokerCompanyServiceImp implements BrokerCompanyService {
    BrokerCompanyDAO dao = new BrokerCompanyDAO();


    public Map getBrokerCompanyList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = dao.getBrokerCompanyList(querySettingVO);
        return returnMap;
    }

    public List getExcelBrokerCompanyList(String brokerType, String brokerCompany) throws Exception{
        return dao.getExcelBrokerCompanyList(brokerType, brokerCompany);
    }

    //介紹人公司
    public List getBrokerCp2List() throws Exception {
        return dao.getBrokerCp2List();
    }

    //裝機公司
    public List getBrokerCp3List() throws Exception {
        return dao.getBrokerCp3List();
    }
}
