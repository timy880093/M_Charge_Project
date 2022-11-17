package com.gate.web.facades;

import com.gateweb.charge.company.bean.SimplifiedCompanyForMenuItem;
import com.gateweb.orm.charge.entity.Company;

import java.util.Set;

/**
 * Created by simon on 2014/7/11.
 */
public interface CompanyService extends Service {

    Set<String> getBillableBusinessNo();

    Set<Company> getContractBasedCompanySet();

    Set<SimplifiedCompanyForMenuItem> getContractBasedSimplifiedCompanyList();

    Set<SimplifiedCompanyForMenuItem> getSimplifiedCompanyList();
}
