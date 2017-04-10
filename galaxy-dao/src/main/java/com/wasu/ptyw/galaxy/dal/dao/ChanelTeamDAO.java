package com.wasu.ptyw.galaxy.dal.dao;

import java.util.List;

import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
public interface ChanelTeamDAO extends BaseDAO<ChanelTeamDO> {
	List<ChanelTeamDO> queryByRegion(ChanelTeamQuery query) throws DAOException;
	int queryCount(SimpleQuery query) throws DAOException;
	
	int updateOnly(ChanelTeamDO baseDO) throws DAOException;
	ChanelTeamDO queryByBid(SimpleQuery query) throws DAOException;
	long querySortBy(SimpleQuery query) throws DAOException;
	int updateSortByAdd(int sortby) throws DAOException;
	int updateSortByDecrease(int sortby) throws DAOException;
	
	int queryCountTeam(SimpleQuery query) throws DAOException;
	
	int queryCountType(SimpleQuery query) throws DAOException;
	
	int queryCountNull(SimpleQuery query) throws DAOException;
	
	List<ChanelTeamDO> queryTeam(ChanelTeamQuery query) throws DAOException;
	
	List<ChanelTeamDO> queryType(ChanelTeamQuery query) throws DAOException;
	
	
	List<ChanelTeamDO> queryNull(ChanelTeamQuery query) throws DAOException;
	
	List<ChanelTeamDO> queryTeamOrder(ChanelTeamQuery query) throws DAOException;
}