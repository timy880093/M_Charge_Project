/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:14 CST 2018
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
 * This class provides methods to populate DB Table of LogData
 */
@Repository("logData")
public class JpaLogDataDaoImpl extends JpaGenericDaoImpl<LogDataEntity,    java.lang.Integer      >  implements LogDataDao{

	/**
	 *
	 */
	public JpaLogDataDaoImpl(){
	}

	/**
	 *
	 */ 
	protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Delete a record in Database.
	 * @param logId   PK 
    */
	public void delete(
				 java.lang.Integer logId 
											) {
		log.debug("JpaLogDataDaoImpl delete  begin "
			+"id="+logId
		);	
		try {
			Object data = entityManager.find(LogDataEntity.class
			, logId
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
	public List<LogDataEntity> getSome(int pageNo, int noRowsPerPage)
		{
		List<LogDataEntity> results = new ArrayList();
		Query q;
		try {
			q = entityManager.createQuery("select obj from LogDataEntity obj ");
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
	public List<LogDataEntity> searchByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		final String queryString = "select model from LogDataEntity model where model."
					+ propertyName + "= :propertyValue";
		log.debug("JpaLogDataDaoImpl findByProperty   queryString :   "+queryString);
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
	public List<LogDataEntity> searchBy(LogDataEntity data) {
		StringBuffer sb = new StringBuffer("select obj from LogDataEntity obj where 1=1 ");

		if ( data.getSessionObj() != null ) {
			sb.append(" AND obj.sessionObj = :sessionObj "); //java.lang.String
		}
		if ( data.getLogId() != null ) {
			sb.append(" AND obj.logId = :logId "); //java.lang.Integer
		}
		if ( data.getUserName() != null ) {
			sb.append(" AND obj.userName = :userName "); //java.lang.String
		}
		if ( data.getRequestObj() != null ) {
			sb.append(" AND obj.requestObj = :requestObj "); //java.lang.String
		}
		if ( data.getLogUrl() != null ) {
			sb.append(" AND obj.logUrl = :logUrl "); //java.lang.String
		}
		if ( data.getParameterId() != null ) {
			sb.append(" AND obj.parameterId = :parameterId "); //java.lang.Integer
		}
		if ( data.getAccessTime() != null ) {
			sb.append(" AND obj.accessTime = :accessTime "); //java.sql.Timestamp
		}
		Query q;
		List<LogDataEntity> results;
		try {
			log.debug("JpaLogDataDaoImpl searchBy  query : "+sb.toString());
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
		if ( data.getSessionObj() != null ) {
			q.setParameter("sessionObj", data.getSessionObj());
		}
		if ( data.getLogId() != null ) {
			q.setParameter("logId", data.getLogId());
		}
		if ( data.getUserName() != null ) {
			q.setParameter("userName", data.getUserName());
		}
		if ( data.getRequestObj() != null ) {
			q.setParameter("requestObj", data.getRequestObj());
		}
		if ( data.getLogUrl() != null ) {
			q.setParameter("logUrl", data.getLogUrl());
		}
		if ( data.getParameterId() != null ) {
			q.setParameter("parameterId", data.getParameterId());
		}
		if ( data.getAccessTime() != null ) {
			q.setParameter("accessTime", data.getAccessTime());
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
	public List<LogDataEntity> searchLike(LogDataEntity data) {
		StringBuffer sb = new StringBuffer("select obj from LogDataEntity obj where 1=1 ");
		if ( data.getSessionObj() != null && !"".equals(data.getSessionObj())) {
			sb.append(" AND obj.sessionObj Like :sessionObj "); //java.lang.String
		}
		if ( data.getLogId() != null && !"".equals(data.getLogId())) {
			sb.append(" AND obj.logId Like :logId "); //java.lang.Integer
		}
		if ( data.getUserName() != null && !"".equals(data.getUserName())) {
			sb.append(" AND obj.userName Like :userName "); //java.lang.String
		}
		if ( data.getRequestObj() != null && !"".equals(data.getRequestObj())) {
			sb.append(" AND obj.requestObj Like :requestObj "); //java.lang.String
		}
		if ( data.getLogUrl() != null && !"".equals(data.getLogUrl())) {
			sb.append(" AND obj.logUrl Like :logUrl "); //java.lang.String
		}
		if ( data.getParameterId() != null && !"".equals(data.getParameterId())) {
			sb.append(" AND obj.parameterId Like :parameterId "); //java.lang.Integer
		}
		if ( data.getAccessTime() != null && !"".equals(data.getAccessTime())) {
			sb.append(" AND obj.accessTime Like :accessTime "); //java.sql.Timestamp
		}
		Query q;
		List<LogDataEntity> results;
		try {
			log.debug("JpaLogDataDaoImpl searchLike  query : "+sb.toString());
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
		if ( data.getSessionObj() != null && !"".equals(data.getSessionObj()) ) {
			q.setParameter("sessionObj", data.getSessionObj()+"%");
		}
		if ( data.getLogId() != null && !"".equals(data.getLogId()) ) {
			q.setParameter("logId", data.getLogId()+"%");
		}
		if ( data.getUserName() != null && !"".equals(data.getUserName()) ) {
			q.setParameter("userName", data.getUserName()+"%");
		}
		if ( data.getRequestObj() != null && !"".equals(data.getRequestObj()) ) {
			q.setParameter("requestObj", data.getRequestObj()+"%");
		}
		if ( data.getLogUrl() != null && !"".equals(data.getLogUrl()) ) {
			q.setParameter("logUrl", data.getLogUrl()+"%");
		}
		if ( data.getParameterId() != null && !"".equals(data.getParameterId()) ) {
			q.setParameter("parameterId", data.getParameterId()+"%");
		}
		if ( data.getAccessTime() != null && !"".equals(data.getAccessTime()) ) {
			q.setParameter("accessTime", data.getAccessTime()+"%");
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