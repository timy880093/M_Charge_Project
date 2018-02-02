package com.gate.web.servlets.backend.warranty;

import static com.gate.utils.MapBeanConverterUtils.mapToBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.gate.web.displaybeans.WarrantyVO;
import com.gate.web.facades.CalCycleService;
import com.gate.web.facades.WarrantyService;
import com.gate.web.formbeans.WarrantyBean;
import com.gate.web.servlets.backend.common.BackendPopTemplateServlet;

@WebServlet(urlPatterns = "/backendAdmin/warrantyEditServlet")
public class WarrantyEditServlet extends BackendPopTemplateServlet {
	
	@Autowired
	CalCycleService calCycleService;
	
	@Autowired
    WarrantyService warrantyService;

    @Override
    public void doSomething(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        

        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();

        List userCompanyList = calCycleService.getUserCompanyList();
        outList.add(userCompanyList); //0.用戶清單

        List userDealerCompanyList = warrantyService.getUserDealerCompanyList();
        outList.add(userDealerCompanyList); //1.經銷商清單

        if(method.equals("create")) { //進入新增畫面
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, getDispatch_page());
        } else if(method.equals("edit")){ //進入編輯畫面
            String[] values = (String[]) requestParameterMap.get("warrantyId");
            WarrantyVO warrantyVO = warrantyService.findWarrantyByWarrantyId(Integer.valueOf(values[0]));
            outList.add(warrantyVO);
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, getDispatch_page());
        } else if(method.equals("update")){ //動作:新增或修改
            WarrantyBean warrantyBean = new WarrantyBean();
            mapToBean(requestParameterMap, warrantyBean);
            warrantyService.updateWarranty(warrantyBean);
            otherMap.put(AJAX_JSON_OBJECT, "success");
        }
    }

    public String getDispatch_page() {
        return "/backendAdmin/warranty/warranty_edit.jsp";
    }
}
