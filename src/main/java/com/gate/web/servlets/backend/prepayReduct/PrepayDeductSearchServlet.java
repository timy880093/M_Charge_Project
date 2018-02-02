package com.gate.web.servlets.backend.prepayReduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.facades.CalCycleService;
import com.gate.web.facades.PrepayDeductService;
import com.gate.web.servlets.SearchServlet;

@WebServlet(urlPatterns = "/backendAdmin/prepayDeductSearchServlet")
public class PrepayDeductSearchServlet extends SearchServlet {

	@Autowired
    PrepayDeductService prepayDeductService;
	
	@Autowired
	CalCycleService calCycleService;

    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        List<Object> outList = new ArrayList<Object>();
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        if (method.equals("search")) {
            QuerySettingVO querySettingVO = new QuerySettingVO();
            Map pageMap = getData(requestParameterMap, querySettingVO, otherMap,"prepayDeductListSearchVO");
            otherMap.put(AJAX_JSON_OBJECT, pageMap);
            return null;
        } else if(method.equals("createPdm")) { //新增使用預用金的用戶
            String data = "success!!";
            Integer exeCnt = 0;
            try{
                //新增使用預用金的用戶
                String companyId  = ((String[]) requestParameterMap.get("companyId"))[0];
                exeCnt = prepayDeductService.transactionCreatePdm(companyId);
                data += "  total counts: "+exeCnt+"";
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else {
            List userCompanyList = calCycleService.getUserCompanyList();
            outList.add(userCompanyList); //1.用戶下拉選單

            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE,getDispatch_page());
            String[] returnList = {FORWARD_TYPE_F, TEMPLATE_PAGE};
            return returnList;
        }

    }

    public String getDispatch_page() {
        return "/backendAdmin/prepayDeduct/prepayDeductCompany_list.jsp";
    }

    /**
     * AJAX 資料來源
     *
     * @param querySettingVO
     * @return
     * @throws Exception
     */
    public Map doSearchData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        Map prepayReductCompanyList = prepayDeductService.getPrepayDeductCompanyList(querySettingVO);
        return prepayReductCompanyList;
    }

    @Override
    public Map doSearchDownloadData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
