package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component;

import com.gateweb.charge.contract.component.ContractRenewComponent;
import com.gateweb.charge.contract.remainingCount.source.RemainingCountAmountProvider;
import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrame;
import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.RemainingContractRenewReq;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ExpireRenewWithoutNewRecordDataCollector implements RemainingContractExpireRenewDataCollector {
    @Autowired
    RemainingCountAmountProvider remainingCountAmountProvider;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    ChargeRemainingCountRenewDataGenerator chargeRemainingCountRenewDataGenerator;
    @Autowired
    ContractRenewComponent contractRenewComponent;

    public Optional<RemainingRecordFrame> updateForExpireCaseNonSplitRenew(
            Long renewPackageId
            , RemainingRecordFrame prevFrame) {
        //取得新合約的張數
        Optional<Integer> remainingOpt = remainingCountAmountProvider.getRemainingCountFromPackageId(renewPackageId);
        if (remainingOpt.isPresent()) {
            InvoiceRemaining updatedTargetRecord = prevFrame.getTargetRecord();
            updatedTargetRecord.setRemaining(
                    remainingOpt.get() - prevFrame.getTargetRecord().getUsage()
            );
            //清空合約號碼
            updatedTargetRecord.setContractId(null);
            return Optional.of(new RemainingRecordFrame(
                    prevFrame.getPrevRecord()
                    , updatedTargetRecord
            ));
        }
        return Optional.empty();
    }

    @Override
    public Optional<ChargeRemainingCountRenewData> execute(RemainingContractRenewReq remainingContractRenewReq, Contract renewedContract) {
        Optional<RemainingRecordFrame> remainingRecordModelOptional
                = updateForExpireCaseNonSplitRenew(
                renewedContract.getPackageId(), remainingContractRenewReq.getRemainingRecordFrame()
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
