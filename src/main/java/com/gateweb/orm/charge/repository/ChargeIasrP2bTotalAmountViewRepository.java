package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.view.ChargeIasrP2bTotalAmountView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChargeIasrP2bTotalAmountViewRepository extends JpaRepository<ChargeIasrP2bTotalAmountView, Long> {
    @Query(nativeQuery = true)
    List<ChargeIasrP2bTotalAmountView> findInvoiceAmountForP2b(
            @Param("sellerTaxNumber") String seller
            , @Param("startDate") String startDate
            , @Param("endDate") String endDate
    );

}
