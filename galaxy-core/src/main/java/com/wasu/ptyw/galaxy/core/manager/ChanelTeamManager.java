package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.AssertMenuAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamDO;
import com.wasu.ptyw.galaxy.dal.dataobject.MenuAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.MenuDetailDO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamAllDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
public interface ChanelTeamManager {
	/**
	 * 新增
	 * 
	 * @param ChanelTeamDO
	 * @return 对象ID
	 */
	public Long insert(ChanelTeamDO baseDO) throws MyException;

	/**
	 * 新增
	 * 
	 * @param ChanelTeamDO
	 * @return 对象ID
	 */
	public Long insertTeam(ChanelTeamDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param ChanelTeamDO
	 * @return 更新成功的记录数
	 */
	public int update(ChanelTeamDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param ChanelTeamDO
	 * @return 更新成功的记录数
	 */
	public int updateTeam(ChanelTeamDO baseDO) throws MyException;

	/**
	 * 根据id删除
	 * 
	 * @param id
	 * @return 删除成功的记录数
	 */
	public int deleteById(long id) throws MyException;

	/**
	 * 根据id删除
	 * 
	 * @param id
	 * @return 删除成功的记录数
	 */
	public int deleteTeam(long id) throws MyException;

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return ChanelTeamDO
	 */
	public ChanelTeamDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<ChanelTeamDO>
	 */
	public List<ChanelTeamDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelTeamDO>
	 */
	public List<ChanelTeamDO> query(ChanelTeamQuery query) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelTeamDO>
	 */
	public List<ChanelTeamDO> queryType(ChanelTeamQuery query) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelTeamDO>
	 */
	public List<ChanelTeamDO> queryTeam(ChanelTeamQuery query) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return ChanelTeamDO
	 */
	public ChanelTeamDO queryFirst(ChanelTeamQuery query) throws MyException;

	/**
	 * 分页区域查询
	 * 
	 * @param query
	 * @return List<MenuDetailDO>
	 */
	public List<ChanelTeamDO> queryByRegion(ChanelTeamQuery query) throws MyException;

	public List<ChanelTeamDO> queryCacheByRegion(String regionId) throws MyException;

	/**
	 * 获取所有频道当天节目单
	 * 
	 * @param queryadd
	 * @return MenuDetailDO
	 */
	public MenuAllDO getMenu(String regionId) throws MyException;

	/**
	 * 获取所有频道当天节目单Cache
	 * 
	 * @param queryadd
	 * @return MenuDetailDO
	 */
	// public MenuAllDO getMenuCache(final ChanelTeamQuery query) throws
	// MyException;

	/**
	 * 获取频道当天节目单
	 * 
	 * @param queryadd
	 * @return MenuDetailDO
	 */
	public MenuAllDO getSingleMenu(ChanelTeamQuery query, String chid) throws MyException;

	public MenuAllDO getCurrentMenu(String regionId, String chid) throws MyException;

	/**
	 * 查询接口分组详细信息
	 * 
	 * @param queryadd
	 * @return List<ChanelItemDO>
	 */
	public TeamAllDO getTeam(ChanelTeamQuery query) throws MyException;

	public AssertMenuAllDO getReplay(ChanelTeamQuery query) throws MyException;

	public int updateOnly(ChanelTeamDO chanelTeamDO) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param queryByBid
	 * @return ChanelTeamDO
	 */
	public ChanelTeamDO queryByBid(ChanelTeamQuery query) throws MyException;
}
