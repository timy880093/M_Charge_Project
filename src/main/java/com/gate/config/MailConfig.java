package com.gate.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author jimichen
 *
 */
public class MailConfig {

	private String host;

	private String port;

	private String userName;

	private String password;
	
	private String from;

	private String encoding;

	private String[] receivers;

	private String mailTitle;
	
	private String imgUrl;
	
	private String testMailTo;
	
	private String errMailTo;
	
	private String sendMail;

    private String sendTaskMail;

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String[] getReceivers() {
		return receivers;
	}

	public void setReceivers(String[] re) {
		this.receivers = re;
	}

	public String toString() {
		Map temp = new HashMap();

		temp.put("server", host);
		temp.put("port", port);
		temp.put("userName", userName);
		temp.put("password", password);

		temp.put("encoding", encoding);
		return temp.toString();
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String server) {
		this.host = server;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	//2012.07 tennia
	public String getTestMailTo() {
		return testMailTo;
	}

	public void setTestMailTo(String testMailTo) {
		this.testMailTo = testMailTo;
	}
	public String getErrMailTo() {
		return errMailTo;
	}

	public void setErrMailTo(String errMailTo) {
		this.errMailTo = errMailTo;
	}

	public String getSendMail() {
		return sendMail;
	}
	public Boolean getSendMailBoolean() {
		if(sendMail.equals("true"))
		{
			return true;
		}else{
			return false;
		}
	}
	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}
	
	public String getSendTaskMail() {
		return sendTaskMail;
	}
	public Boolean getSendTaskMailBoolean() {
		if(sendTaskMail.equals("true"))
		{
			return true;
		}else{
			return false;
		}
	}

}
