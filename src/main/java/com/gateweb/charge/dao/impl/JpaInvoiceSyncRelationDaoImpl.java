/*
 * $Header: $
 * This java source file is generated by pkliu on Mon Oct 30 14:37:48 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.dao.impl; 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Repository;

import com.gateweb.charge.dao.InvoiceSyncRelationDao;
import com.gateweb.charge.model.InvoiceSyncRelationEntity;
import com.gateweb.charge.model.InvoiceSyncRelationEntityPK;
import com.gateweb.db.dao.exception.DaoSystemException;
import com.gateweb.jpa.dao.JpaGenericDaoImpl;
/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of InvoiceSyncRelation
 */
@Repository("invoiceSyncRelation")
public class JpaInvoiceSyncRelationDaoImpl extends JpaGenericDaoImpl<InvoiceSyncRelationEntity, InvoiceSyncRelationEntityPK >  implements InvoiceSyncRelationDao{

	/**
	 *
	 */
	public JpaInvoiceSyncRelationDaoImpl(){
	}

	/**
	 *
	 */ 
	protected final Logger logger = LogManager.getLogger(getClass());

    /**
     * Delete a record in Database.
	 * @param uploadType   PK 
	 * @param sellerIdentifier   PK 
	 * @param cYearMonth   PK 
	 * @param invoiceNumber   PK 
    */
	public void delete(
		InvoiceSyncRelationEntityPK id
	) {
		logger.debug("JpaInvoiceSyncRelationDaoImpl delete  begin "
			+"id="+id.uploadType
+"id="+id.sellerIdentifier
+"id="+id.cYearMonth
+"id="+id.invoiceNumber
		);	
		try {
			Object data = entityManager.find(InvoiceSyncRelationEntity.class
			, id
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
	public List<InvoiceSyncRelationEntity> getSome(int pageNo, int noRowsPerPage)
		{
		List<InvoiceSyncRelationEntity> results = new ArrayList();
		Query q;
		try {
			q = entityManager.createQuery("select obj from InvoiceSyncRelationEntity obj ");
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
	public List<InvoiceSyncRelationEntity> searchByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		final String queryString = "select model from InvoiceSyncRelationEntity model where model."
					+ propertyName + "= :propertyValue";
		logger.debug("JpaInvoiceSyncRelationDaoImpl findByProperty   queryString :   "+queryString);
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
	public List<InvoiceSyncRelationEntity> searchBy(InvoiceSyncRelationEntity data) {
		StringBuffer sb = new StringBuffer("select obj from InvoiceSyncRelationEntity obj where 1=1 ");

		if ( data.getId().getUploadType() != null ) {
			sb.append(" AND obj.uploadType = :uploadType "); //java.lang.String
		}
		if ( data.getId().getSellerIdentifier() != null ) {
			sb.append(" AND obj.sellerIdentifier = :sellerIdentifier "); //java.lang.String
		}
		if ( data.getId().getcYearMonth() != null ) {
			sb.append(" AND obj.cYearMonth = :cYearMonth "); //java.lang.String
		}
		if ( data.getCompanyKey() != null ) {
			sb.append(" AND obj.companyKey = :companyKey "); //java.lang.String
		}
		if ( data.getSendMail() != null ) {
			sb.append(" AND obj.sendMail is :sendMail "); //java.lang.Boolean
		}
		if ( data.getId().getInvoiceNumber() != null ) {
			sb.append(" AND obj.invoiceNumber = :invoiceNumber "); //java.lang.String
		}
		Query q;
		List<InvoiceSyncRelationEntity> results;
		try {
			logger.debug("JpaInvoiceSyncRelationDaoImpl searchBy  query : "+sb.toString());
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
		if ( data.getId().getUploadType() != null ) {
			q.setParameter("uploadType", data.getId().getUploadType());
		}
		if ( data.getId().getSellerIdentifier() != null ) {
			q.setParameter("sellerIdentifier", data.getId().getSellerIdentifier());
		}
		if ( data.getId().getcYearMonth() != null ) {
			q.setParameter("cYearMonth", data.getId().getcYearMonth());
		}
		if ( data.getCompanyKey() != null ) {
			q.setParameter("companyKey", data.getCompanyKey());
		}
		if ( data.getSendMail() != null ) {
			q.setParameter("sendMail", data.getSendMail());
		}
		if ( data.getId().getInvoiceNumber() != null ) {
			q.setParameter("invoiceNumber", data.getId().getInvoiceNumber());
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
	public List<InvoiceSyncRelationEntity> searchLike(InvoiceSyncRelationEntity data) {
		StringBuffer sb = new StringBuffer("select obj from InvoiceSyncRelationEntity obj where 1=1 ");
		if ( data.getId().getUploadType() != null && !"".equals(data.getId().getUploadType())) {
			sb.append(" AND obj.uploadType Like :uploadType "); //java.lang.String
		}
		if ( data.getId().getSellerIdentifier() != null && !"".equals(data.getId().getSellerIdentifier())) {
			sb.append(" AND obj.sellerIdentifier Like :sellerIdentifier "); //java.lang.String
		}
		if ( data.getId().getcYearMonth() != null && !"".equals(data.getId().getcYearMonth())) {
			sb.append(" AND obj.cYearMonth Like :cYearMonth "); //java.lang.String
		}
		if ( data.getCompanyKey() != null && !"".equals(data.getCompanyKey())) {
			sb.append(" AND obj.companyKey Like :companyKey "); //java.lang.String
		}
		if ( data.getSendMail() != null ) {
			sb.append(" AND obj.sendMail is :sendMail "); //java.lang.Boolean
		}
		if ( data.getId().getInvoiceNumber() != null && !"".equals(data.getId().getInvoiceNumber())) {
			sb.append(" AND obj.invoiceNumber Like :invoiceNumber "); //java.lang.String
		}
		Query q;
		List<InvoiceSyncRelationEntity> results;
		try {
			logger.debug("JpaInvoiceSyncRelationDaoImpl searchLike  query : "+sb.toString());
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
		if ( data.getId().getUploadType() != null && !"".equals(data.getId().getUploadType()) ) {
			q.setParameter("uploadType", data.getId().getUploadType()+"%");
		}
		if ( data.getId().getSellerIdentifier() != null && !"".equals(data.getId().getSellerIdentifier()) ) {
			q.setParameter("sellerIdentifier", data.getId().getSellerIdentifier()+"%");
		}
		if ( data.getId().getcYearMonth() != null && !"".equals(data.getId().getcYearMonth()) ) {
			q.setParameter("cYearMonth", data.getId().getcYearMonth()+"%");
		}
		if ( data.getCompanyKey() != null && !"".equals(data.getCompanyKey()) ) {
			q.setParameter("companyKey", data.getCompanyKey()+"%");
		}
		if ( data.getSendMail() != null ) {
			q.setParameter("sendMail", data.getSendMail());
		}  	
		if ( data.getId().getInvoiceNumber() != null && !"".equals(data.getId().getInvoiceNumber()) ) {
			q.setParameter("invoiceNumber", data.getId().getInvoiceNumber()+"%");
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
