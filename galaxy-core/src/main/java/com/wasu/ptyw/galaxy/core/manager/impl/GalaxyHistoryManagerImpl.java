package com.wasu.ptyw.galaxy.core.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.core.manager.GalaxyHistoryManager;
import com.wasu.ptyw.galaxy.dal.dao.GalaxyHistoryDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyHistoryDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.query.GalaxyHistoryQuery;

/**
 * @author wenguang
 * @date 2015年07月01日
 */
@Service("galaxyHistoryManager")
public class GalaxyHistoryManagerImpl implements GalaxyHistoryManager {
	@Resource
	private GalaxyHistoryDAO galaxyHistoryDao;

	@Override
	public Long insert(GalaxyHistoryDO obj) throws MyException {
		try {
			galaxyHistoryDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int update(GalaxyHistoryDO obj) throws MyException {
		try {
			return galaxyHistoryDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int deleteById(long id) throws MyException {
		try {
			return galaxyHistoryDao.deleteById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public GalaxyHistoryDO getById(long id) throws MyException {
		try {
			return galaxyHistoryDao.getById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public List<GalaxyHistoryDO> getByIds(List<Long> ids) throws MyException {
		try {
			return galaxyHistoryDao.getByIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public List<GalaxyHistoryDO> query(GalaxyHistoryQuery query) throws MyException {
		try {
			int count = galaxyHistoryDao.queryCount(query);
			if (count == 0) {
				return Lists.newArrayList();
			}
			query.setTotalItem(count);
			return galaxyHistoryDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public GalaxyHistoryDO queryFirst(GalaxyHistoryQuery query) throws MyException {
		try {
			query.setCurrentPage(1);
			query.setPageSize(1);
			List<GalaxyHistoryDO> list = galaxyHistoryDao.query(query);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
		return null;
	}

	@Override
	public int deleteByQuery(GalaxyHistoryQuery query) throws MyException {
		try {
			return galaxyHistoryDao.deleteByQuery(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

}
