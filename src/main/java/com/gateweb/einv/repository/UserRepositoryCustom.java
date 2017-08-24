/*
 * $Header: $
 * This java source file is generated by pkliu on Thu Aug 03 11:22:57 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.einv.repository; 
    
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import com.gateweb.einv.model.User;
import com.meshinnovation.db.dao.exception.DaoSystemException;
/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of User
 */
@NoRepositoryBean
public interface UserRepositoryCustom {
	

	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<User> searchWithVo(User data) throws DaoSystemException; 
	
	/**
	 *
	 * @param data	serach criteria
	 * @param pageable pagenation
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<User> searchWithVo(User data, Pageable pageable) throws DaoSystemException; 
	
	/**
	 * 
	 * @param vo
	 * @param pageOffset
	 * @param pageSize
	 * @return
	 */
	public List<User> searchWithVo(User vo, int pageOffset, int pageSize);
	
	/**
	 * 
	 * @param vo
	 * @param pageOffset
	 * @param pageSize
	 * @return
	 */
	public List<User> searchLikeVo(User vo, int pageOffset, int pageSize);

	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<User> searchLikeVo(User data) throws DaoSystemException; 
	
	/**
	 *
	 * @param data	serach criteria
	 * @param pageable pagenation
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<User> searchLikeVo(User data, Pageable pageable) throws DaoSystemException; 
			
}

	