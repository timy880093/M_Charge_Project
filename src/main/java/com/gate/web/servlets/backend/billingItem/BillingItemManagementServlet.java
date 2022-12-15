package com.gate.web.servlets.backend.billingItem;

import com.gate.web.facades.UserService;
import com.gate.web.servlets.abstraction.DefaultDisplayPageModelViewController;
import com.gateweb.charge.frontEndIntegration.bean.OutToBillRequest;
import com.gateweb.charge.frontEndIntegration.bean.PageableQueryResponse;
import com.gateweb.charge.frontEndIntegration.bean.SweetAlertResponse;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.charge.frontEndIntegration.enumeration.PageableQueryStatus;
import com.gateweb.charge.frontEndIntegration.enumeration.SweetAlertStatus;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.report.bean.BillingItemReport;
import com.gateweb.charge.security.ChargeUserPrinciple;
import com.gateweb.charge.service.BillService;
import com.gateweb.charge.service.BillingService;
import com.gateweb.charge.service.ContractService;
import com.gateweb.charge.service.dataGateway.BillingItemDataGateway;
import com.gateweb.orm.charge.entity.view.BillingItemFetchView;
import com.gateweb.orm.charge.repository.BillingItemFetchViewRepository;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.utils.CsvUtils;
import com.gateweb.utils.bean.BeanConverterUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.*;

@RequestMapping("/backendAdmin/billingItemManagementServlet")
@Controller
public class BillingItemManagementServlet extends DefaultDisplayPageModelViewController {
    private final Logger logger = LogManager.getLogger(getClass());
    private static final String DEFAULT_DISPATCH_PAGE = "/pages/billingItemListView.html";
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();
    Gson gson = new Gson();
    @Autowired
    BillingService billingService;
    @Autowired
    BillingItemFetchViewRepository billingItemFetchViewRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    BillService billService;
    @Autowired
    UserService userService;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    BillingItemDataGateway billingItemDataGateway;
    @Autowired
    ContractService contractService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/list", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String searchBillingItemList() {
        //過濾已結帳的商品。
        List<BillingItemFetchView> billingItemFetchViewList = billingItemFetchViewRepository.findByBillIdIsNull();
        return gson.toJson(billingItemFetchViewList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/searchBillingItemById/{billingItemId}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String searchBillingItemById(@PathVariable("billingItemId") long billingItemId) {
        //過濾已結帳的商品。
        Optional<BillingItemFetchView> billingItemFetchViewOptional = billingItemFetchViewRepository.findByBillingItemIdAndBillIdIsNull(billingItemId);
        if (billingItemFetchViewOptional.isPresent()) {
            return gson.toJson(billingItemFetchViewOptional.get());
        } else {
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/outputFeeByCondition", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String outToBillByCondition(@RequestBody String requestBody, Authentication authentication) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            ChargeUserPrinciple chargeUserPrinciple = (ChargeUserPrinciple) authentication.getPrincipal();
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByUserId(
                    chargeUserPrinciple.getUserInstance().getUserId().longValue()
            );
            gson = new GsonBuilder().serializeNulls().create();
            Optional<OutToBillRequest> outToBillRequestOptional = beanConverterUtils.convertJsonToObj(requestBody, OutToBillRequest.class);
            if (outToBillRequestOptional.isPresent()) {
                billService.transactionOutToBillByAjaxRequest(
                        outToBillRequestOptional.get(),
                        callerInfoOptional.get().getUserEntity().getUserId().longValue()
                );
            }
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
            sweetAlertResponse.setTitle("出帳成功");
        } catch (Exception e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("出帳失敗");
            sweetAlertResponse.setMessage(e.getMessage());
        }
        return gson.toJson(sweetAlertResponse);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/batch/deleteBillingItem", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String deleteBillingItem(Authentication authentication, @RequestBody String requestBody) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            ChargeUserPrinciple chargeUserPrinciple = (ChargeUserPrinciple) authentication.getPrincipal();
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            gson = new GsonBuilder().serializeNulls().create();
            HashMap<String, Object> billMap = beanConverterUtils.convertJsonToMap(requestBody);
            billingService.deleteBillingItemByMap(
                    billMap,
                    callerInfoOptional.get().getUserEntity().getUserId().longValue()
            );
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
            sweetAlertResponse.setTitle("刪除成功");
        } catch (Exception e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("刪除失敗");
            sweetAlertResponse.setMessage(e.getMessage());
        }
        return gson.toJson(sweetAlertResponse);
    }

