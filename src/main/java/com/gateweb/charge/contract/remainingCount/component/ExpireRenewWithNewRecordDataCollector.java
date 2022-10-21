package com.gateweb.charge.contract.remainingCount.component;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.remainingCount.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.remainingCount.bean.RemainingContractRenewReq;
import com.gateweb.charge.contract.remainingCount.bean.RemainingRecordModel;
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
    RemainingRecordModelComponent remainingRecordModelComponent;
    @Autowired
    RemainingCountAmountProvider remainingCountAmountProvider;
    @Autowired
    IasrDataCounterByInvoiceDate iasrDataCounterByInvoiceDate;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    ChargeRemainingCountRenewDataGenerator chargeRemainingCountRenewDataGenerator;


    public Optional<RemainingRecordModel> genForExpireCaseSplitRenew(
            Company company
            , Contract contract
            , RemainingRecordModel remainingRecordModel) {
        Optional<CustomInterval> expireCaseMarginRecordIntervalOpt
                = remainingRecordModelComponent.getExpireCaseMarginRecordInterval(
                remainingRecordModel.getPrevRecord(), contract
        );
        //取得新合約的張數
        Optional<Integer> remainingOpt = remainingCountAmountProvider.getRemainingCountFromPackageId(contract.getPackageId());
        if (expireCaseMarginRecordIntervalOpt.isPresent()) {
            //計算新區間的張數
            Optional<Integer> usageOpt = iasrDataCounterByInvoiceDate.count(company.getBusinessNo(), expireCaseMarginRecordIntervalOpt.get());
            Optional<String> invoiceDateOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                    contract.getExpirationDate(), "yyyyMMdd"
            );
            InvoiceRemaining invoiceRemaining = new InvoiceRemaining();
            invoiceRemaining.setCompanyId(contract.getCompanyId());
            invoiceRemaining.setContractId(null);
            invoiceRemaining.setRemaining(remainingOpt.get() - usageOpt.get());
            invoiceRemaining.setInvoiceDate(invoiceDateOpt.get());
            invoiceRemaining.setUsage(usageOpt.get());
            invoiceRemaining.setModifyDate(LocalDateTime.now());
            invoiceRemaining.setCreateDate(LocalDateTime.now());
            remainingRecordModel.setTargetRecord(invoiceRemaining);
            return Optional.of(
                    new RemainingRecordModel(
                            remainingRecordModel.getPrevRecord()
                            , invoiceRemaining
                            , remainingRecordModel.getInvoiceDateInterval()
                    )
            );
        }
        return Optional.empty();
    }

    @Override
    public Optional<ChargeRemainingCountRenewData> execute(RemainingContractRenewReq remainingContractRenewReq, Contract renewedContract) {
        Optional<RemainingRecordModel> remainingRecordModelOptional = genForExpireCaseSplitRenew(
                remainingContractRenewReq.getCompany(), remainingContractRenewReq.getContract(), remainingContractRenewReq.getRemainingRecordModel()
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
