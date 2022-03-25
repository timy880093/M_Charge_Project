package com.gateweb.charge.contract.beanValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * https://www.baeldung.com/javax-validation-method-constraints
 */
@Constraint(validatedBy = {ContractPeriodMonthValidator.class})
@Target({PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface ValidContractPeriodMonth {
    String message() default
            "合約月份應為正整數";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
