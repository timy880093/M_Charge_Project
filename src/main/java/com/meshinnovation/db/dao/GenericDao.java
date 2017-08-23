package com.meshinnovation.db.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.meshinnovation.db.model.BaseObject;

/**
 * @author
 *
 * @param <T>
 * @param <PrimaryKey>
 */
public interface GenericDao<T extends BaseObject, PrimaryKey extends Serializable> {

	/**
	 * Generic method used to get all objects of a particular type. This is the
	 * same as lookup up all rows in a table.
	 *
	 * @return List of populated objects
	 */
	List<T> findAll();

	/**
	 * @return Number of All Rows
	 */
	int getAllCount();

	/**
	 * Generic method to get an object based on class and identifier. An
	 * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
	 * found.
	 *
	 * @param id
	 *            the identifier (primary key) of the object to get
	 * @return a populated object
	 */
	T findById(PrimaryKey id);

	/**
	 * @param id the identifier (primary key) of the object to get
	 * @return a populated object or null if not found
	 */
	T get(PrimaryKey id);

	/**
	 * Checks for existence of an object of type T using the id arg.
	 *
	 * @param id
	 *            the id of the entity
	 * @return - true if it exists, false if it doesn't
	 */
	boolean exists(PrimaryKey id);

	/**
	 * Generic method to save an object - handles both update and insert.
	 *
	 * @param object
	 *            the object to save
	 * @return the persisted object
	 */
	T save(T object);

	/**
	 * Generic method to save an object - handles both update and insert.
	 *
	 * @param object
	 *            the object to save
	 * @return the persisted object
	 */
	void update(T object);

	/**
	 * Generic method to delete an object based on class and id
	 *
	 * @param id
	 *            the identifier (primary key) of the object to remove
	 */
	void remove(PrimaryKey id);

	/**
	 * Gets all records without duplicates.
	 * <p>
	 * Note that if you use this method, it is imperative that your model
	 * classes correctly implement the hashcode/equals methods
	 * </p>
	 *
	 * @return List of populated objects
	 */
	List<T> getAllDistinct();

	/**
	 * Find a list of records by using a named query
	 *
	 * @param queryName
	 *            query name of the named query
	 * @param queryParams
	 *            a map of the query names and the values
	 * @return a list of the records found
	 */
	List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams);

	/**
	 * @param ql query language
	 * @param queryParams
	 * @return
	 */
	List<T> findByQuery(String ql, Map<String, Object> queryParams);

	/**
	 * @param <P> expect return type
	 * @param ql query language
	 * @param queryParams
	 * @param clazz
	 * @return the unique result
	 */
	<P> P retrieveUniqueResult(String ql, Map<String, Object> queryParams, Class<P> clazz);

	/**
     *
     */
	void clear();

	/**
     *
     */
	void flush();

	/**
     *
     * @param obj
     */
	void reflesh(T obj);

	/**
	 * @param pageNo
	 * @param noRowsPerPage
	 * @return
	 */
	List<T> findSome(int pageNo, int noRowsPerPage);

	/**
	 * @param pageNo
	 * @param noRowsPerPage
	 * @param ql
	 * @param params
	 * @return
	 */
	List<T> findSomeByQuery(int pageNo, int noRowsPerPage, String ql, Map<String, Object> params);
}