package com.gate.web.servlets.backend.insertData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.gate.web.servlets.backend.common.BackendTemplateServlet;

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
