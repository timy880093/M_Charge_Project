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
 * This class provides methods to populate DB Table of PackageMode
 */
@Repository("packageMode")
public class JpaPackageModeDaoImpl extends JpaGenericDaoImpl<PackageModeEntity,         java.lang.Integer          >  implements PackageModeDao{

	/**
	 *
	 */
	public JpaPackageModeDaoImpl(){
	}

	/**
	 *
	 */ 
	protected final Logger logger = LogManager.getLogger(getClass());

    /**
     * Delete a record in Database.
	 * @param packageId   PK 
    */
	public void delete(
														 java.lang.Integer packageId 
																			) {
		logger.debug("JpaPackageModeDaoImpl delete  begin "
			+"id="+packageId
		);	
		try {
			Object data = entityManager.find(PackageModeEntity.class
			, packageId
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
	public List<PackageModeEntity> getSome(int pageNo, int noRowsPerPage)
		{
		List<PackageModeEntity> results = new ArrayList();
		Query q;
		try {
			q = entityManager.createQuery("select obj from PackageModeEntity obj ");
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
	public List<PackageModeEntity> searchByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		final String queryString = "select model from PackageModeEntity model where model."
					+ propertyName + "= :propertyValue";
		logger.debug("JpaPackageModeDaoImpl findByProperty   queryString :   "+queryString);
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
	public List<PackageModeEntity> searchBy(PackageModeEntity data) {
		StringBuffer sb = new StringBuffer("select obj from PackageModeEntity obj where 1=1 ");

		if ( data.getAdditionId() != null ) {
			sb.append(" AND obj.additionId = :additionId "); //java.lang.Integer
		}
		if ( data.getBroker2() != null ) {
			sb.append(" AND obj.broker2 = :broker2 "); //java.lang.String
		}
		if ( data.getBrokerCp3() != null ) {
			sb.append(" AND obj.brokerCp3 = :brokerCp3 "); //java.lang.String
		}
		if ( data.getDealerCompanyId() != null ) {
			sb.append(" AND obj.dealerCompanyId = :dealerCompanyId "); //java.lang.Integer
		}
		if ( data.getBroker3() != null ) {
			sb.append(" AND obj.broker3 = :broker3 "); //java.lang.String
		}
		if ( data.getCompanyId() != null ) {
			sb.append(" AND obj.companyId = :companyId "); //java.lang.Integer
		}
		if ( data.getPackageId() != null ) {
			sb.append(" AND obj.packageId = :packageId "); //java.lang.Integer
		}
		if ( data.getPackageType() != null ) {
			sb.append(" AND obj.packageType = :packageType "); //java.lang.Integer
		}
		if ( data.getChargeId() != null ) {
			sb.append(" AND obj.chargeId = :chargeId "); //java.lang.Integer
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
		if ( data.getDealerId() != null ) {
			sb.append(" AND obj.dealerId = :dealerId "); //java.lang.Integer
		}
		if ( data.getStatus() != null ) {
			sb.append(" AND obj.status = :status "); //java.lang.String
		}
		if ( data.getBrokerCp2() != null ) {
			sb.append(" AND obj.brokerCp2 = :brokerCp2 "); //java.lang.String
		}
		Query q;
		List<PackageModeEntity> results;
		try {
			logger.debug("JpaPackageModeDaoImpl searchBy  query : "+sb.toString());
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
		if ( data.getAdditionId() != null ) {
			q.setParameter("additionId", data.getAdditionId());
		}
		if ( data.getBroker2() != null ) {
			q.setParameter("broker2", data.getBroker2());
		}
		if ( data.getBrokerCp3() != null ) {
			q.setParameter("brokerCp3", data.getBrokerCp3());
		}
		if ( data.getDealerCompanyId() != null ) {
			q.setParameter("dealerCompanyId", data.getDealerCompanyId());
		}
		if ( data.getBroker3() != null ) {
			q.setParameter("broker3", data.getBroker3());
		}
		if ( data.getCompanyId() != null ) {
			q.setParameter("companyId", data.getCompanyId());
		}
		if ( data.getPackageId() != null ) {
			q.setParameter("packageId", data.getPackageId());
		}
		if ( data.getPackageType() != null ) {
			q.setParameter("packageType", data.getPackageType());
		}
		if ( data.getChargeId() != null ) {
			q.setParameter("chargeId", data.getChargeId());
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
		if ( data.getDealerId() != null ) {
			q.setParameter("dealerId", data.getDealerId());
		}
		if ( data.getStatus() != null ) {
			q.setParameter("status", data.getStatus());
		}
		if ( data.getBrokerCp2() != null ) {
			q.setParameter("brokerCp2", data.getBrokerCp2());
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
	public List<PackageModeEntity> searchLike(PackageModeEntity data) {
		StringBuffer sb = new StringBuffer("select obj from PackageModeEntity obj where 1=1 ");
		if ( data.getAdditionId() != null && !"".equals(data.getAdditionId())) {
			sb.append(" AND obj.additionId Like :additionId "); //java.lang.Integer
		}
		if ( data.getBroker2() != null && !"".equals(data.getBroker2())) {
			sb.append(" AND obj.broker2 Like :broker2 "); //java.lang.String
		}
		if ( data.getBrokerCp3() != null && !"".equals(data.getBrokerCp3())) {
			sb.append(" AND obj.brokerCp3 Like :brokerCp3 "); //java.lang.String
		}
		if ( data.getDealerCompanyId() != null && !"".equals(data.getDealerCompanyId())) {
			sb.append(" AND obj.dealerCompanyId Like :dealerCompanyId "); //java.lang.Integer
		}
		if ( data.getBroker3() != null && !"".equals(data.getBroker3())) {
			sb.append(" AND obj.broker3 Like :broker3 "); //java.lang.String
		}
		if ( data.getCompanyId() != null && !"".equals(data.getCompanyId())) {
			sb.append(" AND obj.companyId Like :companyId "); //java.lang.Integer
		}
		if ( data.getPackageId() != null && !"".equals(data.getPackageId())) {
			sb.append(" AND obj.packageId Like :packageId "); //java.lang.Integer
		}
		if ( data.getPackageType() != null && !"".equals(data.getPackageType())) {
			sb.append(" AND obj.packageType Like :packageType "); //java.lang.Integer
		}
		if ( data.getChargeId() != null && !"".equals(data.getChargeId())) {
			sb.append(" AND obj.chargeId Like :chargeId "); //java.lang.Integer
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
		if ( data.getDealerId() != null && !"".equals(data.getDealerId())) {
			sb.append(" AND obj.dealerId Like :dealerId "); //java.lang.Integer
		}
		if ( data.getStatus() != null && !"".equals(data.getStatus())) {
			sb.append(" AND obj.status Like :status "); //java.lang.String
		}
		if ( data.getBrokerCp2() != null && !"".equals(data.getBrokerCp2())) {
			sb.append(" AND obj.brokerCp2 Like :brokerCp2 "); //java.lang.String
		}
		Query q;
		List<PackageModeEntity> results;
		try {
			logger.debug("JpaPackageModeDaoImpl searchLike  query : "+sb.toString());
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
		if ( data.getAdditionId() != null && !"".equals(data.getAdditionId()) ) {
			q.setParameter("additionId", data.getAdditionId()+"%");
		}
		if ( data.getBroker2() != null && !"".equals(data.getBroker2()) ) {
			q.setParameter("broker2", data.getBroker2()+"%");
		}
		if ( data.getBrokerCp3() != null && !"".equals(data.getBrokerCp3()) ) {
			q.setParameter("brokerCp3", data.getBrokerCp3()+"%");
		}
		if ( data.getDealerCompanyId() != null && !"".equals(data.getDealerCompanyId()) ) {
			q.setParameter("dealerCompanyId", data.getDealerCompanyId()+"%");
		}
		if ( data.getBroker3() != null && !"".equals(data.getBroker3()) ) {
			q.setParameter("broker3", data.getBroker3()+"%");
		}
		if ( data.getCompanyId() != null && !"".equals(data.getCompanyId()) ) {
			q.setParameter("companyId", data.getCompanyId()+"%");
		}
		if ( data.getPackageId() != null && !"".equals(data.getPackageId()) ) {
			q.setParameter("packageId", data.getPackageId()+"%");
		}
		if ( data.getPackageType() != null && !"".equals(data.getPackageType()) ) {
			q.setParameter("packageType", data.getPackageType()+"%");
		}
		if ( data.getChargeId() != null && !"".equals(data.getChargeId()) ) {
			q.setParameter("chargeId", data.getChargeId()+"%");
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
		if ( data.getDealerId() != null && !"".equals(data.getDealerId()) ) {
			q.setParameter("dealerId", data.getDealerId()+"%");
		}
		if ( data.getStatus() != null && !"".equals(data.getStatus()) ) {
			q.setParameter("status", data.getStatus()+"%");
		}
		if ( data.getBrokerCp2() != null && !"".equals(data.getBrokerCp2()) ) {
			q.setParameter("brokerCp2", data.getBrokerCp2()+"%");
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
