package com.gate.web.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.gate.utils.TimeUtils;
import com.gateweb.charge.model.LogDataEntity;
import com.gateweb.charge.model.UserEntity;
import com.google.gson.Gson;

import dao.LogDataDAO;
import dao.LogParameterDAO;

/**
 * Created by Good688 on 2014/8/26.
 */
public class LogDataFilter implements Filter {
    @Autowired
    TimeUtils timeUtils;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request = ((HttpServletRequest)req);
        StringBuffer requestURL = request.getRequestURL();
        LogParameterDAO logParameterDAO = new LogParameterDAO();
        UserEntity user = null;
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (principal instanceof UserEntity) {
    		System.out.println("Admin 1:"+((UserEntity)principal).getUsername());
    		user = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		System.out.println("Admin 2:"+user);
    		userName = user.getUsername();
		} else if (principal instanceof UserDetails) {
	    		System.out.println("Admin 2:"+((UserDetails)principal).getUsername());
	    		UserDetails detail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    		System.out.println("Admin 2:"+detail);
	    		userName = detail.getUsername();
		} else {
			System.out.println("Admin 3:"+principal.toString());

		}
        
         //抓Principal 的 name 切割後取user name
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
                        logDataEntity.setAccessTime(timeUtils.getCurrentTimestamp());
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
