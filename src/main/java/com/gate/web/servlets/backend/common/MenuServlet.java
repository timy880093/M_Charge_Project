package com.gate.web.servlets.backend.common;

import com.gate.web.beans.MenuBean;
import com.gate.web.facades.UserService;
import com.gateweb.charge.frontEndIntegration.datatablePagination.MenuMapProvider;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.security.ChargeUserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/8/13
 * Time: 下午 6:25
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/backendAdmin/menuServlet")
@Controller
public class MenuServlet {
    @Autowired
    UserService userService;
    @Autowired
    MenuMapProvider menuMapProvider;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<MenuBean> get(Authentication authentication, Map otherMap) throws Exception {
        ChargeUserPrinciple chargeUserPrinciple = (ChargeUserPrinciple) authentication.getPrincipal();
        Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
        List<MenuBean> menuList = (List<MenuBean>) menuMapProvider.menuMap.get(
                "role" + callerInfoOptional.get().getUserEntity().getRoleId() + "menu"
        );
        Map menu = new HashMap();
        menu.put("menu", menuList);
        otherMap.put("AJAX_JSON_OBJECT", menuList);
        return menuList; //To change body of implemented methods use File | Settings | File Templates.
    }
}
