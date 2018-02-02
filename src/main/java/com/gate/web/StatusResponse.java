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
public class StatusResponse implements Serializable {

	private Boolean success;
	private List<String> message;

	public StatusResponse() {
		this.message = new ArrayList<String>();
	}

	public StatusResponse(Boolean success) {
		super();
		this.success = success;
		this.message = new ArrayList<String>();
	}

	public StatusResponse(Boolean success, String message) {
		super();
		this.success = success;
		this.message = new ArrayList<String>();
		this.message.add(message);
	}

	public StatusResponse(Boolean success, List<String> message) {
		super();
		this.success = success;
		this.message = message;
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
	 * @param message
	 *            the message to set
	 */
	public void setMessage(List<String> message) {
		this.message = message;
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
