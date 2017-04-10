package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyFilmQuery;

/**
 * @author wenguang
 * @date 2015年09月22日
 */
public interface GalaxyFilmManager {
	/**
	 * 新增
	 * 
	 * @param FilmDTO
	 * @return 对象ID
	 */
	public Long insert(GalaxyFilmDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param FilmDTO
	 * @return 更新成功的记录数
	 */
	public int update(GalaxyFilmDO baseDO) throws MyException;

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
	 * @return GalaxyFilmDO
	 */
	public GalaxyFilmDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<GalaxyFilmDO>
	 */
	public List<GalaxyFilmDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<GalaxyFilmDO>
	 */
	public List<GalaxyFilmDO> query(GalaxyFilmQuery query) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return GalaxyFilmDO
	 */
	public GalaxyFilmDO queryFirst(GalaxyFilmQuery query) throws MyException;

	/**
	 * 根据多个id更新状态
	 * 
	 * @param ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(List<Long> ids, int status) throws MyException;

}
