package com.gate.web.facades;

import com.gateweb.charge.company.bean.SimplifiedCompanyForMenuItem;
import com.gateweb.charge.company.component.SimplifiedCompanyForMenuItemConverter;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Set<Company> getContractBasedCompanySet() {
        Set<Company> resultSet = new HashSet<>();
        try {
            resultSet = new HashSet<>(companyRepository.findBillableCompanyByContract());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return resultSet;
    }

    @Override
    public Set<SimplifiedCompanyForMenuItem> getContractBasedSimplifiedCompanyList() {
        Set<SimplifiedCompanyForMenuItem> resultSet = new HashSet<>();
        try {
            companyRepository.findBillableCompanyByContract().stream().forEach(
                    company -> {
                        resultSet.add(SimplifiedCompanyForMenuItemConverter.convert(company));
                    }
            );
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return resultSet;
    }

    @Override
    public Set<SimplifiedCompanyForMenuItem> getSimplifiedCompanyList() {
        Set<SimplifiedCompanyForMenuItem> resultSet = new HashSet<>();
        try {
            companyRepository.findAll().stream().forEach(
                    company -> {
                        resultSet.add(SimplifiedCompanyForMenuItemConverter.convert(company));
                    }
            );
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return resultSet.stream()
                .sorted(Comparator.comparing(SimplifiedCompanyForMenuItem::getBusinessNo))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
