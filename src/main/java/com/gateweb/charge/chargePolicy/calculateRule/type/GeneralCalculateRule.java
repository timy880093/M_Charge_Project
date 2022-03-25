package com.gateweb.charge.chargePolicy.calculateRule.type;

import com.gateweb.orm.charge.entity.NewGrade;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Optional;

public class GeneralCalculateRule implements CalculateRule {
    FeeCalculateUtils feeCalculateUtils = new FeeCalculateUtils();

    @Override
    public BigDecimal calculateFee(int count, LinkedList<NewGrade> gradeLinkedList) {
        //取得level
        Optional<NewGrade> currentLevel = feeCalculateUtils.getLevel(count, gradeLinkedList, 0);
        if (currentLevel.isPresent()) {
            int currentIndex = gradeLinkedList.indexOf(currentLevel.get());
            return feeCalculateUtils.calculateFee(count, gradeLinkedList, currentIndex);
        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public Integer chargeableCount(int count, LinkedList<NewGrade> gradeLinkedList) {
        //取得level
        Optional<NewGrade> currentLevel = feeCalculateUtils.getLevel(count, gradeLinkedList, 0);
        if (currentLevel.isPresent()) {
            return feeCalculateUtils.sequenceOverlapCount(
                    count
                    , currentLevel.get().getCntStart()
                    , currentLevel.get().getCntEnd()
            );
        } else {
            return 0;
        }
    }
}
