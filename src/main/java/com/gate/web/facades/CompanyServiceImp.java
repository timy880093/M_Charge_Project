package com.gate.web.facades;

import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by simon on 2014/7/11.
 */

@Service("companyService")
public class CompanyServiceImp implements CompanyService {

    private static Logger logger = LoggerFactory.getLogger(CompanyServiceImp.class);

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public Set<String> getBillableBusinessNo() {
        Set<String> result = new HashSet<>();
        try {
            Set<String> contractBillableBusinessNo = companyRepository.findBillableBusinessNoByContract();
            result.addAll(contractBillableBusinessNo);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        try {
            Set<String> billCycleBillableBusinessNo = companyRepository.findBillableBusinessNoByBillCycle();
            result.addAll(billCycleBillableBusinessNo);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    @Override
    public Set<Company> getBillableCompany() {
        Set<Company> resultSet = new HashSet<>();
        try {
            Set<Company> contractBillableCompany = new HashSet<>(companyRepository.findBillableCompanyByContract());
            resultSet.addAll(contractBillableCompany);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return resultSet;
    }
}
