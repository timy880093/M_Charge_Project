package com.meshinnovation.base.exception;


public class SystemException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public SystemException() {
	}

	/**
	 * @param message
	 *            錯誤訊息
	 */
	public SystemException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public SystemException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 *            錯誤訊息
	 * @param cause
	 *            the cause (可使用 Throwable.getCause() method 取回)
	 */
	public SystemException(String message, Throwable cause) {
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
