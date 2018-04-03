package com.gate.web.servlets.backend.calCycle;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gate.core.bean.BaseFormBean;
import com.gate.utils.JsonUtils;
import com.gate.web.exceptions.FormValidationException;
import com.gate.web.exceptions.ReturnPathException;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.CompanyEntity;
import com.gateweb.charge.model.UserEntity;
import com.gateweb.charge.repository.CompanyRepository;
import com.gateweb.einv.exception.EinvSysException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.facades.CalCycleService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/backendAdmin/calCycleSearchServlet")
@Controller

public class CalCycleSearchServlet extends MvcBaseServlet {

    private final String DEFAULT_SEARCH_LIST_DISPATCH_PAGE ="/backendAdmin/calCycle/calCycle_list.jsp";
    private static final String SESSION_SEARCH_OBJ_NAME = "calCycleSearchVO";

    @Autowired
    CalCycleService calCycleService;

    @Autowired
    JsonUtils jsonUtils;

    @Autowired
    CompanyRepository companyRepository;

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


    @RequestMapping(method = RequestMethod.GET, params = "method=search", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String search(@RequestParam MultiValueMap<String, String> paramMap,
                  @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "searchField[]", required = false) List<String> searchField
            , @RequestParam(value = "searchString[]", required = false) List<String> searchString
            , @RequestParam(value = "sidx", required = true) String sidx
            , @RequestParam(value = "sord", required = true) String sord
            , @RequestParam(value = "rows", required = true) Integer rows
            , @RequestParam(value = "page", required = true) Integer page
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("search model:   " + model);
        logger.debug("search paramMap:   " + paramMap);

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
        Map data = calCycleService.getBillCycleList(querySettingVO);
        Map gridData = setGrid(querySettingVO, data);

        // otherMap.put(AJAX_JSON_OBJECT, pageMap);
        String jsonString = convertAjaxToJson(gridData);
        return jsonString;
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=calOverYM", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String calOverYM(@RequestParam MultiValueMap<String, String> paramMap,
                     @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "calYM", required = true) String calYM //帳單年月
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("calOverYM model:   " + model);
        logger.debug("calOverYM calOverYM:   " + calYM);
        logger.debug("outYM paramMap:   " + paramMap);

