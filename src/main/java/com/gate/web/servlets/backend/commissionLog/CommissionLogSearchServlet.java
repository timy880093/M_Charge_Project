package com.gate.web.servlets.backend.commissionLog;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gate.core.bean.BaseFormBean;
import com.gate.web.exceptions.FormValidationException;
import com.gate.web.exceptions.ReturnPathException;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.UserEntity;
import com.gateweb.einv.exception.EinvSysException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import com.gate.utils.ExcelPoiWrapper;
import com.gate.web.beans.CommissionLog;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.facades.CommissionLogService;
import com.gate.web.servlets.SearchServlet;


@RequestMapping("/backendAdmin/commissionLogSearchServlet")
@Controller

public class CommissionLogSearchServlet extends MvcBaseServlet {
    private static final String SESSION_SEARCH_OBJ_NAME = "commissionLogListSearchVO";
    private static final String DOWNLOAD_FILE_NAME = "commission_temp";
    private static String TEMPLATE_Commission_EXCEL_LOCATION = "tempFile/commission_temp.xls";
    private static final String DEFAULT_SEARCH_LIST_DISPATCH_PAGE = "/backendAdmin/commissionLog/commissionLog_list.jsp";
    private static String TEMPLATE_Commission_EXCEL_DOWNLOAD;

    //private static final String TEMPLATE_EXCEL_DOWNLOAD_COMMISSION = SystemConfig.getInstance().getParameter("uploadTempPath") + "//tempFile"+"//commission_temp.xls";

    @Autowired
    CommissionLogService commissionLogService;


    @RequestMapping(method = RequestMethod.POST)
    public String defaultPost(@RequestParam MultiValueMap<String, String> paramMap,
                              Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("defaultPost model:   " + model);
        logger.debug("defaultPost paramMap:   " + paramMap);
        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        otherMap.put(DISPATCH_PAGE, DEFAULT_SEARCH_LIST_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);
        return TEMPLATE_PAGE;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String defaultGet(@RequestParam MultiValueMap<String, String> paramMap
            , Model model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        logger.debug("defaultGet model:   " + model);
        logger.debug("defaultGet paramMap:   " + paramMap);
        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        otherMap.put(DISPATCH_PAGE, DEFAULT_SEARCH_LIST_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);
        return TEMPLATE_PAGE;
    }

    @RequestMapping(method = RequestMethod.GET, params = "sessionClean=Y", produces = "application/json;charset=utf-8")
    public String mainList(@RequestParam("sessionClean") String sessionClean, Model model, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("mainList sessionClean:   " + sessionClean);
        logger.debug("mainList model:   " + model);
        //String returnPage =BOOTSTRAP_TEMPLATE_PAGE
        String returnPage = TEMPLATE_PAGE;
        Gson gson = new Gson();
        String errorMessage = null;

        //commonService(request, response);
        //common service裡面需要4個物件，然後傳給serviceBU，產生jsp 和物件
        //所以這裡要先取出這4個物件然後再做事情
        try {
            UserEntity user = checkLogin(request, response);
            BaseFormBean formBeanObject = formBeanObject(request);
            Map requestParameterMap = request.getParameterMap();
            Map requestAttMap = requestAttMap(request);
            Map sessionAttMap = sessionAttMap(request);
            Map otherMap = otherMap(request, response, formBeanObject);

            //ServiceBU
            List<Object> outList = new ArrayList<Object>();
            List DealerCompanyList = commissionLogService.getDealerCompanyList();
            outList.add(DealerCompanyList); //1.用戶下拉選單
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, DEFAULT_SEARCH_LIST_DISPATCH_PAGE);
            sendObjToViewer(request, otherMap);

        } catch (EinvSysException ese) {
            logger.error(ese.getMessage(), ese);
            errorMessage = gson.toJson("錯誤:" + ese.getMessage());
            returnPage = ERROR_PAGE;
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            errorMessage = gson.toJson("IO動作失敗:" + ex.getMessage());
            request.setAttribute(ERROR_MESSAGE, errorMessage);
            returnPage = ERROR_PAGE;
        } catch (ServletException ex) {
            logger.error(ex.getMessage(), ex);
            errorMessage = gson.toJson("伺服器內部錯誤:" + ex.getMessage());
            returnPage = ERROR_PAGE;
        } catch (FormValidationException ex) {
            errorMessage = gson.toJson("表單驗證失敗:" + ex.getMessage());
            logger.error(ex.getMessage(), ex);
            returnPage = ERROR_PAGE;
        } catch (ReturnPathException ex) {
            errorMessage = gson.toJson("回傳路徑有問題:" + ex.getMessage());
            logger.error(ex.getMessage(), ex);
            returnPage = ERROR_PAGE;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            errorMessage = gson.toJson("錯誤:" + ex.getMessage());
            return ERROR_PAGE;
        }
        if (errorMessage != null) {
            request.setAttribute(ERROR_MESSAGE, errorMessage);
        }

        return returnPage;
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=calCommission", produces = "application/json;charset=utf-8")
    public @ResponseBody
    boolean calCommission(@RequestParam MultiValueMap<String, String> paramMap,
                          @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "companyId", required = true) boolean calCommission
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("calCommission model:   " + model);
        logger.debug("calCommission companyId:   " + calCommission);
        logger.debug("calCommission paramMap:   " + paramMap);

        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);

