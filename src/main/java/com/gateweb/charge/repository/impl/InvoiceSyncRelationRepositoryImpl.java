/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Oct 31 09:15:12 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.repository.impl; 
//import org.springframework.orm.ObjectRetrievalFailureException;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.gateweb.charge.model.InvoiceSyncRelationEntity;
import com.gateweb.charge.model.QInvoiceSyncRelationEntity;
import com.gateweb.charge.repository.InvoiceSyncRelationRepository;
import com.gateweb.charge.repository.InvoiceSyncRelationRepositoryCustom;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;


/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of InvoiceSyncRelation
 */
//@Repository("invoiceSyncRelationRepositoryCustom")
public class InvoiceSyncRelationRepositoryImpl implements InvoiceSyncRelationRepositoryCustom {
	
	/**
	 * <p>
	 * <code>Log</code> instance for this application.
	 * </p>
	 */
	protected final Logger logger = LogManager.getLogger(getClass());
	
	/*	@PersistenceContext
	private EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}*/

	@Autowired
	InvoiceSyncRelationRepository invoiceSyncRelationRepository;
	
	public InvoiceSyncRelationRepositoryImpl(){
	}
	

	@SuppressWarnings("unchecked")
	public List<InvoiceSyncRelationEntity> searchWithVo(InvoiceSyncRelationEntity vo) {
		logger.debug("InvoiceSyncRelationRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QInvoiceSyncRelationEntity invoiceSyncRelationEntity = QInvoiceSyncRelationEntity.invoiceSyncRelationEntity;

		if ( vo.getId().getUploadType() != null && !"".equals(vo.getId().getUploadType())) {
			builder.and(invoiceSyncRelationEntity.id.uploadType.equalsIgnoreCase(vo.getId().getUploadType())); //java.lang.String
		}
		if ( vo.getId().getSellerIdentifier() != null && !"".equals(vo.getId().getSellerIdentifier())) {
			builder.and(invoiceSyncRelationEntity.id.sellerIdentifier.equalsIgnoreCase(vo.getId().getSellerIdentifier())); //java.lang.String
		}
		if ( vo.getId().getcYearMonth() != null && !"".equals(vo.getId().getcYearMonth())) {
			builder.and(invoiceSyncRelationEntity.id.cYearMonth.equalsIgnoreCase(vo.getId().getcYearMonth())); //java.lang.String
		}
		if ( vo.getCompanyKey() != null && !"".equals(vo.getCompanyKey())) {
			builder.and(invoiceSyncRelationEntity.companyKey.equalsIgnoreCase(vo.getCompanyKey())); //java.lang.String
		}
		if ( vo.getSendMail() != null ) {
			builder.and(invoiceSyncRelationEntity.sendMail.eq(vo.getSendMail())); //java.lang.Boolean
		}	
		if ( vo.getId().getInvoiceNumber() != null && !"".equals(vo.getId().getInvoiceNumber())) {
			builder.and(invoiceSyncRelationEntity.id.invoiceNumber.equalsIgnoreCase(vo.getId().getInvoiceNumber())); //java.lang.String
		}
		logger.debug("InvoiceSyncRelationRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(invoiceSyncRelationRepository.findAll(builder.getValue()));	
	}
	
	@SuppressWarnings("unchecked")
	public List<InvoiceSyncRelationEntity> searchWithVo(InvoiceSyncRelationEntity vo, Pageable pageable) {
		logger.debug("InvoiceSyncRelationRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QInvoiceSyncRelationEntity invoiceSyncRelationEntity = QInvoiceSyncRelationEntity.invoiceSyncRelationEntity;

		if ( vo.getId().getUploadType() != null && !"".equals(vo.getId().getUploadType())) {
			builder.and(invoiceSyncRelationEntity.id.uploadType.equalsIgnoreCase(vo.getId().getUploadType())); //java.lang.String
		}
		if ( vo.getId().getSellerIdentifier() != null && !"".equals(vo.getId().getSellerIdentifier())) {
			builder.and(invoiceSyncRelationEntity.id.sellerIdentifier.equalsIgnoreCase(vo.getId().getSellerIdentifier())); //java.lang.String
		}
		if ( vo.getId().getcYearMonth() != null && !"".equals(vo.getId().getcYearMonth())) {
			builder.and(invoiceSyncRelationEntity.id.cYearMonth.equalsIgnoreCase(vo.getId().getcYearMonth())); //java.lang.String
		}
		if ( vo.getCompanyKey() != null && !"".equals(vo.getCompanyKey())) {
			builder.and(invoiceSyncRelationEntity.companyKey.equalsIgnoreCase(vo.getCompanyKey())); //java.lang.String
		}
		if ( vo.getSendMail() != null ) {
			builder.and(invoiceSyncRelationEntity.sendMail.eq(vo.getSendMail())); //java.lang.Boolean
		}	
		if ( vo.getId().getInvoiceNumber() != null && !"".equals(vo.getId().getInvoiceNumber())) {
			builder.and(invoiceSyncRelationEntity.id.invoiceNumber.equalsIgnoreCase(vo.getId().getInvoiceNumber())); //java.lang.String
		}
		logger.debug("InvoiceSyncRelationRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(invoiceSyncRelationRepository.findAll(builder.getValue(), pageable));	
	}
	
	@SuppressWarnings("unchecked")
	public List<InvoiceSyncRelationEntity> searchWithVo(InvoiceSyncRelationEntity vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchWithVo(vo, pageable);
	}
	
	@SuppressWarnings("unchecked")
	public List<InvoiceSyncRelationEntity> searchLikeVo(InvoiceSyncRelationEntity vo) {
		logger.debug("InvoiceSyncRelationRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QInvoiceSyncRelationEntity invoiceSyncRelationEntity = QInvoiceSyncRelationEntity.invoiceSyncRelationEntity;

		if ( vo.getId().getUploadType() != null && !"".equals(vo.getId().getUploadType())) {
			builder.and(invoiceSyncRelationEntity.id.uploadType.containsIgnoreCase(vo.getId().getUploadType())); //java.lang.String
		}
		if ( vo.getId().getSellerIdentifier() != null && !"".equals(vo.getId().getSellerIdentifier())) {
			builder.and(invoiceSyncRelationEntity.id.sellerIdentifier.containsIgnoreCase(vo.getId().getSellerIdentifier())); //java.lang.String
		}
		if ( vo.getId().getcYearMonth() != null && !"".equals(vo.getId().getcYearMonth())) {
			builder.and(invoiceSyncRelationEntity.id.cYearMonth.containsIgnoreCase(vo.getId().getcYearMonth())); //java.lang.String
		}
		if ( vo.getCompanyKey() != null && !"".equals(vo.getCompanyKey())) {
			builder.and(invoiceSyncRelationEntity.companyKey.containsIgnoreCase(vo.getCompanyKey())); //java.lang.String
		}
		if ( vo.getSendMail() != null ) {
			builder.and(invoiceSyncRelationEntity.sendMail.eq(vo.getSendMail())); //java.lang.Boolean
		}	
		if ( vo.getId().getInvoiceNumber() != null && !"".equals(vo.getId().getInvoiceNumber())) {
			builder.and(invoiceSyncRelationEntity.id.invoiceNumber.containsIgnoreCase(vo.getId().getInvoiceNumber())); //java.lang.String
		}
		logger.debug("InvoiceSyncRelationRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(invoiceSyncRelationRepository.findAll(builder.getValue()));
	}		
	
	

	@SuppressWarnings("unchecked")
	public List<InvoiceSyncRelationEntity> searchLikeVo(InvoiceSyncRelationEntity vo, Pageable pageable) {
		logger.debug("InvoiceSyncRelationRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QInvoiceSyncRelationEntity invoiceSyncRelationEntity = QInvoiceSyncRelationEntity.invoiceSyncRelationEntity;

		if ( vo.getId().getUploadType() != null && !"".equals(vo.getId().getUploadType())) {
			builder.and(invoiceSyncRelationEntity.id.uploadType.containsIgnoreCase(vo.getId().getUploadType())); //java.lang.String
		}
		if ( vo.getId().getSellerIdentifier() != null && !"".equals(vo.getId().getSellerIdentifier())) {
			builder.and(invoiceSyncRelationEntity.id.sellerIdentifier.containsIgnoreCase(vo.getId().getSellerIdentifier())); //java.lang.String
		}
		if ( vo.getId().getcYearMonth() != null && !"".equals(vo.getId().getcYearMonth())) {
			builder.and(invoiceSyncRelationEntity.id.cYearMonth.containsIgnoreCase(vo.getId().getcYearMonth())); //java.lang.String
		}
		if ( vo.getCompanyKey() != null && !"".equals(vo.getCompanyKey())) {
			builder.and(invoiceSyncRelationEntity.companyKey.containsIgnoreCase(vo.getCompanyKey())); //java.lang.String
		}
		if ( vo.getSendMail() != null ) {
			builder.and(invoiceSyncRelationEntity.sendMail.eq(vo.getSendMail())); //java.lang.Boolean
		}	
		if ( vo.getId().getInvoiceNumber() != null && !"".equals(vo.getId().getInvoiceNumber())) {
			builder.and(invoiceSyncRelationEntity.id.invoiceNumber.containsIgnoreCase(vo.getId().getInvoiceNumber())); //java.lang.String
		}
		logger.debug("InvoiceSyncRelationRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(invoiceSyncRelationRepository.findAll(builder.getValue(), pageable));
	}	
	
	@SuppressWarnings("unchecked")
	public List<InvoiceSyncRelationEntity> searchLikeVo(InvoiceSyncRelationEntity vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchLikeVo(vo, pageable);
	}
	
}
