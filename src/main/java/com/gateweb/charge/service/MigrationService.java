package com.gateweb.charge.service;

import com.gateweb.charge.exception.IllegalStateException;

import java.text.ParseException;

public interface MigrationService {

    void initialNonSeasonPaidContract();

    void initialSeasonPaidContract();

    void outRentalBillingItemByCompany(String ym);

    void billInTest(String ym);

    void enabledTest(String ym);

    void markPaidAlreadyBillingItems();

    void migrateOverageBillCycleToBillingItemTest();

    void backup11();

    void migrationProcessTest() throws ParseException, IllegalStateException;
}
