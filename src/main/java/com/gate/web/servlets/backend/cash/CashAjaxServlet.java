package com.gate.web.servlets.backend.cash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gate.core.bean.BaseFormBean;
import com.gate.web.exceptions.FormValidationException;
import com.gate.web.exceptions.ReturnPathException;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.UserEntity;
import com.gateweb.einv.exception.EinvSysException;
import com.google.gson.Gson;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;

import com.gate.web.facades.CashService;
import com.gate.web.servlets.BaseServlet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by emily on 2016/1/21.
 */

@RequestMapping("/backendAdmin/cashAjaxServlet")
@Controller

public class CashAjaxServlet extends MvcBaseServlet {

    @Autowired
    CashService cashService;

    @RequestMapping(method = RequestMethod.POST)
    public String defaultPost(@RequestParam MultiValueMap<String, String> paramMap,
                              Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("defaultPost model:   " + model);
        logger.debug("defaultPost paramMap:   " + paramMap);
        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);

        sendObjToViewer(request, otherMap);
        return TEMPLATE_PAGE;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String defaultGet(@RequestParam MultiValueMap<String, String> paramMap
            , Model model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        logger.debug("defaultGet model:   " + model);
        logger.debug("defaultGet paramMap:   " + paramMap);
        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);

        sendObjToViewer(request, otherMap);
        return TEMPLATE_PAGE;
    }

    @RequestMapping(method = RequestMethod.GET, params = "sessionClean=Y", produces = "application/json;charset=utf-8")
    public String mainList(@RequestParam("sessionClean") String sessionClean, Model model, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("mainList sessionClean:   " + sessionClean);
        logger.debug("mainList model:   " + model);
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
            Map otherMap = otherMap(request, response, formBeanObject);

            //ServiceBU
            List<Object> outList = new ArrayList<Object>();
            sendObjToViewer(request, otherMap);

        } catch (EinvSysException ese) {
            logger.error(ese.getMessage(), ese);
            errorMessage = gson.toJson("錯誤:" + ese.getMessage());
            returnPage = ERROR_PAGE;
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            errorMessage = gson.toJson("IO動作失敗:" + ex.getMessage());
            request.setAttribute(ERROR_MESSAGE, errorMessage);
            returnPage = ERROR_PAGE;
        } catch (ServletException ex) {
            logger.error(ex.getMessage(), ex);
            errorMessage = gson.toJson("伺服器內部錯誤:" + ex.getMessage());
            returnPage = ERROR_PAGE;
        } catch (FormValidationException ex) {
            errorMessage = gson.toJson("表單驗證失敗:" + ex.getMessage());
            logger.error(ex.getMessage(), ex);
            returnPage = ERROR_PAGE;
        } catch (ReturnPathException ex) {
            errorMessage = gson.toJson("回傳路徑有問題:" + ex.getMessage());
            logger.error(ex.getMessage(), ex);
            returnPage = ERROR_PAGE;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            errorMessage = gson.toJson("錯誤:" + ex.getMessage());
            return ERROR_PAGE;
        }
        if (errorMessage != null) {
            request.setAttribute(ERROR_MESSAGE, errorMessage);
        }

        return returnPage;
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=in", produces = "application/json;charset=utf-8")

    public String in(@RequestParam("method") String method, Model model
            , @RequestParam(value = "cashMasterId", required = true) Integer cashMasterId
            , @RequestParam(value = "inAmount", required = true) Double inAmount
            , @RequestParam(value = "inDate", required = true) String inDate
            , @RequestParam(value = "inNote", required = true) String inNote
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("delCashMaster model:   " + model);
        System.out.println("delCashMaster method:   " + method);
        System.out.println("delCashMaster cashMasterId:   " + cashMasterId);
        System.out.println("delCashMastery cashMaster:   " + inAmount);
        System.out.println("delCashMastery cashMaster:   " + inDate);
        System.out.println("delCashMastery cashMaster:   " + inNote);
        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);


        String data = "success!!";

        try {

            cashService.in(cashMasterId, inAmount, inDate, inNote);

        } catch (Exception ex) {
            System.out.println(ex);
            data = " fail!!";
        }

        // otherMap.put(AJAX_JSON_OBJECT, pageMap);
        Gson gson = new Gson();
        return gson.toJson(data);

    }

    @RequestMapping(method = RequestMethod.GET, params = "method=reSendEmail", produces = "application/json;charset=utf-8")

    public String reSendEmail(@RequestParam("method") String method, Model model
            , @RequestParam(value = "cashMasterId", required = true) String cashMasterId
            , @RequestParam(value = "email", required = true) String email
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("delCashMaster model:   " + model);
        System.out.println("delCashMaster method:   " + method);
        System.out.println("delCashMaster cashMasterId:   " + cashMasterId);
        System.out.println("delCashMastery cashMaster:   " + email);

        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);


        String data = "success!!";
        Integer exeCnt = 0;

        try {


            exeCnt = cashService.reSendBillEmail(cashMasterId, email);
            data += "  total counts: " + exeCnt + "";
//            if (exeCnt == 0) {
//                data = "error";
//            }
        } catch (EmailException e) {
            e.printStackTrace();
            data = "error";
        }
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=cancelIn", produces = "application/json;charset=utf-8")

    public String cancelIn(@RequestParam("method") String method, Model model
            , @RequestParam(value = "cashMasterId", required = true) String cashMasterId

            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("delCashMaster model:   " + model);
        System.out.println("delCashMaster method:   " + method);
        System.out.println("delCashMaster cashMasterId:   " + cashMasterId);


        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);


        String data = "success!!";
        try {

            int exeCnt = cashService.transactionCancelIn(cashMasterId);
            if (exeCnt == 0) {
                data = "error";
            }
        } catch (EmailException e) {
            e.printStackTrace();
            data = "error";
        }
        Gson gson = new Gson();
        return gson.toJson(data);
    }
}




