package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemTeamDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemTeamQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
public interface ChanelItemTeamManager {
	/**
	 * 新增
	 * 
	 * @param ChanelItemTeamDO
	 * @return 对象ID
	 */
	public Long insert(ChanelItemTeamDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param ChanelItemTeamDO
	 * @return 更新成功的记录数
	 */
	public int update(ChanelItemTeamDO baseDO) throws MyException;
	
	/**
	 * 更新
	 * 
	 * @param ChanelItemTeamDO
	 * @return 更新成功的记录数
	 */
	public int updateItemTeam(ChanelItemTeamDO baseDO) throws MyException;

	/**
	 * 根据id删除
	 * 
	 * @param id
	 * @return 删除成功的记录数
	 */
	public int deleteById(long id) throws MyException;
	
	/**
	 * 根据bid和chid删除
	 * 
	 * @param id
	 * @return 删除成功的记录数
	 */
	public int deleteByIds(ChanelItemTeamDO baseDO) throws MyException;
	
	/**
	 * 根据bid和chid删除
	 * 
	 * @param id
	 * @return 删除成功的记录数
	 */
	public int deleteByOrder(ChanelItemTeamDO baseDO) throws MyException;

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return ChanelItemTeamDO
	 */
	public ChanelItemTeamDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<ChanelItemTeamDO>
	 */
	public List<ChanelItemTeamDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelItemTeamDO>
	 */
	public List<ChanelItemTeamDO> query(ChanelItemTeamQuery query) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return ChanelItemTeamDO
	 */
	public ChanelItemTeamDO queryFirst(ChanelItemTeamQuery query) throws MyException;

	/**
	 * 根据bid查询
	 * 
	 * @param bIds
	 * @return
	 * @throws DAOException
	 */
	public List<ChanelItemTeamDO> getByTeamId(ChanelItemTeamQuery query) throws MyException;
	
	public List<ChanelItemTeamDO> getByTeam(ChanelItemTeamQuery query) throws MyException;
	
	//分组内频道排序
	public List<ChanelItemTeamDO> getByTeam1(ChanelItemTeamQuery query) throws MyException;

	
	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<ChanelItemTeamDO>
	 */
	public List<ChanelItemTeamDO> queryAllByTeamId(ChanelItemTeamQuery query) throws MyException;
	
	public List<ChanelItemTeamDO> queryAllByTeamIds(List<Long> ids) throws MyException;
	
	/**
	 * 根据bid查询
	 * 
	 * @param bIds
	 * @return
	 * @throws DAOException
	 */
	public List<ChanelItemTeamDO> getAllByTeamId(ChanelItemTeamQuery query) throws MyException;
	
	
	public int updateOnly(ChanelItemTeamDO baseDO) throws MyException;
}
