package com.gateweb.charge.deduct.component;

import com.gateweb.charge.enumeration.DeductStatus;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Deduct;
import com.gateweb.orm.charge.entity.DeductHistory;
import com.gateweb.orm.charge.repository.DeductHistoryRepository;
import com.gateweb.orm.charge.repository.DeductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class DeductibleItemFilterComponent {
    @Autowired
    DeductRepository deductRepository;
    @Autowired
    DeductHistoryRepository deductHistoryRepository;

    public Collection<Deduct> findUsableDeductCollection(Long companyId, Long contractId) {
        BillingItem billingItem = new BillingItem();
        billingItem.setCompanyId(companyId);
        billingItem.setContractId(contractId);
        return findUsableDeductCollection(billingItem);
    }

    public Collection<Deduct> findUsableDeductCollection(BillingItem billingItem) {
        Collection<Deduct> matchedDeduct = new HashSet<>();
        Collection<Deduct> candidateDeduct = deductRepository.findByCompanyIdAndDeductStatus(billingItem.getCompanyId(), DeductStatus.E);
        candidateDeduct.stream().forEach(deduct -> {
            HashMap<String, Boolean> deductDecisionResultMap = deductDecisionResultMap(deduct, billingItem);
            boolean result = true;
            for (Boolean decisionFlag : deductDecisionResultMap.values()) {
                result = result && decisionFlag;
            }
            if (result) {
                matchedDeduct.add(deduct);
            }
        });
        return matchedDeduct;
    }

    /**
     * 畫扣抵決策的卡諾圖可以看到
     * 扣抵決策都是and邏輯，只是會因為deduct擁有的屬性而改變
     * 因此使用map作為多參數的彈性來源
     * 並在最後將所有的value一起判斷就簡單的多
     *
     * @return
     */
    public HashMap<String, Boolean> deductDecisionResultMap(Deduct deduct, BillingItem billingItem) {
        HashMap<String, Boolean> deductDecisionResultMap = new HashMap<>();
        HashMap<String, Long> deductDecisionDataMap = deductDecisionDataMap(deduct);
        if (deductDecisionDataMap.containsKey("companyId")) {
            deductDecisionResultMap.put(
                    "companyId",
                    billingItem.getCompanyId().equals(deductDecisionDataMap.get("companyId"))
            );
        }
        if (deductDecisionDataMap.containsKey("packageRefId")) {
            deductDecisionResultMap.put(
                    "packageRefId",
                    billingItem.getPackageRefId().equals(deductDecisionDataMap.get("packageRefId"))
            );
        }
        if (deductDecisionDataMap.containsKey("filterProductCategoryId")) {
            deductDecisionResultMap.put(
                    "filterProductCategoryId",
                    billingItem.getProductCategoryId().equals(deductDecisionDataMap.get("filterProductCategoryId"))
            );
        }
        Optional<Deduct> deductOptional = checkUsableAmount(deduct);
        deductDecisionResultMap.put("usableAmount", deductOptional.isPresent());
        return deductDecisionResultMap;
    }

    public HashMap<String, Long> deductDecisionDataMap(Deduct deduct) {
        HashMap<String, Long> decisionDataMap = new HashMap<>();
        if (deduct.getCompanyId() != null) {
            decisionDataMap.put("companyId", deduct.getCompanyId());
        }
        if (deduct.getPackageRefId() != null) {
            decisionDataMap.put("packageRefId", deduct.getPackageRefId());
        }
        if (deduct.getTargetProductCategoryId() != null) {
            decisionDataMap.put("targetProductCategoryId", deduct.getTargetProductCategoryId());
        }
        return decisionDataMap;
    }

    public Optional<Deduct> checkUsableAmount(Deduct deduct) {
        Optional<Deduct> result = Optional.empty();
        BigDecimal usableAmount = BigDecimal.ZERO;
        List<DeductHistory> deductHistoryList = deductHistoryRepository.findByDeductId(deduct.getDeductId());
        //查看該扣抵是否已經扣抵完畢
        for (DeductHistory deductHistory : deductHistoryList) {
            usableAmount = usableAmount.add(deductHistory.getAmount());
        }
        if (usableAmount.compareTo(BigDecimal.ZERO) > 0) {
            result = Optional.of(deduct);
        } else {
            deduct.setDeductStatus(DeductStatus.D);
            deductRepository.save(deduct);
        }
        return result;
    }

}
