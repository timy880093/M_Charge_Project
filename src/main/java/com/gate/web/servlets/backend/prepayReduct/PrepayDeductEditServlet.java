package com.gate.web.servlets.backend.prepayReduct;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gate.core.bean.BaseFormBean;
import com.gate.utils.MapBeanConverterUtils;
import com.gate.web.facades.PrepayDeductService;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.PrepayDeductMasterEntity;
import com.gateweb.charge.model.PrepayDetailEntity;
import com.gateweb.charge.model.UserEntity;
import com.google.gson.Gson;

/**
 * Created by emily on 2017/5/23.
 */

@RequestMapping("/backendAdmin/prepayDeductEditServlet")
@Controller
public class PrepayDeductEditServlet extends MvcBaseServlet {
	
	private static final String DEFAULT_EDIT_DISPATCH_PAGE = "/backendAdmin/prepayDeduct/edit_prepay.jsp";
	private static final String DEFAULT_SEARCH_LIST_DISPATCH_PAGE = "/backendAdmin/prepayDeduct/prepayDeductCompany_list.jsp";
	//private static final String DEFAULT_CREATE_DISPATCH_PAGE = "/backendAdmin/prepayDeduct/item_create.jsp";
	private static final String DEFAULT_EDIT_DISPATCH_PAGE_2 = "/backendAdmin/prepayDeduct/edit_deduct.jsp";
	
	@Autowired
    PrepayDeductService prepayDeductService;
	
	@RequestMapping(method=RequestMethod.POST)
    public String defaultPost(@RequestParam MultiValueMap<String, String> paramMap, Model model
    				, HttpServletRequest request, HttpServletResponse response)  throws Exception {
        System.out.println("defaultPost model:   "+model);
        System.out.println("defaultPost model:   "+paramMap);
	    	UserEntity user = checkLogin(request, response);
	    	BaseFormBean formBeanObject = formBeanObject(request);
	    	Map requestParameterMap = request.getParameterMap();
	        Map requestAttMap = requestAttMap(request);
	        Map sessionAttMap = sessionAttMap(request);
	        Map otherMap =  otherMap(request, response, formBeanObject);
	        otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
	        sendObjToViewer(request, otherMap);
	    	return POP_TEMPLATE_PAGE;
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public String defaultGet(@RequestParam MultiValueMap<String, String> paramMap
    		, Model model, HttpServletRequest request, HttpServletResponse response)
    	            throws Exception {
    		System.out.println("defaultGet model:   "+model);
    		System.out.println("defaultGet paramMap:   "+paramMap);
	    	UserEntity user = checkLogin(request, response);
	    	BaseFormBean formBeanObject = formBeanObject(request);
	    	Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap =  otherMap(request, response, formBeanObject);
        otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);
        return POP_TEMPLATE_PAGE;
    }
    
    @RequestMapping(method = RequestMethod.GET, params = "method=editPrepay", produces = "application/json;charset=utf-8")
	public String editPrepay(@RequestParam("method") String method, Model model
					, @RequestParam(value="masterId", required=true) Integer masterId
					, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("editPrepay model:   " + model);
		System.out.println("editPrepay method:   " + method);
		System.out.println("editPrepay masterId:   " + masterId);

		UserEntity user = checkLogin(request, response);
		BaseFormBean formBeanObject = formBeanObject(request);
		Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap =  otherMap(request, response, formBeanObject);
        
        List<Object> outList = new ArrayList<Object>();
        PrepayDeductMasterEntity master = prepayDeductService.getPrepayDeductMaster(masterId);
        outList.add(master);
        List<Map> histMap = prepayDeductService.getPrepayDetailHisByCompany(master.getCompanyId());
        outList.add(histMap);
        	otherMap.put(REQUEST_SEND_OBJECT, outList);
		otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
		sendObjToViewer(request, otherMap);

		return POP_TEMPLATE_PAGE;	
		
	}
    
    @RequestMapping(method = RequestMethod.GET, params = "method=viewDeduct", produces = "application/json;charset=utf-8")
	public String viewDeduct(@RequestParam("method") String method, Model model
					, @RequestParam(value="masterId", required=true) Integer masterId
					, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("editPrepay model:   " + model);
		System.out.println("editPrepay method:   " + method);
		System.out.println("editPrepay masterId:   " + masterId);

		UserEntity user = checkLogin(request, response);
		BaseFormBean formBeanObject = formBeanObject(request);
		Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap =  otherMap(request, response, formBeanObject);
        
        List<Object> outList = new ArrayList<Object>();
        PrepayDeductMasterEntity master = prepayDeductService.getPrepayDeductMaster(masterId);
        outList.add(master);
        List<Map> histMap = prepayDeductService.getDeductDetailHisByCompany(master.getCompanyId());
        outList.add(histMap);
        
        	otherMap.put(REQUEST_SEND_OBJECT, outList);
		otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE_2);
		sendObjToViewer(request, otherMap);

		return POP_TEMPLATE_PAGE;	
		
	}
    @RequestMapping(method = RequestMethod.POST, params = "method=insertPrepay", produces = "application/json;charset=utf-8")
	public @ResponseBody String insertPrepay(@RequestParam("method") String method, Model model
					, @RequestParam(value="prepayDeductMasterId", required=true) Integer prepayDeductMasterId
					, @RequestParam(value="companyId", required=true) Integer companyId
					, @RequestParam(value="money", required=true) Integer money
					, @RequestParam(value="calYm", required=true) String calYm
					, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("insertPrepay model:   " + model);
		System.out.println("insertPrepay method:   " + method);
		System.out.println("insertPrepay prepayDeductMasterId:   " + prepayDeductMasterId);
		System.out.println("insertPrepay money:   " + money);
		System.out.println("insertPrepay calYm:   " + calYm);
		String data = "OK";
		UserEntity user = checkLogin(request, response);
        
        PrepayDetailEntity entity = new PrepayDetailEntity();
        entity.setCalYm(calYm);
        entity.setMoney(money);
        entity.setCompanyId(companyId);
        entity.setPrepayDeductMasterId(prepayDeductMasterId);
        entity.setModifierId(user.getUserId().intValue());
        entity.setModifyDate(Timestamp.from(new Date().toInstant()));
        entity.setCreatorId(user.getUserId().intValue());
        entity.setCreateDate(Timestamp.from(new Date().toInstant()));
        
        	prepayDeductService.transactionInsertPrepayDetail(entity, user.getUserId().intValue());
        
        Gson gson = new Gson();
		return gson.toJson(data);
		
	}

}
