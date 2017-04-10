package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyWeixinFollowDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyWeixinFollowQuery;

/**
 * @author wenguang
 * @date 2015年09月15日
 */
public interface GalaxyWeixinFollowManager {
	/**
	 * 新增
	 * 
	 * @param GalaxyWeixinFollowDO
	 * @return 对象ID
	 */
	public Long insert(GalaxyWeixinFollowDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param GalaxyWeixinFollowDO
	 * @return 更新成功的记录数
	 */
	public int update(GalaxyWeixinFollowDO baseDO) throws MyException;

	/**
	 * 根据id删除
	 * 
	 * @param id
	 * @return 删除成功的记录数
	 */
	public int deleteById(long id) throws MyException;

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return GalaxyWeixinFollowDO
	 */
	public GalaxyWeixinFollowDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<GalaxyWeixinFollowDO>
	 */
	public List<GalaxyWeixinFollowDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 查询总数
	 * 
	 * @param query
	 * @return int
	 */
	public int queryCount(GalaxyWeixinFollowQuery query) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<GalaxyWeixinFollowDO>
	 */
	public List<GalaxyWeixinFollowDO> query(GalaxyWeixinFollowQuery query) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return GalaxyWeixinFollowDO
	 */
	public GalaxyWeixinFollowDO queryFirst(GalaxyWeixinFollowQuery query) throws MyException;

	/**
	 * 根据多个id更新状态
	 * 
	 * @param ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(List<Long> ids, int status) throws MyException;

	/**
	 * 根据多个id更新状态
	 * 
	 * @param map
	 *            :int status,SimpleQuery query
	 * @return 更新成功的记录数
	 */
	public int updateStatusByQuery(GalaxyWeixinFollowQuery query, int status) throws MyException;

	/**
	 * 根据多个id更新使用状态
	 * 
	 * @param ids
	 * @return 更新成功的记录数
	 */
	public int updateUsedStatusByIds(List<Long> ids, int status) throws MyException;
}
