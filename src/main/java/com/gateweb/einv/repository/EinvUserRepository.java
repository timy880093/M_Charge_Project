/*
 * $Header: $
 * This java source file is generated by pkliu on Thu Aug 03 11:22:57 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.einv.repository; 
    
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.gateweb.einv.model.User;
import com.meshinnovation.db.dao.exception.DaoSystemException;

/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of User
 */
 @Repository
public interface EinvUserRepository extends JpaRepository<User, Long>
	, QuerydslPredicateExecutor<User> ,EinvUserRepositoryCustom {
	
    /**
     * Delete a record in Database.
	 * @param $pkVar.columnName   PK 
	 * @throws DaoSystemException	if system is wrong.
     */
	void deleteByUserId(	
		java.lang.Long userId 
	) throws DaoSystemException;

	public User findByUserId(Long userId);
//	
//	public Page<User> findByUserId(Long userId, Pageable pageable);
//	
//	public boolean exists(Long userId);
//	
//	public List<User> findAll();
//	
//	public List<User> findTop100ByUserId(Long userId);
//	
//	public long count();

			
}

	