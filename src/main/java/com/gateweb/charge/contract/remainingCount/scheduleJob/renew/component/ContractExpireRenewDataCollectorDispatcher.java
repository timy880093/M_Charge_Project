package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component;

import com.gateweb.charge.contract.component.ContractRenewComponent;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrameComponent;
import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.RemainingContractRenewReq;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ContractExpireRenewDataCollectorDispatcher implements RemainingContractRenewDataCollectorDispatcher {
    @Autowired
    ContractRenewComponent contractRenewComponent;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    RemainingContractComponent remainingContractComponent;
    @Autowired
    RemainingRecordFrameComponent remainingRecordFrameComponent;
    @Autowired
    ExpireRenewWithNewRecordDataCollector expireRenewWithNewRecordDataCollector;
    @Autowired
    ExpireRenewWithoutNewRecordDataCollector expireRenewWithoutNewRecordDataCollector;

    @Override
    public Optional<ChargeRemainingCountRenewData> execute(final RemainingContractRenewReq remainingContractRenewReq) {
        Contract contract = remainingContractRenewReq.getContract();
        Optional<Contract> renewContractOpt = contractRenewComponent.genExpireRemainingRenewContract(
                contract
                , contract.getExpirationDate().plusSeconds(1)
        );
        if (renewContractOpt.isPresent()) {
            //取消autoRenew
            contract.setAutoRenew(false);
            boolean createMarginRecord = needToCreateMarginRecord(
                    contract, remainingContractRenewReq.getRemainingRecordFrame().getPrevRecord()
            );
            //確認是否需要拆分
            if (!createMarginRecord) {
                return expireRenewWithoutNewRecordDataCollector.execute(
                        remainingContractRenewReq,
                        renewContractOpt.get()
                );
            } else {
                //依照前一個記錄產生新的
                return expireRenewWithNewRecordDataCollector.execute(
                        remainingContractRenewReq,
                        renewContractOpt.get()
                );
            }
        }
        return Optional.empty();
    }

    /**
     * 如果剛好前一個記錄的結束日就是合約結束日
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
