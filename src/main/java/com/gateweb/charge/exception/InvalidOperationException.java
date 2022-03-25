package com.gateweb.charge.exception;

import com.gateweb.base.exception.ApplicationException;

public class InvalidOperationException extends ApplicationException {

    public InvalidOperationException() {
        
    }

    public InvalidOperationException(String msg) {
        super(msg);
    }
}
