/**
 *
 */
package com.gate.web.servlets;

import com.gateweb.base.exception.FormValidationException;
import com.gateweb.base.exception.ReturnPathException;
import com.gateweb.einv.exception.EinvAjaxException;
import com.gateweb.einv.exception.EinvSysException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author mac
 *
 */
@ControllerAdvice
public class ExceptionHandlerController {

    protected Logger logger = LogManager.getLogger(getClass());

    @ExceptionHandler(value = {IOException.class})
    public String ioException(IOException ex, HttpServletRequest request, Model model) {
        System.out.println("ioException  ---");
        request.setAttribute("ex", ex);
        model.addAttribute("status", HttpStatus.SERVICE_UNAVAILABLE);
        return "/backendAdmin/exception.jsp";
    }

    @ExceptionHandler(value = {FormValidationException.class, ServletException.class, ReturnPathException.class})
    public String servletException(Exception ex, HttpServletRequest request, Model model) {
        System.out.println("servletException  ---");
        ex.printStackTrace();
        request.setAttribute("ex", ex);
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
        return "/backendAdmin/exception.jsp";
    }

    @ExceptionHandler(value = {SQLException.class, DataIntegrityViolationException.class, DataAccessException.class})
    public String dbException(Exception ex, HttpServletRequest request, Model model) {
        System.out.println("dbException ---");
        request.setAttribute("ex", ex);
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return "/backendAdmin/exception.jsp";
    }

    @ExceptionHandler(value = {NumberFormatException.class})
    public String numberFormatException(NumberFormatException ex, HttpServletRequest request, Model model) {
        logger.error("numberFormatException --- Request: " + request.getRequestURL() + " raised " + ex);
        request.setAttribute("ex", ex);
        ex.printStackTrace();
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return "/backendAdmin/exception.jsp";
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public String nullPointerException(NullPointerException ex, HttpServletRequest request, Model model) {
        logger.error("nullPointerException --- Request: " + request.getRequestURL() + " raised " + ex);
        ex.printStackTrace();
        request.setAttribute("ex", ex);
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

        //return "/backendAdmin/exception.jsp";  
        return "/error.jsp";
    }

    @ExceptionHandler(EinvSysException.class)
    //@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Application Exception occured")
    public String einvSysException(EinvSysException ex, HttpServletRequest request, Model model) {
        logger.error("einvSysException ---,{}", ex.getMessage());
        request.setAttribute("ex", ex);
        model.addAttribute("status", HttpStatus.EXPECTATION_FAILED);

        return "/backendAdmin/applicationException.jsp";
    }

    @ExceptionHandler(EinvAjaxException.class)
    public ResponseEntity<String> einvAjaxException(EinvAjaxException ex, HttpServletRequest request, Model model)
            throws Exception {
        ex.printStackTrace();
        request.setAttribute("errorMessage", ex.getMessage());
        model.addAttribute("status", HttpStatus.EXPECTATION_FAILED);

        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(Exception.class)
    public String defaultException(HttpServletRequest request, Exception ex) {
        logger.error("defaultException --- Request: " + request.getRequestURL() + " raised " + ex);
        request.setAttribute("ex", ex);
        return "/backendAdmin/exception.jsp";
    }
}