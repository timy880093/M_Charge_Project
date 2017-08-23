/**
 * 
 */
package com.meshinnovation.db.service;

import java.io.Serializable;

import com.meshinnovation.db.dao.GenericDao;
import com.meshinnovation.db.model.BaseObject;

/**
 * GenericFacade是一個GenericDao的Wrapper，類似Delegate Pattern。
 * 
 * @author Linus
 *
 */
public interface GenericFacade<T extends BaseObject, PrimaryKey extends Serializable, Dao extends GenericDao<T, PrimaryKey>> extends GenericDao<T, PrimaryKey> {
	/**
	 * 給繼承的Facade傳入依賴的GenericDao，最終implement的facade class必須加上@Autowired或是在spring設定將Dao的implementation注入。
	 * 
	 * @param genericDao
	 */
	void setGenericDao(Dao genericDao);
}
