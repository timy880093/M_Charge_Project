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
 * This class provides methods to populate DB Table of TaskConfig
 */
@NoRepositoryBean
public interface TaskConfigRepositoryCustom {
	

	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<TaskConfig> searchWithVo(TaskConfig data) throws DaoSystemException; 
	
	/**
	 *
	 * @param data	serach criteria
	 * @param pageable pagenation
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<TaskConfig> searchWithVo(TaskConfig data, Pageable pageable) throws DaoSystemException; 
	
	/**
	 * 
	 * @param vo
	 * @param pageOffset
	 * @param pageSize
	 * @return
	 */
	public List<TaskConfig> searchWithVo(TaskConfig vo, int pageOffset, int pageSize);
	
	/**
	 * 
	 * @param vo
	 * @param pageOffset
	 * @param pageSize
	 * @return
	 */
	public List<TaskConfig> searchLikeVo(TaskConfig vo, int pageOffset, int pageSize);

	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<TaskConfig> searchLikeVo(TaskConfig data) throws DaoSystemException; 
	
	/**
	 *
	 * @param data	serach criteria
	 * @param pageable pagenation
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<TaskConfig> searchLikeVo(TaskConfig data, Pageable pageable) throws DaoSystemException; 
			
}

	