package com.gate.web.servlets.backend.prepayReduct;

import com.gate.web.facades.CommissionServiceImp;
import com.gate.web.facades.PrepayDeductServiceImpl;
import com.gate.web.servlets.BaseServlet;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@WebServlet(urlPatterns = "/backendAdmin/prepayDeductAjaxServlet")
public class PrepayDeductAjaxServlet extends BaseServlet {
    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        List<Object> outList = new ArrayList<Object>();
        PrepayDeductServiceImpl serviceImp = new PrepayDeductServiceImpl();
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if(methodObj!=null) method = ((String[])requestParameterMap.get("method"))[0];
        Integer prepayDeductMasterId =Integer.parseInt (((String[])requestParameterMap.get("prepayDeductMasterId"))[0]);
        if(method.equals("stop")){ //經銷商方案暫停
            serviceImp.updateMasterStatus(prepayDeductMasterId, "N");
        } else if(method.equals("open")){ //經銷商方案啟用
            serviceImp.updateMasterStatus(prepayDeductMasterId, "Y");
        }
        otherMap.put(AJAX_JSON_OBJECT,"0");
        return null;
    }
}
