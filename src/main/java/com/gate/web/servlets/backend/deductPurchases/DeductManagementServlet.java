package com.gate.web.servlets.backend.deductPurchases;

import com.gate.web.facades.UserService;
import com.gate.web.servlets.abstraction.DefaultDisplayPageModelViewController;
import com.gateweb.charge.deduct.component.DeductPurchaseComponent;
import com.gateweb.charge.exception.MissingRequiredPropertiesException;
import com.gateweb.charge.frontEndIntegration.bean.SweetAlertResponse;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.charge.frontEndIntegration.enumeration.SweetAlertStatus;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.security.ChargeUserPrinciple;
import com.gateweb.charge.service.BillingService;
import com.gateweb.charge.service.DeductService;
import com.gateweb.orm.charge.entity.Deduct;
import com.gateweb.orm.charge.entity.view.DeductFetchView;
import com.gateweb.orm.charge.entity.view.DeductHistoryFetchView;
import com.gateweb.orm.charge.repository.DeductFetchViewRepository;
import com.gateweb.orm.charge.repository.DeductHistoryFetchViewRepository;
import com.gateweb.utils.JsonUtils;
import com.gateweb.utils.bean.BeanConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/backendAdmin/deductManagementServlet")
@Controller
public class DeductManagementServlet extends DefaultDisplayPageModelViewController {
    private static final String DEFAULT_DISPATCH_PAGE = "/pages/deductListView.html";
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    @Autowired
    DeductService deductService;
    @Autowired
    BillingService billingService;
    @Autowired
    DeductFetchViewRepository deductFetchViewRepository;
    @Autowired
    DeductHistoryFetchViewRepository deductHistoryFetchViewRepository;
    @Autowired
    UserService userService;
    @Autowired
    DeductPurchaseComponent deductPurchaseComponent;

    @RequestMapping(method = RequestMethod.GET, value = "/api/list", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String searchDeductFetchViewList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            List<DeductFetchView> deductFetchViewList = deductFetchViewRepository.findAll();
            dataMap.put("data", deductFetchViewList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.gsonToJson(dataMap);
    }

    /**
     * 因為無法使用Hibernate Entity的Annotation做mapping
     * hibernate 的one to要用many to one 實現
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/api/findDeductHistoryByDeductId/{deductId}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String searchDeductHistoryByDeductId(
            HttpServletRequest request
            , HttpServletResponse response
            , Authentication authentication
            , @PathVariable("deductId") long deductId) {
        Map dataMap = new HashMap();
        try {
            ChargeUserPrinciple chargeUserPrinciple = (ChargeUserPrinciple) authentication.getPrincipal();
            List<DeductHistoryFetchView> deductHistoryFetchViewList = deductService.findDeductHistoryFetchViewByDeductId(deductId);
            dataMap.put("data", deductHistoryFetchViewList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.gsonToJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/priceBaseDeductPurchase", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String createPrepayment(
            Authentication authentication
            , @RequestBody String requestBody) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            ChargeUserPrinciple chargeUserPrinciple = (ChargeUserPrinciple) authentication.getPrincipal();
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            HashMap parameterMap = beanConverterUtils.convertJsonToMap(requestBody);
            Optional<Deduct> deductOptional = deductPurchaseComponent.purchaseDeductByMap(parameterMap, callerInfoOptional.get());
            if (deductOptional.isPresent()) {
                sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
                sweetAlertResponse.setTitle("預繳建立完成");
            } else {
                sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
                sweetAlertResponse.setTitle("預繳建立失敗");
            }
        } catch (MissingRequiredPropertiesException mrpe) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.WARNING);
            sweetAlertResponse.setTitle("預繳建立失敗");
            sweetAlertResponse.setMessage(mrpe.getMessage());
        } catch (Exception e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("預繳建立失敗");
            sweetAlertResponse.setMessage(e.getMessage());
        }
        return JsonUtils.gsonToJson(sweetAlertResponse);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/serverSideProcessingSearch", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String serverSideProcessingSearch(
            Authentication authentication
            , @RequestBody String requestBody) {
        Map dataMap = new HashMap();
        try {
            PageInfo pageInfo = JsonUtils.gsonFromJson(requestBody, PageInfo.class);
            List<DeductFetchView> deductFetchViewList = deductService.serverSideProcessingSearchByPageInfo(pageInfo);
            dataMap.put("status", "success");
            dataMap.put("data", deductFetchViewList);
            dataMap.put("totalCount", pageInfo.getTotalCount());
        } catch (Exception e) {
            dataMap.put("status", "danger");
            e.printStackTrace();
        }
        return JsonUtils.gsonToJson(dataMap);
    }

    @Override
    public String getDefaultPage() {
        return DEFAULT_DISPATCH_PAGE;
    }
}
