package com.gateweb.charge.chargePolicy.calculateRule.type;

import com.gateweb.orm.charge.entity.NewGrade;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Optional;

public class ATypeCalculateRule implements CalculateRule {
    FeeCalculateUtils feeCalculateUtils = new FeeCalculateUtils();

    @Override
    public BigDecimal calculateFee(int count, LinkedList<NewGrade> gradeLinkedList) {
        BigDecimal price = BigDecimal.ZERO;
        //取得level
        Optional<NewGrade> currentLevelOpt = feeCalculateUtils.getLevel(count, gradeLinkedList, 0);
        if (currentLevelOpt.isPresent()) {
            int currentLevelIndex = gradeLinkedList.indexOf(currentLevelOpt.get());
            //得到該level的金額
            price = feeCalculateUtils.accumulateCalculateFee(count, gradeLinkedList, currentLevelIndex);
        }
        return price;
    }

    /**
     * 每一層的chargeable count
     *
     * @param count
     * @return
     */
    @Override
    public Integer chargeableCount(int count, LinkedList<NewGrade> gradeLinkedList) {
        return feeCalculateUtils.getAccumulateChargeableCount(count, gradeLinkedList);
    }


}
