package com.gate.web.servlets.backend.common;

import com.gate.web.authority.UserInfo;
import com.gate.web.authority.UserInfoContext;
//import com.gate.web.displaybeans.CompanyVO;
//import com.gate.web.facades.CompanyServiceImp;
//import com.gate.web.facades.InvoiceServiceImp;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/7/10
 * Time: 上午 9:28
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(urlPatterns = "/backendAdmin/loginServlet")
public class LoginServlet extends BackendTemplateServlet {

    @Override
    public void doSomething(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
//        InvoiceServiceImp serviceImp = new InvoiceServiceImp();
        UserInfo userInfo = UserInfoContext.getUserInfo();
//        CompanyServiceImp companyServiceImp = new CompanyServiceImp();
//        CompanyVO companyVO =  companyServiceImp.findCompanyByCompanyId(Integer.valueOf(userInfo.getCompanyId()));
//        List<Map> invoiceInfo= serviceImp.getIndexInvoiceWeek(companyVO.getBusinessNo());
//        Set<Map> invoiceList = new LinkedHashSet<Map>(invoiceInfo);
//        otherMap.put(REQUEST_SEND_OBJECT,invoiceList);


        //2016.10.14 記錄LogOut時的資訊，for debug timeout 時間未到卻被登出
        HttpServletRequest request = (HttpServletRequest)otherMap.get(REQUEST);

        HttpSession session= request.getSession();
        //set default session inactive timeout
        session.setMaxInactiveInterval(userInfo.getLogout_time()*60);
        logger.info("SessionId : "+session.getId());
        logger.info("SessionTimeLeft:" + userInfo.getLogout_time());

        otherMap.put(DISPATCH_PAGE,"/backendAdmin/index.jsp");
    }


}
