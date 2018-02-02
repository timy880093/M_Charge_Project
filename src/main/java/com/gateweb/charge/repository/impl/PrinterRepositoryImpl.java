/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:15 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.repository.impl; 
import com.gateweb.charge.repository.*; 
import com.gateweb.charge.model.*;    
import com.gateweb.charge.*;

import com.meshinnovation.db.dao.exception.DaoSystemException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Collection;
import javax.persistence.Query;
//import org.springframework.orm.ObjectRetrievalFailureException;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;


/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of Printer
 */
//@Repository("printerRepositoryCustom")
public class PrinterRepositoryImpl implements PrinterRepositoryCustom {
	
	/**
	 * <p>
	 * <code>Log</code> instance for this application.
	 * </p>
	 */
	protected final Log log = LogFactory.getLog(getClass());
	
	/*	@PersistenceContext
	private EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}*/

	@Autowired
	PrinterRepository printerRepository;
	
	public PrinterRepositoryImpl(){
	}
	

	@SuppressWarnings("unchecked")
	public List<PrinterEntity> searchWithVo(PrinterEntity vo) {
		log.debug("PrinterRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QPrinterEntity printerEntity = QPrinterEntity.printerEntity;

		if ( vo.getCompanyType() != null ) {
			builder.and(printerEntity.companyType.eq(vo.getCompanyType())); //java.lang.Integer
		}	
		if ( vo.getCompanyId() != null ) {
			builder.and(printerEntity.companyId.eq(vo.getCompanyId())); //java.lang.Integer
		}	
		if ( vo.getComPort() != null ) {
			builder.and(printerEntity.comPort.eq(vo.getComPort())); //java.lang.Integer
		}	
		if ( vo.getTakeNumber() != null ) {
			builder.and(printerEntity.takeNumber.eq(vo.getTakeNumber())); //java.lang.Integer
		}	
		if ( vo.getPrinterType() != null ) {
			builder.and(printerEntity.printerType.eq(vo.getPrinterType())); //java.lang.Integer
		}	
		if ( vo.getEncryption() != null ) {
			builder.and(printerEntity.encryption.eq(vo.getEncryption())); //java.lang.Boolean
		}	
		if ( vo.getCreatorId() != null ) {
			builder.and(printerEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(printerEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getPrinterId() != null ) {
			builder.and(printerEntity.printerId.eq(vo.getPrinterId())); //java.lang.Integer
		}	
		if ( vo.getPrinterNo() != null && !"".equals(vo.getPrinterNo())) {
			builder.and(printerEntity.printerNo.equalsIgnoreCase(vo.getPrinterNo())); //java.lang.String
		}
		if ( vo.getCreateDate() != null ) {
			builder.and(printerEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getClose() != null ) {
			builder.and(printerEntity.close.eq(vo.getClose())); //java.lang.Boolean
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(printerEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getKey() != null && !"".equals(vo.getKey())) {
			builder.and(printerEntity.key.equalsIgnoreCase(vo.getKey())); //java.lang.String
		}
		log.debug("PrinterRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(printerRepository.findAll(builder.getValue()));	
	}
	
	@SuppressWarnings("unchecked")
	public List<PrinterEntity> searchWithVo(PrinterEntity vo, Pageable pageable) {
		log.debug("PrinterRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QPrinterEntity printerEntity = QPrinterEntity.printerEntity;

		if ( vo.getCompanyType() != null ) {
			builder.and(printerEntity.companyType.eq(vo.getCompanyType())); //java.lang.Integer
		}	
		if ( vo.getCompanyId() != null ) {
			builder.and(printerEntity.companyId.eq(vo.getCompanyId())); //java.lang.Integer
		}	
		if ( vo.getComPort() != null ) {
			builder.and(printerEntity.comPort.eq(vo.getComPort())); //java.lang.Integer
		}	
		if ( vo.getTakeNumber() != null ) {
			builder.and(printerEntity.takeNumber.eq(vo.getTakeNumber())); //java.lang.Integer
		}	
		if ( vo.getPrinterType() != null ) {
			builder.and(printerEntity.printerType.eq(vo.getPrinterType())); //java.lang.Integer
		}	
		if ( vo.getEncryption() != null ) {
			builder.and(printerEntity.encryption.eq(vo.getEncryption())); //java.lang.Boolean
		}	
		if ( vo.getCreatorId() != null ) {
			builder.and(printerEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(printerEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getPrinterId() != null ) {
			builder.and(printerEntity.printerId.eq(vo.getPrinterId())); //java.lang.Integer
		}	
		if ( vo.getPrinterNo() != null && !"".equals(vo.getPrinterNo())) {
			builder.and(printerEntity.printerNo.equalsIgnoreCase(vo.getPrinterNo())); //java.lang.String
		}
		if ( vo.getCreateDate() != null ) {
			builder.and(printerEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getClose() != null ) {
			builder.and(printerEntity.close.eq(vo.getClose())); //java.lang.Boolean
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(printerEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getKey() != null && !"".equals(vo.getKey())) {
			builder.and(printerEntity.key.equalsIgnoreCase(vo.getKey())); //java.lang.String
		}
		log.debug("PrinterRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(printerRepository.findAll(builder.getValue(), pageable));	
	}
	
	@SuppressWarnings("unchecked")
	public List<PrinterEntity> searchWithVo(PrinterEntity vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchWithVo(vo, pageable);
	}
	
	@SuppressWarnings("unchecked")
	public List<PrinterEntity> searchLikeVo(PrinterEntity vo) {
		log.debug("PrinterRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QPrinterEntity printerEntity = QPrinterEntity.printerEntity;

		if ( vo.getCompanyType() != null ) {
			builder.and(printerEntity.companyType.eq(vo.getCompanyType())); //java.lang.Integer
		}	
		if ( vo.getCompanyId() != null ) {
			builder.and(printerEntity.companyId.eq(vo.getCompanyId())); //java.lang.Integer
		}	
		if ( vo.getComPort() != null ) {
			builder.and(printerEntity.comPort.eq(vo.getComPort())); //java.lang.Integer
		}	
		if ( vo.getTakeNumber() != null ) {
			builder.and(printerEntity.takeNumber.eq(vo.getTakeNumber())); //java.lang.Integer
		}	
		if ( vo.getPrinterType() != null ) {
			builder.and(printerEntity.printerType.eq(vo.getPrinterType())); //java.lang.Integer
		}	
		if ( vo.getEncryption() != null ) {
			builder.and(printerEntity.encryption.eq(vo.getEncryption())); //java.lang.Boolean
		}	
		if ( vo.getCreatorId() != null ) {
			builder.and(printerEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(printerEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getPrinterId() != null ) {
			builder.and(printerEntity.printerId.eq(vo.getPrinterId())); //java.lang.Integer
		}	
		if ( vo.getPrinterNo() != null && !"".equals(vo.getPrinterNo())) {
			builder.and(printerEntity.printerNo.containsIgnoreCase(vo.getPrinterNo())); //java.lang.String
		}
		if ( vo.getCreateDate() != null ) {
			builder.and(printerEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getClose() != null ) {
			builder.and(printerEntity.close.eq(vo.getClose())); //java.lang.Boolean
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(printerEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getKey() != null && !"".equals(vo.getKey())) {
			builder.and(printerEntity.key.containsIgnoreCase(vo.getKey())); //java.lang.String
		}
		log.debug("PrinterRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(printerRepository.findAll(builder.getValue()));
	}		
	
	

	@SuppressWarnings("unchecked")
	public List<PrinterEntity> searchLikeVo(PrinterEntity vo, Pageable pageable) {
		log.debug("PrinterRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QPrinterEntity printerEntity = QPrinterEntity.printerEntity;

		if ( vo.getCompanyType() != null ) {
			builder.and(printerEntity.companyType.eq(vo.getCompanyType())); //java.lang.Integer
		}	
		if ( vo.getCompanyId() != null ) {
			builder.and(printerEntity.companyId.eq(vo.getCompanyId())); //java.lang.Integer
		}	
		if ( vo.getComPort() != null ) {
			builder.and(printerEntity.comPort.eq(vo.getComPort())); //java.lang.Integer
		}	
		if ( vo.getTakeNumber() != null ) {
			builder.and(printerEntity.takeNumber.eq(vo.getTakeNumber())); //java.lang.Integer
		}	
		if ( vo.getPrinterType() != null ) {
			builder.and(printerEntity.printerType.eq(vo.getPrinterType())); //java.lang.Integer
		}	
		if ( vo.getEncryption() != null ) {
			builder.and(printerEntity.encryption.eq(vo.getEncryption())); //java.lang.Boolean
		}	
		if ( vo.getCreatorId() != null ) {
			builder.and(printerEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(printerEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getPrinterId() != null ) {
			builder.and(printerEntity.printerId.eq(vo.getPrinterId())); //java.lang.Integer
		}	
		if ( vo.getPrinterNo() != null && !"".equals(vo.getPrinterNo())) {
			builder.and(printerEntity.printerNo.containsIgnoreCase(vo.getPrinterNo())); //java.lang.String
		}
		if ( vo.getCreateDate() != null ) {
			builder.and(printerEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getClose() != null ) {
			builder.and(printerEntity.close.eq(vo.getClose())); //java.lang.Boolean
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(printerEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getKey() != null && !"".equals(vo.getKey())) {
			builder.and(printerEntity.key.containsIgnoreCase(vo.getKey())); //java.lang.String
		}
		log.debug("PrinterRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(printerRepository.findAll(builder.getValue(), pageable));
	}	
	
	@SuppressWarnings("unchecked")
	public List<PrinterEntity> searchLikeVo(PrinterEntity vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchLikeVo(vo, pageable);
	}
	
}
