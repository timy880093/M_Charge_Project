package com.gate.web.servlets.abstraction;

import com.gate.web.facades.UserService;
import com.gateweb.charge.security.ChargeUserPrinciple;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * abstract servlet的實現需要自訂webContext，所以先不實作。
 * 留一個未來可以用的抽象類別，也一併取代舊的mvcServlet。
 */
public abstract class DefaultDisplayPageServletSimpleImpl implements DefaultDisplayPageSimpleServlet {
    protected final Logger logger = LogManager.getLogger(getClass());
    protected static final String TEMPLATE_PAGE = "/backendAdmin/template/template.jsp";
    protected static final String BOOTSTRAP_TEMPLATE_PAGE = "/backendAdmin/template/bootstrap_template.jsp";
    protected static final String DISPATCH_PAGE = "DISPATCH_PAGE";
    protected Gson gson = new Gson();

    @Autowired
    protected UserService userService;

    public String defaultDisplayPage(@RequestParam MultiValueMap<String, String> paramMap,
                                     Model model, HttpServletRequest request) {
        logger.debug("defaultPost model:   " + model);
        logger.debug("defaultPost paramMap:   " + paramMap);
        request.setAttribute(DISPATCH_PAGE, getDefaultDisplayPage());
        return getDefaultTemplatePage();
    }

    public ChargeUserPrinciple getChargeUserPrinciple(Authentication authentication) {
        return (ChargeUserPrinciple) authentication.getPrincipal();
    }

    @RequestMapping(method = RequestMethod.POST)
    String defaultPost(@RequestParam MultiValueMap<String, String> paramMap,
                       Model model, HttpServletRequest request) {
        return defaultDisplayPage(paramMap, model, request);
    }

    @RequestMapping(method = RequestMethod.GET)
    String defaultGet(@RequestParam MultiValueMap<String, String> paramMap,
                      Model model, HttpServletRequest request){
        return defaultDisplayPage(paramMap, model, request);
    }

}
