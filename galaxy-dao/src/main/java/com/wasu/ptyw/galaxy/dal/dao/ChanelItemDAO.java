package com.wasu.ptyw.galaxy.dal.dao;

import java.util.List;
import java.util.Map;

import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
public interface ChanelItemDAO extends BaseDAO<ChanelItemDO> {
	int queryaddCount(Map<String,Object> chId) throws DAOException;
	List<ChanelItemDO> queryadd(Map<String,Object> chId) throws DAOException;
	List<ChanelItemDO> getBychId(List<String> bId) throws DAOException;
	int updateOnly(ChanelItemDO baseDO) throws DAOException;
	ChanelItemDO queryByChid(SimpleQuery query) throws DAOException;
	ChanelItemDO queryByChid1(String chId) throws DAOException;
	List<ChanelItemDO> queryAll(Map<String,Object> chId) throws DAOException;
	int queryAllCount(Map<String,Object> chId) throws DAOException;
	List<ChanelItemDO> querydelete(Map<String,Object> chId) throws DAOException;
	int querydeleteCount(Map<String,Object> chId) throws DAOException;
	List<ChanelItemDO> queryByRegionId(SimpleQuery query) throws DAOException;
	List<ChanelItemDO> queryByRegionId1(String regionId) throws DAOException;
	int queryCount(SimpleQuery query) throws DAOException;
	int queryrecommendCount(SimpleQuery query) throws DAOException;
	List<ChanelItemDO> queryrecommend(SimpleQuery query) throws DAOException;
	int initCount(SimpleQuery query) throws DAOException;
	int init(SimpleQuery query) throws DAOException;
	ChanelItemDO queryRePlay(SimpleQuery query) throws DAOException;
	List<ChanelItemDO> queryByIds(Map<String,Object> chId) throws DAOException;
	List<ChanelItemDO> queryOrder(Map<String,Object> chId) throws DAOException;
}