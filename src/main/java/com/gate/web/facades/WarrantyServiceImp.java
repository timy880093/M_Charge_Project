package com.gate.web.facades;

import java.util.List;
import java.util.Map;

import com.gate.utils.TimeUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
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
    private TimeUtils timeUtils;


    public Map getWarrantyList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = warrantyDAO.getWarrantyList(querySettingVO);
        return returnMap;
    }
    @Override
    public Integer updateWarranty(WarrantyBean warrantyBean, Integer userId) throws Exception {
        WarrantyEntity entity = new WarrantyEntity();
        BeanUtils.copyProperties(entity, warrantyBean);

        if (null == entity.getCompanyId()) {
            entity.setCompanyId(0);
        }
        if (null == entity.getStartDate()) {
            entity.setStartDate(timeUtils.parseDateYYYYMMDD("1000101"));
        }
        if (null == entity.getEndDate()) {
            entity.setEndDate(timeUtils.parseDateYYYYMMDD("1000101"));
        }
        if (null == entity.getOnlyShip()) {
            entity.setOnlyShip(2);
        }
        if (null == entity.getDealerCompanyId()) {
            entity.setDealerCompanyId(0);
        }

//        saveOrUpdateEntity(entity, entity.getWarrantyId());
        if (StringUtils.isEmpty(warrantyBean.getWarrantyId())) {
            //新增
            warrantyDAO.saveEntity(entity);
        } else {
            //更新
            warrantyDAO.updateEntity(entity, entity.getWarrantyId());
        }
        return 1;
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
