package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.Actor1DO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelActorDO;
import com.wasu.ptyw.galaxy.dal.dataobject.QueryActorAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.QueryActorDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelActorQuery;

/**
 * @author wenguang
 * @date 2016年08月24日
 */
public interface ChanelActorManager {
	/**
	 * 新增
	 * 
	 * @param ChanelActorDO
	 * @return 对象ID
	 */
	public Long insert(ChanelActorDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param ChanelActorDO
	 * @return 更新成功的记录数
	 */
	public int update(ChanelActorDO baseDO) throws MyException;

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
	 * @return ChanelActorDO
	 */
	public ChanelActorDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<ChanelActorDO>
	 */
	public List<ChanelActorDO> getByIds(List<Long> ids) throws MyException;
	
	/**
	 * 根据多个actorid查询
	 * 
	 * @param ids
	 * @return List<ChanelActorDO>
	 */
	public List<ChanelActorDO> getByActorIds(List<Integer> ids) throws MyException;

	/**
	 * 在线查询
	 * 
	 * @param query
	 * @return List<ChanelActorDO>
	 */
	public List<ChanelActorDO> queryOnline(ChanelActorQuery query) throws MyException;
	
	/**
	 * 离线查询
	 * 
	 * @param query
	 * @return List<ChanelActorDO>
	 */
	public List<ChanelActorDO> queryOffline(ChanelActorQuery query) throws MyException;
	
	/**
	 * 离线查询
	 * 
	 * @param query
	 * @return List<ChanelActorDO>
	 */
	public List<ChanelActorDO> queryOffline1(ChanelActorQuery query) throws MyException;
	
	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelActorDO>
	 */
	public List<ChanelActorDO> query(ChanelActorQuery query) throws MyException;
	
	/**
	 * 获取所有艺人推荐
	 * 
	 * @param query
	 * @return List<ChanelActorDO>
	 */
	public List<ChanelActorDO> getRecommend(ChanelActorQuery query) throws MyException;
	
	public List<ChanelActorDO> queryRecommend(ChanelActorQuery query) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return ChanelActorDO
	 */
	public ChanelActorDO queryFirst(ChanelActorQuery query) throws MyException;

	/**
	 * 根据多个id更新状态
	 * 
	 * @param ids
	 * @return 更新成功的记录数
	 */
	public int updateStatusByIds(List<Long> ids, int status) throws MyException;

	
	public QueryActorAllDO getActor(String keywords) throws MyException;
	
	public Actor1DO getActor1(String keywords) throws MyException;
	
	public Actor1DO getActor2(String keywords) throws MyException;
	
	public QueryActorAllDO getActors(String keywords) throws MyException;

	public int publishById(long id)throws MyException;
	
	
}