        UserEntity user = checkLogin(request, response);

        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        Integer exeCnt = 0;
        String responseMessage = "";
        try {
            //找出所有公司
            List<CompanyEntity> companyEntityList = companyRepository.findAll();
            for(CompanyEntity companyEntity : companyEntityList){
                exeCnt += calCycleService.batchCalOverByYearMonthAndCompanyId(
                        companyEntity.getCompanyId()
                        , calYM
                        ,user.getUserId().intValue()
                );
            }
            responseMessage += "  total counts: " + exeCnt + "";
        } catch (Exception ex) {
            System.out.println(ex);
            responseMessage = " fail!!";
        }
        Gson gson = new Gson();
        return gson.toJson(responseMessage);
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=calOver", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String calOver(@RequestParam MultiValueMap<String, String> paramMap
            , @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "calOverAry", required = true) String calOverAry //帳單年月
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("calOver model:   " + model);
        logger.debug("calOver calOver:   " + calOverAry);
        logger.debug("calOver paramMap:   " + paramMap);

        UserEntity user = checkLogin(request, response);

        List<Integer> billIdList = jsonUtils.parseMultiSelectedValueJsonArray(calOverAry,"billId",Integer.class);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        Integer exeCnt = 0;
        String responseMessage = "";
        try {
            //這裡的calOverArray存的其實是billCycle的billId
            exeCnt = calCycleService.batchCalOver(billIdList, user.getUserId().intValue());
            responseMessage += "  total counts: " + exeCnt + "";
        } catch (Exception ex) {
            System.out.println(ex);
            responseMessage = " fail!!";
        }
        Gson gson = new Gson();
        return gson.toJson(responseMessage);
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=calOverToCash", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String calOverToCash(@RequestParam MultiValueMap<String, String> paramMap,
                         @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "calYM", required = true) String calYM //帳單年月
            , @RequestParam(value = "userCompanyId", required = true) String userCompanyId //帳單年月
            , @RequestParam(value = "calOverAry", required = true) String calOverAry //帳單年月
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("calOver model:   " + model);
        logger.debug("calOver calYM:   " + calYM);
        logger.debug("calOver companyId:   " + userCompanyId);
        logger.debug("calOver calOverAry:   " + calOverAry);
        logger.debug("calOver paramMap:   " + paramMap);

        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        Integer exeCnt = 0;
        String responseMessage = "";
        try {
            exeCnt = calCycleService.sendOverMailYM(calYM);
            responseMessage += "  total counts: " + exeCnt + "";
        } catch (Exception ex) {
            System.out.println(ex);
            responseMessage = " fail!!";
        }
        Gson gson = new Gson();
        return gson.toJson(responseMessage);
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=emailYM", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String emailYM(@RequestParam MultiValueMap<String, String> paramMap,
                   @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "calYM", required = true) String calYM //帳單年月
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("calOver model:   " + model);
        logger.debug("calOver calYM:   " + calYM);
        logger.debug("calOver paramMap:   " + paramMap);

        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        Integer exeCnt = 0;
        String responseMessage = "";
        try {
            exeCnt = calCycleService.sendOverMailYM(calYM);
            responseMessage += "  total counts: " + exeCnt + "";
        } catch (Exception ex) {
            System.out.println(ex);
            responseMessage = " fail!!";
        }
        Gson gson = new Gson();
        return gson.toJson(responseMessage);
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=email", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String email(@RequestParam MultiValueMap<String, String> paramMap,
                 @RequestHeader HttpHeaders headers, Model model
            , @RequestParam(value = "calOverAry", required = true) String calOverAry //帳單年月
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("calOver model:   " + model);
        logger.debug("calOver calOverAry:   " + calOverAry);
        logger.debug("calOver paramMap:   " + paramMap);

        BaseFormBean formBeanObject = formBeanObject(request);
        Map otherMap = otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
        Integer exeCnt = 0;
        String responseMessage = "";
        try {
            exeCnt = calCycleService.sendOverMailYM(calOverAry);
            responseMessage += "  total counts: " + exeCnt + "";
        } catch (Exception ex) {
            System.out.println(ex);
            responseMessage = " fail!!";
        }
        Gson gson = new Gson();
        return gson.toJson(responseMessage);
    }

    @RequestMapping(method = RequestMethod.GET, params = "sessionClean=Y", produces = "application/json;charset=utf-8")
    public String mainList(@RequestParam("sessionClean") String sessionClean, Model model, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String returnPage = TEMPLATE_PAGE;
        Gson gson = new Gson();
        String errorMessage = null;
        try {
            //寫入template的時間。
            UserEntity user = checkLogin(request, response);
            BaseFormBean formBeanObject = formBeanObject(request);
            Map otherMap = otherMap(request, response, formBeanObject);
            List<Object> outList = new ArrayList<Object>();

            List ymList = calCycleService.getYM();
            outList.add(ymList); //0.
            List userCompanyList = calCycleService.getUserCompanyList();
            outList.add(userCompanyList); //1.

            //2.計算年月:預設每月15日後算上個月的超額
            Calendar now = Calendar.getInstance();
            Integer day = now.get(Calendar.DAY_OF_MONTH);
            if (day < 15) { //每月15日後算上個月的超額
                now.add(Calendar.MONTH, -2);
            } else { //每月15日後算上個月的超額
                now.add(Calendar.MONTH, -1);
            }
            String year = "" + now.get(Calendar.YEAR);
            String month = "";
            if ((now.get(Calendar.MONTH) + 1) < 10) {
                month = "0" + (now.get(Calendar.MONTH) + 1);
            } else {
                month = "" + (now.get(Calendar.MONTH) + 1);
            }

            outList.add(year + month);


            //請先作完YYYYMM前的快到期合約續約後，才可作YYYYMM後的超額計算
            now.add(Calendar.MONTH, 3); //作快到期合約的年月
            String yearAlmost = "" + now.get(Calendar.YEAR);
            String monthAlmost = "";
            if ((now.get(Calendar.MONTH) + 1) < 10) {
                monthAlmost = "0" + (now.get(Calendar.MONTH) + 1);
            } else {
                monthAlmost = "" + (now.get(Calendar.MONTH) + 1);
            }
            outList.add(yearAlmost + monthAlmost);

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

}