package com.gate.web.facades;


import com.gate.web.beans.FirstCompanyPackageBean;

import java.util.List;

/**
 * Created by emily on 2016/4/7.
 */
public interface FirstCompanyPackageService extends Service{
    public String insertFirstCmpPkg(List<FirstCompanyPackageBean> excelDataList) throws Exception;
}
