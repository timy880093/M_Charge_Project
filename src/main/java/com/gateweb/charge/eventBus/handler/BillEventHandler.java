package com.gateweb.charge.eventBus.handler;

import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.service.ContractService;
import com.gateweb.charge.service.DeductService;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.Deduct;
import com.gateweb.orm.charge.repository.*;
import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class BillEventHandler implements ChargeSystemEventHandler {
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    BillRepository billRepository;
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    DeductRepository deductRepository;
    @Autowired
    ContractRepository contractRepository;

    ContractService contractService;

    @Autowired
    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    DeductService deductService;

    @Autowired
    public void setDeductService(DeductService deductService) {
        this.deductService = deductService;
    }

    @Override
    public void onMessage(EventBus eventBus, ChargeSystemEvent chargeSystemEvent) {
        switch (chargeSystemEvent.getEventAction()) {
            case PAID:
                onBillPaid(
                        chargeSystemEvent.getSourceId()
                        , chargeSystemEvent.getCallerId()
                );
                break;
            case DELETE:
            case CREATE:
            case CANCEL:
                break;
            default:
                logger.error("unknown action:{}", chargeSystemEvent);
                break;
        }
    }

    /**
     * 當帳單被付清後會觸發接下來的狀況
     *
     * @param billId
     */
    public void onBillPaid(long billId, long callerId) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isPresent()) {
            Set<BillingItem> billingItemSet = new HashSet<>(billingItemRepository.findByBillId(billId));
            billingItemSet.stream().forEach(billingItem -> {
                enableContractIfPaid(billingItem, callerId);
                enableDeductIfPaid(billingItem);
            });
        } else {
            logger.error("BillId:{}, Operation:onBillPaid , ignore.", billId);
        }
    }

    public void enableContractIfPaid(BillingItem billingItem, Long callerId) {
        if (billingItem.getContractId() != null && billingItem.getPackageRefId() != null) {
            //檢查有沒有合約被付款完成的
            Optional<Contract> contractOptional = contractRepository.findById(billingItem.getContractId());
            if (contractOptional.isPresent() && contractOptional.get().getStatus().equals(ContractStatus.B)) {
                contractService.enableContract(
                        contractOptional.get()
                        , callerId
                );
            }
        }
    }

    public void enableDeductIfPaid(BillingItem billingItem) {
        if (billingItem.getDeductId() != null) {
            //檢查有沒有扣抵或預繳付款完成
            Optional<Deduct> deductOptional = deductRepository.findById(billingItem.getDeductId());
            if (deductOptional.isPresent()) {
                deductService.enableDeduct(deductOptional.get());
            }
        }
    }
}
