package com.gate.web.servlets.backend.chargeRule;

import com.gate.web.facades.UserService;
import com.gate.web.servlets.abstraction.DefaultDisplayPageModelViewController;
import com.gateweb.charge.exception.ItemHaveBeenOccupiedException;
import com.gateweb.charge.exception.MissingRequiredPropertiesException;
import com.gateweb.charge.frontEndIntegration.bean.SweetAlertResponse;
import com.gateweb.charge.frontEndIntegration.enumeration.SweetAlertStatus;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.report.bean.ChargeRuleServletView;
import com.gateweb.charge.security.ChargeUserPrinciple;
import com.gateweb.charge.service.ChargeRuleService;
import com.gateweb.charge.service.dataGateway.ChargeRuleDataGateway;
import com.gateweb.orm.charge.repository.ChargeRuleFetchViewRepository;
import com.gateweb.utils.JsonUtils;
import com.gateweb.utils.bean.BeanConverterUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@RequestMapping("/backendAdmin/chargeRuleManagementServlet")
@Controller
public class ChargeRuleManagementServlet extends DefaultDisplayPageModelViewController {
    private final Logger logger = LogManager.getLogger(getClass());
    private static final String DEFAULT_DISPATCH_PAGE = "/pages/chargeRuleListView.html";
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    @Autowired
    ChargeRuleService chargeRuleService;
    @Autowired
    ChargeRuleDataGateway chargeRuleDataGateway;
    @Autowired
    ChargeRuleFetchViewRepository chargeRuleFetchViewRepository;
    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/list", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String searchChargeRuleList() {
        Map dataMap = new HashMap();
        try {
            List<ChargeRuleServletView> chargeRuleFetchViewList = chargeRuleDataGateway.findAllChargeRuleServletView();
            dataMap.put("data", chargeRuleFetchViewList);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JsonUtils.gsonToJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/findViewById/{chargeRuleId}", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String getFetchView(@PathVariable Long chargeRuleId) {
        Optional<ChargeRuleServletView> chargeRuleFetchViewOptional = chargeRuleDataGateway.findById(chargeRuleId);
        if (chargeRuleFetchViewOptional.isPresent()) {
            return JsonUtils.gsonToJson(chargeRuleFetchViewOptional.get());
        } else {
            return "{}";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/saveOrUpdate", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String modifyChargeMode(Authentication authentication, @RequestBody String jsonBody) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            if (callerInfoOptional.isPresent()) {
                HashMap<String, Object> map = beanConverterUtils.convertJsonToMap(jsonBody);
                chargeRuleService.saveOrUpdateChargeRuleByMap(map, callerInfoOptional.get());
            }
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
            sweetAlertResponse.setTitle("更新成功");
        } catch (Exception e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("更新失敗");
            sweetAlertResponse.setMessage(e.getMessage());
        }
        return JsonUtils.gsonToJson(sweetAlertResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/delete/{chargeRuleId}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String deleteChargeMode(
            HttpServletRequest request
            , HttpServletResponse response
            , Authentication authentication
            , @PathVariable Long chargeRuleId) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            if (callerInfoOptional.isPresent()) {
                chargeRuleService.transactionDeleteChargeRule(chargeRuleId, callerInfoOptional.get());
            }
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
            sweetAlertResponse.setTitle("刪除成功");
        } catch (ItemHaveBeenOccupiedException e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.WARNING);
            sweetAlertResponse.setTitle("該項目目前被佔用中");
        } catch (MissingRequiredPropertiesException e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.WARNING);
            sweetAlertResponse.setTitle("找不到這個項目");
        } catch (Exception e) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("操作失敗");
            sweetAlertResponse.setMessage(e.getMessage());
        }
        return JsonUtils.gsonToJson(sweetAlertResponse);
    }

    @Override
    public String getDefaultPage() {
        return DEFAULT_DISPATCH_PAGE;
    }
}
