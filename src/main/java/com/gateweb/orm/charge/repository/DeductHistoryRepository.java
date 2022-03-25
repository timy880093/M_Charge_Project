package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.DeductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface DeductHistoryRepository extends JpaRepository<DeductHistory, Long> {

    @Query(value = "select * from deduct_history dh\n" +
            "join product_purchase pp on (dh.deduct_purchase_id = pp.product_purchase_id)\n" +
            "join product pro on (pro.product_id = pp.product_id)\n" +
            "join product_source ps on (ps.product_source_id=pro.product_source_id)\n" +
            "join deduct ded on(ded.deduct_id = ps.deduct_id)\n" +
            "where dh.contract_id = ?1 and dh.package_ref_id = ?2 \n", nativeQuery = true)
    List<DeductHistory> findUsableServiceDeduct(Long contractId, Long productReferenceId);

    List<DeductHistory> findByDeductId(Long deductId);

    Collection<DeductHistory> findByDeductBillingItemId(Long billingItemId);

    Collection<DeductHistory> findByDeductIdAndDeductBillingItemIdIsNotNull(Long deductId);


}
