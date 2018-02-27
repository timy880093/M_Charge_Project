/*
 * $Header: $
 * This java source file is generated by pkliu on Mon Jan 29 11:19:52 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.repository.impl;
//import org.springframework.orm.ObjectRetrievalFailureException;

import com.gateweb.charge.model.AllowanceAmountSummaryReportEntity;
import com.gateweb.charge.model.QAllowanceAmountSummaryReportEntity;
import com.gateweb.charge.repository.AllowanceAmountSummaryReportRepository;
import com.gateweb.charge.repository.AllowanceAmountSummaryReportRepositoryCustom;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of AllowanceAmountSummaryReport
 */
//@Repository("allowanceAmountSummaryReportRepositoryCustom")
public class AllowanceAmountSummaryReportRepositoryImpl implements AllowanceAmountSummaryReportRepositoryCustom {
	
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
	@Qualifier("chargeAllowanceAmountSummaryReportRepository")
	AllowanceAmountSummaryReportRepository chargeAllowanceAmountSummaryReportRepository;
	
	public AllowanceAmountSummaryReportRepositoryImpl(){
	}
	

	@SuppressWarnings("unchecked")
	public List<AllowanceAmountSummaryReportEntity> searchWithVo(AllowanceAmountSummaryReportEntity vo) {
		log.debug("AllowanceAmountSummaryReportRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QAllowanceAmountSummaryReportEntity allowanceAmountSummaryReportEntity = QAllowanceAmountSummaryReportEntity.allowanceAmountSummaryReportEntity;

		if ( vo.getSeller() != null && !"".equals(vo.getSeller())) {
			builder.and(allowanceAmountSummaryReportEntity.seller.equalsIgnoreCase(vo.getSeller())); //java.lang.String
		}
		if ( vo.getAmount() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.amount.eq(vo.getAmount())); //java.lang.Integer
		}	
		if ( vo.getTotal() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.total.eq(vo.getTotal())); //java.math.BigDecimal
		}	
		if ( vo.getCreatorId() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getId() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.id.eq(vo.getId())); //java.lang.Long
		}	
		if ( vo.getCreateDate() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getAllowanceDate() != null && !"".equals(vo.getAllowanceDate())) {
			builder.and(allowanceAmountSummaryReportEntity.allowanceDate.equalsIgnoreCase(vo.getAllowanceDate())); //java.lang.String
		}
		if ( vo.getBuyer() != null && !"".equals(vo.getBuyer())) {
			builder.and(allowanceAmountSummaryReportEntity.buyer.equalsIgnoreCase(vo.getBuyer())); //java.lang.String
		}
		if ( vo.getAllowanceStatus() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.allowanceStatus.eq(vo.getAllowanceStatus())); //java.lang.Integer
		}	
		log.debug("AllowanceAmountSummaryReportRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(chargeAllowanceAmountSummaryReportRepository.findAll(builder.getValue()));
	}
	
	@SuppressWarnings("unchecked")
	public List<AllowanceAmountSummaryReportEntity> searchWithVo(AllowanceAmountSummaryReportEntity vo, Pageable pageable) {
		log.debug("AllowanceAmountSummaryReportRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QAllowanceAmountSummaryReportEntity allowanceAmountSummaryReportEntity = QAllowanceAmountSummaryReportEntity.allowanceAmountSummaryReportEntity;

		if ( vo.getSeller() != null && !"".equals(vo.getSeller())) {
			builder.and(allowanceAmountSummaryReportEntity.seller.equalsIgnoreCase(vo.getSeller())); //java.lang.String
		}
		if ( vo.getAmount() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.amount.eq(vo.getAmount())); //java.lang.Integer
		}	
		if ( vo.getTotal() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.total.eq(vo.getTotal())); //java.math.BigDecimal
		}	
		if ( vo.getCreatorId() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getId() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.id.eq(vo.getId())); //java.lang.Long
		}	
		if ( vo.getCreateDate() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getAllowanceDate() != null && !"".equals(vo.getAllowanceDate())) {
			builder.and(allowanceAmountSummaryReportEntity.allowanceDate.equalsIgnoreCase(vo.getAllowanceDate())); //java.lang.String
		}
		if ( vo.getBuyer() != null && !"".equals(vo.getBuyer())) {
			builder.and(allowanceAmountSummaryReportEntity.buyer.equalsIgnoreCase(vo.getBuyer())); //java.lang.String
		}
		if ( vo.getAllowanceStatus() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.allowanceStatus.eq(vo.getAllowanceStatus())); //java.lang.Integer
		}	
		log.debug("AllowanceAmountSummaryReportRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(chargeAllowanceAmountSummaryReportRepository.findAll(builder.getValue(), pageable));
	}
	
	@SuppressWarnings("unchecked")
	public List<AllowanceAmountSummaryReportEntity> searchWithVo(AllowanceAmountSummaryReportEntity vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchWithVo(vo, pageable);
	}
	
	@SuppressWarnings("unchecked")
	public List<AllowanceAmountSummaryReportEntity> searchLikeVo(AllowanceAmountSummaryReportEntity vo) {
		log.debug("AllowanceAmountSummaryReportRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QAllowanceAmountSummaryReportEntity allowanceAmountSummaryReportEntity = QAllowanceAmountSummaryReportEntity.allowanceAmountSummaryReportEntity;

		if ( vo.getSeller() != null && !"".equals(vo.getSeller())) {
			builder.and(allowanceAmountSummaryReportEntity.seller.containsIgnoreCase(vo.getSeller())); //java.lang.String
		}
		if ( vo.getAmount() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.amount.eq(vo.getAmount())); //java.lang.Integer
		}	
		if ( vo.getTotal() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.total.eq(vo.getTotal())); //java.math.BigDecimal
		}	
		if ( vo.getCreatorId() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getId() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.id.eq(vo.getId())); //java.lang.Long
		}	
		if ( vo.getCreateDate() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getAllowanceDate() != null && !"".equals(vo.getAllowanceDate())) {
			builder.and(allowanceAmountSummaryReportEntity.allowanceDate.containsIgnoreCase(vo.getAllowanceDate())); //java.lang.String
		}
		if ( vo.getBuyer() != null && !"".equals(vo.getBuyer())) {
			builder.and(allowanceAmountSummaryReportEntity.buyer.containsIgnoreCase(vo.getBuyer())); //java.lang.String
		}
		if ( vo.getAllowanceStatus() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.allowanceStatus.eq(vo.getAllowanceStatus())); //java.lang.Integer
		}	
		log.debug("AllowanceAmountSummaryReportRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(chargeAllowanceAmountSummaryReportRepository.findAll(builder.getValue()));
	}		
	
	

	@SuppressWarnings("unchecked")
	public List<AllowanceAmountSummaryReportEntity> searchLikeVo(AllowanceAmountSummaryReportEntity vo, Pageable pageable) {
		log.debug("AllowanceAmountSummaryReportRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QAllowanceAmountSummaryReportEntity allowanceAmountSummaryReportEntity = QAllowanceAmountSummaryReportEntity.allowanceAmountSummaryReportEntity;

		if ( vo.getSeller() != null && !"".equals(vo.getSeller())) {
			builder.and(allowanceAmountSummaryReportEntity.seller.containsIgnoreCase(vo.getSeller())); //java.lang.String
		}
		if ( vo.getAmount() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.amount.eq(vo.getAmount())); //java.lang.Integer
		}	
		if ( vo.getTotal() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.total.eq(vo.getTotal())); //java.math.BigDecimal
		}	
		if ( vo.getCreatorId() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getId() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.id.eq(vo.getId())); //java.lang.Long
		}	
		if ( vo.getCreateDate() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getAllowanceDate() != null && !"".equals(vo.getAllowanceDate())) {
			builder.and(allowanceAmountSummaryReportEntity.allowanceDate.containsIgnoreCase(vo.getAllowanceDate())); //java.lang.String
		}
		if ( vo.getBuyer() != null && !"".equals(vo.getBuyer())) {
			builder.and(allowanceAmountSummaryReportEntity.buyer.containsIgnoreCase(vo.getBuyer())); //java.lang.String
		}
		if ( vo.getAllowanceStatus() != null ) {
			builder.and(allowanceAmountSummaryReportEntity.allowanceStatus.eq(vo.getAllowanceStatus())); //java.lang.Integer
		}	
		log.debug("AllowanceAmountSummaryReportRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(chargeAllowanceAmountSummaryReportRepository.findAll(builder.getValue(), pageable));
	}	
	
	@SuppressWarnings("unchecked")
	public List<AllowanceAmountSummaryReportEntity> searchLikeVo(AllowanceAmountSummaryReportEntity vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchLikeVo(vo, pageable);
	}
	
}
