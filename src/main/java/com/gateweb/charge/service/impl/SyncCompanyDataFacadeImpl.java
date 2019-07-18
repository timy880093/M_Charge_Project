package com.gateweb.charge.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateweb.charge.model.Company;
import com.gateweb.charge.repository.CompanyRepository;
import com.gateweb.charge.service.SyncCompanyDataFacade;
import com.gateweb.einv.repository.EinvCompanyRepository;

/**
 * Created by Eason on 3/9/2018.
 */
@Service("syncCompanyDataFacade")
public class SyncCompanyDataFacadeImpl implements SyncCompanyDataFacade {
    protected static final Logger logger = LogManager.getLogger(InvoiceAmountSummaryReportFacadeImpl.class);

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    EinvCompanyRepository einvCompanyRepository;

    /**
     * 用Spring的BeanUtils在Copy時，ID不會一併COPY，但apache的library沒有這個問題。
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Override
    public void syncCompanyDataFromEinvDatabase() throws InvocationTargetException, IllegalAccessException {
        List<com.gateweb.einv.model.Company> einvCompanyEntityList = einvCompanyRepository.findAll();

        for(com.gateweb.einv.model.Company company : einvCompanyEntityList){
            com.gateweb.charge.model.Company existsCompanyEntity = companyRepository.findByCompanyId(company.getCompanyId().intValue());
            if(existsCompanyEntity!=null){
                transactionUpdateCompanyDataFromEinvDatabase(existsCompanyEntity,company);
            }else{
                transactionInsertCompanyDataFromEinvDatabase(company);
            }
        }
    }

    public void transactionUpdateCompanyDataFromEinvDatabase(com.gateweb.charge.model.Company existsCompanyEntity, com.gateweb.einv.model.Company einvCompanyEntity) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(existsCompanyEntity,einvCompanyEntity);
        logger.info("Update Company :" + existsCompanyEntity.getCompanyId());
        companyRepository.save(existsCompanyEntity);
    }

    public void transactionInsertCompanyDataFromEinvDatabase(com.gateweb.einv.model.Company einvCompanyEntity) throws InvocationTargetException, IllegalAccessException {
        com.gateweb.charge.model.Company newCompanyEntity = new com.gateweb.charge.model.Company();
        BeanUtils.copyProperties(newCompanyEntity,einvCompanyEntity);
        logger.info("Insert Company :" + einvCompanyEntity.getCompanyId());
        companyRepository.save(newCompanyEntity);
    }
}
