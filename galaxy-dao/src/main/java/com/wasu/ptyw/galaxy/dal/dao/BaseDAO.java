package com.wasu.ptyw.galaxy.dal.dao;

import java.util.List;

import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

public interface BaseDAO<T> {

	long insert(T obj) throws DAOException;

	int update(T obj) throws DAOException;

	int deleteById(Long id) throws DAOException;

	// void deleteByQuery(SimpleQuery query) throws DAOException;

	T getById(Long id) throws DAOException;
	
	List<T> getByIds(List<Long> ids) throws DAOException;

	List<T> query(SimpleQuery query) throws DAOException;
	
	int queryCount(SimpleQuery query) throws DAOException;
	
	int updateOnly(ChanelTeamDO baseDO) throws DAOException;
}