package com.gate.web.servlets;

import com.gate.config.SystemConfigLoader;
import com.gate.core.db.HibernateCoreUtils;
import com.gate.core.utils.CustomBeanUtilsBean;
import com.gate.utils.MenuBuildUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/7/16
 * Time: 下午 5:00
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(name="InitServlet",urlPatterns={"/backendadmin/index.jsp"},loadOnStartup=1)
public class InitServlet extends HttpServlet {
    private static Log log = LogFactory.getLog(InitServlet.class);
    public static Map menuMap = null;

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        try {
            SystemConfigLoader.instance().load();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        CustomBeanUtilsBean.register();
        log.info("init BeanUtil!!!!!");


        String[] hibernateCfgXML =  {"hibernate.cfg.xml"};
        try {
            HibernateCoreUtils.getSessionFactory(null, hibernateCfgXML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("init hibernate!!!!!");



        try {
            menuMap = MenuBuildUtils.getInstance().initMenu();
        } catch (DocumentException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        log.info("init menuMap!!!!");

//        try {
//            System.loadLibrary("chilkat");
//        } catch (UnsatisfiedLinkError e) {
//            System.err.println("Native code library failed to load.\n" + e);
//        }



    }
}
