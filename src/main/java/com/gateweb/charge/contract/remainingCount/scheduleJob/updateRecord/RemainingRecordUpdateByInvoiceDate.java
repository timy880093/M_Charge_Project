package com.gateweb.charge.contract.remainingCount.scheduleJob.updateRecord;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrame;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrameComponent;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrameUtils;
import com.gateweb.charge.feeCalculation.dataCounter.IasrDataCounterByInvoiceDate;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.utils.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                remainingRecordUpdateReqOpt.ifPresent(this::updateData);
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
                    Optional<UpdateReqGenerator> updateReqGeneratorOpt
                            = updateReqGeneratorDispatcher(remainingRecordFrameOpt.get(), newCountOpt.get());
                    if (updateReqGeneratorOpt.isPresent()) {
                        return Optional.of(
                                updateReqGeneratorOpt.get().gen(
                                        remainingRecordFrameOpt.get(), newCountOpt.get(), businessNo
                                )
                        );
                    }
                }
            }
        }
        return result;
    }

    public Optional<UpdateReqGenerator> updateReqGeneratorDispatcher(RemainingRecordFrame frame, int newUsage) {
        Optional<UpdateTypeEnum> updateTypeEnumOpt = getUpdateType(frame, newUsage);
        if (updateTypeEnumOpt.isPresent()) {
            if (updateTypeEnumOpt.get().equals(UpdateTypeEnum.SAME_CONTRACT_UPDATE)) {
                return Optional.of(new SameContractUpdateReqGenerator());
            }
            if (updateTypeEnumOpt.get().equals(UpdateTypeEnum.DIFF_CONTRACT_UPDATE)) {
                return Optional.of(new DiffContractUpdateReqGenerator());
            }
        }
        return Optional.empty();
    }

    Optional<UpdateTypeEnum> getUpdateType(RemainingRecordFrame frame, int newCount) {
        boolean wrongRemaining = frame.getTargetRecord().getRemaining().compareTo(
                frame.getPrevRecord().getRemaining() - frame.getTargetRecord().getUsage()
        ) != 0;
        boolean sameContract = frame.getTargetRecord().getContractId().compareTo(frame.getPrevRecord().getContractId()) == 0;
        boolean wrongUsage = frame.getTargetRecord().getUsage() != newCount;
        if (sameContract && (wrongRemaining || wrongUsage)) {
            return Optional.of(UpdateTypeEnum.SAME_CONTRACT_UPDATE);
        } else if (!sameContract && (wrongUsage)) {
            return Optional.of(UpdateTypeEnum.DIFF_CONTRACT_UPDATE);
        }
        return Optional.empty();
    }

    private void updateData(RemainingRecordUpdateReq req) {
        req.getRemainingRecordFrame().getTargetRecord().setRemaining(req.getNewRemaining());
        req.getRemainingRecordFrame().getTargetRecord().setUsage(req.getNewUsage());
        invoiceRemainingRepository.save(req.getRemainingRecordFrame().getTargetRecord());
        logger.info("RemainingCountUpdate :{}", JsonUtils.gsonToJson(req));
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
