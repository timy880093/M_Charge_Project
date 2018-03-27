/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:15 CST 2018
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
 * This class provides methods to populate DB Table of Printer
 */
@Repository("printer")
public class JpaPrinterDaoImpl extends JpaGenericDaoImpl<PrinterEntity,           java.lang.Integer      >  implements PrinterDao{

	/**
	 *
	 */
	public JpaPrinterDaoImpl(){
	}

	/**
	 *
	 */ 
	protected final Logger logger = LogManager.getLogger(getClass());

    /**
     * Delete a record in Database.
	 * @param printerId   PK 
    */
	public void delete(
																		 java.lang.Integer printerId 
											) {
		logger.debug("JpaPrinterDaoImpl delete  begin "
			+"id="+printerId
		);	
		try {
			Object data = entityManager.find(PrinterEntity.class
			, printerId
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
	public List<PrinterEntity> getSome(int pageNo, int noRowsPerPage)
		{
		List<PrinterEntity> results = new ArrayList();
		Query q;
		try {
			q = entityManager.createQuery("select obj from PrinterEntity obj ");
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
	public List<PrinterEntity> searchByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		final String queryString = "select model from PrinterEntity model where model."
					+ propertyName + "= :propertyValue";
		logger.debug("JpaPrinterDaoImpl findByProperty   queryString :   "+queryString);
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
	public List<PrinterEntity> searchBy(PrinterEntity data) {
		StringBuffer sb = new StringBuffer("select obj from PrinterEntity obj where 1=1 ");

		if ( data.getCompanyType() != null ) {
			sb.append(" AND obj.companyType = :companyType "); //java.lang.Integer
		}
		if ( data.getCompanyId() != null ) {
			sb.append(" AND obj.companyId = :companyId "); //java.lang.Integer
		}
		if ( data.getComPort() != null ) {
			sb.append(" AND obj.comPort = :comPort "); //java.lang.Integer
		}
		if ( data.getTakeNumber() != null ) {
			sb.append(" AND obj.takeNumber = :takeNumber "); //java.lang.Integer
		}
		if ( data.getPrinterType() != null ) {
			sb.append(" AND obj.printerType = :printerType "); //java.lang.Integer
		}
		if ( data.getEncryption() != null ) {
			sb.append(" AND obj.encryption is :encryption "); //java.lang.Boolean
		}
		if ( data.getCreatorId() != null ) {
			sb.append(" AND obj.creatorId = :creatorId "); //java.lang.Integer
		}
		if ( data.getModifierId() != null ) {
			sb.append(" AND obj.modifierId = :modifierId "); //java.lang.Integer
		}
		if ( data.getPrinterId() != null ) {
			sb.append(" AND obj.printerId = :printerId "); //java.lang.Integer
		}
		if ( data.getPrinterNo() != null ) {
			sb.append(" AND obj.printerNo = :printerNo "); //java.lang.String
		}
		if ( data.getCreateDate() != null ) {
			sb.append(" AND obj.createDate = :createDate "); //java.sql.Timestamp
		}
		if ( data.getClose() != null ) {
			sb.append(" AND obj.close is :close "); //java.lang.Boolean
		}
		if ( data.getModifyDate() != null ) {
			sb.append(" AND obj.modifyDate = :modifyDate "); //java.sql.Timestamp
		}
		if ( data.getKey() != null ) {
			sb.append(" AND obj.key = :key "); //java.lang.String
		}
		Query q;
		List<PrinterEntity> results;
		try {
			logger.debug("JpaPrinterDaoImpl searchBy  query : "+sb.toString());
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
		if ( data.getCompanyType() != null ) {
			q.setParameter("companyType", data.getCompanyType());
		}
		if ( data.getCompanyId() != null ) {
			q.setParameter("companyId", data.getCompanyId());
		}
		if ( data.getComPort() != null ) {
			q.setParameter("comPort", data.getComPort());
		}
		if ( data.getTakeNumber() != null ) {
			q.setParameter("takeNumber", data.getTakeNumber());
		}
		if ( data.getPrinterType() != null ) {
			q.setParameter("printerType", data.getPrinterType());
		}
		if ( data.getEncryption() != null ) {
			q.setParameter("encryption", data.getEncryption());
		}
		if ( data.getCreatorId() != null ) {
			q.setParameter("creatorId", data.getCreatorId());
		}
		if ( data.getModifierId() != null ) {
			q.setParameter("modifierId", data.getModifierId());
		}
		if ( data.getPrinterId() != null ) {
			q.setParameter("printerId", data.getPrinterId());
		}
		if ( data.getPrinterNo() != null ) {
			q.setParameter("printerNo", data.getPrinterNo());
		}
		if ( data.getCreateDate() != null ) {
			q.setParameter("createDate", data.getCreateDate());
		}
		if ( data.getClose() != null ) {
			q.setParameter("close", data.getClose());
		}
		if ( data.getModifyDate() != null ) {
			q.setParameter("modifyDate", data.getModifyDate());
		}
		if ( data.getKey() != null ) {
			q.setParameter("key", data.getKey());
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
	public List<PrinterEntity> searchLike(PrinterEntity data) {
		StringBuffer sb = new StringBuffer("select obj from PrinterEntity obj where 1=1 ");
		if ( data.getCompanyType() != null && !"".equals(data.getCompanyType())) {
			sb.append(" AND obj.companyType Like :companyType "); //java.lang.Integer
		}
		if ( data.getCompanyId() != null && !"".equals(data.getCompanyId())) {
			sb.append(" AND obj.companyId Like :companyId "); //java.lang.Integer
		}
		if ( data.getComPort() != null && !"".equals(data.getComPort())) {
			sb.append(" AND obj.comPort Like :comPort "); //java.lang.Integer
		}
		if ( data.getTakeNumber() != null && !"".equals(data.getTakeNumber())) {
			sb.append(" AND obj.takeNumber Like :takeNumber "); //java.lang.Integer
		}
		if ( data.getPrinterType() != null && !"".equals(data.getPrinterType())) {
			sb.append(" AND obj.printerType Like :printerType "); //java.lang.Integer
		}
		if ( data.getEncryption() != null ) {
			sb.append(" AND obj.encryption is :encryption "); //java.lang.Boolean
		}
		if ( data.getCreatorId() != null && !"".equals(data.getCreatorId())) {
			sb.append(" AND obj.creatorId Like :creatorId "); //java.lang.Integer
		}
		if ( data.getModifierId() != null && !"".equals(data.getModifierId())) {
			sb.append(" AND obj.modifierId Like :modifierId "); //java.lang.Integer
		}
		if ( data.getPrinterId() != null && !"".equals(data.getPrinterId())) {
			sb.append(" AND obj.printerId Like :printerId "); //java.lang.Integer
		}
		if ( data.getPrinterNo() != null && !"".equals(data.getPrinterNo())) {
			sb.append(" AND obj.printerNo Like :printerNo "); //java.lang.String
		}
		if ( data.getCreateDate() != null && !"".equals(data.getCreateDate())) {
			sb.append(" AND obj.createDate Like :createDate "); //java.sql.Timestamp
		}
		if ( data.getClose() != null ) {
			sb.append(" AND obj.close is :close "); //java.lang.Boolean
		}
		if ( data.getModifyDate() != null && !"".equals(data.getModifyDate())) {
			sb.append(" AND obj.modifyDate Like :modifyDate "); //java.sql.Timestamp
		}
		if ( data.getKey() != null && !"".equals(data.getKey())) {
			sb.append(" AND obj.key Like :key "); //java.lang.String
		}
		Query q;
		List<PrinterEntity> results;
		try {
			logger.debug("JpaPrinterDaoImpl searchLike  query : "+sb.toString());
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
		if ( data.getCompanyType() != null && !"".equals(data.getCompanyType()) ) {
			q.setParameter("companyType", data.getCompanyType()+"%");
		}
		if ( data.getCompanyId() != null && !"".equals(data.getCompanyId()) ) {
			q.setParameter("companyId", data.getCompanyId()+"%");
		}
		if ( data.getComPort() != null && !"".equals(data.getComPort()) ) {
			q.setParameter("comPort", data.getComPort()+"%");
		}
		if ( data.getTakeNumber() != null && !"".equals(data.getTakeNumber()) ) {
			q.setParameter("takeNumber", data.getTakeNumber()+"%");
		}
		if ( data.getPrinterType() != null && !"".equals(data.getPrinterType()) ) {
			q.setParameter("printerType", data.getPrinterType()+"%");
		}
		if ( data.getEncryption() != null ) {
			q.setParameter("encryption", data.getEncryption());
		}  	
		if ( data.getCreatorId() != null && !"".equals(data.getCreatorId()) ) {
			q.setParameter("creatorId", data.getCreatorId()+"%");
		}
		if ( data.getModifierId() != null && !"".equals(data.getModifierId()) ) {
			q.setParameter("modifierId", data.getModifierId()+"%");
		}
		if ( data.getPrinterId() != null && !"".equals(data.getPrinterId()) ) {
			q.setParameter("printerId", data.getPrinterId()+"%");
		}
		if ( data.getPrinterNo() != null && !"".equals(data.getPrinterNo()) ) {
			q.setParameter("printerNo", data.getPrinterNo()+"%");
		}
		if ( data.getCreateDate() != null && !"".equals(data.getCreateDate()) ) {
			q.setParameter("createDate", data.getCreateDate()+"%");
		}
		if ( data.getClose() != null ) {
			q.setParameter("close", data.getClose());
		}  	
		if ( data.getModifyDate() != null && !"".equals(data.getModifyDate()) ) {
			q.setParameter("modifyDate", data.getModifyDate()+"%");
		}
		if ( data.getKey() != null && !"".equals(data.getKey()) ) {
			q.setParameter("key", data.getKey()+"%");
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
