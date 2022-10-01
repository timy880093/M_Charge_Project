package com.gateweb.charge.contract.remainingCount.component;

import com.gateweb.orm.charge.entity.ChargeRule;
import com.gateweb.orm.charge.entity.NewGrade;
import com.gateweb.orm.charge.repository.ChargeRuleRepository;
import com.gateweb.orm.charge.repository.NewGradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RemainingCountAmountProvider {
    @Autowired
    ChargeRuleRepository chargeRuleRepository;
    @Autowired
    NewGradeRepository newGradeRepository;

    public Optional<Integer> getRemainingCountFromPackageId(Long packageId) {
        Optional<ChargeRule> chargeRuleOptional
                = chargeRuleRepository.getChargeRuleByPackageIdAndRemainingCountIsTrue(packageId);
        if (chargeRuleOptional.isPresent()) {
            return getRemainingCountFromChargeRule(chargeRuleOptional.get());
        } else {
            return Optional.empty();
        }
    }

    public Optional<Integer> getRemainingCountFromChargeRule(ChargeRule chargeRule) {
        Optional<Integer> result = Optional.empty();
        Optional<NewGrade> newGradeOpt = newGradeRepository.findByGradeId(chargeRule.getGradeId());
        if (newGradeOpt.isPresent()) {
            result = Optional.of(newGradeOpt.get().getCntEnd());
        }
        return result;
    }

    public Optional<Integer> getRemainingCountFromContractId(Long contractId) {
        Optional<ChargeRule> chargeRuleOptional = chargeRuleRepository.getChargeRuleByContractIdAndRemainingCountIsTrue(contractId);
        if (chargeRuleOptional.isPresent()) {
            return getRemainingCountFromChargeRule(chargeRuleOptional.get());
        } else {
            return Optional.empty();
        }
    }
}
