package com.gate.web.servlets.backend.warranty;

import static com.gate.utils.MapBeanConverterUtils.mapToBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gate.core.bean.BaseFormBean;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.gate.web.facades.CalCycleService;
import com.gate.web.facades.WarrantyService;
import com.gate.web.formbeans.WarrantyBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/backendAdmin/warrantyEditServlet")
@Controller
public class WarrantyEditServlet extends MvcBaseServlet {

    private static final String DEFAULT_EDIT_DISPATCH_PAGE = "/backendAdmin/warranty/warranty_edit.jsp";
    private static final String SESSION_SEARCH_OBJ_NAME = "warrantySearchVO";


    @Autowired
    CalCycleService calCycleService;

    @Autowired
    WarrantyService warrantyService;


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
            , @RequestParam(value = "warrantyId", required = true) Integer companyId
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("update model:   " + model);
        System.out.println("update method:   " + method);
        System.out.println("update companyId:   " + companyId);



        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);

        List<Object> outList = new ArrayList<Object>();

        List userCompanyList = calCycleService.getUserCompanyList();
        outList.add(userCompanyList); //0.用戶清單

        List userDealerCompanyList = warrantyService.getUserDealerCompanyList();
        outList.add(userDealerCompanyList); //1.經銷商清單
        otherMap.put(REQUEST_SEND_OBJECT, outList);
        otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
        WarrantyBean warrantyBean = new WarrantyBean();
        mapToBean(requestParameterMap, warrantyBean);
        warrantyService.updateWarranty(warrantyBean);
        otherMap.put(AJAX_JSON_OBJECT, "success");
        sendObjToViewer(request, otherMap);
        return POP_TEMPLATE_PAGE;
    }



    @RequestMapping(method = RequestMethod.GET, params = "method=update", produces = "application/json;charset=utf-8")
    public String editPrepay(@RequestParam("method") String method, Model model
            , @RequestParam(value = "warrantyId", required = true) Integer companyId
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("update model:   " + model);
        System.out.println("update method:   " + method);
        System.out.println("update companyId:   " + companyId);



        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);

        List<Object> outList = new ArrayList<Object>();

        List userCompanyList = calCycleService.getUserCompanyList();
        outList.add(userCompanyList); //0.用戶清單

        List userDealerCompanyList = warrantyService.getUserDealerCompanyList();
        outList.add(userDealerCompanyList); //1.經銷商清單
        otherMap.put(REQUEST_SEND_OBJECT, outList);
        otherMap.put(DISPATCH_PAGE, DEFAULT_EDIT_DISPATCH_PAGE);
        WarrantyBean warrantyBean = new WarrantyBean();
        mapToBean(requestParameterMap, warrantyBean);
        warrantyService.updateWarranty(warrantyBean);
        otherMap.put(AJAX_JSON_OBJECT, "success");
        sendObjToViewer(request, otherMap);
        return POP_TEMPLATE_PAGE;
    }

    }
