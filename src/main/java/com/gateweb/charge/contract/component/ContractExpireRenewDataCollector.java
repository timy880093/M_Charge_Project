package com.gateweb.charge.contract.component;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.remainingCount.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.remainingCount.bean.RemainingRecordModel;
import com.gateweb.charge.contract.remainingCount.bean.RemainingContractRenewReq;
import com.gateweb.charge.contract.remainingCount.component.RemainingContractComponent;
import com.gateweb.charge.contract.remainingCount.component.RemainingContractRenewDataCollector;
import com.gateweb.charge.contract.remainingCount.component.RemainingRecordModelComponent;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ContractExpireRenewDataCollector implements RemainingContractRenewDataCollector {
    @Autowired
    ContractRenewComponent contractRenewComponent;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    RemainingContractComponent remainingContractComponent;
    @Autowired
    RemainingRecordModelComponent remainingRecordModelComponent;

    @Override
    public Optional<ChargeRemainingCountRenewData> execute(final RemainingContractRenewReq remainingContractRenewReq) {
        InvoiceRemaining expireRecord = remainingContractRenewReq.getRemainingRecordModel().getTargetRecord();
        Contract contract = remainingContractRenewReq.getContract();
        Optional<Contract> renewContractOpt = contractRenewComponent.genRenewRemainingContract(
                contract
                , contract.getExpirationDate().plusSeconds(1)
        );
        if (renewContractOpt.isPresent()) {
            //取消autoRenew
            contract.setAutoRenew(false);
            Optional<RemainingRecordModel> remainingRecordModelOptional = genRemainingRecordModel(
                    contract
                    , renewContractOpt.get()
                    , remainingContractRenewReq
            );
            if (remainingRecordModelOptional.isPresent()) {
                //找出需要更新的項目
                List<InvoiceRemaining> updateRecordList =
                        invoiceRemainingRepository.findByCompanyIdAndInvoiceDateGreaterThanOrderByInvoiceDate(
                                contract.getCompanyId()
                                , expireRecord.getInvoiceDate()
                        );
                //更新計數
                Integer remaining = remainingRecordModelOptional.get().getTargetRecord().getRemaining();
                for (InvoiceRemaining updateRemaining : updateRecordList) {
                    updateRemaining.setRemaining(remaining - updateRemaining.getUsage());
                    remaining = updateRemaining.getRemaining();
                }
                ChargeRemainingCountRenewData chargeRemainingCountRenewData = new ChargeRemainingCountRenewData(
                        contract
                        , renewContractOpt.get()
                        , remainingRecordModelOptional.get()
                        , updateRecordList
                );
                return Optional.of(chargeRemainingCountRenewData);
            }
        }
        return Optional.empty();
    }

    private Optional<RemainingRecordModel> genRemainingRecordModel(
            Contract contract, Contract renewContract, RemainingContractRenewReq remainingContractRenewReq) {
        boolean createMarginRecord = needToCreateMarginRecord(
                contract, remainingContractRenewReq.getRemainingRecordModel().getPrevRecord()
        );
        //確認是否需要拆分
        if (!createMarginRecord) {
            return remainingRecordModelComponent.updateForExpireCaseNonSplitRenew(
                    renewContract
                    , remainingContractRenewReq.getRemainingRecordModel()
            );
        } else {
            CustomInterval newPrevRecordInterval = new CustomInterval(
                    remainingContractRenewReq.getRemainingRecordModel().getInvoiceDateInterval().getStartLocalDateTime()
                    , renewContract.getEffectiveDate().minusSeconds(1)
            );
            //依照前一個記錄產生新的
            Optional<RemainingRecordModel> prevRemainingRecordModel = remainingRecordModelComponent.genForExpireCaseSplitRenew(
                    remainingContractRenewReq.getCompany()
                    , remainingContractRenewReq.getRemainingRecordModel()
                    , newPrevRecordInterval
            );
            CustomInterval renewRecordInterval = new CustomInterval(
                    newPrevRecordInterval.getEndLocalDateTime().plusSeconds(1)
                    , remainingContractRenewReq.getRemainingRecordModel().getInvoiceDateInterval().getEndLocalDateTime()
            );
            if (prevRemainingRecordModel.isPresent()) {
                //依照續約內容產生新的
                return remainingRecordModelComponent.genRenewRemainingRecordModel(
                        remainingContractRenewReq.getCompany()
                        , contract
                        , prevRemainingRecordModel.get().getTargetRecord()
                        , renewRecordInterval
                        , true
                );
            }
        }
        return Optional.empty();
    }

    /**
     * 如果剛好前一個記錄的結束日天就是合約結束日
     *
     * @param originalContract
     * @param prevRecord
     * @return
     */
    private boolean needToCreateMarginRecord(Contract originalContract, InvoiceRemaining prevRecord) {
        Optional<String> originalContractInvoiceDate = LocalDateTimeUtils.parseLocalDateTimeToString(
                originalContract.getExpirationDate()
                , "yyyyMMdd"
        );
        if (originalContractInvoiceDate.isPresent()) {
            return !prevRecord.getInvoiceDate().equals(originalContractInvoiceDate.get());
        }
        return false;
    }
}
