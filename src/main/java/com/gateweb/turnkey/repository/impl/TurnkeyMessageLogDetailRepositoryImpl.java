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

import com.gateweb.turnkey.model.QTurnkeyMessageLogDetail;
import com.gateweb.turnkey.model.TurnkeyMessageLogDetail;
import com.gateweb.turnkey.repository.TurnkeyMessageLogDetailRepository;
import com.gateweb.turnkey.repository.TurnkeyMessageLogDetailRepositoryCustom;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;


/**
 * 
 * @author pkliu
 *
 * This class provides methods to populate DB Table of TurnkeyMessageLogDetail
 */
//@Repository("turnkeyMessageLogDetailRepositoryCustom")
public class TurnkeyMessageLogDetailRepositoryImpl implements TurnkeyMessageLogDetailRepositoryCustom {
	
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
	TurnkeyMessageLogDetailRepository turnkeyMessageLogDetailRepository;
	
	public TurnkeyMessageLogDetailRepositoryImpl(){
	}
	

	@SuppressWarnings("unchecked")
	public List<TurnkeyMessageLogDetail> searchWithVo(TurnkeyMessageLogDetail vo) {
		log.debug("TurnkeyMessageLogDetailRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QTurnkeyMessageLogDetail turnkeyMessageLogDetail = QTurnkeyMessageLogDetail.turnkeyMessageLogDetail;

		if ( vo.getId() != null &&vo.getId().getTask() != null && !"".equals(vo.getId().getTask())) {
			builder.and(turnkeyMessageLogDetail.id.task.equalsIgnoreCase(vo.getId().getTask())); //java.lang.String
		}
		if ( vo.getId() != null &&vo.getId().getSeqno() != null && !"".equals(vo.getId().getSeqno())) {
			builder.and(turnkeyMessageLogDetail.id.seqno.equalsIgnoreCase(vo.getId().getSeqno())); //java.lang.String
		}
		if ( vo.getId() != null &&vo.getId().getSubseqno() != null && !"".equals(vo.getId().getSubseqno())) {
			builder.and(turnkeyMessageLogDetail.id.subseqno.equalsIgnoreCase(vo.getId().getSubseqno())); //java.lang.String
		}
		if ( vo.getFilename() != null && !"".equals(vo.getFilename())) {
			builder.and(turnkeyMessageLogDetail.filename.equalsIgnoreCase(vo.getFilename())); //java.lang.String
		}
		if ( vo.getProcessDts() != null && !"".equals(vo.getProcessDts())) {
			builder.and(turnkeyMessageLogDetail.processDts.equalsIgnoreCase(vo.getProcessDts())); //java.lang.String
		}
		if ( vo.getUuid() != null && !"".equals(vo.getUuid())) {
			builder.and(turnkeyMessageLogDetail.uuid.equalsIgnoreCase(vo.getUuid())); //java.lang.String
		}
		
		if ( vo.getStatus() != null && !"".equals(vo.getStatus())) {
			builder.and(turnkeyMessageLogDetail.status.equalsIgnoreCase(vo.getStatus())); //java.lang.String
		}
		log.debug("TurnkeyMessageLogDetailRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(turnkeyMessageLogDetailRepository.findAll(builder.getValue()));	
	}
	
	@SuppressWarnings("unchecked")
	public List<TurnkeyMessageLogDetail> searchWithVo(TurnkeyMessageLogDetail vo, Pageable pageable) {
		log.debug("TurnkeyMessageLogDetailRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QTurnkeyMessageLogDetail turnkeyMessageLogDetail = QTurnkeyMessageLogDetail.turnkeyMessageLogDetail;

		if ( vo.getId() != null &&vo.getId().getTask() != null && !"".equals(vo.getId().getTask())) {
			builder.and(turnkeyMessageLogDetail.id.task.equalsIgnoreCase(vo.getId().getTask())); //java.lang.String
		}
		if ( vo.getId() != null &&vo.getId().getSeqno() != null && !"".equals(vo.getId().getSeqno())) {
			builder.and(turnkeyMessageLogDetail.id.seqno.equalsIgnoreCase(vo.getId().getSeqno())); //java.lang.String
		}
		if ( vo.getId() != null &&vo.getId().getSubseqno() != null && !"".equals(vo.getId().getSubseqno())) {
			builder.and(turnkeyMessageLogDetail.id.subseqno.equalsIgnoreCase(vo.getId().getSubseqno())); //java.lang.String
		}
		if ( vo.getFilename() != null && !"".equals(vo.getFilename())) {
			builder.and(turnkeyMessageLogDetail.filename.equalsIgnoreCase(vo.getFilename())); //java.lang.String
		}
		if ( vo.getProcessDts() != null && !"".equals(vo.getProcessDts())) {
			builder.and(turnkeyMessageLogDetail.processDts.equalsIgnoreCase(vo.getProcessDts())); //java.lang.String
		}
		if ( vo.getUuid() != null && !"".equals(vo.getUuid())) {
			builder.and(turnkeyMessageLogDetail.uuid.equalsIgnoreCase(vo.getUuid())); //java.lang.String
		}
		if ( vo.getStatus() != null && !"".equals(vo.getStatus())) {
			builder.and(turnkeyMessageLogDetail.status.equalsIgnoreCase(vo.getStatus())); //java.lang.String
		}
		log.debug("TurnkeyMessageLogDetailRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(turnkeyMessageLogDetailRepository.findAll(builder.getValue(), pageable));	
	}
	
	@SuppressWarnings("unchecked")
	public List<TurnkeyMessageLogDetail> searchWithVo(TurnkeyMessageLogDetail vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchWithVo(vo, pageable);
	}
	
	@SuppressWarnings("unchecked")
	public List<TurnkeyMessageLogDetail> searchLikeVo(TurnkeyMessageLogDetail vo) {
		log.debug("TurnkeyMessageLogDetailRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QTurnkeyMessageLogDetail turnkeyMessageLogDetail = QTurnkeyMessageLogDetail.turnkeyMessageLogDetail;

		if ( vo.getId() != null &&vo.getId().getTask() != null && !"".equals(vo.getId().getTask())) {
			builder.and(turnkeyMessageLogDetail.id.task.containsIgnoreCase(vo.getId().getTask())); //java.lang.String
		}
		if ( vo.getId() != null &&vo.getId().getSeqno() != null && !"".equals(vo.getId().getSeqno())) {
			builder.and(turnkeyMessageLogDetail.id.seqno.containsIgnoreCase(vo.getId().getSeqno())); //java.lang.String
		}
		if ( vo.getId() != null &&vo.getId().getSubseqno() != null && !"".equals(vo.getId().getSubseqno())) {
			builder.and(turnkeyMessageLogDetail.id.subseqno.containsIgnoreCase(vo.getId().getSubseqno())); //java.lang.String
		}
		if ( vo.getFilename() != null && !"".equals(vo.getFilename())) {
			builder.and(turnkeyMessageLogDetail.filename.containsIgnoreCase(vo.getFilename())); //java.lang.String
		}
		if ( vo.getProcessDts() != null && !"".equals(vo.getProcessDts())) {
			builder.and(turnkeyMessageLogDetail.processDts.containsIgnoreCase(vo.getProcessDts())); //java.lang.String
		}
		if ( vo.getUuid() != null && !"".equals(vo.getUuid())) {
			builder.and(turnkeyMessageLogDetail.uuid.containsIgnoreCase(vo.getUuid())); //java.lang.String
		}
		if ( vo.getStatus() != null && !"".equals(vo.getStatus())) {
			builder.and(turnkeyMessageLogDetail.status.containsIgnoreCase(vo.getStatus())); //java.lang.String
		}
		log.debug("TurnkeyMessageLogDetailRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(turnkeyMessageLogDetailRepository.findAll(builder.getValue()));
	}		
	
	

	@SuppressWarnings("unchecked")
	public List<TurnkeyMessageLogDetail> searchLikeVo(TurnkeyMessageLogDetail vo, Pageable pageable) {
		log.debug("TurnkeyMessageLogDetailRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QTurnkeyMessageLogDetail turnkeyMessageLogDetail = QTurnkeyMessageLogDetail.turnkeyMessageLogDetail;

		if ( vo.getId() != null &&vo.getId().getTask() != null && !"".equals(vo.getId().getTask())) {
			builder.and(turnkeyMessageLogDetail.id.task.containsIgnoreCase(vo.getId().getTask())); //java.lang.String
		}
		if ( vo.getId() != null &&vo.getId().getSeqno() != null && !"".equals(vo.getId().getSeqno())) {
			builder.and(turnkeyMessageLogDetail.id.seqno.containsIgnoreCase(vo.getId().getSeqno())); //java.lang.String
		}
		if ( vo.getId() != null &&vo.getId().getSubseqno() != null && !"".equals(vo.getId().getSubseqno())) {
			builder.and(turnkeyMessageLogDetail.id.subseqno.containsIgnoreCase(vo.getId().getSubseqno())); //java.lang.String
		}
		if ( vo.getFilename() != null && !"".equals(vo.getFilename())) {
			builder.and(turnkeyMessageLogDetail.filename.containsIgnoreCase(vo.getFilename())); //java.lang.String
		}
		if ( vo.getProcessDts() != null && !"".equals(vo.getProcessDts())) {
			builder.and(turnkeyMessageLogDetail.processDts.containsIgnoreCase(vo.getProcessDts())); //java.lang.String
		}
		if ( vo.getUuid() != null && !"".equals(vo.getUuid())) {
			builder.and(turnkeyMessageLogDetail.uuid.containsIgnoreCase(vo.getUuid())); //java.lang.String
		}
		if ( vo.getStatus() != null && !"".equals(vo.getStatus())) {
			builder.and(turnkeyMessageLogDetail.status.containsIgnoreCase(vo.getStatus())); //java.lang.String
		}
		log.debug("TurnkeyMessageLogDetailRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(turnkeyMessageLogDetailRepository.findAll(builder.getValue(), pageable));
	}	
	
	@SuppressWarnings("unchecked")
	public List<TurnkeyMessageLogDetail> searchLikeVo(TurnkeyMessageLogDetail vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchLikeVo(vo, pageable);
	}
	
}
