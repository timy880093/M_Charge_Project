/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:14 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.dao; 
    
import java.sql.SQLException;
import java.util.Collection;
import com.gateweb.charge.*;
import com.gateweb.charge.model.*;
import com.gateweb.db.dao.GenericDao;
import com.gateweb.db.dao.exception.DaoSystemException;
import java.io.Serializable;
import java.util.List;


/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of Company
 */
public interface CompanyDao extends GenericDao<CompanyEntity,               java.lang.Integer                 > {


	
    /**
     * Delete a record in Database.
	 * @param companyId   PK 
	 * @throws DaoSystemException	if system is wrong.
     */
	public void delete(
		java.lang.Integer companyId 
	) throws DaoSystemException;

	/**
	 *
	 * @param pageNo	page number
	 * @param noRowsPerPage	how many rows in one page
	 * @return list of value
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<CompanyEntity> getSome(int pageNo, int noRowsPerPage) throws DaoSystemException;

	
	/**
	 *
	 * @param propertyName	property name
	 * @param value object value
	 * @param rowStartIdxAndCount 
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<CompanyEntity> searchByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) throws DaoSystemException; 

	
	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<CompanyEntity> searchBy(CompanyEntity data) throws DaoSystemException; 


	/**
	 *
	 * @param data	serach criteria
	 * @return list of value object
	 * @throws DaoSystemException	if system is wrong.	 
	 */
	public List<CompanyEntity> searchLike(CompanyEntity data) throws DaoSystemException; 
			
}

	