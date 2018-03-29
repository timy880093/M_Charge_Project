package com.gate.web.servlets.backend.cash;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gate.core.bean.BaseFormBean;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.UserEntity;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gate.web.displaybeans.CashDetailVO;
import com.gate.web.displaybeans.CashMasterVO;
import com.gate.web.facades.CashService;
import com.gateweb.charge.model.BillCycleEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/backendAdmin/cashEditServlet")
@Controller


public class CashEditServlet extends MvcBaseServlet {
    private static final String DEFAULT_EDIT_DISPATCH_PAGE = "/backendAdmin/cash/cash_editCashDetail.jsp";
    private static final String DEFAULT_EDIT_DISPATCH_PAGE_OverListpage = "/backendAdmin/cash/cash_overList.jsp";
    private static final String DEFAULT_EDIT_DISPATCH_PAGE_OverGradeListpage = "/backendAdmin/cash/cash_overGradeList.jsp";

    @Autowired
    CashService cashService;


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

    @RequestMapping(method = RequestMethod.GET, params = "method=viewCashDetail", produces = "application/json;charset=utf-8")
    public String viewCashDetail(@RequestParam("method") String method, Model model
            , @RequestParam(value = "cashMasterId", required = true) Integer cashMasterId
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("editPrepay model:   " + model);
        System.out.println("editPrepay method:   " + method);
        System.out.println("editPrepay masterId:   " + cashMasterId);

        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);

        List<Object> outList = new ArrayList<>();

        if (requestParameterMap.get("cashMasterId") != null) {
            cashMasterId = Integer.parseInt(((String[]) requestParameterMap.get("cashMasterId"))[0]);
        }
        List<CashDetailVO> cashDetailList = cashService.getCashDetailListByMasterId(cashMasterId);
        outList.add(cashDetailList);

