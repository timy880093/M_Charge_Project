package com.gate.web.servlets.backend.contract;

import com.gate.web.facades.UserService;
import com.gate.web.servlets.abstraction.DefaultDisplayPageModelViewController;
import com.gateweb.charge.chargePolicy.cycle.service.CycleService;
import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.bean.request.*;
import com.gateweb.charge.contract.component.ContractValidationComponent;
import com.gateweb.charge.exception.DeleteBilledBillingItemException;
import com.gateweb.charge.frontEndIntegration.bean.SweetAlertResponse;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.charge.frontEndIntegration.enumeration.SweetAlertStatus;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.security.ChargeUserPrinciple;
import com.gateweb.charge.service.BillingService;
import com.gateweb.charge.service.ContractService;
import com.gateweb.charge.service.PackageRefService;
import com.gateweb.charge.service.ReportService;
import com.gateweb.charge.service.dataGateway.ContractDataGateway;
import com.gateweb.charge.service.endPointService.ContractEndpointService;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.view.ContractFetchView;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.utils.bean.BeanConverterUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.*;

@RequestMapping("/backendAdmin/contractManagementServlet")
@Controller
public class ContractManagementServlet extends DefaultDisplayPageModelViewController {
    private static final String DEFAULT_DISPATCH_PAGE = "/pages/contractListView.html";
    protected final Logger logger = LogManager.getLogger(getClass());
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    @Autowired
    ContractService contractService;
    @Autowired
    BillingService billingService;
    @Autowired
    PackageRefService packageRefService;
    @Autowired
    ContractDataGateway contractDataGateway;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    CycleService cycleService;
    @Autowired
    ContractValidationComponent contractValidationComponent;
    @Autowired
    ReportService reportService;
    @Autowired
    UserService userService;
    @Autowired
    ContractEndpointService contractEndpointService;

    Gson gson = new Gson();

