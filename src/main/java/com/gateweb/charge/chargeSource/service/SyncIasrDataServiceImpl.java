package com.gateweb.charge.chargeSource.service;

import com.gate.web.facades.CompanyService;
import com.gateweb.charge.report.bean.PartsOfIasrReport;
import com.gateweb.orm.charge.entity.ChargeIasrEntity;
import com.gateweb.orm.charge.repository.ChargeIasrRepository;
import com.gateweb.orm.einv.repository.EinvIasrRepository;
import com.gateweb.orm.einv.repository.EinvInvoiceMainRepository;
import com.gateweb.orm.einv.repository.InvoiceAmountSummaryReportRepository;
import com.gateweb.utils.ConcurrentUtils;
import com.gateweb.utils.LocalDateTimeUtils;
import com.google.gson.Gson;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.gateweb.utils.ConcurrentUtils.pool;

@Service
public class SyncIasrDataServiceImpl implements SyncIasrDataService {
    final Logger logger = LoggerFactory.getLogger(SyncIasrDataServiceImpl.class);
    Gson gson = new Gson();

    @Autowired
    EinvIasrRepository einvIasrRepository;
    @Autowired
    InvoiceAmountSummaryReportRepository invoiceAmountSummaryReportRepository;
    @Autowired
    EinvInvoiceMainRepository einvInvoiceMainRepository;
    @Autowired
    ChargeIasrRepository chargeIasrRepository;
    @Autowired
    ChargeSourceService chargeSourceService;
    @Autowired
    CompanyService companyService;

    @Override
    public void regenIasrCountAndCheckExists(String businessNo, YearMonth yearMonth) throws InterruptedException {
        String yearMonthStr = LocalDateTimeUtils.yearMonthToString(yearMonth, "yyyyMM");
        regenIasrCountAndCheckExists(businessNo, yearMonthStr, yearMonth);
    }

    @Override
    public void regenIasrCountAndCheckExists(String businessNo, String yearMonthStr) throws InterruptedException {
        Optional<YearMonth> yearMonthOptional = LocalDateTimeUtils.parseYearMonthFromString(yearMonthStr, "yyyyMM");
        if (yearMonthOptional.isPresent()) {
            regenIasrCountAndCheckExists(businessNo, yearMonthStr, yearMonthOptional.get());
        }
    }

    @Override
    public void regenIasrCountAndCheckExists(String businessNo, String yearMonthStr, YearMonth yearMonth) throws InterruptedException {
        boolean retryFlag = false;
        //細分是否需要重算
        Set<String> chargeIasrCountReportSet = chargeSourceService.getChargeSourceInvoiceCountDiffReport(yearMonthStr, businessNo, true)
                .stream().map(chargeSourceInvoiceCountDiffReport -> chargeSourceInvoiceCountDiffReport.getSeller())
                .collect(Collectors.toSet());
        if (!chargeIasrCountReportSet.isEmpty()) {
            retryFlag = !regenIasrCount(businessNo, yearMonthStr, yearMonth);
        } else {
            logger.info("regenIasrCount,businessNo:{},yearMonth:{}", businessNo, yearMonthStr);
        }
        int retryTimes = 5;
        while (retryFlag && retryTimes > 0) {
            Thread.sleep(retryTimes * 100);
            logger.info("regenIasrCount, businessNo:{},yearMonth:{},retryTimes:{}", businessNo, yearMonthStr, retryTimes);
            retryFlag = !regenIasrCount(businessNo, yearMonthStr, yearMonth);
            retryTimes--;
        }
    }

