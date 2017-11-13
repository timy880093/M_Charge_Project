package com.gate.web.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.LoginException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.gate.core.bean.BaseFormBean;
import com.gate.core.db.Dom4jUtils;
import com.gate.utils.DateDeserializer;
import com.gate.utils.RequestToMapUtils;
import com.gate.web.authority.UserInfo;
import com.gate.web.authority.UserInfoContext;
import com.gate.web.exceptions.FormValidationException;
import com.gate.web.exceptions.ReturnPathException;
import com.gate.web.messages.ErrorMessages;
import com.gateweb.charge.model.Company;
import com.gateweb.charge.model.User;
import com.gateweb.charge.service.ChargeFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by simon on 2014/7/4.
 */

public abstract class BaseServlet extends HttpServlet {


    protected static final Logger logger = Logger.getLogger(BaseServlet.class);
    protected static final String REQUEST = "REQUEST";
    protected static final String RESPONSE = "RESPONSE";
    protected static final String FORM_BEAN = "FORM_BEAN";
    protected static final String USER_ID = "USER_ID";
    protected static final String ROLE_ID = "ROLE_ID";
    protected static final String ROLE_NAME = "ROLE_NAME";
    protected static final String COMPANY_ID = "COMPANY_ID";
    protected static final String EMAIL = "EMAIL";
    protected static final String COMPANY_NAME = "COMPANY_NAME";
    protected static final String LOGIN_NAME = "LOGIN_NAME";
    //要傳遞object至前端頁面時，要放入的key.
    protected static final String REQUEST_SEND_OBJECT = "REQUEST_SEND_OBJECT";
    protected static final String SESSION_SEND_OBJECT = "SESSION_SEND_OBJECT";
    protected static final String AJAX_JSON_OBJECT = "AJAX_JSON_OBJECT";
    protected static final String ERROR_PAGE = "/error.jsp";
    protected static final String LOGIN_ERROR_PAGE = "/login_error.jsp";
    protected static final String ERROR_MESSAGE = "ERROR_MESSAGE";
    protected static final String DISPATCH_PAGE = "DISPATCH_PAGE";
    protected static final String FORWARD_TYPE_R="R";
    protected static final String FORWARD_TYPE_F="F";
    protected static final String TEMPLATE_PAGE = "/backendAdmin/template/template.jsp";
    protected static final String POP_TEMPLATE_PAGE = "/backendAdmin/template/pop_template.jsp";
    protected static final String IMPORT_TEMPLATE_PAGE = "/backendAdmin/template/import_template.jsp";

	@Autowired
	ChargeFacade chargeFacade;
	
