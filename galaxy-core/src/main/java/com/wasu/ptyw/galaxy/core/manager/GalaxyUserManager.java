package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyUserDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyUserQuery;

/**
 * @author wenguang
 * @date 2015年06月24日
 */
public interface GalaxyUserManager {
	/**
	 * 新增
	 * 
	 * @param GalaxyUserDO
	 * @return 对象ID
	 */
	public Long insert(GalaxyUserDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param GalaxyUserDO
	 * @return 更新成功的记录数
	 */
	public int update(GalaxyUserDO baseDO) throws MyException;

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
	 * @return GalaxyUserDO
	 */
	public GalaxyUserDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<GalaxyUserDO>
	 */
	public List<GalaxyUserDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<GalaxyUserDO>
	 */
	public List<GalaxyUserDO> query(GalaxyUserQuery query) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return GalaxyUserDO
	 */
	public GalaxyUserDO queryFirst(GalaxyUserQuery query) throws MyException;

	/**
	 * 根据多个id更新状态
	 * 
	 * @param ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(List<Long> ids, int status) throws MyException;

}
