package com.gate.web.servlets.backend.cash;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.gateweb.charge.model.*;
import com.gateweb.charge.repository.CompanyRepository;
import com.gateweb.charge.repository.PackageModeRepository;
import com.gateweb.charge.vo.CashVO;
import com.gateweb.einv.exception.EinvSysException;
import com.google.gson.Gson;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;

import com.gate.utils.ExcelPoiWrapper;
import com.gate.web.beans.CashDetailBean;
import com.gate.web.beans.CashMasterBean;
import com.gate.web.beans.InvoiceExcelBean;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.facades.CalCycleService;
import com.gate.web.facades.CashService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/backendAdmin/cashSearchServlet")
@Controller
public class CashSearchServlet extends MvcBaseServlet {
    private static final String DOWNLOAD_FILE_NAME_OUT ="out_data";
    private static final String DOWNLOAD_FILE_NAME_INVOICE ="invoice_data";
    private final String DEFAULT_SEARCH_LIST_DISPATCH_PAGE = "/backendAdmin/cash/cash_list.jsp";
    private static final String SESSION_SEARCH_OBJ_NAME = "cashSearchVO";
    private static final String SESSION_SEARCH_DETAIL_OBJ_NAME = "cashFlowDetailSearchVO";
    //private static final String TEMPLATE_EXCEL_DOWNLOAD_OUT = SystemConfig.getInstance().getParameter("uploadTempPath") + "/tempFile"+"/out_temp.xls";
    //private static final String TEMPLATE_EXCEL_DOWNLOAD_INVOICE = SystemConfig.getInstance().getParameter("uploadTempPath") + "/tempFile"+"/invoice_temp.xls";
    
    @Autowired
    CashService cashService;
    
    @Autowired
    CalCycleService calCycleService;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    PackageModeRepository packageModeRepository;

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

