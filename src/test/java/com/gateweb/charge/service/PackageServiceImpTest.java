package com.gateweb.charge.service;

import com.gate.web.facades.PackageService;
import com.gateweb.charge.model.BillCycleEntity;
import com.gateweb.charge.model.CompanyEntity;
import com.gateweb.charge.model.PackageModeEntity;
import com.gateweb.charge.repository.BillCycleRepository;
import com.gateweb.charge.repository.CompanyRepository;
import com.gateweb.charge.repository.PackageModeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eason on 4/12/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class PackageServiceImpTest {
    @Autowired
    PackageModeRepository packageModeRepository;

    @Autowired
    BillCycleRepository billCycleRepository;

    @Autowired
    PackageService packageService;

    @Autowired
    CompanyRepository companyRepository;

    public void packageServiceImplTest(String yearMonth){
        List<BillCycleEntity> billCycleEntityList = new ArrayList<>();
        billCycleEntityList.addAll(billCycleRepository.findByYearMonthIs(yearMonth));
        List<PackageModeEntity> resultPackageModeList= new ArrayList<>();
        for(BillCycleEntity billCycleEntity: billCycleEntityList){
            if(billCycleEntity.getCnt()==null && billCycleEntity.getStatus().equals("1")){
                List<PackageModeEntity> packageModeEntityList
                        = packageModeRepository.findByCompanyIdIsAndStatusIs(billCycleEntity.getCompanyId(),"0");
                resultPackageModeList.addAll(packageModeEntityList);
            }
        }

        for(PackageModeEntity packageModeEntity : resultPackageModeList){
            CompanyEntity companyEntity = companyRepository.findByCompanyId(packageModeEntity.getCompanyId());
            System.out.println(packageModeEntity.getCompanyId()+":"+companyEntity.getName());
        }
    }

    @Test
    public void packageServiceImplTest201801And02(){
        packageServiceImplTest("201801");
        packageServiceImplTest("201802");
    }



}
