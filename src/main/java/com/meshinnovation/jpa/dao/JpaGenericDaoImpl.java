package com.meshinnovation.jpa.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.meshinnovation.db.model.BaseObject;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 *
 * @author pkliu
 *
 * @param <T>
 * @param <PrimaryKey>
 */
@Repository
public abstract class JpaGenericDaoImpl<T extends BaseObject, PrimaryKey extends Serializable> implements JpaGenericDao<T, PrimaryKey> {
	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass())
	 * from Commons Logging
	 */
	protected final Log log = LogFactory.getLog(getClass());

	/**
     *
     */
	@PersistenceContext
	protected EntityManager entityManager;

	/**
     *
     */
	private Class<T> persistentClass;

	private Class<PrimaryKey> primaryKeyClass;

	/**
     *
     */
	@SuppressWarnings("unchecked")
	public JpaGenericDaoImpl() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.primaryKeyClass = (Class<PrimaryKey>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
	}

   /*
    * (non-Javadoc)
    * @see com.sysfoundry.base.GenericDao#clear()
    */
	public void clear() {
		entityManager.clear();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sysfoundry.base.GenericDao#flush()
	 */
	public void flush() {
		entityManager.flush();
	}

	public void disable(PrimaryKey id){
//		String idName = entityManager.getMetamodel().entity(persistentClass).getId(primaryKeyClass).getName();
//		StringBuilder sb = new StringBuilder();
//		sb.append("update ").append(persistentClass.getName());
//		sb.append(" set enabled = :enabled where ").append(idName).append(" = :id");
//		Query query = entityManager.createQuery(sb.toString());
//		query.setParameter("enabled", false);
//		query.setParameter("id", id);
//		int executeUpdate = query.executeUpdate();
		com.meshinnovation.jpa.model.JpaBaseObject object = (com.meshinnovation.jpa.model.JpaBaseObject)findById(id);
		object.setEnabled(false);
		entityManager.merge(object);
	}

	/**
	 * {@inheritDoc}
	 */
	/* (non-Javadoc)
	 * @see com.meshinnovation.db.dao.GenericDao#findAll()
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		List<T> results = entityManager.createQuery("select o from " + persistentClass.getName() + " o").getResultList();
		return results;
	}

	/*
	 * (non-Javadoc)
	 * @see com.sysfoundry.base.GenericDao#getAllDistinct()
	 */
	public List<T> getAllDistinct() {
		Collection<T> result = new LinkedHashSet<T>(findAll());
		return new ArrayList<T>(result);
	}

	/*
	 * (non-Javadoc)
	 * @see com.sysfoundry.base.GenericDao#findById(java.io.Serializable)
	 */
	public T findById(PrimaryKey id) {
		T entity = get(id);

		if (entity == null) {
			log.warn("JpaGenericDaoImpl findById Uh oh, '"
					+ this.persistentClass + "' object with id '" + id
					+ "' not found...");
			throw new EntityNotFoundException("JPA10003-"
					+ this.persistentClass.getName() + "  id = " + id
					+ " not found.");
		}

		return entity;
	}

	/* (non-Javadoc)
	 * @see com.meshinnovation.db.dao.GenericDao#get(java.io.Serializable)
	 */
	public T get(PrimaryKey id) {
		T entity = entityManager.find(this.persistentClass, id);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * @see com.sysfoundry.base.GenericDao#exists(java.io.Serializable)
	 */
	public boolean exists(PrimaryKey id) {
		T entity = get(id);
		return entity != null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.sysfoundry.base.GenericDao#save(java.lang.Object)
	 */
	public T save(T object) {
		entityManager.persist(object);
		return object;
	}

	/*
	 * (non-Javadoc)
	 * @see com.sysfoundry.base.GenericDao#update(java.lang.Object)
	 */
	public void update(T object) {
		entityManager.merge(object);
	}

	/*
	 * (non-Javadoc)
	 * @see com.sysfoundry.base.GenericDao#remove(java.io.Serializable)
	 */
	public void remove(PrimaryKey id) {
		entityManager.remove(this.findById(id));
	}

	/*
	 * (non-Javadoc)
	 * @see com.sysfoundry.base.GenericDao#findByNamedQuery(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
		Query query = entityManager.createNamedQuery(queryName);

		setQueryParameters(query, queryParams);

		List<T> result = query.getResultList();
		return result;
	}

/*	@SuppressWarnings("unchecked")
	public List<T> findByProperty(final SingularAttribute<T, ?> property, final Object value) {
		return new JpaTemplate(entityManager).executeFind(new JpaCallback<List<T>>(){
			public List<T> doInJpa(EntityManager em) throws PersistenceException {
				CriteriaBuilder builder = em.getCriteriaBuilder();
				CriteriaQuery<T> criteria = builder.createQuery(persistentClass);
				Root<T> entityRoot = criteria.from(persistentClass);
				criteria.select(entityRoot);
				criteria.where(builder.equal(entityRoot.get(property), value));
				List<T> e = em.createQuery(criteria).getResultList();
				return e;
			}
		});
	}*/

	/* (non-Javadoc)
	 * @see com.meshinnovation.db.dao.GenericDao#findSome(int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<T> findSome(int pageNo, int noRowsPerPage) {
		Query q = entityManager.createQuery("select obj from " + persistentClass.getName() + " obj");

		setPagingConstraint(q, pageNo, noRowsPerPage);

		List<T> results = q.getResultList();
		return results;
	}

	/* (non-Javadoc)
	 * @see com.meshinnovation.db.dao.GenericDao#getAllCount()
	 */
	public int getAllCount() {
		Long numberOfObjects = (Long) entityManager.createQuery(
				"select count(obj) from " + persistentClass.getName()
						+ " obj").getSingleResult();
		return numberOfObjects.intValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sysfoundry.base.GenericDao#reflesh(java.lang.Object)
	 */
	public void reflesh(T obj) {
		entityManager.refresh(obj);
	}

	/* (non-Javadoc)
	 * @see com.meshinnovation.db.dao.GenericDao#retrieveUniqueResult(java.lang.String, java.util.Map, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <P> P retrieveUniqueResult(String ql, Map<String, Object> queryParams, Class<P> clazz) {
		Query query = entityManager.createQuery(ql);

		setQueryParameters(query, queryParams);

		P result = (P)query.getSingleResult();
		return result;
	}

	/* (non-Javadoc)
	 * @see com.meshinnovation.db.dao.GenericDao#findByQuery(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByQuery(String ql, Map<String, Object> queryParams) {
		Query query = entityManager.createQuery(ql);

		setQueryParameters(query, queryParams);

		List<T> result = query.getResultList();
		return result;
	}

	/* (non-Javadoc)
	 * @see com.meshinnovation.db.dao.GenericDao#findSomeByQuery(int, int, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<T> findSomeByQuery(int pageNo, int noRowsPerPage, String ql,
			Map<String, Object> params) {
		Query q = entityManager.createQuery(ql);

		setQueryParameters(q, params);

		setPagingConstraint(q, pageNo, noRowsPerPage);

		List<T> results = q.getResultList();
		return results;
	}

	/**
	 * 將map中的parameter塞入query
	 *
	 * @param query
	 * @param params
	 */
	protected void setQueryParameters(Query query, Map<String, Object> params) {
		if(params != null){
			for(String paramName: params.keySet()){
				query.setParameter(paramName, params.get(paramName));
			}
		}
	}

	/**
	 * 設定分頁時的first result & max results
	 *
	 * @param query
	 * @param pageNo
	 * @param noRowsPerPage
	 */
	protected void setPagingConstraint(Query query, int pageNo, int noRowsPerPage) {
		if (pageNo > 0 && noRowsPerPage > 0) {
			query.setFirstResult(noRowsPerPage * (pageNo - 1)).setMaxResults(noRowsPerPage);
		}
	}

}