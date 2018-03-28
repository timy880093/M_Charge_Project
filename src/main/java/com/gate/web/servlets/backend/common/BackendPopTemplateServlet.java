package com.gate.web.servlets.backend.common;


import com.gate.web.servlets.BaseServlet;


import java.util.Map;

/**
 * Created by simon on 2014/7/4.
 */

public abstract class BackendPopTemplateServlet extends BaseServlet {

    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        doSomething(requestParameterMap,requestAttMap,sessionMap,otherMap);
        String[] returnList= {FORWARD_TYPE_F,"/backendAdmin/template/pop_template.jsp"};
        return returnList;
    }

    public abstract void doSomething(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception;


}
