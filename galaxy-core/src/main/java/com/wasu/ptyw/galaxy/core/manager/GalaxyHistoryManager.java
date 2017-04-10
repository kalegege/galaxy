package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyHistoryDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyHistoryQuery;

/**
 * @author wenguang
 * @date 2015年07月01日
 */
public interface GalaxyHistoryManager {
	/**
	 * 新增
	 * 
	 * @param HistoryDTO
	 * @return 对象ID
	 */
	public Long insert(GalaxyHistoryDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param HistoryDTO
	 * @return 更新成功的记录数
	 */
	public int update(GalaxyHistoryDO baseDO) throws MyException;

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
	 * @return GalaxyHistoryDO
	 */
	public GalaxyHistoryDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<GalaxyHistoryDO>
	 */
	public List<GalaxyHistoryDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<GalaxyHistoryDO>
	 */
	public List<GalaxyHistoryDO> query(GalaxyHistoryQuery query) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return GalaxyHistoryDO
	 */
	public GalaxyHistoryDO queryFirst(GalaxyHistoryQuery query) throws MyException;
	
	/**
	 * 批量删除
	 * 
	 * @param query
	 * @return int
	 */
	public int deleteByQuery(GalaxyHistoryQuery query) throws MyException;

}
