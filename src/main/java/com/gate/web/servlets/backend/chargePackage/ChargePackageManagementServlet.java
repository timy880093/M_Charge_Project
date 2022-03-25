package com.gate.web.servlets.backend.chargePackage;

import com.gate.web.facades.UserService;
import com.gate.web.servlets.abstraction.DefaultDisplayPageModelViewController;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.security.ChargeUserPrinciple;
import com.gateweb.charge.service.ChargePackageService;
import com.gateweb.orm.charge.entity.view.ChargePackageFetchView;
import com.gateweb.orm.charge.repository.ChargePackageFetchViewRepository;
import com.gateweb.orm.charge.repository.ChargePackageRepository;
import com.gateweb.orm.charge.repository.PackageRefRepository;
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
import java.util.*;

@RequestMapping("/backendAdmin/chargePackageManagementServlet")
@Controller
public class ChargePackageManagementServlet extends DefaultDisplayPageModelViewController {
    private static final String DEFAULT_DISPATCH_PAGE = "/pages/chargePackageListView.html";
    protected final Logger logger = LogManager.getLogger(getClass());
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    @Autowired
    ChargePackageService chargePackageService;
    @Autowired
    ChargePackageRepository chargePackageRepository;
    @Autowired
    ChargePackageFetchViewRepository chargePackageFetchViewRepository;
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/list", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String findAllChargePackageFetchView() {
        List<ChargePackageFetchView> chargePackageFetchViewList = chargePackageFetchViewRepository.findAll();
        return JsonUtils.gsonToJson(chargePackageFetchViewList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/availableList", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String findAvailableChargePackageFetchView(HttpServletRequest request, HttpServletResponse response) {
        Set<ChargePackageFetchView> chargePackageFetchViewList =
                new HashSet<>(chargePackageFetchViewRepository.findByEnabledIsTrue());
        return JsonUtils.gsonToJson(chargePackageFetchViewList);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/saveOrUpdate", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String saveOrUpdateChargePackage(
            HttpServletRequest request
            , HttpServletResponse response
            , Authentication authentication
            , @RequestBody String jsonBody) {
        Map dataMap = new HashMap();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            if (callerInfoOptional.isPresent()) {
                HashMap<String, Object> map = beanConverterUtils.convertJsonToMap(jsonBody);
                chargePackageService.transactionSaveChargePackageByMap(map, callerInfoOptional.get());
            }
            dataMap.put("status", "success");
            dataMap.put("message", "操作完成");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(jsonBody);
        }
        return JsonUtils.gsonToJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/delete/{packageId}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String deletePackage(
            Authentication authentication
            , @PathVariable("packageId") long packageId) {
        Map dataMap = new HashMap();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            if (callerInfoOptional.isPresent()) {
                chargePackageService.deleteChargePackage(packageId, callerInfoOptional.get());
            }
            dataMap.put("status", "success");
            dataMap.put("message", "操作完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.gsonToJson(dataMap);
    }

    @Override
    public String getDefaultPage() {
        return DEFAULT_DISPATCH_PAGE;
    }
}
