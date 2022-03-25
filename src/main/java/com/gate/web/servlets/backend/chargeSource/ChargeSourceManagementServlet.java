package com.gate.web.servlets.backend.chargeSource;

import com.gate.web.servlets.abstraction.DefaultDisplayPageModelViewController;
import com.gateweb.bridge.service.ChargeSourceService;
import com.gateweb.charge.frontEndIntegration.bean.SweetAlertResponse;
import com.gateweb.charge.frontEndIntegration.enumeration.SweetAlertStatus;
import com.gateweb.charge.report.bean.ChargeSourceInvoiceCountDiffReport;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.utils.CsvUtils;
import com.gateweb.utils.JsonUtils;
import com.gateweb.utils.LocalDateTimeUtils;
import com.gateweb.utils.bean.BeanConverterUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("/backendAdmin/chargeSourceManagementServlet")
@Controller
public class ChargeSourceManagementServlet extends DefaultDisplayPageModelViewController {
    private final Logger logger = LogManager.getLogger(getClass());
    private static final String DEFAULT_DISPATCH_PAGE = "/pages/chargeSourceEinvCountListView.html";
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    @Autowired
    ChargeSourceService chargeSourceService;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    @Qualifier("regenTaskMap")
    ConcurrentHashMap<LocalDateTime, String> regenTaskMap;

    @RequestMapping(method = RequestMethod.GET, value = "/api/list", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String searchList() {
        YearMonth currentYm = YearMonth.now();
        String ymString = LocalDateTimeUtils.yearMonthToString(currentYm, "yyyyMM");
        List<ChargeSourceInvoiceCountDiffReport> chargeSourceInvoiceCountDiffReportList =
                chargeSourceService.getChargeSourceInvoiceCountDiffReport(ymString, false);
        return JsonUtils.gsonToJson(chargeSourceInvoiceCountDiffReportList);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/list/condition", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String searchListWithCondition(@RequestBody String requestBody) {
        Map<String, Object> conditionMap = beanConverterUtils.convertJsonToMap(requestBody);
        List<ChargeSourceInvoiceCountDiffReport> chargeSourceInvoiceCountDiffReportList =
                chargeSourceService.getChargeSourceInvoiceCountDiffReport(conditionMap);
        return JsonUtils.gsonToJson(chargeSourceInvoiceCountDiffReportList);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/recalculateByCondition", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String recalculateByCondition(@RequestBody String requestBody) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        Map<String, Object> conditionMap = beanConverterUtils.convertJsonToMap(requestBody);
        LocalDateTime key = LocalDateTime.now();
        try {
            if (regenTaskMap.isEmpty()) {
                logger.info("recalculateByCondition add " + key, key);
                regenTaskMap.put(key, JsonUtils.gsonToJson(conditionMap));
                chargeSourceService.recalculateByConditionMap(conditionMap);
                regenTaskMap.remove(key);
                logger.info("recalculateByCondition remove", key);
                sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
                sweetAlertResponse.setTitle("排程執行結束");
            } else {
                sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.WARNING);
                sweetAlertResponse.setTitle("其它排程執行中");
            }
        } catch (Exception ex) {
            regenTaskMap.remove(key);
            logger.info("recalculateByCondition remove " + key, key);
            sweetAlertResponse.setTitle("排程意外結束");
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setMessage(ex.getMessage());
            logger.error(ex.getMessage());
        }
        return JsonUtils.gsonToJson(sweetAlertResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/getRegenTaskMap", produces = "application/text;charset=utf8")
    @ResponseBody
    public String getRegenTaskMap() {
        return JsonUtils.gsonToJson(regenTaskMap);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/downloadInvoiceCountDiffCsvByCondition", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String downloadInvoiceCountDiffCsvByCondition(@RequestBody String requestBody) {
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> conditionMap = beanConverterUtils.convertJsonToMap(requestBody);
            List<ChargeSourceInvoiceCountDiffReport> chargeSourceInvoiceCountDiffReportList = chargeSourceService.getChargeSourceInvoiceCountDiffReport(conditionMap);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CsvUtils.objectListToCsvOutputStream(chargeSourceInvoiceCountDiffReportList, out);
            byte[] media = out.toByteArray();
            String base64Byte = Base64.getEncoder().encodeToString(media);
            resultMap.put("reportBody", base64Byte);
            resultMap.put("status", "success");
            resultMap.put("title", "報表產生成功");
        } catch (Exception ex) {
            resultMap.put("status", "error");
            resultMap.put("title", "報表產生失敗");
            resultMap.put("message", ex.getMessage());
        }
        return JsonUtils.gsonToJson(resultMap);
    }

    @Override
    public String getDefaultPage() {
        return DEFAULT_DISPATCH_PAGE;
    }
}
