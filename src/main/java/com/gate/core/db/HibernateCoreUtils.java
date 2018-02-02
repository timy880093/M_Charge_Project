package com.gate.core.db;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 2014/6/25.
 */
public class HibernateCoreUtils {
    private static Configuration configuration = null;
    private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();

    private static SessionFactory sessionFactory = null;
    protected static Logger logger = Logger.getLogger(HibernateCoreUtils.class);
    protected static final String DEFAULT_HIBERNATE_FILE = "hibernate.cfg.xml";
    private static List<String> dynamicHibernateList = new ArrayList<String>();

    private HibernateCoreUtils() {

    }

    private static void registerDynamicHbmXML() throws URISyntaxException {
        Dom4jUtils dom4jUtils = new Dom4jUtils();
        dynamicHibernateList = dom4jUtils.getDynamicHibernateFileList();
    }



    public static synchronized Configuration getConfiguration(String rootPath, String... hibernateCfgXML) throws IOException {

        if (rootPath != null && (!rootPath.endsWith("\\") || !rootPath.endsWith("/"))) {
            rootPath = rootPath + "/";
        }
        if (configuration == null) {
            if (hibernateCfgXML.length != 0) {
                if (rootPath != null) {//根據特定路徑loading
                    File setupFile = org.apache.commons.io.FileUtils.getFile(rootPath + hibernateCfgXML[0]);
                    logger.warn("Loading Default Configuration = " + setupFile.getAbsoluteFile());
                    configuration = new Configuration().configure(setupFile);
                } else {//抓resource底下的檔案
                    configuration = new Configuration().configure(hibernateCfgXML[0]);
                }
            }
        }
        return configuration;
    }

    public static synchronized SessionFactory getSessionFactory(String rootPath, String... hibernateCfgXML) throws Exception {
        if (sessionFactory == null) {
            configuration = getConfiguration(rootPath, hibernateCfgXML);
            ServiceRegistry serviceRegistry =
                    new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
                            .build();
            //如果有其他的自訂的hibernate.hbm.xml，這邊會寫入，減少共用的Loading
            registerDynamicHbmXML();
            if (dynamicHibernateList != null && dynamicHibernateList.size() != 0) {
                for (int i = 0; i < dynamicHibernateList.size(); i++) {
                    configuration.addResource((String) dynamicHibernateList.get(i));
                }
            }
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }

    
   /* public static Session getCurrentSession() throws Exception {
    		if(sessionFactory ==null) {
    			sessionFactory = getSessionFactory(null);
    		}
    		return sessionFactory.getCurrentSession();
    }*/
    /**
     * 開啟Session
     *
     * @return
     */
    public static Session getSession(String rootPath, String... hibernateCfgXML) throws Exception {
        Session session = (Session) threadLocal.get();
        if (session == null || !session.isOpen()) {  //如果session為空，重新開啟一個session，並放入threadLocal
        		//System.out.println(" session is null or session is not open");
            if (hibernateCfgXML.length == 0) {
                String[] defaultConfig = {DEFAULT_HIBERNATE_FILE};
                hibernateCfgXML = defaultConfig;
            }
            sessionFactory = getSessionFactory(rootPath, hibernateCfgXML);
            session = sessionFactory.openSession();
            threadLocal.set(session);
        } else {
        		//System.out.println(" session is not null or session is open");
        }
        return session;
    }

    /**
     * 關閉Session
     */
    public static void closeSession() {
        Session session = (Session) threadLocal.get();
        threadLocal.set(null);
        if (session != null) {
            session.flush(); // 強制flush session
            session.clear();
            session.close();
        }
    }

    public static void initFactory() throws URISyntaxException {
        registerDynamicHbmXML();
        if (dynamicHibernateList != null && dynamicHibernateList.size() != 0) {
            for (int i = 0; i < dynamicHibernateList.size(); i++) {
                configuration.addResource((String) dynamicHibernateList.get(i));
            }
        }

    }
}