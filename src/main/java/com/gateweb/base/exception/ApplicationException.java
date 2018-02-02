package com.gateweb.base.exception;


public class ApplicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ApplicationException() {
	}

	/**
	 * @param message
	 *            錯誤訊息
	 */
	public ApplicationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ApplicationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 *            錯誤訊息
	 * @param cause
	 *            the cause (可使用 Throwable.getCause() method 取回)
	 */
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}
	
    /**
     * 
     * @return error string 
     */
	public String getStackTraceAsString() {
		return new ExceptionStackTraceString(this).toString();
	}
}
