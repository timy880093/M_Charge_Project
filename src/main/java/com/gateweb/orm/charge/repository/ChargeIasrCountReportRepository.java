package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.ChargeIasrCountReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChargeIasrCountReportRepository extends JpaRepository<ChargeIasrCountReport, Long> {
    @Query(nativeQuery = true)
    Set<ChargeIasrCountReport> findChargeIasrCountReport(
            @Param("invoiceDate") String invoiceDate
    );

    @Query(nativeQuery = true)
    Set<ChargeIasrCountReport> findChargeIasrCountReportWithSeller(
            @Param("invoiceDate") String invoiceDate
            , @Param("seller") String seller
    );
}
