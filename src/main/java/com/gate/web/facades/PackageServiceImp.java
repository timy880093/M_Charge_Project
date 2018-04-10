package com.gate.web.facades;

import com.gate.utils.TimeUtils;
import com.gateweb.charge.model.ChargeModeCycleAddEntity;
import com.gateweb.charge.model.PackageModeEntity;
import com.gateweb.charge.repository.ChargeModeCycleAddRepository;
import com.gateweb.charge.repository.PackageModeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by Eason on 4/10/2018.
 */
@org.springframework.stereotype.Service
public class PackageServiceImp implements PackageService{
    @Autowired
    PackageModeRepository packageModeRepository;

    @Autowired
    ChargeModeCycleAddRepository chargeModeCycleAddRepository;

    @Autowired
    TimeUtils timeUtils;

    /**
     * 取得該間公司當前的package
     * @return
     */
    @Override
    public PackageModeEntity getCurrentPackageMode(Integer companyId, String yearMonth){
        PackageModeEntity currentPackageMode = null;
        List<PackageModeEntity> packageModeEntityList = packageModeRepository.findByCompanyIdIsAndStatusIs(companyId,"1");
        for(PackageModeEntity packageModeEntity: packageModeEntityList){
            //方案的日期記錄在chargeModeCycleAdd當中，也就是說，如果我們要找出當下該公司使用的方案，要根據add當中的real start date 及real end date
            ChargeModeCycleAddEntity chargeModeCycleAddEntity = chargeModeCycleAddRepository.findByAdditionId(packageModeEntity.getAdditionId());
            Date calculateDate = timeUtils.stringToDate(yearMonth,"yyyyMM");
            Date packageStartDate = chargeModeCycleAddEntity.getRealStartDate();
            Date packageEndDate = chargeModeCycleAddEntity.getRealEndDate();
            if(calculateDate.after(packageStartDate)&& calculateDate.before(packageEndDate)){
                currentPackageMode = packageModeEntity;
            }
        }
        return currentPackageMode;
    }
}
