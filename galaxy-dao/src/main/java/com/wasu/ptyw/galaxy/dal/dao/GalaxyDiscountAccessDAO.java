package com.wasu.ptyw.galaxy.dal.dao;

import java.util.Map;

import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyDiscountAccessDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;

/**
 * @author wenguang
 * @date 2015年09月25日
 */
public interface GalaxyDiscountAccessDAO extends BaseDAO<GalaxyDiscountAccessDO> {

	/**
	 * 根据多个id更新状态
	 * 
	 * @param map
	 *            :int status,List ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(Map<String, Object> map) throws DAOException;

}