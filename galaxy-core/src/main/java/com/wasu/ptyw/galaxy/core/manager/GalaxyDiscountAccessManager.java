package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyDiscountAccessDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyDiscountAccessQuery;

/**
 * @author wenguang
 * @date 2015年09月25日
 */
public interface GalaxyDiscountAccessManager {
	/**
	 * 新增
	 * 
	 * @param GalaxyDiscountAccessDO
	 * @return 对象ID
	 */
	public Long insert(GalaxyDiscountAccessDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param GalaxyDiscountAccessDO
	 * @return 更新成功的记录数
	 */
	public int update(GalaxyDiscountAccessDO baseDO) throws MyException;

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
	 * @return GalaxyDiscountAccessDO
	 */
	public GalaxyDiscountAccessDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<GalaxyDiscountAccessDO>
	 */
	public List<GalaxyDiscountAccessDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<GalaxyDiscountAccessDO>
	 */
	public List<GalaxyDiscountAccessDO> query(GalaxyDiscountAccessQuery query) throws MyException;

	/**
	 * 查询总数
	 * 
	 * @param query
	 * @return int
	 */
	public int queryCount(GalaxyDiscountAccessQuery query) throws MyException;

	/**
	 * 查询第一个
	 * 
	 * @param query
	 * @return GalaxyDiscountAccessDO
	 */
	public GalaxyDiscountAccessDO queryFirst(GalaxyDiscountAccessQuery query) throws MyException;

	/**
	 * 根据多个id更新状态
	 * 
	 * @param ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(List<Long> ids, int status) throws MyException;

}
