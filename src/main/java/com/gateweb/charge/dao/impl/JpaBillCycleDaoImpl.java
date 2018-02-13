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
 * This class provides methods to populate DB Table of BillCycle
 */
@Repository("billCycle")
public class JpaBillCycleDaoImpl extends JpaGenericDaoImpl<BillCycleEntity,              java.lang.Integer              >  implements BillCycleDao{

	/**
	 *
	 */
	public JpaBillCycleDaoImpl(){
	}

	/**
	 *
	 */ 
	protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Delete a record in Database.
	 * @param billId   PK 
    */
	public void delete(
																								 java.lang.Integer billId 
																											) {
		log.debug("JpaBillCycleDaoImpl delete  begin "
			+"id="+billId
		);	
		try {
			Object data = entityManager.find(BillCycleEntity.class
			, billId
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
	public List<BillCycleEntity> getSome(int pageNo, int noRowsPerPage)
		{
		List<BillCycleEntity> results = new ArrayList();
		Query q;
		try {
			q = entityManager.createQuery("select obj from BillCycleEntity obj ");
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
	public List<BillCycleEntity> searchByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		final String queryString = "select model from BillCycleEntity model where model."
					+ propertyName + "= :propertyValue";
		log.debug("JpaBillCycleDaoImpl findByProperty   queryString :   "+queryString);
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
	public List<BillCycleEntity> searchBy(BillCycleEntity data) {
		StringBuffer sb = new StringBuffer("select obj from BillCycleEntity obj where 1=1 ");

		if ( data.getCntOver() != null ) {
			sb.append(" AND obj.cntOver = :cntOver "); //java.lang.Integer
		}
		if ( data.getPackageId() != null ) {
			sb.append(" AND obj.packageId = :packageId "); //java.lang.Integer
		}
		if ( data.getCashInOverId() != null ) {
			sb.append(" AND obj.cashInOverId = :cashInOverId "); //java.lang.Integer
		}
		if ( data.getPayMonth() != null ) {
			sb.append(" AND obj.payMonth = :payMonth "); //java.math.BigDecimal
		}
		if ( data.getPriceOver() != null ) {
			sb.append(" AND obj.priceOver = :priceOver "); //java.math.BigDecimal
		}
		if ( data.getSinglePrice() != null ) {
			sb.append(" AND obj.singlePrice = :singlePrice "); //java.math.BigDecimal
		}
		if ( data.getPrice() != null ) {
			sb.append(" AND obj.price = :price "); //java.math.BigDecimal
		}
		if ( data.getBillType() != null ) {
			sb.append(" AND obj.billType = :billType "); //java.lang.Integer
		}
		if ( data.getCntGift() != null ) {
			sb.append(" AND obj.cntGift = :cntGift "); //java.lang.Integer
		}
		if ( data.getCashInMonthId() != null ) {
			sb.append(" AND obj.cashInMonthId = :cashInMonthId "); //java.lang.Integer
		}
		if ( data.getCreateDate() != null ) {
			sb.append(" AND obj.createDate = :createDate "); //java.sql.Timestamp
		}
		if ( data.getBillId() != null ) {
			sb.append(" AND obj.billId = :billId "); //java.lang.Integer
		}
		if ( data.getYearMonth() != null ) {
			sb.append(" AND obj.yearMonth = :yearMonth "); //java.lang.String
		}
		if ( data.getPayOver() != null ) {
			sb.append(" AND obj.payOver = :payOver "); //java.math.BigDecimal
		}
		if ( data.getCashOutMonthId() != null ) {
			sb.append(" AND obj.cashOutMonthId = :cashOutMonthId "); //java.lang.Integer
		}
		if ( data.getCompanyId() != null ) {
			sb.append(" AND obj.companyId = :companyId "); //java.lang.Integer
		}
		if ( data.getCnt() != null ) {
			sb.append(" AND obj.cnt = :cnt "); //java.lang.Integer
		}
		if ( data.getCntLimit() != null ) {
			sb.append(" AND obj.cntLimit = :cntLimit "); //java.lang.Integer
		}
		if ( data.getModifierId() != null ) {
			sb.append(" AND obj.modifierId = :modifierId "); //java.lang.Integer
		}
		if ( data.getCreatorId() != null ) {
			sb.append(" AND obj.creatorId = :creatorId "); //java.lang.Integer
		}
		if ( data.getIsPriceFree() != null ) {
			sb.append(" AND obj.isPriceFree = :isPriceFree "); //java.lang.String
		}
		if ( data.getCashOutOverId() != null ) {
			sb.append(" AND obj.cashOutOverId = :cashOutOverId "); //java.lang.Integer
		}
		if ( data.getModifyDate() != null ) {
			sb.append(" AND obj.modifyDate = :modifyDate "); //java.sql.Timestamp
		}
		if ( data.getPriceMax() != null ) {
			sb.append(" AND obj.priceMax = :priceMax "); //java.math.BigDecimal
		}
		if ( data.getStatus() != null ) {
			sb.append(" AND obj.status = :status "); //java.lang.String
		}
		Query q;
		List<BillCycleEntity> results;
		try {
			log.debug("JpaBillCycleDaoImpl searchBy  query : "+sb.toString());
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
		if ( data.getCntOver() != null ) {
			q.setParameter("cntOver", data.getCntOver());
		}
		if ( data.getPackageId() != null ) {
			q.setParameter("packageId", data.getPackageId());
		}
		if ( data.getCashInOverId() != null ) {
			q.setParameter("cashInOverId", data.getCashInOverId());
		}
		if ( data.getPayMonth() != null ) {
			q.setParameter("payMonth", data.getPayMonth());
		}
		if ( data.getPriceOver() != null ) {
			q.setParameter("priceOver", data.getPriceOver());
		}
		if ( data.getSinglePrice() != null ) {
			q.setParameter("singlePrice", data.getSinglePrice());
		}
		if ( data.getPrice() != null ) {
			q.setParameter("price", data.getPrice());
		}
		if ( data.getBillType() != null ) {
			q.setParameter("billType", data.getBillType());
		}
		if ( data.getCntGift() != null ) {
			q.setParameter("cntGift", data.getCntGift());
		}
		if ( data.getCashInMonthId() != null ) {
			q.setParameter("cashInMonthId", data.getCashInMonthId());
		}
		if ( data.getCreateDate() != null ) {
			q.setParameter("createDate", data.getCreateDate());
		}
		if ( data.getBillId() != null ) {
			q.setParameter("billId", data.getBillId());
		}
		if ( data.getYearMonth() != null ) {
			q.setParameter("yearMonth", data.getYearMonth());
		}
		if ( data.getPayOver() != null ) {
			q.setParameter("payOver", data.getPayOver());
		}
		if ( data.getCashOutMonthId() != null ) {
			q.setParameter("cashOutMonthId", data.getCashOutMonthId());
		}
		if ( data.getCompanyId() != null ) {
			q.setParameter("companyId", data.getCompanyId());
		}
		if ( data.getCnt() != null ) {
			q.setParameter("cnt", data.getCnt());
		}
		if ( data.getCntLimit() != null ) {
			q.setParameter("cntLimit", data.getCntLimit());
		}
		if ( data.getModifierId() != null ) {
			q.setParameter("modifierId", data.getModifierId());
		}
		if ( data.getCreatorId() != null ) {
			q.setParameter("creatorId", data.getCreatorId());
		}
		if ( data.getIsPriceFree() != null ) {
			q.setParameter("isPriceFree", data.getIsPriceFree());
		}
		if ( data.getCashOutOverId() != null ) {
			q.setParameter("cashOutOverId", data.getCashOutOverId());
		}
		if ( data.getModifyDate() != null ) {
			q.setParameter("modifyDate", data.getModifyDate());
		}
		if ( data.getPriceMax() != null ) {
			q.setParameter("priceMax", data.getPriceMax());
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
	public List<BillCycleEntity> searchLike(BillCycleEntity data) {
		StringBuffer sb = new StringBuffer("select obj from BillCycleEntity obj where 1=1 ");
		if ( data.getCntOver() != null && !"".equals(data.getCntOver())) {
			sb.append(" AND obj.cntOver Like :cntOver "); //java.lang.Integer
		}
		if ( data.getPackageId() != null && !"".equals(data.getPackageId())) {
			sb.append(" AND obj.packageId Like :packageId "); //java.lang.Integer
		}
		if ( data.getCashInOverId() != null && !"".equals(data.getCashInOverId())) {
			sb.append(" AND obj.cashInOverId Like :cashInOverId "); //java.lang.Integer
		}
		if ( data.getPayMonth() != null ) {
			sb.append(" AND obj.payMonth >= :payMonth "); // java.math.BigDecimal
		}
		if ( data.getPriceOver() != null ) {
			sb.append(" AND obj.priceOver >= :priceOver "); // java.math.BigDecimal
		}
		if ( data.getSinglePrice() != null ) {
			sb.append(" AND obj.singlePrice >= :singlePrice "); // java.math.BigDecimal
		}
		if ( data.getPrice() != null ) {
			sb.append(" AND obj.price >= :price "); // java.math.BigDecimal
		}
		if ( data.getBillType() != null && !"".equals(data.getBillType())) {
			sb.append(" AND obj.billType Like :billType "); //java.lang.Integer
		}
		if ( data.getCntGift() != null && !"".equals(data.getCntGift())) {
			sb.append(" AND obj.cntGift Like :cntGift "); //java.lang.Integer
		}
		if ( data.getCashInMonthId() != null && !"".equals(data.getCashInMonthId())) {
			sb.append(" AND obj.cashInMonthId Like :cashInMonthId "); //java.lang.Integer
		}
		if ( data.getCreateDate() != null && !"".equals(data.getCreateDate())) {
			sb.append(" AND obj.createDate Like :createDate "); //java.sql.Timestamp
		}
		if ( data.getBillId() != null && !"".equals(data.getBillId())) {
			sb.append(" AND obj.billId Like :billId "); //java.lang.Integer
		}
		if ( data.getYearMonth() != null && !"".equals(data.getYearMonth())) {
			sb.append(" AND obj.yearMonth Like :yearMonth "); //java.lang.String
		}
		if ( data.getPayOver() != null ) {
			sb.append(" AND obj.payOver >= :payOver "); // java.math.BigDecimal
		}
		if ( data.getCashOutMonthId() != null && !"".equals(data.getCashOutMonthId())) {
			sb.append(" AND obj.cashOutMonthId Like :cashOutMonthId "); //java.lang.Integer
		}
		if ( data.getCompanyId() != null && !"".equals(data.getCompanyId())) {
			sb.append(" AND obj.companyId Like :companyId "); //java.lang.Integer
		}
		if ( data.getCnt() != null && !"".equals(data.getCnt())) {
			sb.append(" AND obj.cnt Like :cnt "); //java.lang.Integer
		}
		if ( data.getCntLimit() != null && !"".equals(data.getCntLimit())) {
			sb.append(" AND obj.cntLimit Like :cntLimit "); //java.lang.Integer
		}
		if ( data.getModifierId() != null && !"".equals(data.getModifierId())) {
			sb.append(" AND obj.modifierId Like :modifierId "); //java.lang.Integer
		}
		if ( data.getCreatorId() != null && !"".equals(data.getCreatorId())) {
			sb.append(" AND obj.creatorId Like :creatorId "); //java.lang.Integer
		}
		if ( data.getIsPriceFree() != null && !"".equals(data.getIsPriceFree())) {
			sb.append(" AND obj.isPriceFree Like :isPriceFree "); //java.lang.String
		}
		if ( data.getCashOutOverId() != null && !"".equals(data.getCashOutOverId())) {
			sb.append(" AND obj.cashOutOverId Like :cashOutOverId "); //java.lang.Integer
		}
		if ( data.getModifyDate() != null && !"".equals(data.getModifyDate())) {
			sb.append(" AND obj.modifyDate Like :modifyDate "); //java.sql.Timestamp
		}
		if ( data.getPriceMax() != null ) {
			sb.append(" AND obj.priceMax >= :priceMax "); // java.math.BigDecimal
		}
		if ( data.getStatus() != null && !"".equals(data.getStatus())) {
			sb.append(" AND obj.status Like :status "); //java.lang.String
		}
		Query q;
		List<BillCycleEntity> results;
		try {
			log.debug("JpaBillCycleDaoImpl searchLike  query : "+sb.toString());
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
		if ( data.getCntOver() != null && !"".equals(data.getCntOver()) ) {
			q.setParameter("cntOver", data.getCntOver()+"%");
		}
		if ( data.getPackageId() != null && !"".equals(data.getPackageId()) ) {
			q.setParameter("packageId", data.getPackageId()+"%");
		}
		if ( data.getCashInOverId() != null && !"".equals(data.getCashInOverId()) ) {
			q.setParameter("cashInOverId", data.getCashInOverId()+"%");
		}
		if ( data.getPayMonth() != null ) {
			q.setParameter("payMonth", data.getPayMonth());
		}  	
		if ( data.getPriceOver() != null ) {
			q.setParameter("priceOver", data.getPriceOver());
		}  	
		if ( data.getSinglePrice() != null ) {
			q.setParameter("singlePrice", data.getSinglePrice());
		}  	
		if ( data.getPrice() != null ) {
			q.setParameter("price", data.getPrice());
		}  	
		if ( data.getBillType() != null && !"".equals(data.getBillType()) ) {
			q.setParameter("billType", data.getBillType()+"%");
		}
		if ( data.getCntGift() != null && !"".equals(data.getCntGift()) ) {
			q.setParameter("cntGift", data.getCntGift()+"%");
		}
		if ( data.getCashInMonthId() != null && !"".equals(data.getCashInMonthId()) ) {
			q.setParameter("cashInMonthId", data.getCashInMonthId()+"%");
		}
		if ( data.getCreateDate() != null && !"".equals(data.getCreateDate()) ) {
			q.setParameter("createDate", data.getCreateDate()+"%");
		}
		if ( data.getBillId() != null && !"".equals(data.getBillId()) ) {
			q.setParameter("billId", data.getBillId()+"%");
		}
		if ( data.getYearMonth() != null && !"".equals(data.getYearMonth()) ) {
			q.setParameter("yearMonth", data.getYearMonth()+"%");
		}
		if ( data.getPayOver() != null ) {
			q.setParameter("payOver", data.getPayOver());
		}  	
		if ( data.getCashOutMonthId() != null && !"".equals(data.getCashOutMonthId()) ) {
			q.setParameter("cashOutMonthId", data.getCashOutMonthId()+"%");
		}
		if ( data.getCompanyId() != null && !"".equals(data.getCompanyId()) ) {
			q.setParameter("companyId", data.getCompanyId()+"%");
		}
		if ( data.getCnt() != null && !"".equals(data.getCnt()) ) {
			q.setParameter("cnt", data.getCnt()+"%");
		}
		if ( data.getCntLimit() != null && !"".equals(data.getCntLimit()) ) {
			q.setParameter("cntLimit", data.getCntLimit()+"%");
		}
		if ( data.getModifierId() != null && !"".equals(data.getModifierId()) ) {
			q.setParameter("modifierId", data.getModifierId()+"%");
		}
		if ( data.getCreatorId() != null && !"".equals(data.getCreatorId()) ) {
			q.setParameter("creatorId", data.getCreatorId()+"%");
		}
		if ( data.getIsPriceFree() != null && !"".equals(data.getIsPriceFree()) ) {
			q.setParameter("isPriceFree", data.getIsPriceFree()+"%");
		}
		if ( data.getCashOutOverId() != null && !"".equals(data.getCashOutOverId()) ) {
			q.setParameter("cashOutOverId", data.getCashOutOverId()+"%");
		}
		if ( data.getModifyDate() != null && !"".equals(data.getModifyDate()) ) {
			q.setParameter("modifyDate", data.getModifyDate()+"%");
		}
		if ( data.getPriceMax() != null ) {
			q.setParameter("priceMax", data.getPriceMax());
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