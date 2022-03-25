package com.gateweb.orm.charge.repository;

import com.gateweb.charge.enumeration.DeductStatus;
import com.gateweb.orm.charge.entity.Deduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DeductRepository extends JpaRepository<Deduct, Long>, QuerydslPredicateExecutor<Deduct> {
    Optional<Deduct> findByDeductId(Long deductId);

    @Query(value = "SELECT ded.* " +
            "FROM deduct ded " +
            "JOIN billing_item bi ON (bi.deduct_id = ded.deduct_id) " +
            "JOIN bill b ON (b.bill_id = bi.bill_id) " +
            "JOIN deduct_history dh ON (dh.deduct_billing_item_id = bi.billing_item_id) " +
            "WHERE is_memo is false " +
            " and ded.contract_id = ? " +
            " and ded.package_ref_id = ? " +
            " AND b.bill_status = ? ", nativeQuery = true)
    Set<Deduct> findPrepaymentSetByContractIdAndPackageRefId(Long contractId, Long packageRefId, String billStatus);

    List<Deduct> findByCompanyIdAndDeductStatus(Long companyId, DeductStatus deductStatus);

    List<Deduct> findByCompanyIdAndPackageRefIdAndDeductStatus(Long companyId, Long packageRefId, DeductStatus deductStatus);

    List<Deduct> findByCompanyIdAndContractId(Long companyId, Long contractId);

    List<Deduct> findByCompanyIdAndContractIdAndDeductStatusIsNot(Long companyId, Long contractId, DeductStatus deductStatus);


}
