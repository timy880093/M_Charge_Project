package com.gateweb.charge.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;

/**
 * Created by Eason on 3/12/2018.
 */
public interface SyncInvoiceDataFacade {
    void syncInvoiceDataFromEinvDatabase(Timestamp previousTime) throws InvocationTargetException, IllegalAccessException;

    void syncInvoiceDataFromEinvDatabase(Timestamp from, Timestamp to) throws InvocationTargetException, IllegalAccessException;

    void syncYesterdaysInvoiceDataFromEinvDatabase() throws InvocationTargetException, IllegalAccessException;
}
