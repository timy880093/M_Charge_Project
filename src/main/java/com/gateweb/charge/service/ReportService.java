package com.gateweb.charge.service;

import com.gateweb.orm.charge.entity.Bill;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

public interface ReportService {

    void genScsbConvenientStoreFile(OutputStream outputStream, Collection<Bill> billList) throws IOException;

    void genInvoiceExcelFile(OutputStream outputStream, List<Bill> billList) throws FileNotFoundException;

    void genCurrentlyNotExpireContractReport(OutputStream outputStream);
}
