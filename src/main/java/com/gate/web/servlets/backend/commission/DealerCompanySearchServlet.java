package com.gate.web.servlets.backend.commission;

import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.facades.CommissionService;
import com.gate.web.servlets.SearchServlet;

@WebServlet(urlPatterns = "/backendAdmin/dealerCompanySearchServlet")
public class DealerCompanySearchServlet extends SearchServlet {
	
	@Autowired
    CommissionService CommissionService;

    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        if (method.equals("search")) {
            QuerySettingVO querySettingVO = new QuerySettingVO();
            Map pageMap = getData(requestParameterMap, querySettingVO, otherMap,"dealerCompanyListSearchVO");
            otherMap.put(AJAX_JSON_OBJECT, pageMap);
            return null;
        } else {
            otherMap.put(DISPATCH_PAGE,getDispatch_page());
            String[] returnList = {FORWARD_TYPE_F, TEMPLATE_PAGE};
            return returnList;
        }

    }

    public String getDispatch_page() {
        return "/backendAdmin/commission/dealerCompany_list.jsp";
    }

    /**
     * AJAX 資料來源
     *
     * @param querySettingVO
     * @return
     * @throws Exception
     */
    public Map doSearchData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        Map dealerCompanyList = CommissionService.getDealerCompanyList(querySettingVO);
        return dealerCompanyList;
    }

    @Override
    public Map doSearchDownloadData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
