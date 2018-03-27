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
 * This class provides methods to populate DB Table of Grade
 */
@Repository("grade")
public class JpaGradeDaoImpl extends JpaGenericDaoImpl<GradeEntity,   java.lang.Integer         >  implements GradeDao{

	/**
	 *
	 */
	public JpaGradeDaoImpl(){
	}

	/**
	 *
	 */ 
	protected final Logger logger = LogManager.getLogger(getClass());

    /**
     * Delete a record in Database.
	 * @param gradeId   PK 
    */
	public void delete(
		 java.lang.Integer gradeId 
																	) {
		logger.debug("JpaGradeDaoImpl delete  begin "
			+"id="+gradeId
		);	
		try {
			Object data = entityManager.find(GradeEntity.class
			, gradeId
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
	public List<GradeEntity> getSome(int pageNo, int noRowsPerPage)
		{
		List<GradeEntity> results = new ArrayList();
		Query q;
		try {
			q = entityManager.createQuery("select obj from GradeEntity obj ");
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
	public List<GradeEntity> searchByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		final String queryString = "select model from GradeEntity model where model."
					+ propertyName + "= :propertyValue";
		logger.debug("JpaGradeDaoImpl findByProperty   queryString :   "+queryString);
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
	public List<GradeEntity> searchBy(GradeEntity data) {
		StringBuffer sb = new StringBuffer("select obj from GradeEntity obj where 1=1 ");

		if ( data.getGradeId() != null ) {
			sb.append(" AND obj.gradeId = :gradeId "); //java.lang.Integer
		}
		if ( data.getChargeId() != null ) {
			sb.append(" AND obj.chargeId = :chargeId "); //java.lang.Integer
		}
		if ( data.getPrice() != null ) {
			sb.append(" AND obj.price = :price "); //java.lang.Integer
		}
		if ( data.getCntEnd() != null ) {
			sb.append(" AND obj.cntEnd = :cntEnd "); //java.lang.Integer
		}
		if ( data.getCreatorId() != null ) {
			sb.append(" AND obj.creatorId = :creatorId "); //java.lang.Integer
		}
		if ( data.getModifierId() != null ) {
			sb.append(" AND obj.modifierId = :modifierId "); //java.lang.Integer
		}
		if ( data.getCreateDate() != null ) {
			sb.append(" AND obj.createDate = :createDate "); //java.sql.Timestamp
		}
		if ( data.getModifyDate() != null ) {
			sb.append(" AND obj.modifyDate = :modifyDate "); //java.sql.Timestamp
		}
		if ( data.getCntStart() != null ) {
			sb.append(" AND obj.cntStart = :cntStart "); //java.lang.Integer
		}
		Query q;
		List<GradeEntity> results;
		try {
			logger.debug("JpaGradeDaoImpl searchBy  query : "+sb.toString());
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
		if ( data.getGradeId() != null ) {
			q.setParameter("gradeId", data.getGradeId());
		}
		if ( data.getChargeId() != null ) {
			q.setParameter("chargeId", data.getChargeId());
		}
		if ( data.getPrice() != null ) {
			q.setParameter("price", data.getPrice());
		}
		if ( data.getCntEnd() != null ) {
			q.setParameter("cntEnd", data.getCntEnd());
		}
		if ( data.getCreatorId() != null ) {
			q.setParameter("creatorId", data.getCreatorId());
		}
		if ( data.getModifierId() != null ) {
			q.setParameter("modifierId", data.getModifierId());
		}
		if ( data.getCreateDate() != null ) {
			q.setParameter("createDate", data.getCreateDate());
		}
		if ( data.getModifyDate() != null ) {
			q.setParameter("modifyDate", data.getModifyDate());
		}
		if ( data.getCntStart() != null ) {
			q.setParameter("cntStart", data.getCntStart());
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
	public List<GradeEntity> searchLike(GradeEntity data) {
		StringBuffer sb = new StringBuffer("select obj from GradeEntity obj where 1=1 ");
		if ( data.getGradeId() != null && !"".equals(data.getGradeId())) {
			sb.append(" AND obj.gradeId Like :gradeId "); //java.lang.Integer
		}
		if ( data.getChargeId() != null && !"".equals(data.getChargeId())) {
			sb.append(" AND obj.chargeId Like :chargeId "); //java.lang.Integer
		}
		if ( data.getPrice() != null && !"".equals(data.getPrice())) {
			sb.append(" AND obj.price Like :price "); //java.lang.Integer
		}
		if ( data.getCntEnd() != null && !"".equals(data.getCntEnd())) {
			sb.append(" AND obj.cntEnd Like :cntEnd "); //java.lang.Integer
		}
		if ( data.getCreatorId() != null && !"".equals(data.getCreatorId())) {
			sb.append(" AND obj.creatorId Like :creatorId "); //java.lang.Integer
		}
		if ( data.getModifierId() != null && !"".equals(data.getModifierId())) {
			sb.append(" AND obj.modifierId Like :modifierId "); //java.lang.Integer
		}
		if ( data.getCreateDate() != null && !"".equals(data.getCreateDate())) {
			sb.append(" AND obj.createDate Like :createDate "); //java.sql.Timestamp
		}
		if ( data.getModifyDate() != null && !"".equals(data.getModifyDate())) {
			sb.append(" AND obj.modifyDate Like :modifyDate "); //java.sql.Timestamp
		}
		if ( data.getCntStart() != null && !"".equals(data.getCntStart())) {
			sb.append(" AND obj.cntStart Like :cntStart "); //java.lang.Integer
		}
		Query q;
		List<GradeEntity> results;
		try {
			logger.debug("JpaGradeDaoImpl searchLike  query : "+sb.toString());
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
		if ( data.getGradeId() != null && !"".equals(data.getGradeId()) ) {
			q.setParameter("gradeId", data.getGradeId()+"%");
		}
		if ( data.getChargeId() != null && !"".equals(data.getChargeId()) ) {
			q.setParameter("chargeId", data.getChargeId()+"%");
		}
		if ( data.getPrice() != null && !"".equals(data.getPrice()) ) {
			q.setParameter("price", data.getPrice()+"%");
		}
		if ( data.getCntEnd() != null && !"".equals(data.getCntEnd()) ) {
			q.setParameter("cntEnd", data.getCntEnd()+"%");
		}
		if ( data.getCreatorId() != null && !"".equals(data.getCreatorId()) ) {
			q.setParameter("creatorId", data.getCreatorId()+"%");
		}
		if ( data.getModifierId() != null && !"".equals(data.getModifierId()) ) {
			q.setParameter("modifierId", data.getModifierId()+"%");
		}
		if ( data.getCreateDate() != null && !"".equals(data.getCreateDate()) ) {
			q.setParameter("createDate", data.getCreateDate()+"%");
		}
		if ( data.getModifyDate() != null && !"".equals(data.getModifyDate()) ) {
			q.setParameter("modifyDate", data.getModifyDate()+"%");
		}
		if ( data.getCntStart() != null && !"".equals(data.getCntStart()) ) {
			q.setParameter("cntStart", data.getCntStart()+"%");
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
