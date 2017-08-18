package com.gate.web.facades;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.WarrantyVO;
import com.gate.web.formbeans.WarrantyBean;
import dao.WarrantyDAO;
import dao.WarrantyEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;
import java.util.Map;

public class WarrantyServiceImp {
    WarrantyDAO dao= new WarrantyDAO();

    public Map getWarrantyList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = dao.getWarrantyList(querySettingVO);
        return returnMap;
    }

    public int updateWarranty(WarrantyBean warrantyBean) throws Exception {
        int returnInt = dao.updateWarranty(warrantyBean);
        return returnInt;
    }

    public WarrantyVO findWarrantyByWarrantyId(Integer warrantyId) throws Exception {
        WarrantyEntity warrantyEntity = (WarrantyEntity)dao.getEntity(WarrantyEntity.class,warrantyId);
        WarrantyVO warrantyVO = new WarrantyVO();
        BeanUtils.copyProperties(warrantyVO, warrantyEntity);
        Map map = dao.getCreatorAndModifier(warrantyVO.getCreatorId(),warrantyVO.getModifierId());
        warrantyVO.setCreator((String) map.get("creator"));
        warrantyVO.setModifier((String) map.get("modifier"));
        return warrantyVO;
    }

    public List getUserDealerCompanyList() throws Exception{
        return dao.getUserDealerCompanyList();
    }
}
