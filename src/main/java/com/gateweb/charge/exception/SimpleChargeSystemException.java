package com.gateweb.charge.exception;

import org.springframework.util.StringUtils;

public class SimpleChargeSystemException extends Exception {
    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (StringUtils.isEmpty(message)) {
            return this.getClass().getName();
        } else {
            return message;
        }
    }
}
