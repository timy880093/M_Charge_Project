package com.gate.web.servlets.backend.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginServlet {

    @GetMapping("/")
    public ModelAndView redirectWithUsingRedirectPrefix(ModelMap model) {
        return new ModelAndView("redirect:/login", model);
    }

    // Login form
    @RequestMapping("/login")
    public String login() {
        return "/pages/login.html";
    }
}
