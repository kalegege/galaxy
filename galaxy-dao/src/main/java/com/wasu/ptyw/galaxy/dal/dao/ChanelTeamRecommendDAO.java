package com.wasu.ptyw.galaxy.dal.dao;

import java.util.List;

import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamRecommendDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2016年07月13日
 */
public interface ChanelTeamRecommendDAO extends BaseDAO<ChanelTeamRecommendDO> {
	List<ChanelTeamRecommendDO> getrecommend(SimpleQuery query) throws DAOException;
}