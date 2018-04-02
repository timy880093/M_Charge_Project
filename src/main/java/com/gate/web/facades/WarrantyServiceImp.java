package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.WarrantyVO;
import com.gate.web.formbeans.WarrantyBean;
import com.gateweb.charge.model.WarrantyEntity;

import dao.WarrantyDAO;

@Service("warrantyService")
public class WarrantyServiceImp implements WarrantyService{
    
	@Autowired
	WarrantyDAO warrantyDAO;

    public Map getWarrantyList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = warrantyDAO.getWarrantyList(querySettingVO);
        return returnMap;
    }

    public int updateWarranty(WarrantyBean warrantyBean) throws Exception {

        int returnInt = warrantyDAO.updateWarranty(warrantyBean);
        return returnInt;
    }

    public WarrantyVO findWarrantyByWarrantyId(Integer warrantyId) throws Exception {
        WarrantyEntity warrantyEntity = (WarrantyEntity)warrantyDAO.getEntity(WarrantyEntity.class,warrantyId);
        WarrantyVO warrantyVO = new WarrantyVO();
        BeanUtils.copyProperties(warrantyVO, warrantyEntity);
        Map map = warrantyDAO.getCreatorAndModifier(warrantyVO.getCreatorId(),warrantyVO.getModifierId());
        warrantyVO.setCreator((String) map.get("creator"));
        warrantyVO.setModifier((String) map.get("modifier"));
        return warrantyVO;
    }



    public List getUserDealerCompanyList() throws Exception{
        return warrantyDAO.getUserDealerCompanyList();
    }
}
