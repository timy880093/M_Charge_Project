package com.gate.web.facades;

import com.gateweb.charge.model.PackageModeEntity;

import java.util.Date;

/**
 * Created by Eason on 4/10/2018.
 */
public interface PackageService {

    PackageModeEntity getCurrentPackageModeByYearMonth(Integer companyId, String yearMonth);

    PackageModeEntity getPackageModeByDate(Integer companyId, Date date);
}
