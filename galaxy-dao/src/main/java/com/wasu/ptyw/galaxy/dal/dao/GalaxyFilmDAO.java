package com.wasu.ptyw.galaxy.dal.dao;

import java.util.Map;

import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;

/**
 * @author wenguang
 * @date 2015年12月11日
 */
public interface GalaxyFilmDAO extends BaseDAO<GalaxyFilmDO> {

	/**
	 * 根据多个id更新状态
	 * 
	 * @param map
	 *            :int status,List ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(Map<String, Object> map) throws DAOException;

}