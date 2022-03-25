package com.gateweb.charge.report.bean;

public class ChargeRuleServletView {
    Long chargeRuleId;
    String  chargeRuleName;
    Long productCategoryId;
    String productCategoryName;
    String paidPlanId;
    String paidPlanDesc;
    String chargePlan;
    String chargePlanDesc;
    String chargeBaseType;
    String chargeBaseTypeDesc;
    Long rootGradeId;
    String rootGradeName;
    String chargeCycleType;
    String chargeCycleTypeDesc;
    String calculateCycleType;
    String calculateCycleTypeDesc;
    Boolean chargeByRemainingCount;
    Boolean enabled;
    Boolean accumulation;
    Boolean circulation;

    public ChargeRuleServletView(Long chargeRuleId, String chargeRuleName, Long productCategoryId, String productCategoryName, String paidPlanId, String paidPlanDesc, String chargePlan, String chargePlanDesc, String chargeBaseType, String chargeBaseTypeDesc, Long rootGradeId, String rootGradeName, String chargeCycleType, String chargeCycleTypeDesc, String calculateCycleType, String calculateCycleTypeDesc, Boolean chargeByRemainingCount, Boolean enabled, Boolean accumulation, Boolean circulation) {
        this.chargeRuleId = chargeRuleId;
        this.chargeRuleName = chargeRuleName;
        this.productCategoryId = productCategoryId;
        this.productCategoryName = productCategoryName;
        this.paidPlanId = paidPlanId;
        this.paidPlanDesc = paidPlanDesc;
        this.chargePlan = chargePlan;
        this.chargePlanDesc = chargePlanDesc;
        this.chargeBaseType = chargeBaseType;
        this.chargeBaseTypeDesc = chargeBaseTypeDesc;
        this.rootGradeId = rootGradeId;
        this.rootGradeName = rootGradeName;
        this.chargeCycleType = chargeCycleType;
        this.chargeCycleTypeDesc = chargeCycleTypeDesc;
        this.calculateCycleType = calculateCycleType;
        this.calculateCycleTypeDesc = calculateCycleTypeDesc;
        this.chargeByRemainingCount = chargeByRemainingCount;
        this.enabled = enabled;
        this.accumulation = accumulation;
        this.circulation = circulation;
    }

    public Long getChargeRuleId() {
        return chargeRuleId;
    }

    public void setChargeRuleId(Long chargeRuleId) {
        this.chargeRuleId = chargeRuleId;
    }

    public String getChargeRuleName() {
        return chargeRuleName;
    }

    public void setChargeRuleName(String chargeRuleName) {
        this.chargeRuleName = chargeRuleName;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public String getPaidPlanId() {
        return paidPlanId;
    }

    public void setPaidPlanId(String paidPlanId) {
        this.paidPlanId = paidPlanId;
    }

    public String getPaidPlanDesc() {
        return paidPlanDesc;
    }

    public void setPaidPlanDesc(String paidPlanDesc) {
        this.paidPlanDesc = paidPlanDesc;
    }

    public String getChargePlan() {
        return chargePlan;
    }

    public void setChargePlan(String chargePlan) {
        this.chargePlan = chargePlan;
    }

    public String getChargePlanDesc() {
        return chargePlanDesc;
    }

    public void setChargePlanDesc(String chargePlanDesc) {
        this.chargePlanDesc = chargePlanDesc;
    }

    public String getChargeBaseType() {
        return chargeBaseType;
    }

    public void setChargeBaseType(String chargeBaseType) {
        this.chargeBaseType = chargeBaseType;
    }

    public String getChargeBaseTypeDesc() {
        return chargeBaseTypeDesc;
    }

    public void setChargeBaseTypeDesc(String chargeBaseTypeDesc) {
        this.chargeBaseTypeDesc = chargeBaseTypeDesc;
    }

    public Long getRootGradeId() {
        return rootGradeId;
    }

    public void setRootGradeId(Long rootGradeId) {
        this.rootGradeId = rootGradeId;
    }

    public String getRootGradeName() {
        return rootGradeName;
    }

    public void setRootGradeName(String rootGradeName) {
        this.rootGradeName = rootGradeName;
    }

    public String getChargeCycleType() {
        return chargeCycleType;
    }

    public void setChargeCycleType(String chargeCycleType) {
        this.chargeCycleType = chargeCycleType;
    }

    public String getChargeCycleTypeDesc() {
        return chargeCycleTypeDesc;
    }

    public void setChargeCycleTypeDesc(String chargeCycleTypeDesc) {
        this.chargeCycleTypeDesc = chargeCycleTypeDesc;
    }

    public String getCalculateCycleType() {
        return calculateCycleType;
    }

    public void setCalculateCycleType(String calculateCycleType) {
        this.calculateCycleType = calculateCycleType;
    }

    public String getCalculateCycleTypeDesc() {
        return calculateCycleTypeDesc;
    }

    public void setCalculateCycleTypeDesc(String calculateCycleTypeDesc) {
        this.calculateCycleTypeDesc = calculateCycleTypeDesc;
    }

    public Boolean getChargeByRemainingCount() {
        return chargeByRemainingCount;
    }

    public void setChargeByRemainingCount(Boolean chargeByRemainingCount) {
        this.chargeByRemainingCount = chargeByRemainingCount;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getAccumulation() {
        return accumulation;
    }

    public void setAccumulation(Boolean accumulation) {
        this.accumulation = accumulation;
    }

    public Boolean getCirculation() {
        return circulation;
    }

    public void setCirculation(Boolean circulation) {
        this.circulation = circulation;
    }

    @Override
    public String toString() {
        return "ChargeRuleServletView{" +
                "chargeRuleId=" + chargeRuleId +
                ", chargeRuleName='" + chargeRuleName + '\'' +
                ", productCategoryId=" + productCategoryId +
                ", productCategoryName='" + productCategoryName + '\'' +
                ", paidPlanId='" + paidPlanId + '\'' +
                ", paidPlanDesc='" + paidPlanDesc + '\'' +
                ", chargePlan='" + chargePlan + '\'' +
                ", chargePlanDesc='" + chargePlanDesc + '\'' +
                ", chargeBaseType='" + chargeBaseType + '\'' +
                ", chargeBaseTypeDesc='" + chargeBaseTypeDesc + '\'' +
                ", rootGradeId=" + rootGradeId +
                ", rootGradeName='" + rootGradeName + '\'' +
                ", chargeCycleType='" + chargeCycleType + '\'' +
                ", chargeCycleTypeDesc='" + chargeCycleTypeDesc + '\'' +
                ", calculateCycleType='" + calculateCycleType + '\'' +
                ", calculateCycleTypeDesc='" + calculateCycleTypeDesc + '\'' +
                ", chargeByRemainingCount=" + chargeByRemainingCount +
                ", enabled=" + enabled +
                ", accumulation=" + accumulation +
                ", circulation=" + circulation +
                '}';
    }
}
