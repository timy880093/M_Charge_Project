package com.gateweb.charge.exception;

import com.gateweb.base.exception.ApplicationException;

public class FileStorageException extends ApplicationException {
    public FileStorageException(String msg, Exception ex) {
        super(msg);
    }

    public FileStorageException(String msg) {
        super(msg);
    }
}
