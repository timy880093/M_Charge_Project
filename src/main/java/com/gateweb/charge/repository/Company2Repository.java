/*
 * $Header: $
 * This java source file is generated by pkliu on Thu Aug 03 11:22:57 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.repository; 
    
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.gateweb.charge.model.Company;
import com.meshinnovation.db.dao.exception.DaoSystemException;

/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of Company
 */
@Repository("company2Repository")
public interface Company2Repository extends JpaRepository<com.gateweb.charge.model.Company, Long>
	, QueryDslPredicateExecutor<com.gateweb.charge.model.Company>, Company2RepositoryCustom {
	
    /**
     * Delete a record in Database.
	 * @param $pkVar.columnName   PK 
	 * @throws DaoSystemException	if system is wrong.
     */
	void deleteBycompanyId(	
		java.lang.Long companyId 
	) throws DaoSystemException;

	public Company findByCompanyId(Long companyId);
	
	public Page<Company> findByCompanyId(Long companyId, Pageable pageable);
	
	public boolean exists(Long companyId);
	
	public List<Company> findAll();
	
	public List<Company> findTop100ByCompanyId(Long companyId);
	
	public long count();

			
}

	