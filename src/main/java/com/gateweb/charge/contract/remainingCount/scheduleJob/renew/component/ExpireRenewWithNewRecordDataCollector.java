package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.remainingCount.source.RemainingCountAmountProvider;
import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.RemainingContractRenewReq;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrame;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrameComponent;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrameUtils;
import com.gateweb.charge.feeCalculation.dataCounter.IasrDataCounterByInvoiceDate;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ExpireRenewWithNewRecordDataCollector implements RemainingContractExpireRenewDataCollector {
    @Autowired
    RemainingRecordFrameComponent remainingRecordFrameComponent;
    @Autowired
    RemainingCountAmountProvider remainingCountAmountProvider;
    @Autowired
    IasrDataCounterByInvoiceDate iasrDataCounterByInvoiceDate;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    ChargeRemainingCountRenewDataGenerator chargeRemainingCountRenewDataGenerator;

    public Optional<InvoiceRemaining> updateRenewRecord(
            Company company,
            Contract renewedContract,
            InvoiceRemaining prev,
            InvoiceRemaining target) {
        //取得新合約的張數
        Optional<Integer> remainingOpt = remainingCountAmountProvider.getRemainingCountFromPackageId(renewedContract.getPackageId());
        Optional<CustomInterval> renewRecordIntervalOpt = RemainingRecordFrameUtils.genRemainingRecordInvoiceDateInterval(
                prev.getInvoiceDate(), target.getInvoiceDate()
        );
        //計算新區間的張數
        if (renewRecordIntervalOpt.isPresent()) {
            Optional<Integer> usageOpt = iasrDataCounterByInvoiceDate.count(company.getBusinessNo(), renewRecordIntervalOpt.get());
            if (usageOpt.isPresent() && remainingOpt.isPresent()) {
                target.setRemaining(remainingOpt.get() - usageOpt.get());
                target.setUsage(usageOpt.get());
                target.setModifyDate(LocalDateTime.now());
                return Optional.of(target);
            }
        }
        return Optional.empty();
    }

    public Optional<InvoiceRemaining> genNewMarginRecord(
            Company company
            , Contract contract
            , InvoiceRemaining prev) {
        Optional<CustomInterval> expireCaseMarginRecordIntervalOpt
                = remainingRecordFrameComponent.getExpireCaseMarginRecordInterval(
                prev, contract
        );
        if (expireCaseMarginRecordIntervalOpt.isPresent()) {
            //計算新區間的張數
            Optional<Integer> usageOpt = iasrDataCounterByInvoiceDate.count(company.getBusinessNo(), expireCaseMarginRecordIntervalOpt.get());
            Optional<String> invoiceDateOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                    contract.getExpirationDate(), "yyyyMMdd"
            );
            InvoiceRemaining invoiceRemaining = new InvoiceRemaining();
            invoiceRemaining.setCompanyId(contract.getCompanyId());
            invoiceRemaining.setContractId(contract.getContractId());
            invoiceRemaining.setRemaining(prev.getRemaining() - usageOpt.get());
            invoiceRemaining.setInvoiceDate(invoiceDateOpt.get());
            invoiceRemaining.setUsage(usageOpt.get());
            invoiceRemaining.setModifyDate(LocalDateTime.now());
            invoiceRemaining.setCreateDate(LocalDateTime.now());
            return Optional.of(invoiceRemaining);
        } else {
            return Optional.empty();
        }
    }

    /**
     * renew frame object
     *
     * @param company
     * @param originalContract
     * @param renewedContract
     * @param prevFrame
     * @return
     */
    public Optional<RemainingRecordFrame> genNewFrameForExpireSplitCaseRenew(
            Company company
            , Contract originalContract
            , Contract renewedContract
            , RemainingRecordFrame prevFrame) {
        Optional<InvoiceRemaining> newMarginRecordOpt = genNewMarginRecord(company, originalContract, prevFrame.getPrevRecord());
        if (newMarginRecordOpt.isPresent()) {
            Optional<InvoiceRemaining> updatedRenewRecordOpt =
                    updateRenewRecord(company, renewedContract, newMarginRecordOpt.get(), prevFrame.getTargetRecord());
            if (updatedRenewRecordOpt.isPresent()) {
                return Optional.of(
                        new RemainingRecordFrame(
                                newMarginRecordOpt.get()
                                , updatedRenewRecordOpt.get()
                        )
                );
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<ChargeRemainingCountRenewData> execute(RemainingContractRenewReq remainingContractRenewReq, Contract renewedContract) {
        Optional<RemainingRecordFrame> remainingRecordModelOptional = genNewFrameForExpireSplitCaseRenew(
                remainingContractRenewReq.getCompany(),
                remainingContractRenewReq.getContract(),
                renewedContract,
                remainingContractRenewReq.getRemainingRecordFrame()
        );
        if (remainingRecordModelOptional.isPresent()) {
            return chargeRemainingCountRenewDataGenerator.execute(
                    remainingContractRenewReq.getContract(), renewedContract, remainingRecordModelOptional.get()
            );
        } else {
            return Optional.empty();
        }
    }
}
