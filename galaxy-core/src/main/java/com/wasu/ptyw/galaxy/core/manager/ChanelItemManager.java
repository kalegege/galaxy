package com.wasu.ptyw.galaxy.core.manager;

import java.util.List;

import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemDO;
import com.wasu.ptyw.galaxy.dal.dataobject.LiveRealDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
public interface ChanelItemManager {
	/**
	 * 新增
	 * 
	 * @param ChanelItemDO
	 * @return 对象ID
	 */
	public Long insert(ChanelItemDO baseDO) throws MyException;

	/**
	 * 更新
	 * 
	 * @param ChanelItemDO
	 * @return 更新成功的记录数
	 */
	public int update(ChanelItemDO baseDO) throws MyException;

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
	 * @return ChanelItemDO
	 */
	public ChanelItemDO getById(long id) throws MyException;

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<ChanelItemDO>
	 */
	public List<ChanelItemDO> getByIds(List<Long> ids) throws MyException;
	

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<ChanelItemDO>
	 */
	public List<ChanelItemDO> queryByIds(List<Long> ids,ChanelItemQuery query) throws MyException;
	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelItemDO>
	 */
	public List<ChanelItemDO> query(ChanelItemQuery query) throws MyException;
	
	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelItemDO>
	 */
	public List<ChanelItemDO> queryOrder(List<Long> itemIds) throws MyException;
	
	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelItemDO>
	 */
	public ChanelItemDO queryRePlay(ChanelItemQuery query) throws MyException;
	/**
	 * 分页查询
	 * 
	 * @param queryByRegionId
	 * @return List<ChanelItemDO>
	 */
	public List<ChanelItemDO> queryByRegionId(ChanelItemQuery query) throws MyException;
	
	public List<ChanelItemDO> queryByRegionId(String regionId) throws MyException;
	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelItemDO>
	 */
	public ChanelItemDO queryByChid(ChanelItemQuery query) throws MyException;
	
	
	public ChanelItemDO queryCacheByChid(String chid) throws MyException;

	/**
	 * 查询单个
	 * 
	 * @param query
	 * @return ChanelItemDO
	 */
	public ChanelItemDO queryFirst(ChanelItemQuery query) throws MyException;

	
	/**
	 * 分页查询
	 * 
	 * @param queryadd
	 * @return List<ChanelItemDO>
	 */
	public List<ChanelItemDO> queryadd(List<Long> itemIds,ChanelItemQuery query) throws MyException;
	
	/**
	 * 分页查询
	 * 
	 * @param queryadd
	 * @return List<ChanelItemDO>
	 */
	public List<ChanelItemDO> queryrecommend(ChanelItemQuery query) throws MyException;
	
	/**
	 * 分页查询
	 * 
	 * @param queryAll
	 * @return List<ChanelItemDO>
	 */
	public List<ChanelItemDO> queryAll(List<Long> itemIds,ChanelItemQuery query) throws MyException;
	
	/**
	 * 查询全部
	 * 
	 * @param query
	 * @return
	 * @throws MyException
	 */
	public int queryaddCount(List<Long> itemIds,ChanelItemQuery query) throws MyException;

	/**
	 * 查询直播排名
	 * 
	 * @param queryadd
	 * @return List<ChanelItemDO>
	 */
	public LiveRealDO getLive(int num,ChanelItemQuery query) throws MyException;
	
	/**
	 * 根据频道号查询频道详细信息
	 * 
	 * @param getBychId
	 * @return List<ChanelItemDO>
	 */
	public List<ChanelItemDO> getBychId(List<String> bId) throws MyException;
	
	/**
	 * 单一更新
	 * 
	 * @param bId
	 * @return
	 * @throws DAOException
	 */
	public int updateOnly(ChanelItemDO baseDO) throws MyException;
	
	/**
	 * 单一更新
	 * 
	 * @param bId
	 * @return
	 * @throws DAOException
	 */
	public List<ChanelItemDO> querydelete(List<Long> itemIds,ChanelItemQuery query) throws MyException;
	
	//初始化方法
	public int init(ChanelItemQuery query) throws MyException;
}
