package com.gateweb.charge.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;

/**
 * Created by Eason on 3/12/2018.
 */
public interface SyncInvoiceDataFacade {
    void syncInvoiceMainDataFromEinvDatabase(Timestamp previousTime) throws InvocationTargetException, IllegalAccessException;
}
