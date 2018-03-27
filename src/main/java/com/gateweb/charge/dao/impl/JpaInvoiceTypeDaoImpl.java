/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:14 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.dao.impl; 
import com.gateweb.charge.dao.*; 
import com.gateweb.charge.model.*;    
import com.gateweb.charge.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Collection;
import javax.persistence.Query;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;
import com.gateweb.db.dao.exception.DaoSystemException;
import com.gateweb.jpa.dao.JpaGenericDaoImpl;
import org.springframework.stereotype.Repository;
/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of InvoiceType
 */
@Repository("invoiceType")
public class JpaInvoiceTypeDaoImpl extends JpaGenericDaoImpl<InvoiceTypeEntity,      java.lang.String  >  implements InvoiceTypeDao{

	/**
	 *
	 */
	public JpaInvoiceTypeDaoImpl(){
	}

	/**
	 *
	 */ 
	protected final Logger logger = LogManager.getLogger(getClass());

    /**
     * Delete a record in Database.
	 * @param typeCode   PK 
    */
	public void delete(
								 java.lang.String typeCode 
			) {
		logger.debug("JpaInvoiceTypeDaoImpl delete  begin "
			+"id="+typeCode
		);	
		try {
			Object data = entityManager.find(InvoiceTypeEntity.class
			, typeCode
			);
			entityManager.remove(data);
		} catch (IllegalStateException ise){
			//	RuntimeException
			ise.printStackTrace();
			throw new DaoSystemException("JPA10001-"+ise.getMessage(),ise);
		} catch (IllegalArgumentException iae){
			//	RuntimeException
			iae.printStackTrace();
			throw new DaoSystemException("JPA10002-"+iae.getMessage(),iae);
		} catch (TransactionRequiredException tre){
			//	PersistenceException	
			tre.printStackTrace();
			throw new DaoSystemException("JPA10004-"+tre.getMessage(),tre);	
		} 
    }  
   
	@SuppressWarnings("unchecked")
	public List<InvoiceTypeEntity> getSome(int pageNo, int noRowsPerPage)
		{
		List<InvoiceTypeEntity> results = new ArrayList();
		Query q;
		try {
			q = entityManager.createQuery("select obj from InvoiceTypeEntity obj ");
		} catch (IllegalStateException ise){
			//	RuntimeException
			ise.printStackTrace();
			throw new DaoSystemException("JPA10001-"+ise.getMessage(),ise);
		} catch (IllegalArgumentException iae){
			//	RuntimeException
			iae.printStackTrace();
			throw new DaoSystemException("JPA10002-"+iae.getMessage(),iae);
		}
		try {
			if (pageNo > 0 && noRowsPerPage > 0) {
				q.setFirstResult(noRowsPerPage * (pageNo - 1) + 1).setMaxResults(
					noRowsPerPage);
				results = q.getResultList();
			}
		} catch (IllegalStateException ise){
			//	RuntimeException
			ise.printStackTrace();
			throw new DaoSystemException("JPA10001-"+ise.getMessage(),ise);
		}
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<InvoiceTypeEntity> searchByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		final String queryString = "select model from InvoiceTypeEntity model where model."
					+ propertyName + "= :propertyValue";
		logger.debug("JpaInvoiceTypeDaoImpl findByProperty   queryString :   "+queryString);
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (IllegalStateException ise){
			//	RuntimeException
			ise.printStackTrace();
			throw new DaoSystemException("JPA10001-"+ise.getMessage(),ise);
		} catch (IllegalArgumentException iae){
			//	RuntimeException
			iae.printStackTrace();
			throw new DaoSystemException("JPA10002-"+iae.getMessage(),iae);
		}

	}   	
	
