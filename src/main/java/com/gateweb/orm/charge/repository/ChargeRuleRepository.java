package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.ChargeRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface ChargeRuleRepository extends JpaRepository<ChargeRule, Long>
        , QuerydslPredicateExecutor<ChargeRule> {

    Optional<ChargeRule> findByChargeRuleId(Long ruleId);

    @Query(
            value = " select cr.* from contract c " +
                    " join package_ref pr on (pr.from_package_id=c.package_id) " +
                    " join charge_rule cr on (pr.to_charge_rule_id= cr.charge_rule_id) " +
                    " where cr.charge_by_remaining_count  = true " +
                    " and c.contract_id = :contractId " +
                    " limit 1",
            nativeQuery = true
    )
    Optional<ChargeRule> getChargeRuleByContractIdAndRemainingCountIsTrue(@Param("contractId") Long contractId);

    @Query(
            value = " select cr.* from package_ref pr " +
                    " join charge_rule cr on (pr.to_charge_rule_id= cr.charge_rule_id) " +
                    " where cr.charge_by_remaining_count = true " +
                    " and pr.from_package_id = :packageId " +
                    " limit 1",
            nativeQuery = true
    )
    Optional<ChargeRule> getChargeRuleByPackageIdAndRemainingCountIsTrue(@Param("packageId") Long packageId);

    @Query(
            value = " select cr.* from charge_rule cr " +
                    " join package_ref pr on (pr.to_charge_rule_id= cr.charge_rule_id) " +
                    " where pr.from_package_id = :packageId" +
                    " and cr.paid_plan = :paidPlan limit 1",
            nativeQuery = true
    )
    Optional<ChargeRule> findChargeRuleByPackageIdAndPaidPlan(
            @Param("packageId") Long packageId
            , @Param("paidPlan") String paidPlan
    );

    @Query(
            value = " select cr.* from charge_rule cr " +
                    " join package_ref pr on (pr.to_charge_rule_id= cr.charge_rule_id) " +
                    " where pr.from_package_id = :packageId" +
                    " and cr.paid_plan = :paidPlan " +
                    " and cr.charge_plan = :chargePlan " +
                    " limit 1 ",
            nativeQuery = true
    )
    Optional<ChargeRule> findChargeRuleByPackageIdAndPaidPlanAndChargePlan(
            @Param("packageId") Long packageId
            , @Param("paidPlan") String paidPlan
            , @Param("chargePlan") String chargePlan
    );
}
