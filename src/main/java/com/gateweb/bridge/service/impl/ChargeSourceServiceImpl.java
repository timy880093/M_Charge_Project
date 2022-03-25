package com.gateweb.bridge.service.impl;

import com.gate.web.facades.CompanyService;
import com.gateweb.bridge.service.ChargeSourceService;
import com.gateweb.bridge.service.SyncIasrDataService;
import com.gateweb.charge.report.bean.ChargeSourceInvoiceCountDiffReport;
import com.gateweb.orm.charge.entity.ChargeIasrCountReport;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.ChargeIasrCountReportRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.einv.entity.report.EinvInvoiceDateCountReport;
import com.gateweb.orm.einv.repository.EinvInvoiceDateCountReportRepository;
import com.gateweb.utils.ConcurrentUtils;
import com.gateweb.utils.LocalDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ChargeSourceServiceImpl implements ChargeSourceService {
    ExecutorService pool = Executors.newFixedThreadPool(9);
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ChargeIasrCountReportRepository chargeIasrCountReportRepository;
    @Autowired
    EinvInvoiceDateCountReportRepository einvInvoiceDateCountReportRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    SyncIasrDataService syncIasrDataServiceImpl;
    @Autowired
    CompanyService companyService;

    @Bean
    ConcurrentHashMap<LocalDateTime, String> regenTaskMap() {
        return new ConcurrentHashMap<>();
    }

    @Override
    public List<ChargeSourceInvoiceCountDiffReport> getChargeSourceInvoiceCountDiffReport(String targetYearMonth, boolean diffOnly) {
        List<ChargeSourceInvoiceCountDiffReport> resultList = new ArrayList<>();
        Optional<YearMonth> targetYmOptional = LocalDateTimeUtils.parseYearMonthFromString(targetYearMonth, "yyyyMM");
        if (targetYmOptional.isPresent()) {
            LocalDateTime localDateTime = targetYmOptional.get().atDay(1).atStartOfDay();
            String twYm = LocalDateTimeUtils.getTwYearMonth(localDateTime);
            List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();
            Set<ChargeIasrCountReport> chargeIasrCountReportSet = new HashSet<>();
            Set<EinvInvoiceDateCountReport> einvInvoiceDateCountReportSet = new HashSet<>();
            completableFutureList.add(CompletableFuture.runAsync(() -> {
                chargeIasrCountReportSet.addAll(
                        chargeIasrCountReportRepository.findChargeIasrCountReport(targetYearMonth + '%')
                );
            }, pool));
            completableFutureList.add(CompletableFuture.runAsync(() -> {
                einvInvoiceDateCountReportSet.addAll(
                        einvInvoiceDateCountReportRepository.findEinvInvoiceDateCountReport(
                                twYm
                                , targetYearMonth + '%'
                        )
                );
            }, pool));
            ConcurrentUtils.completableGet(completableFutureList);
            resultList = chargeSourceInvoiceCountCompare(chargeIasrCountReportSet, einvInvoiceDateCountReportSet, diffOnly);
        }
        return resultList;
    }

    @Override
    public List<ChargeSourceInvoiceCountDiffReport> getChargeSourceInvoiceCountDiffReport(String targetYearMonth, String seller, boolean diffOnly) {
        List<ChargeSourceInvoiceCountDiffReport> resultList = new ArrayList<>();
        Optional<YearMonth> targetYmOptional = LocalDateTimeUtils.parseYearMonthFromString(targetYearMonth, "yyyyMM");
        if (targetYmOptional.isPresent()) {
            LocalDateTime localDateTime = targetYmOptional.get().atDay(1).atStartOfDay();
            String twYm = LocalDateTimeUtils.getTwYearMonth(localDateTime);
            List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();
            Set<ChargeIasrCountReport> chargeIasrCountReportSet = new HashSet<>();
            Set<EinvInvoiceDateCountReport> einvInvoiceDateCountReportSet = new HashSet<>();
            completableFutureList.add(CompletableFuture.runAsync(() -> {
                chargeIasrCountReportSet.addAll(chargeIasrCountReportRepository.findChargeIasrCountReportWithSeller(targetYearMonth + '%', seller));
            }, pool));
            completableFutureList.add(CompletableFuture.runAsync(() -> {
                einvInvoiceDateCountReportSet.addAll(einvInvoiceDateCountReportRepository.findEinvInvoiceDateCountReportWithSeller(
                        twYm
                        , targetYearMonth + '%'
                        , seller
                ));
            }, pool));
            ConcurrentUtils.completableGet(completableFutureList);
            resultList = chargeSourceInvoiceCountCompare(chargeIasrCountReportSet, einvInvoiceDateCountReportSet, diffOnly);
        }
        return resultList;
    }

    @Override
    public List<ChargeSourceInvoiceCountDiffReport> getChargeSourceInvoiceCountDiffReport(Map<String, Object> conditionMap) {
        List<ChargeSourceInvoiceCountDiffReport> resultList = new ArrayList<>();
        try {
            Optional<Company> companyOptional = Optional.empty();
            if (conditionMap.containsKey("companyId")) {
                Integer companyId = Double.valueOf(String.valueOf(conditionMap.get("companyId"))).intValue();
                companyOptional = companyRepository.findByCompanyId(companyId.intValue());
            }
            Optional<String> yearMonthOptional = Optional.empty();
            if (conditionMap.containsKey("yearMonth")) {
                yearMonthOptional = Optional.ofNullable(String.valueOf(conditionMap.get("yearMonth")));
            }
            boolean diffOnly = false;
            if (conditionMap.containsKey("diffOnly")) {
                diffOnly = Boolean.valueOf(String.valueOf(conditionMap.get("diffOnly")));
            }
            if (companyOptional.isPresent() && yearMonthOptional.isPresent()) {
                resultList = getChargeSourceInvoiceCountDiffReport(yearMonthOptional.get(), companyOptional.get().getBusinessNo(), diffOnly);
            } else if (companyOptional.isPresent()) {
                YearMonth currentYm = YearMonth.now();
                String ymString = LocalDateTimeUtils.yearMonthToString(currentYm, "yyyyMM");
                resultList = getChargeSourceInvoiceCountDiffReport(ymString, companyOptional.get().getBusinessNo(), diffOnly);
            } else if (yearMonthOptional.isPresent()) {
                resultList = getChargeSourceInvoiceCountDiffReport(yearMonthOptional.get(), diffOnly);
                Set<String> billableBusinessNo = companyService.getBillableBusinessNo();
                //過濾不在範圍內的項目，因為分多次查詢的效率太差
                resultList = resultList.stream().filter(chargeSourceInvoiceCountDiffReport -> {
                    return billableBusinessNo.contains(chargeSourceInvoiceCountDiffReport.getSeller());
                }).collect(Collectors.toList());
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return resultList;
    }

    public List<ChargeSourceInvoiceCountDiffReport> chargeSourceInvoiceCountCompare(
            Collection<ChargeIasrCountReport> chargeIasrCountReportSet
            , Collection<EinvInvoiceDateCountReport> einvInvoiceDateCountReportSet
            , Boolean diffOnly) {
        List<ChargeSourceInvoiceCountDiffReport> resultList = new ArrayList<>();
        //合併兩個查詢結果
        HashMap<String, ChargeSourceInvoiceCountDiffReport> hashMap = new HashMap<>();
        einvInvoiceDateCountReportSet.stream().forEach(einvInvoiceDateCountReport -> {
            String key = einvInvoiceDateCountReport.getSeller() + "_" + einvInvoiceDateCountReport.getInvoiceDate();
            if (!hashMap.containsKey(key)) {
                ChargeSourceInvoiceCountDiffReport chargeSourceInvoiceCountDiffReport = new ChargeSourceInvoiceCountDiffReport();
                chargeSourceInvoiceCountDiffReport.setInvoiceDate(einvInvoiceDateCountReport.getInvoiceDate());
                chargeSourceInvoiceCountDiffReport.setSeller(einvInvoiceDateCountReport.getSeller());
                chargeSourceInvoiceCountDiffReport.setOriginalCount(einvInvoiceDateCountReport.getCount());
                hashMap.put(key, chargeSourceInvoiceCountDiffReport);
            }
        });
        chargeIasrCountReportSet.stream().forEach(chargeIasrCountReport -> {
            String key = chargeIasrCountReport.getSeller() + "_" + chargeIasrCountReport.getInvoiceDate();
            if (!hashMap.containsKey(key)) {
                ChargeSourceInvoiceCountDiffReport chargeSourceInvoiceCountDiffReport = new ChargeSourceInvoiceCountDiffReport();
                chargeSourceInvoiceCountDiffReport.setInvoiceDate(chargeIasrCountReport.getInvoiceDate());
                chargeSourceInvoiceCountDiffReport.setSeller(chargeIasrCountReport.getSeller());
                chargeSourceInvoiceCountDiffReport.setMediumCount(chargeIasrCountReport.getCount());
                hashMap.put(key, chargeSourceInvoiceCountDiffReport);
            } else {
                ChargeSourceInvoiceCountDiffReport chargeSourceInvoiceCountDiffReport = hashMap.get(key);
                chargeSourceInvoiceCountDiffReport.setMediumCount(chargeIasrCountReport.getCount());
                if (diffOnly) {
                    Long diff = chargeSourceInvoiceCountDiffReport.getOriginalCount() - chargeSourceInvoiceCountDiffReport.getMediumCount();
                    if (diff.compareTo(0L) == 0) {
                        hashMap.remove(key);
                    }
                }
            }
        });
        resultList.addAll(hashMap.values());
        return resultList;
    }

    @Override
    public void recalculateByConditionMap(Map<String, Object> conditionMap) {
        try {
            Optional<Company> companyOptional = Optional.empty();
            if (conditionMap.containsKey("companyId")) {
                Integer companyId = Double.valueOf(String.valueOf(conditionMap.get("companyId"))).intValue();
                companyOptional = companyRepository.findByCompanyId(companyId.intValue());
            }
            Optional<String> yearMonthOptional = Optional.empty();
            if (conditionMap.containsKey("yearMonth")) {
                yearMonthOptional = Optional.ofNullable(String.valueOf(conditionMap.get("yearMonth")));
            }

            if (companyOptional.isPresent() && yearMonthOptional.isPresent()) {
                syncIasrDataServiceImpl.regenIasrCount(companyOptional.get().getBusinessNo(), yearMonthOptional.get());
            } else if (yearMonthOptional.isPresent()) {
                syncIasrDataServiceImpl.regenIasrCount(yearMonthOptional.get());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
