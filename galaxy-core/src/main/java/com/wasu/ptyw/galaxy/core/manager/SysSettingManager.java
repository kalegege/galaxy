package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.SysSettingDO;
import com.wasu.ptyw.galaxy.dal.query.SysSettingQuery;

/**
 * @author wenguang
 * @date 2015年09月16日
 */
public interface SysSettingManager {
	/**
	 * 新增
	 * 
	 * @param SysSettingDO
	 * @return 对象ID
	 */
	public Long insert(SysSettingDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param SysSettingDO
	 * @return 更新成功的记录数
	 */
	public int update(SysSettingDO baseDO) throws MyException;

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
	 * @return SysSettingDO
	 */
	public SysSettingDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<SysSettingDO>
	 */
	public List<SysSettingDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<SysSettingDO>
	 */
	public List<SysSettingDO> query(SysSettingQuery query) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return SysSettingDO
	 */
	public SysSettingDO queryFirst(SysSettingQuery query) throws MyException;

}
