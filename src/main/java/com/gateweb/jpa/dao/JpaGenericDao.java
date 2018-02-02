package com.gateweb.jpa.dao;

import java.io.Serializable;

import com.gateweb.db.dao.GenericDao;

public interface JpaGenericDao<T extends Serializable, PrimaryKey extends Serializable> extends GenericDao<T, PrimaryKey> {


}
