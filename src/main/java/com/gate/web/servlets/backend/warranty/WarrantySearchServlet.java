package com.gate.web.servlets.backend.warranty;

import com.gate.utils.ExcelPoiWrapper;
import com.gate.web.beans.CommissionLog;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.facades.CalCycleServiceImp;
import com.gate.web.facades.CashServiceImp;
import com.gate.web.facades.WarrantyServiceImp;
import com.gate.web.servlets.SearchServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by emily on 2016/6/13.
 */
@WebServlet(urlPatterns = "/backendAdmin/warrantySearchServlet")
public class WarrantySearchServlet extends SearchServlet {
    private static final String DOWNLOAD_FILE_NAME_WARRANTY ="warranty_temp";
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
        }else if(method.equals("exportWar")) { //匯出佣金資料
            String data = "success!! ";
            try {
                String warranty = ((String[]) requestParameterMap.get("warranty"))[0]; //commission_log_id
                List<Map> WarrantyList = warrantyService.exportWar(warranty);
                String filePath = this.getClass().getResource("/").getPath() + "/tempFile" + "/warranty_temp.xls";
                ExcelPoiWrapper excel = genWarrantyToExcel(WarrantyList, filePath);
                HttpServletResponse response = (HttpServletResponse) otherMap.get(RESPONSE);
                responseExcelFileToClient(excel, response, DOWNLOAD_FILE_NAME_WARRANTY);
            } catch (Exception e) {
                e.printStackTrace();
                data = "error";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
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
    private void responseExcelFileToClient(ExcelPoiWrapper excel, HttpServletResponse response,String fileName)
            throws Exception {
        response.setContentType("text/plain");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName+".xls");
        excel.getWorkBook().write(response.getOutputStream());
        response.getOutputStream().close();
    }



    //匯出發票資料Excel
    private ExcelPoiWrapper genWarrantyToExcel(List<Map> excelList, String tempPath) throws Exception {
        System.out.println("genWarrantyToExcel tempPath :  "+tempPath);
        ExcelPoiWrapper excel = new ExcelPoiWrapper(tempPath);
        HashMap packageMap = new HashMap();
        excel.setWorkSheet(1);
        int baseRow=2;
        int index = 0;
        for(int j=0; j<excelList.size(); j++){

            Map map = excelList.get(j);

                      List details = (List)map.get("detail");

            if(j != 0){
                excel.copyRows(1, 10, 1, baseRow);
                excel.setValue(baseRow, index + 1, "發票機序號");
                excel.setValue(baseRow, index + 2, "用戶名稱");
                excel.setValue(baseRow, index + 3, "經銷商名稱");
                excel.setValue(baseRow, index + 4, "出貨狀態");
                excel.setValue(baseRow, index + 5, "保固起日");
                excel.setValue(baseRow, index + 6, "保固迄日");
                excel.setValue(baseRow, index + 7, "是否延長保固");
                excel.setValue(baseRow, index + 8, "型號");
                excel.setValue(baseRow, index + 9, "備註");
                excel.setValue(baseRow, index + 10, "狀態");
                        baseRow++;
            }

            for(int i=0; i<details.size(); i++){
                Map detailMap = (Map)details.get(i);

                excel.copyRows(2, 10, 1, baseRow);
                excel.setValue(baseRow, index + 1, detailMap.get("WarrantyNo")); //所屬經銷商
                excel.setValue(baseRow, index + 2, detailMap.get("dealer_company_name")); //入帳時間起
                excel.setValue(baseRow, index + 3, detailMap.get("dealer_company_id")); //入帳時間迄
                excel.setValue(baseRow, index + 4, detailMap.get("only_ship"));//佣金類型
                excel.setValue(baseRow, index + 5, detailMap.get("start_date"));//佣金比例
                excel.setValue(baseRow, index + 6, detailMap.get("end_date"));//佣金付款狀態
                excel.setValue(baseRow, index + 7, detailMap.get("extend")); //用戶名稱
                excel.setValue(baseRow, index + 8, detailMap.get("model")); //是否為首次申請
                excel.setValue(baseRow, index + 9, detailMap.get("note")); //統編
                excel.setValue(baseRow, index + 10, detailMap.get("status")); //繳費類型
                baseRow++;
            }

//            excel.copyRows(2, 17, 1, baseRow);
//            excel.setValue(baseRow, index + 15, master.getInAmount()); //入帳總金額(含稅)
//            excel.setValue(baseRow, index + 17, master.getCommissionAmount());//佣金總金額
//            baseRow++;

        }

        return excel;
    }





















}
