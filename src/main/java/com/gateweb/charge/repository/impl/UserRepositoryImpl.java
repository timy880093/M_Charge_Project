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
 * This class provides methods to populate DB Table of User
 */
//@Repository("userRepositoryCustom")
public class UserRepositoryImpl implements UserRepositoryCustom {
	
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
	UserRepository userRepository;
	
	public UserRepositoryImpl(){
	}
	

	@SuppressWarnings("unchecked")
	public List<UserEntity> searchWithVo(UserEntity vo) {
		log.debug("UserRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QUserEntity userEntity = QUserEntity.userEntity;

		if ( vo.getCompanyId() != null ) {
			builder.and(userEntity.companyId.eq(vo.getCompanyId())); //java.lang.Integer
		}	
		if ( vo.getAuthKey() != null && !"".equals(vo.getAuthKey())) {
			builder.and(userEntity.authKey.equalsIgnoreCase(vo.getAuthKey())); //java.lang.String
		}
		if ( vo.getDefaultTaxType() != null && !"".equals(vo.getDefaultTaxType())) {
			builder.and(userEntity.defaultTaxType.equalsIgnoreCase(vo.getDefaultTaxType())); //java.lang.String
		}
		if ( vo.getDefaultB2bFlag() != null && !"".equals(vo.getDefaultB2bFlag())) {
			builder.and(userEntity.defaultB2bFlag.equalsIgnoreCase(vo.getDefaultB2bFlag())); //java.lang.String
		}
		if ( vo.getPassword() != null && !"".equals(vo.getPassword())) {
			builder.and(userEntity.password.equalsIgnoreCase(vo.getPassword())); //java.lang.String
		}
		if ( vo.getUserId() != null ) {
			builder.and(userEntity.userId.eq(vo.getUserId())); //java.lang.Integer
		}	
		if ( vo.getRoleId() != null ) {
			builder.and(userEntity.roleId.eq(vo.getRoleId())); //java.lang.Integer
		}	
		if ( vo.getName() != null && !"".equals(vo.getName())) {
			builder.and(userEntity.name.equalsIgnoreCase(vo.getName())); //java.lang.String
		}
		if ( vo.getCreatorId() != null ) {
			builder.and(userEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(userEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getLogoutTime() != null ) {
			builder.and(userEntity.logoutTime.eq(vo.getLogoutTime())); //java.lang.Integer
		}	
		if ( vo.getPrinterId() != null ) {
			builder.and(userEntity.printerId.eq(vo.getPrinterId())); //java.lang.Integer
		}	
		if ( vo.getAuthUrl() != null && !"".equals(vo.getAuthUrl())) {
			builder.and(userEntity.authUrl.equalsIgnoreCase(vo.getAuthUrl())); //java.lang.String
		}
		if ( vo.getCreateDate() != null ) {
			builder.and(userEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getClose() != null ) {
			builder.and(userEntity.close.eq(vo.getClose())); //java.lang.Boolean
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(userEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getAccount() != null && !"".equals(vo.getAccount())) {
			builder.and(userEntity.account.equalsIgnoreCase(vo.getAccount())); //java.lang.String
		}
		if ( vo.getEmail() != null && !"".equals(vo.getEmail())) {
			builder.and(userEntity.email.equalsIgnoreCase(vo.getEmail())); //java.lang.String
		}
		log.debug("UserRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(userRepository.findAll(builder.getValue()));	
	}
	
	@SuppressWarnings("unchecked")
	public List<UserEntity> searchWithVo(UserEntity vo, Pageable pageable) {
		log.debug("UserRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QUserEntity userEntity = QUserEntity.userEntity;

		if ( vo.getCompanyId() != null ) {
			builder.and(userEntity.companyId.eq(vo.getCompanyId())); //java.lang.Integer
		}	
		if ( vo.getAuthKey() != null && !"".equals(vo.getAuthKey())) {
			builder.and(userEntity.authKey.equalsIgnoreCase(vo.getAuthKey())); //java.lang.String
		}
		if ( vo.getDefaultTaxType() != null && !"".equals(vo.getDefaultTaxType())) {
			builder.and(userEntity.defaultTaxType.equalsIgnoreCase(vo.getDefaultTaxType())); //java.lang.String
		}
		if ( vo.getDefaultB2bFlag() != null && !"".equals(vo.getDefaultB2bFlag())) {
			builder.and(userEntity.defaultB2bFlag.equalsIgnoreCase(vo.getDefaultB2bFlag())); //java.lang.String
		}
		if ( vo.getPassword() != null && !"".equals(vo.getPassword())) {
			builder.and(userEntity.password.equalsIgnoreCase(vo.getPassword())); //java.lang.String
		}
		if ( vo.getUserId() != null ) {
			builder.and(userEntity.userId.eq(vo.getUserId())); //java.lang.Integer
		}	
		if ( vo.getRoleId() != null ) {
			builder.and(userEntity.roleId.eq(vo.getRoleId())); //java.lang.Integer
		}	
		if ( vo.getName() != null && !"".equals(vo.getName())) {
			builder.and(userEntity.name.equalsIgnoreCase(vo.getName())); //java.lang.String
		}
		if ( vo.getCreatorId() != null ) {
			builder.and(userEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(userEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getLogoutTime() != null ) {
			builder.and(userEntity.logoutTime.eq(vo.getLogoutTime())); //java.lang.Integer
		}	
		if ( vo.getPrinterId() != null ) {
			builder.and(userEntity.printerId.eq(vo.getPrinterId())); //java.lang.Integer
		}	
		if ( vo.getAuthUrl() != null && !"".equals(vo.getAuthUrl())) {
			builder.and(userEntity.authUrl.equalsIgnoreCase(vo.getAuthUrl())); //java.lang.String
		}
		if ( vo.getCreateDate() != null ) {
			builder.and(userEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getClose() != null ) {
			builder.and(userEntity.close.eq(vo.getClose())); //java.lang.Boolean
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(userEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getAccount() != null && !"".equals(vo.getAccount())) {
			builder.and(userEntity.account.equalsIgnoreCase(vo.getAccount())); //java.lang.String
		}
		if ( vo.getEmail() != null && !"".equals(vo.getEmail())) {
			builder.and(userEntity.email.equalsIgnoreCase(vo.getEmail())); //java.lang.String
		}
		log.debug("UserRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(userRepository.findAll(builder.getValue(), pageable));	
	}
	
	@SuppressWarnings("unchecked")
	public List<UserEntity> searchWithVo(UserEntity vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchWithVo(vo, pageable);
	}
	
	@SuppressWarnings("unchecked")
	public List<UserEntity> searchLikeVo(UserEntity vo) {
		log.debug("UserRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QUserEntity userEntity = QUserEntity.userEntity;

		if ( vo.getCompanyId() != null ) {
			builder.and(userEntity.companyId.eq(vo.getCompanyId())); //java.lang.Integer
		}	
		if ( vo.getAuthKey() != null && !"".equals(vo.getAuthKey())) {
			builder.and(userEntity.authKey.containsIgnoreCase(vo.getAuthKey())); //java.lang.String
		}
		if ( vo.getDefaultTaxType() != null && !"".equals(vo.getDefaultTaxType())) {
			builder.and(userEntity.defaultTaxType.containsIgnoreCase(vo.getDefaultTaxType())); //java.lang.String
		}
		if ( vo.getDefaultB2bFlag() != null && !"".equals(vo.getDefaultB2bFlag())) {
			builder.and(userEntity.defaultB2bFlag.containsIgnoreCase(vo.getDefaultB2bFlag())); //java.lang.String
		}
		if ( vo.getPassword() != null && !"".equals(vo.getPassword())) {
			builder.and(userEntity.password.containsIgnoreCase(vo.getPassword())); //java.lang.String
		}
		if ( vo.getUserId() != null ) {
			builder.and(userEntity.userId.eq(vo.getUserId())); //java.lang.Integer
		}	
		if ( vo.getRoleId() != null ) {
			builder.and(userEntity.roleId.eq(vo.getRoleId())); //java.lang.Integer
		}	
		if ( vo.getName() != null && !"".equals(vo.getName())) {
			builder.and(userEntity.name.containsIgnoreCase(vo.getName())); //java.lang.String
		}
		if ( vo.getCreatorId() != null ) {
			builder.and(userEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(userEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getLogoutTime() != null ) {
			builder.and(userEntity.logoutTime.eq(vo.getLogoutTime())); //java.lang.Integer
		}	
		if ( vo.getPrinterId() != null ) {
			builder.and(userEntity.printerId.eq(vo.getPrinterId())); //java.lang.Integer
		}	
		if ( vo.getAuthUrl() != null && !"".equals(vo.getAuthUrl())) {
			builder.and(userEntity.authUrl.containsIgnoreCase(vo.getAuthUrl())); //java.lang.String
		}
		if ( vo.getCreateDate() != null ) {
			builder.and(userEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getClose() != null ) {
			builder.and(userEntity.close.eq(vo.getClose())); //java.lang.Boolean
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(userEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getAccount() != null && !"".equals(vo.getAccount())) {
			builder.and(userEntity.account.containsIgnoreCase(vo.getAccount())); //java.lang.String
		}
		if ( vo.getEmail() != null && !"".equals(vo.getEmail())) {
			builder.and(userEntity.email.containsIgnoreCase(vo.getEmail())); //java.lang.String
		}
		log.debug("UserRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(userRepository.findAll(builder.getValue()));
	}		
	
	

	@SuppressWarnings("unchecked")
	public List<UserEntity> searchLikeVo(UserEntity vo, Pageable pageable) {
		log.debug("UserRepositoryImpl searchWithVo vo: " + vo);
		BooleanBuilder builder = new BooleanBuilder();
		QUserEntity userEntity = QUserEntity.userEntity;

		if ( vo.getCompanyId() != null ) {
			builder.and(userEntity.companyId.eq(vo.getCompanyId())); //java.lang.Integer
		}	
		if ( vo.getAuthKey() != null && !"".equals(vo.getAuthKey())) {
			builder.and(userEntity.authKey.containsIgnoreCase(vo.getAuthKey())); //java.lang.String
		}
		if ( vo.getDefaultTaxType() != null && !"".equals(vo.getDefaultTaxType())) {
			builder.and(userEntity.defaultTaxType.containsIgnoreCase(vo.getDefaultTaxType())); //java.lang.String
		}
		if ( vo.getDefaultB2bFlag() != null && !"".equals(vo.getDefaultB2bFlag())) {
			builder.and(userEntity.defaultB2bFlag.containsIgnoreCase(vo.getDefaultB2bFlag())); //java.lang.String
		}
		if ( vo.getPassword() != null && !"".equals(vo.getPassword())) {
			builder.and(userEntity.password.containsIgnoreCase(vo.getPassword())); //java.lang.String
		}
		if ( vo.getUserId() != null ) {
			builder.and(userEntity.userId.eq(vo.getUserId())); //java.lang.Integer
		}	
		if ( vo.getRoleId() != null ) {
			builder.and(userEntity.roleId.eq(vo.getRoleId())); //java.lang.Integer
		}	
		if ( vo.getName() != null && !"".equals(vo.getName())) {
			builder.and(userEntity.name.containsIgnoreCase(vo.getName())); //java.lang.String
		}
		if ( vo.getCreatorId() != null ) {
			builder.and(userEntity.creatorId.eq(vo.getCreatorId())); //java.lang.Integer
		}	
		if ( vo.getModifierId() != null ) {
			builder.and(userEntity.modifierId.eq(vo.getModifierId())); //java.lang.Integer
		}	
		if ( vo.getLogoutTime() != null ) {
			builder.and(userEntity.logoutTime.eq(vo.getLogoutTime())); //java.lang.Integer
		}	
		if ( vo.getPrinterId() != null ) {
			builder.and(userEntity.printerId.eq(vo.getPrinterId())); //java.lang.Integer
		}	
		if ( vo.getAuthUrl() != null && !"".equals(vo.getAuthUrl())) {
			builder.and(userEntity.authUrl.containsIgnoreCase(vo.getAuthUrl())); //java.lang.String
		}
		if ( vo.getCreateDate() != null ) {
			builder.and(userEntity.createDate.eq(vo.getCreateDate())); //java.sql.Timestamp
		}	
		if ( vo.getClose() != null ) {
			builder.and(userEntity.close.eq(vo.getClose())); //java.lang.Boolean
		}	
		if ( vo.getModifyDate() != null ) {
			builder.and(userEntity.modifyDate.eq(vo.getModifyDate())); //java.sql.Timestamp
		}	
		if ( vo.getAccount() != null && !"".equals(vo.getAccount())) {
			builder.and(userEntity.account.containsIgnoreCase(vo.getAccount())); //java.lang.String
		}
		if ( vo.getEmail() != null && !"".equals(vo.getEmail())) {
			builder.and(userEntity.email.containsIgnoreCase(vo.getEmail())); //java.lang.String
		}
		log.debug("UserRepositoryImpl searchWithVo predicate  " + builder.getValue());
		return Lists.newArrayList(userRepository.findAll(builder.getValue(), pageable));
	}	
	
	@SuppressWarnings("unchecked")
	public List<UserEntity> searchLikeVo(UserEntity vo, int pageOffset, int pageSize) {
		Pageable pageable = new PageRequest(pageOffset, pageSize);
		return searchLikeVo(vo, pageable);
	}
	
}
