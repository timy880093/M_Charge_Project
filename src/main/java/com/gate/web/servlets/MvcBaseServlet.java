package com.gate.web.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.gate.config.SystemConfig;
import com.gate.core.bean.BaseFormBean;
import com.gate.core.db.Dom4jUtils;
import com.gate.utils.Consts;
import com.gate.utils.DateDeserializer;
import com.gate.utils.RequestToMapUtils;
import com.gate.utils.SqlDateDeserializer;
import com.gate.utils.TimeStampDeserializer;
import com.gate.web.authority.UserInfo;
import com.gate.web.authority.UserInfoContext;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.exceptions.FormValidationException;
import com.gate.web.facades.CompanyService;
import com.gate.web.facades.UserService;
import com.gate.web.messages.ErrorMessages;
import com.gateweb.charge.model.CompanyEntity;
import com.gateweb.charge.model.UserEntity;
import com.gateweb.charge.service.ChargeFacade;
import com.gateweb.einv.exception.EinvSysException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Created by simon on 2014/7/4.
 */

public abstract class MvcBaseServlet {


    protected Logger logger = LogManager.getLogger(getClass());
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
    protected static final String ERROR_PAGE = "/exception.jsp";
    protected static final String LOGIN_ERROR_PAGE = "/login_error.jsp";
    protected static final String ERROR_MESSAGE = "ERROR_MESSAGE";
    protected static final String DISPATCH_PAGE = "DISPATCH_PAGE";
    protected static final String FORWARD_TYPE_R = "R";
    protected static final String FORWARD_TYPE_F = "F";
    protected static final String TEMPLATE_PAGE = "/backendAdmin/template/template.jsp";
    protected static final String BOOTSTRAP_TEMPLATE_PAGE = "/backendAdmin/template/indexLayout.jsp";
    protected static final String BOOTSTRAP_POP_TEMPLATE_PAGE = "/backendAdmin/template/pop_indexLayout.jsp";
    protected static final String POP_TEMPLATE_PAGE = "/backendAdmin/template/pop_template.jsp";
    protected static final String POP_TEMPLATE_RELOAD_PAGE = "/backendAdmin/template/pop_templateWithReload.jsp";

    protected static final String IMPORT_TEMPLATE_PAGE = "/backendAdmin/template/import_template.jsp";
    public static final String TIME_LIMIT = SystemConfig.getInstance().getParameter("servlet_log_time_limit");
    
    public static Integer ROLE_ADMIN = 100;
    public static Integer ROLE_FIRM_USER = 300;

    @Autowired
    public ChargeFacade chargeFacade;

    @Autowired
    public UserService userService;

    @Autowired
    public ReloadableResourceBundleMessageSource messageSource;

    @Autowired
    public CompanyService companyService;
	
	/*public void init(ServletConfig config) throws ServletException {
	    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
	      config.getServletContext());
	  }*/
	
/*    
    public void forwardErrorPage(HttpServletRequest request, HttpServletResponse response, String message,
                                    Exception ex)
            throws ServletException, IOException {
        ErrorMessages messages = getErrorMessages(request, message, ex);
        RequestDispatcher rd = getServletContext().getRequestDispatcher(ERROR_PAGE);
        Gson gson = new Gson();
        String errorMessage = gson.toJson(messages);
        request.setAttribute(ERROR_MESSAGE, errorMessage);
        rd.forward(request, response);
    }

    public void forwardLoginErrorPage(HttpServletRequest request, HttpServletResponse response, String message, Exception ex)
            throws ServletException, IOException {
        RequestDispatcher rd = getServletContext().getRequestDispatcher(LOGIN_ERROR_PAGE);
        request.setAttribute(ERROR_MESSAGE, message+":"+ex.getMessage());
        rd.forward(request, response);
    }

    public void forward(String path, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = getServletContext().getRequestDispatcher(path);
        rd.forward(request, response);
    }

    public void include(String path, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = getServletContext().getRequestDispatcher(path);
        rd.include(request, response);
    }

    public void redirect(String path, HttpServletResponse response) throws IOException {
        response.sendRedirect(path);
    }
*/