    @Override
    public boolean regenIasrCount(String businessNo, String yearMonthStr, YearMonth yearMonth) {
        boolean result = true;
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            LocalDateTime startLocalDateTime = yearMonth.atDay(1).atStartOfDay();
            String twYm = LocalDateTimeUtils.getTwYearMonth(startLocalDateTime);
            //刪除已經存在的部份
            chargeIasrRepository.deleteByInvoiceDateLikeAndSellerIs(yearMonthStr + "%", businessNo);
            //搜尋結果
            Collection<PartsOfIasrReport> results = einvIasrRepository.findNullSourceInvoiceAmountReport(
                    businessNo, twYm
            );
            //產生
            genIasrCount(results, yearMonthStr, yearMonthStr + "01");
            stopWatch.stop();
            logger.info("regenIasrCount," + yearMonthStr + "," + businessNo + "," + " elapsed seconds:" + stopWatch.getTime() / 1000);
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    @Override
    public void regenContractBasedIasrCount(String yearMonthStr) {
        Optional<YearMonth> yearMonthOptional = LocalDateTimeUtils.parseYearMonthFromString(yearMonthStr, "yyyyMM");
        if (yearMonthOptional.isPresent()) {
            String twYm = LocalDateTimeUtils.getTwYearMonth(yearMonthOptional.get().atDay(1).atStartOfDay());
            String likeStrCondition = yearMonthStr + "%";
            Set<String> sellerSet = Collections.synchronizedSet(
                    einvInvoiceMainRepository.findByInvoiceDateLikeAndYearMonth(likeStrCondition, twYm)
            );
            Set<String> billableBusinessNoSet = Collections.synchronizedSet(
                    companyService.getBillableBusinessNo().stream().filter(billableBusinessNo -> {
                        return sellerSet.contains(billableBusinessNo);
                    }).collect(Collectors.toSet())
            );
            regenIasrCount(yearMonthStr, billableBusinessNoSet);
        }
    }

    @Override
    public void regenIasrCountByInvoiceDateAndYm(String yearMonthStr) {
        Optional<YearMonth> yearMonthOptional = LocalDateTimeUtils.parseYearMonthFromString(yearMonthStr, "yyyyMM");
        if (yearMonthOptional.isPresent()) {
            String twYm = LocalDateTimeUtils.getTwYearMonth(yearMonthOptional.get().atDay(1).atStartOfDay());
            String likeStrCondition = yearMonthStr + "%";
            Set<String> sellerSet = Collections.synchronizedSet(
                    einvInvoiceMainRepository.findByInvoiceDateLikeAndYearMonth(likeStrCondition, twYm)
            );
            regenIasrCount(yearMonthStr, sellerSet);
        }
    }

    private void regenIasrCount(String yearMonthStr, Set<String> businessNoSet) {
        List<CompletableFuture<Void>> completableFutureList = Collections.synchronizedList(new ArrayList<>());
        completableFutureList.add(CompletableFuture.runAsync(() -> {
            businessNoSet.parallelStream().forEach(businessNo -> {
                try {
                    regenIasrCountAndCheckExists(businessNo, yearMonthStr);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            });
        }, pool));
        ConcurrentUtils.completableGet(completableFutureList);
    }

    public void genIasrCount(
            Collection<PartsOfIasrReport> partsOfIasrReportCollection
            , String likeStr
            , String monthString) {
        Collection<PartsOfIasrReport> filteredResults = partsOfIasrReportCollection.stream().filter(partsOfIasrReport -> {
            if (partsOfIasrReport.getInvoiceDate().contains(likeStr)) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        filteredResults.stream().forEach(result -> {
            try {
                ChargeIasrEntity iasrEntity = new ChargeIasrEntity();
                iasrEntity.setSeller(result.getSeller());
                iasrEntity.setAmount(result.getAmount());
                iasrEntity.setBuyer(result.getBuyer());
                iasrEntity.setTotal(result.getTotal());
                iasrEntity.setInvoiceDate(result.getInvoiceDate());
                iasrEntity.setMonth(monthString);
                iasrEntity.setCreateDate(LocalDateTime.now());
                iasrEntity.setCreatorId(11);
                iasrEntity.setModifyDate(LocalDateTime.now());
                iasrEntity.setModifierId(11);
                iasrEntity.setInvoiceStatus(result.getStatus());
                chargeIasrRepository.save(iasrEntity);
                logger.debug("Insert Iasr Record: " + gson.toJson(iasrEntity));
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        });
    }
}
