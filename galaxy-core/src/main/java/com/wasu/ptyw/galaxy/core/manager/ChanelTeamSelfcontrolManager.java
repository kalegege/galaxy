package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamSelfcontrolDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamSelfcontrolQuery;

/**
 * @author wenguang
 * @date 2016年07月21日
 */
public interface ChanelTeamSelfcontrolManager {
	/**
	 * 新增
	 * 
	 * @param ChanelTeamSelfcontrolDO
	 * @return 对象ID
	 */
	public Long insert(ChanelTeamSelfcontrolDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param ChanelTeamSelfcontrolDO
	 * @return 更新成功的记录数
	 */
	public int update(ChanelTeamSelfcontrolDO baseDO) throws MyException;

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
	 * @return ChanelTeamSelfcontrolDO
	 */
	public ChanelTeamSelfcontrolDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<ChanelTeamSelfcontrolDO>
	 */
	public List<ChanelTeamSelfcontrolDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelTeamSelfcontrolDO>
	 */
	public List<ChanelTeamSelfcontrolDO> query(ChanelTeamSelfcontrolQuery query) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return ChanelTeamSelfcontrolDO
	 */
	public ChanelTeamSelfcontrolDO queryFirst(ChanelTeamSelfcontrolQuery query) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelTeamSelfcontrolDO>
	 */
	public List<ChanelTeamSelfcontrolDO> getrecommend(ChanelTeamSelfcontrolQuery query) throws MyException;
}
