package com.gateweb.charge.contract.component;

import com.gateweb.charge.bill.component.PaidStatusComponent;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.enumeration.PaidPlan;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.orm.charge.entity.*;
import com.gateweb.orm.charge.repository.*;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 暫時不考慮複雜的扣抵情境，如果有一天要實作啟用中的合約終止再來想
 */
@Component
public class ContractTerminateComponent {
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    BillRepository billRepository;
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    ChargeRuleRepository chargeRuleRepository;
    @Autowired
    ContractEnableComponent contractEnableComponent;
    @Autowired
    PaidStatusComponent paidStatusComponent;
    @Autowired
    DeductHistoryRepository deductHistoryRepository;

    EventBus chargeSystemEventBus;

    @Autowired
    @Qualifier("chargeSystemEventBus")
    public void setChargeSystemEventBus(EventBus chargeSystemEventBus) {
        this.chargeSystemEventBus = chargeSystemEventBus;
    }

    public Set<BillingItem> getUpdateItemSet(Set<Bill> unpaidBillSet) {
        Set<BillingItem> updateBillingItemSet = new HashSet<>();
        unpaidBillSet.stream().forEach(bill -> {
            updateBillingItemSet.addAll(billingItemRepository.findByBillId(bill.getBillId()));
        });
        updateBillingItemSet.stream().forEach(billingItem -> {
            //移除這些billingItem與bill的關聯
            billingItem.setBillId(null);
        });
        return updateBillingItemSet;
    }

    public Set<BillingItem> getDeleteItemSet(Long contractId, Set<BillingItem> updateBillingItemSet) {
        Set<BillingItem> deleteBillingItemSet = new HashSet<>();
        updateBillingItemSet.stream().forEach(billingItem -> {
            if (billingItem.getPaidPlan().equals(PaidPlan.PRE_PAID)
                    && billingItem.getContractId().equals(contractId)) {
                deleteBillingItemSet.add(billingItem);
            }
        });
        updateBillingItemSet.removeAll(deleteBillingItemSet);
        return deleteBillingItemSet;
    }

    /**
     * unpaid的billingItem中，有沒有包含chargeRule是chargeByRemainingCount的規則
     *
     * @param contractId
     * @return
     */
    public Set<InvoiceRemaining> getInvoiceRemainingSet(Long contractId) {
        Set<InvoiceRemaining> invoiceRemainingSet = invoiceRemainingRepository.findByContractId(contractId);
        return invoiceRemainingSet;
    }

    /**
     * 只有特定狀態及條件的合約可以被終止
     *
     * @param contract
     * @return
     */
    public void terminateContract(Contract contract) {
        terminateContractWithStatus(contract, ContractStatus.T);
    }

    public void disableContract(Contract contract) {
        terminateContractWithStatus(contract, ContractStatus.D);
    }

    private void terminateContractWithStatus(Contract contract, ContractStatus status) {
        //狀態檢查
        Set<BillingItem> billingItemSet = new HashSet<>(billingItemRepository.findByContractId(contract.getContractId()));

        boolean anyOfPrepaidItemBeenPaid = paidStatusComponent.anyOfPrepaidItemBeenPaid(billingItemSet);
        //未繳費才能夠刪除計數
        if (!anyOfPrepaidItemBeenPaid) {
            //刪除invoiceRemaining的計數
            Set<InvoiceRemaining> invoiceRemainingSet = getInvoiceRemainingSet(contract.getContractId());
            invoiceRemainingRepository.deleteInBatch(invoiceRemainingSet);
        }

        Set<Bill> unpaidBillSet = paidStatusComponent.getUnpaidBillSet(billingItemSet);
        Set<BillingItem> updateItemSet = getUpdateItemSet(unpaidBillSet);
        Set<BillingItem> deleteItemSet = getDeleteItemSet(contract.getContractId(), billingItemSet);

        //已入帳合約也要可以終止，因為他們承諾日後不需要回收金額，因此帳單不需要另外處理，只要修改合約狀態就好了。
        if (!unpaidBillSet.isEmpty()) {

            //移除billingItem與bill的關聯
            billingItemRepository.saveAll(updateItemSet);
            batchBillingItemCancelBillingEventPost(updateItemSet);

            //刪除所有相關bill
            billRepository.deleteInBatch(unpaidBillSet);
            batchBillCancelBillEventPost(unpaidBillSet);
        }

        if (!deleteItemSet.isEmpty()) {
            //刪除預繳的項目
            billingItemRepository.deleteInBatch(deleteItemSet);
            batchBillingItemDeleteEventPost(deleteItemSet);
        }

        //更新contract
        contract.setStatus(status);
        contractRepository.save(contract);
    }

    public void batchBillingItemCancelBillingEventPost(Collection<BillingItem> billingItemCollection) {
        billingItemCollection.stream().forEach(billingItem -> {
            ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                    EventSource.BILLING_ITEM
                    , EventAction.CANCEL
                    , billingItem.getBillingItemId()
                    , null
            );
            chargeSystemEventBus.post(chargeSystemEvent);
        });
    }

    public void batchBillCancelBillEventPost(Collection<Bill> billCollection) {
        billCollection.stream().forEach(bill -> {
            ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                    EventSource.BILLING_ITEM
                    , EventAction.CANCEL
                    , bill.getBillId()
                    , null
            );
            chargeSystemEventBus.post(chargeSystemEvent);
        });
    }

    public void batchBillingItemDeleteEventPost(Collection<BillingItem> billingItemCollection) {
        billingItemCollection.stream().forEach(billingItem -> {
            ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                    EventSource.BILLING_ITEM
                    , EventAction.DELETE
                    , billingItem.getBillingItemId()
                    , null
            );
            chargeSystemEventBus.post(chargeSystemEvent);
        });
    }

}
