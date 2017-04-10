package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyOrderFilmQuery;

/**
 * @author wenguang
 * @date 2015年06月24日
 */
public interface GalaxyOrderFilmManager {
	/**
	 * 新增
	 * 
	 * @param OrderFilmDTO
	 * @return 对象ID
	 */
	public Long insert(GalaxyOrderFilmDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param OrderFilmDTO
	 * @return 更新成功的记录数
	 */
	public int update(GalaxyOrderFilmDO baseDO) throws MyException;

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
	 * @return GalaxyOrderFilmDO
	 */
	public GalaxyOrderFilmDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<GalaxyOrderFilmDO>
	 */
	public List<GalaxyOrderFilmDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<GalaxyOrderFilmDO>
	 */
	public List<GalaxyOrderFilmDO> query(GalaxyOrderFilmQuery query) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return GalaxyOrderFilmDO
	 */
	public GalaxyOrderFilmDO queryFirst(GalaxyOrderFilmQuery query) throws MyException;

	/**
	 * 根据多个id更新状态
	 * 
	 * @param ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(List<Long> ids, int status) throws MyException;

}
