package com.gateweb.charge.contract.beanValidation;


import com.gateweb.orm.charge.entity.Contract;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class ContractPeriodMonthValidator implements ConstraintValidator<ValidContractPeriodMonth, Contract> {
    @Override
    public boolean isValid(Contract contract, ConstraintValidatorContext constraintValidatorContext) {
        if (contract.getPeriodMonth() == null) {
            return false;
        }
        if (contract.getPeriodMonth() <= 0) {
            return false;
        }
        return true;
    }
}
