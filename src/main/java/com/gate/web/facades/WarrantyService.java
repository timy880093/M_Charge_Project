package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.WarrantyVO;
import com.gate.web.formbeans.WarrantyBean;

public interface WarrantyService extends Service {

    public Map getWarrantyList(QuerySettingVO querySettingVO) throws Exception;

    public Integer updateWarranty(WarrantyBean warrantyBean, Integer userId) throws Exception;


    public WarrantyVO findWarrantyByWarrantyId(Integer warrantyId) throws Exception;

//    public List<Map> exportWar(String warranty)throws Exception{
//        return dao.exportWar(warranty);
//    }


    public List getUserDealerCompanyList() throws Exception;

}
