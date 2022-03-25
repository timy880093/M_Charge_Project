package com.gateweb.charge.bill.component;

import com.gateweb.charge.enumeration.BillStatus;
import com.gateweb.charge.enumeration.PaidPlan;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.repository.BillRepository;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PaidStatusComponent {

    private BillingItemRepository billingItemRepository;
    private BillRepository billRepository;

    public PaidStatusComponent(BillingItemRepository billingItemRepository, BillRepository billRepository) {
        this.billingItemRepository = billingItemRepository;
        this.billRepository = billRepository;
    }

    @Deprecated
    public boolean isContractBeenPaid(Contract contract) {
        //查詢該合約的所有預繳項目
        Collection<BillingItem> relatedBillingItem = billingItemRepository.findByContractIdAndPaidPlan(
                contract.getContractId()
                , PaidPlan.PRE_PAID
        );
        //情境二，所有項目已繳費
        Set<BillingItem> paidBillingItemSet = paidBillingItemFilter(relatedBillingItem);
        //情境三，所有項目都被扣抵
        Set<BillingItem> deductedBillingItemSet = deductedBillingItemFilter(relatedBillingItem);
        return (paidBillingItemSet.size() + deductedBillingItemSet.size()) == relatedBillingItem.size();
    }

    public Set<BillingItem> deductedBillingItemFilter(Collection<BillingItem> billingItemCollection) {
        return billingItemCollection.stream().filter(billingItem -> {
            return billingItem.getIsMemo();
        }).collect(Collectors.toSet());
    }

    public Set<BillingItem> paidBillingItemFilter(Collection<BillingItem> billingItemCollection) {
        Set<BillingItem> billedBillingItem = new HashSet<>();
        Set<Long> billIdSet = new HashSet<>();
        billingItemCollection.stream().forEach(billingItem -> {
            if (billingItem.getBillId() != null) {
                billIdSet.add(billingItem.getBillId());
                billedBillingItem.add(billingItem);
            }
        });
        Set<BillingItem> paidBillingItemSet = new HashSet<>();
        billIdSet.stream().forEach(billId -> {
            Optional<Bill> billOpt = billRepository.findById(billId);
            if (billOpt.isPresent() && billOpt.get().getBillStatus().equals(BillStatus.P)) {
                billedBillingItem.stream().forEach(billingItem -> {
                    paidBillingItemSet.add(billingItem);
                });
            }
            billedBillingItem.removeAll(paidBillingItemSet);
        });
        return paidBillingItemSet;
    }

    public boolean anyOfPrepaidItemBeenPaid(Set<BillingItem> billingItemSet) {
        boolean result = false;
        Set<BillingItem> paidBillingItem = paidBillingItemFilter(billingItemSet);
        for (BillingItem billingItem : paidBillingItem) {
            if (billingItem.getPaidPlan().equals(PaidPlan.PRE_PAID)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public Set<Bill> getUnpaidBillSet(Set<BillingItem> totalBillingItemSet) {
        Set<Bill> unpaidBillSet = new HashSet<>();
        Set<Long> targetBillIdSet = new HashSet<>();
        totalBillingItemSet.stream().forEach(billingItem -> {
            if (billingItem.getBillId() != null) {
                if (!targetBillIdSet.contains(billingItem.getBillId())) {
                    Optional<Bill> billOptional = billRepository.findById(billingItem.getBillId());
                    if (billOptional.isPresent()) {
                        if (!billOptional.get().getBillStatus().equals(BillStatus.P)) {
                            unpaidBillSet.add(billOptional.get());
                        }
                    }
                }
            }
        });
        return unpaidBillSet;
    }
}
