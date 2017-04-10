package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyDiscountDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyDiscountQuery;

/**
 * @author wenguang
 * @date 2015年09月01日
 */
public interface GalaxyDiscountManager {
	/**
	 * 新增
	 * 
	 * @param DiscountDTO
	 * @return 对象ID
	 */
	public Long insert(GalaxyDiscountDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param DiscountDTO
	 * @return 更新成功的记录数
	 */
	public int update(GalaxyDiscountDO baseDO) throws MyException;

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
	 * @return GalaxyDiscountDO
	 */
	public GalaxyDiscountDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<GalaxyDiscountDO>
	 */
	public List<GalaxyDiscountDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<GalaxyDiscountDO>
	 */
	public List<GalaxyDiscountDO> query(GalaxyDiscountQuery query) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return GalaxyDiscountDO
	 */
	public GalaxyDiscountDO queryFirst(GalaxyDiscountQuery query) throws MyException;

	/**
	 * 根据多个id更新状态
	 * 
	 * @param ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(List<Long> ids, int status) throws MyException;

}
