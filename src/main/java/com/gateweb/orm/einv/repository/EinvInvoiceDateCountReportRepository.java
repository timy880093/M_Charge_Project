package com.gateweb.orm.einv.repository;

import com.gateweb.orm.einv.entity.report.EinvInvoiceDateCountReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface EinvInvoiceDateCountReportRepository extends JpaRepository<EinvInvoiceDateCountReport, Long> {

    @Query(nativeQuery = true)
    Set<EinvInvoiceDateCountReport> findEinvInvoiceDateCountReport(
            @Param("yearMonth") String yearMonth
            , @Param("invoiceDate") String invoiceDate
    );

    @Query(nativeQuery = true)
    Set<EinvInvoiceDateCountReport> findEinvInvoiceDateCountReportWithSeller(
            @Param("yearMonth") String yearMonth
            , @Param("invoiceDate") String invoiceDate
            , @Param("seller") String seller
    );
}
