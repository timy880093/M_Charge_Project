package com.gateweb.charge.chargePolicy.calculateRule.type;

import com.gateweb.orm.charge.entity.NewGrade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class FeeCalculateUtils {

    /**
     * 只尋找符合的項目
     * 若該項目低於原始項目回傳Empty
     * 若該項目高於最高項目則回傳最高項目
     *
     * @param count
     * @return
     */
    public Optional<NewGrade> getLevel(int count, LinkedList<NewGrade> gradeLinkedList, int index) {
        Optional<NewGrade> gradeTableOptional = Optional.empty();
        NewGrade currentGrade = gradeLinkedList.get(index);
        int chargeableCount = sequenceOverlapCount(count, currentGrade.getCntStart(), currentGrade.getCntEnd());
        if (chargeableCount > 0 && count <= currentGrade.getCntEnd()) {
            gradeTableOptional = Optional.of(currentGrade);
        } else if (count > currentGrade.getCntEnd()) {
            if (gradeLinkedList.indexOf(currentGrade) != gradeLinkedList.size() - 1) {
                return getLevel(count, gradeLinkedList, index + 1);
            } else {
                return Optional.of(currentGrade);
            }
        }
        return gradeTableOptional;
    }

    public int getChargeableCount(int count, NewGrade newGrade) {
        if (count > 0) {
            return sequenceOverlapCount(count, newGrade.getCntStart(), newGrade.getCntEnd());
        } else {
            return count;
        }
    }

    public int getAccumulateChargeableCount(int count, LinkedList<NewGrade> gradeLinkedList) {
        Integer result = 0;
        Optional<NewGrade> currentLevelOptional = getLevel(count, gradeLinkedList, 0);
        if (currentLevelOptional.isPresent()) {
            int currentLevelIndex = gradeLinkedList.indexOf(currentLevelOptional.get());
            result = getAccumulateChargeableCount(count, gradeLinkedList, currentLevelIndex);
        }
        return result;
    }


    public int getAccumulateChargeableCount(int count, LinkedList<NewGrade> gradeLinkedList, int index) {
        int chargeableCount = 0;
        NewGrade currentLevel = gradeLinkedList.get(index);
        chargeableCount = getChargeableCount(count, currentLevel);
        if (index != 0) {
            chargeableCount += getAccumulateChargeableCount(count, gradeLinkedList, index - 1);
        }
        return chargeableCount;
    }

    protected BigDecimal calculateFee(int count, LinkedList<NewGrade> gradeLinkedList, int index) {
        NewGrade currentNewGrade = gradeLinkedList.get(index);
        if (currentNewGrade.getFixPrice() != null) {
            return calculateFixPrice(count, gradeLinkedList, index);
        } else if (currentNewGrade.getUnitPrice() != null) {
            return calculateUnitPrice(count, gradeLinkedList, index);
        }
        return BigDecimal.ZERO;
    }

    protected BigDecimal accumulateCalculateFee(int count, LinkedList<NewGrade> gradeLinkedList, int index) {
        BigDecimal fee;
        NewGrade currentLevel = gradeLinkedList.get(index);
        fee = calculateFee(count, gradeLinkedList, index);
        int currentLevelIndex = gradeLinkedList.indexOf(currentLevel);
        if (currentLevelIndex != 0) {
            fee = fee.add(accumulateCalculateFee(count, gradeLinkedList, index - 1));
        }
        return fee;
    }

    private BigDecimal calculateUnitPrice(int count, LinkedList<NewGrade> gradeLinkedList, int index) {
        NewGrade currentNewGrade = gradeLinkedList.get(index);
        int chargeableCount = sequenceOverlapCount(
                count
                , currentNewGrade.getCntStart()
                , currentNewGrade.getCntEnd()
        );
        if (chargeableCount > 0) {
            return gradeLinkedList.get(index).getUnitPrice().multiply(new BigDecimal(chargeableCount));
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal calculateFixPrice(int count, LinkedList<NewGrade> gradeLinkedList, int index) {
        NewGrade currentNewGrade = gradeLinkedList.get(index);
        if (currentNewGrade.getCntStart() > 0) {
            int chargeableCount = sequenceOverlapCount(count, currentNewGrade.getCntStart(), currentNewGrade.getCntEnd());
            if (chargeableCount > 0) {
                return currentNewGrade.getFixPrice();
            }
        } else if (currentNewGrade.getCntStart() == 0) {
            return currentNewGrade.getFixPrice();
        }
        return BigDecimal.ZERO;
    }

    protected int sequenceOverlapCount(int count, int from, int to) {
        List<Integer> overlapSequenceList = findOverlapSequence(count, from, to);
        Optional<Integer> maximumOptional = overlapSequenceList.stream().max(Integer::compareTo);
        Optional<Integer> minimumOptional = overlapSequenceList.stream().min((o1, o2) -> o2.compareTo(o1) * -1);
        if (maximumOptional.isPresent() && minimumOptional.isPresent()) {
            //從sequence轉為count
            return maximumOptional.get() - (minimumOptional.get() - 1);
        } else {
            return 0;
        }
    }

    /**
     * ex1:開立89張，級距1~100，計入89張
     *
     * @param count
     * @param from
     * @param to
     * @return
     */
    protected List<Integer> findOverlapSequence(int count, int from, int to) {
        List<Integer> overlapIntegers = new ArrayList<>();
        //令A為sequence1
        int a1 = from;
        int a2 = to;
        //令B為sequence2
        int b1 = 0;
        int b2 = 0;
        //兩種情況，0以及0以上
        if (count > 0) {
            b1 = 1;
            b2 = count;
        }

        if (a1 >= b1 && a1 <= b2) {
            overlapIntegers.add(a1);
        }
        if (a2 >= b1 && a2 <= b2) {
            overlapIntegers.add(a2);
        }
        if (b1 >= a1 && b1 <= a2) {
            overlapIntegers.add(b1);
        }
        if (b2 >= a1 && b2 <= a2) {
            overlapIntegers.add(b2);
        }
        return overlapIntegers;
    }
}
