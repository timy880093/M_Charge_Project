package com.gate.web.filters;

import com.gate.realms.LoginUser;
import com.gate.utils.TimeUtils;
import com.google.gson.Gson;
import dao.LogDataDAO;
import dao.LogDataEntity;
import dao.LogParameterDAO;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

/**
 * Created by Good688 on 2014/8/26.
 */
public class LogDataFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request = ((HttpServletRequest)req);
        StringBuffer requestURL = request.getRequestURL();
        LogParameterDAO logParameterDAO = new LogParameterDAO();
        Principal userPrincipal = request.getUserPrincipal();
        LoginUser loginUser = new Gson().fromJson(userPrincipal.getName(),LoginUser.class);
        //Map<String,Object> dataMap = (Map<String, Object>)new Gson().fromJson(userPrincipal.getName(),new TypeToken<Map<String, Object>>(){}.getType());
        String userName = loginUser.getName(); //抓Principal 的 name 切割後取user name
        //String reqSession = request.getSession().getAttributeNames();
        Map reqObj = request.getParameterMap();
        HttpSession session = request.getSession();
        Gson gson = new Gson();
        String reqObject = gson.toJson(reqObj).replace("\"","\\\"");
        String sesObject = gson.toJson(getSessionAttrToList(session)).replace("\"","\\\"");

        if (request.getQueryString() != null) {
            requestURL.append("?").append(request.getQueryString());
        }
        String logURL = requestURL.toString(); //url


        try {
            List<Map> parmList = logParameterDAO.getLogParameterList(); //抓log_parameter table 資料
            String[] logPatterns = logURL.split("/") ;
            if(logPatterns.length>3){
                String logPattern = logURL.split("/")[4]; //切網址 抓最後一段的字串 ex  companyEditServlet?method=edit&companyId=16
                for (Map parameterMap : parmList) {
                    String pattern = (String)parameterMap.get("log_pattern"); //取出 log_pattern 和 parameter_id
                    Integer parameterId = (Integer)parameterMap.get("parameter_id");
                    if(logPattern.matches(pattern)){ //如果有matches到 則存入 log_data
                        LogDataDAO logDataDAO = new LogDataDAO();
                        LogDataEntity logDataEntity = new LogDataEntity();
                        logDataEntity.setParameterId(parameterId);
                        logDataEntity.setLogUrl(logURL);
                        logDataEntity.setUserName(userName);
                        logDataEntity.setAccessTime(TimeUtils.getCurrentTimestamp());
                        logDataEntity.setRequestObj(reqObject);
                        logDataEntity.setSessionObj(sesObject);
                        logDataDAO.saveEntity(logDataEntity);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(logURL);
            e.printStackTrace();
        }



        chain.doFilter(req, resp);
    }

    private List getSessionAttrToList(HttpSession session) {
        List list = new ArrayList();

        Enumeration<String> attrNames= session.getAttributeNames();
        List<String> attrNameList= Collections.list(attrNames);

        for (String attrName : attrNameList) {
            HashMap attrMap = new HashMap();
            attrMap.put(attrName,session.getAttribute(attrName));
            list.add(attrMap);
        }
        return list;
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
