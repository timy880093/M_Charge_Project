package com.gate.web.facades;


import com.gate.web.beans.FirstCompanyPackageBean;
import dao.FirstCompanyPackageDAO;
import java.util.List;

/**
 * Created by emily on 2016/4/7.
 */
public class FirstCompanyPackageServiceImp implements FirstCompanyPackageService {
    FirstCompanyPackageDAO dao = new FirstCompanyPackageDAO();

    //執行批次建立第一次的用戶綁合約的資料
    public String insertFirstCmpPkg(List<FirstCompanyPackageBean> excelDataList) throws Exception{
        return dao.executionFirstCmpPkg(excelDataList);
    }
}
