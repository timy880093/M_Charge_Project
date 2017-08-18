package com.gate.web.servlets.backend.prepayReduct;

import com.gate.utils.MapBeanConverterUtils;
import com.gate.web.displaybeans.PrepayDetailVO;
import com.gate.web.facades.PrepayDeductServiceImpl;
import com.gate.web.servlets.backend.common.BackendPopTemplateServlet;
import dao.PrepayDeductMasterEntity;
import dao.PrepayDetailEntity;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by emily on 2017/5/23.
 */

@WebServlet(urlPatterns = "/backendAdmin/prepayDeductEditServlet")
public class PrepayDeductEditServlet extends BackendPopTemplateServlet {

    @Override
    public void doSomething(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        PrepayDeductServiceImpl serviceImp = new PrepayDeductServiceImpl();
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();
        if (method.equals("editPrepay")) {  //檢視預繳清單
            String strMasterId = ((String[]) requestParameterMap.get("masterId"))[0];
            Integer masterId = Integer.parseInt(strMasterId);
            PrepayDeductMasterEntity master = serviceImp.getPrepayDeductMaster(masterId);
            outList.add(master);
            List<Map> histMap = serviceImp.getPrepayDetailHisByCompany(master.getCompanyId());
            outList.add(histMap);
            otherMap.put("REQUEST_SEND_OBJECT", outList);
            otherMap.put(DISPATCH_PAGE, getDispatch_page());
        } else if (method.equals("viewDeduct")) {  //查看扣抵清單
            String strMasterId = ((String[]) requestParameterMap.get("masterId"))[0];
            Integer masterId = Integer.parseInt(strMasterId);
            PrepayDeductMasterEntity master = serviceImp.getPrepayDeductMaster(masterId);
            outList.add(master);
            List<Map> histMap = serviceImp.getDeductDetailHisByCompany(master.getCompanyId());
            outList.add(histMap);
            otherMap.put("REQUEST_SEND_OBJECT", outList);
            otherMap.put(DISPATCH_PAGE, getDispatch_page2());
        } else if (method.equals("insertPrepay")) {  //新增一筆預繳清單
            PrepayDetailEntity entity = new PrepayDetailEntity();
            MapBeanConverterUtils.mapToBean(requestParameterMap, entity);
            serviceImp.transactionInsertPrepayDetail(entity);
        }
    }

    public String getDispatch_page() {
        return "/backendAdmin/prepayDeduct/edit_prepay.jsp";
    }

    public String getDispatch_page2() {
        return "/backendAdmin/prepayDeduct/edit_deduct.jsp";
    }
}
