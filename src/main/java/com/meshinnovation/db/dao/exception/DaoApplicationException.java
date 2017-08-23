/*
 * $Header: /dao4jspring/src/pk/base/ApplicationException.java 1     06/08/31 11:42p pkliu $
 * All Rights Reserved
 *
 * Description:
 *
 */
package com.meshinnovation.db.dao.exception;

import com.meshinnovation.base.exception.ApplicationException;

/**
 * 
 * @author pkliu
 */
public class DaoApplicationException extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 *            錯誤訊息
	 */
	public DaoApplicationException(String message) {
		super(message);
	}

	/**
	 * @param message
	 *            錯誤訊息
	 * @param cause
	 *            the cause (可使用 Throwable.getCause() method 取回)
	 */
	public DaoApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public DaoApplicationException(Throwable cause) {
		super(cause);
	}
}
