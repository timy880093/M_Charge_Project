/**
 * 
 */
package com.gate.web;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mac
 *
 */
public class XmlUploadStatusResponse extends BaseUploadStatusResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<String> xmlList;

	public XmlUploadStatusResponse() {
		this.message = new ArrayList<String>();
		this.xmlList = new ArrayList<String>();
		this.dataList = new ArrayList<String>();
	}

	public XmlUploadStatusResponse(Boolean success) {
		super();
		this.success = success;
		this.message = new ArrayList<String>();
		this.xmlList = new ArrayList<String>();
		this.dataList = new ArrayList<String>();
	}

	public XmlUploadStatusResponse(Boolean success, String message) {
		super();
		this.success = success;
		this.message = new ArrayList<String>();
		this.message.add(message);
		this.dataList = new ArrayList<String>();
	}

	public XmlUploadStatusResponse(Boolean success, List<String> message) {
		super();
		this.success = success;
		this.message = message;
		this.dataList = new ArrayList<String>();
	}

	/**
	 * @return the xmlList
	 */
	public List<String> getXmlList() {
		return xmlList;
	}

	/**
	 * @param xmlList the xmlList to set
	 */
	public void setXmlList(List<String> xmlList) {
		this.xmlList = xmlList;
	}

	
	/**
	 * 
	 * @param messageTxt
	 */
	public void addXml(String vo) {
		if(this.xmlList == null) {
			this.xmlList = new ArrayList<String>();
		}
		this.xmlList.add(vo);
	}
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String mess : message) {
			sb.append(mess + ", ");
		}
		sb.append(sb + ", dataList = ");
		for (String data : dataList) {
			sb.append(data + ", ");
		}
		sb.append(sb + ", xmlList = ");
		for (String inv : dataList) {
			sb.append(inv + ", ");
		}
		
		return "StatusResponse [success=" + success + ", data =" + sb.toString() + "]";
	}

}
