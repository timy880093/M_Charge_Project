package com.gateweb.charge.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gateweb.charge.model.UserEntity;



public interface UserFacade extends UserDetailsService{
	/**
	 * search user name from database
	 * @param userName The param
	 * @return User
	 */
	public UserEntity getUserByLogin(String userName);

	/**
	 * search user name from database
	 * @param userName The param
	 * @return UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String userName) 
		throws UsernameNotFoundException, DataAccessException;
	/**
	 * 從session取出LoginUser
	 * @return
	 */
	UserEntity getCurrentLoginUser();

	/**
	 * find user by email
	 * @param email
	 * @return User
	 * @
	 */
	public UserEntity findUserByEmail(String email);
	
	/**
	 * check repeat by user email
	 * @param email
	 * @return User
	 * @
	 */
	public UserEntity checkRepeatByUserEmail(String email);
	
	/**
	 * 查詢使用者資料
	 * @param uid
	 * @return User
	 * @
	 */
	public UserEntity findUserByUid(Integer uid);
}