    /**
     * 一般查詢
     * @param paramMap
     * @param headers
     * @param model
     * @param searchField
     * @param searchString
     * @param sidx
     * @param sord
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=search", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String search(@RequestParam MultiValueMap<String, String> paramMap,
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

        BaseFormBean formBeanObject = formBeanObject(request);
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
        Map data = cashService.getCashMaster(querySettingVO);
        Map gridData = setGrid(querySettingVO, data);

        // otherMap.put(AJAX_JSON_OBJECT, pageMap);
        String jsonString = convertAjaxToJson(gridData);
        return jsonString;
    }

    /**
     * 檢視帳單明細
     * @param paramMap
     * @param headers
     * @param model
     * @param searchField
     * @param searchString
     * @param sidx
     * @param sord
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=searchDetail", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String searchDetail(@RequestParam MultiValueMap<String, String> paramMap,
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

        BaseFormBean formBeanObject = formBeanObject(request);
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

        request.getSession().setAttribute(SESSION_SEARCH_DETAIL_OBJ_NAME, querySettingVO);
        logger.debug("setQuerySettingVO: " + querySettingVO);

        //Search list
        //remove function
        //getData()
        Map data = cashService.getCashMaster(querySettingVO);
        Map gridData = setGrid(querySettingVO, data);

        // otherMap.put(AJAX_JSON_OBJECT, pageMap);
        String jsonString = convertAjaxToJson(gridData);
        return jsonString;
    }

    /**
     * 出帳-多筆
     * @param paramMap
     * @param model
     * @param destJson
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=out", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String out(@RequestParam MultiValueMap<String, String> paramMap
            , Model model
            , @RequestParam(value = "destJson", required = true) String destJson
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("out model:   " + model);
        logger.debug("out destJson:   " + destJson);//帳單年月
        logger.debug("out paramMap:   " + paramMap);

        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);

        sendObjToViewer(request, otherMap);

        //Search list
        //remove function
        Integer exeCnt = 0;
        String responseMessage = "";
        try {
            exeCnt = cashService.out(destJson);
            responseMessage += "  total counts: "+exeCnt+"";
        } catch (Exception ex) {
            System.out.println(ex);
        }
        Gson gson = new Gson();
        return gson.toJson(responseMessage);
    }

    /**
     * 出帳-批次
     * @param paramMap
     * @param headers
     * @param model
     * @param outYM
     * @param userCompanyId
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=outYM", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String outYM(@RequestParam MultiValueMap<String, String> paramMap,
               @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "outYM", required = true) String outYM
            , @RequestParam(value = "userCompanyId", required = true) Integer userCompanyId
            , HttpServletRequest request, HttpServletResponse response) throws Exception{
        logger.debug("outYM model:   " + model);
        logger.debug("outYM outYM:   " + outYM);
        logger.debug("outYM userCompanyId:   " + userCompanyId);
        logger.debug("outYM paramMap:   " + paramMap);

        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        Integer exeCnt = 0;
        String responseMessage = "";
        try {
            exeCnt = cashService.outYM(outYM, userCompanyId);
            responseMessage += "  total counts: " + exeCnt;
        }catch(Exception ex){
                System.out.println(ex);
            responseMessage = " fail!!";
        }
        Gson gson = new Gson();
        return gson.toJson(responseMessage);
    }

    /**
     * 匯出Excel帳單-批次(請選擇出帳單年月)
     * @param paramMap
     * @param headers
     * @param model
     * @param outYM
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=outExcelym", produces = "application/json;charset=utf-8")
    public @ResponseBody
    void outExcelym(@RequestParam MultiValueMap<String, String> paramMap,
                 @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "outYM", required = true) String outYM
            , HttpServletRequest request, HttpServletResponse response) throws Exception{
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        try {
            List cashMasterList =  cashService.getCashMasterDetail(outYM);
            String filePath = this.getClass().getResource("/").getPath()+"/tempFile"+"/out_temp.xls";
            ExcelPoiWrapper excel= genCashDataToExcel(cashMasterList, filePath);
            responseExcelFileToClient(excel, response, DOWNLOAD_FILE_NAME_OUT+"_"+outYM);
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    /**
     * 匯出Excel帳單-多筆(請勾選欲執行的資料)
     * @param paramMap
     * @param headers
     * @param model
     * @param destJson
     * @param outYM
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=outExcel", produces = "application/json;charset=utf-8")
    public @ResponseBody
    void outExcel(@RequestParam MultiValueMap<String, String> paramMap,
                    @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "destJson", required = true) String destJson //多筆的選擇
            , @RequestParam(value = "outYM", required = true) String outYM //帳單年月
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        List cashMasterList =  cashService.getCashMasterDetail(outYM, destJson);
        String filePath = this.getClass().getResource("/").getPath()+"/tempFile"+"/out_temp.xls";
        ExcelPoiWrapper excel= genCashDataToExcel(cashMasterList, filePath);
        response = (HttpServletResponse) otherMap.get(RESPONSE);
        responseExcelFileToClient(excel, response, DOWNLOAD_FILE_NAME_OUT+"_"+outYM);
    }

    /**
     * 批次取消出帳
     * @param paramMap
     * @param headers
     * @param model
     * @param outYM
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=cancelOutYM", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String cancelOutYM(@RequestParam MultiValueMap<String, String> paramMap,
                  @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "outYM", required = true) String outYM //帳單年月
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        Integer exeCnt = 0;
        String responseMessage = "";
        try{
            exeCnt = cashService.cancelOutYM(outYM);
            responseMessage += " total counts: " + exeCnt;
        }catch(Exception ex){
            System.out.println(ex);
            responseMessage = " fail!!";
        }
        Gson gson = new Gson();
        return gson.toJson(responseMessage);
    }

    /**
     * 多筆取消出帳
     * @param paramMap
     * @param headers
     * @param model
     * @param destJson
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=cancelOut", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String cancelOut(@RequestParam MultiValueMap<String, String> paramMap,
                       @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "destJson", required = true) String destJson //帳單年月
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        Integer exeCnt = 0;
        String responseMessage = "";
        try{
            exeCnt = cashService.cancelOut(destJson);
            responseMessage += " total counts: " + exeCnt;
        }catch(Exception ex){
            System.out.println(ex);
            responseMessage = " fail!!";
        }
        Gson gson = new Gson();
        return gson.toJson(responseMessage);
    }

    /**
     * 批次-寄email
     * @param paramMap
     * @param headers
     * @param model
     * @param calYM
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=emailYM", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String emailYM(@RequestParam MultiValueMap<String, String> paramMap,
                     @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "outYM", required = true) String calYM
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        Integer exeCnt = 0;
        String responseMessage = "";
        try{
            exeCnt = cashService.sendBillMailYM(calYM);
            responseMessage += " total counts: " + exeCnt;
        }catch(Exception ex){
            System.out.println(ex);
            responseMessage = " fail!!";
        }
        Gson gson = new Gson();
        return gson.toJson(responseMessage);
    }

    /**
     * 多筆-寄email
     * @param paramMap
     * @param headers
     * @param model
     * @param destJson
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=email", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String email(@RequestParam MultiValueMap<String, String> paramMap,
                   @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "destJson", required = true) String destJson //帳單年月
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        Integer exeCnt = 0;
        String responseMessage = "";
        try{
            exeCnt = cashService.sendBillMail(destJson);
            responseMessage += " total counts: " + exeCnt;
        }catch(Exception ex){
            System.out.println(ex);
            responseMessage = " fail!!";
        }
        Gson gson = new Gson();
        return gson.toJson(responseMessage);
    }

    /**
     * 多筆-未繳費客戶通知
     * @param paramMap
     * @param headers
     * @param model
     * @param destJson
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=email1", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String email1(@RequestParam MultiValueMap<String, String> paramMap,
                 @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "destJson", required = true) String destJson
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        Integer exeCnt = 0;
        String responseMessage = "";
        try{
            exeCnt = cashService.transactionSendBillMail1(destJson);
            responseMessage += " total counts: " + exeCnt;
        }catch(Exception ex){
            System.out.println(ex);
            responseMessage = " fail!!";
        }
        Gson gson = new Gson();
        return gson.toJson(responseMessage);
    }

    /**
     * 匯出發票資料-by 年月
     * @param paramMap
     * @param headers
     * @param model
     * @param outYM
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=invoiceExcelYM", produces = "application/json;charset=utf-8")
    public @ResponseBody
    void invoiceExcelYM(@RequestParam MultiValueMap<String, String> paramMap,
                    @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "destJson", required = true) String outYM
            , HttpServletRequest request, HttpServletResponse response) throws Exception{
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        List invoiceItemList =  cashService.getInvoiceItem(outYM);
        String filePath = this.getClass().getResource("/").getPath()+"/tempFile"+"/invoice_temp.xls";
        ExcelPoiWrapper excel= genInvoiceItemToExcel(invoiceItemList, filePath);
        response = (HttpServletResponse) otherMap.get(RESPONSE);
        responseExcelFileToClient(excel, response, DOWNLOAD_FILE_NAME_INVOICE+"_"+outYM);
    }

    /**
     * 匯出發票資料-by 多筆
     * @param paramMap
     * @param headers
     * @param model
     * @param destJson
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=invoiceExcel", produces = "application/json;charset=utf-8")
    public @ResponseBody
    void invoiceExcel(@RequestParam MultiValueMap<String, String> paramMap,
                        @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "destJson", required = true) String destJson //多筆的選擇
            , HttpServletRequest request, HttpServletResponse response) throws Exception{
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        List cashMasterList =  cashService.getInvoiceItem(null, destJson);
        String filePath = this.getClass().getResource("/").getPath()+"/tempFile"+"/invoice_temp.xls";
        ExcelPoiWrapper excel= genInvoiceItemToExcel(cashMasterList, filePath);
        response = (HttpServletResponse) otherMap.get(RESPONSE);
        responseExcelFileToClient(excel, response, DOWNLOAD_FILE_NAME_INVOICE);
    }

    @RequestMapping(method = RequestMethod.GET, params = "sessionClean=Y", produces = "application/json;charset=utf-8")
    public String mainList(@RequestParam("sessionClean") String sessionClean, Model model, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String returnPage = TEMPLATE_PAGE;
        Gson gson = new Gson();
        String errorMessage = null;
        try{
            //寫入template的時間。
            UserEntity user = checkLogin(request, response);
            BaseFormBean formBeanObject = formBeanObject(request);
            Map otherMap = otherMap(request, response, formBeanObject);
            List<Object> outList = new ArrayList<Object>();

            List ymList = cashService.getYM();
            outList.add(ymList); //0.
            List userCompanyList = calCycleService.getUserCompanyList();
            outList.add(userCompanyList); //1.

            //2.計算年月:預設每月15日後算這個月的帳單
            Calendar now = Calendar.getInstance();
            Integer day = now.get(Calendar.DAY_OF_MONTH);
            if(day<15){ //每月15日後算這個月的帳單
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

    private void responseExcelFileToClient(ExcelPoiWrapper excel, HttpServletResponse response,String fileName)
            throws Exception {
        response.setContentType("text/plain");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName+".xls");
        excel.getWorkBook().write(response.getOutputStream());
        response.getOutputStream().close();
    }

    /**
     *  產生查詢下載的Excel報表
     * @param cashMasterList
     * @param tempPath
     * @return
     * @throws Exception
     */
    private ExcelPoiWrapper genCashDataToExcel(List<CashMasterBean> cashMasterList, String tempPath) throws Exception {
        ExcelPoiWrapper excel = new ExcelPoiWrapper(tempPath);

        HashMap packageMap = new HashMap();
        excel.setWorkSheet(1);
        int baseRow=2;
        int index = 0;
        int packageIndex = 8;
        for(CashMasterBean masterBean:cashMasterList){
            excel.copyRows(2, 21, 1, baseRow);

            excel.setValue(baseRow, index + 1, masterBean.getBusinessNo());
            excel.setValue(baseRow, index + 2, masterBean.getInAmount());
            excel.setValue(baseRow, index + 6, masterBean.getCompanyName());
            excel.setValue(baseRow, index + 7, masterBean.getBusinessNo());

            List<CashDetailBean> cashDetailList = masterBean.getCashDetailList();
            for(CashDetailBean detail: cashDetailList){
                Integer chargeId = detail.getChargeId();
                Integer cashType = detail.getCashType(); //1.月租 2.超額 3.代印代寄 4.加值型 5.儲值 6.預繳
                Integer billType = detail.getBillType(); //1.月租 2.級距

                //要繳的金額(月租預繳和超額分開)
                BigDecimal taxInclusivePrice_ = detail.getTaxInclusivePrice();
                Integer taxInclusivePrice = 0;
                if(taxInclusivePrice_.intValue() == 0){
                    taxInclusivePrice = 0; //不然excel顯示的數字會0E-9
                }else{
                    taxInclusivePrice = taxInclusivePrice_.setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
                }

                if(null == packageMap.get(chargeId+","+cashType+","+billType)){
                    packageMap.put(chargeId+","+cashType+","+billType, packageIndex);
                    excel.setValue(baseRow, index + packageIndex, taxInclusivePrice);
                    if(cashType == 1){ //月租
                        excel.setValue(1, index + packageIndex, detail.getPackageName());
                    }else if(cashType == 2){ //超額
                        excel.setValue(1, index + packageIndex, detail.getPackageName()+"(超額)");
                    }else  if(cashType == 6){ //預繳
                        excel.setValue(1, index + packageIndex, "預繳");
                    }else if(cashType == 7){ //7.扣抵
                        excel.setValue(1, index + packageIndex, "扣抵");
                    }
                    packageIndex++;
                }else{
                    Integer packageIndexOld = (Integer)packageMap.get(chargeId + ","+cashType+","+billType);
                    excel.setValue(baseRow, index + packageIndexOld, taxInclusivePrice);
                }
            }
            baseRow++;
        }

        //把沒有packagename的cell填0
        HSSFSheet sheet = excel.getSheet();
        for(int i=2; i<baseRow; i++){
            Row row =sheet.getRow(i-1);
            for(int j=8; j<packageIndex; j++){
                Cell cell = row.getCell(j-1);
                if(!isNotEmptyCell(cell)){
                    excel.setValue(i, j, 0);
                }
            }
        }

        return excel;

    }

