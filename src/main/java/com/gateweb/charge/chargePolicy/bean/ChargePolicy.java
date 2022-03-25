package com.gateweb.charge.chargePolicy.bean;

import com.gateweb.charge.chargePolicy.calculateRule.type.CalculateRule;
import com.gateweb.charge.chargePolicy.cycle.builder.ChargeCycle;
import com.gateweb.charge.enumeration.ChargeBaseType;
import com.gateweb.orm.charge.entity.NewGrade;
import com.gateweb.orm.charge.entity.PackageRef;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Optional;

public class ChargePolicy {
    LinkedList<NewGrade> gradeTableLinkedList;
    ChargeCycle calculateCycle;
    ChargeCycle chargeCycle;
    CalculateRule calculateRule;
    ChargeBaseType chargeBaseType;
    PackageRef packageRef;
    Long productCategoryId;
    boolean chargeByRemainingCount;
    Optional<BigDecimal> maximumCharge;

    public LinkedList<NewGrade> getGradeTableLinkedList() {
        return gradeTableLinkedList;
    }

    public void setGradeTableLinkedList(LinkedList<NewGrade> gradeTableLinkedList) {
        this.gradeTableLinkedList = gradeTableLinkedList;
    }

    public ChargeCycle getCalculateCycle() {
        return calculateCycle;
    }

    public void setCalculateCycle(ChargeCycle calculateCycle) {
        this.calculateCycle = calculateCycle;
    }

    public ChargeCycle getChargeCycle() {
        return chargeCycle;
    }

    public void setChargeCycle(ChargeCycle chargeCycle) {
        this.chargeCycle = chargeCycle;
    }

    public CalculateRule getCalculateRule() {
        return calculateRule;
    }

    public void setCalculateRule(CalculateRule calculateRule) {
        this.calculateRule = calculateRule;
    }

    public ChargeBaseType getChargeBaseType() {
        return chargeBaseType;
    }

    public void setChargeBaseType(ChargeBaseType chargeBaseType) {
        this.chargeBaseType = chargeBaseType;
    }

    public Optional<BigDecimal> getMaximumCharge() {
        return maximumCharge;
    }

    public void setMaximumCharge(Optional<BigDecimal> maximumCharge) {
        this.maximumCharge = maximumCharge;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public boolean isChargeByRemainingCount() {
        return chargeByRemainingCount;
    }

    public void setChargeByRemainingCount(boolean chargeByRemainingCount) {
        this.chargeByRemainingCount = chargeByRemainingCount;
    }

    public PackageRef getPackageRef() {
        return packageRef;
    }

    public void setPackageRef(PackageRef packageRef) {
        this.packageRef = packageRef;
    }

    @Override
    public String toString() {
        return "ChargePolicy{" +
                "gradeTableLinkedList=" + gradeTableLinkedList +
                ", calculateCycle=" + calculateCycle +
                ", chargeCycle=" + chargeCycle +
                ", calculateRule=" + calculateRule +
                ", chargeBaseType=" + chargeBaseType +
                ", packageRef=" + packageRef +
                ", productCategoryId=" + productCategoryId +
                ", chargeByRemainingCount=" + chargeByRemainingCount +
                ", maximumCharge=" + maximumCharge +
                '}';
    }
}
