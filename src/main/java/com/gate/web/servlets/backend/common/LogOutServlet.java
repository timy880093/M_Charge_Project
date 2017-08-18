package com.gate.web.servlets.backend.common;


import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

@WebServlet("/backendAdmin/logOutServlet")
public class LogOutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected Logger logger = Logger.getLogger(getClass());

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html");
//        PrintWriter out= response.getWriter();
        HttpSession session= request.getSession();

        //2016.10.14 記錄LogOut時的資訊，for debug timeout 時間未到卻被登出
        long createTime = session.getCreationTime();
        logger.info("logout session createTime = " + new java.util.Date(createTime));
        long currentTime = Calendar.getInstance().getTimeInMillis();
        logger.info("logout session currentTime = " + new java.util.Date(currentTime));
        long totalTime = currentTime-createTime;
        logger.info("logout session totalTime = " + totalTime);
        logger.info("logout session id = " + session.getId());
        logger.info("logout session LastAccessedTime = " + new java.util.Date(session.getLastAccessedTime()));
        logger.info("logout session MaxInactiveInterval = " + session.getMaxInactiveInterval());
        logger.info("logout session isNew = " + session.isNew());


        session.invalidate();
//        out.println("<font color=red><b>You have been successfully logged out</b></font>");
//        out.println("<font color=green><b>Login again</b></font>");
        response.sendRedirect("/backendAdmin/loginServlet");
//        ServletContext context= getServletContext();
//
//        RequestDispatcher rd= context.getRequestDispatcher("/backendAdmin/template/template.jsp");
//        rd.forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doGet(request, response);
    }

}
