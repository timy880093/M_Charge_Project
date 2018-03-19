package com.gate.web.servlets.backend.commissionLog;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gate.core.bean.BaseFormBean;
import com.gate.utils.JxlsUtils;
import com.gate.web.exceptions.FormValidationException;
import com.gate.web.exceptions.ReturnPathException;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.UserEntity;
import com.gateweb.einv.exception.EinvSysException;
import com.gateweb.reportModel.CommissionRecord;
import com.gateweb.utils.CommissionLogReportUtils;
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


@RequestMapping("/backendAdmin/commissionLogSearchServlet")
@Controller

public class CommissionLogSearchServlet extends MvcBaseServlet {
    private static final String SESSION_SEARCH_OBJ_NAME = "commissionLogListSearchVO";
    private static final String DOWNLOAD_FILE_NAME = "commission_temp";
    private static String TEMPLATE_Commission_EXCEL_LOCATION = "tempFile/commission_temp.xls";
    private static String JXLS_COMMISSION_EXCEL_TEMPLATE = "tempFile/commission_jxls_template.xls";
    private static String JXLS_COMMISSION_EXCEL_OUTPUT = "tempFile/commission_jxls_output.xls";
    private static final String DEFAULT_SEARCH_LIST_DISPATCH_PAGE = "/backendAdmin/commissionLog/commissionLog_list.jsp";
    private static String TEMPLATE_Commission_EXCEL_DOWNLOAD;

    //private static final String TEMPLATE_EXCEL_DOWNLOAD_COMMISSION = SystemConfig.getInstance().getParameter("uploadTempPath") + "//tempFile"+"//commission_temp.xls";

    @Autowired
    CommissionLogService commissionLogService;

    @Autowired
    CommissionLogReportUtils commissionLogReportUtils;

