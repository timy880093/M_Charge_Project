/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:14 CST 2018
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
 * This class provides methods to populate DB Table of LogParameter
 */
//@Repository("logParameterRepositoryCustom")
public class LogParameterRepositoryImpl implements LogParameterRepositoryCustom {
	
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
	LogParameterRepository logParameterRepository;
	
	public LogParameterRepositoryImpl(){
	}
	

	@SuppressWarnings("unchecked")
	public List<LogParameterEntity> searchWithVo(LogParameterEntity vo) {
		log.debug("LogParameterRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QLogParameterEntity logParameterEntity = QLogParameterEntity.logParameterEntity;

		if ( vo.getLogPattern() != null && !"".equals(vo.getLogPattern())) {
			builder.and(logParameterEntity.logPattern.equalsIgnoreCase(vo.getLogPattern())); //java.lang.String
		}
		if ( vo.getActionName() != null && !"".equals(vo.getActionName())) {
			builder.and(logParameterEntity.actionName.equalsIgnoreCase(vo.getActionName())); //java.lang.String
		}
		if ( vo.getParameterId() != null ) {
			builder.and(logParameterEntity.parameterId.eq(vo.getParameterId())); //java.lang.Integer
		}	
		log.debug("LogParameterRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(logParameterRepository.findAll(builder.getValue()));	
	}
	
	@SuppressWarnings("unchecked")
	public List<LogParameterEntity> searchWithVo(LogParameterEntity vo, Pageable pageable) {
		log.debug("LogParameterRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QLogParameterEntity logParameterEntity = QLogParameterEntity.logParameterEntity;

		if ( vo.getLogPattern() != null && !"".equals(vo.getLogPattern())) {
			builder.and(logParameterEntity.logPattern.equalsIgnoreCase(vo.getLogPattern())); //java.lang.String
		}
		if ( vo.getActionName() != null && !"".equals(vo.getActionName())) {
			builder.and(logParameterEntity.actionName.equalsIgnoreCase(vo.getActionName())); //java.lang.String
		}
		if ( vo.getParameterId() != null ) {
			builder.and(logParameterEntity.parameterId.eq(vo.getParameterId())); //java.lang.Integer
		}	
		log.debug("LogParameterRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(logParameterRepository.findAll(builder.getValue(), pageable));	
	}
	
	@SuppressWarnings("unchecked")
	public List<LogParameterEntity> searchWithVo(LogParameterEntity vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchWithVo(vo, pageable);
	}
	
	@SuppressWarnings("unchecked")
	public List<LogParameterEntity> searchLikeVo(LogParameterEntity vo) {
		log.debug("LogParameterRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QLogParameterEntity logParameterEntity = QLogParameterEntity.logParameterEntity;

		if ( vo.getLogPattern() != null && !"".equals(vo.getLogPattern())) {
			builder.and(logParameterEntity.logPattern.containsIgnoreCase(vo.getLogPattern())); //java.lang.String
		}
		if ( vo.getActionName() != null && !"".equals(vo.getActionName())) {
			builder.and(logParameterEntity.actionName.containsIgnoreCase(vo.getActionName())); //java.lang.String
		}
		if ( vo.getParameterId() != null ) {
			builder.and(logParameterEntity.parameterId.eq(vo.getParameterId())); //java.lang.Integer
		}	
		log.debug("LogParameterRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(logParameterRepository.findAll(builder.getValue()));
	}		
	
	

	@SuppressWarnings("unchecked")
	public List<LogParameterEntity> searchLikeVo(LogParameterEntity vo, Pageable pageable) {
		log.debug("LogParameterRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QLogParameterEntity logParameterEntity = QLogParameterEntity.logParameterEntity;

		if ( vo.getLogPattern() != null && !"".equals(vo.getLogPattern())) {
			builder.and(logParameterEntity.logPattern.containsIgnoreCase(vo.getLogPattern())); //java.lang.String
		}
		if ( vo.getActionName() != null && !"".equals(vo.getActionName())) {
			builder.and(logParameterEntity.actionName.containsIgnoreCase(vo.getActionName())); //java.lang.String
		}
		if ( vo.getParameterId() != null ) {
			builder.and(logParameterEntity.parameterId.eq(vo.getParameterId())); //java.lang.Integer
		}	
		log.debug("LogParameterRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(logParameterRepository.findAll(builder.getValue(), pageable));
	}	
	
	@SuppressWarnings("unchecked")
	public List<LogParameterEntity> searchLikeVo(LogParameterEntity vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchLikeVo(vo, pageable);
	}
	
}