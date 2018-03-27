package com.gate.web.servlets.backend.commission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gate.core.bean.BaseFormBean;
import com.gate.web.displaybeans.GiftVO;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.UserEntity;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import com.gate.utils.MapBeanConverterUtils;
import com.gate.web.displaybeans.DealerCompanyVO;
import com.gate.web.displaybeans.DealerVO;
import com.gate.web.facades.CommissionService;
import com.gate.web.formbeans.DealerCompanyBean;
import com.gate.web.servlets.backend.common.BackendPopTemplateServlet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping( "/backendAdmin/dealerCompanyEditServlet")
@Controller

public class DealerCompanyEditServlet extends MvcBaseServlet {

    private static final String DEFAULT_EDIT_DISPATCH_PAGE = "/backendAdmin/commission/edit_dealerCompany.jsp";
    private static final String SESSION_SEARCH_OBJ_NAME = "DealerCompanyVO";
    @Autowired
    CommissionService commissionService;

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
    public String edit(@RequestParam("method") String method, Model model
            , @RequestParam(value = "dealerCompanyId", required = true) String dealerCompanyId
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("editPrepay model:   " + model);
        System.out.println("editPrepay method:   " + method);
        System.out.println("editPrepay masterId:   " + dealerCompanyId);


        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);

        List<Object> outList = new ArrayList<Object>();
        DealerCompanyVO vo = commissionService.getDealerCompanyByDealerCompanyId(Integer.parseInt(dealerCompanyId));
        outList.add(vo);

        List<DealerVO> dealerList = commissionService.getDealerByDealerCompanyId(Integer.parseInt(dealerCompanyId));
        outList.add(dealerList);

        if(method.equals("edit")){
            outList.add("edit");
        }

        otherMap.put(REQUEST_SEND_OBJECT, outList);
        otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);

        return POP_TEMPLATE_PAGE;

    }

    @RequestMapping(method = RequestMethod.POST, params = "method=insert", produces = "application/json;charset=utf-8")
    public String insert(@RequestParam("method") String method, Model model
            , @RequestParam(value = "exist", required = true) String update
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("insert model:   " + model);
        System.out.println("insert method:   " + method);
        System.out.println("insert exist:   " + update);

        Gson gson = new Gson();
        String errorMessage = null;
        String jsonString = gson.toJson("error");



        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);

        DealerCompanyBean bean = new DealerCompanyBean();
        MapBeanConverterUtils.mapToBean(requestParameterMap, bean);
        commissionService.insertDealerCompany(bean,user.getUserId());

        sendObjToViewer(request, otherMap);

        return POP_TEMPLATE_PAGE;

    }
}




