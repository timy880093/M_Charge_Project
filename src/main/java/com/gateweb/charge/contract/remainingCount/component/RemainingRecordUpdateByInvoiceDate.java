package com.gateweb.charge.contract.remainingCount.component;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.remainingCount.bean.RemainingCountRecordUpdateData;
import com.gateweb.charge.contract.remainingCount.bean.RemainingRecordUpdateReq;
import com.gateweb.charge.feeCalculation.dataCounter.IasrDataCounterByInvoiceDate;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RemainingRecordUpdateByInvoiceDate {
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    IasrDataCounterByInvoiceDate iasrDataCounterByInvoiceDate;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    RemainingRecordModelComponent remainingRecordModelComponent;

    public List<InvoiceRemaining> sortByInvoiceDate(Collection<InvoiceRemaining> invoiceRemainingCollection) {
        return invoiceRemainingCollection.stream().sorted(new Comparator<InvoiceRemaining>() {
            @Override
            public int compare(InvoiceRemaining o1, InvoiceRemaining o2) {
                return o1.getInvoiceDate().compareTo(o2.getInvoiceDate());
            }
        }).collect(Collectors.toList());
    }

    public Optional<CustomInterval> getSearchInterval(InvoiceRemaining targetRecord) {
        Optional<CustomInterval> result = Optional.empty();
        try {
            Optional<InvoiceRemaining> prevRecordOpt
                    = invoiceRemainingRepository.findTopByCompanyIdAndInvoiceRemainingIdLessThanOrderByInvoiceRemainingIdDesc(
                    targetRecord.getCompanyId()
                    , targetRecord.getInvoiceRemainingId()
            );
            if (prevRecordOpt.isPresent()) {
                LocalDateTime start = LocalDateTimeUtils.parseLocalDateFromString(
                        prevRecordOpt.get().getInvoiceDate(), "yyyyMMdd"
                ).get().atStartOfDay();

                LocalDateTime end = LocalDateTimeUtils.parseLocalDateFromString(
                        targetRecord.getInvoiceDate(), "yyyyMMdd"
                ).get().atStartOfDay().minusSeconds(1);
                result = Optional.of(new CustomInterval(start, end));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    public void executeUpdate(Set<Company> companySet) {
        companySet.stream().forEach(company -> {
            List<InvoiceRemaining> unstableRecordList
                    = new ArrayList<>(
                    invoiceRemainingRepository.findUnstableRemainingCountByCompanyId(company.getCompanyId().longValue())
            );
            List<RemainingRecordUpdateReq> remainingRecordUpdateReqList = new ArrayList<>();
            //產生RemainingRecordUpdateReq
            unstableRecordList = sortByInvoiceDate(unstableRecordList);
            unstableRecordList.stream().forEach(unstableRecord -> {
                Optional<RemainingRecordUpdateReq> remainingRecordUpdateReqOpt = genUpdateReq(
                        company.getBusinessNo(), unstableRecord);
                if (remainingRecordUpdateReqOpt.isPresent()) {
                    remainingRecordUpdateReqList.add(remainingRecordUpdateReqOpt.get());
                }
            });
            List<RemainingCountRecordUpdateData> updateDataList = genUpdateDataList(remainingRecordUpdateReqList);
            updateDataList.stream().forEach(recordUpdateData -> {
                updateData(recordUpdateData);
            });
        });
    }

    private Optional<RemainingRecordUpdateReq> genUpdateReq(String businessNo, InvoiceRemaining targetRecord) {
        Optional<RemainingRecordUpdateReq> result = Optional.empty();
        Optional<InvoiceRemaining> prevRecordOpt
                = invoiceRemainingRepository.findTopInvoiceRemainingByCompanyIdAndInvoiceDateLessThanOrderByInvoiceDateDesc(
                targetRecord.getCompanyId()
                , targetRecord.getInvoiceDate()
        );
        if (prevRecordOpt.isPresent()) {
            RemainingRecordUpdateReq remainingRecordUpdateReq = new RemainingRecordUpdateReq(
                    businessNo
                    , targetRecord
                    , prevRecordOpt.get()
            );
            result = Optional.of(remainingRecordUpdateReq);
        }
        return result;
    }

    private List<RemainingCountRecordUpdateData> genUpdateDataList(List<RemainingRecordUpdateReq> remainingRecordUpdateReqList) {
        List<RemainingCountRecordUpdateData> resultList = new ArrayList<>();
        remainingRecordUpdateReqList.stream().forEach(req -> {
            Optional<CustomInterval> calculateIntervalOpt = remainingRecordModelComponent.genRemainingRecordInterval(
                    req.getPrevRecord().getInvoiceDate()
                    , req.getTargetRecord().getInvoiceDate()
            );
            if (calculateIntervalOpt.isPresent()) {
                CustomInterval targetInterval = new CustomInterval(
                        calculateIntervalOpt.get().getStartLocalDateTime()
                        , calculateIntervalOpt.get().getEndLocalDateTime()
                );
                //取得張數差異
                Optional<Integer> newCountOpt = iasrDataCounterByInvoiceDate.count(
                        req.getBusinessNo()
                        , targetInterval
                );
                if (newCountOpt.isPresent()) {
                    int diff = newCountOpt.get() - req.getTargetRecord().getUsage();
                    if (diff != 0) {
                        //查詢會影響的記錄
                        List<InvoiceRemaining> relatedRecordList =
                                updateRemainingFromRecord(
                                        req.getTargetRecord(), req.getTargetRecord().getRemaining() + diff
                                );
                        //組合
                        RemainingCountRecordUpdateData remainingCountRecordUpdateData = new RemainingCountRecordUpdateData(
                                diff,
                                req.getTargetRecord(),
                                targetInterval,
                                relatedRecordList
                        );
                        resultList.add(remainingCountRecordUpdateData);
                    }
                }
            }
        });
        return resultList;
    }

    private void updateData(RemainingCountRecordUpdateData remainingCountRecordUpdateData) {
        List<InvoiceRemaining> saveList = new ArrayList<>();
        Integer newUsage = remainingCountRecordUpdateData.getTargetInvoiceRemaining().getUsage()
                + remainingCountRecordUpdateData.getDiff();
        remainingCountRecordUpdateData.getTargetInvoiceRemaining().setUsage(newUsage);
        Integer newRemaining = remainingCountRecordUpdateData.getTargetInvoiceRemaining().getRemaining()
                - remainingCountRecordUpdateData.getDiff();
        remainingCountRecordUpdateData.getTargetInvoiceRemaining().setRemaining(newRemaining);
        remainingCountRecordUpdateData.getTargetInvoiceRemaining().setModifyDate(LocalDateTime.now());
        saveList.add(remainingCountRecordUpdateData.getTargetInvoiceRemaining());
        for (InvoiceRemaining invoiceRemaining : remainingCountRecordUpdateData.getRelatedList()) {
            Integer newSubRemaining = invoiceRemaining.getRemaining() - remainingCountRecordUpdateData.getDiff();
            invoiceRemaining.setRemaining(newSubRemaining);
            invoiceRemaining.setModifyDate(LocalDateTime.now());
            saveList.add(invoiceRemaining);
            logger.info("RemainingCountUpdate :" + invoiceRemaining);
        }
        invoiceRemainingRepository.saveAll(saveList);
    }

    public List<InvoiceRemaining> updateRemainingFromRecord(InvoiceRemaining targetRecord, Integer newRemaining) {
        //找出需要更新的項目
        List<InvoiceRemaining> updateRecordList =
                invoiceRemainingRepository.findByCompanyIdAndInvoiceDateGreaterThanOrderByInvoiceDate(
                        targetRecord.getCompanyId()
                        , targetRecord.getInvoiceDate()
                ).stream().filter(record -> {
                    return !record.getInvoiceRemainingId().equals(targetRecord.getInvoiceRemainingId());
                }).collect(Collectors.toList());
        Integer nextRemaining = newRemaining;
        for (InvoiceRemaining record : updateRecordList) {
            record.setRemaining(nextRemaining - record.getUsage());
            nextRemaining = record.getRemaining();

        }
        return updateRecordList;
    }
}
