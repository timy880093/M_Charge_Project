package com.gateweb.charge.service;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Eason on 3/9/2018.
 */
public interface SyncUserDataFacade {

    void syncUserDataFromEinvDatabase() throws InvocationTargetException, IllegalAccessException;
}
