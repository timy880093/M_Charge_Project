package com.gate.web.servlets;

import com.gate.core.bean.BaseFormBean;
import com.gate.core.db.Dom4jUtils;
import com.gate.realms.LoginUser;
import com.gate.utils.RequestToMapUtils;
import com.gate.web.authority.UserInfo;
import com.gate.web.authority.UserInfoContext;
import com.gate.web.exceptions.FormValidationException;
import com.gate.web.exceptions.ReturnPathException;
import com.gate.web.messages.ErrorMessages;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

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
//            if(request.isUserInRole("停用")){
//                throw new LoginException("此帳號已停用,或是公司已停權,請洽關網客服!");
////                forwardLoginErrorPage(request,response,"此帳號已停用,或是公司已停權,請洽關網客服!");
//            }
//            if(request.isUserInRole("密碼錯誤")){
//                throw new LoginException("密碼輸入錯誤!");
////                forwardLoginErrorPage(request,response,"密碼輸入錯誤!");
//            }
            //parse User Principal.
            Principal principal = request.getUserPrincipal();

            String userId = "";
            String roleId = "";
            String roleName ="";
            String companyId = "";
            String loginName = "";
            String email = "";
            String companyName = "";
            String referenceIds ="";
            if (principal != null) {//表示有經過授權登入

                String principalName = principal.getName();
                LoginUser loginUser = new Gson().fromJson(principalName,LoginUser.class);
                //Map<String,Object> dataMap = (Map<String, Object>)new Gson().fromJson(principalName,new TypeToken<Map<String, Object>>(){}.getType());
                userId = String.valueOf(loginUser.getUserId());
                roleId = String.valueOf(loginUser.getRoleId());
                roleName = loginUser.getCurrentRoleName();
                companyId =String.valueOf(loginUser.getCompanyId());
                loginName = loginUser.getName();
                email = loginUser.getEmail();
                companyName = loginUser.getCompanyName();

//                String[] dataList = StringUtils.split(principalName, "_");
//                userId = dataList[0];
//                roleId = dataList[1];
//                roleName = dataList[2];
//                companyId = dataList[3];
//                loginName = dataList[4];
//                email = dataList[5];
//                companyName = dataList[6];



                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(userId);
                userInfo.setRoleId(roleId);
                userInfo.setRoleName(roleName);
                userInfo.setCompanyId(companyId);
                userInfo.setLoginName(loginName);
                userInfo.setEmail(email);
                userInfo.setCompanyName(companyName);
                if(StringUtils.isNotEmpty(loginUser.getReferenceCompanyId())){
                    referenceIds = loginUser.getReferenceCompanyId();
                    userInfo.setReferenceCompanyId(referenceIds);
                }
                userInfo.setLogout_time(loginUser.getLogout_time());

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
            otherMap.put(USER_ID, userId);
            otherMap.put(ROLE_ID, roleId);
            otherMap.put(ROLE_NAME,roleName);
            otherMap.put(COMPANY_ID, companyId);
            otherMap.put(LOGIN_NAME, loginName);
            otherMap.put(EMAIL, email);
            otherMap.put(COMPANY_NAME, companyName);
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
        Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create();
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
            Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create();
            String jsonString = gson.toJson(jsonObj).replaceAll( "\\\\\"", "\\\\\\\\u0022");
            response.getWriter().write(jsonString);
        }

    }
}
