/**
 * 
 */
package com.gate.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mac
 *
 */
public class BaseUploadStatusResponse implements Serializable {

	public List<String> dataList;
	public Boolean success;
	public List<String> message;

	public BaseUploadStatusResponse() {
		this.message = new ArrayList<String>();
		this.dataList = new ArrayList<String>();
	}

	public BaseUploadStatusResponse(Boolean success) {
		super();
		this.success = success;
		this.message = new ArrayList<String>();
		this.dataList = new ArrayList<String>();
	}

	public BaseUploadStatusResponse(Boolean success, String message) {
		super();
		this.success = success;
		this.message = new ArrayList<String>();
		this.message.add(message);
		this.dataList = new ArrayList<String>();
	}

	public BaseUploadStatusResponse(Boolean success, List<String> message) {
		super();
		this.success = success;
		this.message = message;
		this.dataList = new ArrayList<String>();
	}

	/**
	 * @return the success
	 */
	public Boolean getSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	/**
	 * @return the message
	 */
	public List<String> getMessage() {
		return message;
	}

	/**
	 * @return the xmlList
	 */
	public List<String> getXmlList() {
		return dataList;
	}

	/**
	 * @param xmlList the xmlList to set
	 */
	public void setXmlList(List<String> xmlList) {
		this.dataList = xmlList;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(List<String> message) {
		this.message = message;
	}
	
	/**
	 * 
	 * @param messageTxt
	 */
	public void addMessage(String messageTxt) {
		if(this.message == null) {
			this.message = new ArrayList<String>();
		}
		this.message.add(messageTxt);
	}
	
	public void addData(String data) {
		if(this.dataList == null) {
			this.dataList = new ArrayList<String>();
		}
		this.dataList.add(data);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String mess : message) {
			sb.append(mess + ", ");
		}

		return "StatusResponse [success=" + success + ", message=" + sb.toString() + "]";
	}

}
