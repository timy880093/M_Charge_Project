package com.gateweb.charge.exception;

public class MissingRequiredPropertiesException extends RuntimeException {
    public MissingRequiredPropertiesException(String... args) {
        super(args.toString());
    }
}
