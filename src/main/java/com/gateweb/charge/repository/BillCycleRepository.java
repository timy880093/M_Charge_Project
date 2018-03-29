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
 * This class provides methods to populate DB Table of BillCycle
 */
 @Repository("billCycle")
public interface BillCycleRepository extends JpaRepository<BillCycleEntity, Integer>
	, QuerydslPredicateExecutor<BillCycleEntity> ,BillCycleRepositoryCustom {
	
    /**
     * Delete a record in Database.
	 * @param id PK
	 * @throws DaoSystemException	if system is wrong.
     */
	void deleteByBillId(	
		java.lang.Integer billId 
	) throws DaoSystemException;

	public BillCycleEntity findByBillId(Long billId);
	
	/*public Page<BillCycleEntity> findByBillId(Long billId, Pageable pageable);
	
	public boolean exists(Long billId);
	
	public List<BillCycleEntity> findAll();
	
	public List<BillCycleEntity> findTop100ByBillId(Long billId);
	
	public long count();*/

	public List<BillCycleEntity> findAll();

	public List<BillCycleEntity> findByYearMonthIsAndCompanyIdIs(String yearMonth,Integer companyId);

	public List<BillCycleEntity> findByCashOutOverId(Integer cashOutOverId);
			
}

	