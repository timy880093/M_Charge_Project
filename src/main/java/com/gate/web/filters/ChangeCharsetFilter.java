package com.gate.web.filters;

import javax.servlet.*;
import java.io.IOException;

public class ChangeCharsetFilter implements Filter {
    protected String encoding = null;
    protected FilterConfig filterConfig = null;

    public void destroy() {
        this.encoding = null;
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
            String encoding = getEncoding();
            if (encoding != null){
                request.setCharacterEncoding(encoding);
                response.setCharacterEncoding(encoding);
            }
        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
    }

    protected String getEncoding() {
        return (this.encoding);
    }
}