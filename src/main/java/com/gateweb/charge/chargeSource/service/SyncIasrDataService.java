package com.gateweb.charge.chargeSource.service;

import java.time.YearMonth;

public interface SyncIasrDataService {

    void regenIasrCountAndCheckExists(String businessNo, YearMonth yearMonth) throws InterruptedException;

    void regenIasrCountAndCheckExists(String businessNo, String yearMonthStr) throws InterruptedException;

    void regenIasrCountAndCheckExists(String businessNo, String yearMonthStr, YearMonth yearMonth) throws InterruptedException;

    boolean regenIasrCount(String businessNo, String yearMonthStr, YearMonth yearMonth);

    void regenContractBasedIasrCount(String yearMonthStr);

    void regenIasrCountByInvoiceDateAndYm(String yearMonthStr);
}
