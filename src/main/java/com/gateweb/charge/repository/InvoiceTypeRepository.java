/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:14 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.repository; 
    
import java.sql.SQLException;
import java.util.Collection;
import com.gateweb.charge.*;
import com.gateweb.charge.model.*;
import com.meshinnovation.db.dao.exception.DaoSystemException;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of InvoiceType
 */
 @Repository("invoiceType")
public interface InvoiceTypeRepository extends JpaRepository<InvoiceTypeEntity, String>
	, QuerydslPredicateExecutor<InvoiceTypeEntity> ,InvoiceTypeRepositoryCustom {
	
    /**
     * Delete a record in Database.
	 * @param $pkVar.columnName   PK 
	 * @throws DaoSystemException	if system is wrong.
     */
	void deleteByTypeCode(	
		java.lang.String typeCode 
	) throws DaoSystemException;

//	public InvoiceTypeEntity findByTypeCode(Long typeCode);
//	
//	public Page<InvoiceTypeEntity> findByTypeCode(Long typeCode, Pageable pageable);
//	
//	public boolean exists(Long typeCode);
//	
//	public List<InvoiceTypeEntity> findAll();
//	
//	public List<InvoiceTypeEntity> findTop100ByTypeCode(Long typeCode);
//	
//	public long count();

			
}

	