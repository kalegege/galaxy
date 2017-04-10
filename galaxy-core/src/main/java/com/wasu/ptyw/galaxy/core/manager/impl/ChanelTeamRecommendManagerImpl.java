package com.wasu.ptyw.galaxy.core.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.core.manager.ChanelTeamRecommendManager;
import com.wasu.ptyw.galaxy.dal.dao.ChanelTeamRecommendDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamRecommendDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamRecommendQuery;

/**
 * @author wenguang
 * @date 2016年07月13日
 */
@Service("chanelTeamRecommendManager")
public class ChanelTeamRecommendManagerImpl implements ChanelTeamRecommendManager {
	@Resource
	private ChanelTeamRecommendDAO chanelTeamRecommendDao;

	@Override
	public Long insert(ChanelTeamRecommendDO obj) throws MyException {
		try {
			chanelTeamRecommendDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int update(ChanelTeamRecommendDO obj) throws MyException {
		try {
			return chanelTeamRecommendDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int deleteById(long id) throws MyException {
		try {
			return chanelTeamRecommendDao.deleteById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public ChanelTeamRecommendDO getById(long id) throws MyException {
		try {
			return chanelTeamRecommendDao.getById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public List<ChanelTeamRecommendDO> getByIds(List<Long> ids) throws MyException {
		try {
			return chanelTeamRecommendDao.getByIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public List<ChanelTeamRecommendDO> query(ChanelTeamRecommendQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelTeamRecommendDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return chanelTeamRecommendDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public ChanelTeamRecommendDO queryFirst(ChanelTeamRecommendQuery query) throws MyException {
		try {
			query.setCurrentPage(1);
			query.setPageSize(1);
			List<ChanelTeamRecommendDO> list = chanelTeamRecommendDao.query(query);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
		return null;
	}

	@Override
	public List<ChanelTeamRecommendDO> getrecommend(ChanelTeamRecommendQuery query) throws MyException {
		try {
			return chanelTeamRecommendDao.getrecommend(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}
}
