package com.gateweb.charge.service.impl;

import com.gateweb.charge.repository.CompanyRepository;
import com.gateweb.charge.service.SyncCompanyDataFacade;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Eason on 3/9/2018.
 */
public class SyncCompanyDataImpl implements SyncCompanyDataFacade {
    protected static final Logger logger = Logger.getLogger(InvoiceAmountSummaryReportFacadeImpl.class);

    @Autowired
    CompanyRepository companyRepository;

}
