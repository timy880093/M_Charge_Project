package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.PackageRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRefRepository extends JpaRepository<PackageRef, Long>
        , QuerydslPredicateExecutor<PackageRef> {

    List<PackageRef> findByFromPackageId(Long packageId);

    List<PackageRef> findByFromPackageIdIs(Long packageId);

    Optional<PackageRef> findByFromPackageIdIsAndToProductIdIs(Long packageId, Long productId);

    List<PackageRef> findByToChargeRuleId(Long toChargeRuleId);

    Optional<PackageRef> findByFromPackageIdAndPackageRefId(Long packageId, Long packageRefId);

    Optional<PackageRef> findByFromPackageIdAndToChargeRuleId(Long fromPackageId, Long toChargeRuleId);
}

