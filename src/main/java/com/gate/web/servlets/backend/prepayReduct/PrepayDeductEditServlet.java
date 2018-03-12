package com.gate.web.servlets.backend.prepayReduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gate.utils.MapBeanConverterUtils;
import com.gate.web.facades.PrepayDeductService;
import com.gate.web.facades.PrepayDeductServiceImpl;
import com.gate.web.servlets.MvcBaseServlet;
import com.gate.web.servlets.backend.common.BackendPopTemplateServlet;
import com.gateweb.charge.model.PrepayDeductMasterEntity;
import com.gateweb.charge.model.PrepayDetailEntity;

/**
 * Created by emily on 2017/5/23.
 */

@RequestMapping("/backendAdmin/prepayDeductEditServlet")
@Controller
public class PrepayDeductEditServlet extends MvcBaseServlet {
	
	private static final String DEFAULT_EDIT_DISPATCH_PAGE = "/backendAdmin/prepayDeduct/edit_prepay.jsp";
	private static final String DEFAULT_SEARCH_LIST_DISPATCH_PAGE = "/backendAdmin/prepayDeduct/prepayDeductCompany_list.jsp";
	private static final String DEFAULT_CREATE_DISPATCH_PAGE = "/backendAdmin/prepayDeduct/item_create.jsp";

	@Autowired
    PrepayDeductService prepayDeductService;
	
    @Override
    public void doSomething(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();
        if (method.equals("editPrepay")) {  //檢視預繳清單
            String strMasterId = ((String[]) requestParameterMap.get("masterId"))[0];
            Integer masterId = Integer.parseInt(strMasterId);
            PrepayDeductMasterEntity master = prepayDeductService.getPrepayDeductMaster(masterId);
            outList.add(master);
            List<Map> histMap = prepayDeductService.getPrepayDetailHisByCompany(master.getCompanyId());
            outList.add(histMap);
            otherMap.put("REQUEST_SEND_OBJECT", outList);
            otherMap.put(DISPATCH_PAGE, getDispatch_page());
        } else if (method.equals("viewDeduct")) {  //查看扣抵清單
            String strMasterId = ((String[]) requestParameterMap.get("masterId"))[0];
            Integer masterId = Integer.parseInt(strMasterId);
            PrepayDeductMasterEntity master = prepayDeductService.getPrepayDeductMaster(masterId);
            outList.add(master);
            List<Map> histMap = prepayDeductService.getDeductDetailHisByCompany(master.getCompanyId());
            outList.add(histMap);
            otherMap.put("REQUEST_SEND_OBJECT", outList);
            otherMap.put(DISPATCH_PAGE, getDispatch_page2());
        } else if (method.equals("insertPrepay")) {  //新增一筆預繳清單
            PrepayDetailEntity entity = new PrepayDetailEntity();
            MapBeanConverterUtils.mapToBean(requestParameterMap, entity);
            prepayDeductService.transactionInsertPrepayDetail(entity);
        }
    }

    public String getDispatch_page() {
        return "/backendAdmin/prepayDeduct/edit_prepay.jsp";
    }

    public String getDispatch_page2() {
        return "/backendAdmin/prepayDeduct/edit_deduct.jsp";
    }
}
