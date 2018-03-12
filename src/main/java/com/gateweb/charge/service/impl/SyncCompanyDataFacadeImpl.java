package com.gateweb.charge.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateweb.charge.model.CompanyEntity;
import com.gateweb.charge.repository.CompanyRepository;
import com.gateweb.charge.service.SyncCompanyDataFacade;
import com.gateweb.einv.model.Company;
import com.gateweb.einv.repository.EinvCompanyRepository;

/**
 * Created by Eason on 3/9/2018.
 */
@Service
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
    public void transactionSyncCompanyDataFromEinvDatabase() throws InvocationTargetException, IllegalAccessException {
        List<Company> einvCompanyEntityList = einvCompanyRepository.findAll();

        for(Company company : einvCompanyEntityList){
            CompanyEntity existsCompanyEntity = companyRepository.findByCompanyId(company.getCompanyId().intValue());
            if(existsCompanyEntity!=null){
                BeanUtils.copyProperties(existsCompanyEntity,company);
                logger.info("Update Company :" + existsCompanyEntity.getCompanyId());
            }else{
                existsCompanyEntity = new CompanyEntity();
                BeanUtils.copyProperties(existsCompanyEntity,company);
                logger.info("Insert Company :" + company.getCompanyId());
            }
            companyRepository.save(existsCompanyEntity);
        }
    }
}
