package com.gate.web.servlets.backend.charge;

import com.gate.config.SystemConfig;
import com.gate.utils.TimeUtils;
import com.gate.web.displaybeans.ChargeModeCycleVO;
import com.gate.web.facades.ChargeServiceImp;

//import com.gate.web.facades.CompanyServiceImp;

import com.gate.web.servlets.BaseServlet;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/backendAdmin/chargeAjaxServlet")
public class ChargeAjaxServlet extends BaseServlet {
    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        List<Object> outList = new ArrayList<Object>();
        ChargeServiceImp serviceImp = new ChargeServiceImp();
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if(methodObj!=null) method = ((String[])requestParameterMap.get("method"))[0];
        if(method.equals("closeCharge") ){ //暫停方案
            String type = ((String[])requestParameterMap.get("type"))[0];
            Integer chargeId =Integer.parseInt (((String[])requestParameterMap.get("chargeId"))[0]);
            serviceImp.changeChargeModeStatus(type, chargeId, 4);       //暫停
            otherMap.put(AJAX_JSON_OBJECT,"0");
        } else if(method.equals("openCharge")){ //啟用方案
            String type = ((String[])requestParameterMap.get("type"))[0];
            Integer chargeId =Integer.parseInt (((String[])requestParameterMap.get("chargeId"))[0]);
            serviceImp.changeChargeModeStatus(type, chargeId, 1);  //使用中
            otherMap.put(AJAX_JSON_OBJECT,"0");
        }
        return null;
    }
}
