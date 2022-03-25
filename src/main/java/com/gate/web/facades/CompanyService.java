package com.gate.web.facades;

import com.gateweb.orm.charge.entity.Company;

import java.util.Set;

/**
 * Created by simon on 2014/7/11.
 */
public interface CompanyService extends Service {

    Set<String> getBillableBusinessNo();

    Set<Company> getBillableCompany();
}
