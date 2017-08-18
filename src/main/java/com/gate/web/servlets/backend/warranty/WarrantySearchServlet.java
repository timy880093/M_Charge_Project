package com.gate.web.servlets.backend.warranty;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.facades.CalCycleServiceImp;
import com.gate.web.facades.CashServiceImp;
import com.gate.web.facades.WarrantyServiceImp;
import com.gate.web.servlets.SearchServlet;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by emily on 2016/6/13.
 */
@WebServlet(urlPatterns = "/backendAdmin/warrantySearchServlet")
public class WarrantySearchServlet extends SearchServlet {

    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        CalCycleServiceImp calCycleService = new CalCycleServiceImp();
        WarrantyServiceImp warrantyService = new WarrantyServiceImp();

        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();
        if (method.equals("search")) {
            QuerySettingVO querySettingVO = new QuerySettingVO();
            Map pageMap = getData(requestParameterMap, querySettingVO, otherMap, "warrantySearchVO");
            otherMap.put(AJAX_JSON_OBJECT, pageMap);
            return null;
        }else{
            List userCompanyList = calCycleService.getUserCompanyList();
            outList.add(userCompanyList); //0.用戶清單

            List userDealerCompanyList = warrantyService.getUserDealerCompanyList();
            outList.add(userDealerCompanyList); //1.經銷商清單

            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, getDispatch_page());
            String[] returnList = {FORWARD_TYPE_F, TEMPLATE_PAGE};
            return returnList;
        }
    }

    public String getDispatch_page() {
        return "/backendAdmin/warranty/warranty_list.jsp";
    }


    @Override
    public Map doSearchData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        WarrantyServiceImp serviceImp = new WarrantyServiceImp();
        Map warrantyList = serviceImp.getWarrantyList(querySettingVO);
        return warrantyList;
    }

    @Override
    public Map doSearchDownloadData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        return null;
    }


}
