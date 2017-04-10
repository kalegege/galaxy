package com.wasu.ptyw.galaxy.dal.dao;

import java.util.List;

import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamSelfcontrolDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2016年07月21日
 */
public interface ChanelTeamSelfcontrolDAO extends BaseDAO<ChanelTeamSelfcontrolDO> {
	List<ChanelTeamSelfcontrolDO> getrecommend(SimpleQuery query) throws DAOException;
}