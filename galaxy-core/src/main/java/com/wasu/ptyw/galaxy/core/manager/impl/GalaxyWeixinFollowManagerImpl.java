package com.wasu.ptyw.galaxy.core.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.core.manager.GalaxyWeixinFollowManager;
import com.wasu.ptyw.galaxy.dal.dao.GalaxyWeixinFollowDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyWeixinFollowDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.query.GalaxyWeixinFollowQuery;

/**
 * @author wenguang
 * @date 2015年09月15日
 */
@Service("galaxyWeixinFollowManager")
public class GalaxyWeixinFollowManagerImpl implements GalaxyWeixinFollowManager {
	@Resource
	private GalaxyWeixinFollowDAO galaxyWeixinFollowDao;

	@Override
	public Long insert(GalaxyWeixinFollowDO obj) throws MyException {
		try {
			galaxyWeixinFollowDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int update(GalaxyWeixinFollowDO obj) throws MyException {
		try {
			return galaxyWeixinFollowDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int deleteById(long id) throws MyException {
		try {
			return galaxyWeixinFollowDao.deleteById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public GalaxyWeixinFollowDO getById(long id) throws MyException {
		try {
			return galaxyWeixinFollowDao.getById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public List<GalaxyWeixinFollowDO> getByIds(List<Long> ids) throws MyException {
		try {
			return galaxyWeixinFollowDao.getByIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public int queryCount(GalaxyWeixinFollowQuery query) throws MyException {
		try {
			int count = galaxyWeixinFollowDao.queryCount(query);
			return count;
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public List<GalaxyWeixinFollowDO> query(GalaxyWeixinFollowQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = galaxyWeixinFollowDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return galaxyWeixinFollowDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public GalaxyWeixinFollowDO queryFirst(GalaxyWeixinFollowQuery query) throws MyException {
		try {
			query.setCurrentPage(1);
			query.setPageSize(1);
			List<GalaxyWeixinFollowDO> list = galaxyWeixinFollowDao.query(query);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
		return null;
	}

	@Override
	public int updateStatusByIds(List<Long> ids, int status) throws MyException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", status);
			map.put("ids", ids);
			return galaxyWeixinFollowDao.updateStatusByIds(map);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids, status);
		}
	}

	@Override
	public int updateStatusByQuery(GalaxyWeixinFollowQuery query, int status) throws MyException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", status);
			map.put("query", query);
			return galaxyWeixinFollowDao.updateStatusByQuery(map);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query, status);
		}
	}

	@Override
	public int updateUsedStatusByIds(List<Long> ids, int status) throws MyException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", status);
			map.put("ids", ids);
			return galaxyWeixinFollowDao.updateUsedStatusByIds(map);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids, status);
		}
	}

}
