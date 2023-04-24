package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.component.ContractRenewComponent;
import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.RemainingContractRenewReq;
import com.gateweb.charge.contract.remainingCount.source.RemainingCountAmountProvider;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrame;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrameComponent;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrameUtils;
import com.gateweb.charge.contract.remainingCount.scheduleJob.updateRecord.RemainingRecordUpdateByInvoiceDate;
import com.gateweb.charge.feeCalculation.dataCounter.IasrDataCounterByInvoiceDate;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class NegativeRemainingRenewDataCollectorDispatcher implements RemainingContractRenewDataCollectorDispatcher {
    @Autowired
    ContractRenewComponent contractRenewComponent;
    @Autowired
    RemainingContractComponent remainingContractComponent;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    IasrDataCounterByInvoiceDate iasrDataCounterByInvoiceDate;
    @Autowired
    RemainingRecordUpdateByInvoiceDate remainingRecordUpdateByInvoiceDate;
    @Autowired
    RemainingRecordFrameComponent remainingRecordFrameComponent;
    @Autowired
    RemainingCountAmountProvider remainingCountAmountProvider;

    @Override
    public Optional<ChargeRemainingCountRenewData> execute(final RemainingContractRenewReq remainingContractRenewReq) {
        InvoiceRemaining negativeRecord = remainingContractRenewReq.getRemainingRecordFrame().getTargetRecord();
        Company company = remainingContractRenewReq.getCompany();
        Contract contract = remainingContractRenewReq.getContract();

        Optional<CustomInterval> searchIntervalOpt = RemainingRecordFrameUtils.genRemainingRecordInvoiceDateInterval(
                remainingContractRenewReq.getRemainingRecordFrame().getPrevRecord().getInvoiceDate()
                , negativeRecord.getInvoiceDate()
        );
        Optional<CustomInterval> marginIntervalOpt = findMarginInterval(
                company.getBusinessNo()
                , remainingContractRenewReq.getRemainingRecordFrame().getPrevRecord().getRemaining()
                , searchIntervalOpt.get()
        );
        if (marginIntervalOpt.isPresent()) {
            return marginExistsProcess(
                    company
                    , contract
                    , negativeRecord
                    , searchIntervalOpt.get()
                    , marginIntervalOpt.get()
            );
        } else {
            //使用當前負項的區間作為邊際區間
            return marginNotExistsProcess(
                    company
                    , contract
                    , remainingContractRenewReq.getRemainingRecordFrame().getPrevRecord()
                    , negativeRecord
                    , searchIntervalOpt.get()
            );
        }
    }

    private Optional<ChargeRemainingCountRenewData> marginNotExistsProcess(
            Company company
            , Contract contract
            , InvoiceRemaining prevRecord
            , InvoiceRemaining negativeRecord
            , CustomInterval searchInterval) {
        CustomInterval renewRecordInterval = new CustomInterval(
                searchInterval.getStartLocalDateTime()
                , searchInterval.getEndLocalDateTime()
        );
        //更新舊合約
        contract.setExpirationDate(
                renewRecordInterval.getStartLocalDateTime().minusSeconds(1)
        );
        //產生續約合約
        Optional<Contract> renewContractOpt = contractRenewComponent.genRenewRemainingContract(
                contract
                , searchInterval.getStartLocalDateTime()
        );
        if (!renewContractOpt.isPresent()) {
            return Optional.empty();
        }
        //根據續約合約產生新的記錄
        Optional<RemainingRecordFrame> remainingRecordModelOptional = remainingRecordFrameComponent.genModelForNonMarginNegativeCase(
                company
                , renewContractOpt.get()
                , prevRecord
                , negativeRecord
        );

        List<InvoiceRemaining> relatedRecordList =
                remainingRecordUpdateByInvoiceDate.updateRemainingFromRecord(
                        negativeRecord
                        , remainingRecordModelOptional.get().getTargetRecord().getRemaining()
                );

        if (remainingRecordModelOptional.isPresent()) {
            return Optional.of(
                    new ChargeRemainingCountRenewData(
                            contract
                            , renewContractOpt.get()
                            , remainingRecordModelOptional.get()
                            , relatedRecordList
                    )
            );
        }
        return Optional.empty();
    }

    private Optional<ChargeRemainingCountRenewData> marginExistsProcess(
            Company company
            , Contract contract
            , InvoiceRemaining negativeRecord
            , CustomInterval searchInterval
            , CustomInterval marginInterval) {
        //更新原有的負項記錄
        updateNegativeRecord(company.getBusinessNo(), negativeRecord, marginInterval);
        CustomInterval renewRecordInterval = new CustomInterval(
                marginInterval.getEndLocalDateTime().plusSeconds(1)
                , searchInterval.getEndLocalDateTime()
        );
        //更新舊合約
        contract.setExpirationDate(
                renewRecordInterval.getStartLocalDateTime().minusSeconds(1)
        );
        //產生續約合約
        Optional<Contract> renewContractOpt = contractRenewComponent.genRenewRemainingContract(
                contract
                , negativeRecord.getInvoiceDate()
        );
        if (!renewContractOpt.isPresent()) {
            return Optional.empty();
        }
        //根據續約合約產生新的記錄
        Optional<InvoiceRemaining> renewRemainingRecordOpt =
                genRenewRemainingRecord(
                        company.getBusinessNo()
                        , renewRecordInterval
                        , negativeRecord
                        , renewContractOpt.get()
                );
        List<InvoiceRemaining> relatedRecordList =
                remainingRecordUpdateByInvoiceDate.updateRemainingFromRecord(
                        negativeRecord, renewRemainingRecordOpt.get().getRemaining()
                );
        return Optional.of(
                new ChargeRemainingCountRenewData(
                        contract
                        , renewContractOpt.get()
                        , new RemainingRecordFrame(negativeRecord, renewRemainingRecordOpt.get())
                        , relatedRecordList
                )
        );
    }

    public void updateNegativeRecord(String businessNo, InvoiceRemaining negativeRecord, CustomInterval marginInterval) {
        Optional<String> newInvoiceDateOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                marginInterval.getEndLocalDateTime(), "yyyyMMdd"
        );
        //使用新的區間查詢張數並更新
        Optional<Integer> usageOpt = iasrDataCounterByInvoiceDate.count(businessNo, marginInterval);
        Integer originalRemaining = negativeRecord.getRemaining() + negativeRecord.getUsage();
        if (newInvoiceDateOpt.isPresent() && usageOpt.isPresent()) {
            negativeRecord.setInvoiceDate(newInvoiceDateOpt.get());
            negativeRecord.setUsage(usageOpt.get());
            negativeRecord.setRemaining(originalRemaining - usageOpt.get());
        }
    }

    public Optional<InvoiceRemaining> genRenewRemainingRecord(
            String businessNo
            , CustomInterval renewRecordInterval
            , InvoiceRemaining prevRecord
            , Contract renewContract) {
        //取得新合約的張數
        Optional<Integer> remainingOpt = remainingCountAmountProvider.getRemainingCountFromPackageId(renewContract.getPackageId());
        Optional<String> invoiceDateOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                renewRecordInterval.getEndLocalDateTime(), "yyyyMMdd"
        );
        Optional<Integer> usageOpt = iasrDataCounterByInvoiceDate.count(businessNo, renewRecordInterval);
        if (remainingOpt.isPresent() && invoiceDateOpt.isPresent() && usageOpt.isPresent() && remainingOpt.get() > 0) {
            InvoiceRemaining invoiceRemaining = new InvoiceRemaining();
            invoiceRemaining.setCompanyId(prevRecord.getCompanyId());
            invoiceRemaining.setInvoiceDate(invoiceDateOpt.get());
            invoiceRemaining.setUsage(usageOpt.get());
            invoiceRemaining.setRemaining(remainingOpt.get() + prevRecord.getRemaining() - usageOpt.get());
            invoiceRemaining.setCreateDate(LocalDateTime.now());
            invoiceRemaining.setModifyDate(LocalDateTime.now());
            return Optional.of(invoiceRemaining);
        }
        return Optional.empty();
    }

    /**
     * 找到邊際的invoiceDate
     *
     * @param businessNo
     * @param customInterval
     * @return
     */
    private Optional<CustomInterval> findMarginInterval(final String businessNo, final Integer remaining, final CustomInterval customInterval) {
        if (customInterval.valid()) {
            Optional<Integer> countOpt = iasrDataCounterByInvoiceDate.count(businessNo, customInterval);
            if (countOpt.isPresent()) {
                if (countOpt.get() < remaining) {
                    return Optional.of(customInterval);
                } else {
                    CustomInterval newCustomInterval = new CustomInterval(
                            customInterval.getStartLocalDateTime()
                            , customInterval.getEndLocalDateTime().minusDays(1)
                    );
                    return findMarginInterval(businessNo, remaining, newCustomInterval);
                }
            }
        }
        return Optional.empty();
    }
}
