package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.ChargePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChargePackageRepository extends JpaRepository<ChargePackage, Long> {

    @Query(value = " SELECT pr.from_package_id " +
            " FROM package_ref pr " +
            " JOIN charge_rule cr ON (cr.charge_rule_id = pr.to_charge_rule_id) " +
            " WHERE pr.from_package_id = :packageId and cr.charge_by_remaining_count = true limit 1 ", nativeQuery = true)
    Optional<Long> findChargeByRemainingCountFlagByPackageId(@Param("packageId") Long packageId);
}
