package com.meshinnovation.jpa.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import com.meshinnovation.db.dao.GenericDao;
import com.meshinnovation.db.model.BaseObject;

public interface JpaGenericDao<T extends BaseObject, PrimaryKey extends Serializable> extends GenericDao<T, PrimaryKey> {

/*	*//**
	 * select data by single where condition.
	 *
	 * @param property
	 * @param value
	 * @return
	 *//*
	List<T> findByProperty(SingularAttribute<T, ?> property, Object value);*/

	/**
	 * Disable an object.
	 * @param id
	 */
	void disable(PrimaryKey id);
}
