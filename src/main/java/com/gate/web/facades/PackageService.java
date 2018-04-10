package com.gate.web.facades;

import com.gateweb.charge.model.PackageModeEntity;

/**
 * Created by Eason on 4/10/2018.
 */
public interface PackageService {
    PackageModeEntity getCurrentPackageMode(Integer companyId, String yearMonth);
}
