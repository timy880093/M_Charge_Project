package com.gate.web.servlets.backend.bill;

import com.gate.web.facades.UserService;
import com.gate.web.servlets.abstraction.DefaultDisplayPageModelViewController;
import com.gateweb.charge.component.annotated.InputAndOutputStreamUtils;
import com.gateweb.charge.constant.CompanyConstant;
import com.gateweb.charge.enumeration.BillStatus;
import com.gateweb.charge.exception.CancelAlreadyPaidBillException;
import com.gateweb.charge.exception.InvalidUserException;
import com.gateweb.charge.exception.PayAlreadyException;
import com.gateweb.charge.frontEndIntegration.bean.*;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.charge.frontEndIntegration.enumeration.PageableQueryStatus;
import com.gateweb.charge.frontEndIntegration.enumeration.SweetAlertStatus;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.report.bean.OneReportDownloadRes;
import com.gateweb.charge.security.ChargeUserPrinciple;
import com.gateweb.charge.service.BillService;
import com.gateweb.charge.service.BillingService;
import com.gateweb.charge.service.ReportService;
import com.gateweb.charge.service.dataGateway.BillDataGateway;
import com.gateweb.charge.service.impl.NoticeService;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.view.BillFetchView;
import com.gateweb.orm.charge.repository.BillFetchViewRepository;
import com.gateweb.orm.charge.repository.BillRepository;
import com.gateweb.utils.JsonUtils;
import com.gateweb.utils.bean.BeanConverterUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/backendAdmin/billManagementServlet")
@Controller
public class BillManagementServlet extends DefaultDisplayPageModelViewController {
    private final Logger logger = LogManager.getLogger(getClass());
    private static final String DEFAULT_DISPATCH_PAGE = "/pages/billListView.html";
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    @Autowired
    BillRepository billRepository;
    @Autowired
    BillingService billingService;
    @Autowired
    BillFetchViewRepository billFetchViewRepository;
    @Autowired
    BillService billService;
    @Autowired
    ReportService reportService;
    @Autowired
    InputAndOutputStreamUtils inputAndOutputStreamUtils;
    @Autowired
    BillDataGateway billDataGatewayImpl;
    @Autowired
    UserService userService;
    @Autowired
    NoticeService noticeService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/list", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String searchFetchViewList(HttpServletRequest request, HttpServletResponse response) {
        List<BillFetchView> billFetchViewList = billFetchViewRepository.findAll();
        Map dataMap = new HashMap();
        dataMap.put("data", billFetchViewList);
        return JsonUtils.gsonToJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/listByCondition", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String searchBillListByCondition(
            @RequestParam MultiValueMap<String, String> paramMap, Authentication authentication
    ) {
        Map dataMap = new HashMap();
        try {
            List<BillFetchView> billFetchViewList = new ArrayList<>();
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByUserId(chargeUserPrinciple.getUserInstance().getUserId().longValue());
            if (callerInfoOptional.isPresent()) {
                BillSearchCondition billSearchCondition = new BillSearchCondition();
                if (paramMap.containsKey(CompanyConstant.COMPANY_ID) && !paramMap.get(CompanyConstant.COMPANY_ID).get(0).isEmpty()) {
                    billSearchCondition.setCompanyId(Long.valueOf(paramMap.get(CompanyConstant.COMPANY_ID).get(0)));
                }
                if (paramMap.containsKey("billStatus") && !paramMap.get("billStatus").get(0).isEmpty()) {
                    billSearchCondition.setBillStatus(BillStatus.valueOf(paramMap.get("billStatus").get(0)));
                }
                billFetchViewList = billDataGatewayImpl.searchByCondition(billSearchCondition);
            }
            dataMap.put("data", billFetchViewList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.gsonToJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/markAsPaid", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String markAsPaid(Authentication authentication, @RequestBody String requestBody) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            Optional<PayInfo> payInfoOptional = beanConverterUtils.convertJsonToObj(requestBody, PayInfo.class);
            if (payInfoOptional.isPresent()) {
                ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
                Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByUserId(chargeUserPrinciple.getUserInstance().getUserId().longValue());
                if (callerInfoOptional.isPresent()) {
                    billService.transactionPayBill(payInfoOptional.get(), callerInfoOptional.get().getUserEntity().getUserId().longValue());
                }
                sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
                sweetAlertResponse.setTitle("入帳成功");
            }
        } catch (PayAlreadyException pae) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.WARNING);
            sweetAlertResponse.setTitle("重覆入帳");
        } catch (Exception e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("入帳失敗");
        }
        return JsonUtils.gsonToJson(sweetAlertResponse);
    }

    @GetMapping(value = "/api/cancelPayment/{billId}", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String cancelPayment(Authentication authentication, @PathVariable(name = "billId") Long billId) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByUserId(chargeUserPrinciple.getUserInstance().getUserId().longValue());
            if (callerInfoOptional.isPresent()) {
                billService.transactionCancelPayment(billId, callerInfoOptional.get());
            }
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
            sweetAlertResponse.setTitle("取消成功");
        } catch (Exception e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("取消失敗");
            sweetAlertResponse.setMessage(e.getMessage());
        }
        return JsonUtils.gsonToJson(sweetAlertResponse);
    }

    @GetMapping(value = "/api/cancel/{billId}", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String cancelBill(
            Authentication authentication
            , @PathVariable(name = "billId") Long billId) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByUserId(chargeUserPrinciple.getUserInstance().getUserId().longValue());
            if (callerInfoOptional.isPresent()) {
                billService.transactionCancelBillByBillId(billId, callerInfoOptional.get());
            }
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
            sweetAlertResponse.setTitle("取消成功");
        } catch (CancelAlreadyPaidBillException capbe) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("取消失敗");
            sweetAlertResponse.setMessage("不能取消已入帳的資料");
        } catch (Exception e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("取消失敗");
            sweetAlertResponse.setMessage(e.getMessage());
        }
        return JsonUtils.gsonToJson(sweetAlertResponse);
    }

    @PostMapping(value = "/sendPaymentRequestEmail", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String sendPaymentRequestEmail(Authentication authentication, @RequestBody String requestBody) {
        Map resultMap = new HashMap();
        try {
            CallerInfo callerInfo = userService.getCallerInfoByAuthentication(authentication);
            Optional<OperationObject> operationObjectOpt = beanConverterUtils.convertJsonToObj(requestBody, OperationObject.class);
            if (operationObjectOpt.isPresent()) {
                resultMap = noticeService.sendPaymentRequestEmail(operationObjectOpt.get(), callerInfo);
            }
        } catch (InvalidUserException iue) {
            resultMap = DefaultResponseDataMap.invalidUserResponseMap();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JsonUtils.gsonToJson(resultMap);
    }

    @PostMapping(value = "/downloadConvenientStoreReport", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String downloadConvenientStoreReport(
            Authentication authentication
            , @RequestBody String requestBody) {
        OneReportDownloadRes oneReportDownloadRes = new OneReportDownloadRes();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByUserId(chargeUserPrinciple.getUserInstance().getUserId().longValue());
            Optional<OperationObject> operationObjectOpt = beanConverterUtils.convertJsonToObj(requestBody, OperationObject.class);
            if (callerInfoOptional.isPresent() && operationObjectOpt.isPresent()) {
                List<Bill> billList = billDataGatewayImpl.searchByOperationObj(operationObjectOpt.get());
                if (!billList.isEmpty()) {
                    reportService.genScsbConvenientStoreFile(out, billList);
                    byte[] media = out.toByteArray();
                    String base64Byte = Base64.getEncoder().encodeToString(media);
                    oneReportDownloadRes.setReportBody(base64Byte);
                    oneReportDownloadRes.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
                    oneReportDownloadRes.setTitle("操作成功");
                } else {
                    oneReportDownloadRes.setSweetAlertStatus(SweetAlertStatus.WARNING);
                    oneReportDownloadRes.setTitle("操作失敗");
                    oneReportDownloadRes.setMessage("沒有任何項目在選擇的條件範圍內");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            oneReportDownloadRes.setSweetAlertStatus(SweetAlertStatus.ERROR);
            oneReportDownloadRes.setTitle("報表產生失敗");
            oneReportDownloadRes.setMessage(e.getMessage());
        }
        return JsonUtils.gsonToJson(oneReportDownloadRes);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/downloadInvoiceImportReport", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String downloadInvoiceReportForImport(
            Authentication authentication
            , @RequestBody String requestBody) {
        OneReportDownloadRes oneReportDownloadRes = new OneReportDownloadRes();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            if (callerInfoOptional.isPresent()) {
                Optional<OperationObject> operationObjectOpt = beanConverterUtils.convertJsonToObj(requestBody, OperationObject.class);
                if (operationObjectOpt.isPresent()) {
                    List<Bill> billList = billDataGatewayImpl.searchByOperationObj(operationObjectOpt.get());
                    if (!billList.isEmpty()) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        reportService.genInvoiceExcelFile(out, billList);
                        byte[] media = out.toByteArray();
                        String base64Byte = Base64.getEncoder().encodeToString(media);
                        oneReportDownloadRes.setReportBody(base64Byte);
                        oneReportDownloadRes.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
                        oneReportDownloadRes.setTitle("下載成功");
                    } else {
                        oneReportDownloadRes.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
                        oneReportDownloadRes.setTitle("沒有任何項目在選擇的條件範圍內");
                    }
                }
            }
        } catch (Exception e) {
            oneReportDownloadRes.setSweetAlertStatus(SweetAlertStatus.WARNING);
            oneReportDownloadRes.setTitle("操作失敗");
            oneReportDownloadRes.setMessage(e.getMessage());
        }
        return JsonUtils.gsonToJson(oneReportDownloadRes);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/cancelByYearMonth/{yearMonth}", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String cancelByYearMonth(
            Authentication authentication
            , @PathVariable(name = "yearMonth") String yearMonth) {
        Map dataMap = new HashMap();
        List<Bill> successList = new ArrayList<>();
        List<Bill> failList = new ArrayList<>();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            if (callerInfoOptional.isPresent()) {
                Date startYearMonthDate = new SimpleDateFormat("yyyyMM").parse(yearMonth);
                Date endYearMonthDate = DateUtils.addMonths(startYearMonthDate, 1);
                Timestamp startTimestamp = new Timestamp(startYearMonthDate.getTime());
                Timestamp endTimestamp = new Timestamp(endYearMonthDate.getTime());
                List<Bill> billList = billRepository.findByCreateDateGreaterThanAndCreateDateLessThan(startTimestamp, endTimestamp);
                billList.stream().forEach(bill -> {
                    try {
                        billService.transactionCancelBillByBillId(bill.getBillId(), callerInfoOptional.get());
                        successList.add(bill);
                    } catch (CancelAlreadyPaidBillException pae) {
                        failList.add(bill);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        failList.add(bill);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!failList.isEmpty()) {
                dataMap.put("status", "warning");
                dataMap.put("message", "跳過" + failList.size() + "個操作失敗的項目");
            } else {
                dataMap.put("status", "success");
                dataMap.put("message", "操作成功,已刪除" + successList.size() + "個項目");
            }
        }
        return JsonUtils.gsonToJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/markAsPaidByCondition", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String markAsPaidByCondition(
            Authentication authentication
            , @RequestBody String requestBody) {
        Map dataMap = new HashMap();
        List<Bill> successList = new ArrayList<>();
        List<Bill> failList = new ArrayList<>();
        int totalSize = 0;
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            if (callerInfoOptional.isPresent()) {
                HashMap<String, Object> conditionMap = beanConverterUtils.convertJsonToMap(requestBody);
                //根據conditionMap查詢清單
                List<Bill> billList = billDataGatewayImpl.searchListByCondition(conditionMap);
                billList.stream().forEach(bill -> {
                    try {
                        billService.transactionInBill(bill.getBillId(), callerInfoOptional.get());
                        successList.add(bill);
                    } catch (PayAlreadyException pae) {
                        failList.add(bill);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        failList.add(bill);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!failList.isEmpty()) {
                dataMap.put("status", "warning");
                dataMap.put("message", "跳過" + failList.size() + "/" + totalSize + "個操作失敗的項目");
            } else {
                dataMap.put("status", "success");
                dataMap.put("message", "操作成功,已入帳" + successList.size() + "個項目");
            }
        }
        return JsonUtils.gsonToJson(dataMap);
    }

    @PostMapping(value = "/api/serverSideProcessingSearch", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String serverSideProcessingSearch(@RequestBody String requestBody) {
        PageableQueryResponse pageableQueryResponse = new PageableQueryResponse();
        try {
            PageInfo pageInfo = JsonUtils.gsonFromJson(requestBody, PageInfo.class);
            List<Bill> billList = billService.serverSideProcessingSearchByPageInfo(pageInfo);
            List<BillFetchView> billViewList = billDataGatewayImpl.fetchViews(billList);
            pageableQueryResponse.setPageableQueryStatus(PageableQueryStatus.SUCCESS);
            pageableQueryResponse.setData(billViewList);
            pageableQueryResponse.setTotalCount(pageInfo.getTotalCount());
        } catch (Exception e) {
            pageableQueryResponse.setPageableQueryStatus(PageableQueryStatus.ERROR);
            logger.error("serverSideProcessingSearch:{}", e.getMessage());
        }
        return JsonUtils.gsonToJson(pageableQueryResponse);
    }

    @Override
    public String getDefaultPage() {
        return DEFAULT_DISPATCH_PAGE;
    }
}
