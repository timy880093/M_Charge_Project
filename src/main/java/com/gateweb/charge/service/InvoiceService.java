package com.gateweb.charge.service;

import com.gateweb.orm.charge.entity.ChargeIasrEntity;

import java.util.List;

/**
 * User: se01
 * Date: 7/8/2019 5:06 PM
 */
public interface InvoiceService {

    List<ChargeIasrEntity> getInvoiceAmountSummaryReportBySeller(String seller);

    List<ChargeIasrEntity> getInvoiceAmountSummaryReportByBusinessNoAndCreateDate(String businessNo, long fromMillis, long toMillis);


    Integer calUsedCountByCreateDate(Integer companyId, String yearMonth);

    Integer calUsedCountByInvoiceDate(Integer companyId, String yearMonth);

    int getUsedCount(List<ChargeIasrEntity> invoiceAmountSummaryReportEntityList);
}
