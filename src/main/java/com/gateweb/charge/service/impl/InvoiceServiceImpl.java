package com.gateweb.charge.service.impl;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.feeCalculation.dataCounter.IasrDataCounterByInvoiceDate;
import com.gateweb.charge.service.InvoiceService;
import com.gateweb.orm.charge.entity.ChargeIasrEntity;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.ChargeIasrRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.einv.repository.EinvInvoiceMainRepository;
import com.gateweb.utils.TimeUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * User: se01
 * Date: 7/8/2019 5:06 PM
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    TimeUtils timeUtils;
    @Autowired
    ChargeIasrRepository invoiceAmountSummaryReportRepository;
    @Autowired
    IasrDataCounterByInvoiceDate iasrDataCounterByInvoiceDate;
    @Autowired
    EinvInvoiceMainRepository einvInvoiceMainRepository;

    @Override
    public List<ChargeIasrEntity> getInvoiceAmountSummaryReportBySeller(String seller) {
        return invoiceAmountSummaryReportRepository.findBySeller(seller);
    }

    @Override
    /**
     * invoiceAmountSummaryReport的最早日期為2018/07/27再早是沒有資料的
     */
    public List<ChargeIasrEntity> getInvoiceAmountSummaryReportByBusinessNoAndCreateDate(String businessNo, long fromMillis, long toMillis) {
        java.sql.Date from = new java.sql.Date(fromMillis);
        java.sql.Date to = new java.sql.Date(toMillis);

        List<ChargeIasrEntity> invoiceAmountSummaryReportEntityList
                = invoiceAmountSummaryReportRepository.findBySellerIsAndCreateDateGreaterThanAndCreateDateLessThan(
                businessNo
                , from
                , to
        );
        return invoiceAmountSummaryReportEntityList;
    }

    /**
     * 根據公司資料計算範圍內月份的發票資料。
     * 使用InvoiceAmountSummaryReport進行計算。
     * 因為不包含，所以用夾擠，不然會有問題。
     *
     * @return
     */
    @Override
    public Integer calUsedCountByCreateDate(Integer companyId, String yearMonth) {
        Integer usedCount = 0;
        Optional<Company> companyEntityOptional = companyRepository.findByCompanyId(companyId);
        Optional<Calendar> calendarFromOpt = timeUtils.string2Calendar("yyyyMM", yearMonth);
        Optional<Calendar> calendarToOpt = timeUtils.string2Calendar("yyyyMM", yearMonth);
        if (companyEntityOptional.isPresent()) {
            Calendar calendarFrom = calendarFromOpt.get();
            Calendar calendarTo = calendarToOpt.get();
            calendarTo.add(Calendar.MONTH, 1);
            calendarFrom.add(Calendar.DATE, -1);
            List<ChargeIasrEntity> invoiceAmountSummaryReportEntityList
                    = getInvoiceAmountSummaryReportByBusinessNoAndCreateDate(companyEntityOptional.get().getBusinessNo(), calendarFrom.getTimeInMillis(), calendarTo.getTimeInMillis());

            for (ChargeIasrEntity invoiceAmountSummaryReportEntity : invoiceAmountSummaryReportEntityList) {
                usedCount += invoiceAmountSummaryReportEntity.getAmount();
            }
        }
        return usedCount;
    }

    @Deprecated
    @Override
    public Integer calUsedCountByInvoiceDate(Integer companyId, String yearMonth) {
        Integer usedCount = 0;
        Optional<Company> companyEntityOptional = companyRepository.findByCompanyId(companyId);
        Optional<Calendar> calendarYearMonthOpt = timeUtils.string2Calendar("yyyyMM", yearMonth);
        if (companyEntityOptional.isPresent()) {
            Calendar calendarYearMonth = calendarYearMonthOpt.get();
            DateTime fromDateTime = new DateTime(calendarYearMonth.getTime()).withDayOfMonth(1);
            DateTime toDateTime = new DateTime(calendarYearMonth.getTime()).plusMonths(1).withDayOfMonth(1).minusDays(1);
            CustomInterval customInterval = new CustomInterval(fromDateTime, toDateTime);
            Optional<Integer> countOptional = iasrDataCounterByInvoiceDate.count(companyEntityOptional.get().getBusinessNo(), customInterval);
            if (countOptional.isPresent()) {
                usedCount = countOptional.get();
            }
        }
        return usedCount;
    }

    /**
     * InvoiceAmountSummaryReport的數量計算是用Amount計的。
     *
     * @param invoiceAmountSummaryReportEntityList
     * @return
     */
    @Override
    public int getUsedCount(List<ChargeIasrEntity> invoiceAmountSummaryReportEntityList) {
        Integer usedCount = 0;
        for (ChargeIasrEntity invoiceAmountSummaryReportEntity : invoiceAmountSummaryReportEntityList) {
            usedCount += invoiceAmountSummaryReportEntity.getAmount();
        }
        return usedCount;
    }

}
