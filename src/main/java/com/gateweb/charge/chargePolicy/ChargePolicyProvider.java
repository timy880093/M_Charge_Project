package com.gateweb.charge.chargePolicy;

import com.gateweb.charge.chargePolicy.bean.ChargePolicy;
import com.gateweb.charge.chargePolicy.calculateRule.component.CalculateRuleProvider;
import com.gateweb.charge.chargePolicy.calculateRule.type.ACMixTypeCalculateRule;
import com.gateweb.charge.chargePolicy.calculateRule.type.ATypeCalculateRule;
import com.gateweb.charge.chargePolicy.calculateRule.type.CalculateRule;
import com.gateweb.charge.chargePolicy.cycle.ChargeCycleInstanceProvider;
import com.gateweb.charge.chargePolicy.cycle.builder.ChargeCycle;
import com.gateweb.charge.chargePolicy.cycle.service.CycleService;
import com.gateweb.charge.chargePolicy.grade.service.GradeService;
import com.gateweb.charge.contract.component.ContractPrepayTypeComponent;
import com.gateweb.charge.enumeration.ChargePlan;
import com.gateweb.charge.enumeration.ChargePolicyType;
import com.gateweb.charge.enumeration.ContractPrepayType;
import com.gateweb.charge.enumeration.PaidPlan;
import com.gateweb.orm.charge.entity.ChargeRule;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.NewGrade;
import com.gateweb.orm.charge.entity.PackageRef;
import com.gateweb.orm.charge.repository.ChargeRuleRepository;
import com.gateweb.orm.charge.repository.GradeRepository;
import com.gateweb.orm.charge.repository.PackageRefRepository;
import com.gateweb.orm.charge.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;

/**
 * 理論上所有的計費方式都包含Grade及Cycle
 * contractId+chargeRuleID才會是唯一值，ChargeRule本身可以被重覆使用。
 */
@Component
public class ChargePolicyProvider {
    final ChargeCycleInstanceProvider chargeCycleInstanceProvider = new ChargeCycleInstanceProvider();
    @Autowired
    ChargeRuleRepository chargeRuleRepository;
    @Autowired
    PackageRefRepository modeReferenceRepository;
    @Autowired
    CycleService cycleService;
    @Autowired
    GradeRepository gradeRepository;
    @Autowired
    GradeService gradeService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CalculateRuleProvider calculateRuleProvider;
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    ContractPrepayTypeComponent contractPrepayTypeComponent;

    public Optional<ChargePolicyType> getChargePolicyType(ChargeRule chargeRule) {
        if (chargeRule.getChargePlan().equals(ChargePlan.PERIODIC)
                && chargeRule.getPaidPlan().equals(PaidPlan.PRE_PAID)) {
            return Optional.of(ChargePolicyType.RENTAL);
        } else if (chargeRule.getChargePlan().equals(ChargePlan.PERIODIC)
                && chargeRule.getPaidPlan().equals(PaidPlan.POST_PAID)) {
            return Optional.of(ChargePolicyType.OVERAGE);
        }
        return Optional.empty();
    }

    public Optional<ChargePolicy> genChargePolicy(ChargePolicyType chargePolicyType, Contract contract) {
        ContractPrepayType contractPrepayType = contractPrepayTypeComponent.getContractPrepayType(contract);
        if (chargePolicyType.equals(ChargePolicyType.RENTAL)) {
            Optional<ChargeRule> rentalChargeRuleOpt = chargeRuleRepository.findChargeRuleByPackageIdAndPaidPlan(
                    contract.getPackageId(), PaidPlan.PRE_PAID.name()
            );
            if (rentalChargeRuleOpt.isPresent()) {
                return genRentalChargePolicy(
                        contract.getPackageId()
                        , rentalChargeRuleOpt.get()
                        , contract.getPeriodMonth()
                );
            }
        }
        if (chargePolicyType.equals(ChargePolicyType.OVERAGE)
                && contractPrepayType.equals(ContractPrepayType.PREPAY_BY_MONTH)) {
            Optional<ChargeRule> overageChargeRuleOpt = chargeRuleRepository.findChargeRuleByPackageIdAndPaidPlanAndChargePlan(
                    contract.getPackageId(), PaidPlan.POST_PAID.name(), ChargePlan.PERIODIC.name()
            );
            if (overageChargeRuleOpt.isPresent()) {
                return genOverageChargePolicy(contract.getPackageId(), overageChargeRuleOpt.get());
            }
        }
        return Optional.empty();
    }

