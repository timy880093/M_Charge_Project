package com.gate.config;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class SystemConfig {

	private static SystemConfig instance;

	public static SystemConfig getInstance() {
		if (instance == null) {
			instance = new SystemConfig();
		}
		return instance;
	}

	private Map parameters = new HashMap();

    private Map objects = new HashMap();

    private DataSource dataSource;

    private MailConfig mailConfig;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


    public MailConfig getMailConfig() {
        return mailConfig;
    }

    public void setMailConfig(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

	public String getParameter(String name) {
		return (String) parameters.get(name);
	}

	public void setParameter(String name, String value) {
		parameters.put(name, value);
	}

	public Map getParameters() {
		return parameters;
	}

	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}
	
	public Object get(String name){
		return objects.get(name);
	}
	public void set(String name,Object value){
		objects.put(name,value);
	}
}
