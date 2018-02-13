/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:15 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.dao.impl; 
import com.gateweb.charge.dao.*; 
import com.gateweb.charge.model.*;    
import com.gateweb.charge.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * This class provides methods to populate DB Table of Warranty
 */
@Repository("warranty")
public class JpaWarrantyDaoImpl extends JpaGenericDaoImpl<WarrantyEntity,        java.lang.Integer          >  implements WarrantyDao{

	/**
	 *
	 */
	public JpaWarrantyDaoImpl(){
	}

	/**
	 *
	 */ 
	protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Delete a record in Database.
	 * @param warrantyId   PK 
    */
	public void delete(
												 java.lang.Integer warrantyId 
																			) {
		log.debug("JpaWarrantyDaoImpl delete  begin "
			+"id="+warrantyId
		);	
		try {
			Object data = entityManager.find(WarrantyEntity.class
			, warrantyId
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
	public List<WarrantyEntity> getSome(int pageNo, int noRowsPerPage)
		{
		List<WarrantyEntity> results = new ArrayList();
		Query q;
		try {
			q = entityManager.createQuery("select obj from WarrantyEntity obj ");
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
	public List<WarrantyEntity> searchByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		final String queryString = "select model from WarrantyEntity model where model."
					+ propertyName + "= :propertyValue";
		log.debug("JpaWarrantyDaoImpl findByProperty   queryString :   "+queryString);
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
	public List<WarrantyEntity> searchBy(WarrantyEntity data) {
		StringBuffer sb = new StringBuffer("select obj from WarrantyEntity obj where 1=1 ");

		if ( data.getEndDate() != null ) {
			sb.append(" AND obj.endDate = :endDate "); //java.util.Date
		}
		if ( data.getNote() != null ) {
			sb.append(" AND obj.note = :note "); //java.lang.String
		}
		if ( data.getDealerCompanyId() != null ) {
			sb.append(" AND obj.dealerCompanyId = :dealerCompanyId "); //java.lang.Integer
		}
		if ( data.getCompanyId() != null ) {
			sb.append(" AND obj.companyId = :companyId "); //java.lang.Integer
		}
		if ( data.getWarrantyNo() != null ) {
			sb.append(" AND obj.warrantyNo = :warrantyNo "); //java.lang.String
		}
		if ( data.getWarrantyId() != null ) {
			sb.append(" AND obj.warrantyId = :warrantyId "); //java.lang.Integer
		}
		if ( data.getExtend() != null ) {
			sb.append(" AND obj.extend is :extend "); //java.lang.Boolean
		}
		if ( data.getOnlyShip() != null ) {
			sb.append(" AND obj.onlyShip = :onlyShip "); //java.lang.Integer
		}
		if ( data.getCreatorId() != null ) {
			sb.append(" AND obj.creatorId = :creatorId "); //java.lang.Integer
		}
		if ( data.getModifierId() != null ) {
			sb.append(" AND obj.modifierId = :modifierId "); //java.lang.Integer
		}
		if ( data.getModel() != null ) {
			sb.append(" AND obj.model = :model "); //java.lang.String
		}
		if ( data.getCreateDate() != null ) {
			sb.append(" AND obj.createDate = :createDate "); //java.sql.Timestamp
		}
		if ( data.getModifyDate() != null ) {
			sb.append(" AND obj.modifyDate = :modifyDate "); //java.sql.Timestamp
		}
		if ( data.getStartDate() != null ) {
			sb.append(" AND obj.startDate = :startDate "); //java.util.Date
		}
		if ( data.getStatus() != null ) {
			sb.append(" AND obj.status = :status "); //java.lang.Integer
		}
		Query q;
		List<WarrantyEntity> results;
		try {
			log.debug("JpaWarrantyDaoImpl searchBy  query : "+sb.toString());
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
		if ( data.getEndDate() != null ) {
			q.setParameter("endDate", data.getEndDate());
		}
		if ( data.getNote() != null ) {
			q.setParameter("note", data.getNote());
		}
		if ( data.getDealerCompanyId() != null ) {
			q.setParameter("dealerCompanyId", data.getDealerCompanyId());
		}
		if ( data.getCompanyId() != null ) {
			q.setParameter("companyId", data.getCompanyId());
		}
		if ( data.getWarrantyNo() != null ) {
			q.setParameter("warrantyNo", data.getWarrantyNo());
		}
		if ( data.getWarrantyId() != null ) {
			q.setParameter("warrantyId", data.getWarrantyId());
		}
		if ( data.getExtend() != null ) {
			q.setParameter("extend", data.getExtend());
		}
		if ( data.getOnlyShip() != null ) {
			q.setParameter("onlyShip", data.getOnlyShip());
		}
		if ( data.getCreatorId() != null ) {
			q.setParameter("creatorId", data.getCreatorId());
		}
		if ( data.getModifierId() != null ) {
			q.setParameter("modifierId", data.getModifierId());
		}
		if ( data.getModel() != null ) {
			q.setParameter("model", data.getModel());
		}
		if ( data.getCreateDate() != null ) {
			q.setParameter("createDate", data.getCreateDate());
		}
		if ( data.getModifyDate() != null ) {
			q.setParameter("modifyDate", data.getModifyDate());
		}
		if ( data.getStartDate() != null ) {
			q.setParameter("startDate", data.getStartDate());
		}
		if ( data.getStatus() != null ) {
			q.setParameter("status", data.getStatus());
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
	public List<WarrantyEntity> searchLike(WarrantyEntity data) {
		StringBuffer sb = new StringBuffer("select obj from WarrantyEntity obj where 1=1 ");
		if ( data.getEndDate() != null && !"".equals(data.getEndDate())) {
			sb.append(" AND obj.endDate Like :endDate "); //java.util.Date
		}
		if ( data.getNote() != null && !"".equals(data.getNote())) {
			sb.append(" AND obj.note Like :note "); //java.lang.String
		}
		if ( data.getDealerCompanyId() != null && !"".equals(data.getDealerCompanyId())) {
			sb.append(" AND obj.dealerCompanyId Like :dealerCompanyId "); //java.lang.Integer
		}
		if ( data.getCompanyId() != null && !"".equals(data.getCompanyId())) {
			sb.append(" AND obj.companyId Like :companyId "); //java.lang.Integer
		}
		if ( data.getWarrantyNo() != null && !"".equals(data.getWarrantyNo())) {
			sb.append(" AND obj.warrantyNo Like :warrantyNo "); //java.lang.String
		}
		if ( data.getWarrantyId() != null && !"".equals(data.getWarrantyId())) {
			sb.append(" AND obj.warrantyId Like :warrantyId "); //java.lang.Integer
		}
		if ( data.getExtend() != null ) {
			sb.append(" AND obj.extend is :extend "); //java.lang.Boolean
		}
		if ( data.getOnlyShip() != null && !"".equals(data.getOnlyShip())) {
			sb.append(" AND obj.onlyShip Like :onlyShip "); //java.lang.Integer
		}
		if ( data.getCreatorId() != null && !"".equals(data.getCreatorId())) {
			sb.append(" AND obj.creatorId Like :creatorId "); //java.lang.Integer
		}
		if ( data.getModifierId() != null && !"".equals(data.getModifierId())) {
			sb.append(" AND obj.modifierId Like :modifierId "); //java.lang.Integer
		}
		if ( data.getModel() != null && !"".equals(data.getModel())) {
			sb.append(" AND obj.model Like :model "); //java.lang.String
		}
		if ( data.getCreateDate() != null && !"".equals(data.getCreateDate())) {
			sb.append(" AND obj.createDate Like :createDate "); //java.sql.Timestamp
		}
		if ( data.getModifyDate() != null && !"".equals(data.getModifyDate())) {
			sb.append(" AND obj.modifyDate Like :modifyDate "); //java.sql.Timestamp
		}
		if ( data.getStartDate() != null && !"".equals(data.getStartDate())) {
			sb.append(" AND obj.startDate Like :startDate "); //java.util.Date
		}
		if ( data.getStatus() != null && !"".equals(data.getStatus())) {
			sb.append(" AND obj.status Like :status "); //java.lang.Integer
		}
		Query q;
		List<WarrantyEntity> results;
		try {
			log.debug("JpaWarrantyDaoImpl searchLike  query : "+sb.toString());
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
		if ( data.getEndDate() != null && !"".equals(data.getEndDate()) ) {
			q.setParameter("endDate", data.getEndDate()+"%");
		}
		if ( data.getNote() != null && !"".equals(data.getNote()) ) {
			q.setParameter("note", data.getNote()+"%");
		}
		if ( data.getDealerCompanyId() != null && !"".equals(data.getDealerCompanyId()) ) {
			q.setParameter("dealerCompanyId", data.getDealerCompanyId()+"%");
		}
		if ( data.getCompanyId() != null && !"".equals(data.getCompanyId()) ) {
			q.setParameter("companyId", data.getCompanyId()+"%");
		}
		if ( data.getWarrantyNo() != null && !"".equals(data.getWarrantyNo()) ) {
			q.setParameter("warrantyNo", data.getWarrantyNo()+"%");
		}
		if ( data.getWarrantyId() != null && !"".equals(data.getWarrantyId()) ) {
			q.setParameter("warrantyId", data.getWarrantyId()+"%");
		}
		if ( data.getExtend() != null ) {
			q.setParameter("extend", data.getExtend());
		}  	
		if ( data.getOnlyShip() != null && !"".equals(data.getOnlyShip()) ) {
			q.setParameter("onlyShip", data.getOnlyShip()+"%");
		}
		if ( data.getCreatorId() != null && !"".equals(data.getCreatorId()) ) {
			q.setParameter("creatorId", data.getCreatorId()+"%");
		}
		if ( data.getModifierId() != null && !"".equals(data.getModifierId()) ) {
			q.setParameter("modifierId", data.getModifierId()+"%");
		}
		if ( data.getModel() != null && !"".equals(data.getModel()) ) {
			q.setParameter("model", data.getModel()+"%");
		}
		if ( data.getCreateDate() != null && !"".equals(data.getCreateDate()) ) {
			q.setParameter("createDate", data.getCreateDate()+"%");
		}
		if ( data.getModifyDate() != null && !"".equals(data.getModifyDate()) ) {
			q.setParameter("modifyDate", data.getModifyDate()+"%");
		}
		if ( data.getStartDate() != null && !"".equals(data.getStartDate()) ) {
			q.setParameter("startDate", data.getStartDate()+"%");
		}
		if ( data.getStatus() != null && !"".equals(data.getStatus()) ) {
			q.setParameter("status", data.getStatus()+"%");
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