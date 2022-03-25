package com.gateweb.charge.exception;

import com.gateweb.base.exception.ApplicationException;

public class IllegalStateException extends ApplicationException {
    public IllegalStateException(String msg) {
        super(msg);
    }
}
