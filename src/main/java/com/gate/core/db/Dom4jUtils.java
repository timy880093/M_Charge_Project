package com.gate.core.db;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;



/**
 * Created by simon on 2014/6/25.
 */
public class Dom4jUtils {

    public static Logger logger = Logger.getLogger(Dom4jUtils.class);
    public static String RESOURCE_FILE = "resource.xml";
    public static String VALIDATION_FILE = "validation.xml";

    private static final String DEFAULT_HIBERNATE_CONF = "hibernate.cfg.xml";
    private static final String SUB_DAO_FOLDER = "dao";
    private static final String SUB_DAO_FOLDER_V2 = "pojo";
    protected static Document document = null;
    protected static Document document_resource = null;

    public Dom4jUtils() {

    }

    public String getValidationBean(String servletClassName)
            throws DocumentException, URISyntaxException, FileNotFoundException {
        if (document == null) {
            SAXReader reader = new SAXReader();
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(VALIDATION_FILE);
            if (inputStream != null) {
                document = reader.read(inputStream);
            }
        }
        Node node = document.selectSingleNode("//validation/class[@name='" + servletClassName + "']/@form");
        String formName = "";
        if (node != null) {
            formName = node.getText();
        }
        return formName;
    }


    public List<String> getDynamicHibernateFileList() throws URISyntaxException {
        List<String> returnList = new ArrayList();
        URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(DEFAULT_HIBERNATE_CONF);
        File hibernateFile;
        try {
            hibernateFile = new File(resourceUrl.toURI());
        }catch (Exception e){
            hibernateFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            hibernateFile = hibernateFile.getParentFile();
        }
        String parentPath = hibernateFile.getParent();
        logger.debug(parentPath);
        String[] dataList = new File(parentPath + "/" + SUB_DAO_FOLDER).list();
        if (dataList == null) {
        } else {
            Arrays.stream(dataList).forEach(daoFile->{
                if(StringUtils.endsWith(daoFile,".xml")){
                    returnList.add(String.format("%1$s/%2$s",SUB_DAO_FOLDER,daoFile));
                }
            });
//            for (int i = 0; i < dataList.length; i++) {//處理抓取單純xml檔案
//                if (StringUtils.endsWith(dataList[i], ".xml")) {
//                    returnList.add(SUB_DAO_FOLDER + "/" + dataList[i]);
//                }
//            }
        }
        dataList = new File(parentPath + "/" + SUB_DAO_FOLDER_V2).list();
        if (dataList == null) {
        } else {
            Arrays.stream(dataList).forEach(daoFile->{
                if(StringUtils.endsWith(daoFile,".xml")){
                    returnList.add(String.format("%1$s/%2$s",SUB_DAO_FOLDER_V2,daoFile));
                }
            });
        }
        return returnList;
    }

    public List<Map<String, Object>> getResourceBean(String resourceName)     //xpath格式
            throws DocumentException, URISyntaxException, FileNotFoundException {
        if (document_resource == null) {
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_FILE);
            if (inputStream != null) {
                document_resource = reader.read(inputStream);
            }
        }
        List list = document_resource.selectNodes(resourceName);
        List<Map<String, Object>> attrList = new ArrayList<Map<String, Object>>();     //存放attr
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element child = (Element) list.get(i);
                Map map = new HashMap();
                for (Iterator j = child.attributeIterator(); j.hasNext(); ) {
                    Attribute attr = (Attribute) j.next();
                    map.put(attr.getName(), attr.getValue());
                }
                attrList.add(map);
            }
        } else {
            return null;
        }
        return attrList;
    }



    /**
     * print all xml data
     */
    public static void printXMLTree(Element root) {
        printElement(root, 0);
        return;
    }

    private static void printElement(Element element, int level) {
        // 依照階層print
        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.print("<" + element.getQualifiedName() + ">");
        // 取得該TAG的Attr
        List attributes = element.attributes();
        for (int i = 0; i < attributes.size(); i++) {
            Attribute a = ((Attribute) attributes.get(i));
            System.out.print(" (Attr:\"" + a.getName() + "\"==" + a.getValue() + ")");
        }
        System.out.println(" " + element.getTextTrim());

        Iterator iter = element.elementIterator();
        while (iter.hasNext()) {
            Element sub = (Element) iter.next();
            printElement(sub, level + 1);
        }
        return;
    }

    public void treeWalk(Document document) {
        treeWalk(document.getRootElement());
    }

    public void treeWalk(Element element) {
        for (int i = 0, size = element.nodeCount(); i < size; i++) {
            Node node = element.node(i);
            if (node instanceof Element) {
                treeWalk((Element) node);
            } else {
                Element eElement = (Element) node;
//                System.out.println("Staff id : " + eElement.get("id"));
                System.out.println(node.asXML());
//               System.out.println(node.getText());
            }
        }
    }


    public static void main(String[] args) {
        try {
            Dom4jUtils dom4jUtils = new Dom4jUtils();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
