package com.gateweb.orm.charge.repository;

import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.orm.charge.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long>, PagingAndSortingRepository<Contract, Long>, QuerydslPredicateExecutor<Contract> {

    Optional<Contract> findByContractIdAndStatus(Long contractId, ContractStatus contractStatus);

    Collection<Contract> findByStatusIs(ContractStatus status);

    Collection<Contract> findByStatusIsAndEffectiveDateBefore(ContractStatus status, LocalDateTime localDateTime);

    Collection<Contract> findByCompanyIdAndStatusIsIn(Long companyId, List<ContractStatus> contractStatusList);

    Collection<Contract> findByCompanyIdIsAndEffectiveDateAfterAndStatusIsNot(Long companyId, LocalDateTime effectiveDate, ContractStatus status);

    @Query(value = "Select * from contract con where con.status in ('C','B','E') ", nativeQuery = true)
    List<Contract> findDeductibleContractList();

    Collection<Contract> findByStatusAndFirstInvoiceDateAsEffectiveDateIsTrue(ContractStatus contractStatus);

    Collection<Contract> findByStatusAndInstallationDateIsNotNull(ContractStatus contractStatus);

    Collection<Contract> findByStatusAndIsFirstContractIsTrue(ContractStatus contractStatus);

    @Query(value = "select count(contract_id) from contract " +
            "where company_id = :companyId " +
            "group by company_id", nativeQuery = true)
    int findCountByCompanyId(@Param("companyId") Long companyId);

    Collection<Contract> findByRemarkIsNotNull();

    Optional<Contract> findByRemarkIs(String remark);

    @Query(
            value = " select * from contract con " +
                    " where (con.effective_date, con.expiration_date) " +
                    " OVERLAPS (:targetFrom, :targetTo)" +
                    " and con.status = :status ",
            nativeQuery = true
    )
    Set<Contract> findByStatusAndIntervalOverlaps(
            @Param("targetFrom") LocalDateTime targetFrom
            , @Param("targetTo") LocalDateTime targetTo
            , @Param("status") String status
    );

    @Query(
            value = " select * from contract con " +
                    " where (con.effective_date, con.expiration_date) " +
                    " OVERLAPS (:targetFrom, :targetTo)" +
                    " and con.company_id = :companyId ",
            nativeQuery = true
    )
    Set<Contract> findByCompanyIdAndIntervalOverlaps(
            @Param("targetFrom") LocalDateTime targetFrom
            , @Param("targetTo") LocalDateTime targetTo
            , @Param("companyId") Long companyId
    );

    @Query(
            value = " select * from contract con " +
                    " where con.expiration_date between :targetFrom and :targetTo " +
                    " and con.status = :status " +
                    " and con.contract_id not in ( " +
                    "   SELECT DISTINCT contract_id " +
                    "   FROM contract con2 " +
                    "   JOIN package_ref pr ON (pr.from_package_id = con2.package_id) " +
                    "   JOIN charge_rule cr ON (cr.charge_rule_id = pr.to_charge_rule_id) " +
                    "   WHERE cr.charge_by_remaining_count = true " +
                    " );",
            nativeQuery = true
    )
    Set<Contract> findByStatusAndExpirationDateBetweenAndChargeByRemainingCountIsFalse(
            @Param("targetFrom") LocalDateTime targetFrom
            , @Param("targetTo") LocalDateTime targetTo
            , @Param("status") String status
    );

    Collection<Contract> findByPackageId(Long packageId);

    Collection<Contract> findByStatusAndExpirationDateIsNotNullAndExpirationDateAfter(ContractStatus status, LocalDateTime localDateTime);

    @Query(value = " SELECT cr.charge_by_remaining_count " +
            " FROM contract con " +
            " JOIN package_ref pr ON (con.package_id = pr.from_package_id) " +
            " JOIN charge_rule cr ON (cr.charge_rule_id = pr.to_charge_rule_id) " +
            " WHERE contract_id = :contractId ", nativeQuery = true)
    Boolean findChargeByRemainingCountFlagByContractId(@Param("contractId") Long contractId);

    Optional<Contract> findTopByCompanyIdAndContractIdBeforeOrderByContractIdDesc(
            @Param("companyId") Long companyId
            , @Param("contractId") Long contractId
    );
}

