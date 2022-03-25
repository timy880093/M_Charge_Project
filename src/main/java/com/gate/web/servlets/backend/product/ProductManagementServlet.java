package com.gate.web.servlets.backend.product;

import com.gate.web.servlets.abstraction.DefaultDisplayPageServletSimpleImpl;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.security.ChargeUserPrinciple;
import com.gateweb.charge.service.ProductService;
import com.gateweb.orm.charge.entity.view.ProductFetchView;
import com.gateweb.orm.charge.repository.PackageRefFetchViewRepository;
import com.gateweb.orm.charge.repository.PackageRefRepository;
import com.gateweb.orm.charge.repository.ProductFetchViewRepository;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/backendAdmin/productManagementServlet")
@Controller
public class ProductManagementServlet extends DefaultDisplayPageServletSimpleImpl {
    private static final String DEFAULT_SEARCH_LIST_DISPATCH_PAGE = "/backendAdmin/product/list.jsp";

    @Autowired
    ProductService productService;
    @Autowired
    PackageRefFetchViewRepository productReferenceFetchViewRepository;
    @Autowired
    ProductFetchViewRepository productFetchViewRepository;
    @Autowired
    PackageRefRepository productReferenceRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/api/list", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String searchFetchViewList(final HttpServletRequest request,
                               final HttpServletResponse response) {
        final List<ProductFetchView> productFetchViewList = productFetchViewRepository.findAll();
        final Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data", productFetchViewList);
        return gson.toJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/serverSideProcessingSearch", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String serverSideProcessingSearch(final HttpServletRequest request, final HttpServletResponse response,
                                             final Authentication authentication, @RequestBody final String requestBody) {
        final Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            final ChargeUserPrinciple chargeUserPrinciple = (ChargeUserPrinciple) authentication.getPrincipal();
            Optional<CallerInfo> callerInfoOptional = userService
                    .getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            gson = new GsonBuilder().serializeNulls().create();
            PageInfo pageInfo = gson.fromJson(requestBody, PageInfo.class);
            List<ProductFetchView> productFetchViewList = productService
                    .serverSideProcessingSearchByPageInfo(pageInfo);
            dataMap.put("status", "success");
            dataMap.put("data", productFetchViewList);
            dataMap.put("totalCount", pageInfo.getTotalCount());
        } catch (Exception e) {
            dataMap.put("status", "danger");
            e.printStackTrace();
        }
        return gson.toJson(dataMap);
    }

    @Override
    public String getDefaultDisplayPage() {
        return DEFAULT_SEARCH_LIST_DISPATCH_PAGE;
    }

    @Override
    public String getDefaultTemplatePage() {
        return BOOTSTRAP_TEMPLATE_PAGE;
    }

}