    @Autowired
    JxlsUtils jxlsUtils;

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
    @RequestMapping(method = RequestMethod.GET, params = "method=search", produces = "application/json;charset=utf-8")
    public @ResponseBody String search(@RequestParam MultiValueMap<String, String> paramMap,
                                       @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value="searchField[]", required = false) List<String> searchField
            , @RequestParam(value="searchString[]", required = false) List<String> searchString
            , @RequestParam(value="sidx", required= true) String sidx
            , @RequestParam(value="sord", required= true) String sord
            , @RequestParam(value="rows", required= true) Integer rows
            , @RequestParam(value="page", required= true) Integer page
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("search model:   "+model);
        logger.debug("search paramMap:   "+paramMap);

        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);

        sendObjToViewer(request, otherMap);

        QuerySettingVO querySettingVO = new QuerySettingVO();
        Map searchMap = new HashMap();
        if (searchField != null && searchString != null && searchField.size() == searchString.size()) {
            for (int i = 0; i < searchField.size(); i++) {
                searchMap.put(searchField.get(i), java.net.URLDecoder.decode(searchString.get(i), "UTF-8"));
            }
        } else {
            logger.debug("No searchField");
        }
        querySettingVO.setSearchMap(searchMap);
        querySettingVO.setSidx(sidx);
        querySettingVO.setSord(sord);
        querySettingVO.setPage(page);
        querySettingVO.setRows(rows);

        request.getSession().setAttribute(SESSION_SEARCH_OBJ_NAME, querySettingVO);
        logger.debug("setQuerySettingVO: " + querySettingVO);

        //Search list
        //remove function
        //getData()
        Map data = commissionLogService.getCommissionMasterList(querySettingVO);
        Map gridData = setGrid(querySettingVO, data);

        // otherMap.put(AJAX_JSON_OBJECT, pageMap);
        String jsonString = convertAjaxToJson(gridData);
        return jsonString;

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
    public void downloadFile(
            @RequestParam("method") String method
            , @RequestParam("commissionLogIdArray") String commissionLogIdArrayString
            , Model model
            , HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String[] commissionLogIdArray = commissionLogIdArrayString.split(",");
        List<Integer> commissionLogIdList = new ArrayList<>();
        for(String commissionLogId: commissionLogIdArray){
            commissionLogIdList.add(Integer.parseInt(commissionLogId));
        }

        String classPath = this.getClass().getResource("/").getPath();
        logger.debug("downloadFile model:   " + model);
        logger.debug("downloadFile method:   " + method);
        logger.debug("downloadFile classPath:   " + classPath);
        logger.debug("downloadFile CommissionLogSearchServlet.TEMPLATE_Commission_EXCEL_DOWNLOAD:   "+TEMPLATE_Commission_EXCEL_LOCATION);

        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap = otherMap(request, response, formBeanObject);

        sendObjToViewer(request, otherMap);
        QuerySettingVO querySettingVO = (QuerySettingVO) request.getSession().getAttribute(SESSION_SEARCH_OBJ_NAME);

        // AJAX 資料來源
        List<Map> excelList = commissionLogService.exportCom(commissionLogIdList.toArray(new Integer[]{}));
//        ExcelPoiWrapper excel = genCommissionLogToExcel(excelList, this.getClass().getResource("/").getPath()+ TEMPLATE_Commission_EXCEL_LOCATION);
//        responseExcelFileToClient(excel, response, DOWNLOAD_FILE_NAME);

        //使用jxls實作
        List<CommissionRecord> commissionRecordList = genCommissionRecordList(excelList);
        Map<String,Object> parameterMap = new HashMap<>();
        parameterMap.put("commissionRecordList",commissionRecordList);
        response.setContentType("text/plain");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + JXLS_COMMISSION_EXCEL_OUTPUT);
        FileInputStream fileInputStream = new FileInputStream(this.getClass().getResource("/").getPath()+JXLS_COMMISSION_EXCEL_TEMPLATE);
        jxlsUtils.processTemplate(parameterMap,fileInputStream,response.getOutputStream());
    }

    private void responseExcelFileToClient(ExcelPoiWrapper excel, HttpServletResponse response,String fileName)
            throws Exception {
        response.setContentType("text/plain");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName+".xls");
        excel.getWorkBook().write(response.getOutputStream());
        excel.getWorkBook().close();
        response.getOutputStream().close();
    }

    //取得匯出發票資料的物件
    public List<CommissionRecord> genCommissionRecordList(List<Map> dataMapList){
        List<CommissionRecord> commissionRecordList = new ArrayList<>();
        for(Map dataMap : dataMapList){
            CommissionLog master = (CommissionLog) dataMap.get("master");
            List details = (List)dataMap.get("detail");
            for(int i=0; i<details.size(); i++){

                Map detailMap = (Map)details.get(i);
                CommissionRecord  commissionRecord = new CommissionRecord();
                commissionRecord.setDealerCompanyName(master.getDealerCompanyName());
                commissionRecord.setInDateStart(master.getInDateStart());
                commissionRecord.setInDateEnd(master.getInDateEnd());
                commissionRecord.setCommissionType(
                        commissionLogReportUtils.parseCommissionType(master.getCommissionType())
                );
                commissionRecord.setCommissionPercentage(master.getMainPercent().stripTrailingZeros().toPlainString()+"%");
                commissionRecord.setIsPaid(
                        commissionLogReportUtils.parseIsPaid(master.getIsPaid())
                );
                commissionRecord.setCustomName(detailMap.get("name").toString());
                commissionRecord.setIsFirst(
                        commissionLogReportUtils.parseIsFirst(String.valueOf(detailMap.get("is_first")))
                );
                commissionRecord.setBusinessNo(detailMap.get("business_no").toString());
                commissionRecord.setCashType(
                    commissionLogReportUtils.formatCashType(
                            Integer.parseInt(detailMap.get("cash_type").toString())
                    )
                );
                commissionRecord.setPackageName(detailMap.get("package_name").toString());
                commissionRecord.setCalculateYearMonth(detailMap.get("cal_ym").toString());
                commissionRecord.setInDate(detailMap.get("in_date").toString());
                commissionRecord.setTaxInclusivePrice(new BigDecimal(detailMap.get("tax_inclusive_price").toString()));
                commissionRecord.setIsInoutMoneyUnmatch(
                        commissionLogReportUtils.parseIsInoutMoneyUnmatch(
                                String.valueOf(detailMap.get("is_inout_money_unmatch"))
                        )
                );
                commissionRecord.setCommissionAmount(new BigDecimal(detailMap.get("commission_amount").toString()));
                commissionRecordList.add(commissionRecord);
            }
        }
        return commissionRecordList;
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
                excel.setValue(
                        baseRow
                        , index + 10
                        , commissionLogReportUtils.formatCashType((Integer)detailMap.get("cash_type"))
                ); //繳費類型
                excel.setValue(baseRow, index + 11, detailMap.get("package_name")); //方案名稱
                excel.setValue(baseRow, index + 12, detailMap.get("cal_ym")); //計算年月
                excel.setValue(baseRow, index + 13, detailMap.get("out_ym")); //出帳年月
                excel.setValue(baseRow, index + 14, detailMap.get("in_date")); //入帳時間
                excel.setValue(baseRow, index + 15, ((BigDecimal)detailMap.get("tax_inclusive_price")).doubleValue()); //入帳金額(含稅)
                excel.setValue(
                        baseRow
                        , index + 16
                        , commissionLogReportUtils.isInoutMoneyUnmatch((String) detailMap.get("is_inout_money_unmatch"))
                ); //cash_master的出入帳金額是否相同
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

}

