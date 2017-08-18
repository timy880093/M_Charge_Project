package com.gate.utils;

import com.gate.core.db.Dom4jUtils;
import com.gate.web.beans.MenuBean;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by simon on 2015/12/11.
 */
public class MenuBuildUtils {

    public static Dom4jUtils dom4jUtils = new Dom4jUtils();
    public static MenuBuildUtils menuBuildUtils = null;
    public static String MENU_FILE = "menu.xml";
    private MenuBuildUtils(){

    }

    public static MenuBuildUtils getInstance(){
        if(menuBuildUtils == null){
            menuBuildUtils = new MenuBuildUtils();
        }
        return menuBuildUtils;
    }

    private MenuBean getMenuBean(Element subsubElement) throws InvocationTargetException, IllegalAccessException {
        Map subMap = new HashMap();
        MenuBean subMenuBean = new MenuBean();
        for (Iterator j = subsubElement.attributeIterator(); j.hasNext(); ) {
            Attribute attr = (Attribute) j.next();
            subMap.put(attr.getName(), attr.getValue());
        }
        MapBeanConverterUtils.mapToBean(subMap, subMenuBean);
        return subMenuBean;
    }

    public Map initMenu() throws DocumentException, InvocationTargetException, IllegalAccessException {
        SAXReader reader = new SAXReader();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(MENU_FILE);
        Document document = null;
        if (inputStream != null) {
            document = reader.read(inputStream);
        }
        Map menuMap = new HashMap();
        Element rootElement = document.getRootElement();
        if (rootElement.elements("menu-define") != null) {
            List<Element> elementList = rootElement.elements("role");
            for (Element element : elementList) {
                List<MenuBean> menuList = new ArrayList();
                List<Element> subElementList = element.elements("menu");
                for (Element subElement : subElementList) {
                    MenuBean menuBean = getMenuBean(subElement);
                    List<MenuBean> subMenuList = new ArrayList();
                    List<Element> subsubElementList = subElement.elements("menu");
                    for (Element subsubElement : subsubElementList) {
                        MenuBean subMenuBean = getMenuBean(subsubElement);
                        subMenuList.add(subMenuBean);
                    }
                    menuBean.setChildren(subMenuList);
                    menuList.add(menuBean);
                }

                for (Iterator j = element.attributeIterator(); j.hasNext(); ) {
                    Attribute attr = (Attribute) j.next();
                    menuMap.put("role"+attr.getValue()+"menu",menuList);
                    break;
                }
            }
        }

        return menuMap;
    }

}
