package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmSectionDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyFilmSectionQuery;

/**
 * @author wenguang
 * @date 2015年09月30日
 */
public interface GalaxyFilmSectionManager {
	/**
	 * 新增
	 * 
	 * @param GalaxyFilmSectionDO
	 * @return 对象ID
	 */
	public Long insert(GalaxyFilmSectionDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param GalaxyFilmSectionDO
	 * @return 更新成功的记录数
	 */
	public int update(GalaxyFilmSectionDO baseDO) throws MyException;

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
	 * @return GalaxyFilmSectionDO
	 */
	public GalaxyFilmSectionDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<GalaxyFilmSectionDO>
	 */
	public List<GalaxyFilmSectionDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<GalaxyFilmSectionDO>
	 */
	public List<GalaxyFilmSectionDO> query(GalaxyFilmSectionQuery query) throws MyException;

	/**
	 * 查询总数
	 * 
	 * @param query
	 * @return int
	 */
	public int queryCount(GalaxyFilmSectionQuery query) throws MyException;

	/**
	 * 查询第一个
	 * 
	 * @param query
	 * @return GalaxyFilmSectionDO
	 */
	public GalaxyFilmSectionDO queryFirst(GalaxyFilmSectionQuery query) throws MyException;

	/**
	 * 根据多个id更新状态
	 * 
	 * @param ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(List<Long> ids, int status) throws MyException;
	
	public int deleteByQuery(GalaxyFilmSectionQuery query) throws MyException;

}
