package com.gateweb.charge.chargePolicy.calculateRule.type;

import com.gateweb.orm.charge.entity.NewGrade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

public class ACMixTypeCalculateRule implements CalculateRule {
    FeeCalculateUtils feeCalculateUtils = new FeeCalculateUtils();

    @Override
    public BigDecimal calculateFee(int count, LinkedList<NewGrade> gradeLinkedList) {
        //只有一級不用找
        BigDecimal level = new BigDecimal(count / gradeLinkedList.get(0).getCntEnd() + 1).setScale(0, RoundingMode.DOWN);
        BigDecimal price = feeCalculateUtils.calculateFee(count, gradeLinkedList, 0);
        price = price.multiply(level);
        return price;
    }

    @Override
    public Integer chargeableCount(int count, LinkedList<NewGrade> gradeLinkedList) {
        return feeCalculateUtils.getAccumulateChargeableCount(count, gradeLinkedList);
    }
}
