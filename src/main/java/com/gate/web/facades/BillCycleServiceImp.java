package com.gate.web.facades;

import com.gateweb.charge.model.BillCycleEntity;
import com.gateweb.charge.model.CompanyEntity;
import com.gateweb.charge.repository.BillCycleRepository;
import com.gateweb.charge.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eason on 3/29/2018.
 */
@org.springframework.stereotype.Service
public class BillCycleServiceImp implements BillCycleService{

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    BillCycleRepository billCycleRepository;

    @Override
    public List<BillCycleEntity> getAllCompaniesBillCycle(String yearMonth){
        List<BillCycleEntity> billCycleEntityList = new ArrayList<>();
        //查出所有公司
        List<CompanyEntity> companyEntityList = companyRepository.findAll();
        for(CompanyEntity companyEntity: companyEntityList){
            billCycleEntityList.addAll(billCycleRepository.findByYearMonthIsAndCompanyIdIs(yearMonth,companyEntity.getCompanyId()));
        }
        return billCycleEntityList;
    }
}
