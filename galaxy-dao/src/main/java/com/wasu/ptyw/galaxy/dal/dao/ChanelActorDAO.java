package com.wasu.ptyw.galaxy.dal.dao;

import java.util.List;
import java.util.Map;

import com.wasu.ptyw.galaxy.dal.dataobject.ChanelActorDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2016年08月24日
 */
public interface ChanelActorDAO extends BaseDAO<ChanelActorDO> {

	/**
	 * 根据多个id更新状态
	 * 
	 * @param map
	 *            :int status,List ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(Map<String, Object> map) throws DAOException;

	List<ChanelActorDO> queryRecommend(SimpleQuery query) throws DAOException;

	List<ChanelActorDO> getRecommend(SimpleQuery query) throws DAOException;

	List<ChanelActorDO> queryOnline(SimpleQuery query) throws DAOException;

	List<ChanelActorDO> queryOffline(SimpleQuery query) throws DAOException;

	List<ChanelActorDO> queryOffline1(SimpleQuery query) throws DAOException;

	int queryCountOnline(SimpleQuery query) throws DAOException;

	int queryCountOffline(SimpleQuery query) throws DAOException;

	List<ChanelActorDO> getByActorIds(List<Integer> ids) throws DAOException;

	int publishById(long id) throws DAOException;
}