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
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import com.meshinnovation.db.dao.exception.DaoSystemException;
import java.io.Serializable;
import java.util.List;
/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of ChargeModeGrade
 */
@NoRepositoryBean
public interface ChargeModeGradeRepositoryCustom {
	

	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<ChargeModeGradeEntity> searchWithVo(ChargeModeGradeEntity data) throws DaoSystemException; 
	
	/**
	 *
	 * @param data	serach criteria
	 * @param pageable pagenation
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<ChargeModeGradeEntity> searchWithVo(ChargeModeGradeEntity data, Pageable pageable) throws DaoSystemException; 
	
	/**
	 * 
	 * @param vo
	 * @param pageOffset
	 * @param pageSize
	 * @return
	 */
	public List<ChargeModeGradeEntity> searchWithVo(ChargeModeGradeEntity vo, int pageOffset, int pageSize);
	
	/**
	 * 
	 * @param vo
	 * @param pageOffset
	 * @param pageSize
	 * @return
	 */
	public List<ChargeModeGradeEntity> searchLikeVo(ChargeModeGradeEntity vo, int pageOffset, int pageSize);

	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<ChargeModeGradeEntity> searchLikeVo(ChargeModeGradeEntity data) throws DaoSystemException; 
	
	/**
	 *
	 * @param data	serach criteria
	 * @param pageable pagenation
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<ChargeModeGradeEntity> searchLikeVo(ChargeModeGradeEntity data, Pageable pageable) throws DaoSystemException; 
			
}

	