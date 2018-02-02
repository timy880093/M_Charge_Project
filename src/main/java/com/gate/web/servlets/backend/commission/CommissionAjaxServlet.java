package com.gate.web.servlets.backend.commission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.gate.web.facades.CommissionService;
import com.gate.web.servlets.BaseServlet;


@WebServlet(urlPatterns = "/backendAdmin/commissionAjaxServlet")
public class CommissionAjaxServlet extends BaseServlet {
	
	@Autowired
	CommissionService commissionService;
	
    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        List<Object> outList = new ArrayList<Object>();
        
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if(methodObj!=null) method = ((String[])requestParameterMap.get("method"))[0];
        Integer dealerCompanyId =Integer.parseInt (((String[])requestParameterMap.get("dealerCompanyId"))[0]);
        if(method.equals("stop") ){ //經銷商方案暫停
            commissionService.updateCommissionStatus(dealerCompanyId, 2);
        } else if(method.equals("open")){ //經銷商方案啟用
            commissionService.updateCommissionStatus(dealerCompanyId, 1);
        }
        otherMap.put(AJAX_JSON_OBJECT,"0");
        return null;
    }
}
