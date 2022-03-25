/*
 * $Header: $
 * This java source file is generated by pkliu on Mon Jan 29 11:19:52 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.ChargeIasrEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author pkliu
 * <p>
 * This class provides methods to populate DB Table of InvoiceAmountSummaryReport
 */
@Repository
public interface ChargeIasrRepository extends JpaRepository<ChargeIasrEntity, Long> {

    Optional<ChargeIasrEntity> findById(Long id);

    List<ChargeIasrEntity> findBySeller(String seller);

    List<ChargeIasrEntity> findAll();

    List<ChargeIasrEntity> findBySellerIsAndCreateDateGreaterThanAndCreateDateLessThan(String sellerIdentifier, Date fromModifyDate, Date toModifyDate);

    List<ChargeIasrEntity> findBySellerIsAndInvoiceDateGreaterThanEqualAndInvoiceDateLessThanEqual(String sellerIdentifier, String fromModifyDate, String toModifyDate);

    ChargeIasrEntity findTop1ByCreateDateIsNotNullAndModifyDateIsNotNullOrderByModifyDateDesc();

    @Query(value = "SELECT max(id) FROM invoice_amount_summary_report", nativeQuery = true)
    long findMaximumId();

    @Query(value = "SELECT min(id) FROM invoice_amount_summary_report", nativeQuery = true)
    long findMinimumId();

    @Query(value = "SELECT count(id) FROM invoice_amount_summary_report", nativeQuery = true)
    long findCountId();

    @Transactional
    @Modifying
    @Query(value = "delete from invoice_amount_summary_report where invoice_date like :invoiceDate and seller = :seller", nativeQuery = true)
    void deleteByInvoiceDateLikeAndSellerIs(@Param("invoiceDate") String invoiceDate, @Param("seller") String seller);

}

	