    @PostMapping(value = "/api/downloadCsvByCompanyAndCondition", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String downloadCsvByCompanyAndCondition(@RequestBody String requestBody) {
        Map<String, Object> conditionMap = beanConverterUtils.convertJsonToMap(requestBody);
        List<BillingItemFetchView> billingItemFetchViewList = billingItemDataGateway.searchViewListByConditionMap(conditionMap);
        List<BillingItemReport> billingItemReportList = billingService.reportDataTransformer(billingItemFetchViewList);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CsvUtils.objectListToCsvOutputStream(billingItemReportList, out);
        byte[] media = out.toByteArray();
        String base64Byte = Base64.getEncoder().encodeToString(media);
        return base64Byte;
    }

    @PostMapping(value = "/api/serverSideProcessingSearch", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String serverSideProcessingSearch(Authentication authentication, @RequestBody String requestBody) {
        PageableQueryResponse pageableQueryResponse = new PageableQueryResponse();
        try {
            gson = new GsonBuilder().serializeNulls().create();
            PageInfo pageInfo = gson.fromJson(requestBody, PageInfo.class);
            List<BillingItemFetchView> billingItemFetchViewList = billingItemDataGateway.searchBillingItemFetchViewListByPageInfo(pageInfo);
            pageableQueryResponse.setPageableQueryStatus(PageableQueryStatus.SUCCESS);
            pageableQueryResponse.setData(billingItemFetchViewList);
            pageableQueryResponse.setTotalCount(pageInfo.getTotalCount());
        } catch (Exception e) {
            pageableQueryResponse.setPageableQueryStatus(PageableQueryStatus.ERROR);
            logger.error("serverSideProcessingSearch:{}", e.getMessage());
        }
        return gson.toJson(pageableQueryResponse);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/calculateContractFeeAndOutToBill", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String calculateContractFeeAndOutToBill(Authentication authentication, @RequestBody String requestBody) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            ChargeUserPrinciple chargeUserPrinciple = (ChargeUserPrinciple) authentication.getPrincipal();
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            Optional<OutToBillRequest> outToBillRequestOpt = beanConverterUtils.convertJsonToObj(requestBody, OutToBillRequest.class);
            if (outToBillRequestOpt.isPresent() && callerInfoOptional.isPresent()) {
                contractService.calculateContractFeeAndOutToBill(
                        outToBillRequestOpt.get()
                        , callerInfoOptional.get().getUserEntity().getUserId().longValue()
                );
                sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
                sweetAlertResponse.setTitle("處理完成");
            }
        } catch (Exception e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("處理失敗");
            sweetAlertResponse.setMessage(e.getMessage());
        }
        return gson.toJson(sweetAlertResponse);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/calculateByParameter", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String calculateFeeByParameter(
            Authentication authentication
            , @RequestBody String requestBody) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            gson = new GsonBuilder().serializeNulls().create();
            Optional<OutToBillRequest> outToBillRequestOpt = beanConverterUtils.convertJsonToObj(requestBody, OutToBillRequest.class);
            if (outToBillRequestOpt.isPresent() && callerInfoOptional.isPresent()) {
                contractService.calculateContractFee(
                        outToBillRequestOpt.get(), callerInfoOptional.get().getUserEntity().getUserId().longValue()
                );
            }
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
            sweetAlertResponse.setTitle("操作完成");
        } catch (Exception e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("查詢失敗");
        }
        return gson.toJson(sweetAlertResponse);
    }

    @Override
    public String getDefaultPage() {
        return DEFAULT_DISPATCH_PAGE;
    }
}
