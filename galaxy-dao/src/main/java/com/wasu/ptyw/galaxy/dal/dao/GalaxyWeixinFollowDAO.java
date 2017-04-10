package com.wasu.ptyw.galaxy.dal.dao;

import java.util.Map;

import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyWeixinFollowDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;

/**
 * @author wenguang
 * @date 2015年09月16日
 */
public interface GalaxyWeixinFollowDAO extends BaseDAO<GalaxyWeixinFollowDO> {

	/**
	 * 根据多个id更新状态
	 * 
	 * @param map
	 *            :int status,List ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(Map<String, Object> map) throws DAOException;

	/**
	 * 根据多个id更新状态
	 * 
	 * @param map
	 *            :int status,SimpleQuery query
	 * @return 更新成功的记录数
	 */
	public int updateStatusByQuery(Map<String, Object> map) throws DAOException;

	/**
	 * 根据多个id更新使用状态
	 * 
	 * @param map
	 *            :int status,List ids
	 * @return 更新成功的记录数
	 */
	public int updateUsedStatusByIds(Map<String, Object> map) throws DAOException;

}