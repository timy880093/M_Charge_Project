package com.gate.web.servlets.backend.charge;

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
import com.gateweb.charge.model.ChargeModeGradeEntity;
import com.gateweb.charge.model.PrepayDeductMasterEntity;
import com.gateweb.charge.model.UserEntity;
import com.gateweb.einv.exception.EinvSysException;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gate.utils.MapBeanConverterUtils;
import com.gate.web.displaybeans.ChargeModeCycleVO;
import com.gate.web.displaybeans.ChargeModeGradeVO;
import com.gate.web.facades.ChargeService;
import com.gate.web.facades.ChargeServiceImp;
import com.gate.web.formbeans.ChargeModeCycleBean;
import com.gate.web.formbeans.ChargeModeGradeBean;
import com.gate.web.servlets.backend.common.BackendPopTemplateServlet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@RequestMapping("/backendAdmin/chargeEditServlet")
@Controller

public class ChargeEditServlet extends MvcBaseServlet {

    private static final String DEFAULT_CREATE_DISPATCH_PAGE = "/backendAdmin/charge/edit_charge_package.jsp";
    private static final String DEFAULT_EDIT_DISPATCH_PAGE = "/backendAdmin/charge/edit_charge_package_month.jsp";
    private static final String DEFAULT_EDIT_DISPATCH_PAGE_2 = "/backendAdmin/charge/edit_charge_package_grade.jsp";

    @Autowired
    ChargeService chargeService;

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
        otherMap.put(DISPATCH_PAGE, DEFAULT_CREATE_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);
        return POP_TEMPLATE_PAGE;
    }


    @RequestMapping(method = RequestMethod.GET, params = "method=edit", produces = "application/json;charset=utf-8")
    public String edit(@RequestParam("method") String method, Model model
            , @RequestParam(value = "type", required = true) String charge_type

            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("editPrepay model:   " + model);
        System.out.println("editPrepay method:   " + method);
        System.out.println("editPrepay masterId:   " + charge_type);

        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);

        List<Object> outList = new ArrayList<Object>();
        Integer chargeId = null;
        if ("1".equals(charge_type)) { //月租制
            if (chargeId != null) {
                ChargeModeCycleVO chargeVO = chargeService.findChargeModeCycleByChargeId(chargeId);
                outList.add(chargeVO);
            }

            otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
        } else if ("2".equals(charge_type)) { //級距制
            if (chargeId != null) {
                //級距型方案
                ChargeModeGradeVO chargeVO = chargeService.findChargeModeGradeByChargeId(chargeId);
                outList.add(chargeVO);
            }
            otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE_2);
            //級距型方案的級距表
            List gradeList = chargeService.getGradeList(chargeId);
            outList.add(gradeList);
        }
        sendObjToViewer(request, otherMap);
        return POP_TEMPLATE_PAGE;
    }


    @RequestMapping(method = RequestMethod.POST, params = "method=insert", produces = "application/json;charset=utf-8")
    public String insert(@RequestParam("method") String method, Model model
            , @RequestParam(value = "type", required = true) String charge_type

            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("insert model:   " + model);
        System.out.println("insert method:   " + method);
        System.out.println("insert charge_type:   " + charge_type);


       String data = "OK";
        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        if ("1".equals(charge_type)) { //月租型
            ChargeModeCycleBean bean = new ChargeModeCycleBean();
            MapBeanConverterUtils.mapToBean(requestParameterMap, bean);
            if (StringUtils.isNotEmpty(bean.getChargeId())) {
                chargeService.updateChargeModeCycle(bean,user.getUserId());
            } else {
                bean.setStatus(String.valueOf(1));
                chargeService.insertChargeModeCycle(bean,user.getUserId());
            }

        } else if ("2".equals(charge_type)) { //級距型
            ChargeModeGradeBean bean = new ChargeModeGradeBean();
            MapBeanConverterUtils.mapToBean(requestParameterMap, bean);

            chargeService.transactionInsertChargeModeGrade(bean,user.getUserId());
        }
        Gson gson = new Gson();
        return gson.toJson(data);

    }
}


