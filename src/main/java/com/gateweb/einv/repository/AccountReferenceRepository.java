/*
 * $Header: $
 * This java source file is generated by pkliu on Thu Aug 03 11:22:56 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.einv.repository; 
    
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.gateweb.einv.model.AccountReference;
import com.meshinnovation.db.dao.exception.DaoSystemException;

/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of AccountReference
 */
 @Repository("accountReferenceRepository")
public interface AccountReferenceRepository extends JpaRepository<AccountReference, Long>
	, QueryDslPredicateExecutor<AccountReference> ,AccountReferenceRepositoryCustom {
	
    /**
     * Delete a record in Database.
	 * @param $pkVar.columnName   PK 
	 * @throws DaoSystemException	if system is wrong.
     */
	void deleteByReferenceId(	
		java.lang.Long referenceId 
	) throws DaoSystemException;

	public AccountReference findByReferenceId(Long aeferenceId);
	
	public Page<AccountReference> findByReferenceId(Long aeferenceId, Pageable pageable);
	
	public boolean exists(Long aeferenceId);
	
	public List<AccountReference> findAll();
	
	public List<AccountReference> findTop100ByReferenceId(Long aeferenceId);
	
	public long count();

			
}

	