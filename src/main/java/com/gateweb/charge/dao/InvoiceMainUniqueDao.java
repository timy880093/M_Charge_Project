/*
 * $Header: $
 * This java source file is generated by pkliu on Mon Oct 30 14:37:48 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.dao; 
    
import java.util.List;

import com.gateweb.charge.model.InvoiceMainUniqueEntity;
import com.gateweb.charge.model.InvoiceMainUniqueEntityPK;
import com.gateweb.db.dao.GenericDao;
import com.gateweb.db.dao.exception.DaoSystemException;


/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of InvoiceMainUnique
 */
public interface InvoiceMainUniqueDao extends GenericDao<InvoiceMainUniqueEntity, InvoiceMainUniqueEntityPK > {


	
    /**
     * Delete a record in Database.
	 * @throws DaoSystemException	if system is wrong.
     */
	public void delete(
					InvoiceMainUniqueEntityPK id
	) throws DaoSystemException;

	/**
	 *
	 * @param pageNo	page number
	 * @param noRowsPerPage	how many rows in one page
	 * @return list of value
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<InvoiceMainUniqueEntity> getSome(int pageNo, int noRowsPerPage) throws DaoSystemException;

	
	/**
	 *
	 * @param propertyName	property name
	 * @param value object value
	 * @param rowStartIdxAndCount 
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<InvoiceMainUniqueEntity> searchByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) throws DaoSystemException; 

	
	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<InvoiceMainUniqueEntity> searchBy(InvoiceMainUniqueEntity data) throws DaoSystemException; 


	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<InvoiceMainUniqueEntity> searchLike(InvoiceMainUniqueEntity data) throws DaoSystemException; 
			
}

	