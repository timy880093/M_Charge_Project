/*
 * $Header: $
 * This java source file is generated by pkliu on Thu Aug 03 11:22:57 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.orm.einv.repository;

import com.gateweb.orm.einv.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of User
 */
 @Repository
public interface EinvUserRepository extends JpaRepository<User, Long>
	, QuerydslPredicateExecutor<User> {

	public User findByUserId(Long userId);

			
}

	