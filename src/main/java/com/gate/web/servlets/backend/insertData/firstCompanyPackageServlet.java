package com.gate.web.servlets.backend.insertData;

import com.gate.config.SystemConfig;
import com.gate.utils.TimeUtils;
import com.gate.web.facades.CashServiceImp;
import com.gate.web.facades.CompanyServiceImp;
import com.gate.web.servlets.BaseServlet;
import com.gate.web.servlets.backend.common.BackendTemplateServlet;
import dao.CompanyEntity;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by emily on 2016/2/4.
 */
@WebServlet(urlPatterns = "/backendAdmin/firstCompanyPackageServlet")
public class firstCompanyPackageServlet extends BackendTemplateServlet {

    @Override
    public void doSomething(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();
        otherMap.put(REQUEST_SEND_OBJECT, outList);
        otherMap.put(DISPATCH_PAGE, "/backendAdmin/firstCompanyPackage/first_company_package.jsp");

    }
}
