package com.gateweb.charge.chargePolicy.calculateRule.type;

import com.gateweb.orm.charge.entity.NewGrade;

import java.math.BigDecimal;
import java.util.LinkedList;

public interface CalculateRule {

    BigDecimal calculateFee(int count, LinkedList<NewGrade> gradeLinkedList);

    Integer chargeableCount(int count, LinkedList<NewGrade> gradeLinkedList);
}