	public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
	      config.getServletContext());
	  }
	
    /**
     * 前面是action,後面是path
     *
     * @param requestParameterMap
     * @param requestAttMap
     * @param sessionMap
     * @param otherMap
     * @return
     */
    public abstract String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception;

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        commonService(request, response);
    }


    protected void forwardErrorPage(HttpServletRequest request, HttpServletResponse response, String message,
                                    Exception ex)
            throws ServletException, IOException {
        ErrorMessages messages = getErrorMessages(request, message, ex);
        RequestDispatcher rd = getServletContext().getRequestDispatcher(ERROR_PAGE);
        Gson gson = new Gson();
        String errorMessage = gson.toJson(messages);
        request.setAttribute(ERROR_MESSAGE, errorMessage);
        rd.forward(request, response);
    }

    protected void forwardLoginErrorPage(HttpServletRequest request, HttpServletResponse response, String message, Exception ex)
            throws ServletException, IOException {
        RequestDispatcher rd = getServletContext().getRequestDispatcher(LOGIN_ERROR_PAGE);
        request.setAttribute(ERROR_MESSAGE, message+":"+ex.getMessage());
        rd.forward(request, response);
    }

    private void forward(String path, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = getServletContext().getRequestDispatcher(path);
        rd.forward(request, response);
    }

    private void include(String path, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = getServletContext().getRequestDispatcher(path);
        rd.include(request, response);
    }

    private void redirect(String path, HttpServletResponse response) throws IOException {
        response.sendRedirect(path);
    }

    private ErrorMessages getErrorMessages(HttpServletRequest request, String message, Exception ex) {
        ErrorMessages errorMessages = new ErrorMessages();
        Date nowDate = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        String currentTimes = simpleDateFormat.format(nowDate);
        errorMessages.setCurrentTimes(currentTimes);
        String previousPage = request.getRequestURI();
        errorMessages.setPreviousPage(previousPage);
        errorMessages.setMessage(message);
        String stackTrace = ExceptionUtils.getStackTrace(ex);
        errorMessages.setPrintStackMessage(stackTrace);
        return errorMessages;
    }

    protected void commonService(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
        	
        	
        	//debug purpose
        	Enumeration<String> e = request.getParameterNames();
        	while(e.hasMoreElements()) {
        	    String param = e.nextElement();
        	    String[] paramList = request.getParameterValues(param);
        	    if(paramList != null && paramList.length>0){
                	for(int i =0; i < paramList.length; i++){
                		logger.debug("-------------param:"+param+"--------values:"+paramList[i]);
                	}
                }
        	}
        	logger.debug("--------");
        	Enumeration names = request.getHeaderNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                logger.debug("-------------headername:"+name + ": " + request.getHeader(name));
            }
            logger.debug("--------");
        	Enumeration attName = request.getAttributeNames();
            while (attName.hasMoreElements()) {
                String name = (String) attName.nextElement();
                logger.debug("------------attName:"+name + ": " + request.getAttribute(name));
            }
            logger.debug("--------authType:"+ request.getAuthType());
            logger.debug("--------contextPath:"+request.getContextPath());
            logger.debug("--------localAddr:"+request.getLocalAddr());
            
            Cookie[] cookies = request.getCookies();
            if(cookies != null && cookies.length>0){
            	for(int i =0; i < cookies.length; i++){
            		logger.debug("--------cookies:"+cookies[i].getName()+"   "+cookies[i].getValue());
            	}
            }
            
        	User user = (User)request.getSession().getAttribute("loginUser");
        	//List<AccountReference> referenceList = (List<AccountReference>)request.getSession().getAttribute("accountReferenceList");
        	Company company = (Company)request.getSession().getAttribute("company");
        	UserInfo userInfo = (UserInfo)request.getSession().getAttribute("userContext");

        	if(user == null){
	            String userName = null;
	        	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        	if (principal instanceof User) {
	        		System.out.println("Admin 1:"+((User)principal).getUsername());
	        		user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        		System.out.println("Admin 2:"+user);
	        		request.getSession().setAttribute("loginUser", user);
	        		userName = user.getUsername();
	        		/*AccountReference tmpRef = new AccountReference();
	    			tmpRef.setUserId(user.getUserId());
	    			referenceList = chargeFacade.searchBy(tmpRef);
	    			if(referenceList != null && referenceList.size()>0){
	    				request.getSession().setAttribute("accountReferenceList", referenceList);
	    			} else {
	    				request.getSession().setAttribute("accountReferenceList", new ArrayList<AccountReference>());
	    			}*/
	    		} else if (principal instanceof UserDetails) {
	    	    		System.out.println("Admin 2:"+((UserDetails)principal).getUsername());
	    	    		User user1 = new User();
			    		user1.setAccount(((UserDetails)principal).getUsername());
			    		List<User> usersList = chargeFacade.searchBy(user1);
			    		if (usersList != null && usersList.size()>0){
			    			//System.out.println("usersList.get(0):"+usersList.get(0));
			    			user = usersList.get(0);
			    			request.getSession().setAttribute("loginUser", user);
			    			/*AccountReference tmpRef = new AccountReference();
			    			tmpRef.setUserId(user.getUserId());
			    			referenceList = chargeFacade.searchBy(tmpRef);
			    			if(referenceList != null && referenceList.size()>0){
			    				request.getSession().setAttribute("accountReferenceList", referenceList);
			    			} else {
			    				request.getSession().setAttribute("accountReferenceList", new ArrayList<AccountReference>());
			    			}*/
			    			company = chargeFacade.findCompanyById(user.getCompanyId());
			    			request.getSession().setAttribute("company", company);
			    			
			    		}else{
			    			System.out.println("No user");
			    			System.out.println("Admin No Login");
					    	//be  hacked ?
			    		}
	    		} else {
	    			System.out.println("Admin 3:"+principal.toString());
	
	    		}
        	}
        	System.out.println("user:"+user+",  userInfo:"+UserInfoContext.getUserInfo()+", userContext:"+userInfo);
            if (user != null && (UserInfoContext.getUserInfo() == null || userInfo == null)) {//表示有經過授權登入
                userInfo = new UserInfo();
                userInfo.setUserId(user.getUserId().toString());
                userInfo.setRoleId(user.getRoleId().toString());
                userInfo.setRoleName(User.convRoleName(user.getRoleId()));
                userInfo.setCompanyId(user.getCompanyId().toString());
                userInfo.setLoginName(user.getName());
                userInfo.setEmail(user.getEmail());
                if(company!=null){
                	userInfo.setCompanyName(company.getName());
                }
                /*StringBuffer sb = new StringBuffer();
                if(referenceList!=null){
                	int i = 0;
                	for(AccountReference ref: referenceList){
                		if(i++>0){
                			sb.append(",");
                		}
                		sb.append(ref.getCompanyId().toString());
                	}
                }
                userInfo.setReferenceCompanyId(sb.toString());*/
                userInfo.setLogout_time(user.getLogoutTime().intValue());
    			request.getSession().setAttribute("userContext", userInfo);
                UserInfoContext.setUserInfo(userInfo); 
            }
            BaseFormBean formBeanObject = null;
            //透過validation xml來定義每個servlet進入的form Class...
            String fullClassName = this.getClass().getCanonicalName();
            Dom4jUtils dom4jUtils = new Dom4jUtils();
            String validationBeanName = dom4jUtils.getValidationBean(fullClassName);
            if (!validationBeanName.equals("")) {
                Class classObj = Class.forName(validationBeanName);
                Object dynamicObj = classObj.newInstance();
                RequestToMapUtils requestToMapUtils = new RequestToMapUtils();
                formBeanObject = (BaseFormBean) dynamicObj;
                if (formBeanObject != null) {
                    formBeanObject = requestToMapUtils.parserRequestToMap(request, formBeanObject);
                    List<String> validateDataList = formBeanObject.validateData();
                    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                    Validator validator = factory.getValidator();
                    Set<ConstraintViolation<BaseFormBean>> violations = validator.validate(formBeanObject);
                    //開始顯示驗證錯誤資訊
                    if (violations.size() != 0 || validateDataList.size() != 0) {
                        for (ConstraintViolation<BaseFormBean> violation : violations) {//bean validation
                            logger.error(violation.getPropertyPath() + violation.getMessage());
                        }
                        for (int i = 0; i < validateDataList.size(); i++) {//自訂錯誤驗證
                            logger.error(validateDataList.get(i));
                        }
                        throw new FormValidationException();
                    }
                }
            }
            //放置request parameter
            Map requestParameterMap = request.getParameterMap();
            //放置request Attribute
            Map requestAttMap = new HashMap();
            Enumeration attributeNames = request.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String key = (String) attributeNames.nextElement();
                Object value = request.getAttribute(key);
                requestAttMap.put(key, value);
            }
            //放置session attribute
            Map sessionAttMap = new HashMap();
            Enumeration sessionAttNames = request.getSession().getAttributeNames();
            while (sessionAttNames.hasMoreElements()) {
                String key = (String) sessionAttNames.nextElement();
                Object value = request.getSession().getAttribute(key);
                sessionAttMap.put(key, value);
            }
            //放置其他資訊
            Map otherMap = new HashMap();
            otherMap.put(REQUEST, request);
            otherMap.put(RESPONSE, response);
            otherMap.put(FORM_BEAN, formBeanObject);
            if(user != null){
	            otherMap.put(USER_ID, user.getUserId().toString());
	            otherMap.put(ROLE_ID, user.getRoleId().toString());
	            otherMap.put(ROLE_NAME,User.convRoleName(user.getRoleId()));
	            otherMap.put(COMPANY_ID, user.getCompanyId().toString());
	            otherMap.put(LOGIN_NAME, user.getName());
	            otherMap.put(EMAIL, user.getEmail());
            }
            if(company!=null){
            	otherMap.put(COMPANY_NAME, company.getName());
            }
            String[] returnPath = serviceBU(requestParameterMap, requestAttMap, sessionAttMap, otherMap);
            //set object to session or request.
            sendObjToViewer(request, otherMap);
            //json data response write.
            sendObjToResponse(response,otherMap);
            //跳轉至下一個連結。
            if (returnPath == null) {
                //do nothing...
            } else if (returnPath.length == 2) {
                String action = returnPath[0];
                String path = returnPath[1];
                if (action.equals("F")) {//forward
                    forward(path, request, response);
                } else if (action.equals("I")) {//include
                    include(path, request, response);
                } else if (action.equals("R")) {//redirect
                    redirect(path, response);
                }
            } else {
                throw new ReturnPathException();
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            forwardErrorPage(request, response, "IO動作失敗", ex);
        } catch (ServletException ex) {
            logger.error(ex.getMessage(), ex);
            forwardErrorPage(request, response, "伺服器內部錯誤", ex);
        } catch (FormValidationException ex) {
            logger.error(ex.getMessage(), ex);
            forwardErrorPage(request, response, "表單驗證失敗", ex);
        } catch (ReturnPathException ex) {
            logger.error(ex.getMessage(), ex);
            forwardErrorPage(request, response, "回傳路徑有問題", ex);
        } catch (LoginException ex) {
//            logger.error(ex.getMessage(), ex);
            forwardLoginErrorPage(request,response,"登入失敗",ex);
        }  catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            forwardErrorPage(request, response, "其他錯誤原因", ex);
        }
    }

    private void sendObjToViewer(HttpServletRequest request, Map otherMap) {
        Object sessionObj = otherMap.get(SESSION_SEND_OBJECT);
        Object requestObj = otherMap.get(REQUEST_SEND_OBJECT);
        Object dispatch = otherMap.get(DISPATCH_PAGE);
//        Gson gson = new Gson();
        GsonBuilder gsonBuilder = new GsonBuilder();
    	gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
    	gsonBuilder.setDateFormat("yyyy/MM/dd");
        Gson gson = gsonBuilder.create();
        HttpSession session = request.getSession();
        if (sessionObj instanceof List) {
            List sessionList = (List) sessionObj;
            for (int i = 0; i < sessionList.size(); i++) {
                Object obj = sessionList.get(i);
                session.setAttribute(SESSION_SEND_OBJECT + "_" + i, gson.toJson(obj).replaceAll( "\\\\\"", "\\\\\\\\u0022"));
            }
        } else {
            if (sessionObj != null) {
                session.setAttribute(SESSION_SEND_OBJECT + "_0", gson.toJson(sessionObj).replaceAll( "\\\\\"", "\\\\\\\\u0022"));
            }
        }
        if (requestObj instanceof List) {
            List requestList = (List) requestObj;
            for (int i = 0; i < requestList.size(); i++) {
                Object obj = requestList.get(i);
                request.setAttribute(REQUEST_SEND_OBJECT + "_" + i, gson.toJson(obj).replaceAll( "\\\\\"", "\\\\\\\\u0022"));
            }
        } else {
            if (requestObj != null) {
                request.setAttribute(REQUEST_SEND_OBJECT + "_0", gson.toJson(requestObj).replaceAll( "\\\\\"", "\\\\\\\\u0022"));
            }
        }
        if(dispatch != null){
            request.setAttribute(DISPATCH_PAGE,String.valueOf(dispatch));
        }
    }

    private void sendObjToResponse(HttpServletResponse response, Map otherMap) throws IOException {
        Object jsonObj = otherMap.get(AJAX_JSON_OBJECT);
        if(jsonObj!=null){
//            Gson gson = new Gson();
        	GsonBuilder gsonBuilder = new GsonBuilder();
        	gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();
            String jsonString = gson.toJson(jsonObj).replaceAll( "\\\\\"", "\\\\\\\\u0022");
            response.getWriter().write(jsonString);
        }

    }
}
