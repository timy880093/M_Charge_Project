package com.gateweb.charge.service;

import com.gateweb.einv.model.InvoiceAmountSummaryReportEntity;

/**
 * Created by Eason on 2/13/2018.
 */
public interface InvoiceAmountSummaryReportFacade {

    void transactionInsertDataFromEinvDatabase();

    void transactionGenerateAndInsertSummaryReport(int from, int to);

    boolean isInvoiceRecordExists(InvoiceAmountSummaryReportEntity invoiceAmountSummaryReportEntity);
}
