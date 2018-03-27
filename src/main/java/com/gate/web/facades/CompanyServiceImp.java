package com.gate.web.facades;

import java.util.Map;

import com.gateweb.charge.repository.CompanyRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.utils.SendEmailFileUtils;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.CompanyVO;
import com.gate.web.formbeans.CompanyBean;
import com.gateweb.charge.model.CompanyEntity;

import dao.CompanyDAO;

/**
 * Created by simon on 2014/7/11.
 */

@Service("companyService")
public class CompanyServiceImp implements CompanyService{

	private static Logger logger = LogManager.getLogger(CompanyServiceImp.class);
	
	@Autowired
    CompanyDAO companyDAO;

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public Integer insertCompany(CompanyBean companyBean) throws Exception {
        CompanyEntity companyEntity = new CompanyEntity();
        BeanUtils.copyProperties(companyEntity, companyBean);
        companyDAO.saveEntity(companyEntity);
        return null;
    }

    @Override
    public void updateCompany(CompanyBean companyBean) throws Exception {
        CompanyEntity companyEntity = new CompanyEntity();
//        DateConverter dateConverter = new DateConverter();
//        dateConverter.setPattern("yyyy/MM/dd");
        BeanUtils.copyProperties(companyEntity, companyBean);
        companyDAO.updateEntity(companyEntity, companyEntity.getCompanyId());
    }

    @Override
    public void deleteCompany(Integer companyId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CompanyVO findCompanyByCompanyId(Integer companyId) throws Exception {
        CompanyEntity companyEntity = (CompanyEntity)companyDAO.getEntity(CompanyEntity.class,companyId);
        CompanyVO companyVO = new CompanyVO();
        BeanUtils.copyProperties(companyVO, companyEntity);
        Map map = companyDAO.getCreatorAndModifier(companyVO.getCreatorId(),companyVO.getModifierId());
        companyVO.setCreator((String) map.get("creator"));
        companyVO.setModifier((String) map.get("modifier"));
        return companyVO;
    }

    public Boolean checkIfCompanyKeyExist(Integer companyId) throws Exception{
        boolean result = true;
        CompanyEntity companyEntity = (CompanyEntity)companyDAO.getEntity(CompanyEntity.class,companyId);
        CompanyVO companyVO = new CompanyVO();
        BeanUtils.copyProperties(companyVO, companyEntity);
        if(companyVO.getCompanyKey() == null){
            result = false;
        }else{
            return true;
        }
        return result;
    }


    @Override
    public Map getCompanyList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = companyDAO.getCompanyList(querySettingVO);
        return returnMap;
    }

//    public Map continuePackage(String almostOut) throws Exception{
//        return companyDAO.continuePackage(almostOut);
//    }

    public Boolean checkBusinessNo(String businessNo,String companyId) throws Exception {
        return companyDAO.checkBusinessNo(businessNo,companyId);
    }

    public Map getCompanyByBusinessNo(String businessNo) throws Exception {
        return companyDAO.getCompanyByBusinesNo(businessNo);
    }

    public Map getCompanyInfoByCompanyId(Integer companyId) throws Exception {
        return companyDAO.getCompanyInfo(companyId);
    }

}
