package com.gateweb.charge.deduct.component;

import com.gateweb.orm.charge.entity.DeductHistory;
import com.gateweb.orm.charge.repository.DeductHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
public class DeductibleAmountComponent {
    @Autowired
    DeductHistoryRepository deductHistoryRepository;

    /**
     * 找到所有的deductHistory
     *
     * @param deductId
     * @return
     */
    public BigDecimal getDeductibleAmount(Long deductId) {
        Set<DeductHistory> deductHistorySet = new HashSet<>(
                deductHistoryRepository.findByDeductId(deductId)
        );
        BigDecimal deductibleAmount = BigDecimal.ZERO;
        for (DeductHistory deductHistory : deductHistorySet) {
            deductibleAmount = deductibleAmount.add(deductHistory.getAmount());
        }
        return deductibleAmount;
    }

}
