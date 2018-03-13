package com.gate.web.servlets.backend.brokerCompany;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.gate.utils.ExcelPoiWrapper;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.facades.BrokerCompanyService;
import com.gate.web.servlets.SearchServlet;

/**
 * Created by emily on 2016/2/4.
 */
@WebServlet(urlPatterns = "/backendAdmin/brokerCompanySearchServlet")
public class BrokerCompanySearchServlet extends SearchServlet {

    private static final String DOWNLOAD_FILE_NAME_BROKER ="broker_data";
    //private static final String TEMPLATE_EXCEL_DOWNLOAD_BROKER = SystemConfig.getInstance().getParameter("uploadTempPath") + "/tempFile"+"/broker_temp.xls";

    @Autowired
    BrokerCompanyService brokerCompanyService;
    
    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {

        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();

        if (method.equals("search")) {
            QuerySettingVO querySettingVO = new QuerySettingVO();
            Map pageMap = getData(requestParameterMap, querySettingVO, otherMap,"brokerCompanySearchVO");
            otherMap.put(AJAX_JSON_OBJECT, pageMap);
            return null;
        } else if(method.equals("excel")){
            String brokerType = ((String[]) requestParameterMap.get("brokerType"))[0]; //2.介紹人公司，3.裝機公司
            String brokerCompany = ((String[]) requestParameterMap.get("brokerCompany"))[0]; //值
            List<Map> excelBrokerCompanyList = brokerCompanyService.getExcelBrokerCompanyList(brokerType, brokerCompany);

            String filePath = this.getClass().getResource("/").getPath()+"/tempFile"+"/broker_temp.xls";
            
            //ExcelPoiWrapper excel= genBrokerCpToExcel(excelBrokerCompanyList, TEMPLATE_EXCEL_DOWNLOAD_BROKER);
            ExcelPoiWrapper excel= genBrokerCpToExcel(excelBrokerCompanyList, filePath);
            HttpServletResponse response = (HttpServletResponse) otherMap.get(RESPONSE);
            responseExcelFileToClient(excel, response, DOWNLOAD_FILE_NAME_BROKER);
            return null;
        }else {
            List brokerCp2List = brokerCompanyService.getBrokerCp2List();
            outList.add(brokerCp2List); //0. 介紹人公司
            List brokerCp3List = brokerCompanyService.getBrokerCp3List();
            outList.add(brokerCp3List); //1. 裝機公司

            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, getDispatch_page());
            String[] returnList = {FORWARD_TYPE_F, TEMPLATE_PAGE};

            return returnList;
        }
    }

    public String getDispatch_page() {
        return "/backendAdmin/brokerCompany/brokerCompany_list.jsp";
    }

    @Override
    public Map doSearchData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        Map brokerCompanyMap = brokerCompanyService.getBrokerCompanyList(querySettingVO);
        return brokerCompanyMap;
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
    private ExcelPoiWrapper genBrokerCpToExcel(List<Map> excelList, String tempPath) throws Exception {
    	System.out.println("tempPath:   "+tempPath);
        ExcelPoiWrapper excel = new ExcelPoiWrapper(tempPath);
        HashMap packageMap = new HashMap();
        excel.setWorkSheet(1);
        int baseRow=2;
        int index = 0;
        int packageIndex = 8;
        for(Map map:excelList){
            excel.copyRows(2, 11, 1, baseRow);

            excel.setValue(baseRow, index + 1, map.get("name")); //公司名稱
            excel.setValue(baseRow, index + 2, map.get("business_no")); //統編
            excel.setValue(baseRow, index + 3, map.get("real_start_date")); //實際起始日
            excel.setValue(baseRow, index + 4, map.get("real_end_date")); //實際結束日
            excel.setValue(baseRow, index + 5, map.get("package_name")); //方案名稱
            excel.setValue(baseRow, index + 6, map.get("dealer_company_name")); //經銷商
            excel.setValue(baseRow, index + 7, map.get("dealer_name")); //經銷商業務
            excel.setValue(baseRow, index + 8, map.get("broker_cp2")); //介紹公司
            excel.setValue(baseRow, index + 9, map.get("broker2")); //介紹人
            excel.setValue(baseRow, index + 10, map.get("broker_cp3")); //裝機公司
            excel.setValue(baseRow, index + 11, map.get("broker3"));//裝機人

            baseRow++;
        }
        return excel;
    }


}
