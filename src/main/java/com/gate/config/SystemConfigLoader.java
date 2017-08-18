package com.gate.config;

import org.apache.commons.logging.LogFactory;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SystemConfigLoader {
	private static final org.apache.commons.logging.Log log = LogFactory.getLog(SystemConfigLoader.class);

	private String fileName = "SystemConfig.xml";

	private Document doc;

	private static SystemConfigLoader instance;

	public static SystemConfigLoader instance() {
		if (instance == null) {
			instance = new SystemConfigLoader();
		}
		return instance;
	}

	private SystemConfigLoader() {
	}

	public void load() throws Exception {
		parseWithSAX();
		SystemConfig sysConfig = SystemConfig.getInstance();

		PropertyDescriptor[] pros = propertyDescriptors(SystemConfig.class);
		Map prosMap = new HashMap(pros.length);
		for (int i = 0; i < pros.length; i++) {
			prosMap.put(pros[i].getName(), pros[i]);
		}

		Element root = doc.getRootElement();

		Class type = null;

		for (Iterator iter = root.getChildren().iterator(); iter.hasNext();) {
			Element node = (Element) iter.next();
			String nodeName = node.getName();
			
			// Node attr = node.selectSingleNode("@class");
			Attribute attribute = node.getAttribute("class");
			String classAttr = attribute == null ? null : attribute.getValue();
			
			boolean isParameter = false;
			if (classAttr == null) {
				if (prosMap.keySet().contains(nodeName)) {
					type = ((PropertyDescriptor) prosMap.get(nodeName))
							.getPropertyType();
				} else if ("parameter".equals(nodeName)) {
					isParameter = true;
				} else {
					log.warn("not process config \n");
					continue;
				}
			} else {
				type = Class.forName(classAttr);
			}

			if (isParameter) {
				String[] par = getParameter(node);
				
				if (par == null) {
					log.warn("not process config\n");
				} else {
					sysConfig.setParameter(par[0], par[1]);
				}

			} else {
				Object o = createConfigObject(node, type);
				sysConfig.set(nodeName, o);

				PropertyDescriptor p = (PropertyDescriptor) prosMap
						.get(nodeName);
				if (p != null) {
					Method setter = p.getWriteMethod();
					setter.invoke(sysConfig, new Object[] { o });
				}

			}

		}

		doc = null;
	}

	public String[] getParameter(Element node) {
		String nameNode = node.getChild("name").getValue();
		String valueNode =node.getChild("value").getValue();
		if (nameNode == null || valueNode == null) {
			return null;
		}
		return new String[] { nameNode, valueNode };
	}

	public Object createConfigObject(Element node, Class type) throws Exception {

		Object result = null;

		result = type.newInstance();
		PropertyDescriptor[] pros = propertyDescriptors(type);
		Map prosMap = new HashMap(pros.length);
		for (int i = 0; i < pros.length; i++) {
			prosMap.put(pros[i].getName(), pros[i]);
		}

		for (Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			Element n = (Element) iter.next();
			String name = n.getName();
			String value = n.getValue();

			if (prosMap.containsKey(name)) {
				PropertyDescriptor p = (PropertyDescriptor) prosMap.get(name);

				Class pType = p.getPropertyType();
				Method setter = p.getWriteMethod();

				if (pType.isArray()) {
					String[] temp;
					if (value != null) {
						temp = value.split(",");
					} else {
						temp = new String[0];
					}
					setter.invoke(result, new Object[] { temp });
				} else {

					setter.invoke(result, new Object[] { value });
				}

			} else {
				log.warn("not process attr " + name + " in " + node.getName());
			}

		}

		return result;
	}

	/**
	 * 
	 * <p>
	 * <code>parseWithSAX</code>
	 * </p>
	 * 
	 * @return
	 * @throws DocumentException
	 * @author victorzhang 2006-6-20
	 * @since 1.0
	 */
	private void parseWithSAX() {
		SAXBuilder builder = new SAXBuilder();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(
				fileName);
		try {
			doc = builder.build(in);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns a PropertyDescriptor[] for the given Class.
	 * 
	 * @param c
	 *            The Class to retrieve PropertyDescriptors for.
	 * @return A PropertyDescriptor[] describing the Class.
	 * @throws java.sql.SQLException
	 *             if introspection failed.
	 * @throws IntrospectionException
	 */
	private PropertyDescriptor[] propertyDescriptors(Class c)
			throws IntrospectionException {
		// Introspector caches BeanInfo classes for better performance
		BeanInfo beanInfo = null;

		beanInfo = Introspector.getBeanInfo(c);

		PropertyDescriptor[] temp = beanInfo.getPropertyDescriptors();

		int length = temp.length;
		PropertyDescriptor[] result = new PropertyDescriptor[length - 1];
		boolean isFound = false;
		for (int i = 0, j = 0; i < length; i++) {
			if (!isFound && temp[i].getName().equals("class")) {
				isFound = true;
				continue;
			} else {
				result[j++] = temp[i];
			}
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		instance().load();

	}

}