    @RequestMapping(method = RequestMethod.GET, params = "method=list", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String search(@RequestParam MultiValueMap<String, String> paramMap, Model model) {
        List<ContractFetchView> contractList = contractService.getNotExpireContractFetchViewList();
        Map dataMap = new HashMap();
        dataMap.put("data", contractList);
        return gson.toJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=fetchViewList", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String searchFetchView(@RequestParam MultiValueMap<String, String> paramMap, Model model) {
        List<ContractFetchView> contractFetchViewList = contractService.getNotExpireContractFetchViewList();
        Map dataMap = new HashMap();
        dataMap.put("data", contractFetchViewList);
        return gson.toJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/list", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String searchContractList(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws Exception {
        Map dataMap = new HashMap();
        try {
            List<ContractFetchView> contractFetchViewList = new ArrayList<>();
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            if (callerInfoOptional.isPresent()) {
                contractFetchViewList = contractService.searchAllContractFetchView();
            }
            dataMap.put("data", contractFetchViewList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/origin/deductibleList", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String searchDeductibleOriginContractList(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Map dataMap = new HashMap();
        try {
            List<ContractFetchView> contractList = new ArrayList<>();
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            if (callerInfoOptional.isPresent()) {
                contractList = contractService.findDeductibleContractList();
            }
            dataMap.put("data", contractList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/saveOrUpdate", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<ContractSaveRes> saveOrUpdateContract(
            Authentication authentication
            , @RequestBody String jsonBody) {
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            Optional<ContractSaveReq> contractSaveReqOptional = beanConverterUtils.convertJsonToObjWithTypeCast(
                    jsonBody, ContractSaveReq.class
            );
            if (callerInfoOptional.isPresent() && contractSaveReqOptional.isPresent()) {
                gson = new GsonBuilder().serializeNulls().create();
                return
                        ResponseEntity.ok(
                                contractEndpointService.contractSaveOrUpdate(
                                        contractSaveReqOptional.get()
                                        , chargeUserPrinciple.getUserInstance().getUserId().longValue()
                                )
                        );
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return (ResponseEntity<ContractSaveRes>) ResponseEntity.badRequest();
    }

    /**
     * 初始化合約
     * 初始化合約會把該合約內容中所有預繳的項目都結帳
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/api/initial/{contractId}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String initialContract(
            Authentication authentication
            , @PathVariable("contractId") long contractId) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            if (callerInfoOptional.isPresent()) {
                gson = new GsonBuilder().serializeNulls().create();
                contractService.initialContract(
                        contractId,
                        callerInfoOptional.get().getUserEntity().getUserId().longValue()
                );
                sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
                sweetAlertResponse.setTitle("初始化成功");
            }
        } catch (Exception e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("非預期錯誤");
            sweetAlertResponse.setMessage(e.getMessage());
        }
        return gson.toJson(sweetAlertResponse);
    }

    /**
     * 啟用合約
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/api/enable/{contractId}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String enabledContract(
            Authentication authentication
            , @PathVariable("contractId") long contractId) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            if (callerInfoOptional.isPresent()) {
                gson = new GsonBuilder().serializeNulls().create();
                contractService.enableContract(
                        contractId
                        , callerInfoOptional.get().getUserEntity().getUserId().longValue()
                );
                sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
                sweetAlertResponse.setTitle("啟用成功");
            }
        } catch (Exception e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("非預期錯誤");
            sweetAlertResponse.setMessage(e.getMessage());
        }
        return gson.toJson(sweetAlertResponse);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/batchContinueEnabledContract", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String continueEnabledContractByCondition(
            Authentication authentication
            , @RequestBody String requestBody) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            gson = new GsonBuilder().serializeNulls().create();
            Map<String, Object> conditionMap = beanConverterUtils.convertJsonToMap(requestBody);
            Map<String, Object> resultMap = contractDataGateway.searchListByCondition(conditionMap);
            List<Contract> contractList = (List<Contract>) resultMap.get("data");
            contractList.stream().forEach(contract -> {
                contractService.continueContractWithCurrentDateTime(
                        contract,
                        callerInfoOptional.get()
                );
            });
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
            sweetAlertResponse.setTitle("處理成功");
        } catch (Exception e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("處理失敗");
            sweetAlertResponse.setMessage(e.getMessage());
        }
        return gson.toJson(sweetAlertResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/cancelInitial/{contractId}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String cancelInitialContract(Authentication authentication, @PathVariable("contractId") long contractId) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            Optional<Contract> contractOptional = contractDataGateway.findByContractId(contractId);
            if (callerInfoOptional.isPresent() && contractOptional.isPresent()) {
                contractService.cancelInitialContract(
                        contractOptional.get(),
                        callerInfoOptional.get().getUserEntity().getUserId().longValue()
                );
            }
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
            sweetAlertResponse.setTitle("處理成功");
        } catch (DeleteBilledBillingItemException e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("處理失敗");
            sweetAlertResponse.setMessage("費用已被出帳，無法刪除");
        }
        return gson.toJson(sweetAlertResponse);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/serverSideProcessingSearch", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String searchByParameter(
            Authentication authentication
            , @RequestBody String requestBody) {
        Map dataMap = new HashMap();
        try {
            gson = new GsonBuilder().serializeNulls().create();
            PageInfo pageInfo = gson.fromJson(requestBody, PageInfo.class);
            List<ContractFetchView> contractFetchViewList = contractDataGateway.searchContractViewListByPageInfo(pageInfo);
            dataMap.put("data", contractFetchViewList);
            dataMap.put("status", "success");
            dataMap.put("totalCount", pageInfo.getTotalCount());
        } catch (Exception e) {
            dataMap.put("status", "danger");
            dataMap.put("message", "查詢失敗");
        }
        return gson.toJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/disable/{contractId}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<ContractDisableRes> disableContract(
            Authentication authentication
            , @PathVariable("contractId") long contractId) {
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            Optional<Contract> contractOptional = contractDataGateway.findByContractId(contractId);
            if (contractOptional.isPresent() && callerInfoOptional.isPresent()) {
                return ResponseEntity.ok(contractEndpointService.disableContract(
                        contractOptional.get()
                        , callerInfoOptional.get().getUserEntity().getUserId().longValue()
                ));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return (ResponseEntity<ContractDisableRes>) ResponseEntity.badRequest();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/expirationDateFulfill", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<ExpirationDateFulfillRes> contractExpirationDateFulfillment(@RequestBody String jsonBody) {
        Optional<ExpirationDateFulfillReq> expirationDateFulfillReqOpt
                = beanConverterUtils.convertJsonToObjWithTypeCast(jsonBody, ExpirationDateFulfillReq.class);
        if (expirationDateFulfillReqOpt.isPresent()) {
            return ResponseEntity.ok(contractEndpointService.contractExpirationDateFulfillment(expirationDateFulfillReqOpt.get()));
        } else {
            return (ResponseEntity<ExpirationDateFulfillRes>) ResponseEntity.badRequest();
        }
    }

    /**
     * 返回切割後的時間區間清單
     *
     * @param authentication
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/api/chargeIntervalPreview", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String previewCycle(
            Authentication authentication
            , @RequestBody String jsonBody) {
        ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
        Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
        gson = new GsonBuilder().serializeNulls().create();
        List<CustomInterval> resultList = new ArrayList<>();
        if (callerInfoOptional.isPresent()) {
            HashMap<String, Object> map = beanConverterUtils.convertJsonToMap(jsonBody);
            resultList = cycleService.chargeIntervalListPreviewByMap(map);

        }
        return gson.toJson(resultList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/downloadCurrentlyNotExpireContractReport", produces = "application/text;charset=utf-8")
    @ResponseBody
    String downloadCurrentlyNotExpireContractReport() {
        Map dataMap = new HashMap();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            reportService.genCurrentlyNotExpireContractReport(out);
            byte[] media = out.toByteArray();
            String base64Byte = Base64.getEncoder().encodeToString(media);
            dataMap.put("reportBody", base64Byte);
            dataMap.put("status", "success");
            dataMap.put("title", "報表產生成功");
        } catch (Exception e) {
            dataMap.put("status", "error");
            dataMap.put("title", "操作失敗");
            dataMap.put("message", e.getMessage());
        }
        return gson.toJson(dataMap);
    }

    @Override
    public String getDefaultPage() {
        return DEFAULT_DISPATCH_PAGE;
    }
}
