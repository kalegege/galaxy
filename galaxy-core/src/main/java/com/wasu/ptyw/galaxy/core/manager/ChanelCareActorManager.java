package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelCareActorDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelCareActorQuery;

/**
 * @author wenguang
 * @date 2016年11月07日
 */
public interface ChanelCareActorManager {
	/**
	 * 新增
	 * 
	 * @param ChanelCareActorDO
	 * @return 对象ID
	 */
	public Long insert(ChanelCareActorDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param ChanelCareActorDO
	 * @return 更新成功的记录数
	 */
	public int update(ChanelCareActorDO baseDO) throws MyException;

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
	 * @return ChanelCareActorDO
	 */
	public ChanelCareActorDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<ChanelCareActorDO>
	 */
	public List<ChanelCareActorDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelCareActorDO>
	 */
	public List<ChanelCareActorDO> query(ChanelCareActorQuery query) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return ChanelCareActorDO
	 */
	public ChanelCareActorDO queryFirst(ChanelCareActorQuery query) throws MyException;

	/**
	 * 根据多个id更新状态
	 * 
	 * @param ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(List<Long> ids, int status) throws MyException;

}
