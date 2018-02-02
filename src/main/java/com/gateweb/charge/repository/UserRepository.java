/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:15 CST 2018
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
 * This class provides methods to populate DB Table of User
 */
 @Repository("user")
public interface UserRepository extends JpaRepository<UserEntity, Integer>
	, QuerydslPredicateExecutor<UserEntity> ,UserRepositoryCustom {
	
    /**
     * Delete a record in Database.
	 * @param $pkVar.columnName   PK 
	 * @throws DaoSystemException	if system is wrong.
     */
	void deleteByUserId(	
		java.lang.Integer userId 
	) throws DaoSystemException;

//	public UserEntity findByUserId(Long userId);
//	
//	public Page<UserEntity> findByUserId(Long userId, Pageable pageable);
//	
//	public boolean exists(Long userId);
//	
//	public List<UserEntity> findAll();
//	
//	public List<UserEntity> findTop100ByUserId(Long userId);
//	
//	public long count();

			
}

	