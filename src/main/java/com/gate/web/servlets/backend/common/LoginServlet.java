package com.gate.web.servlets.backend.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gate.core.bean.BaseFormBean;
import com.gate.web.authority.UserInfo;
import com.gate.web.authority.UserInfoContext;
import com.gate.web.servlets.MvcBaseServlet;
//import com.gate.web.displaybeans.CompanyVO;
//import com.gate.web.facades.CompanyServiceImp;
//import com.gate.web.facades.InvoiceServiceImp;
import com.gateweb.charge.model.UserEntity;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/7/10
 * Time: 上午 9:28
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/backendAdmin/loginServlet")
@Controller
public class LoginServlet extends MvcBaseServlet {

	private static final String SESSION_SEARCH_OBJ_NAME= "indexVO";
	private static final String DEFAULT_SEARCH_LIST_DISPATCH_PAGE = "/backendAdmin/index.jsp";

	
	@RequestMapping(method=RequestMethod.POST)
    public String defaultPost(@RequestParam MultiValueMap<String, String> paramMap, 
    		Model model, HttpServletRequest request, HttpServletResponse response)  throws Exception {
        logger.debug("defaultPost model:   "+model);
        logger.debug("defaultPost paramMap:   "+paramMap);
        UserEntity user = checkLogin(request, response);
    		BaseFormBean formBeanObject = formBeanObject(request);
    		Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap =  otherMap(request, response, formBeanObject);
        otherMap.put(DISPATCH_PAGE, DEFAULT_SEARCH_LIST_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);
        return TEMPLATE_PAGE;
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public String defaultGet(@RequestParam MultiValueMap<String, String> paramMap
    			, Model model, HttpServletRequest request, HttpServletResponse response)
    	            throws Exception {
    		logger.debug("defaultGet model:   "+model);
    		logger.debug("defaultGet paramMap:   "+paramMap);
    		UserEntity user = checkLogin(request, response);
    		
    		//request.getSession().setMaxInactiveInterval(user.getLogoutTime().intValue()*60);
    		//request.setAttribute("logoutTime", user.getLogoutTime() != null && user.getLogoutTime().intValue() > 0 ? user.getLogoutTime().intValue() : 120);

        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap =  otherMap(request, response, formBeanObject);
        otherMap.put(DISPATCH_PAGE, DEFAULT_SEARCH_LIST_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);
    		return TEMPLATE_PAGE;
    }
}
