package com.wasu.ptyw.galaxy.core.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.core.manager.GalaxyUserManager;
import com.wasu.ptyw.galaxy.dal.dao.GalaxyUserDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyUserDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.query.GalaxyUserQuery;

/**
 * @author wenguang
 * @date 2015年06月24日
 */
@Service("galaxyUserManager")
public class GalaxyUserManagerImpl implements GalaxyUserManager {
	@Resource
	private GalaxyUserDAO galaxyUserDao;

	@Override
	public Long insert(GalaxyUserDO obj) throws MyException {
		try {
			galaxyUserDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int update(GalaxyUserDO obj) throws MyException {
		try {
			return galaxyUserDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int deleteById(long id) throws MyException {
		try {
			return galaxyUserDao.deleteById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public GalaxyUserDO getById(long id) throws MyException {
		try {
			return galaxyUserDao.getById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public List<GalaxyUserDO> getByIds(List<Long> ids) throws MyException {
		try {
			return galaxyUserDao.getByIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public List<GalaxyUserDO> query(GalaxyUserQuery query) throws MyException {
		try {
			int count = galaxyUserDao.queryCount(query);
			if (count == 0) {
				return Lists.newArrayList();
			}
			query.setTotalItem(count);
			return galaxyUserDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public GalaxyUserDO queryFirst(GalaxyUserQuery query) throws MyException {
		try {
			query.setCurrentPage(1);
			query.setPageSize(1);
			List<GalaxyUserDO> list = galaxyUserDao.query(query);
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
			return galaxyUserDao.updateStatusByIds(map);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids, status);
		}
	}

}
