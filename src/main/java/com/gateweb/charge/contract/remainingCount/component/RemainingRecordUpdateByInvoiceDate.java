package com.gateweb.charge.contract.remainingCount.component;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.remainingCount.bean.RemainingCountRecordUpdateData;
import com.gateweb.charge.contract.remainingCount.bean.RemainingRecordUpdateReq;
import com.gateweb.charge.feeCalculation.dataCounter.IasrDataCounterByInvoiceDate;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
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
            Optional<RemainingCountRecordUpdateData> recordUpdateDataOptional = genUpdateDataByList(remainingRecordUpdateReqList);
            recordUpdateDataOptional.ifPresent(recordUpdateData -> {
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

    private Optional<RemainingCountRecordUpdateData> genUpdateDataByList(List<RemainingRecordUpdateReq> reqList) {
        Optional<RemainingCountRecordUpdateData> resultOpt = Optional.empty();
        for (RemainingRecordUpdateReq remainingRecordUpdateReq : reqList) {
            Optional<RemainingCountRecordUpdateData> dataOpt = genUpdateData(remainingRecordUpdateReq);
            if (dataOpt.isPresent()) {
                resultOpt = dataOpt;
            }
        }
        return resultOpt;
    }

    boolean needToUpdate(RemainingRecordUpdateReq req, int newCount) {
        boolean wrongSummary = req.getTargetRecord().getRemaining().compareTo(
                req.getPrevRecord().getRemaining() - req.getTargetRecord().getUsage().intValue()
        ) != 0;
        boolean sameContract = req.getTargetRecord().getContractId().compareTo(req.getPrevRecord().getContractId()) == 0;
        boolean invalidUsage = req.getTargetRecord().getUsage() != newCount;
        return (sameContract && wrongSummary) || invalidUsage;
    }

    private Optional<RemainingCountRecordUpdateData> genUpdateData(RemainingRecordUpdateReq req) {
        Optional<RemainingCountRecordUpdateData> resultOpt = Optional.empty();
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
                boolean needToUpdate = needToUpdate(req, newCountOpt.get());
                if (needToUpdate) {
                    //查詢會影響的記錄
                    List<InvoiceRemaining> relatedRecordList =
                            updateRemainingFromRecord(
                                    req.getTargetRecord(), req.getPrevRecord().getRemaining()
                            );
                    //組合
                    RemainingCountRecordUpdateData remainingCountRecordUpdateData = new RemainingCountRecordUpdateData(
                            newCountOpt.get() - req.getTargetRecord().getUsage(),
                            req.getTargetRecord(),
                            req.getPrevRecord(),
                            relatedRecordList
                    );
                    resultOpt = Optional.of(remainingCountRecordUpdateData);
                }
            }
        }
        return resultOpt;
    }

    private void updateData(RemainingCountRecordUpdateData remainingCountRecordUpdateData) {
        List<InvoiceRemaining> saveList = new ArrayList<>();
        Integer newUsage = remainingCountRecordUpdateData.getTargetInvoiceRemaining().getUsage()
                + remainingCountRecordUpdateData.getDiff();
        remainingCountRecordUpdateData.getTargetInvoiceRemaining().setUsage(newUsage);
        Integer newRemaining = remainingCountRecordUpdateData.getPreviousInvoiceRemaining().getRemaining()
                - remainingCountRecordUpdateData.getTargetInvoiceRemaining().getUsage();
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
