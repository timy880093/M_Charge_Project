package com.gate.web.servlets.backend.cash;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gate.core.bean.BaseFormBean;
import com.gate.utils.*;
import com.gate.web.beans.CashMasterBean;
import com.gate.web.exceptions.FormValidationException;
import com.gate.web.exceptions.ReturnPathException;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.*;
import com.gateweb.charge.repository.CompanyRepository;
import com.gateweb.charge.repository.PackageModeRepository;
import com.gateweb.charge.vo.CashVO;
import com.gateweb.einv.exception.EinvSysException;
import com.gateweb.reportModel.InvoiceBatchRecord;
import com.gateweb.reportModel.OrderCsv;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jxls.common.CellRef;
import org.springframework.beans.factory.annotation.Autowired;

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
    private static final String TEMPLATE_EXCEL_DOWNLOAD_OUT = "template/out_jxls_template.xls";
    private static final String JXLS_TEMPLATE_CONFIGURATION = "template/out_jxls_template_configuration.xml";
    private static final String TEMPLATE_EXCEL_DOWNLOAD_INVOICE = "template/invoice_jxls_template.xls";


    @Autowired
    CashService cashService;

    @Autowired
    CalCycleService calCycleService;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    PackageModeRepository packageModeRepository;

    @Autowired
    JxlsUtils jxlsUtils;

    @Autowired
    CsvUtils csvUtils;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    JsonUtils jsonUtils;

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
            ex.printStackTrace();
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
    @RequestMapping(method = RequestMethod.POST, params = "method=outYM", produces = "application/json;charset=utf-8")
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
        try {
            FileInputStream templateInputStream = new FileInputStream(this.getClass().getResource("/").getPath()+TEMPLATE_EXCEL_DOWNLOAD_OUT);
            FileInputStream configurationXmlInputStream = new FileInputStream(this.getClass().getResource("/").getPath()+ JXLS_TEMPLATE_CONFIGURATION);
            BaseFormBean formBeanObject = formBeanObject(request);
            Map otherMap = otherMap(request, response, formBeanObject);
            sendObjToViewer(request, otherMap);
            List cashMasterList =  cashService.getCashMasterDetail(outYM);
            Map<String,Object> dataMap = cashService.genCashDataExcelDataMap(cashMasterList);
            Map<String,Object> contextMap = new HashMap<>();
            contextMap.put("headers",dataMap.get("header"));
            contextMap.put("rows", dataMap.get("data"));
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + "outExcel"+outYM+".xls");
            jxlsUtils.processTemplate(contextMap,templateInputStream,response.getOutputStream(),configurationXmlInputStream,new CellRef("Template!A1"));
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
            , HttpServletRequest request, HttpServletResponse response) {
        try{
            FileInputStream templateInputStream = new FileInputStream(this.getClass().getResource("/").getPath()+TEMPLATE_EXCEL_DOWNLOAD_OUT);
            FileInputStream configurationXmlInputStream = new FileInputStream(this.getClass().getResource("/").getPath()+ JXLS_TEMPLATE_CONFIGURATION);
            BaseFormBean formBeanObject = formBeanObject(request);
            Map otherMap = otherMap(request, response, formBeanObject);
            sendObjToViewer(request, otherMap);
            List<Integer> cashMasterIdList = jsonUtils.parseMultiSelectedValueJsonArray(destJson,"cashMasterId",Integer.class);
            List<CashMasterBean> cashMasterBeanList = cashService.getCashMasterDetail(cashMasterIdList);
            Map<String,Object> dataMap = cashService.genCashDataExcelDataMap(cashMasterBeanList);
            Map<String,Object> contextMap = new HashMap<>();
            contextMap.put("headers",dataMap.get("header"));
            contextMap.put("rows", dataMap.get("data"));
            response.setContentType("text/plain");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + "outExcel"+outYM+".xls");
            jxlsUtils.processTemplate(contextMap,templateInputStream,response.getOutputStream(),configurationXmlInputStream,new CellRef("Template!A1"));
        }catch (Exception e){
            e.printStackTrace();
        }
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
            ex.printStackTrace();
            responseMessage = " fail!!";
        }
        Gson gson = new Gson();
        return gson.toJson(responseMessage);
    }

    @RequestMapping(method = RequestMethod.POST, params = "method=cancelCalOverYM", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String cancelCalOverYm(
            @RequestParam MultiValueMap<String, String> paramMap
            , @RequestHeader HttpHeaders headers
            , Model model
            , @RequestParam(value = "outYM", required = true) String outYm
            , HttpServletRequest request
            , HttpServletResponse response ) throws Exception {
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        List<Integer> accept = new ArrayList<>();
        List<Integer> ignore = new ArrayList<>();
        try{
            boolean haveCalOver = false;
            List<CashVO> cashVoList = cashService.findCashVoByOutYm(outYm);
            for(CashVO cashVO : cashVoList){
                for(CashDetailEntity cashDetailEntity: cashVO.getCashDetailEntityList()){
                    if(cashDetailEntity.getCashType().equals(2)){
                        cashService.transactionCancelOver(cashVO.getCashMasterEntity().getCashMasterId(),cashDetailEntity.getCashDetailId());
                        haveCalOver = true;
                    }
                }
                if(haveCalOver){
                    accept.add(cashVO.getCashMasterEntity().getCashMasterId());
                }else{
                    ignore.add(cashVO.getCashMasterEntity().getCashMasterId());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            Gson gson = new Gson();
            return gson.toJson("Accept: "+ accept.size()+", Ignore: "+ignore.size());
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "method=deleteEmptyCashMasterYM", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String deleteEmptyCashMasterYm(
            @RequestParam MultiValueMap<String, String> paramMap
            , @RequestHeader HttpHeaders headers
            , Model model
            , @RequestParam(value = "outYM", required = true) String outYm
            , HttpServletRequest request
            , HttpServletResponse response ) throws Exception {
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        List<Integer> accept = new ArrayList<>();
        List<Integer> ignore = new ArrayList<>();
        try{
            List<CashVO> cashVoList = cashService.findCashVoByOutYm(outYm);
            for(CashVO cashVO: cashVoList){
                if(cashVO.getCashDetailEntityList().size()==0){
                    cashService.delCashMaster(cashVO.getCashMasterEntity().getCashMasterId());
                    accept.add(cashVO.getCashMasterEntity().getCashMasterId());
                }else{
                    ignore.add(cashVO.getCashMasterEntity().getCashMasterId());
                }
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }finally {
            Gson gson = new Gson();
            return gson.toJson("Accept: "+ accept.size()+", Ignore: "+ignore.size());
        }
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=cancelCalOver", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String cancelCalOver(
            @RequestParam MultiValueMap<String, String> paramMap
            , @RequestHeader HttpHeaders headers
            , Model model
            , @RequestParam(value = "destJson", required = true) String destJson //選擇的記錄
            , HttpServletRequest request
            , HttpServletResponse response) throws Exception {
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        List<Integer> accept = new ArrayList<>();
        List<Integer> ignore = new ArrayList<>();
        try{
            List<Integer> cashMasterIdList = jsonUtils.parseMultiSelectedValueJsonArray(destJson,"cashMasterId",Integer.class);
            for(Integer cashMasterId : cashMasterIdList){
                CashVO cashVO = cashService.findCashVoById(cashMasterId);
                boolean haveCalOver = false;
                for(CashDetailEntity cashDetailEntity: cashVO.getCashDetailEntityList()){
                    if(cashDetailEntity.getCashType().equals(2)){
                        cashService.transactionCancelOver(cashMasterId,cashDetailEntity.getCashDetailId());
                        haveCalOver = true;
                    }
                }
                if(haveCalOver){
                    accept.add(cashMasterId);
                }else{
                    ignore.add(cashMasterId);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            Gson gson = new Gson();
            return gson.toJson("Accept: "+ accept.size()+", Ignore: "+ignore.size());
        }
    }

    /**
     * 只有空的CashMaster可以被刪除
     * @param paramMap
     * @param headers
     * @param model
     * @param destJson
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=deleteEmptyCashMaster", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String deleteEmptyCashMaster(
            @RequestParam MultiValueMap<String, String> paramMap
            , @RequestHeader HttpHeaders headers
            , Model model
            , @RequestParam(value = "destJson", required = true) String destJson //選擇的記錄
            , HttpServletRequest request
            , HttpServletResponse response ) throws Exception{
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        List<Integer> accept = new ArrayList<>();
        List<Integer> ignore = new ArrayList<>();
        try{
            List<Integer> cashMasterIdList = jsonUtils.parseMultiSelectedValueJsonArray(destJson,"cashMasterId",Integer.class);
            for(Integer cashMasterId : cashMasterIdList){
                CashVO cashVO = cashService.findCashVoById(cashMasterId);
                if(cashVO.getCashDetailEntityList().size()==0){
                    cashService.delCashMaster(cashMasterId);
                    accept.add(cashMasterId);
                }else{
                    ignore.add(cashMasterId);
                }
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }finally {
            Gson gson = new Gson();
            return gson.toJson("Accept: "+ accept.size()+", Ignore: "+ignore.size());
        }
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
    @RequestMapping(method = RequestMethod.POST, params = "method=emailYM", produces = "application/json;charset=utf-8")
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
            ex.printStackTrace();
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
    public  @ResponseBody
    String email(@RequestParam MultiValueMap<String, String> paramMap,
                 @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "destJson", required = true) String destJson //帳單年月
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("out model:   " + model);
        logger.debug("out destJson:   " + destJson);
        logger.debug("out paramMap:   " + paramMap);


        UserEntity user = checkLogin(request,response);

        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        Integer exeCnt = 0;
        String responseMessage = "";

        try{
            exeCnt = cashService.sendBillMail(destJson);
            responseMessage += " total counts: " + exeCnt+ "";
        }catch(Exception ex){
            ex.printStackTrace();
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

        UserEntity user = checkLogin(request,response);

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
            , @RequestParam(value = "outYM", required = true) String outYM
            , HttpServletRequest request, HttpServletResponse response) throws Exception{
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        List invoiceItemList =  cashService.getInvoiceItem(outYM);
        List<InvoiceBatchRecord> invoiceBatchRecordList = cashService.genInvoiceBatchRecordList(invoiceItemList);
        Map<String,Object> parameterMap = new HashMap<>();
        parameterMap.put("invoiceBatchRecordList",invoiceBatchRecordList);
        response.setContentType("text/plain");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + DOWNLOAD_FILE_NAME_INVOICE+"_"+outYM+".xls");
        FileInputStream templateFileInputStream = new FileInputStream(this.getClass().getResource("/").getPath()+TEMPLATE_EXCEL_DOWNLOAD_INVOICE);
        jxlsUtils.processTemplate(parameterMap,templateFileInputStream,response.getOutputStream());
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
        List invoiceItemList =  cashService.getInvoiceItem(null, destJson);
        List<InvoiceBatchRecord> invoiceBatchRecordList = cashService.genInvoiceBatchRecordList(invoiceItemList);
        Map<String,Object> parameterMap = new HashMap<>();
        parameterMap.put("invoiceBatchRecordList",invoiceBatchRecordList);
        response.setContentType("text/plain");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + DOWNLOAD_FILE_NAME_INVOICE+".xls");
        FileInputStream templateFileInputStream = new FileInputStream(this.getClass().getResource("/").getPath()+TEMPLATE_EXCEL_DOWNLOAD_INVOICE);
        jxlsUtils.processTemplate(parameterMap,templateFileInputStream,response.getOutputStream());
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

    @RequestMapping(method = RequestMethod.GET, params = "method=exportOrderCsv", produces = "application/json;charset=utf-8")
    public @ResponseBody
    void exportOrderCsv(@RequestParam MultiValueMap<String, String> paramMap,
                        @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "destJson", required = true) String destJson //多筆的選擇
            , HttpServletRequest request, HttpServletResponse response) {
        try{
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(destJson,JsonArray.class);
            for(int i=0;i<jsonArray.size();i++){
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                Integer id = jsonObject.get("cashMasterId").getAsInt();
                CashVO cashVO = cashService.findCashVoById(id);
                List<OrderCsv> orderCsvList = cashService.genOrderCsvListByCashVO(new Long(2),cashVO);
                //第一行欄位資料
                List<String> headerValueList = csvUtils.genBeanHeaderData(OrderCsv.class,new ArrayList<>());
                String headerStringData = csvUtils.dataListToCsvLineData(headerValueList,",");
                List<String> detailStringDataList = new ArrayList<>();
                //後續明細資料
                for(OrderCsv orderCsv: orderCsvList){
                    List<Object> beanDataObjectList = csvUtils.genBeanValueData(orderCsv,new ArrayList<>());
                    List<String> beanDataArrayList = csvUtils.objectListToStringList(beanDataObjectList);
                    String detailStringData = csvUtils.dataListToCsvLineData(beanDataArrayList,",");
                    detailStringDataList.add(detailStringData);
                }
                List<String> summaryStringDataList = new ArrayList<>();
                summaryStringDataList.add(headerStringData);
                summaryStringDataList.addAll(detailStringDataList);
                if(detailStringDataList.size()!=0){
                    response.setContentType("text/plain");
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename=" + "exportOrderCsv.xls");
                    fileUtils.writeTextFileToOutputStream(response.getOutputStream(),summaryStringDataList);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void responseExcelFileToClient(ExcelPoiWrapper excel, HttpServletResponse response,String fileName)
            throws Exception {
        response.setContentType("text/plain");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName+".xls");
        excel.getWorkBook().write(response.getOutputStream());
        response.getOutputStream().close();
    }

}
