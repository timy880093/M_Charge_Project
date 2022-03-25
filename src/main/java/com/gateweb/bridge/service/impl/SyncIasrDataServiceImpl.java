package com.gateweb.bridge.service.impl;

import com.gate.web.facades.CompanyService;
import com.gateweb.bridge.service.ChargeSourceService;
import com.gateweb.charge.report.bean.PartsOfIasrReport;
import com.gateweb.orm.charge.entity.ChargeIasrEntity;
import com.gateweb.orm.charge.repository.ChargeIasrRepository;
import com.gateweb.orm.einv.repository.EinvIasrRepository;
import com.gateweb.orm.einv.repository.EinvInvoiceMainRepository;
import com.gateweb.orm.einv.repository.InvoiceAmountSummaryReportRepository;
import com.gateweb.utils.ConcurrentUtils;
import com.gateweb.utils.LocalDateTimeUtils;
import com.google.gson.Gson;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class SyncIasrDataServiceImpl implements com.gateweb.bridge.service.SyncIasrDataService {
    ExecutorService executorService = Executors.newFixedThreadPool(9);
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
    public void regenIasrCount(String businessNo, String yearMonthStr) throws InterruptedException {
        Optional<YearMonth> yearMonthOptional = LocalDateTimeUtils.parseYearMonthFromString(yearMonthStr, "yyyyMM");
        if (yearMonthOptional.isPresent()) {
            boolean retryFlag = false;
            //細分是否需要重算
            Set<String> chargeIasrCountReportSet = chargeSourceService.getChargeSourceInvoiceCountDiffReport(yearMonthStr, businessNo, true)
                    .stream().map(chargeSourceInvoiceCountDiffReport -> chargeSourceInvoiceCountDiffReport.getSeller())
                    .collect(Collectors.toSet());
            if (!chargeIasrCountReportSet.isEmpty()) {
                retryFlag = !regenIasrCount(businessNo, yearMonthStr, yearMonthOptional.get());
            } else {
                logger.info("regenIasrCount,businessNo:{},yearMonth:{}", businessNo, yearMonthStr);
            }
            int retryTimes = 5;
            while (retryFlag && retryTimes > 0) {
                Thread.sleep(retryTimes * 100);
                logger.info("regenIasrCount, businessNo:{},yearMonth:{},retryTimes:{}", businessNo, yearMonthStr, retryTimes);
                retryFlag = !regenIasrCount(businessNo, yearMonthStr, yearMonthOptional.get());
                retryTimes--;
            }
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
    public void regenIasrCount(String yearMonthStr) {
        Optional<YearMonth> yearMonthOptional = LocalDateTimeUtils.parseYearMonthFromString(yearMonthStr, "yyyyMM");
        if (yearMonthOptional.isPresent()) {
            String twYm = LocalDateTimeUtils.getTwYearMonth(yearMonthOptional.get().atDay(1).atStartOfDay());
            String likeStrCondition = yearMonthStr + "%";
            Set<String> sellerList = einvInvoiceMainRepository.findByInvoiceDateLikeAndYearMonth(likeStrCondition, twYm);
            Set<String> billableBusinessNo = companyService.getBillableBusinessNo();
            List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();
            sellerList.stream().forEach(businessNo -> {
                if (billableBusinessNo.contains(businessNo)) {
                    completableFutureList.add(CompletableFuture.runAsync(() -> {
                        try {
                            regenIasrCount(businessNo, yearMonthStr);
                        } catch (InterruptedException e) {
                            logger.error(e.getMessage());
                        }
                    }, executorService));
                } else {
                    logger.info(" not in service businessNo:" + businessNo);
                }
            });
            ConcurrentUtils.completableGet(completableFutureList);
        }
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
        Collection<ChargeIasrEntity> chargeIasrEntityCollection = filteredResults.stream().map(result -> {
            ChargeIasrEntity iasrEntity = new ChargeIasrEntity();
            try {
                BeanUtils.copyProperties(iasrEntity, result);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage());
            } catch (InvocationTargetException e) {
                logger.error(e.getMessage());
            }
            return iasrEntity;
        }).collect(Collectors.toList());
        chargeIasrEntityCollection.stream().forEach(chargeIasrEntity -> {
            chargeIasrEntity.setMonth(monthString);
            chargeIasrEntity.setCreateDate(LocalDateTime.now());
            chargeIasrEntity.setCreatorId(11);
            chargeIasrEntity.setModifyDate(LocalDateTime.now());
            chargeIasrEntity.setModifierId(11);
            //新增
            chargeIasrRepository.save(chargeIasrEntity);
            logger.debug("Insert Iasr Record: " + gson.toJson(chargeIasrEntity));
        });
    }
}
