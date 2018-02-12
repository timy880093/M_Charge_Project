/*
 * $Header: $
 * This java source file is generated by pkliu on Mon Jan 29 11:19:52 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.einv.repository;

import com.gateweb.einv.model.AllowanceAmountSummaryReportEntity;
import com.gateweb.db.dao.exception.DaoSystemException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of AllowanceAmountSummaryReport
 */
@NoRepositoryBean
public interface AllowanceAmountSummaryReportRepositoryCustom {
	

	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException    if system is wrong.
	 */
	public List<AllowanceAmountSummaryReportEntity> searchWithVo(AllowanceAmountSummaryReportEntity data) throws DaoSystemException;
	
	/**
	 *
	 * @param data	serach criteria
	 * @param pageable pagenation
	 * @return list of value object
	 * @throws DaoSystemException    if system is wrong.
	 */
	public List<AllowanceAmountSummaryReportEntity> searchWithVo(AllowanceAmountSummaryReportEntity data, Pageable pageable) throws DaoSystemException;
	
	/**
	 * 
	 * @param vo
	 * @param pageOffset
	 * @param pageSize
	 * @return
	 */
	public List<AllowanceAmountSummaryReportEntity> searchWithVo(AllowanceAmountSummaryReportEntity vo, int pageOffset, int pageSize);
	
	/**
	 * 
	 * @param vo
	 * @param pageOffset
	 * @param pageSize
	 * @return
	 */
	public List<AllowanceAmountSummaryReportEntity> searchLikeVo(AllowanceAmountSummaryReportEntity vo, int pageOffset, int pageSize);

	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException    if system is wrong.
	 */
	public List<AllowanceAmountSummaryReportEntity> searchLikeVo(AllowanceAmountSummaryReportEntity data) throws DaoSystemException;
	
	/**
	 *
	 * @param data	serach criteria
	 * @param pageable pagenation
	 * @return list of value object
	 * @throws DaoSystemException    if system is wrong.
	 */
	public List<AllowanceAmountSummaryReportEntity> searchLikeVo(AllowanceAmountSummaryReportEntity data, Pageable pageable) throws DaoSystemException;
			
}

	