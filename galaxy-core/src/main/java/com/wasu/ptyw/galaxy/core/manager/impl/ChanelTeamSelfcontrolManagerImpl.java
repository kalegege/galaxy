package com.wasu.ptyw.galaxy.core.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.core.manager.ChanelTeamSelfcontrolManager;
import com.wasu.ptyw.galaxy.dal.dao.ChanelTeamSelfcontrolDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamSelfcontrolDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamSelfcontrolQuery;

/**
 * @author wenguang
 * @date 2016年07月21日
 */
@Service("chanelTeamSelfcontrolManager")
public class ChanelTeamSelfcontrolManagerImpl implements ChanelTeamSelfcontrolManager {
	@Resource
	private ChanelTeamSelfcontrolDAO chanelTeamSelfcontrolDao;

	@Override
	public Long insert(ChanelTeamSelfcontrolDO obj) throws MyException {
		try {
			chanelTeamSelfcontrolDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int update(ChanelTeamSelfcontrolDO obj) throws MyException {
		try {
			return chanelTeamSelfcontrolDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int deleteById(long id) throws MyException {
		try {
			return chanelTeamSelfcontrolDao.deleteById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public ChanelTeamSelfcontrolDO getById(long id) throws MyException {
		try {
			return chanelTeamSelfcontrolDao.getById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public List<ChanelTeamSelfcontrolDO> getByIds(List<Long> ids) throws MyException {
		try {
			return chanelTeamSelfcontrolDao.getByIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public List<ChanelTeamSelfcontrolDO> query(ChanelTeamSelfcontrolQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelTeamSelfcontrolDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return chanelTeamSelfcontrolDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}
	
	@Override
	public List<ChanelTeamSelfcontrolDO> getrecommend(ChanelTeamSelfcontrolQuery query) throws MyException {
		try {
			return chanelTeamSelfcontrolDao.getrecommend(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public ChanelTeamSelfcontrolDO queryFirst(ChanelTeamSelfcontrolQuery query) throws MyException {
		try {
			query.setCurrentPage(1);
			query.setPageSize(1);
			List<ChanelTeamSelfcontrolDO> list = chanelTeamSelfcontrolDao.query(query);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
		return null;
	}

}
