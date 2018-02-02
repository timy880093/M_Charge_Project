package com.gate.web.servlets.backend.prepayReduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.gate.web.facades.PrepayDeductService;
import com.gate.web.servlets.BaseServlet;


@WebServlet(urlPatterns = "/backendAdmin/prepayDeductAjaxServlet")
public class PrepayDeductAjaxServlet extends BaseServlet {
	
	@Autowired
    PrepayDeductService prepayDeductService;

    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        List<Object> outList = new ArrayList<Object>();
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if(methodObj!=null) method = ((String[])requestParameterMap.get("method"))[0];
        Integer prepayDeductMasterId =Integer.parseInt (((String[])requestParameterMap.get("prepayDeductMasterId"))[0]);
        if(method.equals("stop")){ //經銷商方案暫停
        		prepayDeductService.updateMasterStatus(prepayDeductMasterId, "N");
        } else if(method.equals("open")){ //經銷商方案啟用
        		prepayDeductService.updateMasterStatus(prepayDeductMasterId, "Y");
        }
        otherMap.put(AJAX_JSON_OBJECT,"0");
        return null;
    }
}
