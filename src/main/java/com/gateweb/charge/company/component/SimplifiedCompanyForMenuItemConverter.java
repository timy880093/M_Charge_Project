package com.gateweb.charge.company.component;

import com.gateweb.charge.company.bean.SimplifiedCompanyForMenuItem;
import com.gateweb.orm.charge.entity.Company;

public class SimplifiedCompanyForMenuItemConverter {
    public static SimplifiedCompanyForMenuItem convert(Company company) {
        SimplifiedCompanyForMenuItem simplifiedCompanyForMenuItem = new SimplifiedCompanyForMenuItem();
        simplifiedCompanyForMenuItem.setCompanyId(company.getCompanyId().longValue());
        simplifiedCompanyForMenuItem.setName(company.getName());
        simplifiedCompanyForMenuItem.setBusinessNo(company.getBusinessNo());
        return simplifiedCompanyForMenuItem;
    }
}
