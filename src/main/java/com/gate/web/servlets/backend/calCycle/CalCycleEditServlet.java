package com.gate.web.servlets.backend.calCycle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gate.core.bean.BaseFormBean;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.gate.web.displaybeans.GiftVO;
import com.gate.web.facades.CalCycleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/backendAdmin/calCycleEditServlet")
@Controller

public class CalCycleEditServlet extends MvcBaseServlet {
    private static final String DEFAULT_EDIT_DISPATCH_PAGE = "/backendAdmin/calCycle/calCycle_editGift.jsp";
    private static final String SESSION_SEARCH_OBJ_NAME= "giftVO";
    @Autowired
    CalCycleService calCycleService;

    @RequestMapping(method= RequestMethod.POST)
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

    @RequestMapping(method = RequestMethod.GET, params = "method=editGift", produces = "application/json;charset=utf-8")
    public String editPrepay(@RequestParam("method") String method, Model model
            , @RequestParam(value="billId", required=true) Integer masterId
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
        sendObjToViewer(request, otherMap);

        List<Object> outList = new ArrayList<Object>();
        Integer billId = null;
        if (requestParameterMap.get("billId") != null) {
            billId = Integer.parseInt(((String[]) requestParameterMap.get("billId"))[0]);
        }
        String dispatch_page = "";

        if (billId != null) {
            GiftVO giftVO = calCycleService.findGiftByBillId(billId);
            outList.add(giftVO);
        }

        otherMap.put(REQUEST_SEND_OBJECT, outList);
        otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);

        return POP_TEMPLATE_PAGE;

    }

    @RequestMapping(method = RequestMethod.POST, params = "method=updateGift", produces = "application/json;charset=utf-8")
    public String updateGift(@RequestParam("method") String method, Model model
            , @RequestParam(value="billId", required=true) Integer masterId
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
        sendObjToViewer(request, otherMap);

        List<Object> outList = new ArrayList<Object>();
        Integer billId = null;
        Integer cntGift = null;
        if (requestParameterMap.get("billId") != null) {
            billId = Integer.parseInt(((String[]) requestParameterMap.get("billId"))[0]);
        }
        if (requestParameterMap.get("cntGift") != null) {
            cntGift = Integer.parseInt(((String[]) requestParameterMap.get("cntGift"))[0]);
        }

        if (billId != null) {
            calCycleService.updateCntGiftByBillId(billId, cntGift);
        }

        sendObjToViewer(request, otherMap);

        return POP_TEMPLATE_PAGE;

    }


}
