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
 * This class provides methods to populate DB Table of Company
 */
 @Repository("company")
public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer>
	, QuerydslPredicateExecutor<CompanyEntity> ,CompanyRepositoryCustom {
	
    /**
     * Delete a record in Database.
	 * @param $pkVar.columnName   PK 
	 * @throws DaoSystemException	if system is wrong.
     */
	void deleteByCompanyId(	
		java.lang.Integer companyId 
	) throws DaoSystemException;

	/*public CompanyEntity findByCompanyId(Long companyId);
	
	public Page<CompanyEntity> findByCompanyId(Long companyId, Pageable pageable);
	
	public boolean exists(Long companyId);
	
	public List<CompanyEntity> findAll();
	
	public List<CompanyEntity> findTop100ByCompanyId(Long companyId);
	
	public long count();*/

			
}

	