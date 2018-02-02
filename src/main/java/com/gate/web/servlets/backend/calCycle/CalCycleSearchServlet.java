package com.gate.web.servlets.backend.calCycle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.facades.CalCycleService;
import com.gate.web.facades.CalCycleServiceImp;
import com.gate.web.servlets.SearchServlet;

@WebServlet(urlPatterns = "/backendAdmin/calCycleSearchServlet")
public class CalCycleSearchServlet extends SearchServlet {

	@Autowired
	CalCycleService calCycleService;
	
    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {

        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();
        if (method.equals("search")) {
            QuerySettingVO querySettingVO = new QuerySettingVO();
            Map pageMap = getData(requestParameterMap, querySettingVO, otherMap, "calCycleSearchVO");
            otherMap.put(AJAX_JSON_OBJECT, pageMap);
            return null;
        } else if(method.equals("calOverYM")) { //計算超額-批次
            String data = "success!!";
            Integer exeCnt = 0;
            try{
                //計算超額-批次
                String calYM  = ((String[]) requestParameterMap.get("calYM"))[0];
                exeCnt = calCycleService.calBatchOver(calYM);
                data += "  total counts: "+exeCnt+"";
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else if(method.equals("calOver")){ //計算超額-多筆
            String data = "success!!";
            Integer exeCnt = 0;
            try{
                //計算超額
                String calOverAry = ((String[]) requestParameterMap.get("calOverAry"))[0]; //帳單年月
                exeCnt = calCycleService.calOver(calOverAry);
                data += "  total counts: "+exeCnt+"";
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else if (method.equals("calOverToCash")){ //計算超額-任選幾筆，出帳
            String data = "success!!";
            Integer exeCnt = 0;
            try{
                //計算超額
                String calYM  = ((String[]) requestParameterMap.get("calYM"))[0];
                String companyId = ((String[]) requestParameterMap.get("userCompanyId"))[0];
                String calOverAry = ((String[]) requestParameterMap.get("calOverAry"))[0]; //帳單年月
                calCycleService.calOverToCash(calYM, Integer.parseInt(companyId), calOverAry);
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else if(method.equals("emailYM")){ //寄E-mail(批次,請選擇計算年月)
            String data = "success!!";
            Integer exeCnt = 0;
            try{
                String calYM  = ((String[]) requestParameterMap.get("calYM"))[0];
                exeCnt = calCycleService.sendOverMailYM(calYM);
                data += "  total counts: "+exeCnt+"";
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else if (method.equals("email")){ //計email(多筆)
            String data = "success!!";
            Integer exeCnt = 0;
            try{
                String calOverAry = ((String[]) requestParameterMap.get("calOverAry"))[0]; //帳單年月
                exeCnt = calCycleService.sendOverMail(calOverAry);
                data += "  total counts: "+exeCnt+"";
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else{
            List ymList = calCycleService.getYM();
            outList.add(ymList); //0. 計算年月下拉選單
            List userCompanyList = calCycleService.getUserCompanyList();
            outList.add(userCompanyList); //1.用戶下拉選單

            //2.計算年月:預設每月15日後算上個月的超額
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
            otherMap.put(DISPATCH_PAGE, getDispatch_page());
            String[] returnList = {FORWARD_TYPE_F, TEMPLATE_PAGE};
            return returnList;
        }
    }

    public String getDispatch_page() {
        return "/backendAdmin/calCycle/calCycle_list.jsp";
    }

    /**
     * AJAX 資料來源
     *
     * @param querySettingVO
     * @return
     * @throws Exception
     */
    public Map doSearchData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        
        Map giftList = calCycleService.getBillCycleList(querySettingVO);
        return giftList;
    }

    @Override
    public Map doSearchDownloadData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
