package com.gate.web.servlets.backend.company;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.facades.CompanyChargeService;
import com.gate.web.facades.CompanyService;
import com.gate.web.servlets.SearchServlet;


@WebServlet(urlPatterns = "/backendAdmin/companySearchServlet")
public class CompanySearchServlet extends SearchServlet {

	@Autowired
    CompanyService companyService;
	
	@Autowired
    CompanyChargeService companyChargeService;

    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {

        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        if (method.equals("search")) {
            QuerySettingVO querySettingVO = new QuerySettingVO();
            Map pageMap = getData(requestParameterMap, querySettingVO, otherMap,"companySearchVO");
            otherMap.put(AJAX_JSON_OBJECT, pageMap);
            return null;
        } else if(method.equals("continuePackage")){ //快過期合約(請自行先搜尋)續約(會出帳)
            String data = "success!!";

            try{
                String almostOut = ((String[]) requestParameterMap.get("almostOut"))[0]; //帳單年月
                companyChargeService.continuePackage(almostOut,10);
            }catch(Exception ex){
                ex.printStackTrace();
                data = " fail!!";
            }

            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else {
            List<Object> outList = new ArrayList<Object>();

            //計算年月:預設每月15日後算上個月的超額
            Calendar now = Calendar.getInstance();
            Integer day = now.get(Calendar.DAY_OF_MONTH);
            if(day<15){ //每月15日後算上個月的超額
                now.add(Calendar.MONTH, -2);
            }else{ //每月15日後算上個月的超額
                now.add(Calendar.MONTH, -1);
            }
            String year = "" + now.get(Calendar.YEAR);
            String month = "";
            if ((now.get(Calendar.MONTH) + 1)<10){
                month = "0" + (now.get(Calendar.MONTH) + 1);
            }else{
                month = "" + (now.get(Calendar.MONTH) + 1);
            }
            outList.add(year + month);

            //請先作完YYYYMM前的快到期合約續約後，才可作YYYYMM後的超額計算
            now.add(Calendar.MONTH, 3); //作快到期合約的年月
            String yearAlmost = "" + now.get(Calendar.YEAR);
            String monthAlmost = "";
            if ((now.get(Calendar.MONTH) + 1)<10){
                monthAlmost = "0" + (now.get(Calendar.MONTH) + 1);
            }else{
                monthAlmost = "" + (now.get(Calendar.MONTH) + 1);
            }
            outList.add(yearAlmost + monthAlmost);


            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE,getDispatch_page());
            String[] returnList = {FORWARD_TYPE_F, TEMPLATE_PAGE};
            return returnList;
        }

    }

    public String getDispatch_page() {
        return "/backendAdmin/companyCharge/company_list.jsp";
    }

    /**
     * AJAX 資料來源
     *
     * @param querySettingVO
     * @return
     * @throws Exception
     */
    public Map doSearchData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        Map companyList = companyService.getCompanyList(querySettingVO);
        return companyList;
    }

    @Override
    public Map doSearchDownloadData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
