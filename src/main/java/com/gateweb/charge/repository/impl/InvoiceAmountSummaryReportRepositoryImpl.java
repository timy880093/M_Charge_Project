/*
 * $Header: $
 * This java source file is generated by pkliu on Mon Jan 29 11:19:52 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.repository.impl;
//import org.springframework.orm.ObjectRetrievalFailureException;

import com.gateweb.charge.model.InvoiceAmountSummaryReportEntity;
import com.gateweb.charge.model.QInvoiceAmountSummaryReportEntity;
import com.gateweb.charge.repository.InvoiceAmountSummaryReportRepository;
import com.gateweb.charge.repository.InvoiceAmountSummaryReportRepositoryCustom;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of InvoiceAmountSummaryReport
 */
//@Repository("invoiceAmountSummaryReportRepositoryCustom")
public class InvoiceAmountSummaryReportRepositoryImpl implements InvoiceAmountSummaryReportRepositoryCustom {
	
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
	InvoiceAmountSummaryReportRepository invoiceAmountSummaryReportRepository;
	
	public InvoiceAmountSummaryReportRepositoryImpl(){
	}
	

	@SuppressWarnings("unchecked")
	public List<InvoiceAmountSummaryReportEntity> searchWithVo(InvoiceAmountSummaryReportEntity vo) {
		log.debug("InvoiceAmountSummaryReportRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QInvoiceAmountSummaryReportEntity invoiceAmountSummaryReportEntity = QInvoiceAmountSummaryReportEntity.invoiceAmountSummaryReportEntity;

		if ( vo.getSeller() != null && !"".equals(vo.getSeller())) {
			builder.and(invoiceAmountSummaryReportEntity.seller.equalsIgnoreCase(vo.getSeller())); //java.lang.String
		}
		if ( vo.getAmount() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.amount.eq(vo.getAmount())); //java.lang.Integer
		}	
		if ( vo.getTotal() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.total.eq(vo.getTotal())); //java.math.BigDecimal
		}	
		if ( vo.getCreatorId() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getId() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.id.eq(vo.getId())); //java.lang.Long
		}	
		if ( vo.getCreateDate() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getInvoiceDate() != null && !"".equals(vo.getInvoiceDate())) {
			builder.and(invoiceAmountSummaryReportEntity.invoiceDate.equalsIgnoreCase(vo.getInvoiceDate())); //java.lang.String
		}
		if ( vo.getBuyer() != null && !"".equals(vo.getBuyer())) {
			builder.and(invoiceAmountSummaryReportEntity.buyer.equalsIgnoreCase(vo.getBuyer())); //java.lang.String
		}
		if ( vo.getInvoiceStatus() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.invoiceStatus.eq(vo.getInvoiceStatus())); //java.lang.Integer
		}	
		log.debug("InvoiceAmountSummaryReportRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(invoiceAmountSummaryReportRepository.findAll(builder.getValue()));	
	}
	
	@SuppressWarnings("unchecked")
	public List<InvoiceAmountSummaryReportEntity> searchWithVo(InvoiceAmountSummaryReportEntity vo, Pageable pageable) {
		log.debug("InvoiceAmountSummaryReportRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QInvoiceAmountSummaryReportEntity invoiceAmountSummaryReportEntity = QInvoiceAmountSummaryReportEntity.invoiceAmountSummaryReportEntity;

		if ( vo.getSeller() != null && !"".equals(vo.getSeller())) {
			builder.and(invoiceAmountSummaryReportEntity.seller.equalsIgnoreCase(vo.getSeller())); //java.lang.String
		}
		if ( vo.getAmount() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.amount.eq(vo.getAmount())); //java.lang.Integer
		}	
		if ( vo.getTotal() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.total.eq(vo.getTotal())); //java.math.BigDecimal
		}	
		if ( vo.getCreatorId() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getId() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.id.eq(vo.getId())); //java.lang.Long
		}	
		if ( vo.getCreateDate() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getInvoiceDate() != null && !"".equals(vo.getInvoiceDate())) {
			builder.and(invoiceAmountSummaryReportEntity.invoiceDate.equalsIgnoreCase(vo.getInvoiceDate())); //java.lang.String
		}
		if ( vo.getBuyer() != null && !"".equals(vo.getBuyer())) {
			builder.and(invoiceAmountSummaryReportEntity.buyer.equalsIgnoreCase(vo.getBuyer())); //java.lang.String
		}
		if ( vo.getInvoiceStatus() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.invoiceStatus.eq(vo.getInvoiceStatus())); //java.lang.Integer
		}	
		log.debug("InvoiceAmountSummaryReportRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(invoiceAmountSummaryReportRepository.findAll(builder.getValue(), pageable));	
	}
	
	@SuppressWarnings("unchecked")
	public List<InvoiceAmountSummaryReportEntity> searchWithVo(InvoiceAmountSummaryReportEntity vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchWithVo(vo, pageable);
	}
	
	@SuppressWarnings("unchecked")
	public List<InvoiceAmountSummaryReportEntity> searchLikeVo(InvoiceAmountSummaryReportEntity vo) {
		log.debug("InvoiceAmountSummaryReportRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QInvoiceAmountSummaryReportEntity invoiceAmountSummaryReportEntity = QInvoiceAmountSummaryReportEntity.invoiceAmountSummaryReportEntity;

		if ( vo.getSeller() != null && !"".equals(vo.getSeller())) {
			builder.and(invoiceAmountSummaryReportEntity.seller.containsIgnoreCase(vo.getSeller())); //java.lang.String
		}
		if ( vo.getAmount() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.amount.eq(vo.getAmount())); //java.lang.Integer
		}	
		if ( vo.getTotal() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.total.eq(vo.getTotal())); //java.math.BigDecimal
		}	
		if ( vo.getCreatorId() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getId() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.id.eq(vo.getId())); //java.lang.Long
		}	
		if ( vo.getCreateDate() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getInvoiceDate() != null && !"".equals(vo.getInvoiceDate())) {
			builder.and(invoiceAmountSummaryReportEntity.invoiceDate.containsIgnoreCase(vo.getInvoiceDate())); //java.lang.String
		}
		if ( vo.getBuyer() != null && !"".equals(vo.getBuyer())) {
			builder.and(invoiceAmountSummaryReportEntity.buyer.containsIgnoreCase(vo.getBuyer())); //java.lang.String
		}
		if ( vo.getInvoiceStatus() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.invoiceStatus.eq(vo.getInvoiceStatus())); //java.lang.Integer
		}	
		log.debug("InvoiceAmountSummaryReportRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(invoiceAmountSummaryReportRepository.findAll(builder.getValue()));
	}		
	
	

	@SuppressWarnings("unchecked")
	public List<InvoiceAmountSummaryReportEntity> searchLikeVo(InvoiceAmountSummaryReportEntity vo, Pageable pageable) {
		log.debug("InvoiceAmountSummaryReportRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QInvoiceAmountSummaryReportEntity invoiceAmountSummaryReportEntity = QInvoiceAmountSummaryReportEntity.invoiceAmountSummaryReportEntity;

		if ( vo.getSeller() != null && !"".equals(vo.getSeller())) {
			builder.and(invoiceAmountSummaryReportEntity.seller.containsIgnoreCase(vo.getSeller())); //java.lang.String
		}
		if ( vo.getAmount() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.amount.eq(vo.getAmount())); //java.lang.Integer
		}	
		if ( vo.getTotal() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.total.eq(vo.getTotal())); //java.math.BigDecimal
		}	
		if ( vo.getCreatorId() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getId() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.id.eq(vo.getId())); //java.lang.Long
		}	
		if ( vo.getCreateDate() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getInvoiceDate() != null && !"".equals(vo.getInvoiceDate())) {
			builder.and(invoiceAmountSummaryReportEntity.invoiceDate.containsIgnoreCase(vo.getInvoiceDate())); //java.lang.String
		}
		if ( vo.getBuyer() != null && !"".equals(vo.getBuyer())) {
			builder.and(invoiceAmountSummaryReportEntity.buyer.containsIgnoreCase(vo.getBuyer())); //java.lang.String
		}
		if ( vo.getInvoiceStatus() != null ) {
			builder.and(invoiceAmountSummaryReportEntity.invoiceStatus.eq(vo.getInvoiceStatus())); //java.lang.Integer
		}	
		log.debug("InvoiceAmountSummaryReportRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(invoiceAmountSummaryReportRepository.findAll(builder.getValue(), pageable));
	}	
	
	@SuppressWarnings("unchecked")
	public List<InvoiceAmountSummaryReportEntity> searchLikeVo(InvoiceAmountSummaryReportEntity vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchLikeVo(vo, pageable);
	}
	
}
