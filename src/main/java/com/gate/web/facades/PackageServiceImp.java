package com.gate.web.facades;

import com.gate.utils.TimeUtils;
import com.gateweb.charge.model.ChargeModeCycleAddEntity;
import com.gateweb.charge.model.PackageModeEntity;
import com.gateweb.charge.repository.ChargeModeCycleAddRepository;
import com.gateweb.charge.repository.PackageModeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
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
    public PackageModeEntity getCurrentPackageModeByYearMonth(Integer companyId, String yearMonth){
        PackageModeEntity currentPackageMode = null;
        List<PackageModeEntity> packageModeEntityList = packageModeRepository.findByCompanyIdIsAndStatusIs(companyId,"1");
        for(PackageModeEntity packageModeEntity: packageModeEntityList){
            //方案的日期記錄在chargeModeCycleAdd當中，也就是說，如果我們要找出當下該公司使用的方案，要根據add當中的real start date 及real end date
            ChargeModeCycleAddEntity chargeModeCycleAddEntity = chargeModeCycleAddRepository.findByAdditionId(packageModeEntity.getAdditionId());
            //取得區間日期為該年一月一號及該年最後一天
            Date calculateStartDate = timeUtils.stringToDate(yearMonth,"yyyyMM");
            Calendar calculateEndDateCalendar = Calendar.getInstance();
            calculateEndDateCalendar.setTime(calculateStartDate);
            calculateEndDateCalendar.add(Calendar.MONTH,1);
            calculateEndDateCalendar.add(Calendar.DATE,-1);
            Date calculateEndDate = calculateEndDateCalendar.getTime();
            Date packageStartDate = chargeModeCycleAddEntity.getRealStartDate();
            Date packageEndDate = chargeModeCycleAddEntity.getRealEndDate();
            //等於起始日或結束日，或是在中間
            if( packageStartDate.equals(calculateStartDate)
                    || packageEndDate.equals(calculateEndDate)
                    || (packageStartDate.after(calculateStartDate) && packageStartDate.before(calculateEndDate))
                    || (packageEndDate.after(calculateStartDate)&& packageEndDate.before(calculateEndDate)) ){
                currentPackageMode = packageModeEntity;
            }
        }
        return currentPackageMode;
    }

    @Override
    public PackageModeEntity getPackageModeByDate(Integer companyId, Date date){
        PackageModeEntity resultPackageMode = null;
        List<PackageModeEntity> packageModeEntityList = packageModeRepository.findByCompanyIdIs(companyId);
        for(PackageModeEntity packageModeEntity : packageModeEntityList){
            ChargeModeCycleAddEntity chargeModeCycleAddEntity = chargeModeCycleAddRepository.findByAdditionId(packageModeEntity.getAdditionId());
            Date packageStartDate = chargeModeCycleAddEntity.getRealStartDate();
            Date packageEndDate = chargeModeCycleAddEntity.getRealEndDate();
            if(date.after(packageStartDate) && date.before(packageEndDate)){
                resultPackageMode = packageModeEntity;
            }
        }
        return resultPackageMode;
    }
}
