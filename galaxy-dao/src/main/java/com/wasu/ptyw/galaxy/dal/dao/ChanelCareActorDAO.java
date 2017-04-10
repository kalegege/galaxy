package com.wasu.ptyw.galaxy.dal.dao;

import java.util.Map;

import com.wasu.ptyw.galaxy.dal.dataobject.ChanelCareActorDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;

/**
 * @author wenguang
 * @date 2016年11月07日
 */
public interface ChanelCareActorDAO extends BaseDAO<ChanelCareActorDO> {

	/**
	 * 根据多个id更新状态
	 * 
	 * @param map
	 *            :int status,List ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(Map<String, Object> map) throws DAOException;

}