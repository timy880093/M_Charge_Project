/*
 * $Header: $
 * This java source file is generated by pkliu on Mon Jan 29 11:19:52 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.repository;

import com.gateweb.charge.model.InvoiceAmountSummaryReportEntity;
import com.gateweb.db.dao.exception.DaoSystemException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of InvoiceAmountSummaryReport
 */
 @Repository("chargeInvoiceAmountSummaryReportRepository")
public interface InvoiceAmountSummaryReportRepository extends JpaRepository<InvoiceAmountSummaryReportEntity, Long>
	, QuerydslPredicateExecutor<InvoiceAmountSummaryReportEntity> {
	
    /**
     * Delete a record in Database.
	 * @param id PK
	 * @throws DaoSystemException    if system is wrong.
     */
	void deleteById(
			Long id
	) throws DaoSystemException;

	List<InvoiceAmountSummaryReportEntity> findAll();

	List<InvoiceAmountSummaryReportEntity> findBySellerIsAndCreateDateGreaterThanAndCreateDateLessThan(String sellerIdentifier, Date fromModifyDate, Date toModifyDate);

	InvoiceAmountSummaryReportEntity findTop1ByOrderByModifyDateDesc();
	
}

	