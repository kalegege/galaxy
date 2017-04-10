package com.wasu.ptyw.galaxy.dal.dao;

import java.util.Map;

import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmSectionDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2015年12月14日
 */
public interface GalaxyFilmSectionDAO extends BaseDAO<GalaxyFilmSectionDO> {

	/**
	 * 根据多个id更新状态
	 * 
	 * @param map
	 *            :int status,List ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(Map<String, Object> map) throws DAOException;
	
	public int deleteByQuery(SimpleQuery query) throws DAOException;

}