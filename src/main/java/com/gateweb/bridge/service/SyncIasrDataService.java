package com.gateweb.bridge.service;

import java.time.YearMonth;
import java.util.concurrent.ExecutionException;

public interface SyncIasrDataService {

    void regenIasrCount(String businessNo, String yearMonthStr) throws InterruptedException;

    boolean regenIasrCount(String businessNo, String yearMonthStr, YearMonth yearMonth);

    void regenIasrCount(String yearMonth) throws ExecutionException, InterruptedException;
}
