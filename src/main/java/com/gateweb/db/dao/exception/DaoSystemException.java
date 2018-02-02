/*
 * $Header: /dao4jspring/src/pk/base/ApplicationException.java 1     06/08/31 11:42p pkliu $
 * All Rights Reserved
 *
 * Description:
 *
 */
package com.gateweb.db.dao.exception;

import com.gateweb.base.exception.SystemException;

/**
 * 
 * @author pkliu
 */
public class DaoSystemException extends SystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 *            錯誤訊息
	 */
	public DaoSystemException(String message) {
		super(message);
	}

	/**
	 * @param message
	 *            錯誤訊息
	 * @param cause
	 *            the cause (可使用 Throwable.getCause() method 取回)
	 */
	public DaoSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public DaoSystemException(Throwable cause) {
		super(cause);
	}
}
