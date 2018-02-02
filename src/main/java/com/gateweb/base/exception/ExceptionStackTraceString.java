package com.gateweb.base.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Linus
 *
 */
public class ExceptionStackTraceString {

	private Throwable e;
	
	/**
	 * @param e
	 */
	public ExceptionStackTraceString(Throwable e) {
		this.e = e;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(e == null) return "";
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		e.printStackTrace(pw);
		return sw.toString();
	}
}
