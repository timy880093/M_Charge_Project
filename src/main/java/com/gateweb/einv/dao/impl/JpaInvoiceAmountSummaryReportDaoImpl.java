/*
 * $Header: $
 * This java source file is generated by pkliu on Mon Jan 29 11:19:52 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.einv.dao.impl;

import com.gateweb.einv.dao.InvoiceAmountSummaryReportDao;
import com.gateweb.einv.model.InvoiceAmountSummaryReportEntity;
import com.gateweb.db.dao.exception.DaoSystemException;
import com.gateweb.jpa.dao.EinvJpaGenericDaoImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of InvoiceAmountSummaryReport
 */
@Repository("einvInvoiceAmountSummaryReportDao")
public class JpaInvoiceAmountSummaryReportDaoImpl extends EinvJpaGenericDaoImpl<InvoiceAmountSummaryReportEntity,        Long      > implements InvoiceAmountSummaryReportDao {

	/**
	 *
	 */
	public JpaInvoiceAmountSummaryReportDaoImpl(){

	}

	/**
	 *
	 */
	protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Delete a record in Database.
	 * @param id   PK
    */
	public void delete(
												 Long id
											) {
		log.debug("JpaInvoiceAmountSummaryReportDaoImpl delete  begin "
			+"id="+id
		);	
		try {
			Object data = entityManager.find(InvoiceAmountSummaryReportEntity.class
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
	public List<InvoiceAmountSummaryReportEntity> getSome(int pageNo, int noRowsPerPage)
		{
		List<InvoiceAmountSummaryReportEntity> results = new ArrayList();
		Query q;
		try {
			q = entityManager.createQuery("select obj from InvoiceAmountSummaryReportEntity obj ");
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
	public List<InvoiceAmountSummaryReportEntity> searchByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		final String queryString = "select model from InvoiceAmountSummaryReportEntity model where model."
					+ propertyName + "= :propertyValue";
		log.debug("JpaInvoiceAmountSummaryReportDaoImpl findByProperty   queryString :   "+queryString);
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
	public List<InvoiceAmountSummaryReportEntity> searchBy(InvoiceAmountSummaryReportEntity data) {
		StringBuffer sb = new StringBuffer("select obj from InvoiceAmountSummaryReportEntity obj where 1=1 ");

		if ( data.getSeller() != null ) {
			sb.append(" AND obj.seller = :seller "); //java.lang.String
		}
		if ( data.getAmount() != null ) {
			sb.append(" AND obj.amount = :amount "); //java.lang.Integer
		}
		if ( data.getTotal() != null ) {
			sb.append(" AND obj.total = :total "); //java.math.BigDecimal
		}
		if ( data.getCreatorId() != null ) {
			sb.append(" AND obj.creatorId = :creatorId "); //java.lang.Integer
		}
		if ( data.getModifierId() != null ) {
			sb.append(" AND obj.modifierId = :modifierId "); //java.lang.Integer
		}
		if ( data.getId() != null ) {
			sb.append(" AND obj.id = :id "); //java.lang.Long
		}
		if ( data.getCreateDate() != null ) {
			sb.append(" AND obj.createDate = :createDate "); //java.sql.Timestamp
		}
		if ( data.getModifyDate() != null ) {
			sb.append(" AND obj.modifyDate = :modifyDate "); //java.sql.Timestamp
		}
		if ( data.getInvoiceDate() != null ) {
			sb.append(" AND obj.invoiceDate = :invoiceDate "); //java.lang.String
		}
		if ( data.getBuyer() != null ) {
			sb.append(" AND obj.buyer = :buyer "); //java.lang.String
		}
		if ( data.getInvoiceStatus() != null ) {
			sb.append(" AND obj.invoiceStatus = :invoiceStatus "); //java.lang.Integer
		}
		Query q;
		List<InvoiceAmountSummaryReportEntity> results;
		try {
			log.debug("JpaInvoiceAmountSummaryReportDaoImpl searchBy  query : "+sb.toString());
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
		if ( data.getSeller() != null ) {
			q.setParameter("seller", data.getSeller());
		}
		if ( data.getAmount() != null ) {
			q.setParameter("amount", data.getAmount());
		}
		if ( data.getTotal() != null ) {
			q.setParameter("total", data.getTotal());
		}
		if ( data.getCreatorId() != null ) {
			q.setParameter("creatorId", data.getCreatorId());
		}
		if ( data.getModifierId() != null ) {
			q.setParameter("modifierId", data.getModifierId());
		}
		if ( data.getId() != null ) {
			q.setParameter("id", data.getId());
		}
		if ( data.getCreateDate() != null ) {
			q.setParameter("createDate", data.getCreateDate());
		}
		if ( data.getModifyDate() != null ) {
			q.setParameter("modifyDate", data.getModifyDate());
		}
		if ( data.getInvoiceDate() != null ) {
			q.setParameter("invoiceDate", data.getInvoiceDate());
		}
		if ( data.getBuyer() != null ) {
			q.setParameter("buyer", data.getBuyer());
		}
		if ( data.getInvoiceStatus() != null ) {
			q.setParameter("invoiceStatus", data.getInvoiceStatus());
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
	public List<InvoiceAmountSummaryReportEntity> searchLike(InvoiceAmountSummaryReportEntity data) {
		StringBuffer sb = new StringBuffer("select obj from InvoiceAmountSummaryReportEntity obj where 1=1 ");
		if ( data.getSeller() != null && !"".equals(data.getSeller())) {
			sb.append(" AND obj.seller Like :seller "); //java.lang.String
		}
		if ( data.getAmount() != null && !"".equals(data.getAmount())) {
			sb.append(" AND obj.amount Like :amount "); //java.lang.Integer
		}
		if ( data.getTotal() != null ) {
			sb.append(" AND obj.total >= :total "); // java.math.BigDecimal
		}
		if ( data.getCreatorId() != null && !"".equals(data.getCreatorId())) {
			sb.append(" AND obj.creatorId Like :creatorId "); //java.lang.Integer
		}
		if ( data.getModifierId() != null && !"".equals(data.getModifierId())) {
			sb.append(" AND obj.modifierId Like :modifierId "); //java.lang.Integer
		}
		if ( data.getId() != null ) {
			sb.append(" AND obj.id >= :id "); // java.lang.Long
		}
		if ( data.getCreateDate() != null && !"".equals(data.getCreateDate())) {
			sb.append(" AND obj.createDate Like :createDate "); //java.sql.Timestamp
		}
		if ( data.getModifyDate() != null && !"".equals(data.getModifyDate())) {
			sb.append(" AND obj.modifyDate Like :modifyDate "); //java.sql.Timestamp
		}
		if ( data.getInvoiceDate() != null && !"".equals(data.getInvoiceDate())) {
			sb.append(" AND obj.invoiceDate Like :invoiceDate "); //java.lang.String
		}
		if ( data.getBuyer() != null && !"".equals(data.getBuyer())) {
			sb.append(" AND obj.buyer Like :buyer "); //java.lang.String
		}
		if ( data.getInvoiceStatus() != null && !"".equals(data.getInvoiceStatus())) {
			sb.append(" AND obj.invoiceStatus Like :invoiceStatus "); //java.lang.Integer
		}
		Query q;
		List<InvoiceAmountSummaryReportEntity> results;
		try {
			log.debug("JpaInvoiceAmountSummaryReportDaoImpl searchLike  query : "+sb.toString());
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
		if ( data.getSeller() != null && !"".equals(data.getSeller()) ) {
			q.setParameter("seller", data.getSeller()+"%");
		}
		if ( data.getAmount() != null && !"".equals(data.getAmount()) ) {
			q.setParameter("amount", data.getAmount()+"%");
		}
		if ( data.getTotal() != null ) {
			q.setParameter("total", data.getTotal());
		}  	
		if ( data.getCreatorId() != null && !"".equals(data.getCreatorId()) ) {
			q.setParameter("creatorId", data.getCreatorId()+"%");
		}
		if ( data.getModifierId() != null && !"".equals(data.getModifierId()) ) {
			q.setParameter("modifierId", data.getModifierId()+"%");
		}
		if ( data.getId() != null ) {
			q.setParameter("id", data.getId());
		}  	
		if ( data.getCreateDate() != null && !"".equals(data.getCreateDate()) ) {
			q.setParameter("createDate", data.getCreateDate()+"%");
		}
		if ( data.getModifyDate() != null && !"".equals(data.getModifyDate()) ) {
			q.setParameter("modifyDate", data.getModifyDate()+"%");
		}
		if ( data.getInvoiceDate() != null && !"".equals(data.getInvoiceDate()) ) {
			q.setParameter("invoiceDate", data.getInvoiceDate()+"%");
		}
		if ( data.getBuyer() != null && !"".equals(data.getBuyer()) ) {
			q.setParameter("buyer", data.getBuyer()+"%");
		}
		if ( data.getInvoiceStatus() != null && !"".equals(data.getInvoiceStatus()) ) {
			q.setParameter("invoiceStatus", data.getInvoiceStatus()+"%");
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