        sendObjToViewer(request, otherMap);

        //Search list
        //remove function
        String data = "calculate success!! ";
        Integer exeCnt = 0;
        try {
            String dealerCompany = null;
            String inDateS = null;
            String inDateE = null;
            if (null != requestParameterMap.get("dealerCompany")) {
                dealerCompany = ((String[]) requestParameterMap.get("dealerCompany"))[0];
            }
            if (null != requestParameterMap.get("inDateS")) {
                inDateS = ((String[]) requestParameterMap.get("inDateS"))[0];
            }
            if (null != requestParameterMap.get("inDateE")) {
                inDateE = ((String[]) requestParameterMap.get("inDateE"))[0];
            }
            System.out.println("dealerCompany:   " + dealerCompany + ",inDateS:   " + inDateS + ", inDateE:    " + inDateE);
            commissionLogService.calCommission(dealerCompany, inDateS, inDateE);
        } catch (Exception ex) {
            System.out.println(ex);
            data = " calculate error!!";
        }

        // otherMap.put(AJAX_JSON_OBJECT, pageMap);
        Gson gson = new Gson();
        return Boolean.parseBoolean(gson.toJson(data));

    }


    @RequestMapping(method=RequestMethod.GET, params="method=exportCom", produces="text/plain;charset=utf-8")
    public void downloadFile(@RequestParam("method") String method, Model model
            , HttpServletRequest request, HttpServletResponse response)
            throws Exception {




        String classPath = this.getClass().getResource("/").getPath();
        CommissionLogSearchServlet.TEMPLATE_Commission_EXCEL_LOCATION = classPath + CommissionLogSearchServlet.TEMPLATE_Commission_EXCEL_LOCATION;
        logger.debug("downloadFile model:   " + model);
        logger.debug("downloadFile method:   " + method);
        logger.debug("downloadFile classPath:   " + classPath);
        logger.debug("downloadFile CommissionLogSearchServlet.TEMPLATE_Commission_EXCEL_DOWNLOAD:   "+CommissionLogSearchServlet.TEMPLATE_Commission_EXCEL_DOWNLOAD);


        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);

        sendObjToViewer(request, otherMap);
        QuerySettingVO querySettingVO = (QuerySettingVO) request.getSession().getAttribute(SESSION_SEARCH_OBJ_NAME);

        // AJAX 資料來源
        List<Map> excelList = commissionLogService.getDownloadcommissionLogList(querySettingVO);
        ExcelPoiWrapper excel = genCommissionLogToExcel(excelList, TEMPLATE_Commission_EXCEL_DOWNLOAD);
        response.setHeader("Content-Disposition", "attachment;filename=" + DOWNLOAD_FILE_NAME + ".xls");
        excel.getWorkBook().write(response.getOutputStream());
        response.getOutputStream().close();

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
                excel.copyRows(1, 17, 1, baseRow);
                excel.setValue(baseRow, index + 1, "所屬經銷商");
                excel.setValue(baseRow, index + 2, "入帳時間起");
                excel.setValue(baseRow, index + 3, "入帳時間迄");
                excel.setValue(baseRow, index + 4, "佣金類型");
                excel.setValue(baseRow, index + 5, "佣金比例");
                excel.setValue(baseRow, index + 6, "佣金付款狀態");
                excel.setValue(baseRow, index + 7, "用戶名稱");
                excel.setValue(baseRow, index + 8, "是否為首次申請");
                excel.setValue(baseRow, index + 9, "統編");
                excel.setValue(baseRow, index + 10, "繳費類型");
                excel.setValue(baseRow, index + 11, "方案名稱");
                excel.setValue(baseRow, index + 12, "計算年月");
                excel.setValue(baseRow, index + 13, "出帳年月");
                excel.setValue(baseRow, index + 14, "入帳時間");
                excel.setValue(baseRow, index + 15, "入帳金額(含稅)");
                excel.setValue(baseRow, index + 16, "出入帳金額是否相同");
                excel.setValue(baseRow, index + 17, "佣金金額");
                baseRow++;
            }

            for(int i=0; i<details.size(); i++){
                Map detailMap = (Map)details.get(i);

                excel.copyRows(2, 17, 1, baseRow);
                excel.setValue(baseRow, index + 1, master.getDealerCompanyName()); //所屬經銷商
                excel.setValue(baseRow, index + 2, master.getInDateStart()); //入帳時間起
                excel.setValue(baseRow, index + 3, master.getInDateEnd()); //入帳時間迄
                excel.setValue(baseRow, index + 4, master.getStrCommissionType());//佣金類型
                excel.setValue(baseRow, index + 5, master.getStrMainPercent());//佣金比例
                excel.setValue(baseRow, index + 6, master.getStrIsPaid());//佣金付款狀態
                excel.setValue(baseRow, index + 7, detailMap.get("name")); //用戶名稱
                excel.setValue(baseRow, index + 8, detailMap.get("is_first")); //是否為首次申請
                excel.setValue(baseRow, index + 9, detailMap.get("business_no")); //統編
                excel.setValue(baseRow, index + 10, formatCashType((Integer)detailMap.get("cash_type"))); //繳費類型
                excel.setValue(baseRow, index + 11, detailMap.get("package_name")); //方案名稱
                excel.setValue(baseRow, index + 12, detailMap.get("cal_ym")); //計算年月
                excel.setValue(baseRow, index + 13, detailMap.get("out_ym")); //出帳年月
                excel.setValue(baseRow, index + 14, detailMap.get("in_date")); //入帳時間
                excel.setValue(baseRow, index + 15, ((BigDecimal)detailMap.get("tax_inclusive_price")).doubleValue()); //入帳金額(含稅)
                excel.setValue(baseRow, index + 16, isInoutMoneyUnmatch((String) detailMap.get("is_inout_money_unmatch"))); //cash_master的出入帳金額是否相同
                excel.setValue(baseRow, index + 17, ((BigDecimal)detailMap.get("commission_amount")).doubleValue());//佣金金額
                baseRow++;
            }

            excel.copyRows(2, 17, 1, baseRow);
            excel.setValue(baseRow, index + 15, master.getInAmount()); //入帳總金額(含稅)
            excel.setValue(baseRow, index + 17, master.getCommissionAmount());//佣金總金額
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

