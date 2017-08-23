/*
 * $Header: $
 * This java source file is generated by pkliu on Fri Aug 11 14:13:12 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.turnkey.repository.impl; 
//import org.springframework.orm.ObjectRetrievalFailureException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.gateweb.turnkey.model.QTurnkeyMessageLog;
import com.gateweb.turnkey.model.TurnkeyMessageLog;
import com.gateweb.turnkey.repository.TurnkeyMessageLogRepository;
import com.gateweb.turnkey.repository.TurnkeyMessageLogRepositoryCustom;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;


/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of TurnkeyMessageLog
 */
//@Repository("turnkeyMessageLogRepositoryCustom")
public class TurnkeyMessageLogRepositoryImpl implements TurnkeyMessageLogRepositoryCustom {
	
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
	TurnkeyMessageLogRepository turnkeyMessageLogRepository;
	
	public TurnkeyMessageLogRepositoryImpl(){
	}
	

	@SuppressWarnings("unchecked")
	public List<TurnkeyMessageLog> searchWithVo(TurnkeyMessageLog vo) {
		log.debug("TurnkeyMessageLogRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QTurnkeyMessageLog turnkeyMessageLog = QTurnkeyMessageLog.turnkeyMessageLog;

		if ( vo.getFromRoutingId() != null && !"".equals(vo.getFromRoutingId())) {
			builder.and(turnkeyMessageLog.fromRoutingId.equalsIgnoreCase(vo.getFromRoutingId())); //java.lang.String
		}
		if ( vo.getId() != null && vo.getId().getSeqno() != null && !"".equals(vo.getId().getSeqno())) {
			builder.and(turnkeyMessageLog.id.seqno.equalsIgnoreCase(vo.getId().getSeqno())); //java.lang.String
		}
		if ( vo.getId() != null && vo.getId().getSubseqno() != null && !"".equals(vo.getId().getSubseqno())) {
			builder.and(turnkeyMessageLog.id.subseqno.equalsIgnoreCase(vo.getId().getSubseqno())); //java.lang.String
		}
		if ( vo.getCategoryType() != null && !"".equals(vo.getCategoryType())) {
			builder.and(turnkeyMessageLog.categoryType.equalsIgnoreCase(vo.getCategoryType())); //java.lang.String
		}
		if ( vo.getInvoiceIdentifier() != null && !"".equals(vo.getInvoiceIdentifier())) {
			builder.and(turnkeyMessageLog.invoiceIdentifier.equalsIgnoreCase(vo.getInvoiceIdentifier())); //java.lang.String
		}
		if ( vo.getMessageType() != null && !"".equals(vo.getMessageType())) {
			builder.and(turnkeyMessageLog.messageType.equalsIgnoreCase(vo.getMessageType())); //java.lang.String
		}
		if ( vo.getUuid() != null && !"".equals(vo.getUuid())) {
			builder.and(turnkeyMessageLog.uuid.equalsIgnoreCase(vo.getUuid())); //java.lang.String
		}
		if ( vo.getFromPartyId() != null && !"".equals(vo.getFromPartyId())) {
			builder.and(turnkeyMessageLog.fromPartyId.equalsIgnoreCase(vo.getFromPartyId())); //java.lang.String
		}
		if ( vo.getMessageDts() != null && !"".equals(vo.getMessageDts())) {
			builder.and(turnkeyMessageLog.messageDts.equalsIgnoreCase(vo.getMessageDts())); //java.lang.String
		}
		if ( vo.getInOutBound() != null && !"".equals(vo.getInOutBound())) {
			builder.and(turnkeyMessageLog.inOutBound.equalsIgnoreCase(vo.getInOutBound())); //java.lang.String
		}
		if ( vo.getToRoutingId() != null && !"".equals(vo.getToRoutingId())) {
			builder.and(turnkeyMessageLog.toRoutingId.equalsIgnoreCase(vo.getToRoutingId())); //java.lang.String
		}
		if ( vo.getToPartyId() != null && !"".equals(vo.getToPartyId())) {
			builder.and(turnkeyMessageLog.toPartyId.equalsIgnoreCase(vo.getToPartyId())); //java.lang.String
		}
		if ( vo.getProcessType() != null && !"".equals(vo.getProcessType())) {
			builder.and(turnkeyMessageLog.processType.equalsIgnoreCase(vo.getProcessType())); //java.lang.String
		}
		
		if ( vo.getCharacterCount() != null && !"".equals(vo.getCharacterCount())) {
			builder.and(turnkeyMessageLog.characterCount.equalsIgnoreCase(vo.getCharacterCount())); //java.lang.String
		}
		if ( vo.getStatus() != null && !"".equals(vo.getStatus())) {
			builder.and(turnkeyMessageLog.status.equalsIgnoreCase(vo.getStatus())); //java.lang.String
		}
		log.debug("TurnkeyMessageLogRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(turnkeyMessageLogRepository.findAll(builder.getValue()));	
	}
	
	@SuppressWarnings("unchecked")
	public List<TurnkeyMessageLog> searchWithVo(TurnkeyMessageLog vo, Pageable pageable) {
		log.debug("TurnkeyMessageLogRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QTurnkeyMessageLog turnkeyMessageLog = QTurnkeyMessageLog.turnkeyMessageLog;

		if ( vo.getFromRoutingId() != null && !"".equals(vo.getFromRoutingId())) {
			builder.and(turnkeyMessageLog.fromRoutingId.equalsIgnoreCase(vo.getFromRoutingId())); //java.lang.String
		}
		if ( vo.getId() != null && vo.getId().getSeqno() != null && !"".equals(vo.getId().getSeqno())) {
			builder.and(turnkeyMessageLog.id.seqno.equalsIgnoreCase(vo.getId().getSeqno())); //java.lang.String
		}
		if ( vo.getId() != null && vo.getId().getSubseqno() != null && !"".equals(vo.getId().getSubseqno())) {
			builder.and(turnkeyMessageLog.id.subseqno.equalsIgnoreCase(vo.getId().getSubseqno())); //java.lang.String
		}
		if ( vo.getCategoryType() != null && !"".equals(vo.getCategoryType())) {
			builder.and(turnkeyMessageLog.categoryType.equalsIgnoreCase(vo.getCategoryType())); //java.lang.String
		}
		if ( vo.getInvoiceIdentifier() != null && !"".equals(vo.getInvoiceIdentifier())) {
			builder.and(turnkeyMessageLog.invoiceIdentifier.equalsIgnoreCase(vo.getInvoiceIdentifier())); //java.lang.String
		}
		if ( vo.getMessageType() != null && !"".equals(vo.getMessageType())) {
			builder.and(turnkeyMessageLog.messageType.equalsIgnoreCase(vo.getMessageType())); //java.lang.String
		}
		if ( vo.getUuid() != null && !"".equals(vo.getUuid())) {
			builder.and(turnkeyMessageLog.uuid.equalsIgnoreCase(vo.getUuid())); //java.lang.String
		}
		if ( vo.getFromPartyId() != null && !"".equals(vo.getFromPartyId())) {
			builder.and(turnkeyMessageLog.fromPartyId.equalsIgnoreCase(vo.getFromPartyId())); //java.lang.String
		}
		if ( vo.getMessageDts() != null && !"".equals(vo.getMessageDts())) {
			builder.and(turnkeyMessageLog.messageDts.equalsIgnoreCase(vo.getMessageDts())); //java.lang.String
		}
		if ( vo.getInOutBound() != null && !"".equals(vo.getInOutBound())) {
			builder.and(turnkeyMessageLog.inOutBound.equalsIgnoreCase(vo.getInOutBound())); //java.lang.String
		}
		if ( vo.getToRoutingId() != null && !"".equals(vo.getToRoutingId())) {
			builder.and(turnkeyMessageLog.toRoutingId.equalsIgnoreCase(vo.getToRoutingId())); //java.lang.String
		}
		if ( vo.getToPartyId() != null && !"".equals(vo.getToPartyId())) {
			builder.and(turnkeyMessageLog.toPartyId.equalsIgnoreCase(vo.getToPartyId())); //java.lang.String
		}
		if ( vo.getProcessType() != null && !"".equals(vo.getProcessType())) {
			builder.and(turnkeyMessageLog.processType.equalsIgnoreCase(vo.getProcessType())); //java.lang.String
		}
		if ( vo.getCharacterCount() != null && !"".equals(vo.getCharacterCount())) {
			builder.and(turnkeyMessageLog.characterCount.equalsIgnoreCase(vo.getCharacterCount())); //java.lang.String
		}
		if ( vo.getStatus() != null && !"".equals(vo.getStatus())) {
			builder.and(turnkeyMessageLog.status.equalsIgnoreCase(vo.getStatus())); //java.lang.String
		}
		log.debug("TurnkeyMessageLogRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(turnkeyMessageLogRepository.findAll(builder.getValue(), pageable));	
	}
	
	@SuppressWarnings("unchecked")
	public List<TurnkeyMessageLog> searchWithVo(TurnkeyMessageLog vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchWithVo(vo, pageable);
	}
	
	@SuppressWarnings("unchecked")
	public List<TurnkeyMessageLog> searchLikeVo(TurnkeyMessageLog vo) {
		log.debug("TurnkeyMessageLogRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QTurnkeyMessageLog turnkeyMessageLog = QTurnkeyMessageLog.turnkeyMessageLog;

		if ( vo.getFromRoutingId() != null && !"".equals(vo.getFromRoutingId())) {
			builder.and(turnkeyMessageLog.fromRoutingId.containsIgnoreCase(vo.getFromRoutingId())); //java.lang.String
		}
		if ( vo.getId() != null && vo.getId().getSeqno() != null && !"".equals(vo.getId().getSeqno())) {
			builder.and(turnkeyMessageLog.id.seqno.containsIgnoreCase(vo.getId().getSeqno())); //java.lang.String
		}
		if ( vo.getId() != null && vo.getId().getSubseqno() != null && !"".equals(vo.getId().getSubseqno())) {
			builder.and(turnkeyMessageLog.id.subseqno.containsIgnoreCase(vo.getId().getSubseqno())); //java.lang.String
		}
		if ( vo.getCategoryType() != null && !"".equals(vo.getCategoryType())) {
			builder.and(turnkeyMessageLog.categoryType.containsIgnoreCase(vo.getCategoryType())); //java.lang.String
		}
		if ( vo.getInvoiceIdentifier() != null && !"".equals(vo.getInvoiceIdentifier())) {
			builder.and(turnkeyMessageLog.invoiceIdentifier.containsIgnoreCase(vo.getInvoiceIdentifier())); //java.lang.String
		}
		if ( vo.getMessageType() != null && !"".equals(vo.getMessageType())) {
			builder.and(turnkeyMessageLog.messageType.containsIgnoreCase(vo.getMessageType())); //java.lang.String
		}
		if ( vo.getUuid() != null && !"".equals(vo.getUuid())) {
			builder.and(turnkeyMessageLog.uuid.containsIgnoreCase(vo.getUuid())); //java.lang.String
		}
		if ( vo.getFromPartyId() != null && !"".equals(vo.getFromPartyId())) {
			builder.and(turnkeyMessageLog.fromPartyId.containsIgnoreCase(vo.getFromPartyId())); //java.lang.String
		}
		if ( vo.getMessageDts() != null && !"".equals(vo.getMessageDts())) {
			builder.and(turnkeyMessageLog.messageDts.containsIgnoreCase(vo.getMessageDts())); //java.lang.String
		}
		if ( vo.getInOutBound() != null && !"".equals(vo.getInOutBound())) {
			builder.and(turnkeyMessageLog.inOutBound.containsIgnoreCase(vo.getInOutBound())); //java.lang.String
		}
		if ( vo.getToRoutingId() != null && !"".equals(vo.getToRoutingId())) {
			builder.and(turnkeyMessageLog.toRoutingId.containsIgnoreCase(vo.getToRoutingId())); //java.lang.String
		}
		if ( vo.getToPartyId() != null && !"".equals(vo.getToPartyId())) {
			builder.and(turnkeyMessageLog.toPartyId.containsIgnoreCase(vo.getToPartyId())); //java.lang.String
		}
		if ( vo.getProcessType() != null && !"".equals(vo.getProcessType())) {
			builder.and(turnkeyMessageLog.processType.containsIgnoreCase(vo.getProcessType())); //java.lang.String
		}
		if ( vo.getCharacterCount() != null && !"".equals(vo.getCharacterCount())) {
			builder.and(turnkeyMessageLog.characterCount.containsIgnoreCase(vo.getCharacterCount())); //java.lang.String
		}
		if ( vo.getStatus() != null && !"".equals(vo.getStatus())) {
			builder.and(turnkeyMessageLog.status.containsIgnoreCase(vo.getStatus())); //java.lang.String
		}
		log.debug("TurnkeyMessageLogRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(turnkeyMessageLogRepository.findAll(builder.getValue()));
	}		
	
	

	@SuppressWarnings("unchecked")
	public List<TurnkeyMessageLog> searchLikeVo(TurnkeyMessageLog vo, Pageable pageable) {
		log.debug("TurnkeyMessageLogRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QTurnkeyMessageLog turnkeyMessageLog = QTurnkeyMessageLog.turnkeyMessageLog;

		if ( vo.getFromRoutingId() != null && !"".equals(vo.getFromRoutingId())) {
			builder.and(turnkeyMessageLog.fromRoutingId.containsIgnoreCase(vo.getFromRoutingId())); //java.lang.String
		}
		if ( vo.getId() != null && vo.getId().getSeqno() != null && !"".equals(vo.getId().getSeqno())) {
			builder.and(turnkeyMessageLog.id.seqno.containsIgnoreCase(vo.getId().getSeqno())); //java.lang.String
		}
		if ( vo.getId() != null && vo.getId().getSubseqno() != null && !"".equals(vo.getId().getSubseqno())) {
			builder.and(turnkeyMessageLog.id.subseqno.containsIgnoreCase(vo.getId().getSubseqno())); //java.lang.String
		}
		if ( vo.getCategoryType() != null && !"".equals(vo.getCategoryType())) {
			builder.and(turnkeyMessageLog.categoryType.containsIgnoreCase(vo.getCategoryType())); //java.lang.String
		}
		if ( vo.getInvoiceIdentifier() != null && !"".equals(vo.getInvoiceIdentifier())) {
			builder.and(turnkeyMessageLog.invoiceIdentifier.containsIgnoreCase(vo.getInvoiceIdentifier())); //java.lang.String
		}
		if ( vo.getMessageType() != null && !"".equals(vo.getMessageType())) {
			builder.and(turnkeyMessageLog.messageType.containsIgnoreCase(vo.getMessageType())); //java.lang.String
		}
		if ( vo.getUuid() != null && !"".equals(vo.getUuid())) {
			builder.and(turnkeyMessageLog.uuid.containsIgnoreCase(vo.getUuid())); //java.lang.String
		}
		if ( vo.getFromPartyId() != null && !"".equals(vo.getFromPartyId())) {
			builder.and(turnkeyMessageLog.fromPartyId.containsIgnoreCase(vo.getFromPartyId())); //java.lang.String
		}
		if ( vo.getMessageDts() != null && !"".equals(vo.getMessageDts())) {
			builder.and(turnkeyMessageLog.messageDts.containsIgnoreCase(vo.getMessageDts())); //java.lang.String
		}
		if ( vo.getInOutBound() != null && !"".equals(vo.getInOutBound())) {
			builder.and(turnkeyMessageLog.inOutBound.containsIgnoreCase(vo.getInOutBound())); //java.lang.String
		}
		if ( vo.getToRoutingId() != null && !"".equals(vo.getToRoutingId())) {
			builder.and(turnkeyMessageLog.toRoutingId.containsIgnoreCase(vo.getToRoutingId())); //java.lang.String
		}
		if ( vo.getToPartyId() != null && !"".equals(vo.getToPartyId())) {
			builder.and(turnkeyMessageLog.toPartyId.containsIgnoreCase(vo.getToPartyId())); //java.lang.String
		}
		if ( vo.getProcessType() != null && !"".equals(vo.getProcessType())) {
			builder.and(turnkeyMessageLog.processType.containsIgnoreCase(vo.getProcessType())); //java.lang.String
		}
		if ( vo.getCharacterCount() != null && !"".equals(vo.getCharacterCount())) {
			builder.and(turnkeyMessageLog.characterCount.containsIgnoreCase(vo.getCharacterCount())); //java.lang.String
		}
		if ( vo.getStatus() != null && !"".equals(vo.getStatus())) {
			builder.and(turnkeyMessageLog.status.containsIgnoreCase(vo.getStatus())); //java.lang.String
		}
		log.debug("TurnkeyMessageLogRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(turnkeyMessageLogRepository.findAll(builder.getValue(), pageable));
	}	
	
	@SuppressWarnings("unchecked")
	public List<TurnkeyMessageLog> searchLikeVo(TurnkeyMessageLog vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchLikeVo(vo, pageable);
	}
	
}
