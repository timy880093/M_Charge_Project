package com.gate.web.servlets.backend.prepayReduct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gate.core.bean.BaseFormBean;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.exceptions.FormValidationException;
import com.gate.web.exceptions.ReturnPathException;
import com.gate.web.facades.CalCycleService;
import com.gate.web.facades.PrepayDeductService;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.UserEntity;
import com.gateweb.einv.exception.EinvSysException;
import com.google.gson.Gson;

@RequestMapping("/backendAdmin/prepayDeductSearchServlet")
@Controller
public class PrepayDeductSearchServlet extends MvcBaseServlet {
	
	private static final String SESSION_SEARCH_OBJ_NAME= "prepayDeductListSearchVO";
	private static final String DEFAULT_SEARCH_LIST_DISPATCH_PAGE = "/backendAdmin/prepayDeduct/prepayDeductCompany_list.jsp";
    //2016.5.3 匯出功能
    //private static final String DOWNLOAD_FILE_NAME ="item_data";
    //不指定位置，而是在程式裡面使用getResource
    //private static String TEMPLATE_INVOICE_EXCEL_LOCATION = "template/tempFile/item_temp.xls" ;
    //private static String TEMPLATE_INVOICE_EXCEL_DOWNLOAD;

	@Autowired
    PrepayDeductService prepayDeductService;
	
