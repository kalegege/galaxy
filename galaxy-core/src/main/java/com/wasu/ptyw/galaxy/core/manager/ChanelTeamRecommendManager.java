package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamRecommendDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamRecommendQuery;

/**
 * @author wenguang
 * @date 2016年07月13日
 */
public interface ChanelTeamRecommendManager {
	/**
	 * 新增
	 * 
	 * @param ChanelTeamRecommendDO
	 * @return 对象ID
	 */
	public Long insert(ChanelTeamRecommendDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param ChanelTeamRecommendDO
	 * @return 更新成功的记录数
	 */
	public int update(ChanelTeamRecommendDO baseDO) throws MyException;

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
	 * @return ChanelTeamRecommendDO
	 */
	public ChanelTeamRecommendDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<ChanelTeamRecommendDO>
	 */
	public List<ChanelTeamRecommendDO> getByIds(List<Long> ids) throws MyException;

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelTeamRecommendDO>
	 */
	public List<ChanelTeamRecommendDO> query(ChanelTeamRecommendQuery query) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return ChanelTeamRecommendDO
	 */
	public ChanelTeamRecommendDO queryFirst(ChanelTeamRecommendQuery query) throws MyException;
	
	//获取推荐频道
	public List<ChanelTeamRecommendDO> getrecommend(ChanelTeamRecommendQuery query) throws MyException;

}