    //匯出發票資料Excel
    private ExcelPoiWrapper genInvoiceItemToExcel(List<InvoiceExcelBean> InvoiceExcelList, String tempPath) throws Exception {
        ExcelPoiWrapper excel = new ExcelPoiWrapper(tempPath);
        HashMap packageMap = new HashMap();
        excel.setWorkSheet(1);
        int baseRow=3;
        int index = 0;
        int packageIndex = 8;
        for(InvoiceExcelBean bean:InvoiceExcelList){
            excel.copyRows(3, 21, 1, baseRow);

            excel.setValue(baseRow, index + 1, bean.getInvoiceIndex()); //發票張數
            excel.setValue(baseRow, index + 2, bean.getInvoiceDate()); //發票日期
            excel.setValue(baseRow, index + 3, bean.getItemIndex()); //品名序號
            excel.setValue(baseRow, index + 4, bean.getItemName()); //發票品名
            excel.setValue(baseRow, index + 5, bean.getItemCnt()); //數量
            excel.setValue(baseRow, index + 6, bean.getUnitPrice()); //單價
            excel.setValue(baseRow, index + 7, bean.getTaxType()); //課稅別
            excel.setValue(baseRow, index + 8, bean.getTax()); //稅率
            excel.setValue(baseRow, index + 10, bean.getBusinessNo()); //買方統編
            excel.setValue(baseRow, index + 11, 2); //1.列印 2.列印+email

            baseRow++;
        }
        return excel;

    }

    /**
     * 過瀘不允許的欄位值。
     * @param cell
     * @return
     */
    private boolean isNotEmptyCell(Cell cell){
        boolean result = true;
        //parameters
        if(cell!=null){
            if(getCellValue(cell)!=null){
                Object cellValue = getCellValue(cell);
                if(cellValue==null){
                    result = false;
                }
                if(String.valueOf(getCellValue(cell)).trim().length()==0){
                    result = false;
                }
                if(cellValue.equals("null")){
                    result = false;
                }
            }else{
                result = false;
            }
        }else{
            result = false;
        }
        return result;
    }

    private Object getCellValue(Cell cell) {
        Object value = new Object();
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date != null) {
                        value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    } else {
                        value = "";
                    }
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            default:
                value = null;
        }
        return value;
    }
}
