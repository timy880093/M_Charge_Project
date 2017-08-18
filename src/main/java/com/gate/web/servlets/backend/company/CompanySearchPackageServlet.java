package com.gate.web.servlets.backend.company;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/backendAdmin/companySearchPackageServlet")
public class CompanySearchPackageServlet extends CompanySearchServlet {

    public String getDispatch_page() {
        return "/backendAdmin/companyCharge/company_list.jsp";
    }


}
