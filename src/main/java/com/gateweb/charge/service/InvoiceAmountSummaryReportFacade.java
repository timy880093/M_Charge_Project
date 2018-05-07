package com.gateweb.charge.service;

/**
 * Created by Eason on 2/13/2018.
 */
public interface InvoiceAmountSummaryReportFacade {

    void transactionInsertDataFromEinvDatabase();

    void transactionGenerateAndInsertSummaryReport(int from, int to);

    void reportDataGenerateAndInsertByDate(int date);
}
