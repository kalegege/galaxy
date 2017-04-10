package com.wasu.ptyw.galaxy.dal.dao;

import java.util.List;
import java.util.Map;

import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemTeamDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemTeamQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
public interface ChanelItemTeamDAO extends BaseDAO<ChanelItemTeamDO> {
	List<ChanelItemTeamDO> getByTeamId(ChanelItemTeamQuery query)throws DAOException;
	List<ChanelItemTeamDO> getAllByTeamId(ChanelItemTeamQuery query)throws DAOException;
	int queryCountByTeamId(ChanelItemTeamQuery query)throws DAOException;
	int deleteByIds(ChanelItemTeamDO baseDO) throws DAOException;
	int updateOnly(ChanelItemTeamDO baseDO) throws DAOException;
	int queryCount(SimpleQuery query) throws DAOException;
	int queryCountTeam(SimpleQuery query) throws DAOException;
	long queryOrderId(SimpleQuery query) throws DAOException;
	int updateOrderIdAdd(ChanelItemTeamQuery query) throws DAOException;
	int updateOrderIdDecrease(ChanelItemTeamQuery query) throws DAOException;
	int queryCountNull(SimpleQuery query) throws DAOException;
	List<ChanelItemTeamDO> queryItemTeam(ChanelItemTeamQuery query) throws DAOException;
	List<ChanelItemTeamDO> queryTeamOrder(ChanelItemTeamQuery query) throws DAOException;
	List<ChanelItemTeamDO> queryTeam(ChanelItemTeamQuery query) throws DAOException;
	
	List<ChanelItemTeamDO> queryNull(ChanelItemTeamQuery query) throws DAOException;
	List<ChanelItemTeamDO> queryNulls(Map<String,Object> chId) throws DAOException;
	
	List<ChanelItemTeamDO> queryByTeamId(ChanelItemTeamQuery query) throws DAOException;
	List<ChanelItemTeamDO> queryAllByTeamId(ChanelItemTeamQuery query) throws DAOException;
	List<ChanelItemTeamDO> queryAllByTeamIds(List<Long> ids) throws DAOException;
}