    public Optional<ChargePolicy> genOverageChargePolicy(Long packageId, ChargeRule chargeRule) {
        Optional<ChargePolicy> chargePolicyOpt = Optional.empty();
        Optional<ChargeCycle> chargeCycleOptional = chargeCycleInstanceProvider.genGeneralChargeCycleInstance(
                chargeRule.getChargeCycleType()
        );
        Optional<ChargeCycle> calculateCycleOptional = chargeCycleInstanceProvider.genGeneralChargeCycleInstance(
                chargeRule.getCalculateCycleType()
        );
        Optional<PackageRef> packageRefOptional = packageRefRepository.findByFromPackageIdAndToChargeRuleId(
                packageId
                , chargeRule.getChargeRuleId()
        );
        LinkedList<NewGrade> gradeLinkedList = gradeService.getLinkedNewGrade(chargeRule.getGradeId());
        Optional<CalculateRule> calculateRuleOptional = calculateRuleProvider.getCalculateRule(chargeRule);
        if (calculateCycleOptional.isPresent()
                && calculateRuleOptional.isPresent()
                && chargeCycleOptional.isPresent()
                && packageRefOptional.isPresent()
                && !gradeLinkedList.isEmpty()) {
            ChargePolicy chargePolicy = new ChargePolicy();
            chargePolicy.setGradeTableLinkedList(gradeLinkedList);
            chargePolicy.setCalculateCycle(calculateCycleOptional.get());
            chargePolicy.setChargeCycle(chargeCycleOptional.get());
            chargePolicy.setChargeBaseType(chargeRule.getChargeBaseType());
            chargePolicy.setCalculateRule(getCalculateRule(chargeRule));
            chargePolicy.setMaximumCharge(Optional.ofNullable(chargeRule.getMaximumCharge()));
            chargePolicy.setProductCategoryId(chargeRule.getProductCategoryId());
            chargePolicy.setChargeByRemainingCount(chargeRule.getChargeByRemainingCount());
            chargePolicy.setCalculateRule(calculateRuleOptional.get());
            chargePolicy.setPackageRef(packageRefOptional.get());
            chargePolicyOpt = Optional.of(chargePolicy);
        }
        return chargePolicyOpt;
    }

    public Optional<ChargePolicy> genRentalChargePolicy(Long packageId, ChargeRule chargeRule, Integer calCycleLimitPartition) {
        Optional<ChargePolicy> chargePolicyOpt = Optional.empty();
        Optional<ChargeCycle> chargeCycleOptional = chargeCycleInstanceProvider.genGeneralChargeCycleInstance(
                chargeRule.getChargeCycleType()
        );
        Optional<ChargeCycle> calculateCycleOptional = chargeCycleInstanceProvider.genRentalChargeCycleInstance(
                chargeRule.getCalculateCycleType()
                , calCycleLimitPartition
        );
        Optional<PackageRef> packageRefOptional = packageRefRepository.findByFromPackageIdAndToChargeRuleId(
                packageId
                , chargeRule.getChargeRuleId()
        );
        LinkedList<NewGrade> gradeLinkedList = gradeService.getLinkedNewGrade(chargeRule.getGradeId());
        Optional<CalculateRule> calculateRuleOptional = calculateRuleProvider.getCalculateRule(chargeRule);
        if (calculateCycleOptional.isPresent()
                && calculateRuleOptional.isPresent()
                && chargeCycleOptional.isPresent()
                && packageRefOptional.isPresent()
                && !gradeLinkedList.isEmpty()) {
            ChargePolicy chargePolicy = new ChargePolicy();
            chargePolicy.setGradeTableLinkedList(gradeLinkedList);
            chargePolicy.setCalculateCycle(calculateCycleOptional.get());
            chargePolicy.setChargeCycle(chargeCycleOptional.get());
            chargePolicy.setChargeBaseType(chargeRule.getChargeBaseType());
            chargePolicy.setCalculateRule(getCalculateRule(chargeRule));
            chargePolicy.setMaximumCharge(Optional.ofNullable(chargeRule.getMaximumCharge()));
            chargePolicy.setProductCategoryId(chargeRule.getProductCategoryId());
            chargePolicy.setChargeByRemainingCount(chargeRule.getChargeByRemainingCount());
            chargePolicy.setCalculateRule(calculateRuleOptional.get());
            chargePolicy.setPackageRef(packageRefOptional.get());
            chargePolicyOpt = Optional.of(chargePolicy);
        }
        return chargePolicyOpt;
    }

    public CalculateRule getCalculateRule(ChargeRule chargeRule) {
        CalculateRule result = null;
        if (chargeRule.getAccumulation() && chargeRule.getCirculation()) {
            result = new ACMixTypeCalculateRule();
        } else if (chargeRule.getAccumulation() && !chargeRule.getCirculation()) {
            result = new ATypeCalculateRule();
        }
        return result;
    }
}
