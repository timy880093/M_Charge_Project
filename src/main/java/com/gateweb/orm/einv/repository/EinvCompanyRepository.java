/*
 * $Header: $
 * This java source file is generated by pkliu on Thu Aug 03 11:22:57 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.orm.einv.repository;

import com.gateweb.orm.einv.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author pkliu
 * <p>
 * This class provides methods to populate DB Table of Company
 */
@Repository
public interface EinvCompanyRepository extends JpaRepository<Company, Long>
        , QuerydslPredicateExecutor<Company> {

//	public Company findByCompanyId(Long companyId);
//	
//	public Page<Company> findByCompanyId(Long companyId, Pageable pageable);
//	
//	public boolean exists(Long companyId);
//	
//	public List<Company> findAll();
//	
//	public List<Company> findTop100ByCompanyId(Long companyId);
//	
//	public long count();

    public Company findByCompanyKeyEquals(String businessNo);

    List<Company> findByModifyDateAfter(Timestamp timestamp);

}

	