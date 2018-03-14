package com.gate.web.servlets.backend.commissionLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gate.core.bean.BaseFormBean;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.UserEntity;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import com.gate.web.facades.CommissionLogService;
import com.gate.web.servlets.backend.common.BackendPopTemplateServlet;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

/**
 * Created by emily on 2016/1/11.
 */
@RequestMapping("/backendAdmin/CommissionLogEditServlet")
@Controller
public class CommissionLogEditServlet extends MvcBaseServlet {
    private static final String DEFAULT_EDIT_DISPATCH_PAGE = "/backendAdmin/commissionLog/commissionLog_edit.jsp";


    @Autowired
    CommissionLogService commissionLogService;

    @RequestMapping(method = RequestMethod.POST)
    public String defaultPost(@RequestParam MultiValueMap<String, String> paramMap, Model model
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("defaultPost model:   " + model);
        System.out.println("defaultPost model:   " + paramMap);
        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);
        return POP_TEMPLATE_PAGE;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String defaultGet(@RequestParam MultiValueMap<String, String> paramMap
            , Model model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        System.out.println("defaultGet model:   " + model);
        System.out.println("defaultGet paramMap:   " + paramMap);
        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);
        return POP_TEMPLATE_PAGE;
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=edit", produces = "application/json;charset=utf-8")
    public String edit(@RequestParam("method") Integer method, Model model
            , @RequestParam(value = "commissionLogId", required = true) String  commissionLogId
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("edit model:   " + model);
        System.out.println("edit method:   " + method);
        System.out.println("edit commissionLogId:   " + commissionLogId);

        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);

        List<Object> outList = new ArrayList<Object>();
        List comLogDetailList = commissionLogService.getCommissionLogDetailList(commissionLogId);
        outList.add(comLogDetailList);

        otherMap.put(REQUEST_SEND_OBJECT, outList);
        otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);

        return POP_TEMPLATE_PAGE;
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=updateNote", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String updateNote(@RequestParam MultiValueMap<String, String> paramMap,
                      @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "commission_log_id", required = true) String commission_log_id
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("updateNote model:   " + model);
        logger.debug("updateNote commission_log_id:   " + commission_log_id);
        logger.debug("updateNote paramMap:   " + paramMap);

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
        Integer commissionLogId = 0;
        try {

            String note = ((String[]) requestParameterMap.get("note"))[0];
            commissionLogService.updateNote(commissionLogId, note);

        } catch (Exception ex) {
            System.out.println(ex);
            data = " fail!!";
        }

        // otherMap.put(AJAX_JSON_OBJECT, pageMap);
        Gson gson = new Gson();
        return gson.toJson(data);

    }

    @RequestMapping(method = RequestMethod.GET, params = "method=delete", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String delete(@RequestParam MultiValueMap<String, String> paramMap,
                  @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "commission_log_id", required = true) String commission_log_id
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("updateNote model:   " + model);
        logger.debug("updateNote commission_log_id:   " + commission_log_id);
        logger.debug("updateNote paramMap:   " + paramMap);

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
       Integer commissionLogId = 0;
        try {


            commissionLogService.delCommissionLog(commissionLogId);

        } catch (Exception ex) {
            System.out.println(ex);
            data = " fail!!";
        }

        // otherMap.put(AJAX_JSON_OBJECT, pageMap);
        Gson gson = new Gson();
        return gson.toJson(data);

    }
}





