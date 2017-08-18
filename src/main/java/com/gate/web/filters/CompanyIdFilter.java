package com.gate.web.filters;

import com.gate.realms.LoginUser;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;


public class CompanyIdFilter  implements Filter {
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

            if(StringUtils.isNotEmpty(request.getParameter("method")) && StringUtils.isNotEmpty(request.getParameter("companyId"))){

                Principal principal = request.getUserPrincipal();
                LoginUser loginUser = new Gson().fromJson(principal.getName(),LoginUser.class);
                //Map<String,Object> dataMap = (Map<String, Object>)new Gson().fromJson(principal.getName(),new TypeToken<Map<String, Object>>(){}.getType());
                //String userName = dataMap.get("name").toString(); //抓Principal 的 name 切割後取user name
                //String principalName = principal.getName();
                //String[] dataList = org.apache.commons.lang3.StringUtils.split(principalName, "_");
                String roleId = String.valueOf(loginUser.getRoleId());
                String referenceIds="";
                if(StringUtils.isNotEmpty(loginUser.getReferenceCompanyId())){
                    referenceIds = loginUser.getReferenceCompanyId();
                }
                if(!roleId.equals("100")){
                    String method = request.getParameter("method");
                    String companyId = request.getParameter("companyId");
                    if(method.equals("create") || method.equals("import")){

                        String refCompanyId = referenceIds;
                        String[] refCompanyIds = refCompanyId.split(",");
                        boolean exist = false;
                        for(String refCompany:refCompanyIds){
                            if(refCompany.equals(companyId)){
                                exist = true;
                            }
                        }
                        if(exist==false){
                            request.getRequestDispatcher("/error.jsp").forward(request, response);
                        }
                    }
                }


        }


        chain.doFilter(request, response);
    }
}
