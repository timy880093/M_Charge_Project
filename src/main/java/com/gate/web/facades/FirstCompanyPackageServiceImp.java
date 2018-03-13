package com.gate.web.facades;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.FirstCompanyPackageBean;

import dao.FirstCompanyPackageDAO;

/**
 * Created by emily on 2016/4/7.
 */
@Service("firstCompanyPackageService")
public class FirstCompanyPackageServiceImp implements FirstCompanyPackageService {
	
	@Autowired
    FirstCompanyPackageDAO firstCompanyPackageDAO;

    //執行批次建立第一次的用戶綁合約的資料
    public String insertFirstCmpPkg(List<FirstCompanyPackageBean> excelDataList, Integer modifierId) throws Exception{
        return firstCompanyPackageDAO.executionFirstCmpPkg(excelDataList, modifierId);
    }
}
