package com.wasu.ptyw.galaxy.dal.dao;

import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyHistoryDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2015年07月07日
 */
public interface GalaxyHistoryDAO extends BaseDAO<GalaxyHistoryDO> {
	int deleteByQuery(SimpleQuery query) throws DAOException;
}