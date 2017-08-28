package com.gate.web.servlets.backend.commissionLog;

import com.gate.config.SystemConfig;
import com.gate.utils.ExcelPoiWrapper;
import com.gate.web.beans.CommissionLog;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.facades.CommissionLogServiceImp;
import com.gate.web.servlets.SearchServlet;
import dao.CommissionLogEntity;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(urlPatterns = "/backendAdmin/commissionLogSearchServlet")
public class CommissionLogSearchServlet extends SearchServlet {
    private static final String DOWNLOAD_FILE_NAME_COMMISSION ="commission_temp";
    //private static final String TEMPLATE_EXCEL_DOWNLOAD_COMMISSION = SystemConfig.getInstance().getParameter("uploadTempPath") + "//tempFile"+"//commission_temp.xls";

    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        CommissionLogServiceImp serviceImp = new CommissionLogServiceImp();

        Object methodObj = requestParameterMap.get("method");
        String method = "";
        List<Object> outList = new ArrayList<Object>();
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        if (method.equals("search")) {
            QuerySettingVO querySettingVO = new QuerySettingVO();
            Map pageMap = getData(requestParameterMap, querySettingVO, otherMap,"commissionLogListSearchVO");
            otherMap.put(AJAX_JSON_OBJECT, pageMap);
            return null;
        } else if(method.equals("calCommission")){ //計算佣金
            String data="calculate success!! ";
            try{
                String dealerCompany = null;
                String inDateS = null;
                String inDateE = null;
                if(null != requestParameterMap.get("dealerCompany")){
                    dealerCompany = ((String[]) requestParameterMap.get("dealerCompany"))[0];
                }
                if(null != requestParameterMap.get("inDateS")){
                    inDateS = ((String[]) requestParameterMap.get("inDateS"))[0];
                }
                if(null != requestParameterMap.get("inDateE")){
                    inDateE = ((String[]) requestParameterMap.get("inDateE"))[0];
                }
                serviceImp.calCommission(dealerCompany, inDateS, inDateE);
            } catch (Exception e) {
                e.printStackTrace();
                data = "calculate error";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else if(method.equals("payCommission")){ //佣金付款
            String data="success!! ";
            try{
                String commissionLog = ((String[]) requestParameterMap.get("commissionLog"))[0]; //commission_log_id
                serviceImp.payCommission(commissionLog);
            } catch (Exception e) {
                e.printStackTrace();
                data = "error";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        }else if(method.equals("exportCom")){ //匯出佣金資料
            String data="success!! ";
            try{
                String commissionLog = ((String[]) requestParameterMap.get("commissionLog"))[0]; //commission_log_id
                List<Map> commissionLogList = serviceImp.exportCom(commissionLog);
                String filePath = this.getClass().getResource("/").getPath()+"/tempFile"+"/commission_temp.xls";
                ExcelPoiWrapper excel= genCommissionLogToExcel(commissionLogList, filePath);
                HttpServletResponse response = (HttpServletResponse) otherMap.get(RESPONSE);

                responseExcelFileToClient(excel, response, DOWNLOAD_FILE_NAME_COMMISSION);
            } catch (Exception e) {
                e.printStackTrace();
                data = "error";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        }else{
            List dealerCpList = serviceImp.getDealerCompanyList();
            outList.add(dealerCpList);

            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE,getDispatch_page());
            String[] returnList = {FORWARD_TYPE_F, TEMPLATE_PAGE};
            return returnList;
        }

    }

    public String getDispatch_page() {
        return "/backendAdmin/commissionLog/commissionLog_list.jsp";
    }

    /**
     * AJAX 資料來源
     *
     * @param querySettingVO
     * @return
     * @throws Exception
     */
    public Map doSearchData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        CommissionLogServiceImp serviceImp = new CommissionLogServiceImp();
        Map commissionList = serviceImp.getCommissionMasterList(querySettingVO);
        return commissionList;
    }

    @Override
    public Map doSearchDownloadData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
    private ExcelPoiWrapper genCommissionLogToExcel(List<Map> excelList, String tempPath) throws Exception {
    	System.out.println("genCommissionLogToExcel tempPath :  "+tempPath);
        ExcelPoiWrapper excel = new ExcelPoiWrapper(tempPath);
        HashMap packageMap = new HashMap();
        excel.setWorkSheet(1);
        int baseRow=2;
        int index = 0;
        for(int j=0; j<excelList.size(); j++){

            Map map = excelList.get(j);

            CommissionLog master = (CommissionLog)map.get("master");
            List details = (List)map.get("detail");

            if(j != 0){
                excel.copyRows(1, 15, 1, baseRow);
                excel.setValue(baseRow, index + 1, "所屬經銷商");
                excel.setValue(baseRow, index + 2, "入帳時間起");
                excel.setValue(baseRow, index + 3, "入帳時間迄");
                excel.setValue(baseRow, index + 4, "佣金類型");
                excel.setValue(baseRow, index + 5, "佣金比例");
                excel.setValue(baseRow, index + 6, "佣金付款狀態");
                excel.setValue(baseRow, index + 7, "用戶名稱");
                excel.setValue(baseRow, index + 8, "繳費類型");
                excel.setValue(baseRow, index + 9, "方案名稱");
                excel.setValue(baseRow, index + 10, "計算年月");
                excel.setValue(baseRow, index + 11, "出帳年月");
                excel.setValue(baseRow, index + 12, "入帳時間");
                excel.setValue(baseRow, index + 13, "入帳金額(含稅)");
                excel.setValue(baseRow, index + 14, "出入帳金額是否相同");
                excel.setValue(baseRow, index + 15, "佣金金額");
                baseRow++;
            }


            for(int i=0; i<details.size(); i++){
                Map detailMap = (Map)details.get(i);

                excel.copyRows(2, 15, 1, baseRow);
                excel.setValue(baseRow, index + 1, master.getDealerCompanyName()); //所屬經銷商
                excel.setValue(baseRow, index + 2, master.getInDateStart()); //入帳時間起
                excel.setValue(baseRow, index + 3, master.getInDateEnd()); //入帳時間迄
                excel.setValue(baseRow, index + 4, master.getStrCommissionType());//佣金類型
                excel.setValue(baseRow, index + 5, master.getStrMainPercent());//佣金比例
                excel.setValue(baseRow, index + 6, master.getStrIsPaid());//佣金付款狀態
                excel.setValue(baseRow, index + 7, detailMap.get("name")); //用戶名稱
                excel.setValue(baseRow, index + 8, formatCashType((Integer)detailMap.get("cash_type"))); //繳費類型
                excel.setValue(baseRow, index + 9, detailMap.get("package_name")); //方案名稱
                excel.setValue(baseRow, index + 10, detailMap.get("cal_ym")); //計算年月
                excel.setValue(baseRow, index + 11, detailMap.get("out_ym")); //出帳年月
                excel.setValue(baseRow, index + 12, detailMap.get("in_date")); //入帳時間
                excel.setValue(baseRow, index + 13, ((BigDecimal)detailMap.get("tax_inclusive_price")).doubleValue()); //入帳金額(含稅)
                excel.setValue(baseRow, index + 14, isInoutMoneyUnmatch((String) detailMap.get("is_inout_money_unmatch"))); //cash_master的出入帳金額是否相同
                excel.setValue(baseRow, index + 15, ((BigDecimal)detailMap.get("commission_amount")).doubleValue());//佣金金額
                baseRow++;
            }

            excel.copyRows(2, 15, 1, baseRow);
            excel.setValue(baseRow, index + 13, master.getInAmount()); //入帳總金額(含稅)
            excel.setValue(baseRow, index + 15, master.getCommissionAmount());//佣金總金額
            baseRow++;

        }

        return excel;
    }

    //cash_master的出入帳金額是否相同
    private String isInoutMoneyUnmatch(String value){
        String result = "相同";
        if("1".equals(value)){
            result = "不相同";
        }
        return result;
    }

    //計費類型 1.月租2.月租超額3.代印代計4.加值型服務5.儲值
    private String  formatCashType(Integer cashType){
        String result = "";
        switch (cashType) {
            case 1:
                result = "月租預繳";
                break;
            case 2:
                result = "超額";
                break;
            case 3:
                result = "代印代計";
                break;
            case 4:
                result = "加值型服務";
                break;
        }
        return result;
    }

}
