package com.gate.web.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

public class SessionCleanFilter implements Filter {
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        if("Y".equals(request.getParameter("sessionClean"))){
            Enumeration em = request.getSession().getAttributeNames();
            while(em.hasMoreElements()){
                String value =  em.nextElement().toString();
                boolean isInUpperCase=true;
                for(int i=0;i<value.length();i++){
                    char c=value.charAt(i);
                    if( Character.isLowerCase(c)){
                        isInUpperCase=false;
                        break;
                    }
                }
                if(!isInUpperCase){
                    request.getSession().removeAttribute(value);
                }
            }
        }

        chain.doFilter(request, response);
    }


}