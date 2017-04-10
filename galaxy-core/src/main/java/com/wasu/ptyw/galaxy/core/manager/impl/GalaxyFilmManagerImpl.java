package com.wasu.ptyw.galaxy.core.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.core.manager.GalaxyFilmManager;
import com.wasu.ptyw.galaxy.dal.dao.GalaxyFilmDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.query.GalaxyFilmQuery;

/**
 * @author wenguang
 * @date 2015年09月22日
 */
@Service("galaxyFilmManager")
public class GalaxyFilmManagerImpl implements GalaxyFilmManager {
	@Resource
	private GalaxyFilmDAO galaxyFilmDao;

	@Override
	public Long insert(GalaxyFilmDO obj) throws MyException {
		try {
			galaxyFilmDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int update(GalaxyFilmDO obj) throws MyException {
		try {
			return galaxyFilmDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int deleteById(long id) throws MyException {
		try {
			return galaxyFilmDao.deleteById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public GalaxyFilmDO getById(long id) throws MyException {
		try {
			return galaxyFilmDao.getById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public List<GalaxyFilmDO> getByIds(List<Long> ids) throws MyException {
		try {
			return galaxyFilmDao.getByIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public List<GalaxyFilmDO> query(GalaxyFilmQuery query) throws MyException {
		try {
			if (query.isQueryCount()) {
				int count = galaxyFilmDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return galaxyFilmDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public GalaxyFilmDO queryFirst(GalaxyFilmQuery query) throws MyException {
		try {
			query.setCurrentPage(1);
			query.setPageSize(1);
			List<GalaxyFilmDO> list = galaxyFilmDao.query(query);
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
			return galaxyFilmDao.updateStatusByIds(map);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids, status);
		}
	}

}
