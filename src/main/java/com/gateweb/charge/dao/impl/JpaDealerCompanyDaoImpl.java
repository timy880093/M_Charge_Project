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
 * This class provides methods to populate DB Table of DealerCompany
 */
@Repository("dealerCompany")
public class JpaDealerCompanyDaoImpl extends JpaGenericDaoImpl<DealerCompanyEntity,    java.lang.Integer                >  implements DealerCompanyDao{

	/**
	 *
	 */
	public JpaDealerCompanyDaoImpl(){
	}

	/**
	 *
	 */ 
	protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Delete a record in Database.
	 * @param dealerCompanyId   PK 
    */
	public void delete(
				 java.lang.Integer dealerCompanyId 
																															) {
		log.debug("JpaDealerCompanyDaoImpl delete  begin "
			+"id="+dealerCompanyId
		);	
		try {
			Object data = entityManager.find(DealerCompanyEntity.class
			, dealerCompanyId
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
	public List<DealerCompanyEntity> getSome(int pageNo, int noRowsPerPage)
		{
		List<DealerCompanyEntity> results = new ArrayList();
		Query q;
		try {
			q = entityManager.createQuery("select obj from DealerCompanyEntity obj ");
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
	public List<DealerCompanyEntity> searchByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		final String queryString = "select model from DealerCompanyEntity model where model."
					+ propertyName + "= :propertyValue";
		log.debug("JpaDealerCompanyDaoImpl findByProperty   queryString :   "+queryString);
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
	public List<DealerCompanyEntity> searchBy(DealerCompanyEntity data) {
		StringBuffer sb = new StringBuffer("select obj from DealerCompanyEntity obj where 1=1 ");

		if ( data.getDealerCompanyName() != null ) {
			sb.append(" AND obj.dealerCompanyName = :dealerCompanyName "); //java.lang.String
		}
		if ( data.getDealerCompanyId() != null ) {
			sb.append(" AND obj.dealerCompanyId = :dealerCompanyId "); //java.lang.Integer
		}
		if ( data.getBusinessNo() != null ) {
			sb.append(" AND obj.businessNo = :businessNo "); //java.lang.String
		}
		if ( data.getCollectMoney() != null ) {
			sb.append(" AND obj.collectMoney = :collectMoney "); //java.math.BigDecimal
		}
		if ( data.getCompanyAddress() != null ) {
			sb.append(" AND obj.companyAddress = :companyAddress "); //java.lang.String
		}
		if ( data.getMainAmount() != null ) {
			sb.append(" AND obj.mainAmount = :mainAmount "); //java.math.BigDecimal
		}
		if ( data.getMainPercent() != null ) {
			sb.append(" AND obj.mainPercent = :mainPercent "); //java.math.BigDecimal
		}
		if ( data.getPhone() != null ) {
			sb.append(" AND obj.phone = :phone "); //java.lang.String
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
		if ( data.getFax() != null ) {
			sb.append(" AND obj.fax = :fax "); //java.lang.String
		}
		if ( data.getModifyDate() != null ) {
			sb.append(" AND obj.modifyDate = :modifyDate "); //java.sql.Timestamp
		}
		if ( data.getAdditionPercent() != null ) {
			sb.append(" AND obj.additionPercent = :additionPercent "); //java.math.BigDecimal
		}
		if ( data.getCommissionType() != null ) {
			sb.append(" AND obj.commissionType = :commissionType "); //java.lang.String
		}
		if ( data.getEmail() != null ) {
			sb.append(" AND obj.email = :email "); //java.lang.String
		}
		if ( data.getStatus() != null ) {
			sb.append(" AND obj.status = :status "); //java.lang.Integer
		}
		Query q;
		List<DealerCompanyEntity> results;
		try {
			log.debug("JpaDealerCompanyDaoImpl searchBy  query : "+sb.toString());
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
		if ( data.getDealerCompanyName() != null ) {
			q.setParameter("dealerCompanyName", data.getDealerCompanyName());
		}
		if ( data.getDealerCompanyId() != null ) {
			q.setParameter("dealerCompanyId", data.getDealerCompanyId());
		}
		if ( data.getBusinessNo() != null ) {
			q.setParameter("businessNo", data.getBusinessNo());
		}
		if ( data.getCollectMoney() != null ) {
			q.setParameter("collectMoney", data.getCollectMoney());
		}
		if ( data.getCompanyAddress() != null ) {
			q.setParameter("companyAddress", data.getCompanyAddress());
		}
		if ( data.getMainAmount() != null ) {
			q.setParameter("mainAmount", data.getMainAmount());
		}
		if ( data.getMainPercent() != null ) {
			q.setParameter("mainPercent", data.getMainPercent());
		}
		if ( data.getPhone() != null ) {
			q.setParameter("phone", data.getPhone());
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
		if ( data.getFax() != null ) {
			q.setParameter("fax", data.getFax());
		}
		if ( data.getModifyDate() != null ) {
			q.setParameter("modifyDate", data.getModifyDate());
		}
		if ( data.getAdditionPercent() != null ) {
			q.setParameter("additionPercent", data.getAdditionPercent());
		}
		if ( data.getCommissionType() != null ) {
			q.setParameter("commissionType", data.getCommissionType());
		}
		if ( data.getEmail() != null ) {
			q.setParameter("email", data.getEmail());
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
	public List<DealerCompanyEntity> searchLike(DealerCompanyEntity data) {
		StringBuffer sb = new StringBuffer("select obj from DealerCompanyEntity obj where 1=1 ");
		if ( data.getDealerCompanyName() != null && !"".equals(data.getDealerCompanyName())) {
			sb.append(" AND obj.dealerCompanyName Like :dealerCompanyName "); //java.lang.String
		}
		if ( data.getDealerCompanyId() != null && !"".equals(data.getDealerCompanyId())) {
			sb.append(" AND obj.dealerCompanyId Like :dealerCompanyId "); //java.lang.Integer
		}
		if ( data.getBusinessNo() != null && !"".equals(data.getBusinessNo())) {
			sb.append(" AND obj.businessNo Like :businessNo "); //java.lang.String
		}
		if ( data.getCollectMoney() != null ) {
			sb.append(" AND obj.collectMoney >= :collectMoney "); // java.math.BigDecimal
		}
		if ( data.getCompanyAddress() != null && !"".equals(data.getCompanyAddress())) {
			sb.append(" AND obj.companyAddress Like :companyAddress "); //java.lang.String
		}
		if ( data.getMainAmount() != null ) {
			sb.append(" AND obj.mainAmount >= :mainAmount "); // java.math.BigDecimal
		}
		if ( data.getMainPercent() != null ) {
			sb.append(" AND obj.mainPercent >= :mainPercent "); // java.math.BigDecimal
		}
		if ( data.getPhone() != null && !"".equals(data.getPhone())) {
			sb.append(" AND obj.phone Like :phone "); //java.lang.String
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
		if ( data.getFax() != null && !"".equals(data.getFax())) {
			sb.append(" AND obj.fax Like :fax "); //java.lang.String
		}
		if ( data.getModifyDate() != null && !"".equals(data.getModifyDate())) {
			sb.append(" AND obj.modifyDate Like :modifyDate "); //java.sql.Timestamp
		}
		if ( data.getAdditionPercent() != null ) {
			sb.append(" AND obj.additionPercent >= :additionPercent "); // java.math.BigDecimal
		}
		if ( data.getCommissionType() != null && !"".equals(data.getCommissionType())) {
			sb.append(" AND obj.commissionType Like :commissionType "); //java.lang.String
		}
		if ( data.getEmail() != null && !"".equals(data.getEmail())) {
			sb.append(" AND obj.email Like :email "); //java.lang.String
		}
		if ( data.getStatus() != null && !"".equals(data.getStatus())) {
			sb.append(" AND obj.status Like :status "); //java.lang.Integer
		}
		Query q;
		List<DealerCompanyEntity> results;
		try {
			log.debug("JpaDealerCompanyDaoImpl searchLike  query : "+sb.toString());
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
		if ( data.getDealerCompanyName() != null && !"".equals(data.getDealerCompanyName()) ) {
			q.setParameter("dealerCompanyName", data.getDealerCompanyName()+"%");
		}
		if ( data.getDealerCompanyId() != null && !"".equals(data.getDealerCompanyId()) ) {
			q.setParameter("dealerCompanyId", data.getDealerCompanyId()+"%");
		}
		if ( data.getBusinessNo() != null && !"".equals(data.getBusinessNo()) ) {
			q.setParameter("businessNo", data.getBusinessNo()+"%");
		}
		if ( data.getCollectMoney() != null ) {
			q.setParameter("collectMoney", data.getCollectMoney());
		}  	
		if ( data.getCompanyAddress() != null && !"".equals(data.getCompanyAddress()) ) {
			q.setParameter("companyAddress", data.getCompanyAddress()+"%");
		}
		if ( data.getMainAmount() != null ) {
			q.setParameter("mainAmount", data.getMainAmount());
		}  	
		if ( data.getMainPercent() != null ) {
			q.setParameter("mainPercent", data.getMainPercent());
		}  	
		if ( data.getPhone() != null && !"".equals(data.getPhone()) ) {
			q.setParameter("phone", data.getPhone()+"%");
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
		if ( data.getFax() != null && !"".equals(data.getFax()) ) {
			q.setParameter("fax", data.getFax()+"%");
		}
		if ( data.getModifyDate() != null && !"".equals(data.getModifyDate()) ) {
			q.setParameter("modifyDate", data.getModifyDate()+"%");
		}
		if ( data.getAdditionPercent() != null ) {
			q.setParameter("additionPercent", data.getAdditionPercent());
		}  	
		if ( data.getCommissionType() != null && !"".equals(data.getCommissionType()) ) {
			q.setParameter("commissionType", data.getCommissionType()+"%");
		}
		if ( data.getEmail() != null && !"".equals(data.getEmail()) ) {
			q.setParameter("email", data.getEmail()+"%");
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