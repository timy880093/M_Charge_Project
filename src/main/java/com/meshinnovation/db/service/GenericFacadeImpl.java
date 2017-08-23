package com.meshinnovation.db.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.meshinnovation.db.dao.GenericDao;
import com.meshinnovation.db.model.BaseObject;

public abstract class GenericFacadeImpl<T extends BaseObject, PrimaryKey extends Serializable, Dao extends GenericDao<T, PrimaryKey>>
		implements GenericFacade<T, PrimaryKey, Dao> {

	protected Dao genericDao;

	public List<T> findAll() {
		return genericDao.findAll();
	}

	public int getAllCount() {
		return genericDao.getAllCount();
	}

	public T findById(PrimaryKey id) {
		return genericDao.findById(id);
	}

	public T get(PrimaryKey id) {
		return genericDao.get(id);
	}

	public boolean exists(PrimaryKey id) {
		return genericDao.exists(id);
	}

	public T save(T object) {
		return genericDao.save(object);
	}

	public void update(T object) {
		genericDao.update(object);
	}

	public void remove(PrimaryKey id) {
		genericDao.remove(id);
	}

	public List<T> getAllDistinct() {
		return genericDao.getAllDistinct();
	}

	public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
		return genericDao.findByNamedQuery(queryName, queryParams);
	}

	public void clear() {
		genericDao.clear();
	}

	public void flush() {
		genericDao.flush();
	}

	public void reflesh(T obj) {
		genericDao.reflesh(obj);
	}

	public List<T> findSome(int pageNo, int noRowsPerPage) {
		return genericDao.findSome(pageNo, noRowsPerPage);
	}

	public <P> P retrieveUniqueResult(String ql, Map<String, Object> queryParams, Class<P> clazz) {
		return genericDao.retrieveUniqueResult(ql, queryParams, clazz);
	}

	public List<T> findByQuery(String ql, Map<String, Object> queryParams) {
		return genericDao.findByQuery(ql, queryParams);
	}

	public List<T> findSomeByQuery(int pageNo, int noRowsPerPage, String ql, Map<String, Object> params) {
		return genericDao.findSomeByQuery(pageNo, noRowsPerPage, ql, params);
	}

}
