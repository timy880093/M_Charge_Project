package com.gate.web.servlets.backend.charge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.gate.web.facades.ChargeService;

//import com.gate.web.facades.CompanyServiceImp;

import com.gate.web.servlets.BaseServlet;

@WebServlet(urlPatterns = "/backendAdmin/chargeAjaxServlet")
public class ChargeAjaxServlet extends BaseServlet {
	
	@Autowired
	ChargeService chargeService;
	
    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        List<Object> outList = new ArrayList<Object>();
        
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if(methodObj!=null) method = ((String[])requestParameterMap.get("method"))[0];
        if(method.equals("closeCharge") ){ //暫停方案
            String type = ((String[])requestParameterMap.get("type"))[0];
            Integer chargeId =Integer.parseInt (((String[])requestParameterMap.get("chargeId"))[0]);
            chargeService.changeChargeModeStatus(type, chargeId, 4);       //暫停
            otherMap.put(AJAX_JSON_OBJECT,"0");
        } else if(method.equals("openCharge")){ //啟用方案
            String type = ((String[])requestParameterMap.get("type"))[0];
            Integer chargeId =Integer.parseInt (((String[])requestParameterMap.get("chargeId"))[0]);
            chargeService.changeChargeModeStatus(type, chargeId, 1);  //使用中
            otherMap.put(AJAX_JSON_OBJECT,"0");
        }
        return null;
    }
}
