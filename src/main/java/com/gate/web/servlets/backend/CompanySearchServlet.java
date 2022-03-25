package com.gate.web.servlets.backend;

import com.gate.web.facades.CompanyService;
import com.gate.web.servlets.abstraction.DefaultDisplayPageServletSimpleImpl;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/backendAdmin/companySearchServlet")
@Controller
public class CompanySearchServlet extends DefaultDisplayPageServletSimpleImpl {

    private static final String DEFAULT_SEARCH_LIST_DISPATCH_PAGE = "/backendAdmin/companyCharge/company_list.jsp";
    @Autowired
    CompanyService companyService;

    @Autowired
    CompanyRepository companyRepository;

    @RequestMapping(method = RequestMethod.GET, params = "method=list", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String searchList(@RequestParam MultiValueMap<String, String> paramMap, Model model) {
        List<Integer> verifyStatusList = new ArrayList<>();
        verifyStatusList.add(3);
        verifyStatusList.add(9);
        List<Company> companyList = companyRepository.findByVerifyStatusIsIn(verifyStatusList);
        Map dataMap = new HashMap();
        dataMap.put("data", companyList);
        return gson.toJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/list", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String searchList() {
        Map dataMap = new HashMap();
        try {
            List<Company> companyList = companyRepository.findAll();
            dataMap.put("data", companyList);
        } catch (Exception e) {
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

