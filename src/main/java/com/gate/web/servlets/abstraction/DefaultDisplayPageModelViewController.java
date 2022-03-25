package com.gate.web.servlets.abstraction;

import com.gateweb.charge.security.ChargeUserPrinciple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

public abstract class DefaultDisplayPageModelViewController {

    private final Logger logger = LogManager.getLogger(getClass());

    public abstract String getDefaultPage();

    public ChargeUserPrinciple getChargeUserPrinciple(Authentication authentication) {
        return (ChargeUserPrinciple) authentication.getPrincipal();
    }

    @RequestMapping(method = RequestMethod.POST)
    ModelAndView defaultPost() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(getDefaultPage());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET)
    ModelAndView defaultGet() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(getDefaultPage());
        return modelAndView;
    }

}