	@Autowired
	CalCycleService calCycleService;
	
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
    		BaseFormBean formBeanObject = formBeanObject(request);
    		Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap =  otherMap(request, response, formBeanObject);
        otherMap.put(DISPATCH_PAGE, DEFAULT_SEARCH_LIST_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);
    		return TEMPLATE_PAGE;
    }
    
    @RequestMapping(method=RequestMethod.GET, params="sessionClean=Y", produces="application/json;charset=utf-8")
    public String mainList(@RequestParam("sessionClean") String sessionClean, Model model, HttpServletRequest request, HttpServletResponse response)
    	            throws ServletException, IOException {
    	logger.debug("mainList sessionClean:   "+sessionClean);    	
    	logger.debug("mainList model:   "+model);
    	//String returnPage =BOOTSTRAP_TEMPLATE_PAGE
    	String returnPage = TEMPLATE_PAGE;
    	Gson gson = new Gson();
    	String errorMessage = null;
        
    	//commonService(request, response);
    	//common service裡面需要4個物件，然後傳給serviceBU，產生jsp 和物件
    	//所以這裡要先取出這4個物件然後再做事情
    	try {
	    	UserEntity user = checkLogin(request, response);
	    	BaseFormBean formBeanObject = formBeanObject(request);
	    	Map requestParameterMap = request.getParameterMap();
	    	Map requestAttMap = requestAttMap(request);
	    	Map sessionAttMap = sessionAttMap(request);
	    	Map otherMap =  otherMap(request, response, formBeanObject);
	    	
	    	//ServiceBU
	    	List<Object> outList = new ArrayList<Object>();
	    	List userCompanyList = calCycleService.getUserCompanyList();
        outList.add(userCompanyList); //1.用戶下拉選單
        otherMap.put(REQUEST_SEND_OBJECT, outList);
	    	otherMap.put(DISPATCH_PAGE, DEFAULT_SEARCH_LIST_DISPATCH_PAGE);
	    	sendObjToViewer(request, otherMap);
	    	
    	} catch (EinvSysException ese) {
    		logger.error(ese.getMessage(), ese);
    		errorMessage = gson.toJson("錯誤:"+ese.getMessage());
            returnPage =  ERROR_PAGE;
    	} catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            errorMessage = gson.toJson("IO動作失敗:"+ex.getMessage());
            request.setAttribute(ERROR_MESSAGE, errorMessage);
            returnPage =  ERROR_PAGE;
        } catch (ServletException ex) {
            logger.error(ex.getMessage(), ex);
            errorMessage = gson.toJson("伺服器內部錯誤:"+ex.getMessage());
            returnPage =  ERROR_PAGE;
        } catch (FormValidationException ex) {
        	errorMessage = gson.toJson("表單驗證失敗:"+ex.getMessage());
            logger.error(ex.getMessage(), ex);
            returnPage =  ERROR_PAGE;
        } catch (ReturnPathException ex) {
        	errorMessage = gson.toJson("回傳路徑有問題:"+ex.getMessage());
            logger.error(ex.getMessage(), ex);
            returnPage =  ERROR_PAGE;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            errorMessage = gson.toJson("錯誤:"+ex.getMessage());
    		return ERROR_PAGE;
        }
    	if(errorMessage != null){
    		request.setAttribute(ERROR_MESSAGE, errorMessage);
    	}
    	
    	return returnPage;
    }
    
	/**
	 * 可以改寫成回傳承一個json物件而不是json字串，這樣就不會有亂碼 因為json字串是8859─1編碼，而json物件是utf
	 * 
	 * @param method
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, params = "method=search", produces = "application/json;charset=utf-8")
	public @ResponseBody String search(@RequestParam MultiValueMap<String, String> paramMap,
					@RequestHeader HttpHeaders headers, Model model
					, @RequestParam(value="searchField[]", required = false) List<String> searchField
					, @RequestParam(value="searchString[]", required = false) List<String> searchString
					, @RequestParam(value="sidx", required= true) String sidx
					, @RequestParam(value="sord", required= true) String sord
					, @RequestParam(value="rows", required= true) Integer rows
					, @RequestParam(value="page", required= true) Integer page
					, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("search model:   "+model);
    		logger.debug("search paramMap:   "+paramMap);
    	
    		UserEntity user = checkLogin(request, response);
		BaseFormBean formBeanObject = formBeanObject(request);
		Map requestParameterMap = request.getParameterMap();
		Map requestAttMap = requestAttMap(request);
		Map sessionAttMap = sessionAttMap(request);
		Map otherMap = otherMap(request, response, formBeanObject);
		
		sendObjToViewer(request, otherMap);

		QuerySettingVO querySettingVO = new QuerySettingVO();
		Map searchMap = new HashMap();
		if (searchField != null && searchString != null && searchField.size() == searchString.size()) {
			for (int i = 0; i < searchField.size(); i++) {
				searchMap.put(searchField.get(i), java.net.URLDecoder.decode(searchString.get(i), "UTF-8"));
			}
		} else {
			logger.debug("No searchField");
		}
		querySettingVO.setSearchMap(searchMap);
		querySettingVO.setSidx(sidx);
		querySettingVO.setSord(sord);
		querySettingVO.setPage(page);
		querySettingVO.setRows(rows);

		request.getSession().setAttribute(SESSION_SEARCH_OBJ_NAME, querySettingVO);
		logger.debug("setQuerySettingVO: " + querySettingVO);
		
		//Search list
		//remove function
		//getData()
		Map data = prepayDeductService.getPrepayDeductCompanyList(querySettingVO);
		Map gridData = setGrid(querySettingVO, data);

		// otherMap.put(AJAX_JSON_OBJECT, pageMap);
		String jsonString = convertAjaxToJson(gridData);
		return jsonString;

	}
	
	/**
	 * 可以改寫成回傳承一個json物件而不是json字串，這樣就不會有亂碼 因為json字串是8859─1編碼，而json物件是utf
	 * 
	 * @param method
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, params = "method=createPdm", produces = "application/json;charset=utf-8")
	public @ResponseBody String createPdm(@RequestParam MultiValueMap<String, String> paramMap,
					@RequestHeader HttpHeaders headers, Model model
					, @RequestParam(value="companyId", required= true) Integer companyId
					, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("createPdm model:   "+model);
		logger.debug("createPdm companyId:   "+companyId);
    		logger.debug("createPdm paramMap:   "+paramMap);
    	
    		UserEntity user = checkLogin(request, response);
		BaseFormBean formBeanObject = formBeanObject(request);
		Map requestParameterMap = request.getParameterMap();
		Map requestAttMap = requestAttMap(request);
		Map sessionAttMap = sessionAttMap(request);
		Map otherMap = otherMap(request, response, formBeanObject);
		
		sendObjToViewer(request, otherMap);

		//Search list
		//remove function
		String data = "success!!";
        Integer exeCnt = 0;
		try{
            //新增使用預用金的用戶
            exeCnt = prepayDeductService.transactionCreatePdm(companyId);
            data += "  total counts: "+exeCnt+"";
        }catch(Exception ex){
            System.out.println(ex);
            data = " fail!!";
        }

		// otherMap.put(AJAX_JSON_OBJECT, pageMap);
		Gson gson = new Gson();
		return gson.toJson(data);

	}

}
