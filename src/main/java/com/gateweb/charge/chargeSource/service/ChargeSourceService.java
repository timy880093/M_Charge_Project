package com.gateweb.charge.chargeSource.service;

import com.gateweb.charge.chargeSource.iasr.bean.MaxIasrInvoiceDatePeriod;
import com.gateweb.charge.report.bean.ChargeSourceInvoiceCountDiffReport;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ChargeSourceService {
    List<ChargeSourceInvoiceCountDiffReport> getChargeSourceInvoiceCountDiffReport(String targetYearMonth,boolean diffOnly);

    List<ChargeSourceInvoiceCountDiffReport> getChargeSourceInvoiceCountDiffReport(String targetYearMonth, String seller,boolean diffOnly);

    List<ChargeSourceInvoiceCountDiffReport> getChargeSourceInvoiceCountDiffReport(Map<String, Object> conditionMap);

    void reSyncContractBasedIasrCountByConditionMap(Map<String, Object> conditionMap);

    void reSyncIasrDataBySeller(String businessNo);

    Optional<MaxIasrInvoiceDatePeriod> findMaxInvoiceDatePeriodBySeller(String seller);
}
