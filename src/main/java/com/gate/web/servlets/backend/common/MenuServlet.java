package com.gate.web.servlets.backend.common;

import com.gate.web.beans.MenuBean;
import com.gate.web.servlets.BaseServlet;
import com.gate.web.servlets.InitServlet;

import javax.servlet.annotation.WebServlet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/8/13
 * Time: 下午 6:25
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(urlPatterns = "/backendAdmin/menuServlet")
public class MenuServlet extends BaseServlet {
    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {

        List<MenuBean> menuList = (List<MenuBean>) InitServlet.menuMap.get("role"+otherMap.get(ROLE_ID)+"menu");
        Map menu = new HashMap();
        menu.put("menu",menuList);
        otherMap.put(AJAX_JSON_OBJECT,menuList);
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