	@SuppressWarnings("unchecked")
	public List<InvoiceTypeEntity> searchBy(InvoiceTypeEntity data) {
		StringBuffer sb = new StringBuffer("select obj from InvoiceTypeEntity obj where 1=1 ");

		if ( data.getTypeFormat() != null ) {
			sb.append(" AND obj.typeFormat = :typeFormat "); //java.lang.String
		}
		if ( data.getDescription() != null ) {
			sb.append(" AND obj.description = :description "); //java.lang.String
		}
		if ( data.getInvoiceNumber() != null ) {
			sb.append(" AND obj.invoiceNumber = :invoiceNumber "); //java.lang.Integer
		}
		if ( data.getTypeCode() != null ) {
			sb.append(" AND obj.typeCode = :typeCode "); //java.lang.String
		}
		if ( data.getOutFormat() != null ) {
			sb.append(" AND obj.outFormat = :outFormat "); //java.lang.String
		}
		Query q;
		List<InvoiceTypeEntity> results;
		try {
			logger.debug("JpaInvoiceTypeDaoImpl searchBy  query : "+sb.toString());
			q = entityManager.createQuery(sb.toString());
		} catch (IllegalStateException ise){
			//	RuntimeException
			ise.printStackTrace();
			throw new DaoSystemException("JPA10001-"+ise.getMessage(),ise);
		} catch (IllegalArgumentException iae){
			//	RuntimeException
			iae.printStackTrace();
			throw new DaoSystemException("JPA10002-"+iae.getMessage(),iae);
		}
		if ( data.getTypeFormat() != null ) {
			q.setParameter("typeFormat", data.getTypeFormat());
		}
		if ( data.getDescription() != null ) {
			q.setParameter("description", data.getDescription());
		}
		if ( data.getInvoiceNumber() != null ) {
			q.setParameter("invoiceNumber", data.getInvoiceNumber());
		}
		if ( data.getTypeCode() != null ) {
			q.setParameter("typeCode", data.getTypeCode());
		}
		if ( data.getOutFormat() != null ) {
			q.setParameter("outFormat", data.getOutFormat());
		}
		try {
			results = q.getResultList();
		} catch (IllegalStateException ise){
			//	RuntimeException
			ise.printStackTrace();
			throw new DaoSystemException("JPA10001-"+ise.getMessage(),ise);
		} 
		return results; 
	}

	@SuppressWarnings("unchecked")
	public List<InvoiceTypeEntity> searchLike(InvoiceTypeEntity data) {
		StringBuffer sb = new StringBuffer("select obj from InvoiceTypeEntity obj where 1=1 ");
		if ( data.getTypeFormat() != null && !"".equals(data.getTypeFormat())) {
			sb.append(" AND obj.typeFormat Like :typeFormat "); //java.lang.String
		}
		if ( data.getDescription() != null && !"".equals(data.getDescription())) {
			sb.append(" AND obj.description Like :description "); //java.lang.String
		}
		if ( data.getInvoiceNumber() != null && !"".equals(data.getInvoiceNumber())) {
			sb.append(" AND obj.invoiceNumber Like :invoiceNumber "); //java.lang.Integer
		}
		if ( data.getTypeCode() != null && !"".equals(data.getTypeCode())) {
			sb.append(" AND obj.typeCode Like :typeCode "); //java.lang.String
		}
		if ( data.getOutFormat() != null && !"".equals(data.getOutFormat())) {
			sb.append(" AND obj.outFormat Like :outFormat "); //java.lang.String
		}
		Query q;
		List<InvoiceTypeEntity> results;
		try {
			logger.debug("JpaInvoiceTypeDaoImpl searchLike  query : "+sb.toString());
			q = entityManager.createQuery(sb.toString());
		} catch (IllegalStateException ise){
			//	RuntimeException
			ise.printStackTrace();
			throw new DaoSystemException("JPA10001-"+ise.getMessage(),ise);
		} catch (IllegalArgumentException iae){
			//	RuntimeException
			iae.printStackTrace();
			throw new DaoSystemException("JPA10002-"+iae.getMessage(),iae);
		}
		if ( data.getTypeFormat() != null && !"".equals(data.getTypeFormat()) ) {
			q.setParameter("typeFormat", data.getTypeFormat()+"%");
		}
		if ( data.getDescription() != null && !"".equals(data.getDescription()) ) {
			q.setParameter("description", data.getDescription()+"%");
		}
		if ( data.getInvoiceNumber() != null && !"".equals(data.getInvoiceNumber()) ) {
			q.setParameter("invoiceNumber", data.getInvoiceNumber()+"%");
		}
		if ( data.getTypeCode() != null && !"".equals(data.getTypeCode()) ) {
			q.setParameter("typeCode", data.getTypeCode()+"%");
		}
		if ( data.getOutFormat() != null && !"".equals(data.getOutFormat()) ) {
			q.setParameter("outFormat", data.getOutFormat()+"%");
		}
		try {
			results = q.getResultList();
		} catch (IllegalStateException ise){
			//	RuntimeException
			ise.printStackTrace();
			throw new DaoSystemException("JPA10001-"+ise.getMessage(),ise);
		}
		
		return results;
	}	
	
}
