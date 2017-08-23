/*
 * $Header: $
 * This java source file is generated by pkliu on Fri Aug 11 14:13:12 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.turnkey.repository; 
    
import java.sql.SQLException;
import java.util.Collection;
import com.gateweb.turnkey.*;
import com.gateweb.turnkey.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import com.meshinnovation.db.dao.exception.DaoSystemException;
import java.io.Serializable;
import java.util.List;
/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of TurnkeyMessageLog
 */
@NoRepositoryBean
public interface TurnkeyMessageLogRepositoryCustom {
	

	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<TurnkeyMessageLog> searchWithVo(TurnkeyMessageLog data) throws DaoSystemException; 
	
	/**
	 *
	 * @param data	serach criteria
	 * @param pageable pagenation
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<TurnkeyMessageLog> searchWithVo(TurnkeyMessageLog data, Pageable pageable) throws DaoSystemException; 
	
	/**
	 * 
	 * @param vo
	 * @param pageOffset
	 * @param pageSize
	 * @return
	 */
	public List<TurnkeyMessageLog> searchWithVo(TurnkeyMessageLog vo, int pageOffset, int pageSize);
	
	/**
	 * 
	 * @param vo
	 * @param pageOffset
	 * @param pageSize
	 * @return
	 */
	public List<TurnkeyMessageLog> searchLikeVo(TurnkeyMessageLog vo, int pageOffset, int pageSize);

	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<TurnkeyMessageLog> searchLikeVo(TurnkeyMessageLog data) throws DaoSystemException; 
	
	/**
	 *
	 * @param data	serach criteria
	 * @param pageable pagenation
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<TurnkeyMessageLog> searchLikeVo(TurnkeyMessageLog data, Pageable pageable) throws DaoSystemException; 
			
}

	