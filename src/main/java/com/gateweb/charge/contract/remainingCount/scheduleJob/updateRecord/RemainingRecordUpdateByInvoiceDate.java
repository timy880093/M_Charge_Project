package com.gateweb.charge.contract.remainingCount.scheduleJob.updateRecord;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrame;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrameComponent;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrameUtils;
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
    RemainingRecordFrameComponent remainingRecordFrameComponent;

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
            //產生RemainingRecordUpdateReq
            unstableRecordList = sortByInvoiceDate(unstableRecordList);
            unstableRecordList.stream().forEach(unstableRecord -> {
                Optional<RemainingRecordUpdateReq> remainingRecordUpdateReqOpt = genUpdateReq(
                        company.getBusinessNo(), unstableRecord);
                if (remainingRecordUpdateReqOpt.isPresent()) {
                    updateData(remainingRecordUpdateReqOpt.get());
                }
            });
        });
    }

    private Optional<RemainingRecordUpdateReq> genUpdateReq(String businessNo, InvoiceRemaining targetRecord) {
        Optional<RemainingRecordUpdateReq> result = Optional.empty();
        Optional<RemainingRecordFrame> remainingRecordFrameOpt
                = remainingRecordFrameComponent.getRemainingRecordModel(targetRecord);
        if (remainingRecordFrameOpt.isPresent()) {
            Optional<CustomInterval> calculateIntervalOpt = RemainingRecordFrameUtils.genRemainingRecordInvoiceDateInterval(
                    remainingRecordFrameOpt.get()
            );
            if (calculateIntervalOpt.isPresent()) {
                //取得張數差異
                Optional<Integer> newCountOpt = iasrDataCounterByInvoiceDate.count(
                        businessNo
                        , calculateIntervalOpt.get()
                );
                if (newCountOpt.isPresent()) {
                    boolean needToUpdate = needToUpdate(remainingRecordFrameOpt.get(), newCountOpt.get());
                    if (needToUpdate) {
                        //組合
                        RemainingRecordUpdateReq remainingRecordUpdateReq = new RemainingRecordUpdateReq(
                                businessNo
                                , remainingRecordFrameOpt.get()
                                , newCountOpt.get()
                                , remainingRecordFrameOpt.get().getPrevRecord().getRemaining() - newCountOpt.get()
                        );
                        result = Optional.of(remainingRecordUpdateReq);
                    }
                }
            }
        }
        return result;
    }

    boolean needToUpdate(RemainingRecordFrame frame, int newCount) {
        boolean wrongSummary = frame.getTargetRecord().getRemaining().compareTo(
                frame.getPrevRecord().getRemaining() - frame.getTargetRecord().getUsage().intValue()
        ) != 0;
        boolean sameContract = frame.getTargetRecord().getContractId().compareTo(frame.getPrevRecord().getContractId()) == 0;
        boolean invalidUsage = frame.getTargetRecord().getUsage() != newCount;
        return (sameContract && wrongSummary) || invalidUsage;
    }

    private void updateData(RemainingRecordUpdateReq req) {
        req.getRemainingRecordFrame().getTargetRecord().setRemaining(req.getNewRemaining());
        req.getRemainingRecordFrame().getTargetRecord().setUsage(req.getNewUsage());
        invoiceRemainingRepository.save(req.getRemainingRecordFrame().getTargetRecord());
    }

    @Deprecated
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
