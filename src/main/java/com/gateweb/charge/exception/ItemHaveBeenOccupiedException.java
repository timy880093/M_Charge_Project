package com.gateweb.charge.exception;

import com.gateweb.base.exception.ApplicationException;

public class ItemHaveBeenOccupiedException extends ApplicationException {
    public ItemHaveBeenOccupiedException(String... args) {
        super(args.toString());
    }
}
