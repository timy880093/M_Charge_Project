package com.gateweb.charge.contract.remainingCount.component;

import com.gateweb.charge.contract.component.ContractRenewComponent;
import com.gateweb.charge.contract.remainingCount.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.remainingCount.bean.RemainingContractRenewReq;
import com.gateweb.charge.contract.remainingCount.bean.RemainingRecordModel;
import com.gateweb.orm.charge.entity.Contract;
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

    public Optional<RemainingRecordModel> updateForExpireCaseNonSplitRenew(
            Long renewPackageId
            , RemainingRecordModel remainingRecordModel) {
        //取得新合約的張數
        Optional<Integer> remainingOpt = remainingCountAmountProvider.getRemainingCountFromPackageId(renewPackageId);
        if (remainingOpt.isPresent()) {
            remainingRecordModel.getTargetRecord().setRemaining(remainingOpt.get());
            //清空合約號碼
            remainingRecordModel.getTargetRecord().setContractId(null);
            return Optional.of(remainingRecordModel);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ChargeRemainingCountRenewData> execute(RemainingContractRenewReq remainingContractRenewReq, Contract renewedContract) {
        Optional<RemainingRecordModel> remainingRecordModelOptional
                = updateForExpireCaseNonSplitRenew(
                renewedContract.getPackageId(), remainingContractRenewReq.getRemainingRecordModel()
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