        CashMasterVO cashMasterVO = cashService.getCashMasterByMasterId(cashMasterId);
        outList.add(cashMasterVO);
        otherMap.put(REQUEST_SEND_OBJECT, outList);
        otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);

        return POP_TEMPLATE_PAGE;

    }

    @RequestMapping(method = RequestMethod.POST, params = "method=updateCashDetail", produces = "application/json;charset=utf-8")
    public String updateCashDetail(@RequestParam("method") String method, Model model
            , @RequestParam(value = "cashMasterId", required = true) Integer cashMasterId
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("updateCashDetail model:   " + model);
        System.out.println("updateCashDetail method:   " + method);
        System.out.println("updateCashDetail cashMasterId:   " + cashMasterId);

        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);

        List<Object> outList = new ArrayList<>();

        Integer cashDetailId = null;
        if (requestParameterMap.get("updateCashDetailId") != null) {
            if (!StringUtils.isEmpty(((String[]) requestParameterMap.get("updateCashDetailId"))[0])) {
                cashDetailId = Integer.parseInt(((String[]) requestParameterMap.get("updateCashDetailId"))[0]);
            }
        }
        Double diffPrice = 0d;
        if (requestParameterMap.get("updateDiffPrice") != null) {
            if (!StringUtils.isEmpty(((String[]) requestParameterMap.get("updateDiffPrice"))[0])) {
                diffPrice = Double.parseDouble(((String[]) requestParameterMap.get("updateDiffPrice"))[0]);
            }
        }
        String diffPriceNote = null;
        if (requestParameterMap.get("updateDiffPriceNote") != null) {
            diffPriceNote = ((String[]) requestParameterMap.get("updateDiffPriceNote"))[0];
        }
        if (requestParameterMap.get("cashMasterId") != null) {
            if (!StringUtils.isEmpty(((String[]) requestParameterMap.get("cashMasterId"))[0])) {
                cashMasterId = Integer.parseInt(((String[]) requestParameterMap.get("cashMasterId"))[0]);
            }
        }

        boolean isOK = cashService.updateCashDetail(cashDetailId, diffPrice, diffPriceNote);

        List<CashDetailVO> cashDetailList = cashService.getCashDetailListByMasterId(cashMasterId);
        outList.add(cashDetailList);

        CashMasterVO cashMasterVO = cashService.getCashMasterByMasterId(cashMasterId);
        outList.add(cashMasterVO);

        otherMap.put(REQUEST_SEND_OBJECT, outList);
        otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);

        return POP_TEMPLATE_PAGE;

    }

    @RequestMapping(method = RequestMethod.GET, params = "method=viewOrverList", produces = "application/json;charset=utf-8")
    public String viewOrverList(@RequestParam("method") String method, Model model
            , @RequestParam(value = "cashDetailId", required = true) Integer cashDetailId
            , @RequestParam(value = "billType", required = true) Integer billType
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("viewOrverList model:   " + model);
        System.out.println("viewOrverList method:   " + method);
        System.out.println("viewOrverList cashDetailId:   " + cashDetailId);

        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);

        List<Object> outList = new ArrayList<>();


        List<BillCycleEntity> billCycleList = cashService.getOverListByDetailId(cashDetailId);
        outList.add(billCycleList);

        String dispatch_page = DEFAULT_EDIT_DISPATCH_PAGE_OverListpage;
        if (2 == billType) {
            dispatch_page = DEFAULT_EDIT_DISPATCH_PAGE_OverGradeListpage; //級距型
        }
        otherMap.put(REQUEST_SEND_OBJECT, outList);
        otherMap.put(DISPATCH_PAGE, dispatch_page);

        sendObjToViewer(request, otherMap);

        return POP_TEMPLATE_PAGE;

    }


    @RequestMapping(method = RequestMethod.GET, params = "method=cancelOver", produces = "application/json;charset=utf-8")
    public String cancelOver(@RequestParam("method") String method, Model model
            , @RequestParam(value = "cashDetailId", required = true) Integer cashDetailId
            , @RequestParam(value = "cashMasterId", required = true) Integer cashMasterId
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("cancelOver model:   " + model);
        System.out.println("cancelOver method:   " + method);
        System.out.println("cancelOver masterId:   " + cashDetailId);

        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);

        List<Object> outList = new ArrayList<>();

        boolean isOK = cashService.transactionCancelOver(cashDetailId);

        List<CashDetailVO> cashDetailList = cashService.getCashDetailListByMasterId(cashMasterId);
        outList.add(cashDetailList);

        CashMasterVO cashMasterVO = cashService.getCashMasterByMasterId(cashMasterId);
        outList.add(cashMasterVO);

        otherMap.put(REQUEST_SEND_OBJECT, outList);
        otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);

        return POP_TEMPLATE_PAGE;

    }

    @RequestMapping(method = RequestMethod.GET, params = "method=cancelPrepay", produces = "application/json;charset=utf-8")
    public String cancelPrepay(@RequestParam("method") String method, Model model
            , @RequestParam(value = "cashDetailId", required = true) Integer cashDetailId
            , @RequestParam(value = "cashMasterId", required = true) Integer cashMasterId
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("cancelPrepay model:   " + model);
        System.out.println("cancelPrepay method:   " + method);
        System.out.println("cancelPrepay cashDetailId:   " + cashDetailId);

        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);

        List<Object> outList = new ArrayList<>();


        boolean isOK = cashService.cancelPrepay(cashDetailId);


        List<CashDetailVO> cashDetailList = cashService.getCashDetailListByMasterId(cashMasterId);
        outList.add(cashDetailList);

        CashMasterVO cashMasterVO = cashService.getCashMasterByMasterId(cashMasterId);
        outList.add(cashMasterVO);

        otherMap.put(REQUEST_SEND_OBJECT, outList);
        otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);

        return POP_TEMPLATE_PAGE;
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=delCashMaster", produces = "application/json;charset=utf-8")

    public  @ResponseBody
    String delCashMaster(@RequestParam("method") String method, Model model
            , @RequestParam(value = "cashMasterId", required = true) Integer cashMasterId
            , @RequestParam(value = "cashMaster", required = true) Integer cashMaster
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("delCashMaster model:   " + model);
        System.out.println("delCashMaster method:   " + method);
        System.out.println("delCashMaster cashMasterId:   " + cashMasterId);
        System.out.println("delCashMastery cashMaster:   " + cashMaster);
        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);


        String data = "success!!";
        cashMasterId = 0;
        try {

            cashService.delCashMaster(cashMasterId);

        } catch (Exception ex) {
            System.out.println(ex);
            data = " fail!!";
        }

        // otherMap.put(AJAX_JSON_OBJECT, pageMap);
        Gson gson = new Gson();
        return gson.toJson(data);

    }
}





















