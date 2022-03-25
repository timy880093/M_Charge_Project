package com.gateweb.bridge.service;

import com.gateweb.charge.report.bean.ChargeSourceInvoiceCountDiffReport;

import java.util.List;
import java.util.Map;

public interface ChargeSourceService {
    List<ChargeSourceInvoiceCountDiffReport> getChargeSourceInvoiceCountDiffReport(String targetYearMonth,boolean diffOnly);

    List<ChargeSourceInvoiceCountDiffReport> getChargeSourceInvoiceCountDiffReport(String targetYearMonth, String seller,boolean diffOnly);

    List<ChargeSourceInvoiceCountDiffReport> getChargeSourceInvoiceCountDiffReport(Map<String, Object> conditionMap);

    void recalculateByConditionMap(Map<String, Object> conditionMap);
}