    public ErrorMessages getErrorMessages(HttpServletRequest request, String message, Exception ex) {
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

    public UserEntity checkLogin(HttpServletRequest request, HttpServletResponse response)
            throws Exception, EinvSysException {

        long startTime = Calendar.getInstance().getTimeInMillis();
        UserEntity user = (UserEntity) request.getSession().getAttribute("loginUser");
        List<CompanyEntity> referenceList = (List<CompanyEntity>) request.getSession().getAttribute("accountReferenceList");
        CompanyEntity company = (CompanyEntity) request.getSession().getAttribute("company");
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userContext");

        try {
            if (user == null) {
                String userName = null;
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof UserEntity) {
                    logger.debug("instanceof Username:" + ((UserEntity) principal).getUsername());
                    user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    logger.debug("instanceof User:" + user);
                    request.getSession().setAttribute("loginUser", user);
                    userName = user.getUsername();
                    CompanyEntity c_ = new CompanyEntity();
                    c_.setParentId(user.getCompanyId().intValue());
                    referenceList = chargeFacade.searchBy(c_);
                    if (referenceList != null && referenceList.size() > 0) {
                        request.getSession().setAttribute("accountReferenceList", referenceList);
                    } else {
                        request.getSession().setAttribute("accountReferenceList", new ArrayList<CompanyEntity>());
                    }
                } else if (principal instanceof UserDetails) {
                    logger.debug("instanceof UserDetails:" + ((UserDetails) principal).getUsername());
                    UserEntity user1 = new UserEntity();
                    user1.setAccount(((UserDetails) principal).getUsername());
                    List<UserEntity> usersList = chargeFacade.searchBy(user1);
                    if (usersList != null && usersList.size() > 0) {
                        // logger.debug("usersList.get(0):"+usersList.get(0));
                        user = usersList.get(0);
                        request.getSession().setAttribute("loginUser", user);

                       
                        	CompanyEntity c_ = new CompanyEntity();
                        	c_.setParentId(user.getCompanyId().intValue());
                        referenceList = chargeFacade.searchBy(c_);
                        if (referenceList != null && referenceList.size() > 0) {
                            request.getSession().setAttribute("accountReferenceList", referenceList);
                        } else {
                            request.getSession().setAttribute("accountReferenceList",
                                    new ArrayList<CompanyEntity>());
                        }
                        
                        company = chargeFacade.findCompanyById(user.getCompanyId().intValue());
                        if (company == null) {
                            throw new EinvSysException(messageSource.getMessage("Company.notfound", null, null, null));
                        }
                        request.getSession().setAttribute("company", company);

                    } else {
                        logger.error("No user");
                        logger.error("No Login? behacked?");
                        throw new EinvSysException(messageSource.getMessage("User.notfound", null, null, null));
                    }
                } else {
                    logger.error("How can user login?:" + principal.toString());
                    throw new EinvSysException(messageSource.getMessage("User.loginFail", null, null, null));
                }
            }
            // from template.jsp to here
            request.setAttribute("login_name", user.getName());
            if (company != null) {
                request.setAttribute("company_name", company.getName());
            }
            request.setAttribute("logoutTime", user.getLogoutTime() != null && user.getLogoutTime().intValue() > 0 ? user.getLogoutTime().intValue() : 120);

            // logger.debug("user:"+user.getUsername()+",
            // userInfo:"+UserInfoContext.getUserInfo().getLoginName()+",
            // userContext:"+userInfo.getEmail());
            if (user != null && (UserInfoContext.getUserInfo() == null || userInfo == null)) {// 表示有經過授權登入
                userInfo = new UserInfo();
                userInfo.setUserId(user.getUserId().toString());
                userInfo.setRoleId(user.getRoleId().toString());
                userInfo.setRoleName(Consts.convRoleIdToSecurityId(user.getRoleId().intValue()));
                userInfo.setCompanyId(user.getCompanyId().toString());
                userInfo.setAccount(user.getAccount());
                if (user.getPrinterId() != null) {
                    userInfo.setAccount(user.getAccount());
                    userInfo.setPrinterId(user.getPrinterId().toString());
                }
                userInfo.setLoginName(user.getName());
                userInfo.setEmail(user.getEmail());
                if (company != null) {
                    userInfo.setCompanyName(company.getName());
                }
                StringBuffer sb = new StringBuffer();
                StringBuffer bnoSb = new StringBuffer();
                List<String> referenceBusinessNo = new ArrayList<String>();

                if (referenceList != null && referenceList.size() > 0) {
//                    userInfo.setReferenceCompany(referenceList);
                    int i = 0;
                    for (CompanyEntity ref : referenceList) {
                        if (i++ > 0) {
                            sb.append(",");
                            bnoSb.append(",");

                        }
                        sb.append(ref.getCompanyId().toString());
                        bnoSb.append(ref.getBusinessNo());
                        referenceBusinessNo.add(ref.getBusinessNo());

//                        if (CompanyServiceImp.ALL_COMPANY_ID_MAP != null) {
//                            if (CompanyServiceImp.ALL_COMPANY_ID_MAP.get(ref.getCompanyId()) != null) {
//                                bnoSb.append(CompanyServiceImp.ALL_COMPANY_ID_MAP.get(ref.getCompanyId()).getBusinessNo());
//                                referenceBusinessNo.add(CompanyServiceImp.ALL_COMPANY_ID_MAP.get(ref.getCompanyId()).getBusinessNo());
//                            } else {
//                                throw new EinvFacadeException("Something wrong!!!");
//                            }
//                        } else {
//                            companyService.setCompanyListAndSetStatic();
//                        }
                    }
                    sb.append(",");
                    bnoSb.append(",");
                    sb.append(company.getCompanyId());
                    bnoSb.append(company.getBusinessNo());
                    referenceBusinessNo.add(company.getBusinessNo());

                } else {
                    if (company != null) {
                        sb.append(company.getCompanyId());
                        bnoSb.append(company.getBusinessNo());
                        referenceBusinessNo.add(company.getBusinessNo());
                    }
                }

                request.getSession().setAttribute("referenceBusinessNo", referenceBusinessNo);
                userInfo.setReferenceCompanyId(sb.toString());
                userInfo.setReferenceCompanyBusinessNo(bnoSb.toString());

                request.getSession().setAttribute("AcceptCompanyIdString", sb.toString());
                request.getSession().setAttribute("AcceptBusinessNoString", bnoSb.toString());


                userInfo.setLogout_time((user.getLogoutTime() != null && user.getLogoutTime().intValue() > 0) ? user.getLogoutTime().intValue() : 120);
                request.getSession().setAttribute("userContext", userInfo);

                UserInfoContext.setUserInfo(userInfo);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new EinvSysException(messageSource.getMessage("User.loginFail", null, null));
            //forwardErrorPage(request, response, "其他錯誤原因", ex);
        }
        long endTime = Calendar.getInstance().getTimeInMillis();
        long totalTime = endTime - startTime;
        // 如果總時間大於SystemConfig中servlet_log_time_limit所設定的秒數就寫log
        if (totalTime > Integer.valueOf(TIME_LIMIT)) {
            logger.info("spend time = " + totalTime);
        }
        return user;
    }

    /**
     * @param request
     * @return
     * @throws Exception
     * @throws FormValidationException
     */
    public BaseFormBean formBeanObject(HttpServletRequest request)
            throws Exception, FormValidationException {
        BaseFormBean formBeanObject = null;

        try {
            UserEntity user = (UserEntity) request.getSession().getAttribute("loginUser");
            CompanyEntity company = (CompanyEntity) request.getSession().getAttribute("company");

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formBeanObject;

    }
    /*public Map requestParameterMap(HttpServletRequest request){
    	//放置request parameter
        Map requestParameterMap = request.getParameterMap();
        return requestParameterMap;
    }*/

    public Map requestAttMap(HttpServletRequest request) {
        //放置request Attribute
        Map requestAttMap = new HashMap();
        Enumeration attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String key = (String) attributeNames.nextElement();
            Object value = request.getAttribute(key);
            requestAttMap.put(key, value);
        }
        return requestAttMap;
    }

    public Map sessionAttMap(HttpServletRequest request) {
        //放置session attribute
        Map sessionAttMap = new HashMap();
        Enumeration sessionAttNames = request.getSession().getAttributeNames();
        while (sessionAttNames.hasMoreElements()) {
            String key = (String) sessionAttNames.nextElement();
            Object value = request.getSession().getAttribute(key);
            sessionAttMap.put(key, value);
        }
        return sessionAttMap;
    }

    public Map otherMap(HttpServletRequest request, HttpServletResponse response, BaseFormBean formBeanObject) {
        UserEntity user = (UserEntity) request.getSession().getAttribute("loginUser");
        CompanyEntity company = (CompanyEntity) request.getSession().getAttribute("company");

        //放置其他資訊
        Map otherMap = new HashMap();
        otherMap.put(REQUEST, request);
        otherMap.put(RESPONSE, response);
        otherMap.put(FORM_BEAN, formBeanObject);
        otherMap.put("AcceptCompanyIdString", request.getSession().getAttribute("AcceptCompanyIdString"));
        otherMap.put("AcceptBusinessNoString", request.getSession().getAttribute("AcceptBusinessNoString"));
        if (user != null) {
            otherMap.put(USER_ID, user.getUserId().toString());
            otherMap.put(ROLE_ID, user.getRoleId().toString());
            otherMap.put(ROLE_NAME, Consts.convRoleIdToSecurityId(user.getRoleId().intValue()));
            otherMap.put(COMPANY_ID, user.getCompanyId().toString());
            otherMap.put(LOGIN_NAME, user.getName());
            otherMap.put(EMAIL, user.getEmail());
        }
        if (company != null) {
            otherMap.put(COMPANY_NAME, company.getName());
        }

        return otherMap;

    }
    	            
    	   /*         
    	        try    
    				long startTime = Calendar.getInstance().getTimeInMillis();
    	            String[] returnPath = serviceBU(requestParameterMap, requestAttMap, sessionAttMap, otherMap);
    	            //log end time
    	            long endTime = Calendar.getInstance().getTimeInMillis();
    	            long totalTime = endTime-startTime;
    	            //如果總時間大於SystemConfig中servlet_log_time_limit所設定的秒數就寫log
    	            if(totalTime>Integer.valueOf(TIME_LIMIT)){
    	                logger.info("spend time = "+ totalTime);
    	            }

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
    	                logger.debug("action:   "+action);
    	                logger.debug("path:   "+path);
    	                if (action.equals("F")) {//forward
    	                    //forward(path, request, response);
    	                } else if (action.equals("I")) {//include
    	                    //include(path, request, response);
    	                } else if (action.equals("R")) {//redirect
    	                    //redirect(path, response);
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
//    	            logger.error(ex.getMessage(), ex);
    	            forwardLoginErrorPage(request,response,"登入失敗",ex);
    	        }  catch (Exception ex) {
    	            logger.error(ex.getMessage(), ex);
    	            forwardErrorPage(request, response, "其他錯誤原因", ex);
    	        }
    	    }
    */


    public void sendObjToViewer(HttpServletRequest request, Map otherMap) {
        Object sessionObj = otherMap.get(SESSION_SEND_OBJECT);
        Object requestObj = otherMap.get(REQUEST_SEND_OBJECT);
        Object dispatch = otherMap.get(DISPATCH_PAGE);
//        Gson gson = new Gson();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        Gson gson = gsonBuilder.create();
        HttpSession session = request.getSession();
        if (sessionObj instanceof List) {
            List sessionList = (List) sessionObj;
            for (int i = 0; i < sessionList.size(); i++) {
                Object obj = sessionList.get(i);
                //去除雙引號在顯示資料中的處理。
                String jsonString = gson.toJson(obj).replaceAll("\\\\\\\\", "\\\\\\\\u005c").replaceAll("\\\\\"", "\\\\\\\\u0022");
                session.setAttribute(SESSION_SEND_OBJECT + "_" + i, jsonString);
            }
        } else {
            if (sessionObj != null) {
                String jsonString = gson.toJson(sessionObj).replaceAll("\\\\\\\\", "\\\\\\\\u005c").replaceAll("\\\\\"", "\\\\\\\\u0022");
                session.setAttribute(SESSION_SEND_OBJECT + "_0", jsonString);
            }
        }
        if (requestObj instanceof List) {
            List requestList = (List) requestObj;
            for (int i = 0; i < requestList.size(); i++) {
                Object obj = requestList.get(i);
                String jsonString = gson.toJson(obj).replaceAll("\\\\\\\\", "\\\\\\\\u005c").replaceAll("\\\\\"", "\\\\\\\\u0022");
                request.setAttribute(REQUEST_SEND_OBJECT + "_" + i, jsonString);
            }
        } else {
            if (requestObj != null) {
                String jsonString = gson.toJson(requestObj).replaceAll("\\\\\\\\", "\\\\\\\\u005c").replaceAll("\\\\\"", "\\\\\\\\u0022");
                request.setAttribute(REQUEST_SEND_OBJECT + "_0", jsonString);
            }
        }
        if (dispatch != null) {
            request.setAttribute(DISPATCH_PAGE, String.valueOf(dispatch));
        }
    }

    public String sendObjToResponse(HttpServletResponse response, Map otherMap) throws IOException {
        Object jsonObj = otherMap.get(AJAX_JSON_OBJECT);
        if (jsonObj != null) {
            String jsonString = "";
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();
            //Gson gson = new GsonBuilder().create();
            if (gson != null) {
                try {
                    jsonString = gson.toJson(jsonObj).replaceAll("\\\\\\\\", "\\\\\\\\u005c").replaceAll("\\\\\"", "\\\\\\\\u0022");
                } catch (Exception e) {
                    logger.debug("##############");
                    logger.debug(jsonString);
                    logger.debug("##############");
                }
            } else {
                throw new IOException("sendObjToResponse gson to json error.");
            }
            response.getWriter().write(jsonString);
            return jsonString;
        }
        return "";
    }

    public String sendObjToResponse(Map otherMap) throws Exception {
        Object jsonObj = otherMap.get(AJAX_JSON_OBJECT);
        if (jsonObj != null) {
            String jsonString = "";
            Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create();
            //Gson gson = new GsonBuilder().create();
            if (gson != null) {
                try {
                    jsonString = gson.toJson(jsonObj).replaceAll("\\\\\\\\", "\\\\\\\\u005c").replaceAll("\\\\\"", "\\\\\\\\u0022");
                } catch (Exception e) {
                    logger.debug("##############");
                    logger.debug(jsonString);
                    logger.debug("##############");
                }
            } else {
                throw new EinvSysException(messageSource.getMessage("common.toJsonFail", null, null));

            }
            return jsonString;
        }
        return "";
    }

    public String convertAjaxToJson(Map jsonObj) throws Exception {
        if (jsonObj != null) {

            String jsonString = "";
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(java.sql.Date.class, new SqlDateDeserializer())
                    .registerTypeAdapter(java.util.Date.class, new DateDeserializer())
                    .registerTypeAdapter(java.sql.Timestamp.class, new TimeStampDeserializer()).create();
            //Gson gson = new GsonBuilder().create();
            if (gson != null) {
                try {
                    jsonString = gson.toJson(jsonObj).replaceAll("\\\\\\\\", "\\\\\\\\u005c").replaceAll("\\\\\"", "\\\\\\\\u0022");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.debug("error ##############");
                    logger.debug(jsonString);
                    logger.debug("##############");
                }
            } else {
                throw new EinvSysException(messageSource.getMessage("common.toJsonFail", null, null));
            }
            return jsonString;
        }
        return "";
    }

    public String getRandomPassword() {
        int z;
        StringBuilder sb = new StringBuilder();
        int i;
        for (i = 0; i < 6; i++) {
            z = (int) ((Math.random() * 7) % 3);

            if (z == 1) { // 放數字
                sb.append((int) ((Math.random() * 10) + 48));
            } else if (z == 2) { // 放大寫英文
                sb.append((char) (((Math.random() * 26) + 65)));
            } else {// 放小寫英文
                sb.append(((char) ((Math.random() * 26) + 97)));
            }
        }
        return sb.toString();
    }

    public Map setGrid(QuerySettingVO querySettingVO, Map data) {
        Integer size = Integer.valueOf(data.get("TOTAL_COUNT").toString());
        Map pageMap = new HashMap();
        Integer pages = null;
        if (size % querySettingVO.getRows() == 0) {
            pages = size / querySettingVO.getRows();
        } else {
            pages = size / querySettingVO.getRows() + 1;
        }
        pageMap.put("total", pages);
        pageMap.put("page", querySettingVO.getPage());
        pageMap.put("records", size);
        pageMap.put("rows", data.get("DATA_LIST"));
        return pageMap;
    }
    
/*    @ResponseStatus(value=HttpStatus.CONFLICT, reason="Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflict(HttpServletRequest req, Exception exception) {
    	logger.error("Request: " + req.getRequestURL() + " raised " + exception);
    }
    

    @ExceptionHandler({SQLException.class,DataAccessException.class})
    public String databaseError(HttpServletRequest req, Exception exception) {
    	logger.error("Request: " + req.getRequestURL() + " raised " + exception);
      return "/backedAdmin/exception.jsp";
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception exception) {
      logger.error("Request: " + req.getRequestURL() + " raised " + exception);

      ModelAndView mav = new ModelAndView();
      mav.addObject("exception", exception);
      mav.addObject("url", req.getRequestURL());
      mav.setViewName("/backedAdmin/exception.jsp");
      return mav;
    }*/


    public boolean isAdmin(HttpServletRequest request) {
        boolean isAdmin = false;
        try {
            UserEntity user = (UserEntity) request.getSession().getAttribute("loginUser");
            if (user.getRoleId().equals(100)) {
                isAdmin = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAdmin;
    }

    public boolean isAdmin(Integer roleId) {
        boolean isAdmin = false;
        try {
            if (roleId.equals(100)) {
                isAdmin = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAdmin;
    }
    
    public boolean isFirmUser(HttpServletRequest request) {
        boolean isAdmin = false;
        try {
            UserEntity user = (UserEntity) request.getSession().getAttribute("loginUser");
            if (user.getRoleId().equals(ROLE_FIRM_USER)) {
                isAdmin = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAdmin;
    }

    public boolean isFirmUser(Integer roleId) {
        boolean isAdmin = false;
        try {
            if (roleId.equals(ROLE_FIRM_USER)) {
                isAdmin = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAdmin;
    }

    /*public boolean isAdmin(){
	    	boolean isAdmin = false;
	    	try{
		        if(UserInfoContext.getUserInfo().getRoleId().equals("100")){
		            isAdmin = true;
		        }
	    	} catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	return isAdmin;
    }*/

    public String getAcceptCompanyIdString(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("AcceptCompanyIdString");
    }

    public String getAcceptBusinessNoString(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("AcceptBusinessNoString");
    }
}
